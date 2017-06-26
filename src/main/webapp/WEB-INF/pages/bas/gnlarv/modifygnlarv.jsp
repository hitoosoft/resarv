<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>修改全宗</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false" class="htborder-left">
   <form id="formGnlArvModify" columns='1' class="easyui-form">
    <input title="全宗编码" name="gnlArvCod" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true"/>
    <input title="全宗名称" name="gnlArvNam" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true"/>
    <input id="orgNam" title="立档单位" name="orgNam" type="text" style="width:300px;" class="easyui-validatebox"   disabled="disabled" />
    <input title="顺序号" name="seqNO" class="easyui-numberbox" type="text" colspan='2' style='width: 98.5%;'/>
    <textarea title="全宗描述" name="descr" colspan="3" rows='5' style='width: 98%;'></textarea>
    <input name="gnlArvID" type="hidden">
    <input name="orgID" type="hidden">
   </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
     <a class="easyui-linkbutton" onClick="btnGnlArvModifySaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
     <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
<body>
<script type="text/javascript">
	var gnlarvID = "${param.gnlarvID}";
	$(function(){
	  if(gnlarvID!=""){
	    hitooctrl.eamsOperPost("${app}/bas/gnlarv/queryGnlarvByID.do", {gnlarvID:gnlarvID}, function(paraMap){
	      if(paraMap!=null&&paraMap.Gnlarv!=null){
	        $("#formGnlArvModify").form('setData',paraMap.Gnlarv);
	        $("#orgNam").val(paraMap.Gnlarv.orgNam);
	      }
	   });
	  }
	});
	
	//保存修改
	function btnGnlArvModifySaveClick(obj){
	 var valid = $("#formGnlArvModify").form('validate');
	 if(!valid){
	   return;
	 }
	 
	 hitooctrl.eamsOperPost("${app}/bas/gnlarv/modifyGnlarv.do", $("#formGnlArvModify").form("getData"), function(paraMap){
		    parent.initGnlArvTree(paraMap);
	      parent.hitooctrl.closeWin();
	  },obj);
	}
</script>
</body>
</html>