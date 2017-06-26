<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>数据权限管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="west" style="width:500px;" border="false" >
		<table id="dgRole"></table>
		<div id="tb">
			<div class="perm-panel">
			    <a class="easyui-linkbutton" show="true" onClick="manageRoleFnc(0);" icon="icon-add" plain="true">新增</a> 
				<a class="easyui-linkbutton" show="true" onClick="manageRoleFnc(1);" icon="icon-edit" plain="true">修改</a>
				<a class="easyui-linkbutton" show="true" onClick="delRoleFnc();" icon="icon-remove-edit" plain="true">删除</a>
			</div>
		</div>
	</div>
	<div region="center" border="false" class="htborder-left">
		<div class="easyui-layout" fit="true">
		  <div region="center" border="false">
		       <div class="easyui-panel" fit="true" border="false" title="为角色赋全宗"
		       data-options="tools:'#treepaneltool'">
				 	 <ul id="role_datautTree" class="easyui-tree"></ul>	
				</div>
				<div id="treepaneltool" >
        <a style="width:25px;vertical-align:bottom;text-decoration:none;cursor: e-resize;" href="javascript:void(0)" class="">级联</a>
        <input  type="checkbox" checked onclick="configcascade();" style="margin-right:10px;"/>
    </div>
		  </div>
			<div region="south" border="false" style="text-align:center;float:right;padding:3px;" class="htborder-top">
	             <a id="btnSave" class="easyui-linkbutton" onClick="save(this);" iconCls="icon-save">保存</a>
	        </div>
		</div>
		
	</div>
</body>

<script type="text/javascript">

var cascadeFlag=true;
/**
 * 界面初始化，加载
 */
$(document).ready(function() {
	hitooctrl.loadPageBtnAuthority(parent.hitoo.curMenuID, initPage());
});
/**
 * 初始化角色datagrid
 */
function initPage(){
	$('#dgRole').datagrid({
	    height: 'auto',
		fitColumns: true,
		striped:true,
		fit:true,
		toolbar:'#tb',
		url: '${app}/bas/dataut/queryRoleDgOwnByUserWithPage.do',
		border:false,
		singleSelect:true,
		pagination:true,
	    columns:[[
	        {field:'roleID', hidden:true},
	        {field:'roleType', hidden:true},
	        {field:'roleNam',title:'角色名称',width:200},
	        {field:'descr',title:'描述',width:300}
	    ]],
	    onLoadSuccess:function(data){
	    	if(data.total >0){
	    		$('#dgRole').datagrid('selectRow', 0);
	    	}
	    },
	    onSelect:function(rowIndex,rowData){
	    	loadRoleFuncTree(rowData.roleID);
	    }
	});
}

function queryRole(){
	var param = {};
	$("#dgRole").datagrid("options").url=  '${app}/bas/dataut/queryRoleDgOwnByUserWithPage.do';
	$('#dgRole').datagrid("load",param);
	$('#role_datautTree').tree("loadData",[]);
}

//设置选择级联
function configcascade(){
	cascadeFlag=!cascadeFlag;
	var currrole = $('#dgRole').datagrid('getSelected');
	if(currrole){
		  loadRoleFuncTree(currrole.roleID);
	}

}


function loadRoleFuncTree(roleID){
	var role_funcLoading=false;
	var frontPageCascade="1";
	if(!cascadeFlag){
		frontPageCascade="0";
	}
	$('#role_datautTree').tree({
		lines : true,
		url:'${app}/bas/dataut/queryRoleDatautTree.do?roleID='+roleID+'&frontPageCascade='+frontPageCascade,
		checkbox:true,	
		cascadeCheck: cascadeFlag,
		onBeforeLoad:function(node, param){ 
			role_funcLoading=true; 
		}, 
		onLoadSuccess:function(node, data){ 
			role_funcLoading=false; 		    
		},
		onCheck:function(node,checked){
			if(role_funcLoading){
				return ;
			}else{
				//role_funcTreeByRoleIDChecked(roleID);					
			}
		}
	});
}

function save(obj){
	var row =$("#dgRole").datagrid("getSelected");
	if(row==null){
		return ;
	}
	var gnlIDs = '';
	var nodes = $('#role_datautTree').tree('getCheckedExt');//选择中的节点，包括实习节点
	for ( var i = 0; i < nodes.length; i++) {
		if (gnlIDs != ''){
			gnlIDs += ',';
		}
		gnlIDs += nodes[i].id;
	}
	var urlparms = {};
	urlparms.roleID = row.roleID;
	urlparms.gnlIDs = gnlIDs;
	hitooctrl.eamsOperPost("${app}/bas/role/saveRoleGnl.do",urlparms,function(){
		$("#btnSave").linkbutton("enable");
	},obj);
}

//新增或者修改角色按钮事件        
function manageRoleFnc(t){
	var url = '${app}/bas/dataut/forwardManagerole.do';
	var width = 370;
	var height = 200;
	var title = "新增角色";
	var paraMap ={};
	paraMap.roleType="DATAUT";
	paraMap.editFlag="F";
	if(t){
		var row = $("#dgRole").datagrid("getSelected");
		if(!row){
			$.messager.alert("提示", "请选择一条数据！", "info");
			return;
		}
		paraMap.editFlag="T";
		paraMap.roleID = row.roleID;
		paraMap.roleNam = row.roleNam;
		paraMap.roleType = row.roleType;
		paraMap.descr = trim(row.descr);
		title = "修改角色";
	}
	
	hitooctrl.openWin(url, paraMap, width, height, title);
}
//删除角色按钮
function delRoleFnc(){
	var row = $("#dgRole").datagrid("getSelected");
	if(!row){
		$.messager.alert("提示", "请选择一条数据！", "info");
		return;
	}
	
	$.messager.confirm('提示', '与该角色相关的信息将会一起删除，确认删除吗？', function(r) {
		if (!r) {
			return;
		}
		var url='${app}/bas/dataut/delRoleDataut.do';
		var urlparms = {};
		urlparms.roleID= row.roleID ;
		hitooctrl.eamsOperPost(url, urlparms, function (){
			queryRole();
		});
	});
}
</script>
</html>