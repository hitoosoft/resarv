<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>组织机构管理</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>

<body class="easyui-layout">
	<div region="west" class="left-panel" border="false">
		<div class="easyui-panel" border="false" fit="true">
				<ul id="orgTree" class="easyui-tree"></ul>
	    </div>			
	</div>
	<div region="center" border="false" class="htborder-left">
	 <div class="easyui-layout" fit="true">
	   <div region="north" border="false">
			<div class='perm-panel'>
				<a class="easyui-linkbutton" id="EAMS00SYS010102" onClick="btnOrgAddClick();" icon="icon-add" plain="true">新增</a>
				<a class="easyui-linkbutton" id="EAMS00SYS010103" onClick="btnOrgAddSubClick();" icon="icon-add" plain="true">新增下级</a>
				<a class="easyui-linkbutton" id="EAMS00SYS010104" onClick="btnOrgModifyClick();" icon="icon-edit" plain="true">修改</a>
				<a class="easyui-linkbutton" id="EAMS00SYS010105" onClick="btnOrgDelClick();" icon="icon-remove-edit" plain="true">删除</a>
		  </div>
	   </div>
     <div region="center" border="false">
		   <form id="formOrgDtl" columns='2' class="easyui-form">
	      <input title="机构编码" name="orgCod" colspan="2" style="width:380px;" type="text" disabled="disabled" />
	      <input title="机构名称" name="orgNam" colspan="2" style="width:380px;" type="text" disabled="disabled" />
	      <input title="职能区划" name="areaCod"  colspan="2" style="width:386px;" syscode="AREACOD" class="easyui-combobox"   disabled="disabled"
	      panelHeight="auto" editable="false"  />
	      <input title="上级机构" name="parentNam" colspan="2" style="width:380px;"type="text"  disabled="disabled" />
	      <input title="负责人" name="leader" colspan="2" style="width:380px;" type="text" disabled="disabled" />
	      <input title="联系人" name="contacts" colspan="2" style="width:380px;" type="text" disabled="disabled" />
	      <input title="联系电话" name="phones" colspan="2" style="width:380px;" type="text" disabled="disabled" />
	      <textarea title="机构描述" name="descr"  colspan="3"  rows='5' disabled="disabled" style='width:380px;'></textarea>
		   </form>
	   </div>
	 </div>
  </div>
</body>
<script type="text/javascript">
	var orgRootNode = {
		id : "EAMSROOT",
		text : "顶级机构"
	};
	
	/**
	 *打开页面加载的信息
	 */
	$(document).ready(function() {
		hitooctrl.loadPageBtnAuthority(parent.hitoo.curMenuID, initPage());
	});
	
	/**
	 * 刷新树
	 */
	function initPage(paraMap) {
		$("#orgTree").tree({
			lines :true,
			url : "${app}/sys/org/queryAllOrgTree.do",
			onClick : function(node) {
				orgDealNode();
			},
			onLoadSuccess : function(node, data) {
				//树刷新成功之后
				orgDealNode(paraMap);
			}
		});
	}

	/**
	 * 各种动作之后（新增、修改、删除等动作之后，加载完全机构树）,对Node的处理
	 * 1.定义当前机构和当前机构的父机构，如果其父机构为空，设置为根机构 
	 * 2.定位并选中
	 * 3.显示明细
	 */
	function orgDealNode(paraMap) {
		
		//选择某个节点
		if (paraMap) {
			var nodeid = paraMap.ID;
			if (nodeid) {
				var searchNode = $("#orgTree").tree("find", nodeid);	
				if(searchNode){
					$('#orgTree').tree("expandTo", searchNode.target);
					$('#orgTree').tree('select', searchNode.target);
				}
			}
		}
		
		//当前选择的节点
		var curNode = $('#orgTree').tree("getSelected");
		if (!curNode) {
			//如果没有选择的节点，则默认选择第一个节点，即根节点
			curNode = $('#orgTree').tree("getRoot");
			
			if(curNode){
				$('#orgTree').tree("expandTo", curNode.target);
				$('#orgTree').tree('select', curNode.target);
			}else{
				//如果没有节点，清空右侧formn内容，则直接返回
				$('#formOrgDtl').hide();
				return;
			}
		}
		
		
		var parentNode =  $("#orgTree").tree("getParent", curNode.target);
		if (parentNode == null) {
			parentNode = orgRootNode;
		}
		
		$('#formOrgDtl').form("setData", curNode.attributes);
		/** 
		上级机构的设值，有两种方式，第1，上级机构为combotree，根据ID设置关联机构树，连后台加载，orgShowDtl()不需要额外操作。
		                                                                      第2，上级机构为text，需要orgShowDtl()setValue之后重新设置一下  parentNam
		*/
		$("#formOrgDtl input[name=parentNam]").val(parentNode.text);
		$('#formOrgDtl').show();
	}

	/**
	 * 新增按钮的事件
	 */
	function btnOrgAddClick() {
		var paraMap = {};
		paraMap.parentID = orgRootNode.id;
		paraMap.parentNam = orgRootNode.text;
		
		var curNode = $('#orgTree').tree("getSelected");
		if(curNode!=null){
			var parentNode =  $("#orgTree").tree("getParent", curNode.target);
			if (parentNode!=null) {
				paraMap.parentID  = parentNode.id;
				paraMap.parentNam  = parentNode.text;
			}
		}
		
		var url = '${app}/sys/org/forwardAddOrg.do';
		var width = 470;
		var height = null;
		var title = "新增机构";
		
		hitooctrl.openWin(url, paraMap, width, height, title);
	}
	
	/**
	 * 新增下级按钮的事件 
	 */
	 function btnOrgAddSubClick() {
	  //判断
	  var curNode = $('#orgTree').tree("getSelected");
		if (curNode == null) {
			$.messager.alert("提示", "请先选中一个机构！", "info");
			return;
		}
		var paraMap = {};
		paraMap.parentID = curNode.id;
	  paraMap.parentNam = curNode.text;
		var url = '${app}/sys/org/forwardAddOrg.do';
		var width = 470;
		var height = null;
		var title = "新增下级机构";
		
		hitooctrl.openWin(url, paraMap, width, height, title);
	}

	/**
	 * 修改按钮的事件 
	 */
	 function btnOrgModifyClick() {
			//判断
			var curNode = $('#orgTree').tree("getSelected");
			if (curNode == null) {
				$.messager.alert("提示", "请先选中一个需要修改的机构！", "info");
				return;
			}
			var url = '${app}/sys/org/forwardModifyorg.do';
			var paraMap ={'orgID':curNode.id};
			var width = 470;
			var height = null;
			var title = "修改机构";
			
			hitooctrl.openWin(url, paraMap, width, height, title);
		}

	/**
	 *删除按钮的事件 
	 */
	function btnOrgDelClick() {
		//判断是否选择了一个机构
		var curNode = $('#orgTree').tree("getSelected");
		
		if (curNode == null) {
			$.messager.alert("提示", "请先选中一个需要删除的机构！", "info");
			return;
		}
		
		//判断选择的机构是否有下级机构，如果有下级机构，则不能删除
		var childrenNodes = $("#orgTree").tree("getChildren",curNode.target);
		if (childrenNodes != null && childrenNodes.length > 0) {
			$.messager.alert("提示","该机构存在下级机构，不能删除！", "info");
			return;
		} 
		
		$.messager.confirm("提示", "确定要删除组织机构[" + curNode.attributes.orgNam
				+ "]吗？", function(result) {
			if (result == false) {
				return;
			}
			var urlparms = {};
			urlparms.delID = curNode.id;
			hitooctrl.eamsOperPost("${app}/sys/org/delOrg.do", urlparms, initPage);
		});
	}

</script>
</html>