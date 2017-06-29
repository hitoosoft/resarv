<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@ include file="/commons/head.jspf"%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="shortcut icon" href="${app}/favicon.ico"/>
  	<link rel="bookmark" href="${app}/favicon.ico"/>
	<title>${app_name}</title>
</head>
<body class="easyui-layout" id="indexPageLos">
	<div data-options="region:'north',border:false">
		<table class="tb-top">
			<tr>
				<td class="td-logo" valign="middle">
					<div onclick="childMenuList('fixed')" class="div-logo"></div>
				</td>
				<td id="menu-top-container" style="padding-left:100px;">
					<c:forEach items="${menuList}" var="menu">
						<div class="menu-first" onclick="childMenuList('${menu.id }')">${menu.text }</div>
					</c:forEach>
				</td>
				<td style="width:200px;">
					<a class="usr-icon">${login_user.usrNam}</a>
					<a href="${app }/logout.do" class="logout-icon">退出</a>
				</td>
			</tr>
		</table>
	</div>
	
	<div data-options="region:'west',border:false,collapsed:true" style="width:210px;">
  		<div class="easyui-layout" fit="true">
  	  		<div data-options="region:'east'" class="menu-switch-img" onclick="$('#indexPageLos').layout('collapse', 'west');"></div>
  	  		<div id="menudiv" data-options="region:'center',border:false" style="background-color:#e7edf1;">
  	  			<div id="fixed" class="easyui-accordion" border="false"  fit="false" animate="true" style="display:none;">
					<div iconCls="icon-user"  title="我的账户" style="background-color: #e7edf1;overflow:auto;">
						<div class="menu-single">
							<a class="menu-a" href="javascript:void(0);" onclick="modifyUserInfo()"><span class="icon-user">基本信息</span></a>
						</div>
						<div class="menu-single">
							<a class="menu-a" href="javascript:void(0);" onclick="modifyUserPwd()"><span class="icon-key">修改密码</span></a>
						</div>
				   	</div>
				    <div iconCls="icon-download-file"  title="常用下载" style="background-color: #e7edf1;overflow: auto;">
						<div class="menu-single">
							<a class="menu-a" href="javascript:void(0);" onclick="downLoadAssistFile('instructions');"><span class="icon-download-file">系统说明书</span></a>
						</div>
						<div class="menu-single">
							<a class="menu-a" href="javascript:void(0);" onclick="downLoadAssistFile('scandevice');"><span class="icon-download-file">高拍仪扫描控件</span></a>
						</div>
				   	</div>
				</div>
  	  			<c:forEach items="${menuList}" var="menu1">
					<div id="${menu1.id }" class="easyui-accordion" style="display:none;" border="false" fit="false" animate="true">
						<c:forEach items="${menu1.children}" var="menu2">
							<div iconCls="${menu2.iconCls }"  title="${menu2.text }" style="overflow: auto;">
						    	<c:forEach items="${menu2.children}" var="menu3">
									<div class="menu-single">
										<a class="menu-a" href="javascript:void(0);" onclick="hitooctrl.addIndexTab('${menu3.id}', '${menu3.attributes.funcUrl}', '${menu3.text}', '${menu3.iconCls}')">
											<span class="${menu3.iconCls}">${menu3.text}</span>
										</a>
									</div>
								</c:forEach>
						   	</div>
						</c:forEach>
					</div>
				</c:forEach>
  	  		</div>
  	  		
  		</div>
	</div>
	
	<div id="userpasswin" class="easyui-window" title="修改密码" style="width:280px;height:200px;display:none;"
  		data-options="iconCls:'icon-key',collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true,resizable:false">
	  	<form id="usrFormPass" columns='1' class="easyui-form">
			<input title="旧密码" type="password" name="oldPwd" class="easyui-validatebox" required="true">
			<input title="新密码" type="password" name="newPwd" class="easyui-validatebox" required="true">
			<input title="确认密码" type="password" name="newPwdSure" class="easyui-validatebox" required="true">
		</form>
	 	<div style="width:100%;height:30px;position:absolute;bottom:10px;text-align:center;margin:0 auto;">
	   		<a class="easyui-linkbutton" iconCls="icon-save" onclick="saveUserPass();">保存</a>
	   		<a class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#userpasswin').window('close');">取消</a>
	 	</div>
	</div>
	 
	<div data-options="region:'center',border:false" id="contextmenudiv" style="display:none;">
    	<div id="indexPageTabs" class="easyui-tabs" border="false" fit="true" >
    		
    	</div>
    	<div id="tabsMenu" class="easyui-menu" style="width:120px;">
      		<div iconCls="icon-remove" name="close">关闭</div>
      		<div iconCls="icon-minus-sign" name="other">关闭其他</div>
      		<div iconCls="icon-remove-circle" name="all">关闭所有</div>
    	</div>
  	</div>
</body>
<script type="text/javascript">
	var hitoodictionary = ${cacheEnumData};
	var hitoo = {};
	hitoo.globalUsrCod = "${sessionScope.login_user.usrCod}"; 
	
	var tabMap = {};
	$(function () {
		$('#menu-top-container div').bind('click', function () {
			if ($(this).hasClass('menu-first-active')) {
				if ($($('#indexPageLos').layout('panel', 'west')).panel('options').collapsed) {
					$('#indexPageLos').layout('expand', 'west');
				} else {
					$('#indexPageLos').layout('collapse', 'west');
				}
			} else {
				$('#menu-top-container div').removeClass('menu-first-active');
				$(this).addClass('menu-first-active');
				if ($($('#indexPageLos').layout('panel', 'west')).panel('options').collapsed) {
					$('#indexPageLos').layout('expand', 'west');
				}
			}
		});
		
		$('#indexPageTabs').tabs({
			onContextMenu:function(e, title) {
				e.preventDefault();
				$('#tabsMenu').menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			},
			onSelect:function (title) {
				hitoo.curMenuID = tabMap[title];
			}
		});
		$('#tabsMenu').menu({
			onClick:function (item) {
				closeTab(this, item.name);
			}
		});
		function closeTab(menu, type) {
			var curTabTitle = $(menu).data("tabTitle");
			var tabs = $("#indexPageTabs");
			if (type === 'close') {
				if (curTabTitle === '首页') return;
				tabs.tabs("close", curTabTitle);
				return;
			}
			var allTabs = tabs.tabs("tabs");
			var closeTabsTitle = [];
			$.each(allTabs, function () {
	            var opt = $(this).panel("options");
	            if (opt.closable && opt.title != curTabTitle && type === "other") {
	                closeTabsTitle.push(opt.title);
	            } else if (opt.closable && type === "all") {
	                closeTabsTitle.push(opt.title);
	            }
	        });
			for (var i = 0; i < closeTabsTitle.length; i++) {
				tabs.tabs("close", closeTabsTitle[i]);
			}
		}
		hitooctrl.addIndexTab('', '${app}/welcome.do', '首页', 'icon-house');
		
		//$("#fixed").css("display", "block");
		$("#menu-top-container div:first").click();
		$("#contextmenudiv").css("display", "block");
		$('#indexPageLos').layout('expand', 'west');
	});
	
	function closeTab(tabName){
		$('#indexPageTabs').tabs('close', tabName);
	}
	
	function childMenuList(id) {
		if('fixed' == id){
			if ($($('#indexPageLos').layout('panel', 'west')).panel('options').collapsed) {
				$('#indexPageLos').layout('expand', 'west');
			} else {
				$('#indexPageLos').layout('collapse', 'west');
			}
		}
		$("div.easyui-accordion").css("display", "none");
		$("#"+id).css("display", "block");
		$.parser.parse('#menudiv');
	}
	
	//基本信息
	function modifyUserInfo(){
    	var paraMap = {};
    	var width = 580;
    	var height = null;
    	var title = "修改基本信息";
		hitooctrl.openWin("${app}/sys/usr/forwardModifyInfo.do", paraMap, width, height, title);
	}
	
	//修改用户密码窗口
	function modifyUserPwd(){
		$('#userpasswin').window('open');
		$("#userpasswin").css("display", "block");
	}
	
	//修改用户密码
	function saveUserPass(){
		if(!$("#usrFormPass").form("validate")){
			return;
		} 
		hitooctrl.eamsOperPost("${app}/sys/usr/modifyPwd.do",$("#usrFormPass").form("getData"),function(paraMap){
			$('#userpasswin').window('close');
		});
	}
	
	//常用下载
	function downLoadAssistFile(fileID){
		if(fileID==null||fileID==""){
		    return ;
		  }
		window.location.href="${app}/sys/download/downLoadAssistFile.do?fileID="+fileID;
	}
</script>
</html>