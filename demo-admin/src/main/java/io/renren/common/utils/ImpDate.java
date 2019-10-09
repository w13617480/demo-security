package io.renren.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.multipart.MultipartFile;


import io.renren.common.utils.ZhuangHaoUtil;

import jxl.*;
import jxl.read.biff.BiffException;

public class ImpDate {
	HSSFWorkbook wb;
	Sheet sheet;
	Workbook book;

	public ImpDate write(HttpServletResponse response, String fileName) throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		// response.setHeader("Content-Disposition", "attachment; filename=" +
		// Encodes.urlEncode(fileName));
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("gbk"), "ISO-8859-1"));
		write(response.getOutputStream());
		return this;
	}

	public ImpDate write(OutputStream os) throws IOException {
		wb.write(os);
		os.close();
		return this;
	}

	public void PortLuJi() {
		this.wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("自救器个体信息统计表");
		sheet.setColumnWidth(0, 25 * 256);
		sheet.setColumnWidth(1, 5 * 256);
		sheet.setColumnWidth(2, 5 * 256);
		sheet.setColumnWidth(3, 5 * 256);
		sheet.setColumnWidth(4, 5 * 256);
		sheet.setColumnWidth(5, 5 * 256);
		sheet.setColumnWidth(6, 5 * 256);
		sheet.setColumnWidth(7, 5 * 256);
		sheet.setColumnWidth(8, 5 * 256);
		sheet.setColumnWidth(9, 5 * 256);
		sheet.setColumnWidth(10, 5 * 256);
		sheet.setColumnWidth(11, 5 * 256);
		sheet.setColumnWidth(12, 5 * 256);
		sheet.setColumnWidth(13, 5 * 256);
		sheet.setColumnWidth(14, 5 * 256);
		sheet.setColumnWidth(15, 5 * 256);
		sheet.setColumnWidth(16, 5 * 256);
		sheet.setColumnWidth(17, 5 * 256);
		sheet.setColumnWidth(18, 5 * 256);
		sheet.setColumnWidth(19, 5 * 256);
		sheet.setColumnWidth(20, 5 * 256);
		sheet.setColumnWidth(21, 5 * 256);
		sheet.setColumnWidth(22, 5 * 256);
		sheet.setColumnWidth(23, 5 * 256);

		HSSFFont font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 10);// 设置字体大小

		HSSFFont font2 = wb.createFont();
		font2.setFontName("仿宋_GB2312");
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		font2.setFontHeightInPoints((short) 14);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setFillForegroundColor((short) 43);// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style2.setFillForegroundColor((short) 41);// 设置背景色
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style3.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style3.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style3.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style3.setFillForegroundColor((short) 47);// 设置背景色
		style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style4.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style4.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style4.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style4.setFillForegroundColor((short) 47);// 设置背景色
		style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFCellStyle style5 = wb.createCellStyle();
		style5.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style5.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style5.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style5.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style5.setFillForegroundColor((short) 38);// 设置背景色
		style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		CreationHelper creationHelper = wb.getCreationHelper();
		style5.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

		// 第一行布局
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		// style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style1.setFillForegroundColor((short) 40);// 设置背景色
		style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		HSSFRow firstHeaderRow = sheet.createRow((int) 0 );// 第一行
		sheet.addMergedRegion(new CellRangeAddress(0 , 1 , 0, 15));
		HSSFCell yearCell = firstHeaderRow.createCell(0);
		yearCell.setCellValue("自救器个体信息统计表");
		style1.setFont(font2);

		yearCell.setCellStyle(style1);
		HSSFRow row = sheet.createRow((int) 2 );
		sheet.addMergedRegion(new CellRangeAddress(2 , 2 , 1, 4));
		sheet.addMergedRegion(new CellRangeAddress(2 , 2 , 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(2 , 2 , 7, 9));
		sheet.addMergedRegion(new CellRangeAddress(2 , 2 , 10, 11));
		sheet.addMergedRegion(new CellRangeAddress(2 , 2 , 12, 15));

		// sheet.addMergedRegion(new CellRangeAddress(2+c,2+c,12,13));
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("序号");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 1);
		cell.setCellValue("型号");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 2);
		cell.setCellValue("生产日期");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 3);
		cell.setCellValue("产品编号");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 4);
		cell.setCellValue("使用编码");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 5);
		cell.setCellValue("检查检验日期");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 6);
		cell.setCellValue("完好情况");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 7);
		cell.setCellValue("使用周期");
		cell.setCellStyle(style5);
		cell = row.createCell((short) 8);
		cell.setCellValue("是否到期");
		cell.setCellStyle(style5);
		cell = row.createCell((short) 9);
		cell.setCellValue("现存地点");
		cell.setCellStyle(style5);
		cell = row.createCell((short) 10);
		cell.setCellValue("检查人员");
		cell.setCellStyle(style3);
		cell = row.createCell((short) 11);
		cell.setCellStyle(style3);
		cell = row.createCell((short) 12);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellStyle(style);
		cell = row.createCell((short) 15);
		cell.setCellStyle(style);

	}

	public void show(HSSFRow row, HSSFCell cell, HSSFCellStyle style, int z, int y, Map map) {

		String name = "";

		for (int x = 5; x < 15; x++) {

			if (y == 0) {
				if (x == 5) {
					name = "yi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "er";
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "san";
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "si";
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "wu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "liu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "qi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "ba";
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "jiu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			} else {

				if (x == 5) {
					name = "yi" + z;
					if (map.get(name) != null) {

						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "er" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "san" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "si" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "wu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "liu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "qi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "ba" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "jiu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			}

		}

	}

	public void showLiQing(HSSFRow row, HSSFCell cell, HSSFCellStyle style, int z, int y, Map map, int g) {

		String name = "";

		for (int x = 4; x < g; x++) {

			if (y == 0) {
				if (x == 4) {
					name = "yi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 4);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 4);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 5) {
					name = "er";
					if (map.get(name) != null) {
						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "san";
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "si";
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "wu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "liu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "qi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "ba";
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "jiu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "shi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shiyi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 15) {
					name = "shier";
					if (map.get(name) != null) {
						cell = row.createCell((short) 15);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 15);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 16) {
					name = "shisan";
					if (map.get(name) != null) {
						cell = row.createCell((short) 16);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 16);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 17) {
					name = "shisi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 17);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 17);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 18) {
					name = "shiwu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 18);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 18);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			} else {

				if (x == 4) {
					name = "yi" + z;
					if (map.get(name) != null) {

						cell = row.createCell((short) 4);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 4);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 5) {
					name = "er" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "san" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "si" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "wu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "liu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "qi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "ba" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "jiu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "shi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shiyi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 15) {
					name = "shier" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 15);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 15);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 16) {
					name = "shisan" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 16);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 16);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 17) {
					name = "shisi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 17);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 17);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 18) {
					name = "shiwu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 18);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 18);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			}

		}

	}







	





	

	


	





	private void erShi(HSSFSheet sheet2, int size, List ershi, HSSFFont font, HSSFCellStyle style, int c,
			Map<String, String> map) {

		for (int x = 0; x < ershi.size(); x++) {
			HSSFRow row13 = sheet2.createRow((int) 10 + c + size + x);
			sheet2.addMergedRegion(new CellRangeAddress(10 + c + size + x, 10 + c + size + x, 0, 2));
			HSSFCell cell13 = row13.createCell((short) 0);
			String ddzh = "ddzh";
			String ddyi = "ddyi";
			String dder = "dder";
			String ddsan = "ddsan";
			style.setFont(font);
			cell13.setCellValue(map.get(ddzh + (x + 1)));
			cell13.setCellStyle(style);
			cell13 = row13.createCell((short) 1);
			cell13.setCellStyle(style);
			cell13 = row13.createCell((short) 2);
			cell13.setCellStyle(style);
			cell13 = row13.createCell((short) 3);
			cell13.setCellValue(map.get(ddyi + (x + 1)));
			cell13.setCellStyle(style);
			cell13 = row13.createCell((short) 4);
			cell13.setCellValue(map.get(dder + (x + 1)));
			cell13.setCellStyle(style);

		}

	}

	private void baiMi(HSSFSheet sheet2, List baimi, HSSFFont font, HSSFCellStyle style, int c,
			Map<String, String> map) {

		for (int x = 0; x < baimi.size(); x++) {
			HSSFRow row9 = sheet2.createRow((int) 9 + c + x);
			sheet2.addMergedRegion(new CellRangeAddress(9 + c + x, 9 + c + x, 0, 2));
			HSSFCell cell9 = row9.createCell((short) 0);
			style.setFont(font);
			String bmzh = "bmzh";
			String bmyi = "bmyi";
			String bmer = "bmer";
			String bmsan = "bmsan";
			cell9.setCellValue(map.get(bmzh + (x + 1)));
			cell9.setCellStyle(style);
			cell9 = row9.createCell((short) 1);
			cell9.setCellStyle(style);
			cell9 = row9.createCell((short) 2);
			cell9.setCellStyle(style);

			cell9 = row9.createCell((short) 3);
			cell9.setCellValue(map.get(bmyi + (x + 1)));
			cell9.setCellStyle(style);
			cell9 = row9.createCell((short) 4);
			cell9.setCellValue(map.get(bmer + (x + 1)));
			cell9.setCellStyle(style);
			cell9 = row9.createCell((short) 5);
			cell9.setCellValue(map.get(bmsan + (x + 1)));
			cell9.setCellStyle(style);
		}

	}

	

	



	

	public String strtoDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
		String str1 = format1.format(date);
		return str1;
	}

	public String dateString(String str) {
		if ("".equals(str)) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String str1 = format1.format(date);
		return str1;
	}

	/**
	 * 根据长度自动实现需要调查病害百米个数
	 * 
	 * @param row
	 * @param ge
	 * @param cell
	 * @param i
	 * @param style3
	 */
	public void baiMiShu(HSSFRow row, int ge, HSSFCell cell, int i, HSSFCellStyle style3) {
		int a = 0;
		if (i == 5) {
			a = 4;
		} else if (i == 4) {
			a = 3;
		}
		for (int x = i; x <= ge; x++) {
			int c = x - a;
			if (x < ge) {
				cell = row.createCell((short) x);
				cell.setCellValue(c + "");
				cell.setCellStyle(style3);
			} else {
				cell = row.createCell((short) x);
				cell.setCellValue("累计损坏");
				cell.setCellStyle(style3);
			}

		}
	}

	/**
	 * 根据长度自动填写病害记录默认为0
	 * 
	 * @param row
	 * @param cell
	 * @param style
	 * @param style3
	 */
	public void tianXie(int g, int i, HSSFRow row, HSSFCell cell, HSSFCellStyle style, HSSFCellStyle style3) {
		for (int x = i; x <= g; x++) {
			if (x < g) {
				cell = row.createCell((short) x);
				cell.setCellValue("0");
				cell.setCellStyle(style);
			}
			if (x == g) {
				cell = row.createCell((short) x);
				cell.setCellValue("0");
				cell.setCellStyle(style3);
			}

		}
	}

	public void tianXie(HSSFRow row, HSSFCell cell, HSSFCellStyle style, int z, int y, Map map, int g) {

		String name = "";

		for (int x = 5; x < g; x++) {

			if (y == 0) {
				if (x == 5) {
					name = "yi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "er";
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "san";
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "si";
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "wu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "liu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "qi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "ba";
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "jiu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 15) {
					name = "shiyi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 15);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 15);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 16) {
					name = "shier";
					if (map.get(name) != null) {
						cell = row.createCell((short) 16);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 16);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 17) {
					name = "shisan";
					if (map.get(name) != null) {
						cell = row.createCell((short) 17);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 17);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 18) {
					name = "shisi";
					if (map.get(name) != null) {
						cell = row.createCell((short) 18);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 18);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 19) {
					name = "shiwu";
					if (map.get(name) != null) {
						cell = row.createCell((short) 19);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 19);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			} else {

				if (x == 5) {
					name = "yi" + z;
					if (map.get(name) != null) {

						cell = row.createCell((short) 5);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 5);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 6) {
					name = "er" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 6);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 6);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 7) {
					name = "san" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 7);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 7);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 8) {
					name = "si" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 8);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 8);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 9) {
					name = "wu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 9);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 9);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 10) {
					name = "liu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 10);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 10);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 11) {
					name = "qi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 11);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 11);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 12) {
					name = "ba" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 12);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 12);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 13) {
					name = "jiu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 13);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 13);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 14) {
					name = "shi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 14);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 14);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 15) {
					name = "shiyi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 15);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 15);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 16) {
					name = "shier" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 16);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 16);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 17) {
					name = "shisan" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 17);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 17);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 18) {
					name = "shisi" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 18);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 18);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				} else if (x == 19) {
					name = "shiwu" + z;
					if (map.get(name) != null) {
						cell = row.createCell((short) 19);
						cell.setCellValue(map.get(name).toString());

						cell.setCellStyle(style);
					} else {
						cell = row.createCell((short) 19);
						cell.setCellValue("0");

						cell.setCellStyle(style);
					}

				}

			}

		}

	}

	/**
	 * 评定导出
	 */


	public void pdx(HSSFRow row6, HSSFCellStyle style, Map<String, String> map) {
		HSSFCell cell2 = row6.createCell((short) 0);
		cell2.setCellValue(map.get("ldbm"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 1);
		cell2.setCellValue(map.get("qdzh") + "~" + map.get("zdzh"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 2);
		cell2.setCellValue(map.get("pdcd"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 3);
		cell2.setCellValue(map.get("mqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 4);
		cell2.setCellValue(map.get("pqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 5);
		cell2.setCellValue(map.get("pci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 6);
		cell2.setCellValue(map.get("rqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 7);
		cell2.setCellValue(map.get("rdi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 8);
		cell2.setCellValue(map.get("sri"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 9);
		cell2.setCellValue(map.get("pssi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 10);

		cell2.setCellValue(map.get("sci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 11);
		cell2.setCellValue(map.get("bci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 12);
		cell2.setCellValue(map.get("tci"));
		cell2.setCellStyle(style);
	}



	private void lishipdx(HSSFRow row6, HSSFCellStyle style, Map<String, String> map) {
		HSSFCell cell2 = row6.createCell((short) 0);
		cell2.setCellValue(map.get("ldbm"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 1);
		cell2.setCellValue(map.get("qdzh") + "~" + map.get("zdzh"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 2);
		cell2.setCellValue(map.get("pdcd"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 3);
		cell2.setCellValue(map.get("mqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 4);
		cell2.setCellValue(map.get("pqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 5);
		cell2.setCellValue(map.get("pci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 6);
		cell2.setCellValue(map.get("rqi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 7);
		cell2.setCellValue(map.get("rdi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 8);
		cell2.setCellValue(map.get("sri"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 9);
		cell2.setCellValue(map.get("pssi"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 10);

		cell2.setCellValue(map.get("sci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 11);
		cell2.setCellValue(map.get("bci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 12);
		cell2.setCellValue(map.get("tci"));
		cell2.setCellStyle(style);
		cell2 = row6.createCell((short) 13);
		cell2.setCellValue(map.get("pdsj"));
		cell2.setCellStyle(style);

	}



	

	private void showjtl(HSSFRow row, Map<String, String> map, HSSFCellStyle style, HSSFCellStyle style3,
			HSSFCellStyle style8) {
		HSSFCell cell = row.createCell((short) 0);

		cell.setCellValue(map.get("lxbh"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 1);
		cell.setCellValue(map.get("lxmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 2);
		cell.setCellValue(map.get("qjbm"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 3);
		cell.setCellValue(map.get("qjqd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 4);
		cell.setCellValue(map.get("qjzhd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 5);
		cell.setCellValue(map.get("qjzd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 6);
		cell.setCellValue(map.get("lmlxmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 7);
		cell.setCellValue(map.get("cdlxmcs"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 8);
		cell.setCellValue(map.get("xxqc"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 9);
		cell.setCellValue(map.get("zxqc"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 10);
		cell.setCellValue(map.get("dxke"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 11);
		cell.setCellValue(map.get("zhxqc"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 12);
		cell.setCellValue(map.get("txqc"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 13);
		cell.setCellValue(map.get("zxkcbl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 14);
		cell.setCellValue(map.get("dxkcbl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 15);
		cell.setCellValue(map.get("aadt"));
		cell.setCellStyle(style8);
		cell = row.createCell((short) 16);
		cell.setCellValue(map.get("xxqc1"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 17);
		cell.setCellValue(map.get("zxqc1"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 18);
		cell.setCellValue(map.get("dxke1"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 19);
		cell.setCellValue(map.get("zhxqc1"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 20);
		cell.setCellValue(map.get("txqc1"));

		cell.setCellStyle(style);
		cell = row.createCell((short) 21);
		cell.setCellValue(map.get("zxkcbl1"));

		cell.setCellStyle(style);
		cell = row.createCell((short) 22);
		cell.setCellValue(map.get("dxkcbl1"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 23);
		cell.setCellValue(map.get("aadt1"));
		cell.setCellStyle(style8);
		cell = row.createCell((short) 24);
		cell.setCellValue(map.get("jtzzl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 25);
		cell.setCellValue(map.get("sxrjlzc"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 26);
		cell.setCellValue(map.get("xsjtl"));
		cell.setCellStyle(style8);
		cell = row.createCell((short) 27);
		cell.setCellValue(map.get("cdxs"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 28);
		cell.setCellValue(map.get("cdlxmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 29);
		cell.setCellValue(map.get("cddlzc"));
		cell.setCellStyle(style8);
		cell = row.createCell((short) 30);
		cell.setCellValue(map.get("sxxfb"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 31);
		cell.setCellValue(map.get("dzczl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 32);
		cell.setCellValue(map.get("hcmzl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 33);
		cell.setCellValue(map.get("hckzl"));
		cell.setCellStyle(style);
		cell = row.createCell((short) 34);
		cell.setCellValue(map.get("hcczl"));
		cell.setCellStyle(style);
	}

	



	private void showqj(HSSFRow row, Map<String, String> map, HSSFCellStyle style3, HSSFCellStyle style20,
			HSSFCellStyle style21) {
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(map.get("lxbh"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 1);
		cell.setCellValue(map.get("lxmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 2);
		cell.setCellValue(map.get("qdzh"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 3);
		cell.setCellValue(map.get("zdzh"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 4);
		cell.setCellValue(map.get("qjbm"));
		cell.setCellStyle(style20);
		cell = row.createCell((short) 5);
		cell.setCellValue(map.get("qjqd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 6);
		cell.setCellValue(map.get("qjzhd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 7);
		cell.setCellValue(map.get("jsdj"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 8);
		cell.setCellValue(map.get("yxlmkd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 9);
		cell.setCellValue(map.get("zqqy"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 10);
		cell.setCellValue(map.get("gydwName"));
		cell.setCellStyle(style20);
		cell = row.createCell((short) 11);
		cell.setCellValue(map.get("lmlx"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 12);
		cell.setCellValue(map.get("cdlx"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 13);
		cell.setCellValue(map.get("ldglfs"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 14);
		cell.setCellValue(map.get("qdmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 15);
		cell.setCellValue(map.get("zdmc"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 16);
		cell.setCellValue(map.get("xcdkd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 17);
		cell.setCellValue(map.get("ccdkd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 18);
		cell.setCellValue(map.get("ljkd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 19);
		cell.setCellValue(map.get("fgdkd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 20);
		cell.setCellValue(map.get("dlydkd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 21);
		cell.setCellValue(map.get("kgsj"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 22);
		cell.setCellValue(map.get("jgsj"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 23);
		cell.setCellValue(map.get("xjnd"));
		cell.setCellStyle(style3);
		cell = row.createCell((short) 24);
		cell.setCellValue(map.get("ljkdd"));
		cell.setCellStyle(style3);
	}



	

	private void showlu(HSSFRow row, Map<String, String> map, HSSFCellStyle style17, HSSFCellStyle style18,
			HSSFCellStyle style19, HSSFCellStyle style20, HSSFCellStyle style21, HSSFCellStyle style22) {

		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(map.get("ldbm"));
		cell.setCellStyle(style20);
		cell = row.createCell((short) 1);

		cell.setCellValue(map.get("lmjgbm"));
		cell.setCellStyle(style20);
		cell = row.createCell((short) 2);

		cell.setCellValue(map.get("lmjgmc"));
		cell.setCellStyle(style20);
		cell = row.createCell((short) 3);

		cell.setCellValue(map.get("mcjgmc"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 4);
		cell.setCellValue(map.get("mchd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 5);
		cell.setCellValue(map.get("mcdj"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 6);
		cell.setCellValue(map.get("smcmc"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 7);

		cell.setCellValue(map.get("smchd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 8);
		cell.setCellValue(map.get("smcdj"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 9);
		cell.setCellValue(map.get("zmcmc"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 10);

		cell.setCellValue(map.get("zmchd"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 11);

		cell.setCellValue(map.get("zmcdj"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 12);

		cell.setCellValue(map.get("xmcmc"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 13);

		cell.setCellValue(map.get("xmchd"));

		cell.setCellStyle(style21);
		cell = row.createCell((short) 14);
		cell.setCellValue(map.get("xmcdj"));
		cell.setCellStyle(style21);
		cell = row.createCell((short) 15);
		cell.setCellValue(map.get("jcjgmc"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 16);
		cell.setCellValue(map.get("jchd"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 17);
		cell.setCellValue(map.get("jcdj"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 18);
		cell.setCellValue(map.get("jsmcmc"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 19);
		cell.setCellValue(map.get("jsmchd"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 20);
		cell.setCellValue(map.get("jsmcdj"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 21);
		cell.setCellValue(map.get("jzmcmc"));

		cell.setCellStyle(style22);
		cell = row.createCell((short) 22);
		cell.setCellValue(map.get("jzmchd"));

		cell.setCellStyle(style22);
		cell = row.createCell((short) 23);
		cell.setCellValue(map.get("jzmcdj"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 24);
		cell.setCellValue(map.get("jxmcmc"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 25);
		cell.setCellValue(map.get("jxmchd"));
		cell.setCellStyle(style22);
		cell = row.createCell((short) 26);
		cell.setCellValue(map.get("jxmcdj"));
		cell.setCellStyle(style22);
	}

	

}
