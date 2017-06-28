<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>智能划分单元格</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
    <form id="formCellAutoAdd" columns='1' class="easyui-form">
      <input title="每列单元格数量" name="cellNO" style="width: 300px;" class="easyui-numberbox" required="true"/>
      <input title="格容量" name="totalNum" style="width: 300px;" class="easyui-numberbox" required="true"/>
      <input name="eqptID" type="hidden" value="${param.eqptID}" />
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="btnCellAutoAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">

//保存
function btnCellAutoAddSaveClick(obj) {
  //保存之前需要前台校验
  var valid = $("#formCellAutoAdd").form("validate");
  if(!valid){
    return false;
  }
  
  hitooctrl.eamsOperPost("${app}/repo/cell/addAutoCell.do",$("#formCellAutoAdd").form("getData"),function(paraMap){
	  parent.loadEqpt(paraMap);
    parent.hitooctrl.closeWin();
  }, obj);
}
</script>
</html>