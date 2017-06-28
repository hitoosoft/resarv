<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增设备</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
 <div region="center" border="false">
    <form id="formEqptAdd" class="easyui-form" columns="1">
      <input title="设备名称" name="eqptNam" type="text" class="easyui-validatebox" required="true" style='width: 300px;'>
      <input title="排数量" name="partNum" type="text" class="easyui-numberbox" min=1 required="true" style='width: 300px;'>
      <input title="列数量" name="levNum" type="text" class="easyui-numberbox" min=1 required="true" style='width: 300px;'>
      <input title="顺序号" name="seqNO" type="text" class="easyui-numberbox" style='width: 300px;'>
      <textarea title="描述" name="descr" colspan="3" rows='5' style='width: 300px;'></textarea>
      <input name="repoID" type="hidden" value="${param.repoID}">
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="btnEqptAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">

//保存新增设备
function btnEqptAddSaveClick(obj) {
  //保存之前需要前台校验
  var valid = $("#formEqptAdd").form("validate");
  if(!valid){
    return false;
  }
  
  hitooctrl.eamsOperPost("${app}/repo/eqpt/addEqpt.do",$("#formEqptAdd").form("getData"),function(paraMap){
	  parent.loadEqpt(paraMap);
    parent.hitooctrl.closeWin();
  }, obj);
}
</script>
</html>