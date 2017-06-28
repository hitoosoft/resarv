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
    <form id="formCellAdd" columns='1' class="easyui-form">
      	<input id="partNO" title="排号" name="partNO" type="text" style="width: 300px;" class="easyui-combobox" panelHeight="auto" required="true"/>
	    <input id="levNO" title="列号" name="levNO" type="text" style="width: 300px;" class="easyui-combobox" panelHeight="auto" required="true"/>
	    <input title="格容量" name="totalNum" style="width: 300px;" class="easyui-numberbox" required="true"/>
      	<input name="eqptID" type="hidden" value="${param.eqptID}" />
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="btnCellAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">
var eqptID = "${param.eqptID}";

$(function () {
  if(eqptID != ""){
     //初始化本设备的排，构造combobox
      $("#partNO").combobox({
        url:"${app}/repo/eqpt/queryEqptPartCombobox.do?eqptID="+eqptID,
        valueField:'id',   
        textField:'text',
        onChange:function(newValue, oldValue){
          queryEqptLevCombobox(newValue);
        }
     }); 
  }
});

//排变化了，层跟着变化
function queryEqptLevCombobox(newValue){
   $("#levNO").combobox({
      url:"${app}/repo/eqpt/queryEqptLevCombobox.do?eqptID="+eqptID+"&partNO="+newValue,
      valueField:'id',   
      textField:'text'  
    }); 
}

//保存新增单元格
function btnCellAddSaveClick(obj) {
  //保存之前需要前台校验
  var valid = $("#formCellAdd").form("validate");
  if(!valid){
    return false;
  }
  
  hitooctrl.eamsOperPost("${app}/repo/cell/addCell.do",$("#formCellAdd").form("getData"),function(paraMap){
    parent.queryCellByEqptIDAndPartNO();
    parent.hitooctrl.closeWin();
  }, obj);
}
</script>
</html>