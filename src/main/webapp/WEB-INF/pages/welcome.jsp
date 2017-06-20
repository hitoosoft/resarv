<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/commons/head.jspf"%>
</head>
<body class="easyui-layout">
	<div region="center" border="false">
		<!-- ie7下 滚动条拖动无效，添加position:relative;解决问题 -->
	   	<!-- <div class="easyui-panel" title="信息提醒" style="position:relative;width:500px;height:400px;" data-options="iconCls:'icon-tip'">
	   	</div> -->
	</div>
</body>
<script type="text/javascript">
$(function () {
	//1、查询通知
	hitooctrl.eamsOperPost("${app}/notice/queryUnReadNotice.do",{},function(paraMap){
		if(paraMap.unRead){
			$.messager.show({
				title:'通知提醒',
				msg:'<a href="script:void(0);" onclick="readNotice()">您有未查看的通知</a>',
				timeout:5000,
				showType:'slide'
			});
		}
    });
	//2、查询发文办理
	hitooctrl.eamsOperPost("${app}/issue/queryNotDealIssue.do",{},function(paraMap){
		if(paraMap.infos){
			$.messager.show({
				title:'发文提醒',
				msg:'<a href="script:void(0);" onclick="dealIssue()">您有未办理的发文</a>',
				timeout:5000,
				showType:'slide'
			});
		}
    });
	//3、查询来文办理
	hitooctrl.eamsOperPost("${app}/receoffice/queryNotDealReceoffice.do",{},function(paraMap){
		if(paraMap.fileInfos){
			$.messager.show({
				title:'来文提醒',
				msg:'<a href="script:void(0);" onclick="dealReceoffice()">您有未办理的来文</a>',
				timeout:5000,
				showType:'slide'
			});
		}
    });
});

function readNotice(){
	hitooctrl.addIndexTab('EAMSNOT00620000', '${app}/notice/forwardDocNoticeCheck.do', '通知查看', 'icon-magnifier');
}

function dealIssue(){
	hitooctrl.addIndexTab('EAMS00400040200', '${app}/issue/forwardManage.do', '发文办理', 'icon-undo');
}

function dealReceoffice(){
	hitooctrl.addIndexTab('EAMS00400040100', '${app}/receoffice/forwardManage.do', '来文办理', 'icon-redo');
}
</script>
</html>