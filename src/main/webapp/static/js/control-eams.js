/*******************************************************************************
 * 加载档案动态选择框
 * 
 * @author qinchao 20160411
 ******************************************************************************/
// 档案档案分类页面
hitooctrl.eamsArvTypeSearcher = function(value, name, searcherparam) {
	if (value == null) {
		value = "";
	}
	var url = hitooctrl.webprojectname
			+ "/bas/arvtype/forwardArvTypeSearcher.do";
	var width = 700;
	var height = 400;
	var title = "选择档案分类";
	hitooctrl.openWin(url, searcherparam, width, height, title, 100);
}

// 档案档案分类页面--single单选
hitooctrl.eamsArvTypeSearcherSingle = function(value, name, searcherparam) {
	if (value == null) {
		value = "";
	}
	var url = hitooctrl.webprojectname
			+ "/bas/arvtype/forwardArvTypeSearcherSingle.do";
	var width = 700;
	var height = 400;
	var title = "选择档案分类";
	hitooctrl.openWin(url, searcherparam, width, height, title, 100);
}

//档案档案分类页面--single单选，并且获取档案分类的id时，不获取复合分类
hitooctrl.eamsArvTypeSearcherSingleAndNotComplex = function (value,name,searcherparam){
if (value == null ){
	    value = "";
}
var url = hitooctrl.webprojectname +"/bas/arvtype/forwardArvTypeSearcherSingleAndNotComplex.do";
var width = 650;
var height = 300;
var title = "选择档案分类";
hitooctrl.openWin(url, searcherparam, width, height, title,100); 
}