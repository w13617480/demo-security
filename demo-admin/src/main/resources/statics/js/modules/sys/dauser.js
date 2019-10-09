var deptList = null;

$(function () {
	
});

function iniGrid(){
	$("#jqGrid").jqGrid({
        url: baseURL + 'sys/dauser/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
			 
			{ label: '部门名称', name: 'deptId', index: 'deptId', formatter:function(deptId){
					
					var deptName="";
				
				
					for(i=0; i< deptList.length; i++){
						if (deptList[i].deptId == deptId ){
							deptName = deptList[i].name;
							break;
						}
					}
				
					return deptName;
				} 
			}, 	
			{ label: '姓名', name: 'name', index: 'name', width: 60, align:'center' }, 			
			{ label: '性别', name: 'sex', index: 'sex', width: 50, align:'center', hidden:true, formatter: function(sex){
				    var type="sex";
					return getSelectvalue(type,sex);	
				} 
			}, 	
			
			{ label: '工号', name: 'worknumber', index: 'worknumber', width: 60, align:'center' },
			{ label: '身份证号', name: 'idnumber', index: 'idnumber', width: 90, align:'center' }, 
			
			{ label: '联系方式', name: 'mobile', index: 'mobile', width: 80, align:'center', hidden:true }, 					
			{ label: '出生日期', name: 'birthday', index: 'birthday', width: 80, align:'center', hidden:true }, 			

			{ label: '积分', name: 'points', index: 'points', width: 60, align:'right' }, 			
			{ label: '等级', name: 'level', index: 'level', width: 60, align:'right' }, 			
				
			{ label: '状态', name: 'status', index: 'status', width: 50, align:'center',formatter: function(status){
			    	var type="zaizhi";
			    	return getSelectvalue(type,status);
				}  
			}, 
			{ label: '创建人', name: 'creator', index: 'creator', width: 60, align:'center',formatter: function(creator){
					return getUsername(creator);
				}  
			},
			{ label: '创建时间', name: 'cretime', index: 'cretime', align:'center', width: 100 },
			{ label: '操作', name:'op', index:'op', width:50,sortable: false, align:'center', 
				formatter: function (cellvalue, options, rowObject) {
					return "<a href='#' onclick='resetPass(\"" + options.rowId + "\")'>重置密码</a>";
				},
			}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 50, 
        autowidth:true,
       
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
       		//自适应
        	$("#jqGrid").setGridWidth($(window).width());
        }
    });
}

var setting = {
	    data: {
	        simpleData: {
	            enable: true,
	            idKey: "deptId",
	            pIdKey: "parentId",
	            rootPId: -1
	        },
	        key: {
	            url:"nourl"
	        }
	    }
	};
	var ztree;
var vm = new Vue({
	el:'#rrapp',
	data:{
		  q:{
	            name: null,
	            status:null,
	            policetype:null,
	            deptId:null
	        },
		showList: true,
		title: null,
		daUser: {
			
			    status:1,
	            deptId:null,
	            deptName:null,
	            policetype:null
	            //birthday:null
		}
	},
	methods: {
		query: function () {
			var d=document.getElementById("start").value;
			if(d=='1'){
				return;
			}
			vm.reload();
		},
		add: function(){
			var d=document.getElementById("start").value;
			if(d=='1'){
				return;
			}
			vm.showList = false;
			 
			
			
			vm.title = "新增";
			vm.daUser = {};
			//vm.getDept();
			 
			
			var type="sex";
			var sex=document.getElementById("sex");
		    getSelect(type,sex,true);
		    
 			vm.daUser.policetype = "-1";
//			var types="xueli";
//			var xueli=document.getElementById("xueli");
//			getSelect(types,xueli,true);
			
//			var typess="zaizhi";
//			var zaizhi=document.getElementById("zaizhi");
//			getSelect(typess,zaizhi,true);
			 
		},
		update: function (event) {
			
			
			
			var d=document.getElementById("start").value;
			if(d=='1'){
				alert('22');
				return;
			}
			
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
        	var type="sex";
			var sex=document.getElementById("sex");
			getSelect(type,sex,false);
			
 
			
			var typess="zaizhi";
			var zaizhi=document.getElementById("zaizhi");
			getSelect(typess,zaizhi,false);
			
            vm.getInfo(id);
            //vm.getDept();
		},
		getDept: function(){
	            //加载部门树
	            $.get(baseURL + "sys/dept/list", function(r){
	            	
	            	deptList = r;
	            	
	            	
	            	
	                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
	                var node = ztree.getNodeByParam("deptId", vm.daUser.deptId);
	                if(node != null){
	                    ztree.selectNode(node);
	                    vm.daUser.deptName = node.name;
	                }
	                
	                iniGrid();
	            })
	        },
	    deptTree: function(flag){
	            layer.open({
	                type: 1,
	                offset: '50px',
	                skin: 'layui-layer-molv',
	                title: "请选择部门",
	                area: ['300px', '450px'],
	                shade: 0,
	                shadeClose: false,
	                content: jQuery("#deptLayer"),
	                btn: ['确定', '取消'],
	                btn1: function (index) {
	                	
	                    var node = ztree.getSelectedNodes();
	                    //选择上级部门
	                    if ( flag == '2'){
	                    	document.getElementById("deptId").value=node[0].deptId;
	                    	document.getElementById("deptName").value=node[0].name;
	                	}
	                    else if ( flag == '1' ){
	                    	$("#qDeptName").val(node[0].name);
	                    	$("#qDeptId").val(node[0].deptId);
	                    }
	                   

	                    layer.close(index);
	              
	                }
	            });
	        },
	        getRoleList: function(){
	            $.get(baseURL + "sys/role/select", function(r){
	                vm.roleList = r.list;
	            });
	        },
		saveOrUpdate: function (event) {
			//var birthday=document.getElementById("birthday").value;
			//vm.daUser.birthday=birthday
            vm.daUser.deptId = document.getElementById("deptId").value
            vm.daUser.deptName = document.getElementById("deptName").value
            
			
            if ( vm.daUser.deptId == null || vm.daUser.deptId == "" ){
            	alert("请输入“部门”！", function(){$("#deptName").focus()});
            	//vm.deptTree();
	        	return;
	        }
			var name=vm.daUser.name;
			if(name==null||name==""){
            	alert("请输入“姓名”！", function(){$("#name").focus()});
            	return;
            }
			
			var sex = vm.daUser.sex;
			if(sex==null||sex==""){
            	alert("请输入“性别”！", function(){$("#sex").focus()});
            	return;
            }
			
			var policetype = vm.daUser.policetype;
			if(policetype==null||policetype=="-1"){
            	alert("请输入“民警/辅警”！", function(){$("#policetype").focus()});
            	return;
            }
			
			
			var worknumber = vm.daUser.worknumber;			
			if (policetype == '0' && (worknumber==null||worknumber=="") )	{		
            	alert("请输入“警号”！", function(){$("#worknumber").focus()});
            	return;
            }
			
			
			var idnumber = vm.daUser.idnumber;
			if ( policetype == '1' && (idnumber==null||idnumber=="" )){
            	alert("请输入“身份证号”！", function(){$("#idnumber").focus()});
            	return;
            }

			
			var status = vm.daUser.status;
			if(status==null||status==""||status=="-1"){
            	alert("请输入“是否在职”！", function(){$("#zaizhi").focus()});
            	return;
            }
			
			 
			
			
			/*
			var mobile=vm.daUser.mobile;
			if(mobile==null||mobile==""){
            	alert("请输入“联系方式”！", function(){$("#mobile").focus()});
            	return;
            }
			
			else if(mobile!=null||mobile!=""){
            	//手机号正则
            	var phoneReg = /(^1[3|4|5|7|8]\d{9}$)|(^09\d{8}$)/;
            	if (!phoneReg.test(mobile)) {
            		alert('请输入有效的手机号码！');
            		return;
            	}
            }
            */
			
			 
			var url = vm.daUser.id == null ? "sys/dauser/save" : "sys/dauser/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.daUser),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var d=document.getElementById("start").value;
			if(d=='1'){
				return;
			}
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
		
			
			
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/dauser/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "sys/dauser/info/"+id, function(r){
                vm.daUser = r.daUser;
            });
		},
		reload: function (event) {
			
			vm.showList = true;
			
			vm.q.deptId = $("#qDeptId").val();
			
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{
					'name':vm.q.name, 
					'status': vm.q.status,
					'policetype': vm.q.policetype,
					'deptid': vm.q.deptId
				},
                page:page
            }).trigger("reloadGrid");
			
			 
		},
		creHT: function(event) {
			$.ajax({
				type: "GET",
				async:true,
			    url: baseURL + "sys/export/savePdf",
		        contentType: "application/json",
			    success: function(r){
			    	console.log(r);
			    	alert('success');
				}
			});
		},
		downHT: function(event) {
			$.ajax({
				type: "GET",
				async:true,
			    url: baseURL + "sys/export/downloadPdf",
		        contentType: "application/json",
			    success: function(r){
			    	console.log(r);
			    	alert('success');
				}
			});
		}
	}
});
 
//下载模板
function impquery(){
	var d=document.getElementById("start").value;
	if(d=='1'){
		return;
	}
	location.href = baseURL + "sys/dauser/impUser";
}
//导入
$("#import").click(function(){
	var d=document.getElementById("start").value;
	if(d=='1'){
		return;
	}
	var file = document.getElementById('files');
	file.value = ''; //虽然file的value不能设为有字符的值，但是可以设置为空值
	//或者
	file.outerHTML = file.outerHTML; //重新初始化了file的html
	
	//点击导入按钮，使files触发点击事件，然后完成读取文件的操作。
    $("#files").click();
   
});

function imortsa(){
	
	var selectedFile = document.getElementById("files").files[0];// 获取读取的File对象
	var name = selectedFile.name;// 读取选中文件的文件名
	var size = selectedFile.size;// 读取选中文件的大小
	show();

	if(name.indexOf("xls")>-1){
		show();
		start();
		  var formFile = new FormData();
          formFile.append("action", "UploadVMKImagePath");  
          formFile.append("file", selectedFile); //加入文件对象
          var data = formFile;
		  $.ajax({
				type: "POST",
				async:true,
			    url: baseURL + "sys/dauser/importExcel",
			    processData:false,
	            contentType:false,
               // cache: true,//上传文件无需缓存
			    data: data,
			    success: function(r){
			    		  
					if(r.code == 0){
						end();
						document.getElementById("start").value=0;
						var dis=r.dicts;
						setTimeout(function () { 
							$(".aaa").hide();
							$(".bbb").hide();
						}, 1000);
						alert("共"+dis.count+"条"+"导入"+dis.count+"条成功");
						
						vm.reload();
					}else{
						end();
						document.getElementById("start").value=0;
						setTimeout(function () { 
							$(".aaa").hide();
							$(".bbb").hide();
						}, 1000);
						alert(r.msg);
					}
				}
			});
	}else{
		setTimeout(function () { 
			$(".aaa").hide();
			$(".bbb").hide();
		}, 1000);
		document.getElementById("start").value=0;
		end();
		alert("请上传Excel文件")
	}
	  
}
function show(){
	
	
	$(".aaa").show();
	$(".bbb").show();
	document.getElementById("start").value=1;

}

var timer=null;
function start()
{
    timer=setInterval(function()   //开启循环：每秒出现一次提示框
    {
    	  $.ajax({
				type: "POST",
				async:true,
			    url: baseURL + "sys/dauser/importExcelMessige",
		        contentType: "application/json",
			    success: function(r){
			    		  
					if(r.code == 0){
						var dis=r.dicts;
						if(dis.count>0){
							
							document.getElementById("messige").innerHTML="共"+dis.count+"条"+"导入"+dis.gs+"条成功";
						} else {
							end();
						}
						
					}else{
						
						alert(r.msg);
					}
				}
			});
    },1000);
}
function end()
{
    clearInterval(timer);        //关闭循环
}
function read() {
	var typess="zaizhi";
	var status=document.getElementById("status");
	getSelect(typess,status,false, '0');
	 
	vm.q.status = "-1";
	vm.q.policetype = "-1";
 
	vm.getDept();
	
	var typess="zaizhi";
	var zaizhi=document.getElementById("zaizhi");
	getSelect(typess,zaizhi,false, '-1');
		
}


function resetPass(rowid){
	$("#jqGrid").jqGrid('setSelection', rowid);
	var rowData = $("#jqGrid").jqGrid('getRowData',rowid);
	//console.log(rowData);
	
	window.confirm('确定要把<span style="color:blue">' + rowData.deptId + ' ' + rowData.name  + '</span>的密码重置为初始密码？', function(){
		var userid = rowData.id;
        var data2 = "userid=" + userid ; //+ "&newpass=" + escape($.md5(newpass1));
        $.ajax({ 
            type: "POST",
            url: "/jywh/sys/dauser/resetPassword",
            data: data2,
            dataType: "json",
            success: function(result){

                if ( result != null ){

                	if ( result.code == '0') {
                		window.alert(result.msg)	
                	} else {
                    	window.alert(result.msg);
                	}
                }
            }
        }); 
	})
}







