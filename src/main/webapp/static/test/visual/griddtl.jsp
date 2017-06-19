<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>格内信息</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
    <script src="http://jq22.qiniudn.com/masonry-docs.min.js"></script>
    
     <style>
.container-fluid {
  padding: 20px;
  }
.box {
  margin-bottom: 20px;
  float: left;
  width: 35px;
  height:320px;
  text-align:center;
  background-image:url(${app}/static/images/dah/cemian_big.png);
  }
  
  
  #div-a{
text-align:center;
height:60px;
line-height:60px;
}

.box h4{
    width: 12px;
    font-size: 12px;
    word-wrap: break-word;
	margin-top:60px;
	margin-left:8px;
}

	 
	 </style>
</head>
<body>

 
<div id="masonry" class="container-fluid">
  <div class="box">
        <h4> NETTUTS </h4>
  </div>
  <div class="box">
       <h4>工伤认定受理2015年底063卷</h4>
  </div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
   <div class="box"></div>
  <div class="box"></div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
   <div class="box"></div>
  <div class="box"></div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
   <div class="box"></div>
  <div class="box"></div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
   <div class="box"></div>
  <div class="box"></div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
   <div class="box"></div>
  <div class="box"></div>
  <div class="box"></div>
  <div class="box"> </div>
  <div class="box"> </div>
  </div>
	
	<script>
$(function() {
    var $container = $('#masonry');
    $container.imagesLoaded(function() {
        $container.masonry({
                itemSelector: '.box',
                gutter: 20,
                isAnimated: true,
            });
     });
});
	</script>
</body>
</html>