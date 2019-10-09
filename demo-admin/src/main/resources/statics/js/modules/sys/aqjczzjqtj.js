$(function () {
	$("#ends").val(getCurrDay());
})

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: false,
		title: '安全检查站自救器发放台账',
		tzBorrow: {},
		zjqtj:{
			    start:null,
	            ends:null
	    }
	},
	methods: {
		query: function () {
			vm.reload();
		},
		 
		 
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});
 
function valid(bc, ends) {
	if(bc=="-1"){
		alert("请选择班次", function(){$("#bc").focus()});
		return false;
	}
	
	if(ends==null||ends==""){
		alert("请选择日期", function(){$("#ends").focus()});
		return false ;
	}

	return true;
}
 


//生成表报
function tjquery(){
	
	var ends= document.getElementById("ends").value;
	var bc =document.getElementById("bc").value;
	var tbodys=document.getElementById("tbodys");
	
	tbodys.innerHTML="";
	
	if ( valid( bc, ends)) {
		
		vm.zjqtj.start=ends;
		vm.zjqtj.ends=bc;
		$.ajax({
			type: "POST",
		    url: baseURL + "sys/tzborrow/aqjczzjqtj",
	        contentType: "application/json",
		    data: JSON.stringify(vm.zjqtj),
		    success: function(r){
				if(r.code == 0){
					  var diss=r.dars;
					  var typea="pili";
					  var typed="sbzt";
					  var ds="sfdq";
					  var cont=0;
					  for(var c=0;c<diss.length;c++){
					      var dis=diss[c];
					      cont++;
						  var tbody=document.getElementById("tbodys");
						  
						  var tr=document.createElement("tr");
						  
						  var td1=document.createElement("td");
						  td1.innerHTML=cont;
						  tr.appendChild(td1);
						  
						  var td2=document.createElement("td");
						  td2.innerHTML=dis.dept_name;
						  tr.appendChild(td2);
						  
						  var td3=document.createElement("td");
						  td3.innerHTML=dis.sl;
						  tr.appendChild(td3);
						  
						  var td4=document.createElement("td");
						  td4.innerHTML=dis.name;
						  tr.appendChild(td4);

						  tbody.appendChild(tr);
						 
					     }
				}else{
					alert(r.msg);
				}
			}
		});
	}
}

function impquery(){
	var ends= document.getElementById("ends").value;
	var bc =document.getElementById("bc").value;
	if ( valid(bc, ends) ){
		location.href =baseURL + "sys/tzborrow/impaqjczzjqtj?stat="+ends+"&bc="+bc;
	}
}

function read(){
	var type="bc";
	var sex=document.getElementById("bc");
	getSelect(type,sex,true);
}
