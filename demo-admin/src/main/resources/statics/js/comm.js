//获取下拉框
function getSelect(type,select,ts, dv){
	
	 
	$.ajax({
		type: "POST",
		async: ts,
	    url: baseURL + "sys/dict/showtype",
        contentType: "application/json",
	    data: type,
	    success: function(r){
	    	 
			if(r.code == 0){
				var dis=r.dicts;
				select.length = 1;
				for(var i=0;i<dis.length;i++){
					select.options.add(new Option(dis[i].value,dis[i].code));
				}
				
				//select.value=dv;
				 
				 
			}else{
				alert(r.msg);
			}
		}
	});
}
//获取下拉框对应值
function getSelectvalue(type,code){

	var val_succuess='<span class="label label-success">';
	var val_danger='<span class="label label-danger">';
	var va="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/dict/showtype",
        contentType: "application/json",
	    data: type,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dicts;
				for(var i=0;i<dis.length;i++){
					if(dis[i].code==code){
							val_succuess+=dis[i].value;
							val_danger+=dis[i].value;
						    va=dis[i].value;
					}
				}
			}else{
				alert(r.msg);
			}
		}
	});
	//'<span class="label label-success">正常</span>';
	val_succuess+='</span>';
	val_danger+='</span>';
	
	if(type=="sex"||type=="xueli" || type == "bc" || type == 'sbtz'){
		return va
	}else{
		if(code=="0"){
			return val_succuess;
		}else{
			return val_danger;
		}
		
	}
	
}

//获取下拉框对应值
function getSelectvalues(type,id){
	var vale="";
	$.ajax({
		type: "POST",
		async:true,
	    url: baseURL + "sys/dict/showtype",
        contentType: "application/json",
	    data: type,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dicts;
				for(var i=0;i<dis.length;i++){
					if(dis[i].id==id){
						vale=dis[i].name;
					}
				}
			}else{
				alert(r.msg);
			}
		}
	});
	
    return vale;
	
	
	
}
//获取用户名
function getUsername(id){
	
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/user/info",
        contentType: "application/json",
	    data: id,
	    success: function(r){
			if(r.code == 0){
				var user=r.user;
				if(user.username!=""){
					vale=user.username;
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取下拉框班组
function getSelectBanz(sel,ts){
	
	var vale="";
	$.ajax({
		type: "POST",
		async:ts,
	    url: baseURL + "sys/dateam/alllist",
        contentType: "application/json",
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				sel.length=1;
				for(var i=0;i<dis.length;i++){
					sel.options.add(new Option(dis[i].name,dis[i].id));
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取下拉框班组name
function getSelectBanzName(id){
	
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/dateam/alllist",
        contentType: "application/json",
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				for(var i=0;i<dis.length;i++){
					if(dis[i].id==id){
						vale=dis[i].name;
					}
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取下拉框检查站
function getSelectJcz(select,ts){
	
	var vale="";
	$.ajax({
		type: "POST",
		async:ts,
	    url: baseURL + "sys/dacheckpoint/alllist",
      contentType: "application/json",
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				select.length=1;
				for(var i=0;i<dis.length;i++){
					select.options.add(new Option(dis[i].name,dis[i].id));
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取下拉框检查站name
function getSelectJczName(id){
	if ( id == null || id == ''){
		return '';
	}
		
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/dacheckpoint/alllist",
      contentType: "application/json",
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				for(var i=0;i<dis.length;i++){
					if(dis[i].id==id){
						vale=dis[i].name;
					}
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//根据id获取人员信息
function getuserName(id){
	
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/tzborrow/getuserName",
      contentType: "application/json",
      data: id,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				if(dis.name!=""){
					vale=dis.name;
				}
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取设备名称
function getshebName(id){
	 
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/tzborrow/getshebName",
      contentType: "application/json",
      data: id,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				if(dis.name!=""){
					vale=dis.name;
				}
				
				
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}


//获取设备名称
function getshebCode(id){
 
	
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/tzborrow/getshebName",
	    contentType: "application/json",
	    data: id,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				if(dis.code!=""){
					vale=dis.code;
				}
			}else{
				alert(r.msg);
			}
		}
	});
	return vale;
}

//获取下拉框检查站name
function getoperator(id){
	if ( id == null || id == ''){
		return '';
	}
	
	 
	var ids=id+"";
	var vale="";
	$.ajax({
		type: "POST",
		async:false,
	    url: baseURL + "sys/tzborrow/getoperator",
        contentType: "application/json",
        data: ids,
	    success: function(r){
			if(r.code == 0){
				var dis=r.dars;
				vale=dis.operatorname;
				
			}else{
				alert(r.msg);
			}
		}
	});
	
	return vale;
}


function getCurrDay(){
	var myDate = new Date();
	var year=myDate.getFullYear();
	var month=myDate.getMonth()+1;
	var day = myDate.getDate();

	return year + '-' + month + '-' + day;
}