$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/daquery/querypoints',
        datatype: "json",
        colModel: [			
            { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
            { label: 'userid', name: 'userid', index: 'userid', width: 80, hidden:true }, 			
            { label: '姓名', name: 'name', index: 'userid', width: 80, sortable:true }, 			
            { label: '单位', name: 'dept_name', index: 'userid', width: 80, sortable:true }, 			
            { label: '发生时间', name: 'occtime', index: 'occtime', width: 80, align:'center' }, 			
            { label: '积分类型', name: 'type', index: 'type', width: 80, hidden:true }, 			
            { label: '积分数', name: 'points', index: 'points', width: 80, align:'right' }, 			
            { label: '备注', name: 'memo', index: 'memo', width: 80 }		
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 50, 
        
        sortable:true,
    	sortname:'occtime',
    	sortorder:'desc',
        
        autowidth:true,
        multiselect: false,
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			 name: null,
			 start:null,
			 ends:null
        },
		showList: true,
		title: null,
		daPointsQry: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		
		reload: function (event) {
			vm.showList = true;
			
			vm.q.start = $("#start").val();
			vm.q.ends = $("#ends").val();
			
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{
					'name':vm.q.name,
					'start': vm.q.start,
					'ends': vm.q.ends
				},
				datatype:'json',
                page:page
            }).trigger("reloadGrid");
		}
	}
});