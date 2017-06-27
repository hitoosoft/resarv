<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增库房</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
    <form id="formDefAdd" class="easyui-form" columns="1" style="line-height: 25px;">
      <input title="所属全宗" id="gnlArv" name="gnlArvID" class="easyui-combobox" panelHeight="180" editable="false" style="width:304px;" required="true"/>
      <input title="库房名称" name="repoNam" type="text" class="easyui-validatebox" required="true" style='width: 300px;'>
      <input title="联系人" name="contacts" type="text" class="easyui-validatebox" required="true" style='width: 300px;'>
      <input title="库房序号" name="seqNO" type="text" class="easyui-numberbox" style='width: 300px;'>
      <textarea title="描述" name="descr" colspan="3" rows='5' style='width: 299px;'></textarea>
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="btnDefAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">
$(function(){
	$("#gnlArv").combobox({
	 	url:'${app}/bas/gnlarv/queryGnlArv.do',    
        valueField:'id',    
        textField:'text'
	});
});

//保存新增库房
function btnDefAddSaveClick(obj) {
  //保存之前需要前台校验
  var valid = $("#formDefAdd").form("validate");
  if(!valid){
    return false;
  }
  
  hitooctrl.eamsOperPost("${app}/repo/def/addDef.do",$("#formDefAdd").form("getData"),function(paraMap){
    parent.initDefTree(paraMap);
    parent.hitooctrl.closeWin();
  }, obj);
}
</script>
</html>