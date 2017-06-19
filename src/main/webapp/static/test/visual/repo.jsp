<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>库房</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>

<script src="${app}/static/highcharts/highcharts.js"></script>
<script src="${app}/static/highcharts/highcharts-3d.js"></script>
</head>
<body>


<div id="container" style="width: 1200px; height: 400px; margin: 0 auto"></div>

<div id="sliders">
    <table>
        <tr>
            <td>Alpha Angle</td>
            <td><input id="alpha" type="range" min="0" max="45" value="15"/> <span id="alpha-value" class="value"></span></td>
        </tr>
        <tr>
            <td>Beta Angle</td>
            <td><input id="beta" type="range" min="-45" max="45" value="15"/> <span id="beta-value" class="value"></span></td>
        </tr>
        <tr>
            <td>Depth</td>
            <td><input id="depth" type="range" min="20" max="100" value="50"/> <span id="depth-value" class="value"></span></td>
        </tr>
    </table>
</div>

<div id="choosesinglerowwin" class="easyui-window" title="请先选择一个观察视图" style="width:280px;height:150px;display:none;"
  		data-options="iconCls:'icon-key',collapsible:false,minimizable:false,maximizable:false,closed:true,modal:true,resizable:false">
	  	<form id="rowsForm" columns='1' class="easyui-form">
			<input title="第1排" type="radio" name="rowid" value="1"  checked>
			<input title="第2排" type="radio" name="rowid" value="2">
		</form>
	 	<div style="width:100%;height:30px;position:absolute;bottom:10px;text-align:center;margin:0 auto;">
	   		<a class="easyui-linkbutton" iconCls="icon-save" onclick="singlerowOnClick();">确定</a>
	   		<a class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#choosesinglerowwin').window('close');">取消</a>
	 	</div>
	</div>



<script language="JavaScript">
$(document).ready(function() {  
   var chart = {
      renderTo: 'container',
      type: 'column',
      margin: 75,
      options3d: {
         enabled: true,
         alpha: 15,
         beta: 0,
         depth: 50,
         viewDistance: 50,
		  frame:{
		     
            bottom: {
                //size: 100,
               // color: 'red'
            }
		  }
      }
   };
   var title = {
      text: '327repo'   
   };
   var subtitle = {
      text: ''  
   };
   
   var plotOptions = {
      column: {
         depth: 500,
		 groupZPadding: 0,
		  cursor: 'pointer',
        events: {
            click: function(e) {
				//alert(e.point.category);
		//location.href = e.point.url;
               // window.open('../2rowgroupandrow/rowgroup.html');
               
               //
               if(e.point.category=='11'){
            	   window.open('./singlerowdtl.jsp');
               }else{
            	   $('#choosesinglerowwin').window('open');
    		       $("#choosesinglerowwin").css("display", "block");
               }
              
		
              /*   
                var url = "${app}/static/test/visual/rowgroup.jsp";
        	 	var paraMap = {};
        	 	var width = 1000;
           		var height = 600;
           		var title = "排组信息";
           		hitooctrl.openWin(url,paraMap,width,height,title); */
           		
           		
	    }
	},
      }
   };
   var series= [{
      name:'327repo',
      data: [100,100,100,100,100,100]
   }];     
   
   var legend = {                                                                    
            enabled: false                                                           
        } ;

	var yAxis={title:{text:''},labels: {
                enabled: false
            } ,gridLineWidth:0 };
	
	var xAxis= {title:{text:''},categories:  ['1-2','3-4','5-6','7-8','9-10','11'] ,gridLineWidth:0};
	
	var tooltip={
    enabled: false,//提示
	};
      
   var json = {};   
   json.chart = chart; 
   json.title = title;   
   json.subtitle = subtitle;    
   json.series = series;
   json.plotOptions = plotOptions;
  json.yAxis=yAxis;
	json.xAxis=xAxis;
	json.tooltip=tooltip;
	json.legend=legend;
   var highchart = new Highcharts.Chart(json);
   
   
  
  
   function showValues() {
      $('#alpha-value').html(highchart.options.chart.options3d.alpha);
      $('#beta-value').html(highchart.options.chart.options3d.beta);
   }

   // Activate the sliders
   $('#alpha').on('change', function () {
      highchart.options.chart.options3d.alpha = this.value;
      showValues();
      highchart.redraw(false);
   });
   $('#beta').on('change', function () {
      highchart.options.chart.options3d.beta = this.value;
      showValues();
      highchart.redraw(false);
   });

   showValues();
});

function singlerowOnClick(){
	alert($('#rowsForm input:radio[name="rowid"]:checked').val());
	$('#choosesinglerowwin').window('close');
	 window.open('./singlerowdtl.jsp');
}
</script>
</body>
</html>
