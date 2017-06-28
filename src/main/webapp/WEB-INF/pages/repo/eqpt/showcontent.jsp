<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增单元格</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
    <table id="showContentDg"></table>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">关闭</a>
  </div>
</body>
<script type="text/javascript">
var eqptID = "${param.eqptID}";

$(function () {
	$("#showContentDg").datagrid({
		   idField : "PK",
		   loadMsg : '数据加载中,请稍后...',
		   fit:true,
		   border:false,
		   fitColumns:true,
		   singleSelect:true,
		   pagination : false,
		   rownumbers : true,
		   striped:true,
		   pageSize : 10,
		   pageList : [ 10,30,50],
		   columns : [ [ {
		     field : "PK",
		     width : 80,
		     title : "ID",
		     hidden : true
		   }, {
			   field : "TYPE",
		      width : 60,
		      formatter:hitooctrl.hitooDicConvert('ORGANIZEMODEL') ,
		      title : "类型"
			}, {
		      field : "TYPENO",
		      width : 100,
		      title : "编号"
			}, {
		      field : "TYPENAM",
		      width : 120,
		      align : 'center',
		      title : "名称"
		   } ] ]
	});
	
	$("#showContentDg").datagrid("loadData",JSON.parse('${contentDg}'));
});
</script>
</html>