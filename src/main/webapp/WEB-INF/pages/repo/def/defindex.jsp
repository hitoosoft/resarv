<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>库房管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
  <div region="west" class="left-panel" border="false">
      <div class="easyui-panel" border="false" fit="true" title="库房">
	       <ul id="defTree" class="easyui-tree"></ul>
      </div> 
  </div>
  <div region="center" class="htborder-left" border="false">
  <div  class="easyui-layout" fit="true">
	  <div region="north" border="false">
		    <div class="perm-panel">
		      <a id="EAMSREP00010002" class="easyui-linkbutton"
		       onClick="addDef();" icon="icon-add" plain="true">新增库房</a> 
		      <a class="easyui-linkbutton" id="EAMSREP00010003"
		       onClick="modifyDef();" icon="icon-edit" plain="true">修改库房</a>
		      <a class="easyui-linkbutton" id="EAMSREP00010004"
		       onClick="deleteDef();" icon="icon-remove-edit" plain="true">删除库房</a> 
		    </div> 
		    <form id="formDef" class="easyui-form" columns="2">
		      <input title="所属全宗" id="gnlArvID" name="gnlArvID" colspan="2" disabled="disabled" class="easyui-combobox" style="width: 410px;"/>
		      <input title="库房名称" name="repoNam" type="text" colspan="2" disabled="disabled" style='width: 99%;'>
		      <input title="联系人" name="contacts" type="text" disabled="disabled" style='width: 98.5%;'>
		      <input title="顺序号" name="seqNO" type="text" disabled="disabled" style='width: 99%;'>
		      <textarea title="库房描述" name="descr" colspan="3" rows='5'
		        disabled="disabled" style='width: 98.5%;'></textarea>
		    </form>
	    </div>
	     <div region="center" class="htborder-top" border="false">
	        <div id="tbEqpt" class="perm-panel">
		    <a id="EAMSREP00020002" class="easyui-linkbutton" onClick="btnRepoEqptAddClick();" icon="icon-add" plain="true">添加设备</a>
	        <a id="EAMSREP00020003" class="easyui-linkbutton" onClick="btnRepoEqptModifyClick();" icon="icon-edit" plain="true">修改设备</a>
	        <a id="EAMSREP00020004" class="easyui-linkbutton" onClick="btnRepoEqptDelClick();" icon="icon-remove-edit" plain="true">删除设备</a>
	        <a id="EAMSREP00020010" class="easyui-linkbutton" onClick="btnRepoCellAutoAddClick();" icon="icon-arrow-out" plain="true">智能划分单元格</a>
		    </div> 
		    <table id="eqptDg"></table>
	     </div>
    </div>
  </div>
</body>
<script type="text/javascript">
$(document).ready(function() {
    //先初始化页面的权限按钮，再回调相应的页面函数
    hitooctrl.loadPageBtnAuthority(parent.hitoo.curMenuID, initPage());
  });

  function initPage(){
	 $("#gnlArvID").combobox({
	 	url:'${app}/bas/gnlarv/queryGnlArv.do',    
        valueField:'id',    
        textField:'text'
	 })
     $('#eqptDg').datagrid({
			idField : "eqptID",
		    height: 'auto',
			fitColumns: true,
			fit:true,
			toolbar:'#tbEqpt',
			border:false,
			singleSelect:true,
			pagination:false,
			striped:true,
		    columns:[[
				{field:'eqptID',title:'设备ID',hidden:true,width:120},
				{field:'eqptNam',title:'设备名称',width:120},
				{field:'partNum',title:'排数量',width:60},
				{field:'levNum',title:'列数量',width:60},
				{field:'seqNO',title:'顺序号',width:60},
				{field:'descr',title:'描述',width:120}
		    ]]
		});
     
     initDefTree() ;
		
  }
  
  //刷新树
  function initDefTree(paraMap){
	  $("#defTree").tree({
		  url: '${app}/repo/def/queryDefTree.do',
		  onSelect : function(node){
			  $('#formDef').form("setData",node.attributes);
			  loadEqpt();
		  },
		  onLoadSuccess : function(node, data){
			  //选择某个节点
			    if (paraMap != null) {
			      var nodeid = paraMap.ID;
			      if (nodeid != null) {
			        var searchNode = $("#defTree").tree("find", nodeid); 
			        if(searchNode!=null){
			          $('#defTree').tree("expandTo", searchNode.target);
			          $('#defTree').tree('select', searchNode.target);
			        }
			      }
			    }
			    
			    //当前选择的节点
			    var curNode = $('#defTree').tree("getSelected");
			    if (curNode == null) {
			      //如果没有选择的节点，则默认选择第一个节点，即根节点
			      curNode = $('#defTree').tree("getRoot");
			      if(curNode!=null){
			        $('#defTree').tree("expandTo", curNode.target);
			        $('#defTree').tree('select', curNode.target);
			      }else{
			    	  $('#formDef').form("hidden");
			    	  $('#eqptDg').datagrid("loadData",[]);
			          return ;
			      }
			    }
		  }
	  })
  }
  
  //1.显示基本属性值 ,2.根据库房id查询该库房的设备
  function loadEqpt() {
	  var curNode = $("#defTree").tree("getSelected");
	  var param={};
	  param.repoID=curNode.id;
	  $("#eqptDg").datagrid("options").url=  '${app}/repo/eqpt/queryEqptDgWithoutPage.do';
	  $('#eqptDg').datagrid("load",param);
	  $("#eqptDg").datagrid('clearSelections');
  }
  
  //新增库房
  function addDef(){
	  var url = '${app}/repo/def/forwardAddDef.do';
    var paraMap ={};
    var width = 470;
    var height = 300;
    var title = "新增库房";
    hitooctrl.openWin(url, paraMap, width, height, title);
  }
  
  //修改库房
  function modifyDef(){
	  var Node = $("#defTree").tree('getSelected');
	  if(Node==null){
		  $.messager.alert("提示","请选择要的修改的库房！","info");
		  return;
	  }
    var url = '${app}/repo/def/forwardModifyDef.do';
    var paraMap ={};
    paraMap.repoID = Node.id;
    var width = 470;
    var height = 300;
    var title = "修改库房";
    hitooctrl.openWin(url, paraMap, width, height, title);
  }
  
  //删除库房
  function deleteDef(){
	  var curNode = $("#defTree").tree('getSelected');
	  if(curNode==null){
		  $.messager.alert("提示","请选择要的删除的库房！","info");
	    return;
	  }
	  $.messager.confirm("提示","确定要删除["+curNode.attributes.repoNam+"]吗？",function(r){
		  if(!r){
			  return;
		  }
		  var url = '${app}/repo/def/deleteDef.do';
		  var paraMap={};
		  paraMap.repoID = curNode.id;
		  hitooctrl.eamsOperPost(url,paraMap,initDefTree);
	  });
  }
  
  /********************************************************************************
   *                                  设备按钮的事件                                                         *
  ********************************************************************************/
   //新增设备
   function btnRepoEqptAddClick(){
  	 
  	 var curNode = $("#defTree").tree('getSelected');
  	 if(curNode==null){
       $.messager.alert("提示","请选择一个要新增设备的库房！","info");
       return;
     }
  	 var paraMap = {};
  	 paraMap.repoID = curNode.id;
  	 
  	 var url = "${app}/repo/eqpt/forwardAddEqpt.do";
  	 var width = 470;
     var height = 330;
     var title = "新增设备";
     hitooctrl.openWin(url,paraMap,width,height,title);
   }
   
   //删除设备
   function btnRepoEqptDelClick(){
  	 var row = $("#eqptDg").datagrid('getSelected');
  	 if(row==null){
  		 $.messager.alert("提示","请选择你要删除的设备！","info");
  		 return;
  	 }
  	 
  	 $.messager.confirm('提示', '确定删除['+row.eqptNam+']设备吗？', function(r) {
        if (!r) {
          return;
        }
		 var url = "${app}/repo/eqpt/deleteEqpt.do";
		 var paraMap = {};
		 paraMap.eqptID = row.eqptID;
		 hitooctrl.eamsOperPost(url,paraMap,loadEqpt);
  	 });
   }
   
   //修改设备
   function btnRepoEqptModifyClick(){
     var row = $("#eqptDg").datagrid('getSelected');
  	 if(row==null){
  		 $.messager.alert("提示","请选择你要修改的设备！","info");
  		 return;
  	 }
	  	 
  	 var url = "${app}/repo/eqpt/forwardModifyEqpt.do";
     var paraMap = {};
     paraMap.eqptID = row.eqptID;
     var width = 470;
     var height = 330;
     var title = "修改设备";
     hitooctrl.openWin(url,paraMap,width,height,title);
   }
   
   //智能划分单元格，如果已经划分了单元格，不能智能划分了
   function btnRepoCellAutoAddClick(){
	   var row = $("#eqptDg").datagrid('getSelected');
  	   if(row==null){
  		 $.messager.alert("提示","请选择你要划分的设备！","info");
  		 return;
  	   }
      hitooctrl.eamsOperPost("${app}/repo/cell/queryCellByEqptID.do",{eqptID:row.eqptID},function(paraMap){
    	  if(paraMap!=null&&paraMap.Cell!=null){
          $.messager.alert("提示", "该设备已经划分了单元格，不能智能划分！", "info");
          return;
        }
        var url = '${app}/repo/cell/forwardCellAutoAdd.do';
        var paraMap = {}
        paraMap.eqptID = row.eqptID;
        var width = 470;
        var height = 200;
        var title = "智能划分单元格";
        hitooctrl.openWin(url, paraMap, width, height, title);
      });
    }
</script>
</html>