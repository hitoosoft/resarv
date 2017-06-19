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
<style>
body{width:100%;height:100%}
</style>
</head>
<body>
	<div id="container" style=" height: 400px;margin: 0 auto"></div>
</body>


	<script type="text/javascript">
		var chart;
		$(document).ready(function(){
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					defaultSeriesType: 'line',
					marginRight: 130,
					marginBottom: 25
			},
			title: {
				text: 'Monthly Average Temperature',
				x: -20    //center
			},
			subtitle: {
				text: '测试中文乱码',
				x: -20
			},
			xAxis: {
				categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
								'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
			},
			yAxis: {
				title: {
					text: 'Temperature (Â°C)'
				},
				plotLines: [{
					value: 0,
					width: 1, 
					color: '#808080'
				}]
			},
			exporting:{
			    filename:'class-booking-chart',
			    url:'http://localhost:8080/eams/highcharts/export'
			},
			tooltip: {
				formatter: function(){
					return '<b>' + this.series.name + '</b><br/>' + 
					this.x + ': ' + this.y + 'Â°C';
				}
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle',
				x: 10,
				y: 100,
				borderWidth: 0
			},
			series: [{
				name: 'Tokyo',
				data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
			},{
				name: 'New York',
				data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
			},{
				name: 'Berlin',
				data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
			},{
				name: 'London',
				data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
			}]
			});
		});
	</script>
	
</html>