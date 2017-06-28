<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>设备管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="west" class="left-panel" border="false" title="库房设备">
		<div class="easyui-layout" fit="true">
			<div region="north" border="false" style="height:180px;" class="htborder-bottom">
		    	<div class="easyui-panel" border="false" fit="true" >
			      <ul id="repoDefAndEqptTree"></ul>
		        </div>
		    </div>
		    <div region="center" border="false">
			    <div class="easyui-panel" border="false" fit="true" title="设备的排">
			      <ul id="cellReptIDAndPartNO" fit="true"></ul>
			    </div>
		    </div>
		</div>
	</div>
  
	<div region="center" border="false" class="htborder-left">
		<div class="easyui-layout" fit="true">
         	<div region="north" border="false" style="height:27px;" class="htborder-bottom">
				<div class='perm-panel' >
		          	<a id="EAMSREP00020016" class="easyui-linkbutton" onClick="btnRepoCellPartNOAddClick();" icon="icon-layers" plain="true">添加排</a>
		         	<a id="EAMSREP00020011" class="easyui-linkbutton" onClick="btnRepoCellAddClick();" icon="icon-add" plain="true">添加单元格</a>
		         	<a id="EAMSREP00020013" class="easyui-linkbutton" onClick="btnRepoCellDelClick();" icon="icon-remove-edit" plain="true">删除单元格</a>
		         	<a id="EAMSREP00030002" class="easyui-linkbutton" onClick="btnCellContentClick();" icon="icon-calculator" plain="true">内容管理</a>
		         	<!-- 材料的入库上架保存 -->
		         	<c:if test="${param.saveInRepo=='1'}">
		         	<a show="true" class="easyui-linkbutton" onClick="btnInRepoSave(this);" icon="icon-save" plain="true">入库保存</a>
		         	</c:if>
		       
		        	<!-- 档案的库房位置分配 -->
		         	<c:if test="${param.allocateCell=='1' || allocateCell == '1'}">
		              	<a show="true" class="easyui-linkbutton" onClick="btnResetVolumn();" icon="icon-page" plain="true">设定格容量</a>
		         	</c:if>
	        	</div>
         	</div>
           	<div region="center" border="false"  style="padding-left:5px;padding-top:5px;">
            	<div id="repoCellDiv" class="easyui-panel" fit="true" border="false" ></div>
           	</div>
		</div>
  	</div>
</body>

<script type="text/javascript">
	//入库信息
	var pageEntityType="${entityType}";
	var pageEntityIDs="${param.entityIDs}";
	
	var pageRepoDef = false; //库房
	$(document).ready(function() {
		if(pageEntityIDs==null||pageEntityIDs.length<=0){
	 		pageEntityIDs="${entityIDs}";
		}
		//先初始化页面的权限按钮，再回调相应的页面函数
		hitooctrl.loadPageBtnAuthority(window.top.hitoo.curMenuID, repoDefAndEqptTree());
	});

	function repoDefAndEqptTree(paraMap){
		var gnlArvID = '${param.gnlArvID}';
		$("#repoDefAndEqptTree").tree({
	    	lines : true,
	    	url: '${app}/repo/eqpt/queryRepoDefAndEqptTree.do?gnlArvID=' + gnlArvID,
	    	onSelect : function(node){
	    		var parent = $("#repoDefAndEqptTree").tree('getParent', node.target);
	        	if(!parent){//库房
	          		pageRepoDef = true;
	        	}else{
	          		pageRepoDef = false;
	        	}
	        	cellReptIDAndPartNO(node.id);
	    	},
	    	onLoadSuccess : function(node, data){
	    		$("#repoDefAndEqptTree").tree('expandAll', data.target);
	    		var curNode = $("#repoDefAndEqptTree").tree('getRoot');
	    		if(curNode){
	    			var childreNodes = $("#repoDefAndEqptTree").tree('getChildren', curNode.target);
	    			if(childreNodes!=null&&childreNodes.length>0){
	    				$("#repoDefAndEqptTree").tree('select', childreNodes[0].target);
	    			}else{
	    				$("#repoDefAndEqptTree").tree('select', curNode.target);
	    			}
	    		}
	    	}
		});
	}

	//单元格排号
 	function cellReptIDAndPartNO(eqptID){
	 	if(pageRepoDef){
		 	$("#repoCellDiv").hide();
	 	}else{
			if(eqptID==null){
			 	var curNode = $("#repoDefAndEqptTree").tree('getSelected');
			 	eqptID = curNode.id;
		 	}
		 	$("#repoCellDiv").show();
	 	}
	 
	 	$("#cellReptIDAndPartNO").tree({
	     	url:"${app}/repo/cell/queryCellPartNOByEqptID.do?eqptID="+eqptID,
	     	onSelect : function(node){
		     	queryCellByEqptIDAndPartNO(eqptID, node.id);
	     	},
	     	onLoadSuccess : function(node, data){
         		if(data ==null || data.length <= 0){
            		$("#repoCellDiv").hide();
          		}else{
		        	var rootNode = $("#cellReptIDAndPartNO").tree("getRoot");
		        	if (rootNode != null) {
		          		$("#cellReptIDAndPartNO").tree("select", rootNode.target);
		        	}
            		$("#repoCellDiv").show();
          		}
	     	}
	   	});
 	}
 
 	//单元格实例
 	function queryCellByEqptIDAndPartNO(eqptID, partNO){
	 	var url = "${app}/repo/cell/queryCellByEqptIDAndPartNO.do";
	 	var paraMap = {};
	 	if(eqptID==null){
		 	var curNode = $("#repoDefAndEqptTree").tree('getSelected');
		 	if(curNode==null){
			 	return;
		 	}
	     	eqptID = curNode.id;
	 	}
	 	if(partNO==null){
		 	var curPart = $("#cellReptIDAndPartNO").tree('getSelected');
		 	if(curPart==null){
			 	return;
		 	}
		 	partNO = curPart.id;
	 	}

	 	paraMap.eqptID = eqptID;
	 	paraMap.partNO = partNO;
	 	hitooctrl.eamsOperPost(url,paraMap,function(backMap){
		 	$('#repoCellDiv').html(backMap.celltableHtml);
		});
 	}
 
	function btnResetVolumn(){
		 var selectedCellIDs="";
		 $('input:checkbox[name="red"]').each(function(){
	    	 if($(this).attr("checked")){
	    		 if(selectedCellIDs.length>0){
	    			 selectedCellIDs += ",";
	    		 }
	    		 selectedCellIDs += $(this).attr("value");
	    	 }
	    });
		 
		if(selectedCellIDs.length<=0){
			$.messager.alert('提示','请先选择格!','info');
			return;
		}
		 
		$.messager.prompt('提示', '请输入新的容量:', function(r){
			if (r){
				if(!isPInt(r)){
					alert("请输入正整数");
					return;
				}
				var paraMap = {};
				paraMap.selectedCellIDs=selectedCellIDs;
				paraMap.totalNum=r;
				hitooctrl.eamsOperPost("${app}/repo/cell/updateCellVolumn.do",paraMap,function(){
				});
			}
		});
	}
 
//正整数
 function isPInt(str) {
     var g = /^[1-9]*[1-9][0-9]*$/;
     return g.test(str);
 }
  
/********************************************************************************
 *                                  单元格按钮的事件                                                         *
********************************************************************************/
 //新增设备排
 function btnRepoCellPartNOAddClick(){
	 var curNode = $("#repoDefAndEqptTree").tree('getSelected');
	 if(curNode==null || pageRepoDef==true){
		 $.messager.alert("提示","请先选择一个设备！","info");
	   return;
	 }
	 $.messager.confirm('提示', '确定添加新的一排吗？', function(r) {
	      if (!r) {
	        return;
	      }
	      hitooctrl.eamsOperPost("${app}/repo/cell/addEqptPartNO.do",{eqptID:curNode.id},cellReptIDAndPartNO);
	   });
	
 }
 
 	//新增单元格
 	function btnRepoCellAddClick(){
	 	var curNode = $("#repoDefAndEqptTree").tree('getSelected');
	 	if(curNode==null||pageRepoDef==true){
		 	$.messager.alert("提示","请先选择一个设备！","info");
			return;
	 	}
	 	var url = "${app}/repo/cell/forwardAddCell.do";
	 	var paraMap = {};
	 	paraMap.eqptID = curNode.id;
	 	var width = 420;
   		var height = 230;
   		var title = "新增单元格";
   		hitooctrl.openWin(url,paraMap,width,height,title);
 	}

	 //删除单元格
	function btnRepoCellDelClick(){
		var selectedCellIDs="";
		 $('input:checkbox[name="red"]').each(function(){
	    	 if($(this).attr("checked")){
	    		 if(selectedCellIDs.length>0){
	    			 selectedCellIDs += ",";
	    		 }
	    		 selectedCellIDs += $(this).attr("value");
	    	 }
	    });
		if(selectedCellIDs.length<=0){
			$.messager.alert('提示','请先选择格!','info');
			return;
		}
		
	   	$.messager.confirm('提示', '确定删除吗？', function(r) {
	      	if (!r) {
	        	return;
	      	}
			var paraMap = {};
			paraMap.cellIDs = selectedCellIDs;
	      	var url = "${app}/repo/cell/deleteCell.do";
	      	hitooctrl.eamsOperPost(url,paraMap,queryCellByEqptIDAndPartNO);
	   	});
	}
 
	//查看单元格档案
 	function btnCellContentClick(){
   		var curData = $('input:checkbox[name="red"]:checked').val();
   		if(curData==null){
     		$.messager.alert("提示","请选择你查看的单元格档案！","info");
     		return;
   		}
     	var url = "${app}/repo/cell/forwardShowCellContent.do";
	 	var paraMap = {};
	 	paraMap.cellID = curData;
	 	var width = 500;
	 	var height = 400;
	 	var title = "格内案卷/档案";
	 	hitooctrl.openWin(url,paraMap,width,height,title);
 	}
 
 	//入库保存
 	function btnInRepoSave(obj){
 		var selectedCellIDs = "";
		 $('input:checkbox[name="red"]').each(function(){
	    	 if($(this).attr("checked")){
	    		 if(selectedCellIDs.length>0){
	    			 selectedCellIDs += ",";
	    		 }
	    		 selectedCellIDs += $(this).attr("value");
	    	 }
	    });
		if(selectedCellIDs.length <= 0){
			$.messager.alert('提示','请选择单元格!','info');
			return;
		}
		
   		if(pageEntityIDs.length < 0){
	   		$.messager.alert("提示","请选择要入库的档案！","info");
	     	return; 
   		}
   
   		$.messager.confirm('提示', '确定入库吗？', function(r) {
	    	if (!r) return;
			var paraMap = {};
			paraMap.entityType = pageEntityType;
			paraMap.entityIDs = pageEntityIDs;
			paraMap.cellIDs = selectedCellIDs;
	    	var url = "${app}/repo/cell/saveInRepo.do";
	    	hitooctrl.eamsOperPost(url,paraMap,function(){
	    	  	parent.queryVol();
	    	  	parent.hitooctrl.closeWin();
	      	},obj);
		});
 	}
</script>
</html>