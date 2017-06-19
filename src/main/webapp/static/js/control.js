// 定义常量
var hitooctrl = {};
hitooctrl.dicList = "";
hitooctrl.webprojectname = "/docms";

/*******************************************************************************
 * 初始化TAB页面的权限按钮
 * 
 * @param pageFuncID:本页面的权限按钮
 * @param bcp:加载字典之后的回调函数
 * @author qinchao 20131029
 ******************************************************************************/
hitooctrl.loadPageBtnAuthority = function(pageFuncID, afterBCP) {
	// 隐藏页面所有class='perm-panel'下面的a元素
	$('.perm-panel a').hide();
	$('.perm-panel a[show]').show();
	hitooctrl.eamsOperPost(hitooctrl.webprojectname +"/admin/queryBtnMapByPageFuncID.do", {pageFuncID:pageFuncID}, function(paraMap){
		if(paraMap.pageBtns!=null){
			for (var i=0;i<paraMap.pageBtns.length;i++) {
				$('#' + paraMap.pageBtns[i]).show();
			}

		}
		afterBCP;
	});
	
}


/*************************************************
 * 为tab添加页面
 **************************************************/
hitooctrl.createFrame=function createFrame(url) {
	return '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
}

/*************************************************
 * 登录超时，打开弹出窗口
 **************************************************/
hitooctrl.checkLoginOverTime=function checkLoginOverTime(successAfterOper) {
	hitooctrl.eamsOperPost(hitooctrl.webprojectname+"/checkLoginOverTime.do",{},function(backParaMap){
		if(backParaMap!=null&&backParaMap.normalsta!=null&&backParaMap.normalsta=="1"){
			successAfterOper();
		}else{
			hitooctrl.openLoginWin();
		}
	});   
}

hitooctrl.openLoginWin=function openLoginWin() {
	if(hitoo.globalUsrCod==null||hitoo.globalUsrCod.length<=0){
		window.location=hitooctrl.webprojectname+"/login.do";
	}else{
		var url = hitooctrl.webprojectname+'/forwardRelogin.do';
		var paraMap ={};
		paraMap.usrCod=hitoo.globalUsrCod;
		var width = 350;
		var height = 200;
		var title = "您还未登录或者登录超时，请重新登录！";
		hitooctrl.openWin(url, paraMap, width, height, title,99999,true,true);
	}
	
}


/*************************************************
 * 为index的tab添加页面
 **************************************************/
//在index页面打开tab页
hitooctrl.addIndexTab=function(menuID, url, tabName, iconCls) {
	window.top.hitooctrl.addIndexTabFUNC(menuID, url, tabName, iconCls);
}

hitooctrl.addIndexTabFUNC=function(menuID, url, tabName, iconCls){
	if (!url) return;
	if ($('#indexPageTabs').tabs('exists', tabName) == false) {
		hitooctrl.checkLoginOverTime(function (){
			tabMap[tabName] = menuID;
			$('#indexPageTabs').tabs('add', {
				title : tabName,
				content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>',
				iconCls : iconCls,
				fit : true,
				closable : tabName=='首页' ? false:true
			});
			hitoo.curMenuID = tabMap[tabName];
		});
		
	} else {
		$('#indexPageTabs').tabs('select', tabName);
		hitoo.curMenuID = tabMap[tabName];
	}
}

//在index页面下的某个tab页面下新建子tab页
hitooctrl.addTabForPage=function(url, tabName, iconCls, pageTabID) {
	if (url == null || url == "") return;
	if ($('#'+pageTabID).tabs('exists', tabName) == false) {
		hitooctrl.checkLoginOverTime(function (){
			$('#'+pageTabID).tabs('add', {
				title : tabName,
				content : '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>',
				iconCls : iconCls,
				fit : true,
				closable : tabName=='首页' ? false:true
			});
		});
	} else {
		$('#'+pageTabID).tabs('select', tabName);
	}
}


/*******************************************************************************
 * 弹出打开窗口
 ******************************************************************************/
hitooctrl.openWin = (function() {
	var _remotingDiv;
	var _randID = 0;

	function toHtml(obj) {
		var html = '';
		for (field in obj) {
			html += '<input type="hidden" name="' + field + '" value="'
					+ obj[field] + '">';
		}
		return html;
	}
	function createRemotingDiv(id, url, obj) {
		var rDiv = document.createElement('div');
		rDiv.id = id + '_remotingDiv';
		rDiv.form = document.createElement('form');
		rDiv.form.setAttribute('id', id + 'RemotingForm');
		rDiv.form.setAttribute('action', url);
		rDiv.form.setAttribute('target', id);
		rDiv.form.target = id;
		rDiv.form.setAttribute('method', 'post');
		rDiv.form.innerHTML = toHtml(obj);
		rDiv.appendChild(rDiv.form);
		return rDiv;
	}

	function getIframe(id) {
		return "<iframe name='" + id + "' id='" + id
				+ "' style='width:100%;height:99%;' frameborder='0'></iframe>";
	}
	
	function popFrame(url, obj, w, h, t, level, closableMark){
		// 是否显示关闭按钮
		if (!(closableMark === false)) {
			closableMark = true;
		}

		if (level == null || "" == level) {
			level = "";
		}
		if (w == null || "" == w || w == 0) {
			// 设定默认宽度
			w = 700;
		}

		if (h == null || "" == h || 0 == h) {
			// 设定默认高度
			h = 400;
		}

		_remotingDiv = createRemotingDiv('remotingFrame' + level + _randID,
				url, obj);
		document.body.appendChild(_remotingDiv);

		var winSta = "0";// 弹窗标志位，防止onOpen时出现发送两次url的情况 xfh 20131024
		var win = document.getElementById("_iframeWin");
		if (!win) {
			win = $("<div id='_iframeWin' class='easyui-window'></div>")
					.appendTo("body");
			win.window({
				modal : true,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				cache : false,
				resizable : false,
				draggable : false,
				closable : closableMark
			});
		}
		$(win).window({
			title : t,
			closable : closableMark,
			content : getIframe("remotingFrame" + level + _randID),
			onOpen : function() {
				// 为什么发两次请求呢？状态解决它
				if (winSta == "0") {
					winSta = "1";
				} else {
					_remotingDiv.form.submit();
				}
			}
		});
		_randID++;
		var scnHeight = (parent != null && parent.window != null) ? $(
				parent.document).height() : $(document).height();
		var top = (2 * ($(document).height()) - scnHeight - h) / 2;
		if (top < 0) {
			top = 0;
		}
		var winHeight = $(document).height() < h ? $(document).height() - 5 : h;
		var winWidth = $(document).width() < w ? $(document).width() - 5 : w;
		var winLeft = $(document).width() < w ? 2
				: ($(document).width() - w) / 2;
		$(win).window("resize", {
			width : winWidth,
			height : winHeight,
			top : top,
			left : winLeft
		});
		$(win).window("open");
	}

	return function(url, obj, w, h, t, level, closableMark ,igoreLoginOverTimeFlag) {
		if(true==igoreLoginOverTimeFlag){
			popFrame(url, obj, w, h, t, level, closableMark);
		}else{
			hitooctrl.checkLoginOverTime(function (){
				popFrame(url, obj, w, h, t, level, closableMark);
			});
		}
	};
})();

hitooctrl.closeWin = function() {
	$("#_iframeWin").window("close");
}

/*******************************************************************************
 * 前后打印，调用jasper
 ******************************************************************************/
/**
 * 修改PDF打印页面大小，yyz 2014.4.4
 * 打印页面弹窗封装 xfh 20131031
 * 
 * 参数说明：
 * reportCod：打印模板代码
 * paraMap：初始化内容时需要的参数
 * wintitle：打印窗口的标题，默认为‘打印’ * 
 * windowOpenType :以独立窗口的方式打开（值1），以iframe方式打开（值2）
 * 
 * 修改记录：
 * 20140328 添加打印窗口打开方式参数  qinchao
 */
hitooctrl.printReport = function(prepareDataUrl, wintitle, windowOpenType){
	var url = hitooctrl.webprojectname+'/print/forwardReportPrint.do';	
	
	if(!prepareDataUrl){
		$.messager.alert("提示", "调用打印时未指定数据来源！", "info");
		return ;
	}
	if (!wintitle){
		wintitle ="打印";
	}	
	
	//设置默认打开方式，如果不传，以独立窗口打开
	if(!windowOpenType){
		windowOpenType = "1";
	}
	
	if(windowOpenType=="2"){
		var paraMap = {};
		paraMap.reqUrlContent = prepareDataUrl;	
		paraMap.windowOpenType = windowOpenType ;
		hitooctrl.openWin(url, paraMap, 700, 400, wintitle, 'print');
	}else{
		hitooctrl.checkLoginOverTime(function (){
			windowOpenType = "1";
			var aWindowwidth=screen.availWidth/2;   
			var aWindowheight=screen.availHeight/5*4;   
			var aWindowtop=(screen.availHeight - aWindowheight)/2;    
			var aWindowleft=(screen.availWidth - aWindowwidth)/2; 
			var Windowparam0="scrollbars=no,status=no,menubar=no,resizable=yes,location=no"; 
			var windowParams="top=" + aWindowtop + ",left=" + aWindowleft + ",width=" + aWindowwidth + ",height=" 
			          + aWindowheight + "," + Windowparam0 +",resizable=NO";
			window.open(url+"?reqUrlContent="+prepareDataUrl+"&windowOpenType="+windowOpenType,wintitle,windowParams) ;
		});
	}
}

/*******************************************************************************
 * easyui-datagrid页面上的上移和下移 add by qinchao
 ******************************************************************************/
/** 上移 * */
hitooctrl.dgmoveup = function(rowIndex, gridid) {
	if (rowIndex == null || rowIndex <= 0) {
		var row = $("#" + gridid).datagrid('getSelected');
		rowIndex = $("#" + gridid).datagrid('getRowIndex', row);
	}
	hitooctrl._privateDatagridSort('up', rowIndex, gridid);
}

/** 下移 * */
hitooctrl.dgmovedown = function(rowIndex, gridid) {
	if (rowIndex == null || rowIndex <= 0) {
		var row = $("#" + gridid).datagrid('getSelected');
		index = $("#" + gridid).datagrid('getRowIndex', row);
	}
	hitooctrl._privateDatagridSort('down', rowIndex, gridid);
}

hitooctrl._privateDatagridSort = function(type, index, gridname) {
	if ("up" == type) {
		if (index != 0) {
			var toup = $('#' + gridname).datagrid('getData').rows[index];
			var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
			$('#' + gridname).datagrid('getData').rows[index] = todown;
			$('#' + gridname).datagrid('getData').rows[index - 1] = toup;
			$('#' + gridname).datagrid('refreshRow', index);
			$('#' + gridname).datagrid('refreshRow', index - 1);
			$('#' + gridname).datagrid('clearSelections');
			$('#' + gridname).datagrid('selectRow', index - 1);
		}
	} else if ("down" == type) {
		var rows = $('#' + gridname).datagrid('getRows').length;
		if (index != rows - 1) {
			var todown = $('#' + gridname).datagrid('getData').rows[index];
			var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
			$('#' + gridname).datagrid('getData').rows[index + 1] = todown;
			$('#' + gridname).datagrid('getData').rows[index] = toup;
			$('#' + gridname).datagrid('refreshRow', index);
			$('#' + gridname).datagrid('refreshRow', index + 1);
			$('#' + gridname).datagrid('clearSelections');
			$('#' + gridname).datagrid('selectRow', index + 1);
		}
	}

}

/*******************************************************************************
 * 构造的map add by qinchao
 ******************************************************************************/
hitooctrl.HashMap = function() {
	/** Map 大小 * */
	var size = 0;
	/** 对象 * */
	var entry = new Object();

	/** 存 * */
	this.put = function(key, value) {
		if (!this.containsKey(key)) {
			size++;
		}
		entry[key] = value;
	}

	/** 取 * */
	this.get = function(key) {
		if (this.containsKey(key)) {
			return entry[key];
		} else {
			return null;
		}
	}

	/** 删除 * */
	this.remove = function(key) {
		if (delete entry[key]) {
			size--;
		}
	}

	/** 是否包含 Key * */
	this.containsKey = function(key) {
		return (key in entry);
	}

	/** 是否包含 Value * */
	this.containsValue = function(value) {
		for ( var prop in entry) {
			if (entry[prop] == value) {
				return true;
			}
		}
		return false;
	}

	/** 所有 Value * */
	this.values = function() {
		var values = new Array();
		for ( var prop in entry) {
			values.push(entry[prop]);
		}
		return values;
	}

	/** 所有 Value转为string返回 * */
	this.valuesToString = function() {
		var valuesStr = "";
		for ( var prop in entry) {
			valuesStr += entry[prop];
		}
		return valuesStr;
	}

	/** 所有 Key * */
	this.keys = function() {
		var keys = new Array();
		for ( var prop in entry) {
			keys.push(prop);
		}
		return keys;
	}

	/** Map Size * */
	this.size = function() {
		return size;
	}
}

/*******************************************************************************
 * 时间日期定义
 ******************************************************************************/

/**
 * 判断字符串是否是合法的日期 ，如果是合法的日期，返回yyyy-MM-dd格式的日期串，如果不是，返回null
 * 
 * @author qinchao 2014-5-15
 * @param dateString
 */
hitooctrl.getDateStrByFormat = function(dateString) {

	if (dateString == null || "" == dateString) {
		return dateString;
	}

	dateString = dateString.toString();
	dateString = dateString.replace(/\./, "-"); // 把yyyy.MM.dd格式的替换成yyyy-MM-dd
	dateString = dateString.replace(/\//g, "-"); // 把yyyy/MM/dd格式的替换成yyyy-MM-dd

	if (dateString.length == 8) {
		// yyyyMMdd格式的换成yyyy-MM-dd
		dateString = dateString.substr(0, 4) + "-" + dateString.substr(4, 2)
				+ "-" + dateString.substr(6);
	}
	return dateString;
}

/**
 * 判断字符串是否是合法的日期
 */
hitooctrl.isDate = function(dateString) {
	if (dateString == null || "" == dateString) {
		return false;
	}
	var flag = true;

	dateString = dateString.toString();
	dateString = dateString.replace(/\./, "-"); // 把yyyy.MM.dd格式的替换成yyyy-MM-dd
	dateString = dateString.replace(/\//g, "-"); // 把yyyy/MM/dd格式的替换成yyyy-MM-dd

	if (dateString.length == 8) {
		// yyyyMMdd格式的换成yyyy-MM-dd
		dateString = dateString.substr(0, 4) + "-" + dateString.substr(4, 2)
				+ "-" + dateString.substr(6);
	}

	DATE_FORMAT = /^[0-9]{4}-(0[1-9]|1[0-2])-((0[1-9])|1[0-9]|2[0-9]|3[0-1])$/;
	if (!DATE_FORMAT.test(dateString)) {
		flag = false;
	} else {
		flag = true;

		// 获得年
		var year = dateString.substr(0, dateString.indexOf('-'));
		// 下面操作获得月份
		var transition_month = dateString
				.substr(0, dateString.lastIndexOf('-'));
		var month = transition_month.substr(
				transition_month.lastIndexOf('-') + 1, transition_month.length);
		if (month.indexOf('0') == 0) {
			month = month.substr(1, month.length);
		}
		// 下面操作获得日期
		var day = dateString.substr(dateString.lastIndexOf('-') + 1,
				dateString.length);
		if (day.indexOf('0') == 0) {
			day = day.substr(1, day.length);
		}
		// 4,6,9,11月份日期不能超过30
		if ((month == 4 || month == 6 || month == 9 || month == 11)
				&& (day > 30)) {
			flag = false;
		}

		// 判断2月份
		if (month == 2) {
			if (hitooctrl.LeapYear(year)) {
				if (day > 29 || day < 1) {
					flag = false;
				}
			} else {
				if (day > 28 || day < 1) {
					flag = false;
				}
			}
		}
	}

	return flag;
}

/**
 * 判断字符串是否是日期时间
 */
hitooctrl.isDateTime = function(dateTimeString) {
	if (dateTimeString == null || "" == dateTimeString) {
		return false;
	}
	var flag = true;
	dateTimeString = dateTimeString.toString();
	dateTimeString = dateTimeString.replace(/\./, "-");
	dateTimeString = dateTimeString.replace(/\//g, "-");

	DATE_FORMAT = /^[0-9]{4}-(0[1-9]|[1-9]|1[0-2])-((0[1-9]|[1-9])|1[0-9]|2[0-9]|3[0-1]) ((0[0-9]|[1-9])|1[0-9]|2[0-4]):((0[0-9]|[1-9])|[1-5][0-9]):((0[0-9]|[1-9])|[1-5][0-9])$/;
	if (!DATE_FORMAT.test(dateTimeString)) {
		flag = false;
	} else {
		flag = true;
		// 获得年
		var year = dateTimeString.substr(0, dateTimeString.indexOf('-'));
		// 下面操作获得月份
		var transition_month = dateTimeString.substr(0, dateTimeString
				.lastIndexOf(' '));
		transition_month = dateTimeString.substr(0, dateTimeString
				.lastIndexOf('-'));
		var month = transition_month.substr(
				transition_month.lastIndexOf('-') + 1, transition_month.length);

		if (month.indexOf('0') == 0) {
			month = month.substr(1, month.length);
		}

		// 下面操作获得日期
		var transition_day = dateTimeString.substr(0, dateTimeString
				.lastIndexOf(' '));
		var day = transition_day.substr(transition_day.lastIndexOf('-') + 1,
				transition_day.length);

		if (day.indexOf('0') == 0) {
			day = day.substr(1, day.length);
		}
		// 4,6,9,11月份日期不能超过30
		if ((month == 4 || month == 6 || month == 9 || month == 11)
				&& (day > 30)) {
			flag = false;
		}
		// 判断2月份
		if (month == 2) {
			if (hitooctrl.LeapYear(year)) {
				if (day > 29 || day < 1) {
					flag = false;
				}
			} else {
				if (day > 28 || day < 1) {
					flag = false;
				}
			}
		}
	}

	return flag;
}

// 判断是否是闰年
hitooctrl.LeapYear = function(year) {
	var isYear = year;

	if ((isYear % 4 == 0) && (isYear % 100 != 0)) {
		return true;
	} else if (isYear % 400 == 0) {
		return true;
	}

	return false;
}

/**
 * 格式化日期字符串为指定格式
 * 
 * @returns yyyy-MM-dd hh:mm:ss
 */
hitooctrl.hitooDateStringFormat = function(dateString) {
	if (dateString == null || "" == dateString) {
		return "";
	}

	if (hitooctrl.isDate(dateString)) {
		// getDateStrByFormat作用是把yyyyMMdd格式的时间，转化为前提需要的yyyy-MM-dd，否则datebox赋值的时候会报错。
		return hitooctrl.getDateStrByFormat(dateString);
	} else if (hitooctrl.isDateTime(dateString)) {
		return hitooctrl.getDateStrByFormat(dateString.substr(0, dateString
				.lastIndexOf(' ')));
	} else {
		// 在ie中，new Date("20131212");这种写法是无效的，new date()的参数支持毫秒形式。
		var date = new Date(dateString);
		if (date != null) {
			return hitooctrl.hitooDateFormat(date);
		} else {
			return "";
		}
	}
}

/**
 * 格式化字符串日期时间为指定格式
 * 
 * @returns yyyy-MM-dd hh:mm:ss
 */
hitooctrl.hitooDateTimeStringFormat=function(dateString) {
	if (dateString == null || "" == dateString) {
		return "";
	}
	if (hitooctrl.isDateTime(dateString)) {
		return dateString;
	} else if (hitooctrl.isDate(dateString)) {
		return dateString + " 00:00:00";
	} else {
		var date = new Date(dateString);
		if (date != null) {
			return hitooctrl.hitooDateTimeFormat(date);
		} else {
			return "";
		}
	}
}

/**
 * 格式化日期为yyyy-MM-dd
 * 
 * @author xfh 2013-10-11
 * @param date
 */
hitooctrl.hitooDateFormat = function(date) {
	var formatNumber = function(n) {
		return n < 10 ? '0' + n : n;
	};
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + formatNumber(m) + '-' + formatNumber(d);
}

/**
 * 格式化日期为yyyy-MM-dd hh:mm:ss
 * 
 * @author xfh 2013-10-11
 * @param date
 */
hitooctrl.hitooDateTimeFormat = function(date) {
	var formatNumber = function(n) {
		return n < 10 ? '0' + n : n;
	};
	var h = date.getHours();
	var minutes = date.getMinutes();
	var s = date.getSeconds();

	return hitooctrl.hitooDateFormat(date) + ' ' + formatNumber(h) + ':'
			+ formatNumber(minutes) + ':' + formatNumber(s);
}

/*******************************************************************************
 * datagrid字典转换
 * 
 * @param code
 * @returns {Function}
 ******************************************************************************/
hitooctrl.dicValuesWithAll = function(cod, rootflag) {
	var dicList = window.top.hitoodictionary;
	if (typeof dicList == 'undefined' || typeof dicList[cod] == 'undefined') {
		return [];
	}
	var cloneArray = [];
	if ("root" == rootflag) {
		cloneArray.push({
			text : '全部',
			value : ''
		});
	} else if ("null" == rootflag) {
		cloneArray.push({
			text : '无',
			value : ''
		});
	}

	for ( var i = 0; i < dicList[cod].length; i++) {
		cloneArray.push(dicList[cod][i]);
	}
	return cloneArray;
}

hitooctrl.hitooDicConvert = function(code) {
	return function(value, rowData, rowIndex) {
		var text = value;
		var dicList = window.top.hitoodictionary;
		if (code in dicList) {
			var codeList = dicList[code];
			for ( var i = 0; i < codeList.length; i++) {
				if (value === codeList[i].value) {
					text = codeList[i].text;
					break;
				}
			}
		}else{
			hitooctrl.synchOperAjax(hitooctrl.webprojectname+"/sys/dic/getDicDtlNamConverter.do", {"dicCod":code,"dicDtlCod":value}, function(paraMap){
				if(paraMap){
					text = paraMap.dicDtlNam;
				}
			});
		}
		return text;
	};
}

hitooctrl.hitooDicConvert2 = function(value, rowData, rowIndex,code) {
	var text = value;
	var dicList = window.top.hitoodictionary;
	if (code in dicList) {
		var codeList = dicList[code];
		for ( var i = 0; i < codeList.length; i++) {
			if (value === codeList[i].value) {
				text = codeList[i].text;
				break;
			}
		}
	}else{
		hitooctrl.synchOperAjax(ctrl.webprojectname+"/sys/dic/getDicDtlNamConverter.do", {"dicCod":code,"dicDtlCod":value}, function(paraMap){
			if(paraMap){
				text = paraMap.dicDtlNam;
			}
		});
	}
	return text;
}

/**
 * ajax同步调用
 */
hitooctrl.synchOperAjax = function(url, params, afterOper){
	$.ajax({
		async : false,
		type : "post",
		url : url,
		data : params,
		success : function(data){
			if (data.errflag == "1") {
				$.messager.alert("错误", data.errtext, "error");
				return;
			}
			if (data.msgtext) {
				$.messager.alert("提示", data.msgtext, "info", function() {
					afterOper(data.paramMap);
				});
			} else {
				afterOper(data.paramMap);
			}
		}
	});
}

/*******************************************************************************
 * 前后台jquery Ajax请求调用
 ******************************************************************************/
hitooctrl.eamsOperPost = function(operUrl, params, afterOper, btn) {
	function mofifyBtnStatus(status) {
		if (btn != null) {
			switch (typeof btn) {
			case "string":
				if (btn != "") {
					if (btn.indexOf(",") > 0) {
						// 多个buttnon的情况
						var btnAry = btn.split(",");
						for ( var tmp = 0; tmp < btnAry.length; tmp++) {
							$('#' + btnAry[tmp]).linkbutton(status);
						}
					} else {
						$('#' + btn).linkbutton(status);
					}
				}
				break;
			case "object":
				$(btn).linkbutton(status);
				break;
			default:
				break;
			}
		}
	}

	mofifyBtnStatus('disable');
	$.post(operUrl, params, function(data) {
		if (data.errflag == "1") {
			$.messager.alert("错误", data.errtext, "error");
			mofifyBtnStatus('enable');
			return;
		}else if(data.errflag == "logonOverTimeError"){
			mofifyBtnStatus('enable');
			hitooctrl.openLoginWin();
			return;
		}
		
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.paramMap);
			});
		} else {
			afterOper(data.paramMap);
		}
	});
}

/**
 * 前后台jquery Ajax请求调用:不管后台是否抛出异常，总是会在最后执行回调函数
 */
hitooctrl.eamsOperPostAlwayExeCBP = function(operUrl, params, afterOper, btn) {
	function mofifyBtnStatus(status) {
		if (btn != null) {
			switch (typeof btn) {
			case "string":
				if (btn != "") {
					if (btn.indexOf(",") > 0) {
						// 多个buttnon的情况
						var btnAry = btn.split(",");
						for ( var tmp = 0; tmp < btnAry.length; tmp++) {
							$('#' + btnAry[tmp]).linkbutton(status);
						}
					} else {
						$('#' + btn).linkbutton(status);
					}
				}
				break;
			case "object":
				$(btn).linkbutton(status);
				break;
			default:
				break;
			}
		}
	}

	mofifyBtnStatus('disable');
	$.post(operUrl, params, function(data) {
		if (data.errflag == "1") {
			$.messager.alert("错误", data.errtext, "error", function() {
				mofifyBtnStatus('enable');
				afterOper(data.errflag);
			});
			return;
		}else if(data.errflag == "logonOverTimeError"){
			mofifyBtnStatus('enable');
			hitooctrl.openLoginWin();
			return;
		}
		
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.errflag, data.paramMap);
			});
		} else {
			afterOper(data.errflag, data.paramMap);
		}
	});
}

/**
 * 前后台jquery Ajax请求调用:不管后台是否抛出异常,后台抛出异常的回调和后台执行成功的回调不是一个函数
 */
hitooctrl.eamsOperPostExeFailOrExeSucc = function (operUrl, params, afterOper,afterFailOper) { 
	$.post(operUrl, params, function(data) {
		if (data.errflag == "1") {
			$.messager.alert("", data.errtext, "error",function() {
				afterFailOper(data.errflag);
			});
			return ;
		}else if(data.errflag == "logonOverTimeError"){
			hitooctrl.openLoginWin();
			return;
		}
		
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.errflag, data.paramMap);
			});
		} else {
			afterOper(data.errflag, data.paramMap);
		}
	});
}

/**
 * 前后台jquery Ajax请求调用
 */
hitooctrl.eamsOperPostWithWindowClose = function(windowName, operUrl, params,
		afterOper) {
	$.post(operUrl, params, function(data) {

		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		if (data.errflag == "1") {
			$.messager.alert("错误", data.errtext, "error");
			return;
		}else if(data.errflag == "logonOverTimeError"){
			mofifyBtnStatus('enable');
			hitooctrl.openLoginWin();
			return;
		}
		
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info", function() {
				afterOper(data.paramMap);
				$('#' + windowName).window('close');
			});
		} else {
			afterOper(data.paramMap);
			$('#' + windowName).window('close');
		}

	});
}

/**
 * 前后台jquery Ajax请求调用,返回结果集data.paramMap
 */
hitooctrl.eamsOperPostWithReturn = function(operUrl, params) {
	$.post(operUrl, params, function(data) {

		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
		if (data.errflag == "1") {
			$.messager.alert("错误", data.errtext, "error");
			return false;
		}else if(data.errflag == "logonOverTimeError"){
			mofifyBtnStatus('enable');
			hitooctrl.openLoginWin();
			return;
		}
		
		if (data.msgtext != null && data.msgtext != "") {
			$.messager.alert("提示", data.msgtext, "info");
		}
		if (data.paramMap == null) {
			return null;
		}

		return data.paramMap;
	});
}

/**
 * dataGrid通用查询请求 前后台jquery Ajax请求调用:传递的数据为json类型 add by qinchao
 */
hitooctrl.eamsOperPostWithJSON = function(operUrl, params, afterOper) {
	/**
	 * 直接用$.post()直接请求会有点小问题，尽管标识为json协议，
	 * 但实际上提交的ContentType还是application/x-www-form-urlencoded。
	 * 需要使用$.ajaxSetup()标示下ContentType。
	 * 
	 * ajaxSetup之后，本页面之后的所有传输都是json格式，所以需要设置回来 或者改为以下通过$.ajax的形式，这个代表只是本次请求
	 */
	// $.ajaxSetup({ contentType : 'application/json'});
	// $.ajaxSetup({ contentType : 'application/x-www-form-urlencoded'});
	$.ajax({
		type : "POST",
		url : operUrl,
		data : params,
		contentType : 'application/json',
		success : function(data) {
			if (data.errflag == "1") {
				$.messager.alert("错误", data.errtext, "error");
				return;
			}else if(data.errflag == "logonOverTimeError"){
				mofifyBtnStatus('enable');
				hitooctrl.openLoginWin();
				return;
			}
			
			if (data.msgtext != null && data.msgtext != "") {
				$.messager.alert("提示", data.msgtext, "info", function() {
					afterOper(data.paramMap);
				});
			} else {
				afterOper(data.paramMap);
			}

		}

	});
}

function dataFilter(data) {
    if (data.errflag) {
        $.messager.alert('错误', data.errtext, "error");
        return {
            total: 0,
            rows: []
        };
    } else {
        return data;
    }
}