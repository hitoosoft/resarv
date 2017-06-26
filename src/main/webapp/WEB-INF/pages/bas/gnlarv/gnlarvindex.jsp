<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>全宗定义</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="west" class="left-panel" border="false">
    <div class="easyui-panel" border="false" fit="true"  title="全宗定义">
        <ul id="gnlArvTree" class="easyui-tree"></ul>
      </div>      
  </div>
  <div region="center" border="false" class="htborder-left">
    <div class='perm-panel'>
     <a class="easyui-linkbutton" id="EAMSBSE00010002" 
       onClick="btnGnlArvAddClick();" icon="icon-add" plain="true">新增</a>
     <a  class="easyui-linkbutton" id="EAMSBSE00010003"
       onClick="btnGnlArvModifyClick();" icon="icon-edit" plain="true">修改</a>
     <a class="easyui-linkbutton" id="EAMSBSE00010004"
       onClick="btnGnlArvDeleteClick();" icon="icon-remove-edit" plain="true">删除</a>
    </div>  
   <form id="formGnlArvDtl" columns='1' class="easyui-form">
    <input title="全宗编码" name="gnlArvCod" type="text" disabled="disabled" style="width:300px;"/>
    <input title="全宗名称" name="gnlArvNam" type="text" disabled="disabled" style="width:300px;"/>
    <input title="立档单位" name="orgNam" type="text" disabled="disabled" style="width:300px;"  />
    <input title="全宗序号" name="seqNO" type="text" disabled="disabled"  style="width:300px;"/>
    <textarea title="全宗描述" name="descr" colspan="3" rows='5'
      disabled="disabled" style='width: 98%;'></textarea>
    <input name="orgID" type="hidden">
   </form>
  </div>
</body>
<script type="text/javascript">

  $(document).ready(function() {
    //先初始化页面的权限按钮，再回调相应的页面函数
    hitooctrl.loadPageBtnAuthority(parent.hitoo.curMenuID, initPage());
  });

  function initPage(){
	   initGnlArvTree() ;
  }
  
  //刷新树
  function initGnlArvTree(paraMap) {
    $("#gnlArvTree").tree({
    	lines :true,
      url : "${app}/bas/gnlarv/queryGnlArvTree.do",
      onClick : function(node) {
        gnlArvDealNode();
      },
      onLoadSuccess : function(node, data) {
        //树刷新成功之后
        gnlArvDealNode(paraMap);
      }
    });
  }
  //定位全宗节点
  function gnlArvDealNode(paraMap) {
    //选择某个节点
    if (paraMap != null) {
      var nodeid = paraMap.ID;
      if (nodeid != null) {
        var searchNode = $("#gnlArvTree").tree("find", nodeid); 
        if(searchNode!=null){
          $('#gnlArvTree').tree("expandTo", searchNode.target);
          $('#gnlArvTree').tree('select', searchNode.target);
          
        }
      }
    }
    
    //当前选择的节点
    var curNode = $('#gnlArvTree').tree("getSelected");
    if (curNode == null) {
      //如果没有选择的节点，则默认选择第一个节点，即根节点
      curNode = $('#gnlArvTree').tree("getRoot");
      
      if(curNode!=null){
        $('#gnlArvTree').tree("expandTo", curNode.target);
        $('#gnlArvTree').tree('select', curNode.target);
      }else{
        return ;
      }
    }
    //显示基本属性值
    $('#formGnlArvDtl').form("setData",curNode.attributes);
  }


  //新增全宗
  function btnGnlArvAddClick() {
	  var url = '${app}/bas/gnlarv/forwardAddGnlarv.do';
    var paraMap ={};
    var width = 470;
    var height = 330;
    var title = "新增全宗";
    hitooctrl.openWin(url, paraMap, width, height, title);
  }
  
  //修改全宗
  function btnGnlArvModifyClick() {
    //判断
    var curNode = $("#gnlArvTree").tree('getSelected');
    if (curNode == null) {
      $.messager.alert("提示", "请先选中一个需要修改的全宗！", "info");
      return;
    }
    var url = '${app}/bas/gnlarv/forwardModifygnlarv.do';
    var paraMap = {};
    paraMap.gnlarvID = curNode.id; 
    var width = 470;
    var height = 330;
    hitooctrl.openWin(url, paraMap, width, height, "修改全宗");
  }
  
  //删除全宗
  function btnGnlArvDeleteClick() {
    //判断
    var curNode = $("#gnlArvTree").tree('getSelected');
    if (curNode == null) {
      $.messager.alert("提示", "请先选中一个要删除的全宗！", "info");
      return;
    }

    $.messager.confirm("提示", "确定要删除全宗[" + curNode.attributes.gnlArvNam
        + "]吗？",
        function(result) {
          if (result == false) {
            return;
          }
          var urlparms = {};
          urlparms.gnlarvID = curNode.id;
          hitooctrl.eamsOperPost("${app}/bas/gnlarv/deleteGnlArv.do", urlparms,
              initGnlArvTree);
        });
  }
</script> 
</html>