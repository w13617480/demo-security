//全局2d画笔
var globalContext = null;
//页面右上角圆弧的背景色
var arcBgColor = "rgb(54, 100, 139)";
//页面背景色
//var bgColor = "rgb(0, 0, 0)";
//验证标记--是否已经进行验证 true:正在进行验证;false:否
var verifyFlag = false;
//模式:1--1:1,2--1:N
var model = "2";
//绘画双手的起点横坐标
var x = 28;
//绘画双手的起点纵坐标
var y = 346;
//存放画手指函数的数组
var fingerList = [];
//保存当前正在采集的手指标记--删除时，消除当前正在采集的手指颜色时用到
var lastFPIdNum = null;
//绘画手指的边框颜色
var fingerBorderColor = "rgb(71,75,79)";
//页面右上角圆弧的背景色
//var arcBgColor = "rgb(243, 245,240)";
//页面背景色
var bgColor = "rgb(243, 245,240)";
//绘画的图形边框样式--边线绘图
var strokeStyle = "stroke";
//绘画的图形填充样式--填充绘图
var fillStyle = "fill";
//点击手指 的颜色
var fillFingerColor="rgb(71,75,79)";
//判断是否修改了数据(包括新增和删除)
var fpModifyFlag = false;

/**
 * 画椭圆 -- 给context添加绘画椭圆的属性
 * @author wenxin
 * @create 2013-05-15 10:11:21 am
 * @param x, y 椭圆定位的坐标
 * @param width, height 椭圆的宽度和高度
 */
CanvasRenderingContext2D.prototype.oval = function(x, y, width, height) 
{
    var k = (width/0.75)/2,w = width/2,h = height/2;
    this.strokeStyle = bgColor;
	this.beginPath();
	
	this.moveTo(x, y-h);
	this.bezierCurveTo(x+k, y-h, x+k, y+h, x, y+h);
	this.bezierCurveTo(x-k, y+h, x-k, y-h, x, y-h);
	this.closePath();
	this.stroke();
	return this;
}

 /**
 * 查动态库连接回调函数
 * @author wenxin
 * @create 2013-05-15 17:12:21 pm
 * @param ${pers_person_templateCount}:指纹数
 */
function getDLLConnectCallBack(result,isComp)
{
	if(globalContext == null)
	{
		if(isComp==true){
			globalContext = document.getElementById("canvasComp").getContext("2d");
		}else{
			globalContext = document.getElementById("canvas").getContext("2d");
		}
	}
	//返回码
	var ret = null;
	ret = result.ret;
	//接口调用成功返回时
	if(ret == 0)
	{
		//${base_fp_connectFail}:连接指纹采集器失败
		collectTips(globalContext, "未检测到指纹采集器.1", "verification");
	}
	else
	{
		//${base_fp_loadFail}:加载ZKFinger10失败
		collectTips(globalContext, "加载动态库失败.", "verification");
	}
}
/**
 * 调用begincapture接口，开始采集指纹
 * @author wenxin
 * @create 2013-06-24 10:11:21 am
 * @param context 2d画布上下文
 */
function beginCapture(context)
{
	$.ajax( {
		type : "GET",
		url : issOnlineUrl+"/fingerprint/beginCapture?type=2&FakeFunOn=0&random="+getRandomNum(),
		dataType : "json",
		async: true,
		success : function(result) 
		{
			//返回码
			var ret = null;
			ret = result.ret;
			//接口调用成功返回时
			if(ret == 0)
			{
				verifyFlag = true;
				//检查采集、显示图像
				checkColl();
			}
			else if(ret == -2001)
			{
				//${base_fp_connectFail}:连接指纹采集器失败
				//显示框--采集提示
				collectTips(context, "未检测到指纹采集器.2", "verification");
			}
			else if(ret == -2002)
			{
				getWebServerInfo(null, null, "1");
			}
			else if(ret == -2005)
			{
				//取消采集
				cancelCapture();
				//开始采集
				beginCapture(globalContext);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert("请安装指纹驱动或启动该服务!");
			//messageBox({messageType: "alert", title: "${common_prompt_title}", text: "${base_fp_connectPrompt}"});
	    }
	});
}
/**
 * 检查采集--递归调用，如果有采集到指纹，显示图像，获取模板，进行比对
 * @author wenxin
 * @create 2013-06-24 10:11:21 am
 */
function checkColl()
{
	var base64FPImg = "";
	//返回码
	var ret = null;
	$.ajax( {
		type : "GET",
		url : issOnlineUrl+"/fingerprint/getImage?random="+getRandomNum(),
		dataType : "json",
		async: true,
		success : function(result) 
		{debugger;
			//alert(objToStr(data));
			//指纹采集次数
			var collCount = 0;
			ret = result.ret;
			if(ret == 0)
			{
				collCount = result.data.enroll_index;
				base64FPImg = result.data.jpg_base64;
			}
			if(collCount == 0)
			{
				//定时器
				timer = setTimeout("checkColl()", 200);//比对失败重新开始
			}
			else
			{
				//将定时器关闭
				clearTimeout(verifyTimer);
				//显示指纹图像
				showImage(globalContext, base64FPImg, "verification");
				//存放国际化元素数组
				var paramArray = new Array();
				paramArray[0] = '成功登记指纹';//${base_fp_registerSuccess}:成功登记指纹
				paramArray[1] = '请重按手指';//${base_fp_pressFingerAgain}:请重按手指
				paramArray[3] = '请检查网络连接';//${base_fp_connectPrompt}:请检查网络连接
				//获取指纹模板
				getFPTemplate(paramArray, "verification");
				$("#oneToMany").attr("disabled", false);
				$("#oneToOne").attr("disabled", false);
				setTimeout("beginCapture(null)", 200);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert("请安装指纹驱动或启动该服务!");
			//messageBox({messageType: "alert", title: "${common_prompt_title}", text: "${base_fp_connectPrompt}"});
	    }
	});
}
/**
 * 指纹比对
 * @author wenxin
 * @create 2013-06-24 17:41:21 pm
 * @param fpTemplate 指纹模板
 */
function fpComparison(fpTemplate)
{
	if(model == "1")
	{
		if($("#persNumText").val() != "${pers_person_pin}")
		{
			$("#pin").val($("#persNumText").val());
		}
	}
	$("#verifyModel").val(model);
	$("#verifyTemplate").val(fpTemplate);
	//表单提交
	//formSubmit("fpVerifyForm");
	
}
/**
 * 取消采集
 * @author wenxin
 * @create 2013-06-24 19:57:11 pm
 */
function cancelCapture()
{
	//将定时器的递归调用关闭
	clearTimeout(timer);
	//取消采集
	$.ajax( {
		type : "GET",
		url : issOnlineUrl+"/fingerprint/cancelCapture?random="+getRandomNum(),
		dataType : "json",
		async: true,
		success : function(result) 
		{
			verifyFlag = false;
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert("请安装指纹驱动或启动该服务!");
			//messageBox({messageType: "alert", title: "${common_prompt_title}", text: "${base_fp_connectPrompt}"});
	    }
	});
}
/**
 * 确定按钮事件
 * @author wenxin
 * @create 2013-06-26 16:57:11 pm
 */
function beginVerify()
{
	if($("#persNumText").val() == "${pers_person_pin}" && model == "1")
	{
		//${base_fp_enterPin}:请输入人员编号
		//显示框--采集提示
		collectTips(globalContext, "请输入人员编号", "verification");
		return;
	}
	else
	{
		//${base_fp_verifyInfo}:请水平按压手指验证
		//显示框--采集提示
		collectTips(globalContext, "请水平按压手指验证", "verification");
	}
	
	//取消采集--如果当前正在采集
	cancelEvent();
	$("#oneToMany").attr("disabled", true);
	$("#oneToOne").attr("disabled", true);
	//开始采集
	beginCapture(globalContext);
}

/**
 * 清空
 * @author wenxin
 * @create 2013-09-05 16:57:11 pm
 */
function clearImageData()
{
	
	if(isComp){
		//清空指纹图像
		clearFPImage(globalContext, "verification");
		//显示框--采集提示
		collectTips(globalContext, "请水平按压手指验证", "verification");
	}else{
		//清空指纹图像
		clearFPImage(globalContext, "register");
	}
}
/**
 * 表单提交回调函数
 * @author wenxin
 * @create 2013-06-26 16:57:11 pm
 */
function callBackFormSubmit(msg)
{ 
	if(msg.ret == "ok")//成功
	{
		collectTips(globalContext, "验证通过", "verification");
		setTimeout("closeVerify()", 1000);
		setTimeout(_callBackFunction("dashboard.action"), 1000);
	}
	else if(msg.msg == "disabled")
	{
		collectTips(globalContext, "${auth_login_disabled}", "verification");
		setTimeout("closeVerify()", 1000);
		$(".errorTip").html("${auth_login_disabled}");
		$(".errorTip").show();
	}else if(msg.msg == "notExist")
	{
		collectTips(globalContext, "验证失败", "verification");
		setTimeout("clearImageData()", 1000);
		beginCapture(globalContext);
	}
	
	
}
/**
 * 关闭比对页面
 * @author wenxin
 * @create 2013-06-21 14:57:11 pm
 */
function closeVerify()
{
	//正在进行验证，还没有关闭指纹采集
	if(verifyFlag)
	{
		//取消采集
		cancelCapture();
	}
	close();
}
/**
 * 页面加载时，初始化数据
 * @author wenxin
 * @create 2013-07-09 15:18:31 pm
 */
function dataInitComp()
{
	var canvas = document.getElementById("canvasComp");
	var context = canvas.getContext("2d");
	globalContext = context;
	//文本框内提示信息并清空
	//checkText();
	
	//${base_fp_verifyInfo}:请水平按压手指验证
	//显示框--采集提示
	collectTips(context, "请水平按压手指验证", "verification");
	
	//开始采集
	beginCapture(context);
	//定时器
	verifyTimer = setTimeout("closeVerify()", 500000);
}
//初始化数据
//dataInit();
/**
 * 点击确定按钮时，触发事件
 * @author wenxin
 * @create 2013-05-15 17:12:21 pm
 * @param ${pers_person_templateCount}:指纹数
 */
function submitEvent()
{
	storeDataToHtml();
	showFPCount('指纹数:');
	//closeWindow();
	close();
}

 /**
  * 采集指纹
  * @author wenxin
  * @create 2013-05-13 10:18:31 am
  * @param context 2d画布上下文
  */
 function fpCollection(context) 
 {
 	$.ajax( {
 		type : "GET",
 		url : issOnlineUrl+"/fingerprint/beginCapture?type=1&FakeFunOn=0&random="+getRandomNum(),
 		dataType : "json",
 		async: true,
 		success : function(result) 
 		{
 			//返回码
 			var ret = null;
 			ret = result.ret;
 			//接口调用成功返回时
 			if(ret == 0)
 			{
 				//检查采集次数、显示图像
 				checkCollCount();
 			}
 			else if(ret == -2001)
 			{
 				//${base_fp_connectFail}:连接指纹采集器失败
 				//显示框--采集提示
 				collectTips(globalContext, "未检测到指纹采集器.3", "html5");
 			}
 			else if(ret == -2002)
 			{
 				getWebServerInfo(null, null, "1");
 			}
 			else if(ret == -2005)
 			{
 				//取消采集
 				cancelRegister();
 				//切换手指后，渲染手指(消除原来手指的颜色)
 				renderAfterColl(globalContext, fpIdNum, bgColor, false);
 				//${base_fp_pressFinger}:请选择手指
 				//显示框--采集提示
 				collectTips(globalContext, "请选择手指.", "html5");
 			}
 		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) 
 		{
 			alert("请安装指纹驱动或启动该服务!");
 			//messageBox({messageType: "alert", title: "提示", text: "请安装指纹驱动或启动该服务!"});
 	    }
 	});
 }
 /**
  * 检查采集次数
  * @author wenxin
  * @create 2013-05-22 09:24:31 am
  * @param collCount 采集次数
  */
 function checkCollCount()
 {
 	var base64FPImg = "";
 	//返回码
 	var ret = null;
 	$.ajax( {
 		type : "GET",
 		url : issOnlineUrl+"/fingerprint/getImage?random="+getRandomNum(),
 		dataType : "json",
 		async: false,
 		success : function(result) 
 		{
 			//alert(objToStr(data));
 			//指纹采集次数
 			var collCount = 0;
 			ret = result.ret;
 			if(ret == 0)
 			{
 				collCount = result.data.enroll_index;
 				base64FPImg = result.data.jpg_base64;
 			}
 			if(collCount != 3)
 			{
 				//第一次和第二次采集，显示采集次数、指纹图像、进度条
 				if(collCount == 1 || collCount == 2)
 				{
 					//${base_fp_collCount}:按压指纹剩余次数:
 					var text = "按压剩余次数:"+(FINGERPRINT_NUMBER - collCount);
 					//显示框--采集提示
 					collectTips(globalContext, text, "html5");
 					//进度条
 					drawProgressBar(globalContext, collCount);
 					//显示指纹图像
 					showImage(globalContext, base64FPImg, "html5");
 					//清空图像
 					setTimeout("clearImageData()", 200);
 				}
 				//定时器
 				timer = setTimeout("checkCollCount()", 200);
 			}
 			else
 			{
 				//显示指纹图像
 				showImage(globalContext, base64FPImg, "html5");
 				//清空图像
 				setTimeout("clearImageData()", 200);
 				//存放国际化元素数组
 				var paramArray = new Array();
 				paramArray[0] = "成功登记指纹.";//base_fp_registerSuccess:成功登记指纹
 				paramArray[1] = "采集失败，请重新登记.";//base_fp_pressFingerAgain:请重按手指
 				paramArray[2] = "请不要重复录入指纹!";//base_fp_repeatCollection:请不要重复录入指纹!
 				paramArray[3] = "请安装指纹驱动或启动该服务!";//base_fp_connectPrompt:请检查网络连接
 				//进度条
 				drawProgressBar(globalContext, collCount);
 				//获取指纹模板
 				if(!getFPTemplate(paramArray, "register"))
 				{
 					drawProgressBar(globalContext, 0);//进度条灰显
 				}
 				
 				//如果胁迫指纹选中，则取消选中
 				if(duressFingerFlag)
 				{
 					$("#duressFinger").attr("checked", false);
 				}
 				$("#duressFinger").attr("disabled", false);
 				$("#submitButtonId").attr("disabled", false);
 				collectFlag = false;
 				fpIdNum = -1;
 				return collCount;
 			}
 		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) 
 		{
 			alert("请安装指纹驱动或启动该服务!");
 			//messageBox({messageType: "alert", title: "提示", text: "请安装指纹驱动或启动该服务!"});
 	    }
 	});
 	
 }
 
 /**
  * 取消采集，当采集中断时
  * @author wenxin
  * @create 2013-05-27 17:46:31 pm
  */
 function cancelRegister()
 {
 	//当前有手指在采集指纹
 	if(collectFlag)
 	{
 		//将定时器的递归调用关闭
 		clearTimeout(timer);
 		//取消采集
 		$.ajax( {
 			type : "GET",
 			url : issOnlineUrl+"/fingerprint/cancelCapture?random="+getRandomNum(),
 			dataType : "json",
 			async: false,
 			success : function(result) 
 			{
 				//如果胁迫指纹选中，则取消选中
 				if(duressFingerFlag)
 				{
 					$("#duressFinger").attr("checked", false);
 				}
 				if(fpModifyFlag)
 				{
 					$("#submitButtonId").attr("disabled", false);
 				}
 				$("#duressFinger").attr("disabled", false);
 				if(fpIdNum != null)
 				{
 					//消除原来手指的颜色
 					
 					renderAfterColl(globalContext, lastFPIdNum, bgColor, false);
 				}
 				collectFlag = false;
 			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) 
 			{
 				alert("请安装指纹驱动或启动该服务!");
 				//messageBox({messageType: "alert", title: "提示", text: "请安装指纹驱动或启动该服务!"});
 		    }
 		});
 	}
 }
 
 /**
  * 绘画
  * @author wenxin
  * @create 2013-05-13 10:18:31 am
  * @param context 2d画布上下文
  * @param x, y 绘画左手掌的第一个点的坐标，后面的绘画手指和右手掌的坐标都是相对于此点坐标来计算
  * @param color 绘画手指和手掌的边框颜色
  */
 function draw(context, x, y, color) 
 {
 	var coordArray = new Array();
 	//初始化起始坐标,并返回json格式数据 
 	var coordJson = initCoordJson();
 	for(var i=0; i<coordJson.length; i++)
 	{
 		//绘画双手和圆弧
 		drawHandAndArc(context, coordArray, color, coordJson[i].coord.x, coordJson[i].coord.y, coordJson[i].num);
//  		drawHandAndArc(context, coordArray, color, coordJson[i].coord.x, coordJson[i].coord.y, i);
 		coordArray = null;
 		coordArray = new Array();
 	}
 	//存放国际化元素数组
 	var paramArray = new Array();
 	paramArray[0] = "请选择手指.";
 	paramArray[1] = "未检测到指纹采集器.4";
 	paramArray[2] = "加载动态库失败.";
 	paramArray[3] = "请选择手指.";
 	
 	
 	//检查指纹采集器
 	checkFPReader(context, paramArray, "html5");
 	
 	//进度条
 	drawProgressBar(context, 0);
 	//将确定按钮置灰
 	$("#submitButtonId").attr("disabled", true);
 }
 /**
  * 绘画双手和圆弧
  * @author wenxin
  * @create 2013-06-17 10:18:31 am
  * @param context 2d画布上下文
  * @param coordArray 坐标数组
  * @param x, y 绘画左手掌的第一个点的坐标，后面的绘画手指和右手掌的坐标都是相对于此点坐标来计算
  * @param color 绘画手指和手掌的边框颜色
  * @param num 当前会话对象编号
  */
 function drawHandAndArc(context, coordArray, color, x, y, num)
 {
 	//初始化坐标
 	coordArray = initCoordArray(coordArray, x, y, num);
 	var drawObj = null;
 	//绘画手指
 	if(num < 10)
 	{
 		drawObj = "finger"+num;
 		drawObj = new renderFinger(context, coordArray);
 		drawObj.drawFinger(strokeStyle, color);
 		//初始化时，渲染手指
 		renderInit(context, num, "html5");
 		//将绘画的手指实例放入数组，方便重画时用
 		if(fingerList.length < 10)
 		{
 			fingerList.push(drawObj);
 		}
 	}
 	//绘画双手掌心
 	else if(num < 12)
 	{
 		new renderHand(context, coordArray).drawHand(color);
 	}
 	//绘画圆圈
 	showImage(context, "image/base_fpVerify_clearImage.png", "clearForRegister");
 	//绘画圆弧
 	//else if(num == 12)
 	//{
 	//	new FillArc(context, coordArray).drawArc(arcBgColor);
 	//}
 }
 /**
  * 重画
  * @author wenxin
  * @create 2013-05-13 10:18:31 am
  * @param x, y 鼠标点击处的坐标
  */
 function redraw(x, y) 
 {
 	var canvas = document.getElementById("canvas");
 	if (canvas.getContext) 
 	{
 		var context = canvas.getContext("2d");
 		
 		//是否点击在手指区域
 		var isInFingerArea = false;
 		//判断当前点击是否在手指区域
 		for ( var i = 0; i < fingerList.length; i++)
 		{
 			var finger = fingerList[i];
 			finger.drawFinger(strokeStyle, fingerBorderColor);
 			if (context.isPointInPath(x, y))
 			{
 				isInFingerArea = true;
 				break;
 			}
 		}
 		
 		outerloop:
 		for ( var i = 0; i < fingerList.length; i++) 
 		{
 			if(collectFlag)
 			{
 				//当点击的是同一个手指时，如何判断?(编辑指纹时，有问题)
 				if(fpIdNum == i)
 				{
 					//切换手指后，渲染手指(消除原来手指的颜色)
 					renderAfterColl(globalContext, fpIdNum, bgColor, false);
 				}
 			}
 			var finger = fingerList[i];
 			finger.drawFinger(strokeStyle, fingerBorderColor);
 			//currentContext = context;
 			if (context.isPointInPath(x, y)) 
 			{
 				globalContext = context;
 				//两次是否点击的同一个手指进行采集。如果是，则第二次点击时取消采集。
 				var iaSameFinger = false;
 				if(fpIdNum == i && collectFlag)
 				{
 					iaSameFinger = true;
 				}
 				var fingerId;
 				//判断该手指是否已经有指纹
 				var isCollected = false;
 				isCollected = isContains(fingerIdArray, i);
 				fpIdNum = i;
 				if(!isCollected)
 				{
 					//保存当前正在采集的手指标记
 					lastFPIdNum = fpIdNum;
 				}
 				//如果已经有指纹
 				if(isCollected)
 				{
 					//取消采集
 					cancelRegister();
 					var flag=confirm("删除当前选中的指纹吗?");
 					if(flag){
 						delFPData(flag, context, "html5");
 						collectTips(globalContext, "请选择手指.", "html5");
 						//进度条
 						drawProgressBar(globalContext, 0);
 					}
 				
 					break outerloop;
 				}
 				else
 				{
 					//两次点击的同一个手指进行采集，则第二次取消采集。
 					if(iaSameFinger)
 					{
 						//取消采集
 						cancelRegister();
 						//取消采集后重新提示请选择手指
 						collectTips(globalContext, "请选择手指.", "html5");
 						//取消采集后重新绘制进度条
 						drawProgressBar(context, 0);
 						fpIdNum = -1;
 					}
 					else
 					{
 						//取消采集
 						cancelRegister();
 						context.fillStyle = fillFingerColor;
 						context.fill();
 						//globalContext = context;
 						collectFlag = true;//需要判断，当重复点击时，颜色改变
 						$("#duressFinger").attr("disabled", true);
 						$("#submitButtonId").attr("disabled", true);
 						//${base_fp_collCount}:按压指纹剩余次数:
 						var text = "按压剩余次数:"+FINGERPRINT_NUMBER;
 						//进度条
 						drawProgressBar(globalContext, 0);
 						//显示框--采集提示
 						collectTips(globalContext, text, "html5");
 						//指纹采集
 						fpCollection(context);
 					}
 				}
 			} 
 			else 
 			{
 				context.fillStyle = bgColor;
 				context.fill(); 
 				renderInit(context, i, "html5");
 				if(collectFlag)
 				{
 					if(fpIdNum == i && !isInFingerArea)
 					{
 						context.fillStyle = fillFingerColor;
 						context.fill();
 					}
 				}
 			}
 		}
 	}
 }
 
 /**
  * 页面加载时，初始化数据
  * @author wenxin
  * @create 2013-07-09 15:18:31 pm
  */
 function dataInitReg()
 {
 	if(!duressFingerShowFlag)
 	{
 		$("#duressFingerDiv").hide();
 	}
 	var canvas = document.getElementById("canvas");
 	var context = canvas.getContext("2d");
 	
 	fpIdNum = null;
 	//获取页面的指纹数据
 	getDataFromPage();
 	//绘画
 	draw(context, x, y, fingerBorderColor);
 	//jquery在ie下实现cors跨域请求
 	jQuery.support.cors = true;
 	//鼠标事件
 	canvas.onmousedown = function(event){
 		//event.which == 1--鼠标左键
 		if(event.which == 1)
 		{
 			var pageInfo = canvas.getBoundingClientRect();
 			var x = event.clientX - pageInfo.left;
 			var y = event.clientY - pageInfo.top;
 	
 			duressFingerFlag = $("#duressFinger").attr("checked");
			
 			//重画
 			redraw(x, y);
 		}
 	}
 }
 
function myfunction(){
	//加载xml中ISSOnline_server的ip和port
	//loadXml("<%=basePath%>"+"webapp/xml/BaseISSOnlineServer.xml");
	//加载指纹标记和指纹模板数据到页面
	loadFPDataTemplate("[]", "[]");
	var browserFlag = "";
	//存放国际化元素数组
	var paramArray = new Array();
	//获取浏览器类型
	browserFlag = getBrowserType();
	paramArray[0] = '指纹';
	paramArray[1] = '指纹数:';
	paramArray[2] = '确认保存当前修改吗?';
	paramArray[3] = '登记';
	paramArray[4] = '请安装指纹驱动或启动该服务!';
	paramArray[5] = '0';
	paramArray[6] = '指纹数:';
	paramArray[7] = '验证';
	//检查驱动
	checkDriver(paramArray, browserFlag, false);
}
/**
 * 判断是否安装指纹驱动
 * @author wenxin
 * @create 2013-04-22 20:18:31 pm
 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 * @param paramArray 存放国际化元素的数组
 * @param isFPLogin 是否是指纹登录 true:是；false:否
 */
function checkDriver(paramArray, browserFlag, isFPLogin)
{
	var hrefStr = "";
	if(browserFlag == "html5")
	{
		// 发送一个请求，检查是否安装驱动
		getWebServerInfo(paramArray, isFPLogin, "0");
	}
	else if(browserFlag == "simple")
	{
		//发送一个请求，检查是否安装驱动
		getWebServerInfoForSimple(paramArray, isFPLogin, "0");
	}
	else if(browserFlag == "upgradeBrowser")
	{
		if($("#userLoginForm [name='fingerLogin']").val() != undefined)
		{
			$("#userLoginForm [name='fingerLogin']").attr("onclick", "");
			$("#userLoginForm [name='fingerLogin']").attr("title", "当前浏览器不支持改功能,请升级浏览器!");
		}
		if($("#fpRegister").val() != undefined)
		{
			$("#fpRegister").attr("onclick", "");
			$("#fpRegister").attr("title", "当前浏览器不支持改功能,请升级浏览器!");
		}
	}
}
/**
 * 获取webserver的信息
 * @author wenxin
 * @param 
 * @param paramArray 存放国际化元素的数组
 * @param isFPLogin 是否是指纹登录 true:是；false:否
 * @param type 0 表示发送完请求后,还有别的操作。1 表示发送完请求后，没有其余的操作了
 * @create 2013-08-09 17:24:31 pm
 */
function getWebServerInfo(paramArray, isFPLogin, type)
{
	$.ajax( {
		type : "GET",
		url : issOnlineUrl+"/info",
		dataType : "json",
		async: true,
		//timeout:1000,
		success : function(result) 
		{
			//检查驱动
			if(type == "0")
			{
				getWebServerInfoCallBack(result, paramArray, isFPLogin);
			}
			//检查动态库连接
			else if(type == "1")
			{
				getDLLConnectCallBack(result,isComp);
			}
			
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			
	    }
	});
}

/**
 * 获取webserver的信息
 * @author wenxin
 * @param paramArray 存放国际化元素的数组
 * @param isFPLogin 是否是指纹登录 true:是；false:否
 * @param type 0 表示发送完请求后,还有别的操作。1 表示发送完请求后，没有其余的操作了
 * @create 2013-08-09 17:24:31 pm
 */
function getWebServerInfoForSimple(paramArray, isFPLogin, type)
{
	//创建XDomainRequest实例，用于ie8和ie9跨域访问
	var xDomainRequest = new XDomainRequest();
	//如果xDomainRequest存在,则可以使用xDomainRequest函数，否则，说明不是ie浏览器
	if (xDomainRequest) 
	{  
		xDomainRequest.open('GET', issOnlineUrl+"/info?random="+getRandomNum());  
		xDomainRequest.onload = function()
		{
			//检查驱动
			if(type == "0")
			{
				getInfoForSimpleCallBack(xDomainRequest, paramArray, isFPLogin);
			}
			//检查动态库连接
			else if(type == "1")
			{
				getDLLConnectCallBack(xDomainRequest,isComp);
			}
		};  
		xDomainRequest.onerror = function()
		{
			//用完后，将对象置为空
			xDomainRequest = null;
		};
		xDomainRequest.send();  
	} 
}

function closeCompa() 
{
	$("#bg").css("display", "none");
	$("#box").css("display", "none");
	$("#comparisonDiv").css("display", "none");
}

//初始化界面数据，即清除指纹记录。
function cleanData()
{
	var canvas = document.getElementById("canvas");
	var context = canvas.getContext("2d");
	fpIdNum = null;
	getDataFromPage();
	draw(context, x, y, fingerBorderColor);
	jQuery.support.cors = true;
	redraw(x, y);
}

function doVerify()
{
	var regTemplate = "TVFTUjIyAAAEEhAFBQUHCc7QAAAcE2kAAAAAgyEgiBKiAacPlQABAIIdnAHcAH4PagBlEmYN6ACJAA0PLhKrAaoPSADAAZcdEQGWACsONgA8EqwMzQAYAEYPZBJMAIMOhAB3AJodXgGdALIPeAB5EmQPqQBZADAMNxKXAbMPRACKAMIdAABwAM4PYQAbEvkOoQA9Ab0PAhMXAFMOtQCeAfAdlwGIAM0PZABvEucMcgDfAEoPyBJqAUgOngDAAX0dcAEyANkPUAAnE/cOIwBXAH8PAxMHAFMMGQDiAMEdLAmp3xIhWOopGLPgtfpCBkfzxGWAgSogmv2fgkJ1qPtygM/6FgYnAf6ecoBWeeZzdz8K4i9neo/e2VeB9g7jj7PzfPgBCHvu5hdWAxMP/QzLAbaAsJMYEaa8IAtOidYD9w2j/FoCFgk3ia4jhmX6I8cHuYf3hcMQ8AG335Nj830/ERZyASuC6G9qhZJs8OoY8hMfC8filOfG4M5Wk4TO7EYDExab4gbnvZFXfbcHvvW69PsZcAGv37/XMgzfEcLuzLf4/cQJBBIDMVlMFw+e9VYh1l1f/06bOqskIQADRB4qBMUsBEicDQA4AFoBwIJDgiUARABXVXhw7cCVw4DDb0fAf9CGxRQAVACnosXSwkSEwsDCBosDEmABXpNcDsWHBHvCgWd4eAbFngRlx8HAwwUAHwAN7P7ZDAAOAoZ4+9FSbgMAGQWJwwgSYhNTwVyABYgZEswVj6lpwkl6ldSDocPBSQnF1R0dwf/9/f4kwQDJCIGOBAAbLviECxMNL6mZfsUFwcDSwcMRAO83WcPFgJONkMQLAKs4SNLDcMHClhbFp0r7/P3A+fjAm/767PzB/f/9wOMDBNtTgcMJAJmlU6XUyMLDDQELoEkyVMHB+10GAMtrOYHBDQAUazAFc4BuBQG8b3Cn3gDPYEf//vz9/D76+u38//7///w7/vns//v9wP3A3gEPb0j///9HwDhU++3+/ztE/0beAQiBSP/+PlP+Ov/7UsH8///AwDv/xewLARSXKXivhQ4SNp0peIOJzgEPvVIqV8AzDsUMvznDdcFsb8HdARSuXVVTPsDA7v867f9LBwAMyvWZaB4ADsta/0eQ/kkeABDZXP/COcFP0zMHAAveJwbAxNECAZ3fdP3QAQTsZUZM//5bBf/60j/BChAXC9N0emUGEZ8Icf6F/w0DAgtiPkr/yBEGC2ZL/8D+wzr+XRYRWVt9MgrUAiJ1RcH/ZAwQyynz63PD/8PAasIQED0XwMDCagjVDjU7n8GNBRChhXTEKgQR5kNtRcEQ511sRVJCABmGAQYTAccAWl8AxACmEoXUAAAZRZcAQFAAAAAAABbFAAQSAzIAAAAAxQBBUA==";
	var fpTemplate = "TVFTUjIyAAAEEhAFBQUHCc7QAAAcE2kAAAAAgyEgiBKiAacPlQABAIIdnAHcAH4PagBlEmYN6ACJAA0PLhKrAaoPSADAAZcdEQGWACsONgA8EqwMzQAYAEYPZBJMAIMOhAB3AJodXgGdALIPeAB5EmQPqQBZADAMNxKXAbMPRACKAMIdAABwAM4PYQAbEvkOoQA9Ab0PAhMXAFMOtQCeAfAdlwGIAM0PZABvEucMcgDfAEoPyBJqAUgOngDAAX0dcAEyANkPUAAnE/cOIwBXAH8PAxMHAFMMGQDiAMEdLAmp3xIhWOopGLPgtfpCBkfzxGWAgSogmv2fgkJ1qPtygM/6FgYnAf6ecoBWeeZzdz8K4i9neo/e2VeB9g7jj7PzfPgBCHvu5hdWAxMP/QzLAbaAsJMYEaa8IAtOidYD9w2j/FoCFgk3ia4jhmX6I8cHuYf3hcMQ8AG335Nj830/ERZyASuC6G9qhZJs8OoY8hMfC8filOfG4M5Wk4TO7EYDExab4gbnvZFXfbcHvvW69PsZcAGv37/XMgzfEcLuzLf4/cQJBBIDMVlMFw+e9VYh1l1f/06bOqskIQADRB4qBMUsBEicDQA4AFoBwIJDgiUARABXVXhw7cCVw4DDb0fAf9CGxRQAVACnosXSwkSEwsDCBosDEmABXpNcDsWHBHvCgWd4eAbFngRlx8HAwwUAHwAN7P7ZDAAOAoZ4+9FSbgMAGQWJwwgSYhNTwVyABYgZEswVj6lpwkl6ldSDocPBSQnF1R0dwf/9/f4kwQDJCIGOBAAbLviECxMNL6mZfsUFwcDSwcMRAO83WcPFgJONkMQLAKs4SNLDcMHClhbFp0r7/P3A+fjAm/767PzB/f/9wOMDBNtTgcMJAJmlU6XUyMLDDQELoEkyVMHB+10GAMtrOYHBDQAUazAFc4BuBQG8b3Cn3gDPYEf//vz9/D76+u38//7///w7/vns//v9wP3A3gEPb0j///9HwDhU++3+/ztE/0beAQiBSP/+PlP+Ov/7UsH8///AwDv/xewLARSXKXivhQ4SNp0peIOJzgEPvVIqV8AzDsUMvznDdcFsb8HdARSuXVVTPsDA7v867f9LBwAMyvWZaB4ADsta/0eQ/kkeABDZXP/COcFP0zMHAAveJwbAxNECAZ3fdP3QAQTsZUZM//5bBf/60j/BChAXC9N0emUGEZ8Icf6F/w0DAgtiPkr/yBEGC2ZL/8D+wzr+XRYRWVt9MgrUAiJ1RcH/ZAwQyynz63PD/8PAasIQED0XwMDCagjVDjU7n8GNBRChhXTEKgQR5kNtRcEQ511sRVJCABmGAQYTAccAWl8AxACmEoXUAAAZRZcAQFAAAAAAABbFAAQSAzIAAAAAxQBBUA==";
	$.ajax( {
		type : "POST",
		url : "http://127.0.0.1:22001/ZKBIOOnline/fingerprint/verify",
		dataType : "json",
		data:JSON.stringify({'reg':regTemplate,
			'ver':fpTemplate}),
		async: true,
		success : function(data) 
		{
			//返回码
			var ret = null;
 			ret = data.ret;
 			//接口调用成功返回时
 			if(ret == 0)
			{
				alert("score:" + data.score);
			}
			else
			{
				alert("ret:" + data.ret);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert("请安装指纹驱动或启动该服务!");
	    }
	});
}

/**
 * ----------------------------------------------------------common start--------------------------------------------------------------
*/
//采集指纹次数
var FINGERPRINT_NUMBER = 3;
//胁迫指纹数--胁迫指纹时，在普通指纹上加的数
var DURESS_FINGER_NUM = 16;
//胁迫指纹标记
var duressFingerFlag = null;
//是否显示胁迫指纹(用户登记指纹时，不需要胁迫指纹)，默认显示胁迫指纹
var duressFingerShowFlag = true;
//手指标记数组
var fingerIdArray = new Array();//[]
//指纹模板数据数组
var templateDataArray = new Array();//[]
//定时器--关闭setTimeOut时用到
var timer = null;
//定时器--验证
var verifyTimer = null;
//判断当前手指是否正在采集中
var collectFlag = false;
//当前点击的手指标记
var fpIdNum = null;
//访问ISSOnline_server的ip
var serverIp = null;
//访问的ISSOnline_server端口
var serverPort = null;
//ISSOnline_server的url的公共部分:http://127.0.0.1:22001/ZKBIOOnline
//var issOnlineUrl = null;
var issOnlineUrl = "http://127.0.0.1:22001/ZKBIOOnline";
//是否是访客
var isVisPager = false;
//定义驱动的版本号
var fpDriverVersion="5.0.0.1";
//是否为比对功能
var isComp= false;

/**
 * 加载xml中ISSOnline_server的ip和port
 * @author wenxin
 * @create 2013-06-15 15:01:31 pm
 * @param url 加载xml的url
 */
function loadXml(url)
{
	$.ajax( {
		type : "GET",
		url : url,
		dataType : "xml",
		async: false,
		success : function(xml) 
		{
			$(xml).find('service').each(function(){
				var service = $(this);
				serverIp = service.find('ISSOnline_serverIp').text(); 
				serverPort = service.find('ISSOnline_serverPort').text();
			})
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			//如果取配置文件出错，则设置默认
			serverIp = "127.0.0.1";
			serverPort = "22001";
	    }
	});
	//给issOnlineUrl赋值
	issOnlineUrl = "http://"+serverIp+":"+serverPort+"/ZKBIOOnline";
}

/**
 * 获取编辑时，查询的数据库的指纹标记和指纹模板
 * @author wenxin
 * @create 2013-04-22 10:18:31 am
 * @param fingerIdList 数据库查询的指纹标记
 * @param templateList 数据库查询的指纹模板
 */
function loadFPDataTemplate(fingerIdList, templateList)
{
	if(fingerIdList == "[]") 
	{
		$("#fingerId").val(" ");
	} 
	else 
	{
		$("#fingerId").val(fingerIdList);
	}
	if(templateList == "[]") 
	{
		$("#fingerTemplate10").val(" ");
	} 
	else 
	{
		$("#fingerTemplate10").val(templateList);
	}
}


/**
 * 获取浏览器类型
 * @author wenxin
 * @create 2013-08-09 17:24:31 pm
 */
function getBrowserType()
{
	var browserFlag = "";
	 //是否支持html5的cors跨域
    if (typeof(Worker) !== "undefined")
    {
        browserFlag = "html5";
    }
    //此处判断ie8、ie9
    else if(navigator.userAgent.indexOf("MSIE 8.0")>0 || navigator.userAgent.indexOf("MSIE 9.0")>0)
    {
        browserFlag = "simple";
    }
    else
	{
		browserFlag = "upgradeBrowser";//当前浏览器不支持该功能,请升级浏览器
	}
    return browserFlag;
}






/**
 * 获取webserver信息的回调函数
 * @author wenxin
 * @create 2013-08-09 17:24:31 pm
 */
function getWebServerInfoCallBack(result, paramArray, isFPLogin)
{
	//返回码
	var ret = null;
	ret = result.ret;
	//接口调用成功返回时
	if(ret == 0)
	{
		if(isFPLogin)
		{
			//显示指纹登录
			showFPVerify(paramArray);
		}
		else
		{
			//显示登记--可以点击采集指纹
			showRegister(paramArray);
			//计算指纹数量${pers_person_templateCount}:指纹数
			showFPCountInit(paramArray[5], paramArray[6]);
			//鼠标over事件
			//mouseOverEvent();
			// 对比指纹驱动
			if (result.data&&result.data.server_version){
			    compareFPDriver(result.data.server_version);
			}
		}
		
	}
}

/**
 * 显示指纹登录--点击进行指纹验证
 * @author wenxin
 * @create 2013-06-14 10:09:20 am
 * @param paramArray 存放国际化元素的数组
 */
function showFPVerify(paramArray)
{
	$("#userLoginForm .but_fing_disabled").hide();
	$("#userLoginForm .but_fing").show();
}

/**
 * 显示登记--点击采集指纹
 * @author wenxin
 * @create 2013-06-14 10:09:20 am
 * @param paramArray 存放国际化元素的数组
 */
function showRegister(paramArray)
{
	var hrefStr = "";
	var param = '"'+paramArray[0]+'", "'+paramArray[1]+'", "'+paramArray[2]+'", '+null+'';
	$("#fpRegister").remove();
	$("#downloadDriver").remove();
	//webservice接口调用成功，说明驱动已经安装
	hrefStr = "<a id='fpRegister' onclick='submitRegister("+param+", true"+")' title='"+paramArray[3]+"' class='linkStyle'>"+paramArray[3]+"</a>";
	$("#fpRegisterDiv").append(hrefStr);
}

/**
 * 在页面初始化时，计算指纹数量
 * @author wenxin
 * @create 2013-04-25 11:31:20 am
 * 
 */
function showFPCountInit(fingerIdCount, text)
{
	$("#fpCountMessage").text(text +" "+ fingerIdCount);
}

/**
 * 对比指纹驱动版本
 * @author gordon.zhang
 * @param oldVersion 旧驱动版本
 * @create 2015-01-28 17:24:31 pm
 */
function compareFPDriver(oldVersion)
{
    var existVersion = fpDriverVersion
	var curVersion = oldVersion;//3.5.2
	var existVersionArr = existVersion.split(".");
	var curVersionArr = curVersion.split(".");
	var isLast = true;
	var len = existVersionArr.length;
	for(var i=len;i>0;i--)
	{
		var existVersionTemp = parseInt(existVersionArr[i-1]);
		var curVersionTemp = parseInt(curVersionArr[i-1]);
		if(existVersionTemp<curVersionTemp)
		{
			isLast = false;
		}
		else if(existVersionTemp>curVersionTemp)
		{
			isLast = true;
		}
		else
		{
			//等于 忽略
		}
	}
}
/**
 * 显示发现新驱动
 * @author gordon.zhang
 * @create 2015-01-28 17:24:31 pm
 */
function showNewDriver(){
    var hrefStr = "<a id='downloadDriver' href='base/middleware/zkbioonline.exe' title='下载新驱动'>下载新驱动</a>";
    $("#driverDownload").append(hrefStr);
}

/**
 * 获取webserver的信息--简易版
 * @author wenxin
 * @param paramArray 存放国际化元素的数组
 * @param isFPLogin 是否是指纹登录 true:是；false:否
 * @create 2013-08-09 17:43:31 pm
 */
function getInfoForSimpleCallBack(xDomainRequest, paramArray, isFPLogin)
{
	//获取接口返回值
	var resultData = xDomainRequest.responseText;
	//转化为json对象
	var obj = jQuery.parseJSON(resultData);
	//返回码
	var ret = null;
	if(obj != null && obj.ret != undefined)
	{
		ret = obj.ret;
	}
	//接口调用成功返回时
	if(ret == 0)
	{
		if(isFPLogin)
		{
			//显示指纹比对
			showFPVerify(paramArray);
		}
		else
		{
			//显示登记--点击采集指纹
			showRegister(paramArray);
			//计算指纹数量${pers_person_templateCount}:指纹数
			
			showFPCountInit(paramArray[5], paramArray[6]);
			//鼠标over事件
			//mouseOverEvent();
		}
		//用完后，将对象置为空
		xDomainRequest = null;
	}
}
//------
/**
 * 点击登记，触发事件
 * @author wenxin
 * @create 2013-05-21 11:31:20 am
 * @param title 页面标题国际化内容
 * @param fpCount 指纹数国际化内容
 * @param saveText 提示:是否保存国际化内容
 * @param downloadText 驱动安装国际化内容
 * @param isDriverInstall 是否安装了驱动
 */
function submitRegister(title, fpCount, saveText, downloadText, isDriverInstall)
{
	//支持html5
	if (typeof(Worker) !== "undefined" && isDriverInstall)   
	{  
		var box=document.getElementById("box");
	    var bg=document.getElementById("bg");
	   	box.style.display="block";//显示内容层，显示覆盖层
	 	box.style.left=parseInt((document.documentElement.scrollWidth-box.offsetWidth)/2)+document.documentElement.scrollLeft+"px";
	 	box.style.top=Math.abs(parseInt((document.documentElement.clientHeight-box.offsetHeight)/2))+document.documentElement.scrollTop+"px"; //为内容层设置位置
	 	bg.style.display="block";
	 	bg.style.height=document.documentElement.scrollHeight+"px"; 
	 	isComp=false;
	 	dataInitReg();
	    //关闭页面时，提示保存数据
		//storeBeforeClose(fpCount, saveText);
	} 
	else if(typeof(Worker) == "undefined" && isDriverInstall)
	{  
	    //createWindow('base_baseFPRegisterSimple.action?random=' + getRandomNum() + '^0^0^465^460^' + title);//public/html/applet.html
	    showModalDialog('webapp/html/baseFPRegisterSimple.html',title,'dialogWidth:465px;dialogHeight:460px;dialogLeft:600px;dialogTop:150px;center:yes;resizable:no;status:yes');
	    //关闭页面时，提示保存数据
		//storeBeforeClose(fpCount, saveText);
	}  
	else if(!isDriverInstall)
	{
		alert("请安装指纹驱动或启动该服务!");
		//messageBox({messageType: "alert", title: "提示", text: "请安装指纹驱动或启动该服务!"});
		if(typeof($("#downloadDriver").val()) == "undefined")
		{
			var hrefStr = "<a id='downloadDriver' href='webapp/middleware/zkbioonline.exe' title='" + downloadText + "'>"+downloadText+"</a>";
			$("#driverDownload").append(hrefStr);
		}
	}
}


/**
 * 关闭页面时，如果有修改操作，则提示用户保存数据
 * @author wenxin
 * @create 2013-06-08 19:36:20 pm
 * @param fpCount 指纹数国际化内容
 * @param saveText 提示:是否保存国际化内容
 */
function storeBeforeClose(fpCount, saveText)
{
	 //关闭页面时，监听关闭的onclick事件
	getCurrentWindow().button("close").attachEvent("onClick", function(){
		//判断是否修改了数据(包括新增和删除)
		if ($("#whetherModify").val() != undefined && (fpModifyFlag != undefined && fpModifyFlag)) 
		{
			//获取指纹标记数据
			var fingerIdData = fingerIdArray;
			//获取指纹模板数据
			var fingerTemplateData = templateDataArray;
			var flag=confirm(saveText);
			saveFPData(flag, fpCount);
		}
		else
		{
			//取消采集 
			cancelRegister();
			//将定时器的递归调用关闭
			clearTimeout(timer);
			//closeWindow();
			close();
		}
	});
}

/**
 * 获取页面的指纹数据 
 * @author wenxin
 * @create 2013-05-13 10:18:31 am
 * @param 
 */
function getDataFromPage() 
{
    var fingerId = $("#fingerId").val();
    var fingerTemplate = $("#fingerTemplate10").val();
    //如果有数据
    if($.trim(fingerId) != "")
    {
	    fingerId = fingerId.substr(1, fingerId.length-2);
	    fingerTemplate = fingerTemplate.substr(1, fingerTemplate.length-2);
	    fingerIdArray = fingerId.split(",");
	    templateDataArray = fingerTemplate.split(",");
    }else{
    	fingerIdArray=new Array();
    	templateDataArray=new Array();
    }
}

/**
 * 初始化绘画手指、手掌、圆弧的起始坐标,并做成json格式
 * @author wenxin
 * @create 2013-06-15 15:40:31 pm
 */
function initCoordJson()
{
	var coordJson = [{"num" : 0, "coord" : {"x" : x + 3, "y" : y - 37}},
		             {"num" : 1, "coord" : {"x" : x + 25, "y" : y - 37}},
		             {"num" : 2, "coord" : {"x" : x + 47, "y" : y - 34}},
		             {"num" : 3, "coord" : {"x" : x + 67, "y" : y - 26}},
		             {"num" : 4, "coord" : {"x" : x + 77, "y" : y + 18}},
		             {"num" : 5, "coord" : {"x" : x + 153, "y" : y + 34}},
		             {"num" : 6, "coord" : {"x" : x + 159, "y" : y - 19}},
		             {"num" : 7, "coord" : {"x" : x + 177, "y" : y - 30}},
		             {"num" : 8, "coord" : {"x" : x + 198, "y" : y - 36}},
		             {"num" : 9, "coord" : {"x" : x + 220, "y" : y - 36}},
		             {"num" : 10, "coord" : {"x" : x, "y" : y}},
		             {"num" : 11, "coord" : {"x" : x + 170, "y" : y + 12}},
		             {"num" : 12, "coord" : {"x" : x + 210, "y" : y - 346}}];
	return coordJson;
}

/**
 * 将绘画的坐标点放入数组
 * @author wenxin
 * @create 2013-05-31 18:01:33 pm
 * @param coordArray 传入的数组，放入坐标后，返回
 * @param x, y 绘画手指的起点的坐标
 * @param num 手指、手掌编号0-9：手指编号；10：左手掌，11：右手掌,12:圆弧。
 */
function initCoordArray(coordArray, x, y, num)
{
	if(num == 0)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 2, y - 35);
		coordArray[2] = new Coord(x + 5, y - 40);
		coordArray[3] = new Coord(x + 11, y - 42);
		coordArray[4] = new Coord(x + 16, y - 40);
		coordArray[5] = new Coord(x + 18, y - 35);
		coordArray[6] = new Coord(x + 18, y + 1);
		coordArray[7] = new Coord(x + 15, y + 5);
		coordArray[8] = new Coord(x + 9, y + 7);
		coordArray[9] = new Coord(x + 3, y + 5);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 1)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 8, y - 50);
		coordArray[2] = new Coord(x + 12, y - 54);
		coordArray[3] = new Coord(x + 19, y - 55);
		coordArray[4] = new Coord(x + 22, y - 53);
		coordArray[5] = new Coord(x + 24, y - 49);
		coordArray[6] = new Coord(x + 18, y + 1);
		coordArray[7] = new Coord(x + 15, y + 6);
		coordArray[8] = new Coord(x + 8, y + 7);
		coordArray[9] = new Coord(x + 3, y + 4);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 2)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 14, y - 54);
		coordArray[2] = new Coord(x + 16, y - 57);
		coordArray[3] = new Coord(x + 23, y - 58);
		coordArray[4] = new Coord(x + 28, y - 55);
		coordArray[5] = new Coord(x + 29, y - 50);
		coordArray[6] = new Coord(x + 17, y + 4);
		coordArray[7] = new Coord(x + 13, y + 8);
		coordArray[8] = new Coord(x + 6, y + 9);
		coordArray[9] = new Coord(x + 1, y + 5);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 3)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 19, y - 37);
		coordArray[2] = new Coord(x + 21, y - 39);
		coordArray[3] = new Coord(x + 28, y - 39);
		coordArray[4] = new Coord(x + 32, y - 36);
		coordArray[5] = new Coord(x + 33, y - 31);
		coordArray[6] = new Coord(x + 17, y + 6);
		coordArray[7] = new Coord(x + 12, y + 10);
		coordArray[8] = new Coord(x + 6, y + 10);
		coordArray[9] = new Coord(x + 1, y + 6);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 4){
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 30, y - 18);
		coordArray[2] = new Coord(x + 34, y - 17);
		coordArray[3] = new Coord(x + 37, y - 14);
		coordArray[4] = new Coord(x + 39, y - 11);
		coordArray[5] = new Coord(x + 39, y - 8);
		coordArray[6] = new Coord(x + 38, y - 6);
		coordArray[7] = new Coord(x + 12, y + 15);
		coordArray[8] = new Coord(x + 8, y + 17);
		coordArray[9] = new Coord(x + 2, y + 14);
		coordArray[10] = new Coord(x - 2, y + 8);
		coordArray[11] = new Coord(x, y);
	}
	else if(num == 5)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x - 26, y - 21);
		coordArray[2] = new Coord(x - 27, y - 24);
		coordArray[3] = new Coord(x - 26, y - 30);
		coordArray[4] = new Coord(x - 21, y - 34);
		coordArray[5] = new Coord(x - 16, y - 34);
		coordArray[6] = new Coord(x + 12, y - 18);
		coordArray[7] = new Coord(x + 15, y - 10);
		coordArray[8] = new Coord(x + 13, y - 3);
		coordArray[9] = new Coord(x + 7, y + 1);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 6)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x - 17, y - 46);
		coordArray[2] = new Coord(x - 17, y - 50);
		coordArray[3] = new Coord(x - 13, y - 56);
		coordArray[4] = new Coord(x - 6, y - 56);
		coordArray[5] = new Coord(x - 3, y - 54);
		coordArray[6] = new Coord(x + 15, y - 11);
		coordArray[7] = new Coord(x + 15, y - 4);
		coordArray[8] = new Coord(x + 11, y + 2);
		coordArray[9] = new Coord(x + 4, y + 2);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 7)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x - 12, y - 54);
		coordArray[2] = new Coord(x - 10, y - 58);
		coordArray[3] = new Coord(x - 5, y - 62);
		coordArray[4] = new Coord(x + 1, y - 61);
		coordArray[5] = new Coord(x + 4, y - 58);
		coordArray[6] = new Coord(x + 18, y - 4);
		coordArray[7] = new Coord(x + 16, y + 1);
		coordArray[8] = new Coord(x + 11, y + 5);
		coordArray[9] = new Coord(x + 5, y + 4);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 8)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x - 5, y - 50);
		coordArray[2] = new Coord(x - 2, y - 54);
		coordArray[3] = new Coord(x + 3, y - 57);
		coordArray[4] = new Coord(x + 9, y - 55);
		coordArray[5] = new Coord(x + 11, y - 52);
		coordArray[6] = new Coord(x + 18, y - 1);
		coordArray[7] = new Coord(x + 14, y + 4);
		coordArray[8] = new Coord(x + 9, y + 6);
		coordArray[9] = new Coord(x + 4, y + 5);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 9)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x, y - 37);
		coordArray[2] = new Coord(x + 3, y - 41);
		coordArray[3] = new Coord(x + 7, y - 43);
		coordArray[4] = new Coord(x + 13, y - 41);
		coordArray[5] = new Coord(x + 15, y - 37);
		coordArray[6] = new Coord(x + 17, y + 1);
		coordArray[7] = new Coord(x + 15, y + 3);
		coordArray[8] = new Coord(x + 10, y + 6);
		coordArray[9] = new Coord(x + 3, y + 4);
		coordArray[10] = new Coord(x, y);
	}
	else if(num == 10)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x + 2, y - 8);
		coordArray[2] = new Coord(x + 6, y - 16);
		coordArray[3] = new Coord(x + 13, y - 23);
		coordArray[4] = new Coord(x + 27, y - 27);
		coordArray[5] = new Coord(x + 37, y - 25);
		coordArray[6] = new Coord(x + 43, y - 23);
		coordArray[7] = new Coord(x + 64, y - 16);
		coordArray[8] = new Coord(x + 69, y - 11);
		coordArray[9] = new Coord(x + 73, y - 3);
		
		coordArray[10] = new Coord(x + 73, y + 10);
		coordArray[11] = new Coord(x + 71, y + 18);
		coordArray[12] = new Coord(x + 57, y + 40);
		coordArray[13] = new Coord(x + 50, y + 46);
		coordArray[14] = new Coord(x + 41, y + 49);
		coordArray[15] = new Coord(x + 34, y + 49);
		coordArray[16] = new Coord(x + 14, y + 43);
		coordArray[17] = new Coord(x + 10, y + 41);
		coordArray[18] = new Coord(x + 6, y + 36);
		coordArray[19] = new Coord(x + 2, y + 29);
		coordArray[20] = new Coord(x, y);
	}
	else if(num == 11)
	{
		coordArray[0] = new Coord(x, y);
		coordArray[1] = new Coord(x - 2, y - 10);
		coordArray[2] = new Coord(x + 1, y - 20);
		coordArray[3] = new Coord(x + 14, y - 31);
		coordArray[4] = new Coord(x + 47, y - 39);
		coordArray[5] = new Coord(x + 55, y - 38);
		coordArray[6] = new Coord(x + 61, y - 34);
		coordArray[7] = new Coord(x + 68, y - 26);
		coordArray[8] = new Coord(x + 72, y - 16);
		coordArray[9] = new Coord(x + 72, y + 13);
		
		coordArray[10] = new Coord(x + 68, y + 22);
		coordArray[11] = new Coord(x + 62, y + 29);
		coordArray[12] = new Coord(x + 60, y + 30);
		coordArray[13] = new Coord(x + 39, y + 36);
		coordArray[14] = new Coord(x + 34, y + 36);
		coordArray[15] = new Coord(x + 20, y + 33);
		coordArray[16] = new Coord(x + 16, y + 29);
		coordArray[17] = new Coord(x, y);
	}
	else if(num == 12)
	{
		coordArray[0] = new Coord(x - 10, y);
		coordArray[1] = new Coord(x + 212, y);
		coordArray[2] = new Coord(x + 212, y + 129);
		coordArray[3] = new Coord(x + 201, y + 130);
		coordArray[4] = new Coord(x + 191, y + 131);
		coordArray[5] = new Coord(x + 174, y + 131);
		coordArray[6] = new Coord(x + 159, y + 129);
		coordArray[7] = new Coord(x + 142, y + 127);
		coordArray[8] = new Coord(x + 133, y + 125);
		coordArray[9] = new Coord(x + 114, y + 120);
		
		coordArray[10] = new Coord(x + 97, y + 113);
		coordArray[11] = new Coord(x + 86, y + 108);
		coordArray[12] = new Coord(x + 72, y + 100);
		coordArray[13] = new Coord(x + 52, y + 87);
		coordArray[14] = new Coord(x + 40, y + 76);
		coordArray[15] = new Coord(x + 29, y + 64);
		coordArray[16] = new Coord(x + 16, y + 48);
		coordArray[17] = new Coord(x + 5, y + 30);
		coordArray[18] = new Coord(x - 10, y);
	}
	return coordArray;
}

/**
 * 坐标点对象
 * @author wenxin
 * @create 2013-05-31 18:01:33 pm
 */
var Coord = function(x, y)
{
	this.x = x;
	this.y = y;
}

/**
 * 绘画手指
 * @author wenxin
 * @create 2013-05-31 18:01:33 pm
 * @param context 2d画布上下文
 * @param pointArray 坐标点数组
 * @param renderFlag 渲染标记 stroke:绘画边线；fill:填充
 * @param color 渲染颜色
 */
var renderFinger = function(context, pointArray) 
{
	this.context = context;
	this.pointArray = pointArray;
	this.isClick = false;
	this.drawFinger = function(renderFlag, color) 
	{
		if(renderFlag == "stroke")
		{
			this.context.strokeStyle = color;
		}
		else if(renderFlag == "fill")
		{
			this.context.fillStyle = color;
		}
		this.context.lineWidth = 1;
		this.context.beginPath();
		for(var i=0; i<this.pointArray.length; i++)
		{
			if(i == 0)
			{
				this.context.moveTo(this.pointArray[0].x, this.pointArray[0].y);
			}
			else
			{
				this.context.lineTo(this.pointArray[i].x, this.pointArray[i].y);
			}
		}
		if(renderFlag == "stroke")
		{
			this.context.stroke();
		}
		else if(renderFlag == "fill")
		{
			this.context.fill();
		}
	};
};

/**
 * 页面加载和重画时渲染手指
 * @author wenxin
 * @create 2013-05-13 15:26:31 pm
 * @param context 2d画布上下文
 * @param num 当前需要渲染的手指编号
 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 */
function renderInit(context, num, browserFlag)
{
	var fingerId;
	for(var i=0; i<fingerIdArray.length; i++)
	{
		fingerId = eval(fingerIdArray[i]);
		if(fingerId >= DURESS_FINGER_NUM)
		{
			fingerId = fingerId -DURESS_FINGER_NUM;
			if(browserFlag == "html5")
			{
				if(fingerId == num)
			    {
					context.fillStyle = "red";
					context.fill();
			    }
			}
		}
		else
		{
			if(browserFlag == "html5"){
				if(fingerId == num)
				{
					context.fillStyle = "rgb(122,193,66)";
					context.fill();
				}
			}
		}
		if(browserFlag == "simple")
		{
			document.getElementById("finger" + fingerId).checked = true;
		}
	}
}

/**
 * 显示指纹图像
 * @author wenxin
 * @create 2013-05-18 11:22:31 am
 * @param context 2d画布上下文
 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 */
function showImage(context, base64FPImg, browserFlag)
{
	var img;
	var imgSrc = "data:image/jpg;base64,"+base64FPImg;
	if(browserFlag == "html5"){
		img = new Image();
		//img.src = sysCfg.rootPath + "/public/html/bmpFile1.jpg";
		img.src = "";
		img.src = imgSrc;
		img.onload=function() {
			// 保存当前的绘图状态
			context.save();
			// 开始创建路径
			context.beginPath();
			// 画一个椭圆
			context.oval(125, 142, 112, 145);
			// 关闭路径
			context.closePath();
			// 剪切路径
			context.clip();
			//将图片画到画布上
	        context.drawImage(img, 70, 70, 112, 145);
			//调用restore最后一次存储的状态会被恢复
	        context.restore();
	    }
	}
	else if(browserFlag == "verification")
	{
		img = new Image();
		img.src = "";
		img.src = imgSrc;
		img.onload=function() {
			// 保存当前的绘图状态
			context.save();
			// 开始创建路径
			context.beginPath();
			// 画一个椭圆
			context.oval(92, 159, 100, 128);
			// 关闭路径
			context.closePath();
			// 剪切路径
			context.clip();
			//将图片画到画布上
	        context.drawImage(img, 37, 90, 112, 145);
			//调用restore最后一次存储的状态会被恢复
	        context.restore();
	    }
	}
	else if(browserFlag == "clearForVerify" || browserFlag == "clearForRegister")
	{
		img = new Image();
		img.src = "";
		img.src = base64FPImg;
		img.onload=function() {
			// 保存当前的绘图状态
			context.save();
			// 开始创建路径
			context.beginPath();
			// 画一个椭圆
			if(browserFlag == "clearForVerify")
			{
				context.oval(91, 160, 112, 145);
			}
			else if(browserFlag == "clearForRegister")
			{
				context.oval(125, 142, 132, 165);
			}
			// 关闭路径
			context.closePath();
			// 剪切路径
			context.clip();
			//将图片画到画布上
			if(browserFlag == "clearForVerify")
			{
				context.drawImage(img, 12, 54, 160, 213);
			}
	        else if(browserFlag == "clearForRegister")
	        {
	        	context.drawImage(img, 60, 60, 132, 165);
	        }
			//调用restore最后一次存储的状态会被恢复
	        context.restore();
	    }
	}
	else if(browserFlag == "simple")
	{
		$("#showFPImageDiv").html("<img src="+imgSrc+" width='112' height='145' />");
	}
	else if(browserFlag == "verifySimple")
	{
		$("#showSeachingDiv").show();
		$("#showSeachingDiv").html("&nbsp;&nbsp;<img src=\"/public/images/searching.gif\" width='20' height='20'/></br><label style='color:yellow;align:center;font-size: 14px;'>处理中...</label>");
	}
	else if(browserFlag == "clearForSimple")
	{
		$("#showFPImageDiv").html("");
	}
}

/**
 * 绘画手掌
 * @author wenxin
 * @create 2013-06-01 09:01:33 am
 * @param context 2d画布上下文
 * @param pointArray 坐标点数组
 * @param color 渲染颜色
 */
var renderHand = function(context, pointArray) 
{
	this.context = context;
	this.pointArray = pointArray;
	this.isClick = false;
	this.drawHand = function(color) 
	{
		this.context.strokeStyle = color;
		this.context.lineWidth = 1;
		this.context.beginPath();
		for(var i=0; i<this.pointArray.length; i++)
		{
			if(i == 0)
			{
				this.context.moveTo(this.pointArray[0].x, this.pointArray[0].y);
			}
			else
			{
				this.context.lineTo(this.pointArray[i].x, this.pointArray[i].y);
			}
		}
		this.context.stroke();
	};
};

/**
 * 检查指纹采集器
 * @author wenxin
 * @create 2013-06-17 20:18:31 pm
 * @param context 2d画布上下文
 * @param paramArray 存放国际化元素的数组
 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 */
function checkFPReader(context, paramArray, browserFlag)
{
	alert(issOnlineUrl);
	if(browserFlag == "html5")
	{
		$.ajax( {
			type : "GET",
			url : issOnlineUrl+"/fingerprint/beginCapture?type=1&FakeFunOn=0&random="+getRandomNum(),
			dataType : "json",
			async: false,
			//timeout:1000,
			success : function(result) 
			{
				//返回码
				var ret = null;
				ret = result.ret;
				//接口调用成功返回时
				if(ret == 0)
				{
					//显示框--采集提示
					collectTips(context, paramArray[0], "html5");
				}
				else if(ret == -2001)
				{
					//显示框--采集提示
					collectTips(context, paramArray[1], "html5");
				}
				else if(ret == -2002)
				{
					getWebServerInfo(null, null, "1");
				}
				else if(ret == -2005)
				{
					//显示框--采集提示
					collectTips(context, paramArray[3], "html5");
				}
				collectFlag = true;
				//取消采集
				cancelRegister();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) 
			{
				alert("请安装指纹驱动或启动该服务!");
				//messageBox({messageType: "alert", title: "${common_prompt_title}", text: "${base_fp_connectPrompt}"});
		    }
		});
	}
	else if(browserFlag == "simple")
	{
		//创建XDomainRequest实例，用于ie8和ie9跨域访问
		var xDomainRequest = new XDomainRequest();
		//如果xDomainRequest存在,则可以使用xDomainRequest函数，否则，说明不是ie浏览器
		if (xDomainRequest) 
		{  
			xDomainRequest.open('GET', issOnlineUrl+"/fingerprint/beginCapture?type=1&FakeFunOn=0&random="+getRandomNum());  
			xDomainRequest.onload = function()
			{
				//获取接口返回值
				var resultData = xDomainRequest.responseText;
				//转化为json对象
				var obj = jQuery.parseJSON(resultData);
				//返回码
				var ret = null;
				if(obj != null && obj.ret != undefined)
				{
					ret = obj.ret;
				}
				//接口调用成功返回时
				if(ret == 0)
				{
					//显示框--采集提示
					collectTips(null, paramArray[0], "simple");
				}
				else if(ret == -2001)
				{
					//显示框--采集提示
					collectTips(null, paramArray[1], "simple");
				}
				else if(ret == -2002)
				{
					getWebServerInfoForSimple(null, null, "1");
				}
				collectFlag = true;
				//取消采集
				cancelRegister();
			};  
			xDomainRequest.onerror = function()
			{
				//用完后，将对象置为空
				xDomainRequest = null;
			};
			xDomainRequest.send(); 
		} 
	}
}

/**
 * 显示框--显示采集次数、采集成功、失败等信息
 * @author wenxin
 * @create 2013-05-16 16:56:31 pm
 * @param context 2d画布上下文
 * @param text  显示信息内容
 * @param browserFlag 浏览器标记或比对验证标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 * --verification:指纹验证标记
 */
function collectTips(context, text, browserFlag)
{
	if(browserFlag == "simple")
	{
		$("#showCollInfoDiv").html("<span style='color:rgb(122,193,66); font-size: 12px;word-break: break-all; word-wrap: break-word;'>"+text+"</span>");
	}
	else if(browserFlag == "html5")
	{
		context.fillStyle = bgColor;//bgColor;
		context.fillRect(205, 18, 240, 16);
		
		//文字右对齐
		context.fillStyle = "rgb(122,193,66)";
		context.font ="12px Arial,微软雅黑"; 
		//context.shadowColor = 'white'; 
	    //context.shadowBlur = 10; 
	    //context.strokeText(text, 230, 30);
		context.textAlign = "end";
		context.fillText(text, 400, 30);
	}
	else if(browserFlag == "verification")
	{
		//#6BA5D7
		context.fillStyle = "#F3F5F0";//#6BA5D7
		context.fillRect(2, 8, 600, 30);
		//获取canvas对象
		var canvas ="";
		if(isComp){
			canvas = document.getElementById("canvasComp");
		}else{
			canvas = document.getElementById("canvas");
		}
//		canvas.width = canvas.width;
		//返回一个文本的度量信息对象metrics
		var metrics = context.measureText(text);
		//文本宽度
		var textWidth = metrics.width;
		//canvas宽度
		canvas != null?canvasWidth = canvas.width:canvasWidth = 450;
		//文本开始x坐标
		var x = textWidth/2 + (canvasWidth - textWidth)/2;
		
		
		//context.fillStyle = bgColor;
		//context.fillRect(0, 18, 445, 16);
		
		//文字右对齐
		context.fillStyle = "rgb(122,193,66)";
		context.font ="24px Arial,微软雅黑"; 
		context.textAlign = "center";
		//自动换行
		autoWordBreak(context,text,canvasWidth,x);
		context.restore();
	}
	else if(browserFlag == "verifyForSimple")
	{
		$("#showCollInfoDiv").html("<span style='color:yellow;align:center;font-size: 18px;word-break: break-all; word-wrap: break-word;'>"+text+"</span>");
	}
}

/**
 * 画进度条
 * @author wenxin
 * @create 2013-05-16 16:56:31 pm
 * @param context 2d画布上下文
 * @param x,y,width,height 进度条底框的坐标和宽度、高度
 */
function drawProgressBar(context, collCount)
{
	var x = 300;
	var y = 60;
	var width = 90;
	var height = 20;
	context.fillStyle = bgColor;
	context.fillRect(x, y, width, height);
	if(collCount == 0)
	{
		context.fillStyle = "rgb(175,181,185)";
		context.fillRect(x + 4, y + 2, width - 52, height - 4);
		context.fillRect(x + 46, y + 2, width -52, height - 4);
		context.fillRect(x + 86, y + 2, width - 52, height - 4);
	}
	else if(collCount == 1)
	{
		context.fillStyle = "rgb(122,193,66)";
		context.fillRect(x + 4, y + 2, width - 52, height - 4);
		context.fillStyle = "rgb(175,181,185)";
		context.fillRect(x + 46, y + 2, width - 52, height - 4);
		context.fillRect(x + 86, y + 2, width - 52, height - 4);
	}
	else if(collCount == 2)
	{
		context.fillStyle = "rgb(122,193,66)";
		context.fillRect(x + 4, y + 2, width - 52, height - 4);
		context.fillRect(x + 46, y + 2, width - 52, height - 4);
		context.fillStyle = "rgb(175,181,185)";
		context.fillRect(x + 86, y + 2, width - 52, height - 4);
	}
	else if(collCount == 3)
	{
		context.fillStyle = "rgb(122,193,66)";
		context.fillRect(x + 4, y + 2, width - 52, height - 4);
		context.fillRect(x + 46, y + 2, width - 52, height - 4);
		context.fillRect(x + 86, y + 2, width - 52, height - 4);
	}
}

/**
 * 判断当前手指是否在fingerIdArray中，如果在，则说明此手指已经采集指纹
 * @author wenxin
 * @create 2013-05-15 16:26:31 pm
 * @param num 手指编号
 * @param fingerIdArray 存放手指编号的数组
 * @return 返回boolean值 true:num包含在fingerIdArray中；false:没有包含
 */
function isContains(fingerIdArray, num)
{
	var fingerId;
	var isCollected = false;
	for(var j=0; j<fingerIdArray.length; j++)
	{
		fingerId = eval(fingerIdArray[j]);
		if(fingerId >= DURESS_FINGER_NUM)
		{
			fingerId = fingerId -DURESS_FINGER_NUM;
		}
		if(fingerId == num)
		{
			isCollected = true;
		}
	}
	return isCollected;
}

/**
 * 清空指纹图像
 * @author wenxin
 * @create 2013-09-05 15:15:11 pm
 */
function clearFPImage(context, browserFlag)
{
	if(browserFlag == "verification")
	{
//		showImage(context, "${base}/base/images/base_fpVerify_clearImage.png", "clearForVerify");
	}
	else if(browserFlag == "register")
	{
		showImage(context, "image/base_fpVerify_clearImage.png", "clearForRegister");
	}
	else if(browserFlag == "verifyForSimple" || browserFlag == "registerForSimple")
	{
		showImage(null, "", "clearForSimple");
	}
}

/**
 * 获取指纹模板
 * @author wenxin
 * @create 2013-05-22 19:51:31 pm
 * @param paramArray 存放国际化元素的数组
 * @param flag 判断是登记和验证标记 register:登记；verification:验证
 */
function getFPTemplate(paramArray, flag)
{
	var fpTemplate = "";
	var collectSuccessFlag = false;
	$.ajax( {
		type : "GET",
		url : issOnlineUrl+"/fingerprint/getTemplate?random="+getRandomNum(),
		dataType : "json",
		async: false,
		success : function(result) 
		{
			//返回码
			var ret = null;
			ret = result.ret;
			if(ret == 0)
			{
				fpTemplate = result.data.template;
			}
			//成功
			if(ret == 0)
			{
				collectSuccessFlag = true;
				if(flag == "register")
				{
					//判断手指是否已经采集指纹
					var compareRet = "";
					//如果前面已经录入指纹
					if(templateDataArray.length > 0)
					{
						//发送请求，进行后台指纹比对yls
						//compareRet = fpComparision(fpTemplate, templateDataArray, paramArray[3]);
					}
					if($.trim(compareRet) == "dllNotExist")
					{
						//采集完指纹，渲染手指
						renderAfterColl(globalContext, fpIdNum, bgColor, false);//bgColor判断
						//显示框--采集提示
						collectTips(globalContext, "动态库加载失败", "html5");
					}
					else
					{
						if(compareRet == "noFingerServer")
						{
							//采集完指纹，渲染手指
							renderAfterColl(globalContext, fpIdNum, bgColor, false);//bgColor判断
							//显示框--采集提示
							collectTips(globalContext, "未启动比对服务", "html5");
						}
						else
						{
							//此手指未采集指纹
							if(compareRet != "ok")
							{
								//采集完指纹，渲染手指
								renderAfterColl(globalContext, fpIdNum, bgColor, true);//bgColor判断
								//显示框--采集提示
								collectTips(globalContext, paramArray[0], "html5");
								//胁迫指纹
								if(duressFingerFlag)
								{
									//将手指标记保存到数组中
									fingerIdArray[fingerIdArray.length] = fpIdNum + DURESS_FINGER_NUM;
								}
								else
								{
									//将手指标记保存到数组中
									fingerIdArray[fingerIdArray.length] = fpIdNum;
								}
								//将指纹模板保存到数组中
								templateDataArray[templateDataArray.length] = fpTemplate;
							}
							else
							{
								//采集完指纹，渲染手指
								renderAfterColl(globalContext, fpIdNum, bgColor, false);//bgColor判断
								//Please don't repeat input fingerprint!
								//显示框--采集提示
								collectTips(globalContext, paramArray[2], "html5");
							}
						}
					}
				}
				else if(flag == "verification")
				{
					verifyFlag = false;
					//指纹比对
					fpComparison(fpTemplate);
				}
			}
			else if(ret == -2003)
			{
				//采集完指纹，渲染手指
				renderAfterColl(globalContext, fpIdNum, bgColor, false);
				//显示框--采集提示
				collectTips(globalContext, paramArray[1], "html5");
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert(paramArray[3]);
			//messageBox({messageType: "alert", title: "${common_prompt_title}", text: paramArray[3]});
	    }
	});
	return collectSuccessFlag;
}

/**
 * 发送请求到后台，进行比对
 * @author wenxin
 * @create 2013-08-05 16:20:31 pm
 * @param fpTemplate 指纹模板
 * @param templateArray 指纹模板数组
 * @param errorMsg ajax请求报错，错误信息
 */
function fpComparision(fpTemplate, templateArray, errorMsg)
{
	var ret = "";
	var templates = templateArray.toString();
	//特殊字符转义
	fpTemplate = transferredMeaning(fpTemplate);
	templates = transferredMeaning(templates);
	$.ajax( {
		type : "POST",
		url : "baseBioVerifyAction!fpComparison.action",
		contentType : "application/x-www-form-urlencoded;charset=UTF-8",
		data : "verifyTemplate="+fpTemplate+"&templates="+templates,
		dataType : "json",
		async: false,
		success : function(result) 
		{
			if(result.ret == "ok")
			{
				ret = "ok";
			}
			if(result.msg == "noFingerServer")
			{
				ret = "noFingerServer";
			}
			if(result.msg == "dllNotExist")
			{
				ret = "dllNotExist";
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) 
		{
			alert("服务器处理数据失败，请重试！错误码：");
			//messageBox({messageType: "alert", title: "${common_prompt_title}", text: "${common_prompt_serverError}"});
	    }
	});
	return ret;
}

/**
 * 采集完指纹后渲染手指
 * @author wenxin
 * @create 2013-05-18 11:33:31 am
 * @param context 2d画布上下文
 * @param num 当前需要渲染的手指编号
 * @param fillColor 采集完后填充颜色
 * @param successOrNot 采集是否成功--布尔值 true:采集成功；false:采集失败
 */
function renderAfterColl(context, num, fillColor, successOrNot)
{
	var canvas="";
	if(isComp){
		canvas = document.getElementById("canvasComp");
	}else{
		canvas = document.getElementById("canvas");
	}
	var localContext = canvas.getContext("2d");
	var coordArray = new Array();
	//初始化起始坐标,并返回json格式数据 
	var coordJson = initCoordJson();
	//进来页面，点击删除
	if(num == null)
	{
		num = fpIdNum;
	}
	//点击的手指编号和json中num相等
	if(coordJson[num].num == num)
	{
		//初始化坐标数组和绘画手指
		initCoordAndDrawFinger(context, coordArray, coordJson[num].coord.x, coordJson[num].coord.y, num);
	}
	
	//采集成功，填充颜色(红、绿)
	if(successOrNot)
	{
		if(duressFingerFlag)
		{
			localContext.fillStyle = "red";//fillColor
			localContext.fill();
			fpModifyFlag = true;
		}
		else
		{
			localContext.fillStyle = "rgb(122,193,66)";//fillColor
			localContext.fill();
			fpModifyFlag = true;
		}
	}
	else
	{
		//采集失败，填充背景色--消除颜色(黄)
		localContext.fillStyle = fillColor;
		localContext.fill();
	}
}

/**
 * 初始化坐标数组和绘画手指--获取当前的context
 * @author wenxin
 * @create 2013-05-31 18:01:33 pm
 * @param context 2d画布上下文
 * @param pointArray 坐标点数组
 * @param x,y 绘画当前手指的起始坐标
 * @param num 手指标记
 */
function initCoordAndDrawFinger(context, coordArray, x, y, num)
{
	coordArray = initCoordArray(coordArray, x, y, num);
	new renderFinger(context, coordArray).drawFinger(strokeStyle, fingerBorderColor);
}

/**
 * 点击取消按钮时，触发事件
 * @author wenxin
 * @create 2013-05-15 17:21:13 pm
 * @param "${base_fp_save}":确认保存当前修改吗?
 */
function cancelEvent(saveText, fpCountText)
{
	if(!fpModifyFlag)
	{
		if(collectFlag)
		{
			//取消采集
			cancelRegister();
			//将定时器的递归调用关闭
			clearTimeout(timer);
		}
		//closeWindow();
		close();
	}
	else
	{
		var flag=confirm(saveText);
		if(flag){
			saveFPData(flag, fpCountText);
		}else{
			close();
		}
		
	}
	
}

/**
 * 点击取消按钮和关闭页面时，弹出框保存数据
 * cancelEvent()和submitRegister()函数中回调用到
 * @author wenxin
 * @create 2013-05-14 15:11:31 pm
 * @param result 弹出框选择确定还是取消 
 * @param fpCount 指纹数国际化内容
 * @param ${pers_person_templateCount}:指纹数
 */
var saveFPData = function(result, fpCount)
{
	if(collectFlag)
	{
		//取消采集
		cancelRegister();
		//将定时器的递归调用关闭
		clearTimeout(timer);
	}
	if(result)
	{
		storeDataToHtml();
		showFPCount(fpCount);
		//closeWindow();
		close();
	}
	else
	{
		clearImageData();
		//closeWindow();
		close();
	}
}

/**
 * 判断指纹数量--页面加载时，没有计算。只是在采集完指纹后计算指纹数量
 * @author wenxin
 * @create 2013-04-22 21:26:31 pm
 */
function showFPCount(text)
{
	var fingerId = $("#fingerId").val();
	if($.trim(fingerId) == "")
	{
		$("#fpCountMessage").text(text + " " + 0);
	}
	else
	{
		fingerId = fingerId.substr(1, fingerId.length - 2);
		var fingerIdArray = new Array();
		fingerIdArray = fingerId.split(",");
		$("#fpCountMessage").text(text + " " + fingerIdArray.length);
	}
}

/**
 * 将指纹数据保存到页面
 * @author wenxin
 * @create 2013-05-24 16:12:21 pm
 */
function storeDataToHtml()
{
	//没有手指标记数据
	if(fingerIdArray.length == 0)
	{
		$("#fingerId").val(" ");
	}
	else
	{
		//将手指标记数据保存到页面
		$("#fingerId").val("["+fingerIdArray.toString()+"]");
	}
	//没有指纹模板数据
	if(templateDataArray.length == 0)
	{
		$("#fingerTemplate10").val(" ");
	}
	else
	{
		//将指纹模板数据保存到页面
		$("#fingerTemplate10").val("["+templateDataArray.toString()+"]");
	}
}

//关闭窗体
function close(){
	$("#bg").css("display", "none");
	$("#box").css("display", "none");
	$("#comparisonDiv").css("display", "none");
	globalContext="";
}

/**
 * 点击已经采集指纹的手指时，弹出框删除数据
 * 删除时的回调函数
 * @author wenxin
 * @create 2013-05-14 17:12:21 pm
 * @param result 弹出框选择确定还是取消 
 * @param context 2d画布上下文
 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
 */
var delFPData = function(result, context, browserFlag)
{
	var fingerId;
	if(result)
	{
		//将数组中的指定元素删除
		for(var i=0; i<fingerIdArray.length; i++)
		{
			fingerId = eval(fingerIdArray[i]);
			if(fingerId >= DURESS_FINGER_NUM)
			{
				fingerId = fingerId - DURESS_FINGER_NUM;
				if(fingerId == fpIdNum)
				{
					//fingerIdArray.remove(i);
					//templateDataArray.remove(i);
					removeItem(fingerIdArray, i);
					removeItem(templateDataArray, i);
				}
			}
			else
			{
				if(fingerId == fpIdNum)
				{
					//fingerIdArray.remove(i);
					//templateDataArray.remove(i);
					removeItem(fingerIdArray, i);
					removeItem(templateDataArray, i);
				}
			}
		}
		if(browserFlag == "simple")
		{
			document.getElementById("finger" + fingerId).checked = false;
		}
		else if(browserFlag == "html5")
		{
			//将手指颜色改变--重画时也要判断
			context.fillStyle = bgColor;
			context.fill();
			if(lastFPIdNum != null && lastFPIdNum != lastFPIdNum)
			{
				//消除原来手指的颜色
				renderAfterColl(globalContext, lastFPIdNum, bgColor, false);
			}
			//消除需要删除的手指颜色
			renderAfterColl(globalContext, fpIdNum, bgColor, false);
		}
		fpModifyFlag = true;
		$("#duressFinger").attr("disabled", false);
		$("#submitButtonId").attr("disabled", false);
	}
	else
	{
		if(browserFlag == "simple")
		{
			document.getElementById("finger" + fpIdNum).checked = true
			collectFlag = true;
		}
		else if(browserFlag == "html5")
		{
			//消除原来手指的颜色--有问题，如果原来手指和现在的一样，有问题
			//renderAfterColl(globalContext, lastFPIdNum, bgColor, false);
		}
	}
}

/**
 * 删除数组元素 -- 从dx下标开始，删除一个元素
 * @author wenxin
 * @create 2013-05-15 11:11:31 am
 * @param dx 要删除元素的下标
 */
function removeItem(array, dx)
{  
   array.splice(dx, 1);
}

/**
 * 指纹验证
 * @author wenxin
 * @create 2013-06-21 11:09:20 am
 * @param title 页面标题国际化内容
 * @param isDriverInstall 是否安装了驱动
 * @param downloadPrompt 提示安装驱动国际化内容
 */
function fpVerification(title, downloadPrompt, isDriverInstall,context)
{
	//安装驱动
	if(isDriverInstall)
	{
		//支持html5
		if(typeof(Worker) != "undefined")
		{
			//createWindow('base_baseFPVerify.do?random=' + getRandomNum() + '^0^0^465^320^'+title);
			var comparisonDiv=document.getElementById("comparisonDiv");
		    var bg=document.getElementById("bg");
		    comparisonDiv.style.display="block";//显示内容层，显示覆盖层
		    comparisonDiv.style.left=parseInt((document.documentElement.scrollWidth-comparisonDiv.offsetWidth)/2)+document.documentElement.scrollLeft+"px";
		    comparisonDiv.style.top=Math.abs(parseInt((document.documentElement.clientHeight-comparisonDiv.offsetHeight)/2))+document.documentElement.scrollTop+"px"; //为内容层设置位置
		 	bg.style.display="block";
		 	bg.style.height=document.documentElement.scrollHeight+"px"; 
		 	isComp= true;
		 	//开始采集
		 	//beginCapture(context);
		 	dataInitComp();
		    //关闭页面前，取消采集
			//cancelCaptureBeforeClose("html5");
		}
		else
		{
			createWindow('base_baseFPVerifySimple.do?random=' + getRandomNum() + '^0^0^465^320^' + title);
		    //关闭页面前，取消采集
			//cancelCaptureBeforeClose("simple");
		}
	}
	else
	{
		alert(downloadPrompt);
		//messageBox({messageType: "alert", title: "提示", text: downloadPrompt});
	}
}

/**
 * 画布文本自动换行
 * @author chenpf
 * @create 2015-03-10 16:56:31 pm
 * @param context 2d画布上下文
 * @param text  显示信息内容
 * @param CWidth 画布宽度
 * @param x 文本X坐标值
 * 
 */
function autoWordBreak(context,text,CWidth,x){
	context.clear();
	var rownum = CWidth / 10;
	var len = strlen(text);
	if (rownum > len)
	{
		context.fillText(text, x, 30);
	}
	else
	{
		var endInd = rownum<text.length?rownum:text.length;
		var beginInd = 0;
		var endTemp=0;
		for (var i = 0; i <= text.length / rownum; i++)
		{
			endTemp = text.substr(beginInd, endInd).lastIndexOf(" ");
			if(endTemp!=-1)
				endInd=beginInd+endTemp;
			context.fillText(text.substr(beginInd, endInd), x, (i + 1) * 30);
			beginInd = endInd+1;
			if(beginInd>=text.length)
				break;
			endInd = beginInd + rownum;
		}
	}
}


//清除画布内容
CanvasRenderingContext2D.prototype.clear =
	  CanvasRenderingContext2D.prototype.clear || function (preserveTransform) {
	    if (preserveTransform) {
	      this.save();
	      this.setTransform(1, 0, 0, 1, 0, 0);
	    }
	 
	    this.clearRect(0, 0, this.canvas.width, this.canvas.height);
	 
	    if (preserveTransform) {
	      this.restore();
	    }          
	};
	
	/**
	 * 关闭页面前，如果正在进行验证，则先取消采集
	 * @author wenxin
	 * @create 2013-06-24 19:57:11 pm
	 * @param browserFlag 浏览器标记 simple:简易版本，表示是ie浏览器；html5:表示支持html5的浏览器
	 */
	function cancelCaptureBeforeClose(browserFlag)
	{
		 //关闭页面时，监听关闭的onclick事件
		getCurrentWindow().button("close").attachEvent("onClick", function()
		{
			clearTimeout(verifyTimer);
			if(browserFlag == "html5")
			{
				//正在进行验证，还没有关闭指纹采集
				if(verifyFlag)
				{
					//取消采集
					cancelCapture();
				}
				//关闭页面
				closeWindow();
			}
			else if(browserFlag == "simple")
			{
				//alert("cancel capture before close window!");
				//将定时器的递归调用关闭
				clearTimeout(timer);
				//取消采集 
				cancelRegister();
				//此处应该在取消结束后，再关闭窗口
				closeWindow();
			}
		});
	}
	
	/**
	 * 表单提交
	 * @author wenxin
	 * @create 2013-08-05 15:19:11 pm
	 */
	function formSubmit(id)
	{
		
		$('#'+id).serialize();
		$('#'+id).ajaxForm(function(data){
			callBackFormSubmit(data);
		});
		$('#'+id).submit(); //表单提交。
	}

     /**
 * @Description 生成随机数
 * @Author: wenxin
 * @Date: 2014-04-15
 */
function getRandomNum() 
{
    var random = parseInt(Math.random() * 10000);
    return random;
}
/**
 * 对特殊字符进行转义(+、&、%)
 * @author wenxin
 * @create 2013-08-05 17:20:31 pm
 * @param obj 需要转义的字符
 */
function transferredMeaning(src)
{
	src = src.replace(/\+/g, "%2B");
	src = src.replace(/&/g, "%26");
	src = src.replace(/\%/g, "%25");
	src = src.replace(/\//g, "%2F");
	src = src.replace(/\?/g, "%3F");
	src = src.replace(/\#/g, "%23");
	src = src.replace(/\=/g, "%3D");
	src = src.replace(/\ /g, "%20");
	return src;
}
/**
 * @Description: 计算字符串长度(可同时字母和汉字，字母占一个字符，汉字占2个字符)
 * @Author: ob.huang 黄玲彬
 * @Modified By:
 * @Date: 2013-09-24
 * @param: 
 */
function strlen(str)
{  
    var len = 0;  
    if(str != null)
    {
   		for (var i=0; i<str.length; i++)
    	{   
			var c = str.charCodeAt(i);
			if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) 
			{
				len++;   
			}	
			else 
			{
				len+=2;   
			}
    	}
    }
    return len;
}  

var myCombos = new Array();

/**
 * @Description: 下面自定义dhtmlx的ajax错误提示（重写）
 * @Author: lynn.chen  陈立  
 * @Modified By:
 * @Date: 2013-01-23
 * @param: dhtmlx源码参数
 */ 
if(typeof(_dhtmlxError) != "undefined")
{
	_dhtmlxError.prototype.throwError = function(type, name, params){
		//alert(type + "---" + name + "---" + params[1]);
		//alert(type + "---" + name + "---" + params[0].status);
		if(typeof(params) != "undefined")
		{
			var status = params[0].status;
			dealAjaxError(status);
		}
		return null;
	};
}

/**
 * @Description: 此方法为表单操作页面如何显示的方法，其它需要弹出框的地方，请使用createWindow()方法
 * @Author: lynn.chen  陈立  
 * @Modified By:
 * @Date: 2013-01-23
 * @param: paramStr 打开页面或者弹出框的参数
 */ 
var winPath = "";//当前窗口界面的路径
function openWindow(paramStr, isWindow) {
	//persCustomFieldAction!getAllField.action^900^430^新增人员
	var params = paramStr.split("^");//获取参数值
	winPath = params[0];
	
	if(typeof(isWindow) != "undefined" && isWindow)
	{
		if(isWindow == "yes")
		{
		    createWindow(paramStr);
		}
		else
		{
			getAddTemplate(params[0]);
		}
		
	}
	else
	{
		if(system.isFormByWinOpen)
		{
		    createWindow(paramStr);
		}
		else
		{
			getAddTemplate(params[0],"");
		}
	}
}

/**
 * 创建顶层Top窗口，适用于iframe中需要将窗口弹在最外层时
 * 
 * @author lynn.chen
 * @since 2015年5月14日 下午1:51:05
 */
function createTopWindow(paramStr, html, confirmFun)
{
	//如果为iframe弹窗的话则调用top弹窗，显示在外层
	if(typeof(window.top.createWindow) != "undefined")
	{
		window.top.createWindow(paramStr, html, confirmFun);
	}
	else
	{
		createWindow(paramStr, html, confirmFun);
	}
}

/**
 * @Description: 创建窗口，窗口个数不受限制
 * @Author: lynn.chen  陈立  
 * @Modified By:
 * @Date: 2013-01-23
 * @param: paramStr 弹出框的参数(path^x^y^w^h^title-----persCustomFieldAction!getAllField.action^0^0^900^430^导入)
 */ 
var fixedCWinId = "userWin";
var dhxWins;
var newWin;
function createWindow(paramStr, html, confirmFun)
{
	paramStr = paramStr.replace("#","public_winTemplate.action");
	if(!dhxWins)
	{
		dhxWins = new dhtmlXWindows();
		//dhxWins.enableAutoViewport(false);
		//dhxWins.attachViewportTo(document.body);
		dhxWins.zIndexStep = 15;   //grid分栏后，左边的z-index=11  --zhangc 2014-6-13
		//dhxWins.setSkin((typeof(sysCfg) != "undefined" ? sysCfg.dhxSkin : "dhx_skyblue"));
		//dhxWins.setImagePath("/public/controls/dhtmlx/dhtmlxWindows/codebase/imgs/");
		dhxWins.attachEvent("onClose", function(win){
			
			
			//此函数为窗口关闭之后，如何存在下一级的窗口，则对下一级窗口进行屏蔽处理
			//win.setModal(false); //---将会导致下一个弹出框z-index每次增加zIndexStep(50),而当前窗口会关闭，没必要先屏蔽，lynn.chen 陈立
			
			/*窗口关闭回调函数
			 * getCurrentWindow().onClose = function(){
				return false;//返回false不关闭窗口
			};*/
			if($(".dhtmlx_window_active").size() == 0)
			{
				//$(window.top.document.body).removeClass("dhxwins_vp_dhx_web");
			}
			if(win.onClose)
			{
				var ret = win.onClose();
				if(ret == false)
				{
					return;
				}
			}
			
			if(win.onFormClose)//表单关闭回调函数
			{
				var ret = win.onFormClose();
				if(ret == false)
				{
					return;
				}
			}
			
			try
			{
				//销毁pagingToolbar
				$(".dhtmlx_window_active .gridbox").each(function(index,element){
					if(document.getElementById(this.id) && mygrids[this.id] && mygrids[this.id].pagingToolbar)
					{
						mygrids[this.id].pagingToolbar.unload();
					}
				});
				
				//判断函数destroyComboxTree是否存在；如果存在则执行
				if(typeof(destroyComboxTree) == "function")
				{
					destroyComboxTree();
				}
				
				$("div[class*='dhtmlxcalendar_in_input']").hide();//时间控件隐藏
				
				$("div[class^='dhx_popup_dhx_']").hide();//popup提示控件隐藏
				
			}
			catch(e)
			{
				//alert("not function);
			}
			
			var lastId = 0;
			dhxWins.forEachWindow(function(){
        		lastId++;
    		});
			
			win.setModal(false);//是否进行屏蔽
			if(lastId != 1)
			{
				var id = fixedCWinId + (lastId-1);
				newWin = dhxWins.window(id);
				newWin.setModal(true);//是否进行屏蔽
				/*var newWinZIndex = $(newWin).css("z-index") - dhxWins.zIndexStep;
				$(newWin).css("z-index", newWinZIndex);
				$(".dhx_modal_cover_ifr").css("z-index", newWinZIndex -2);
				$(".dhx_modal_cover_dv").css("z-index", newWinZIndex -2);*/
			}
			else
			{
				//视频ocx控件显示
				$("iframe[src*='.action']:eq(0)").contents().find(".current").css("visibility", "visible");
			}
			window.setTimeout(function(){
				$(".dhxwin_active input[type=text][readonly!=readonly][disabled!='disabled'],.dhxwin_active input[type=password][readonly!=readonly][disabled!='disabled']").eq(0).focus();
			}, 300);
			return true;
    	});
		//document.onkeydown=esckeydown;   //esc键关闭弹出框口
	}
	var title = "";
	var idPrefix = 0;
	dhxWins.forEachWindow(function() {
        idPrefix++;
        var oldId = fixedCWinId + idPrefix;
        getCurrentWindow().setModal(false);//是否进行屏蔽
    });
	idPrefix++;//每次进来就来原有的窗口个数下加1
	var id = fixedCWinId +idPrefix;
	//globalCWinId = createWinId ? createWinId : fixedCWinId;
	//path^x^y^w^h^title(persCustomFieldAction!getAllField.action^0^0^900^430^导入)
	var params = paramStr.split("^");//获取参数值
    newWin = dhxWins.createWindow(id, params[1]?params[1] : 0, params[2]?params[2] : 0, params[3]?params[3] : 750, params[4] ? params[4] : 300);
    newWin.setText(params[5]? params[5] : title);
    //添加“正在加载中……”功能
	var loadingHtml = "<div id='progressWin" + id + "' style='float: left;position: absolute;z-index: 999999999;height:100%; width:100%; background:#F4F7FF;text-align:center;'><img src='public/controls/dhtmlx/dhtmlxLayout/codebase/imgs/dhxlayout_dhx_web/dhxlayout_progress.gif' style='margin-top:"+ (params[4]/2-60) +"px'/></div>";
	newWin.attachHTMLString(loadingHtml);
	if(!params[1] && !params[2])
	{
    	newWin.center();
    }
	else if(params[1] == 0 && params[2] == 0)
	{
    	newWin.center();
    }
    newWin.setModal(true);//是否进行屏蔽
	newWin.denyResize();//是否可以放大
	newWin.button("park").hide();
	newWin.button("minmax1").hide();
	
	$(".dhxwin_button_close").attr("title", "${common_close}");
	
	newWin.bringToTop();
	newWin.path = params[0];
	//newWin.hideHeader();
	
	//视频ocx控件隐藏，避免其覆盖弹出框
	$("iframe[src*='.action']:eq(0)").contents().find(".current").css("visibility", "hidden");
	
	if(params[0] == "*")//表示直接使用html
	{
		if(html)
		{
			newWin.attachHTMLString(html);
		}
	}
	else
	{
		//添加“正在加载中……”功能
		var xmlHttpObj = createXMLHttpReuestObject();
		var url = params[0];
		
		var unParm = url.indexOf("?") != -1 ? "&" : "?";
		xmlHttpObj.open("get",url + unParm + "un=" + Math.round(Math.random()*100000));
		xmlHttpObj.url = url;
		xmlHttpObj.win = newWin;
		xmlHttpObj.onload = function(a){
			var status = 200;
			try
			{
				status = this.status;
			}
			catch(e)
			{
				
				status = 0;
			}
			try
			{
				if((this.readyState == 4 && status == 200) || status == 404 || status == 500)
				{
					var responseText = this.responseText;
					try
					{	//添加对返回结果的判断是否异常结果
						var tempText = this.responseText.substring(0,20);
						if(tempText.indexOf("ret") != -1 && (tempText.indexOf(sysCfg.warning) != -1 || tempText.indexOf(sysCfg.error) != -1))
						{
						    //格式化返回的字符为对象
							var responseJson = eval("("+this.responseText+")");
							if(responseJson.ret == 500)
							{
								messageBox({messageType:"alert", text:responseJson.msg,callback:closeWindow});
							}
							return;
						}
					}
					catch(e)
					{
		                
					}
					var repText = "<span></span>";
	                var text = (html ? html : "");
	                if(responseText.indexOf(repText) != -1)
	                {
	                    responseText = responseText.replace("<span></span>", text);
	                }
	                else
	                {
	                    responseText += text;
	                }
	                responseText = responseText.replace("confirmFun",confirmFun);
	                this.win.attachHTMLString(loadingHtml + responseText);
				}				
			}
			catch(e)
			{
				status = status;
				throw e;
			}
			finally
			{
				//去除转圈处理和获取焦点
				window.setTimeout(function(){
					$("#progressWin" + id).remove();
					$(".dhxwin_active input[type=text][readonly!=readonly][disabled!='disabled'],.dhxwin_active input[type=password][readonly!=readonly][disabled!='disabled']").eq(0).focus();
				}, 300);
				if(typeof(dealAjaxError) != "undefined")
				{
					status = status == 0 ? "${REQUEST_lOGIN_TIME_OUT!201}" : status;
					dealAjaxError(status);
				}
			}
			
		}
		/*
		xmlHttpObj.onreadystatechange = function()
		{
			alert("b");
			
		}*/
		xmlHttpObj.send(null);
	}
	//return newWin;
	
}
/**
 * @Description: 根据不同浏览器，动态创建XMLHttpRequest对象
 * @Author: 何朗 helang  
 * @Date: 2013-04-18
 * @return 创建成功的XMLHttpRequest对象
 */
function createXMLHttpReuestObject()
{
	var XMLHttpFactories = [
		function(){return new XMLHttpRequest()},
		function(){return new ActiveXObject("Msxml2.XMLHTTP")},
		function(){return new ActiveXObject("Msxml3.XMLHTTP")},
		function(){return new ActiveXObject("Microsoft.XMLHTTP")}
	];	
	var xmlHttp = false;
	for(var i=0; i<XMLHttpFactories.length; i++)
	{
		try
		{
			xmlHttp = XMLHttpFactories[i]();
		}
		catch(e)
		{
			continue;
		}
		break;
	}
	return xmlHttp;
}

/**
 * @Description: 修改窗口的属性
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-01-23
 * @param: width 宽
 * @param: height 高
 * @param: text 标题文本
 */ 
function updateWindow(width, height, text, type)
{
	var idPrefix = 0;
	dhxWins.forEachWindow(function() {
        idPrefix++;
    });
	var id = fixedCWinId + (idPrefix);
	var ob = dhxWins.window(id).getDimension();
	if(type == "+")
	{
		dhxWins.window(id).setDimension(ob[0]+width, ob[1]+height);
	}
	else if(type == "-")
	{
		dhxWins.window(id).setDimension(ob[0]-width, ob[1]-height);
	}
	else
	{
		dhxWins.window(id).setDimension(width, height);
		dhxWins.window(id).center();
	}
	
	if(text)
	{
		dhxWins.window(id).setText(text);	
	}
	$(".dhxwin_active .content_div").height(0);
	$(".dhxwin_active .content_div").height($(".dhxwin_active .content_td").height());
}
/**
 * @Description: 获取当前窗口对象
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-02-25
 */

function getCurrentWindow()
{
	var idPrefix = 0;
	dhxWins.forEachWindow(function() {
        idPrefix++;
    });
	var id = fixedCWinId + idPrefix;
	return dhxWins.window(id);
}

/**
 * @Description: 获取前某个打开的窗口对象
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-02-25
 */

function getPreWindow(index)
{
	var idPrefix = 0;
	dhxWins.forEachWindow(function() {
        idPrefix++;
    });
	var id = fixedCWinId + (idPrefix-index);
	return dhxWins.window(id);
}

/**
 * 得到第一个窗口对象
 * 
 * @author lynn.chen
 * @since 2014年12月17日 下午5:45:53
 * @returns dhxWins.window(id)
 */
function getFirstWindow()
{
	var idPrefix = 1;
	if(typeof(dhxWins) == "undefined")
	{
		return null;
	}
	return dhxWins.window(fixedCWinId + idPrefix);
}

/**
 * 刷新之前的某个window
 * @param index 
 */
function refreshPreWindow(index)
{
	var preWindow = getPreWindow(index);
	if(preWindow)
	{
		$.get(preWindow.path, function(result) {
			 preWindow.attachHTMLString(result);
		}, "html");
	}
}

/**
 * @Description: 刷新窗口，自动获取最前面的窗口进行刷新
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-01-23
 */
function refreshCurrentWindow()
{
	if(system.isFormByWinOpen && dhxWins)
	{
		var currentWindow = getCurrentWindow();
		$.get(currentWindow.path, function(result) {
			currentWindow.attachHTMLString(result)
		}, "html");
	}
	else
	{
		setIdHtmlByPath(winPath, "addBox");
	}
	
}

/**
 * @Description: 关闭窗口，自动获取最前面的窗口进行关闭
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-01-23
 */
function closeWindow()
{
	if(system.isFormByWinOpen && dhxWins)
	{
		if(getCurrentWindow() != null){
			getCurrentWindow().close();
		}
		else
		{
			$("#listBox").show();
			$("#addBox").html("");
			$("#addBox").hide();
		}
	}
	else
	{
		$("#listBox").show();
		$("#addBox").html("");
		$("#addBox").hide();
	}
	
}

/**
 * @Description: 打开公共模版的进度条
 * @Author: lynn.chen 陈立
 * @Modified By:
 * @Date: 2013-06-15
 * @param: openProgressParams 窗体属性设置，参数前面加*的为必填项
 * 		var	openProgressParams =
			{
				"winTitle" : "处理进度",//窗口标题，默认为:处理进度
			*	"dealDataPath" : "dealDataPath.action?id=1,2,3",
				"getProgressPath" : "processAction!getProcess.action",
			*	"singleMode" : true/false,//true:为单进度条模式，false:双进度条模式
				"currentProgressTitle" : "当前设备进度",
				"totalProgressTitle" : "总体进度",
				"callback" : function(){//进度完成时的回调函数
					alert("进度已完成");
				}
			};
 * @param: selectFun 如果操作后右边选择框存在数据触发的函数
 * @param: noselectFun 如果操作后右边选择框不存在数据触发的函数
 * 说明：var selectedOptions = myForms[objId].getSelect("c_blocked").options;是获取其值的用法
 */
var openProgressParams = null;//全局进度处理参数
function openProgress(progressParams)
{
	openProgressParams = progressParams;
	//如果获取进度路径不存在，默认为：processAction!getProcess.action
	var getProgressPath = openProgressParams.getProgressPath;
	openProgressParams.getProgressPath = (!getProgressPath ||getProgressPath == null || getProgressPath == "" ? "processAction!getProcess.action" : getProgressPath);
	
	var winTitle = openProgressParams.winTitle ? openProgressParams.winTitle : "${common_op_deal}";//${common_op_deal}:处理进度
	var winParam = "public_opTemplate.action^0^0^600^285^" + winTitle;
	if(openProgressParams.singleMode == true)//这里主要是窗口高度不一样
	{
		winParam = "public_opTemplate.action^0^0^600^245^" + winTitle;
	}
	createWindow(winParam);
}

/**
 * @Description: 左右选择框控件函数
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-01-23
 * @param: leftOptions 左边需要进行备选的数据列，格式如：[{"value":"0","text":"人员编号"},{"value":"1","text":"姓名"},{"value":"2","text":"卡号"}]
 * @param: rightOptions 右边需要进行备选的数据列，格式如：[{"value":"0","text":"人员编号"},{"value":"1","text":"姓名"},{"value":"2","text":"卡号"}]
 * @param: obId 该窗体显示时相应对象的id,也就是它该在哪显示
 * @param: attributes 窗体属性设置
 * 			var attributes = {
				"leftLabel":"备选数据列",
				"rightLabel":"备选数据列",
				"size": 12,
				"buttonWidth" : 50,
				"isAllSelect" : true,
				"inputWidth" : 160,
				"labelWidth" : 160,
				"labelHeight" : 20
			};
 * @param: selectFun 如果操作后右边选择框存在数据触发的函数
 * @param: noselectFun 如果操作后右边选择框不存在数据触发的函数
 * 说明：var selectedOptions = myForms[objId].getSelect("c_blocked").options;是获取其值的用法
 */

var myForms = new Array();
function leftRightSelect(leftOptions, rightOptions, objId, attributes, selectFunc, noselectFunc)
{
	if (myForms[objId] && $("#" + objId + " select").size() > 0)
	{
		return;
	}
	else if(myForms[objId])
	{
		myForms[objId].unload();//清空之前的myForm，否则会new多次，出现多个
	}
	
	attributes = attributes? attributes : new Array();
	var size= attributes.size ? attributes.size : 22;
	var buttonWidth = attributes.buttonWidth ? attributes.buttonWidth : 50;
	buttonWidth = buttonWidth > 60 ? 60 : buttonWidth;
	var inputWidth = attributes.inputWidth? attributes.inputWidth : 210;
	var labelWidth = inputWidth;
	var labelHeight = attributes.labelHeight ? attributes.labelHeight : 20;
	var leftLabel = attributes.leftLabel ? attributes.leftLabel : "备选人员";
	var rightLabel = attributes.rightLabel ? attributes.rightLabel : "已选人员";
	
	if(attributes.isAllSelect == undefined)
	{
		var isAllSelect = true;
	}
	else
	{
		var isAllSelect = attributes.isAllSelect;
	}
	
	var allOffsetTop = size*6;
	var offsetTop = isAllSelect ? 0 : size*8;
	var allButtonType = isAllSelect ? "button" : "hidden";
	formData = [ 
		{
			type: "settings",
			position: "label-top",
			labelWidth: labelWidth,
			inputWidth: inputWidth,
			labelHeight: labelHeight
		}, 
		{
			type: "multiselect",
			label: leftLabel,
			name: "c_all",
			size: size,
			options: (leftOptions != null ? eval(leftOptions) : [])
		}, 
		{
			type: "newcolumn"
		}, 
		{
			type: "block",
			inputWidth:"auto",
			offsetLeft: 0,
			list: [{
				type: allButtonType,
				name: "addAll",
				value: ">>",
				offsetLeft: 0,//(60-buttonWidth)/2,
				offsetTop: allOffsetTop,
				inputWidth: buttonWidth
			}, {
				type: "button",
				name: "add",
				value: "&nbsp;>&nbsp;",
				offsetLeft: 0,//(60-buttonWidth)/2,
				offsetTop: offsetTop,
				inputWidth: buttonWidth
			}, {
				type: "button",
				name: "remove",
				value: "&nbsp;<&nbsp;",
				offsetLeft: 0,//(60-buttonWidth)/2,
				inputWidth : buttonWidth
			}, {
				type: allButtonType,
				name: "removeAll",
				value: "<<",
				offsetLeft: 0,//(60-buttonWidth)/2,
				inputWidth: buttonWidth
			}]
		}, 
		{
			type: "newcolumn"
		}, 
		{
			type: "multiselect",
			label: rightLabel,
			name: "c_blocked",
			size: size,
			options: (rightOptions != null ? eval(rightOptions) : [])
		} 
	];
	myForms[objId] = new dhtmlXForm(objId, formData);
	
	//按钮部分样式修复
	$("#"+ objId +" .in_block").css("padding", "0 5px");
	$("#"+ objId +" .dhxform_btn_txt").css("margin", "0 10px");
	//下面2句是解决ie下已下拉框显示的问题
	/* 暂时屏蔽，Occupancy有问题    $.browser.msie *******梁海波-20140410********* */
	//myForms[objId].getSelect("c_blocked").options.add(new Option("0" , ""));
	//$(myForms[objId].getSelect("c_blocked")).find('option').filter(':eq(0)').remove();
	myForms[objId].attachEvent("onButtonClick", function(name){
		if (name == "add" || name == "remove" || name == "addAll" || name == "removeAll") 
		{
			changeContactState(objId, name == "add" || name == "addAll", name, isAllSelect, selectFunc, noselectFunc);
		}
	});

	//双击
	$(".dhxform_select[name^='c_all']").dblclick(function(){
		changeContactState(objId, true, "add", isAllSelect,selectFunc, noselectFunc);
	});

	$(".dhxform_select[name^='c_blocked']").dblclick(function(){
		changeContactState(objId, false, "remove", isAllSelect, selectFunc, noselectFunc);
	});
	
	//获取当前值
	myForms[objId].getValue = function(){
		var optValue = "";
		var selectedOptions = myForms[objId].getSelect("c_blocked").options;
		for ( var i = 0; i < selectedOptions.length; i++) 
		{
			optValue += selectedOptions[i].value + ",";
		}
		return optValue != "" ? optValue.substring(0,optValue.length-1) : optValue;
	};
}

function changeContactState(objId, block, name, isAllSelect, selectFunc, noselectFunc) 
{
	var ida = (block ? "c_all" : "c_blocked");
	var idb = (block ? "c_blocked" : "c_all");

	var sa = myForms[objId].getSelect(ida);
	var sb = myForms[objId].getSelect(idb);

	if(name == "addAll" || name == "removeAll") 
	{
		for ( var i = 0; i < sa.options.length; i++) 
		{
			sb.options.add(new Option(sa.options[i].text, sa.options[i].value));

		}
		for ( var i = 0; i < sa.options.length;) 
		{
			$(sa).find('option').filter(':eq(' + i + ')').remove();
		}
	} 
	else 
	{
		var t = myForms[objId].getItemValue(ida);
		if(t.length == 0)
		{
			return;
		}
		eval("var k={'" + t.join(":true,") + "':true};");
		var w = 0;
		var ind = -1;
		while(w < sa.options.length) 
		{
			if(k[sa.options[w].value]) 
			{
				sb.options.add(new Option(sa.options[w].text,
						sa.options[w].value));
				$(sa).find('option').filter(':eq(' + w + ')').remove();
				ind = w;
			}
			else
			{
				w++;
			}
		}
		if(sa.options.length > 0 && ind >= 0)
		{
			if(sa.options.length > 0)
			{
				sa.options[t.length > 1 ? 0 : Math.min(ind,
						sa.options.length - 1)].selected = true;
			}
		}

		//单选
		if(isAllSelect == false && block == true && sb.options.length > 1)
		{
			sa.options.add(new Option(sb.options[0].text, sb.options[0].value));
			$(sb).find('option').filter(':eq(0)').remove();
		}
	}
	
	//对已选数据列进行判断，是否已选数据
	if(myForms[objId].getSelect("c_blocked").options.length > 0)
	{
		if(selectFunc)
		{
			selectFunc();
		}
	}
	else
	{
		if(noselectFunc)
		{
			noselectFunc();
		}
	}
}

/**
 * @Description: 打开表单页面，只适用于操作栏中进行调用
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-03-06
 * @param: id 操作时的参数，
 * 形如persCustomFieldAction!getAllField.action^0^0^900^430为path^x^y^w^h^title
 */
function openWindowToForm(id)
{
	var objId = "operate";
	var winStr = id;
	if(id.split("^")[0].indexOf("]") > 0)
	{
		objId = id.split("^")[0].substring(1,id.split("^")[0].indexOf("]"));
		winStr = id.substring(id.split("^")[0].indexOf("]")+1);
	}
	if(operateToolbars[objId])
	{
		try
		{
			var isExistId = false;
			operateToolbars[objId].forEachItem( function(itemId){
		        if(itemId == id)
				{
					isExistId = true;
					return;
				}
		    });
			if(isExistId)
			{
				winStr += "^"+operateToolbars[objId].getItemText(id);
			}
			else
			{
				winStr += "^"+operateToolbars[objId].getListOptionText("moreActions",id);
			}
		}
		catch(e)
		{
			winStr += "^"+operateToolbars["leftOperate"].getItemText(id);
		}
	}
	openWindow(winStr);
}

/**
 * @Description: 专门为导出提供，弹出窗口
 * @Author: pokiz.xu
 * id 的格式为accTransactionAction!export.action(type=XLS|PDF)^0^0^500^190#rightGridbox#leftOperate
 * 				    ^前面的为导出的action路径，#后面接着哪个grid，与哪个操作区域
 * 					#后面的可以不填，如果填，需要两个一起填写
 *                  (type=...)为导出页面显示的导出格式
 * @Date: 2013-03-06
 * @modify: gordon.zhang@zkteco.com 2014-12-18
 */
function openWindowForExport(id)
{
	var winStr;
	var objId = "operate";
	var gridName;
	var sourceId = id;
	if(id.indexOf("#") >= 1)
	{
		gridName = id.substring(id.indexOf("#")+1, id.lastIndexOf("#"));
		objId = id.substring(id.lastIndexOf("#") + 1, id.length);
		id = id.substring(0, id.indexOf("#"));
	}
	var typeStr="";
	if (id.indexOf("(type=")>=1)
	{
	    var typeBegini = id.indexOf("(type=");
	    var typeEndi = id.substring(typeBegini).indexOf(")");
	    typeStr = id.substring(typeBegini,typeBegini+typeEndi);
	    typeStr = typeStr.split("=")[1];
	    id = id.substring(0,typeBegini)+id.substring(typeBegini+typeEndi+1);
	}
	var opStr;
	if(id.indexOf("(select") > 0)
	{
		var opParamStr = id.substring(id.indexOf("(select") + "(select".length + 1, id.lastIndexOf(")"));
		var val = $("input:hidden[name='"+opParamStr+"']").val();
		opStr = val;
	}
	var winHtml = "";
	if(operateToolbars[objId])
	{
		winHtml = operateToolbars[objId].getItemText(sourceId);
	}
	else
	{
		winHtml = "${common_op_export}";
	}
	winStr = "^"+winHtml;
	var actionName = id.split("^")[0];
	var size = id.substring(id.indexOf("^"));
	var actionInput = "<input style='display: none' id='actionName' value='"+ actionName +"'/>";
	var gridNameInput = "<input style='display: none' id='gridName' value='"+ gridName +"'/>";
	var custom = "<input style='display: none' id='cutomName' value='"+ opStr +"'/>";
	var typeStr = "<input style='display: none' id='typeStr' value='"+ typeStr +"'/>";
	createWindow("public_opExportRecord.action" + size +winStr, actionInput + gridNameInput + custom+typeStr);
}

/**
 * @Description: 已打开窗口的方式来进行操作，只适用于操作栏中进行调用
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-03-06
 * @param: id 操作时的参数，
 * 形如persCustomFieldAction!getAllField.action^0^0^900^430为path^x^y^w^h^title
 * 下面为需在弹窗前进行进行判断的配置
 * persCustomFieldAction!getAllField.action?
 * id=(id*{checkboxName:'ids',selectedNum:1,conditionMode:'!=',noSelectPrompt:'请选择你要操作的对象',noRightSelectPrompt:'只能选择一个对象进行操作',split:','})
 * ^0^0^900^430
 */
var currentOperateId;
var operateObjId = "operate";
function openWindowByOperate(id)
{
	var winStr = id;
	if(id.split("^")[0].indexOf("]") > 0)
	{
		operateObjId = id.split("^")[0].substring(1,id.split("^")[0].indexOf("]"));
		winStr = id.substring(id.split("^")[0].indexOf("]")+1);
	}
	else
	{
		operateObjId = "operate";
	}
	if(operateToolbars[operateObjId] && id.split("^").length > 6)
	{
		winStr += "^"+operateToolbars[operateObjId].getItemText(id);
	}

	if(id.indexOf("(id")>0)
	{

		var checkboxName;
		var idStr = "(id)";
		var selectedNum = 1;
		var conditionMode = "!=";
		var noSelectPrompt = "${common_prompt_selectObj}";
		var noRightSelectPrompt = "${common_prompt_onlySelectOneObject}";
		var split = ",";
		var idNum = 0;
		if(id.indexOf("(id*")>0)
		{

			//(id*{checkboxName:'ids',selectedNum:1,conditionMode:'!=',noSelectPrompt:'请选择你要操作的角色',noRightSelectPrompt:'只能选择一个操作的角色',split:','})
			var opParamStr = id.substring(id.indexOf("(id*") + "(id*".length, id.lastIndexOf(")"));
			var opParams = eval("(" + opParamStr + ")");
			checkboxName = opParams.checkboxName ? opParams.checkboxName : checkboxName;
			selectedNum = opParams.selectedNum != undefined ? opParams.selectedNum : selectedNum;
			conditionMode = opParams.conditionMode ? opParams.conditionMode : conditionMode;
			noSelectPrompt = opParams.noSelectPrompt ? opParams.noSelectPrompt : noSelectPrompt;
			noRightSelectPrompt = opParams.noRightSelectPrompt ? opParams.noRightSelectPrompt : noRightSelectPrompt;
			split = opParams.split ? opParams.split : split;
			idStr = id.substring(id.indexOf("(id*"), id.lastIndexOf(")")+1);
		}

		var checkboxVlaues = getGridCheckboxValue(checkboxName, split);
		if(checkboxVlaues == "")
		{
			messageBox({messageType:"alert",text: noSelectPrompt});
			return;
		}
		else
		{
			idNum = checkboxVlaues.split(split).length;

			switch (conditionMode)
			{
				case "!=":
					if(idNum != selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt.format(selectedNum)});
						return;
					}
					break;
				case "==":
					if(idNum == selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt.format(selectedNum)});
						return;
					}
					break;
				case ">":
					if(idNum > selectedNum)
					{
						//noRightSelectPrompt = "已选择{0}，最多只能选择{1}个对象进行操作";
						messageBox({messageType:"alert",text: noRightSelectPrompt.format(idNum,selectedNum)});
						return;
					}
					break;
				case ">=":
					if(idNum >= selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt.format(selectedNum)});
						return;
					}
					break;
				case "<":
					if(idNum < selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt.format(selectedNum)});
						return;
					}
					break;
				case "<=":
					if(idNum > selectedNum)
					{
						var msg = "${common_prompt_noPass}";
						messageBox({messageType:"alert",text:  msg.format(selectedNum)});
						return;
					}
					break;
				default:
					messageBox({messageType:"alert",text: "条件运算符不合法,必须为'!=,==,>,>=,<,<='"});
					return;
					break;
			}
		}
		winStr = id.replace(idStr, checkboxVlaues);
	}
	
	var isExistId = false;
	operateToolbars[operateObjId].forEachItem( function(itemId){
        if(itemId == id)
		{
			isExistId = true;
			return;
		}
    });
	if(isExistId)
	{
		winStr += "^"+operateToolbars[operateObjId].getItemText(id);
	}
	else
	{
		winStr += "^" + operateToolbars[operateObjId].getListOptionText("moreActions",id);
	}
	currentOperateId = id;
	createWindow(winStr);
}

/**
 * @Description: 非打开窗口的方式来进行操作，非操作栏中同样可以进行调用
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-03-06
 * @param: id 操作时的参数，
 * 形如persCustomFieldAction!getAllField.action为path
 * 下面为需在弹窗前进行进行判断的配置
 * [operateId]persCustomFieldAction!getAllField.action?
 * id=(id*{checkboxName:'ids',selectedNum:1,conditionMode:'!=',noSelectPrompt:'请选择你要操作的对象',noRightSelectPrompt:'只能选择一个对象进行操作',split:',',gridName: 'gridbox'})^#自定义提示信息
 *
 */
function executeOperate(id, callbackFun)
{
	var opStr = id.split("^")[0];
	if(id.indexOf("]") > 0)
	{
		operateObjId = id.substring(1,id.indexOf("]"));
		opStr = id.substring(id.indexOf("]")+1);
	}
	else
	{
		operateObjId = "operate";
	}
	
	var idNum = 0;
	var itemText = ""; 
	var gridName = null;
	if(id.indexOf("(id")>0)//判断是否是grid复选
	{
		var checkboxName;
		var idStr = "(id)";
		var selectedNum = 0;
		var conditionMode = "==";
		var noSelectPrompt = "${common_prompt_selectObj}";
		var noRightSelectPrompt = "${common_prompt_onlySelectOneObject}";
		var split = ",";
		var isOnline = false;
		if(id.indexOf("(id*")>0)//这里是判断是否存在提示信息参数
		{
			//(id*{checkboxName:'ids',selectedNum:1,conditionMode:'!=',noSelectPrompt:'请选择你要操作的角色',noRightSelectPrompt:'只能选择一个操作的角色',split:','})
			var opParamStr = id.substring(id.indexOf("(id*") + "(id*".length, id.lastIndexOf(")"));
			var opParams = eval("(" + opParamStr + ")");
			
			checkboxName = opParams.checkboxName ? opParams.checkboxName : checkboxName;
			
			selectedNum = opParams.selectedNum != undefined ? opParams.selectedNum : selectedNum;
			
			conditionMode = opParams.conditionMode ? opParams.conditionMode : conditionMode;
			noSelectPrompt = opParams.noSelectPrompt ? opParams.noSelectPrompt : noSelectPrompt;
			noRightSelectPrompt = opParams.noRightSelectPrompt ? opParams.noRightSelectPrompt : noRightSelectPrompt;
			isOnline = opParams.isOnline ? opParams.isOnline : isOnline ;
			split = opParams.split ? opParams.split : split;
			gridName = opParams.gridName ? opParams.gridName : gridName;

			idStr = id.substring(id.indexOf("(id*"), id.lastIndexOf(")")+1);
			
		}
		var checkboxVlaues = getGridCheckboxValue(checkboxName, split);
		if(checkboxVlaues == "")
		{
			messageBox({messageType:"alert",text: noSelectPrompt});
			return;
		}
		else
		{
			idNum = checkboxVlaues.split(split).length;

			switch (conditionMode)
			{
				case "!=":
					if(idNum != selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				case "==":
					if(idNum == selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				case ">":
					if(idNum > selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				case ">=":
					if(idNum >= selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				case "<":
					if(idNum < selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				case "<=":
					if(idNum >= selectedNum)
					{
						messageBox({messageType:"alert",text: noRightSelectPrompt});
						return;
					}
					break;
				default:
					messageBox({messageType:"alert",text: "条件运算符不合法"});
					return;
					break;
			}
		}
		opStr = opStr.replace(idStr, checkboxVlaues);
	}
	else if(id.indexOf("(select") > 0)
	{
		var opParamStr = id.substring(id.indexOf("(select") + "(select".length + 1, id.lastIndexOf(")"));
		var val = $("input:hidden[name='"+opParamStr+"']").val();
		opStr = id.substring(0, id.indexOf("custom=") + "custom=".length) + (val == undefined ? "" : val);
	}
	var msg = "${common_prompt_executeOperate}";//你确定要执行{0}操作吗？
	if(id.split("^").length >= 2)//判断是否存在自定义文本
	{
		itemText = id.split("^")[1];
		if(itemText.indexOf("#") >= 0)
		{
			msg = itemText.replace("#","");
		}
	}
	else if(operateToolbars[operateObjId])//判断是否是Toolbar中的操作
	{
		var isExistId = false;
		operateToolbars[operateObjId].forEachItem( function(itemId){
	        if(itemId == id)
			{
				isExistId = true;
				return;
			}
	    });
		if(isExistId)//判断当前操作栏对象中是否存在该id，存在则直接获取文本(通过id来获取文本)
		{
			itemText = operateToolbars[operateObjId].getItemText(id);
		}
		else//不存在时从下来框中去获取，下拉框id统一使用moreActions
		{
			itemText += "^" + operateToolbars[operateObjId].getListOptionText("moreActions",id);
		}
	}
	msg = msg.format(itemText);
	
	var submit = function (result) {
	    if(result)
	    {
	    	//这里判断该操作是否为ajax操作
	    	if(opStr.indexOf(".action") > 0 || opStr.indexOf(".do") > 0)
	    	{
	    		var urlStr=opStr;
	    		if(opStr.indexOf("?")!=-1){
	    			urlStr=opStr.substr(0,opStr.indexOf("?"));
	    		}
		    	onLoading(function(){
		        	$.ajax({
						type: "POST",
						url: urlStr,
						data:parseURL2JSON(opStr),
						dataType:"json",
						async : true,
						success: function(result)
					    {
		        			if(callbackFun)
		        			{
		        				dealRetResult(result,callbackFun);
		        			}
		        			else
		        			{
		        				dealRetResult(result, undefined, gridName);
		        			}
						 	
						}
			        });
	       		});
	    	}
	    	else//不是ajax则为执行函数
	    	{
	    		eval(opStr + '()');
	    	}
	    }
	    return true; //close
	};
	messageBox({messageType:"confirm", text:msg, callback:submit});
	
	currentOperateId = id;
}

/**
 * @Description: 根据解析xml来创建操作栏,在list模版页面进行调用
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-01-23
 * @param: pageConfigXML 需要解析的xml路径
 * @param: obId 用于显示对象的id，也就是一个页面可以出现多个，默认为list模版中的operate
 * @param: callback 加载完回调函数
 */
var operateToolbars = new Array();
function createToolbar(pageConfigXML, obj, callback)
{
	var obId = "operate";
	
	pageConfigXML += (pageConfigXML.indexOf("?") > 0 ? "&" : "?") + "rootType=toolbars";
	if(typeof(obj) == "undefined" || typeof(obj) == "string")//判断是否是对象id字符串
	{
		obId = (obj ? obj : obId);
		operateToolbars[obId] = new dhtmlXToolbarObject(obId);
		$("#" + obId).addClass("dhxToolbar");
	}
	else
	{
		obId = (obj.id ? obj.id : obId);
		operateToolbars[obId] = obj;
	}
	//operateToolbars[obId].setSkin(sysCfg.dhxSkin);
	
	operateToolbars[obId].attachEvent("onClick", function(id){
		/****
		this.objPull[this.idPrefix + "input_1"];
		inputA = Toolbar.objPull[Toolbar.idPrefix+"input_1"].obj.firstChild;*/
	});
	
	operateToolbars[obId].setIconSize(18);
	operateToolbars[obId].setIconsPath(sysCfg.rootPath + "/public/images/opToolbar/");
	var path = pageConfigXML + (pageConfigXML.indexOf("?") == -1 ? "?" : "&") + "un=" + new Date().getTime() + "&" + $.param($.ajaxSetup().data);
	
	operateToolbars[obId].loadXML(path, function(){
		var $dhxToolbar = $(this.base);
		
		if($dhxToolbar.size() == 1)
		{
			
			var toolbarWidth = 0;
			$dhxToolbar.find("div.dhx_toolbar_btn").each(function(i){
				toolbarWidth +=  $(this).outerWidth();
			});
			//$(".dhx_cell_cont_layout .dhxToolbar .dhxtoolbar_float_left div.dhx_toolbar_btn").
			
			var left = $dhxToolbar.offset().left + 30;
			if(left < 50 && $("iframe[src*='.action'],iframe[src*='.do']:eq(0)", parent.document).size() > 0)
			{
				left = $("iframe[src*='.action'],iframe[src*='.do']:eq(0)", parent.document).parent().offset().left + 40;
			}
			
			if((left + toolbarWidth) > $(window.top.document.body).width())
			{
				$(window.top.document.body).css("min-width",left + toolbarWidth);
				window.top.resizeWindow();
			}

		}
		if(typeof(callback) != "undefined")
		{
			callback();
		}
		
	});
}

/**
 * @Description: 获取操作栏真实节点id
 * @Author: lynn.chen 陈立
 * @Date: 2015-01-22
 * @param:toolbarId 操作栏对象div id
 * @param:itemId 操作栏节点id
 */
function getToolbarRealItemId(toolbarId, itemId)
{
	return operateToolbars[toolbarId].idPrefix + itemId;
}

/**
 * @Description: 封装dhmlxmessage消息提示框
 * @Author: helang 何朗
 * @Date: 2013-04-22
 * @param:paramsJson  传入的配置参数
  	  下面是关于paramsJson格式的说明及实例，每个属性都有默认值：
	  confirmBox({
	  	title:"提示信息",
	  	type:"confirm-error dhtmlx-myCss",(dhtmlxmessage内置的三种type:confirm-warning confirm confirm-error)
	  	ok:"确定",
	  	cancel:"取消",
	  	expire:3000,
	  	text:"你确定要删除此条信息吗?",
	  	messageType:"confirm",
	  	callback:function(result){alert(11);},
	  	width:250
	  });
	  title ok cancel text 分别对应消息提示框上相应的文本内容；
	  messageType指的是消息提示框类型（有confirm message alert三种）；
	  callback是回调方法；
	  特别注意下type属性，这里可以自己定义样式,指定多个样式以空格分隔开，自定义的类样式中属性必须加!important标识且自定义样式必须用dhtmlx-xxxx这种格式类名。
	  expire当组件的messageType为message的时候，设定message提示框多少毫秒消失,width指当消息提示框类型为message的时候，所显示的提示框的宽度，默认250。
 */
function messageBox(paramsJson)
{
	
	this.messageType = paramsJson.messageType ? $.trim(paramsJson.messageType) : "confirm";
	this.types = "";
  	if(paramsJson.type)
  	{
  		this.typeArray = paramsJson.type.split(" ");
	  	for(var i=0; i<this.typeArray.length; i++)
	  	{
  			this.types += this.typeArray[i]+" ";
	  	}
  	}
	switch(this.messageType)
	{
		case "confirm":
		  	this.title = paramsJson.title ? $.trim(paramsJson.title) : "${common_prompt_title}";
		  	this.type = this.types ? $.trim(this.types) : "confirm";
		  	this.ok = paramsJson.ok ? $.trim(paramsJson.ok) : "${common_edit_ok}";
		  	this.cancel = paramsJson.cancel ? $.trim(paramsJson.cancel) : "${common_edit_cancel}";
		  	this.text = paramsJson.text ? $.trim(paramsJson.text) : "${common_prompt_sureToDel}";
		  	this.width = paramsJson.width ? $.trim(paramsJson.width) : "300px";
		  	this.callback = paramsJson.callback;
			dhtmlx.confirm({
			    title:this.title,
			    type:this.type,
			    ok:this.ok,
			    cancel:this.cancel,
			    width: this.width,
			    text:this.text,
			    callback:this.callback
			});
			break;
		case "confirm-warning":
		  	this.title = paramsJson.title ? $.trim(paramsJson.title) : "${common_prompt_title}";
		  	this.type = this.types ? $.trim(this.types) : "confirm-warning";
		  	this.ok = paramsJson.ok ? $.trim(paramsJson.ok) : "${common_edit_ok}";
		  	this.cancel = paramsJson.cancel ? $.trim(paramsJson.cancel) : "${common_edit_cancel}";
		  	this.text = paramsJson.text ? $.trim(paramsJson.text) : "${common_prompt_sureToDel}";
		  	this.width = paramsJson.width ? $.trim(paramsJson.width) : "300px";
		  	this.callback = paramsJson.callback;
			dhtmlx.confirm({
			    title:this.title,
			    type:this.type,
			    ok:this.ok,
			    width: this.width,
			    cancel:this.cancel,
			    text:this.text,
			    callback:this.callback
			});
			break;
		/*case "message":
			//添加当两次message提示框间隔很小出现时，导致提示框样式干扰问题
			if($("#messageIconId").parent().parent().parent())
			{
				$("#messageIconId").parent().parent().empty();
			}
			this.width = paramsJson.width ? paramsJson.width : 250;
			this.type = this.types ? $.trim(this.types) : "warning";
			this.text = paramsJson.text ? $.trim(paramsJson.text) : "";
			this.expire = paramsJson.expire ? $.trim(paramsJson.expire) : 1200;
			dhtmlx.message({
				id:"messageBoxId",
				type:"info",
				text:"<img id='messageIconId' src=''/>&nbsp;&nbsp;"+this.text,
				expire:this.expire
			});
			//动态计算message的显示位置
			//$($(".dhtmlx_message_area")[0]).css("left",(document.body.offsetWidth)/2-40);
			//$($(".dhtmlx_message_area")[0]).css("top",(document.body.offsetHeight)/2);
			//添加icon图片，并动态更新样式
			var messageIconObj = $("#messageIconId");
			messageIconObj.parent().css("text-align","center");
			messageIconObj.parent().css("line-height","38px");
			messageIconObj.parent().css("overflow","hidden");
			messageIconObj.parent().css("height","38px");
			messageIconObj.parent().css("padding-left","8px");
			messageIconObj.parent().css("padding-right","8px");
			messageIconObj.parent().parent().parent().css("width","auto");
			messageIconObj.parent().parent().parent().css("display","inline-table");
			
			var messageIconPath = "public/controls/dhtmlx/dhtmlxMessage/codebase/icons/";
			//为message框动态指图标,succsee error loading warning 四种不同图标
			switch (this.type)
			{
				case "success" :
					messageIconObj.attr("src",messageIconPath+"message_success.png");
					break;
				case "error" :
					messageIconObj.attr("src",messageIconPath+"message_error.png");
					break;
				case "loading" :
					messageIconObj.attr("src",messageIconPath+"message_loading.gif");
					break;
				case "warning" :
					messageIconObj.attr("src",messageIconPath+"message_warning.png");
					break;
			}
			messageIconObj.css("vertical-align","middle");
			break;*/
		case "alert":
			this.title = paramsJson.title ? $.trim(paramsJson.title) : "${common_prompt_title}";
			this.type = this.types ? $.trim(this.types) : "alert";	//'alert', 'alert-warning', 'alert-error
			this.text = paramsJson.text ? $.trim(paramsJson.text) : "";
			this.ok = paramsJson.ok ? $.trim(paramsJson.ok) : "${common_edit_ok}";
			this.callback = paramsJson.callback;
			dhtmlx.alert({
					title:this.title,
					type:this.type,
					text:this.text,
					ok:this.ok,
					callback:this.callback
			})
			break;
		case "alert-warning":
			this.title = paramsJson.title ? $.trim(paramsJson.title) : "${common_prompt_title}";
			this.type = this.types ? $.trim(this.types) : "alert";	//'alert', 'alert-warning', 'alert-error
			this.text = paramsJson.text ? $.trim(paramsJson.text) : "";
			this.ok = paramsJson.ok ? $.trim(paramsJson.ok) : "${common_edit_ok}";
			this.callback = paramsJson.callback;
			dhtmlx.alert({
					title:this.title,
					type:this.messageType,
					text:this.text,
					ok:this.ok,
					callback:this.callback
			})
			break;
	}
	//添加样式切换功能
	/*$(".dhtmlx_popup_button").each(function(b){
		$(this).hover(function(){
			$(this).css("background","url(public/images/btn_bg_hover.jpg) repeat scroll 0 0 transparent");
		},
		function(){
			$(this).css("background","url(public/images/btn_bg.jpg) repeat scroll 0 0 transparent");
		});
	});*/
	
}
/**
 * 限制startTimeId和endTimeId不能大于当前时间
 * startTimeId不能大于endTimeId
 * @author colin.yao 2014-4-16 17:42:14
 * @param mCal new出来的日期控件对象
 * @param startTimeId
 * @param endTimeId
 * @param timeType 日期格式类型
 * @param type 类型（1为开始，2为结束）
 * @return
 */
function limitTime(mCal, startTimeId, endTimeId, timeType, type){
	//获取当前日期
    var myDate = new Date();
	var oneYear = myDate.getFullYear();
    var oneMonth = myDate.getMonth()+1;
	var oneDay = myDate.getDate(); 
	myDate = oneYear+"-"+oneMonth+"-"+oneDay; 
	var startTime = $("#"+startTimeId).val();
	var endTime = $("#"+endTimeId).val();
	if(timeType == 0)
	{
		mCal.setDateFormat(sysCfg.dhxShortDateFmt);		
		mCal.hideTime();	
	}	
	else	
	{		
		mCal.setDateFormat(sysCfg.dhxLongDateFmt);
	}	
	if(type == 1)
	{
		if(endTime == "" || endTime == null)
		{
			mCal.setSensitiveRange(null, myDate);
		}
		else
		{
			mCal.setSensitiveRange(null, endTime);
		}
	}
	else
	{
		if(startTime == "" || startTime == null)
		{
			mCal.setSensitiveRange(null, myDate);
		}
		else
		{
			mCal.setSensitiveRange(startTime, myDate);
		}
	}
}

/**
 * 函数主要用于有开始时间和结束时间需要输入的情况
 * @author liangm 20130427
 * @param mCal 日期控件
 * @param startTimeId 开始时间输入框id
 * @param referTimeId 参照时间id（即当填写开始时间时，那么它的参照时间id即为结束时间输入框id，反之，亦然）
 * @return
 */
function inputTime(mCal, startTimeId, referTimeId, timeType)
{
	if(timeType == 0)
	{
		mCal.setDateFormat(sysCfg.dhxShortDateFmt);		
		mCal.hideTime();	
	}	
	else	
	{		
		mCal.setDateFormat(sysCfg.dhxLongDateFmt);
	}	
	if (referTimeId == startTimeId)
	{
        mCal.setSensitiveRange($("#"+referTimeId).val() == "" ? null : $("#"+referTimeId).val(), null);
    }
    else
	{
        mCal.setSensitiveRange(null, $("#"+referTimeId).val() == "" ? null : $("#"+referTimeId).val());
    }
}

/**
 * 限制开始时间不能大于结束时间、结束时间不能小于开始时间
 * @author Jason 20130620
 * @param myCalendar new出来的日期控件对象
 * @param startTimeId 开始时间输入框id
 * @param referTimeId 参照时间id（即当填写开始时间时，那么它的参照时间id即为结束时间输入框id，反之，亦然）
 * @return
 */
function inputDateTime(myCalendar, startTimeId, referTimeId)
{
	if (referTimeId == startTimeId)
	{
        myCalendar.setSensitiveRange($("#"+referTimeId).val(), null);
    }
    else
	{
        myCalendar.setSensitiveRange(null, $("#"+referTimeId).val());
    }
}

/**
 * @Description: 创建长日期格式的dhx日历控件（格式如：2013-5-20 18:00:00）
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-09-03
 * @param: object 需要解析的xml路径
 */
var myCalendars = new Array();
function createLongDhxCalendar(object, startTime)
{
	var longCalendar;
	var objArray = object.toString().split(",");//保存id值
	//下面是同过ajax请求加载到相应控件中
	longCalendar = new dhtmlXCalendarObject(object);
	longCalendar.setDateFormat(sysCfg.dhxLongDateFmt);
	if(startTime)
	{
		longCalendar.setSensitiveRange(startTime);
	}
	//longCalendar._strToDate(new Date(), sysCfg.dhxShortDateFmt);
	setCalendarTopPosition(longCalendar,objArray);
	myCalendars[object] = longCalendar;
	return longCalendar;
}

/**
 * @Description: 创建短日期格式的dhx日历控件（格式如：2013-5-20）
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-09-03
 * @param: object 
 */
function createShortDhxCalendar(object, startDate)
{
	var shortCalendar;
	var objArray = object.toString().split(",");//保存id值
	//下面是同过ajax请求加载到相应控件中
	shortCalendar = new dhtmlXCalendarObject(object);
	shortCalendar.setDateFormat(sysCfg.dhxShortDateFmt);
	if(startDate)
	{
		shortCalendar.setSensitiveRange(startDate);
	}
	shortCalendar.hideTime();//隐藏时间
	setCalendarTopPosition(shortCalendar, objArray);
	myCalendars[object] = shortCalendar;
	return shortCalendar;
}

/**
 * @Description: 自动判断给Calendar设置Position为Top
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2013-09-03
 * @param: calendar 日期控件对象
 * @param: objArray 对象id数组
 * @param: autoJudge 自动判断，false则是一定设置为top
 */
function setCalendarTopPosition(calendar, objArray, autoJudge)
{
	autoJudge = typeof(autoJudge) == "undefined" || autoJudge == null || autoJudge ? true : false;
	function doOnInpClick()
	{
		if(!autoJudge)//非自动，必须使用
		{
			this.calendar.setPosition($(this).offset().left, ($(this).offset().top - $(this.calendar.base).height()));
		}
		else if(($(this).offset().top + $(this.calendar.base).height()) > $(document.body).height() && $(this).offset().top > $(this.calendar.base).height())//动态判断位置
		{
			this.calendar.setPosition($(this).offset().left, ($(this).offset().top - $(this.calendar.base).height()));
		}
	};
	var objectArray = (typeof(objArray) == "string" ? new Array([objArray]) : objArray);
	for(var i = 0; i < objectArray.length; i++)
	{
		var obj = document.getElementById(objectArray[i]);
		if(obj == null || !obj)
		{
			break;
		}
		if(String(obj.tagName).toLowerCase() == "input")
		{
			$(obj).bind("blur", function(){
				if(this.calendar.isVisible())
				{
					var inputObj = this;
					var t = window.setTimeout(function(){
						inputObj.calendar.hide();
						window.clearTimeout(t);
					},50);
				}
			});
			
			$(obj).bind("click", doOnInpClick);
			
			obj.calendar = calendar;
			obj.autoJudge = autoJudge;
		}
	}
	objArray = null;
}

/**
 * @Description: 给Layout设置Resize自动适应大小变化
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2014-3-20
 * @param: layout dhtmlXLayoutObject对象
 */
function setLayoutResize(layout)
{
	layout.attachEvent("onPanelResizeFinish", setSizesGrid);//内部拖动
    layout.attachEvent("onResize", setSizesGrid); //外部缩放
    layout.attachEvent("onCollapse", setSizesGrid); //按钮缩
    layout.attachEvent("onExpand", setSizesGrid);//按钮展开
    layout.setSizes(false);
}

/**
 * @Description: 重置grid大小
 * @Author: lynn.chen  陈立  
 * @Modified By:
 * @Date: 201312-27
 * @param: girdName
 */ 
function resizeLayout(layoutName)
{
	if(document.getElementById(layoutName))
	{
		var height = $("#" + layoutName).outerHeight() - $("#" + layoutName).position().top;
		$("#" + layoutName).height(height);
	}
}

/**
 * @Description: 按钮操作模板高度重置方法
 * @Author: lynn.chen  陈立
 * @Modified By:
 * @Date: 2015-01-23
 */ 
function opTemplateHeightResize()
{
	if($(".dhxwin_active").size() > 0)
	{
		var $contentDiv = $(".dhxwin_active .content_div");
		$contentDiv.height($(".dhxwin_active .content_td").height());
		$contentDiv.css("overflow", "auto");
	}
	else
	{
		var $contentDiv = $(".dhx_cell_cont_layout .content_div");
		$contentDiv.height($(".dhx_cell_cont_layout .content_td").height());
		$contentDiv.css("overflow", "auto");
	}
}
