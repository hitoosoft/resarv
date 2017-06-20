<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>修改机构</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false">
     <form id="formOrgModify" columns='1' class="easyui-form">
      <input title="机构编码" name="orgCod" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true" />
      <input title="机构名称" name="orgNam" type="text" style="width:300px;" class="easyui-validatebox" validType="maxUTFLength[100]" required="true"/>
      
      <input id="areaCod" title="职能区划" name="areaCod" style="width:306px;" syscode="AREACOD" class="easyui-combobox"  panelHeight="auto" editable="false"  />
      <input id="parentOrgCombotree" title="上级机构" class="easyui-combotree" name="parentID" style="width:306px;"/>
      <input title="负责人" name="leader" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[100]"/>
      <input title="联系人" name="contacts" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[100]"/>
      <input title="联系电话" name="phones" style="width:300px;"type="text" class="easyui-validatebox" validType="maxUTFLength[40]"/>
      <input title="顺序号" name="seqNO" style="width:300px;" type="text" class="easyui-numberbox"/>
      <textarea title="机构描述" name="descr" colspan="3" rows='5' style="width:100%;" class="easyui-validatebox" validType="maxUTFLength[1000]"></textarea>
      <input name="orgID" type="hidden">
    </form>
  </div>
  <div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
    <a class="easyui-linkbutton" onClick="btnOrgAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
    <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
  </div>
</body>
<script type="text/javascript">

   var orgID = "${param.orgID}";
   $(function(){
	   $("#parentOrgCombotree").combotree({
		    lines:true ,
		    url:"${app}/sys/org/queryOrgTreeWithRoot.do"
		  });
	   if(orgID!=""){
		   hitooctrl.eamsOperPost("${app}/sys/org/queryOrgByID.do", {orgID:orgID}, function(paraMap){
			   if(paraMap!=null&&paraMap.Org!=null){
				   $("#formOrgModify").form('setData',paraMap.Org);
			   }
	    });
	   }
   })
   
  //保存修改
  function btnOrgAddSaveClick(obj){
	  var valid = $("#formOrgModify").form('validate');
	  if(!valid){
		  return;
	  }
	  
	  hitooctrl.eamsOperPost("${app}/sys/org/modifyOrg.do", $("#formOrgModify").form("getData"), function(paraMap){
         parent.initPage(paraMap);
         parent.hitooctrl.closeWin();
	   },obj);
  }

</script>
</html>