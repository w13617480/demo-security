package io.renren.modules.sys.controller;

import io.renren.common.utils.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
		String str = "2007-12-02";
		
		boolean ret = DateUtils.isDate(str);
		
		System.out.println( ret);
		
		 
	}

}
