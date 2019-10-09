package io.renren.modules.sys.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.validator.group.ValidatorUtils;
import io.renren.modules.sys.entity.DaUserEntity;
import io.renren.modules.sys.entity.SysDeptEntity;
import io.renren.modules.sys.entity.SysDictEntity;
import io.renren.modules.sys.service.DaUserService;
import io.renren.modules.sys.service.SysDeptService;
import io.renren.modules.sys.service.SysDictService;
import io.renren.modules.sys.shiro.ShiroUtils;



/**
 * 人员档案
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-07-19 08:51:45
 */
@RestController
@RequestMapping("sys/dauser")
public class DaUserController {
    @Autowired
    private DaUserService daUserService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysDictService sysDictService;
	private Map<String,String> maps =new HashMap<String,String>();

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dauser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = daUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:dauser:info")
    public R info(@PathVariable("id") Integer id){
        DaUserEntity daUser = daUserService.selectById(id);
        return R.ok().put("daUser", daUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:dauser:save")
    public R save(@RequestBody DaUserEntity daUser){
    
    	if (daUser.getWorknumber() != null && ! daUser.getWorknumber().equals("") ) 	{
    		
	    	if(daUserService.getDaUserEntitysByWorknumber("", daUser.getWorknumber())!=null){
		    	return R.error(1, "警号不能重复，请修改！");
		    }
    	}
    	
    	 
    	
	    if(ValidatorUtils.validateEntity(daUser)==null){
	    	try{
	    		daUser.setCreator(ShiroUtils.getUserId());
	    		daUserService.insert(daUser);
	    		return R.ok();
	    	} catch (Exception e) {
			   return R.error(1,e.getMessage());
	    	}
	    }else{
	    	return ValidatorUtils.validateEntity(daUser);
	    }
        
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:dauser:update")
    public R update(@RequestBody DaUserEntity daUser){
    	
    	if (daUser.getWorknumber() != null && ! daUser.getWorknumber().equals("") ) 	{
    		
	    	if(daUserService.getDaUserEntitysByWorknumber(daUser.getId().toString(), daUser.getWorknumber())!=null){
		    	return R.error(1, "警号不能重复，请修改！");
		    }
    	}
    	
    	 
    
    	
    	if(ValidatorUtils.validateEntity(daUser)==null){
	         try{
	        	 daUserService.updateAllColumnById(daUser);//全部更新
	        	 return R.ok();
	        	 
	         } catch (Exception e) {
	        	 return R.error(1,e.getMessage());
	         }
        }else{
        	return ValidatorUtils.validateEntity(daUser);
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dauser:delete")
    public R delete(@RequestBody Integer[] ids){
        daUserService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }
    
    @RequestMapping("/deletebyids")
    @RequiresPermissions("sys:dauser:deletebyids")
    public R deletebyids(@RequestBody Integer[] ids){
    
        daUserService.deletebyids(ids);

        return R.ok();
    }
    /**
	 * 导出用户导入模板
	 */
	@RequestMapping("/impUser")
	@RequiresPermissions("sys:dauser:impUser")
	public void impUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = getPath();

		File f = new File(path + "templates/modules/sys/report/impUser.xlsx");
		OutputStream out = response.getOutputStream();
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;

		response.reset(); // 非常重要
		response.setContentType("application/x-msdownload");
		String fileName = "员工信息导入模板.xlsx";
		// 采用中文文件名需要在此处转码
		fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
		br.close();
		out.close();

	}	
	/**
	 * 获取导入实时信息
	 */
	@RequestMapping("/importExcelMessige")
	@RequiresPermissions("sys:dauser:importExcelMessige")
	public R importExcelMessige() {
		System.out.println(maps);
		return R.ok().put("dicts", maps);
	}
	
	/**
	* 导入用户
	*/
	@RequestMapping("/importExcel")
	@RequiresPermissions("sys:dauser:importExcel")
	public R importExcel(@RequestParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException {
			List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
			List<SysDictEntity> dicts = null;
			DaUserEntity user =new DaUserEntity();
		    try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			int shs = sheet.getLastRowNum();
			
			System.out.println("==============rows:" + shs);
			
			if ( shs == 0 ) {
				return R.error( "您导入的文件没数据！");
			}
			
			XSSFRow row = null;
			XSSFCell cell = null;
			String cellValue = "";
			int sesson=1;//0成功1失败
			int count=0;
			List list =new ArrayList();
			for(int a = 1; a <= shs; a++){
				row = sheet.getRow(a);
				cell = row.getCell(1);
				cell.setCellType(cell.CELL_TYPE_STRING);
				String e1 = cell.getStringCellValue();
				
				cell = row.getCell(2);
				cell.setCellType(cell.CELL_TYPE_STRING);
				String e2 = cell.getStringCellValue();
				
				cell = row.getCell(3);
				cell.setCellType(cell.CELL_TYPE_STRING);
				String e3 = cell.getStringCellValue();
				
				cell = row.getCell(4);
				cell.setCellType(cell.CELL_TYPE_STRING);
				String e4 = cell.getStringCellValue();
				
				cell = row.getCell(5);
				
				cell.setCellType(cell.CELL_TYPE_STRING);
				String e5 = cell.getStringCellValue();
				
				cell = row.getCell(6);
				String e6="";
				if(cell!=null){
					cell.setCellType(cell.CELL_TYPE_STRING);
					e6 = cell.getStringCellValue();	
				}
				cell = row.getCell(7);
				String e7="";
				if(cell!=null){
					cell.setCellType(cell.CELL_TYPE_STRING);
				    e7 = cell.getStringCellValue();
				}
				
				
				cell = row.getCell(8);
				String e8 = cell.getStringCellValue();
				if(!e1.equals("")||!e2.equals("")||!e3.equals("")||!e4.equals("")
				||!e5.equals("")||!e6.equals("")||!e7.equals("")||!e8.equals("")){
					count+=1;
					list.add(e4);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				for (int j = i + 1; j < list.size(); j++) {
					if (list.get(i).equals(list.get(j))) {
						 return R.error(1,"本次导入文件中存在相同工号，该工号为："+list.get(i)+"请做修改后重新导入");
					}
				}
			}
			maps.put("count", count+"");
			for (int a = 1; a <= count; a++) {
				int i=0;//0 表示新增，1表示修改
				row = sheet.getRow(a);
				cell = row.getCell(1);
				cell.setCellType(cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue();
				System.out.println("第" + a + "行" + "第1列的值" + cellValue);
				
				long id=getDeptId(deptList,cellValue);
				System.out.println(cellValue.equals(""));
				System.out.println(cellValue.equals(""));
				if(cellValue.equals("")){
					 return R.error(1,"第" + a + "行" + "第1列的值" + cellValue+"，填写错误，部门必须填写！");
				}
				if(id== -1){
					return R.error(1,"第" + a + "行" + "第1列的值" + cellValue+"，填写错误，不存在该名称部门，请修改！");
				}
				user.setDeptId(id);
				cell = row.getCell(2);
				cell.setCellType(cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue();
				System.out.println("第" + a + "行" + "第2列的值" + cellValue);
				if(cellValue.equals("")){
					   return R.error(1,"第" + a + "行" + "第2列的值" + cellValue+"，填写错误，姓名必须填写！");
					}
				user.setName(cellValue);
				cell = row.getCell(3);
				cellValue = cell.getStringCellValue();
				System.out.println("第" + a + "行" + "第3列的值" + cellValue);
				
				if(cellValue.equals("")){
					   return R.error(1,"第" + a + "行" + "第3列的值" + cellValue+"，填写错误，请选择性别！");
					}
				dicts = sysDictService.showtype("sex");
				String disid=getDisId(dicts, cellValue);
				if(disid.equals("-1")){
					   return R.error(1,"第" + a + "行" + "第3列的值" + cellValue+"，填写错误，性别只能填写：男或者女！");
					}
				user.setSex(disid+"");
				
				cell = row.getCell(4);
				cell.setCellType(cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue();
				
				System.out.println("第" + a + "行" + "第4列的值" + cellValue);
				if(cellValue.equals("")){
					   return R.error(1,"第" + a + "行" + "第4列的值" + cellValue+"，填写错误，工号必须填写！");
					}
				if(cellValue.length()>5){
					   return R.error(1,"第" + a + "行" + "第4列的值" + cellValue+"，填写错误，工号长度最大为5位数！");
					}
				if(!isInteger(cellValue)){
					return R.error(1,"第" + a + "行" + "第4列的值" + cellValue+"，填写错误，工号填写错误，请修改！");
				}
				String gh=cellValue;
				if(getUderGh(cellValue).size()>0){
					i=1;
				}else{
					i=0;
				}
				user.setWorknumber(cellValue);
				
				cell = row.getCell(5);
				cell.setCellType(cell.CELL_TYPE_STRING);
				cellValue = cell.getStringCellValue();
				System.out.println("第" + a + "行" + "第5列的值" + cellValue);
				if(cellValue.equals("")){
					   return R.error(1,"第" + a + "行" + "第5列的值" + cellValue+"，填写错误，身份证号必须填写！");
					}
				if(cellValue.length()>18){
					   return R.error(1,"第" + a + "行" + "第5列的值" + cellValue+"，填写错误,身份证号长度超限，请修改！");
					}
				if(!isInteger(cellValue)){
					return R.error(1,"第" + a + "行" + "第5列的值" + cellValue+"，填写错误，身份证号填写错误，请修改！");
				}
				user.setIdnumber(cellValue);
				
				
				
				cell = row.getCell(7);
				if ( cell != null ) {
					cell.setCellType(cell.CELL_TYPE_STRING);
					cellValue = cell.getStringCellValue();
				} else {
					cellValue = "";
				}
				
				System.out.println("第" + a + "行" + "第7列的值" + cellValue);
				if(cellValue.length()>11){
					return R.error(1,"第" + a + "行" + "第7列的值" + cellValue+"，填写错误，联系方式填写错误，请修改！");
				}
				if(!isInteger(cellValue)){
					return R.error(1,"第" + a + "行" + "第7列的值" + cellValue+"，填写错误，联系方式填写错误，请修改！");
				}
				if(cellValue!=""){
					user.setMobile(cellValue);
				}
				cell = row.getCell(8);
				cellValue = cell.getStringCellValue();
				System.out.println("第" + a + "行" + "第8列的值" + cellValue);
				if(cellValue.equals("")){
					return R.error(1,"第" + a + "行" + "第8列的值" + cellValue+"，填写错误，请选择是否在职！");
					}
				dicts = sysDictService.showtype("zaizhi");
				String disids=getDisId(dicts, cellValue);
			
				if(disids.equals("-1")){
					   return R.error(1,"第" + a + "行" + "第8列的值" + cellValue+"，填写错误，是否在职只能填写：在职或离职！");
				}
				
				user.setStatus(disids+"");
				
				
				if(i==0){
					 
		        	    user.setCreator(ShiroUtils.getUserId());
			    		daUserService.insert(user);
			    		sesson=0;
			    		maps.put("gs", a+"");
				}else{
					 user.setId(getUderGh(gh).get(0).getId());
					 user.setCreator(ShiroUtils.getUserId());
					 daUserService.updateAllColumnById(user);//全部更新
					 maps.put("gs", a+"");
					 sesson=0;
				}
			}

			file.getInputStream().close();
			if(sesson==0){
				Map<String,String> map=new HashMap<String,String>();
				map.put("count", count+"");
				return R.ok().put("dicts", map);
			}else{
				return R.error();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return R.error( "导入异常：" + e.getMessage());
//			HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
//			HSSFSheet sheet = workbook.getSheetAt(0);
//			int shs = sheet.getLastRowNum();
//			HSSFRow row = null;
//			HSSFCell cell = null;
//			int count = shs;
//			int sum;
//			if (count == 0) {
//				sum = count;
//			} else {
//				sum = count;
//			}
//			String cellValue = "";
//			for (int a = 0; a <= sum; a++) {
//
//				   int i=0;//0 表示新增，1表示修改
//				row = sheet.getRow(a);
//				cell = row.getCell(1);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第1列的值" + cellValue);
//				 long id=getDeptId(deptList,cellValue);
//				if(cellValue.equals("")){
//					 return R.error(1,"第" + a + "行" + "第1列的值" + cellValue+"，填写错误，部门必须填写！");
//				  }
//				if(id== -1){
//					return R.error(1,"第" + a + "行" + "第1列的值" + cellValue+"，填写错误，不存在该名称部门，请修改！");
//				}
//				user.setDeptId(id);
//				user.setDeptName(cellValue);
//				cell = row.getCell(2);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第2列的值" + cellValue);
//				if(cellValue.equals("")){
//					   return R.error(1,"第" + a + "行" + "第2列的值" + cellValue+"，填写错误，姓名必须填写！");
//					}
//				user.setName(cellValue);
//				cell = row.getCell(3);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第3列的值" + cellValue);
//				dicts = sysDictService.showtype("sex");
//				long disid=getDisId(dicts, cellValue);
//				if(cellValue.equals("")){
//					   return R.error(1,"第" + a + "行" + "第3列的值" + cellValue+"，填写错误，请选择性别！");
//					}
//				if(disid==-1){
//					   return R.error(1,"第" + a + "行" + "第3列的值" + cellValue+"，填写错误，性别只能填写：男或者女！");
//					}
//				user.setSex(disid+"");
//				cell = row.getCell(4);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第4列的值" + cellValue);
//				if(cellValue.equals("")){
//					   return R.error(1,"第" + a + "行" + "第4列的值" + cellValue+"，填写错误，工号必须填写！");
//					}
//				if(getUderGh(cellValue)!=null){
//					i=1;
//				}else{
//					i=0;
//				}
//				user.setWorknumber(cellValue);
//				cell = row.getCell(5);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第5列的值" + cellValue);
//				if(cellValue.equals("")){
//					   return R.error(1,"第" + a + "行" + "第5列的值" + cellValue+"，填写错误，身份证号必须填写！");
//					}
//				if(cellValue.length()>18){
//					   return R.error(1,"第" + a + "行" + "第5列的值" + cellValue+"，填写错误,身份证号长度超限，请修改！");
//					}
//				user.setIdnumber(cellValue);
//				cell = row.getCell(6);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第6列的值" + cellValue);
//				if(cellValue.length()>22){
//					return R.error(1,"第" + a + "行" + "第6列的值" + cellValue+"，填写错误,用户id卡号长度超限，请修改！");
//				}
//				if(cellValue!=""){
//					user.setUsernumber(cellValue);
//				}
//				cell = row.getCell(7);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第7列的值" + cellValue);
//				if(cellValue.length()>11){
//					return R.error(1,"第" + a + "行" + "第6列的值" + cellValue+"，填写错误,联系方式填写错误，请修改！");
//				}
//				if(cellValue!=""){
//					user.setMobile(cellValue);
//				}
//				cell = row.getCell(8);
//				cellValue = cell.getStringCellValue();
//				System.out.println("第" + a + "行" + "第8列的值" + cellValue);
//				if(cellValue.equals("")){
//					return R.error(1,"第" + a + "行" + "第8列的值" + cellValue+"，填写错误，请选择是否在职！");
//					}
//				dicts = sysDictService.showtype("zaizhi");
//				long disids=getDisId(dicts, cellValue);
//			
//				if(disid==-1){
//					   return R.error(1,"第" + a + "行" + "第8列的值" + cellValue+"，填写错误，是否在职只能填写：在职或离职！");
//					}
//				user.setStatus(disid+"");
//				if(i==0){
//					 
//		        	    user.setCreator(ShiroUtils.getUserId());
//			    		daUserService.insert(user);
//			    		return R.ok();
//				}else{
//					user.setId(getUderGh(cellValue).get(0).getId());
//					 user.setCreator(ShiroUtils.getUserId());
//					 daUserService.updateAllColumnById(user);//全部更新
//		        	 return R.ok();
//				}
//			
//			}
		}	 
	}
	public String getPath(){
		String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
		path = path.replace('/', '\\'); // 将/换成\
		path = path.replace("file:", ""); // 去掉file:
		path = path.substring(1); 
		return path;
	}
   public long getDeptId(List<SysDeptEntity> deptList,String name){
	   long id=-1;
	   for(SysDeptEntity dep :deptList){
		   if(dep.getName().equals(name)){
			   
			   return dep.getDeptId();
		   }
	   }
	   return id;
   }
   public String getDisId(List<SysDictEntity> dicts,String name){
	   String id="-1";
	   for(SysDictEntity dep :dicts){
		   if(dep.getValue().indexOf(name)>-1){
			   
			   return dep.getCode();
		   }
	   }
	   return id;
   }
   /**
    * 校验用户工号
    */
   public List<DaUserEntity> getUderGh(String worknumber){
	   List<DaUserEntity> users=daUserService.getUderGh(worknumber);
	   return users;
   }
   public  boolean isInteger(String str) {  
       Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
       return pattern.matcher(str).matches();  
   }
   
   
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	//delete by wangjh
	//@RequiresPermissions("sys:user:list")	
	public R login(@RequestParam Map<String, Object> params){
		
		String username = params.get("username").toString();
		String password = params.get("password").toString();
		
		DaUserEntity daUser = daUserService.login(username, password);
		
		R r = R.ok().put("daUser", daUser);
		
		if ( daUser != null ){
			SysDeptEntity sysDeptEntity = sysDeptService.getDeptById(daUser.getDeptId().toString());
			r.put("deptname", sysDeptEntity.getName());
		}

		return r;
	}
	
	@RequestMapping(value = "/updPassword", method = RequestMethod.POST)
	public R updPassword(@RequestParam Map<String, Object> params){
		
		String userid = params.get("userid").toString();
		String oldpass = params.get("oldpass").toString();
		String newpass = params.get("newpass").toString();
		
		DaUserEntity daUser = daUserService.getDaUserEntityByIdAndPassword(userid, oldpass);
		
		if ( daUser != null ) {
			try {
				daUserService.updPassword(userid, newpass);
				return R.ok();
			} catch(Exception e){
				return R.error(1, e.getMessage());
			}
		} else {
			return R.error(100, "旧密码输入错误！");
		}
		
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public R resetPassword(@RequestParam Map<String, Object> params){
		
		String userid = params.get("userid").toString();
		String newpass = "e10adc3949ba59abbe56e057f20f883e";
		
		
		try {
			daUserService.updPassword(userid, newpass);
			return R.ok("密码重置成功！");
		} catch(Exception e){
			return R.error(1, "密码重置失败（" + e.getMessage() + "）");
		}
		  
	}

}
