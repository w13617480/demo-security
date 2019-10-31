package io.renren.modules.sys.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
  
       
import com.alibaba.fastjson.JSON;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;   
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import io.renren.common.utils.R;
import io.renren.modules.sys.entity.*;
import oracle.net.aso.e;

import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import java.util.zip.ZipEntry;

/**
 * pdf导出
 * @author liming
 *
 */
@Controller
@RequestMapping("/sys/export")
public class PdfExportController implements Serializable{
	/**
	 * 序列化标识
	 */
	private static final long serialVersionUID = -6625103162170547725L;
	/**
	 * 文件ID（测试用）
	 */
	private static String fileId="123456789";
	/**
	 * 合同文件保存并下载
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadPdf", method = RequestMethod.GET)
	public R downloadPdf(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 指定解析器
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
				"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		//时间戳
		String timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String filename = "装修合同"+timestamp;
		response.setContentType("application/pdf");
		try {
			response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename+".pdf", "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return R.error();
		}
		OutputStream os = null;
		PdfStamper ps = null;
		PdfReader reader = null;
		try {
			os = response.getOutputStream(); // 生成的新文件路径 ，这里指页面
			/**
			 * System.getProperty("user.dir") 返回项目的根路径
			 *  /template/ 代表项目根目录下有一个名为template的文件夹下，文件夹下存放着我们的模板文件
			 */
			// 1 读取根目录下的pdf文件 
			String templatePath=System.getProperty("user.dir")+"/hetong/" + fileId+".pdf";
			reader = new PdfReader(templatePath);
			// 2 根据表单生成一个新的pdf
			ps = new PdfStamper(reader, os);
			System.out.println("===============PDF下载成功=============");
		} catch (Exception e) {
			System.out.println("===============PDF下载失败=============");
			e.printStackTrace();
			return R.error();
		}finally {
			//关闭资源
			try {
				ps.close();
				reader.close();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
				return R.error();
			}
		}
		return R.ok();
	}
	/**
	 * 合同文件保存并下载
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/savePdf", method = RequestMethod.GET)
	public R savePdf(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 模板路径
		String templatePath=System.getProperty("user.dir")+"/template/" + "test.pdf";
		// 服务器备份路径
		String sourcePath=System.getProperty("user.dir")+"/hetong/";
		// 合同参数
		HeTong heTong=this.formatEntity(new HashMap<String, Object>());
		// 文件名
//		String filename= "装修合同"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String filename=fileId;
		boolean flag=this.saveHeTong(heTong,filename,sourcePath,templatePath);
		if (flag) {
			System.out.println("===============PDF保存成功=============");
			return R.ok();
		}else {
			System.err.println("===============PDF保存失败=============");
			return R.error();
		}
	}

	/**
	 * 保存文件备份，并把文件名字入库
	 * @param heTong
	 * @param filename
	 * @param path
	 */
	public boolean  saveHeTong(HeTong heTong,String filename,String sourcePath,String templatePath) {
		boolean flag=false;
		File file = new File(sourcePath+filename+".pdf");
		try {
			file.createNewFile();
			PdfReader reader = new PdfReader(templatePath);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PdfStamper ps = new PdfStamper(reader, bos);
			/*使用中文字体 */
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
			fontList.add(bf);	
			AcroFields s = ps.getAcroFields();
			s.setSubstitutionFonts(fontList);  
			s.setField("jf",heTong.getJf());
			s.setField("jfs",heTong.getJfs());
			s.setField("yf",heTong.getYf());
			s.setField("yfs",heTong.getYfs());   
			s.setField("fs",heTong.getFs());
			ps.setFormFlattening(true);
			ps.close();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bos.toByteArray());
			fos.close();
			//文件名入数据库  
			this.saveDataBase(filename);
			flag = true;
			
			
			List<String> x = new ArrayList<String>();
			x.add(sourcePath+filename+".pdf");
			x.add(sourcePath+"1.pdf");
			try{
				reZipCsvFiles(sourcePath+filename+".zip", x);
			} catch ( Exception iex) {
				iex.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("=================保存文件失败IOException"+e.getMessage()+"==================");
			e.printStackTrace();
		} catch (DocumentException e) {
			System.err.println("=================保存文件失败DocumentException"+e.getMessage()+"==================");
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 保存进数据库
	 * @param filename
	 */
	public void saveDataBase(String fileName){
		Map<String, Object> saveMap=new HashMap<>();
		saveMap.put("fileName", fileName);
		//saveMap.put("user_id", );
	}
	/**
	 * @param 组装参数
	 * @throws Exception 
	 */
	public HeTong formatEntity(Map<String, Object> map) {
		HeTong heTong = new HeTong();

		//生产环境使用map中的值替换写死的固定值
		heTong.setJf("山西金能集团");
		heTong.setJfs("11234519992939239239");
		heTong.setYf("山西银能集团");
		heTong.setYfs("14232619992939239239");
		heTong.setFs("(1)");

		return heTong;
	}
	
	
	private void reZipCsvFiles(String targetZipRealPath, List<String> targetFilePathList) throws Exception {
        File targetZipFile = new File(targetZipRealPath);
        InputStream in = null;
        FileOutputStream fos = null;
        ZipOutputStream zipOutputStream = null;
        try {
            fos = new FileOutputStream(targetZipFile);
            zipOutputStream = new ZipOutputStream(fos);
            for (String csvFilePath: targetFilePathList) {
                in = new FileInputStream(csvFilePath);
                String csvFileName = csvFilePath.substring(csvFilePath.lastIndexOf(File.separator) + 1);
                zipOutputStream.putNextEntry(new ZipEntry(csvFileName));
                IOUtils.copy(in, zipOutputStream);
                zipOutputStream.closeEntry();
				in.close();
			}
        } finally {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}