<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>业务监控</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>

<script src="${app}/static/highcharts/5.0.9/highcharts.js"></script>
<script src="${app}/static/highcharts/5.0.9/highcharts-3d.js"></script>
<script src="${app}/static/highcharts/5.0.9/modules/exporting.js"></script>
<script src="${app}/static/highcharts/5.0.9/highcharts-zh_CN.js"></script>
<script src="${app}/static/highcharts/5.0.9/theme/dark-unica.js"></script>
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
