<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>新增全宗</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="center" border="false" class="htborder-left">
   <form id="formGnlArvAdd" columns='1' class="easyui-form">
    <input title="全宗编码" name="gnlArvCod" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true"/>
    <input title="全宗名称" name="gnlArvNam" type="text" style="width:300px;" class="easyui-validatebox"  validType="maxUTFLength[32]" required="true"/>
    <input id="queryAllOrgTree" title="立档单位" name="orgID" type="text" style="width:300px;" class="easyui-combotree"   required="true" />
    <input title="顺序号" name="seqNO" type="text" colspan='2' class="easyui-numberbox"  style='width: 98.5%;'/>
    <textarea title="全宗描述" name="descr" colspan="3" rows='5' style='width: 98%;'></textarea>
   </form>
  </div>
	<div region="south" border="false" style="text-align:right;float:right;padding:3px;" class="htborder-top">
		 <a class="easyui-linkbutton" onClick="btnGnlArvAddSaveClick(this);" iconCls="icon-save">保存</a> &nbsp;&nbsp;
		 <a class="easyui-linkbutton" onClick="parent.hitooctrl.closeWin();" iconCls="icon-cancel">取消</a>
	</div>
<body>
<script type="text/javascript">

  $(function(){
	  //加载机构树
	  $("#queryAllOrgTree").combotree({
		  lines:true ,
	    url:"${app}/sys/org/queryAllOrgTree.do"
	  });
  });

  //保存新增全宗
  function btnGnlArvAddSaveClick(obj) {
    //保存之前需要前台校验
    var valid = $("#formGnlArvAdd").form("validate");
    if(!valid){
      return false;
    }
    
    hitooctrl.eamsOperPost("${app}/bas/gnlarv/addGnlArv.do",$("#formGnlArvAdd").form("getData"),function(paraMap){
      parent.initGnlArvTree(paraMap);
      parent.hitooctrl.closeWin();
    }, obj);
  }
</script>
</body>
</html>