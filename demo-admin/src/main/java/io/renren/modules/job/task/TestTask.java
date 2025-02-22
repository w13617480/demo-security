package io.renren.modules.job.task;

import java.text.ParseException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.2.0 2016-11-28
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysUserService sysUserService;
	
	
	
	public void test(String params) throws ParseException{
		logger.info("开始计算设备到期时间");
		
		
//		
//		 } catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		 
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SysUserEntity user = sysUserService.selectById(1L);
		System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	
	
	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
	//转换函数，可以封装成公用方法
	public  int longTimeToDay(Long ms){
	       Integer ss = 1000;  
	       Integer mi = ss * 60;  
	       Integer hh = mi * 60;  
	       Integer dd = hh * 24;  

	       int day = (int) (ms / dd);  
	       Long hour = (ms - day * dd) / hh;  
	       Long minute = (ms - day * dd - hour * hh) / mi;  
	       Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
	       Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;  

	       
	      
	       return day;  
	   }
}
