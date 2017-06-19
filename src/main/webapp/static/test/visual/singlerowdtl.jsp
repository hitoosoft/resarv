<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>排</title>
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
  width: 194px;
  border:1px solid #333;
  }
  .box div {
  width: 192px;
  height:125px;
  border:1px solid #ccc;
   
}

  .box .footer{
background:#ddd; 
  height:20px;
  text-align:center;
}
	 
	 </style>
</head>
<body>
	<div id="masonry" class="container-fluid">
  <div class="box">
    <div style="" onclick="opengrid('111')">
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	</div>
    <div style="" onclick="opengrid('112')">
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	<img src="${app}/static/test/imgdah/cemian.png"/>
	</div>
    <div style=""></div>
	<div style=""></div>
	<div class="footer">1part</div>
  </div>
  <div class="box">
    <div style=""></div>
    <div style=""></div>
    <div style=""></div>
	<div style=""></div>
	<div class="footer">2part</div>
  </div>
  <div class="box">
      <div style=""></div>
    <div style=""></div>
	  <div style=""></div>
    <div style=""></div>
	<div class="footer">3part</div>
  </div>
 
   <div class="box">
      <div style=""></div>
    <div style=""></div>
	  <div style=""></div>
    <div style=""></div>
		<div class="footer">4part</div>
  </div>
  
    <div class="box">
      <div style=""></div>
    <div style=""></div>
	  <div style=""></div>
    <div style=""></div>
		<div class="footer">5part</div>
  </div>
  
    <div class="box">
      <div style=""></div>
    <div style=""></div>
	  <div style=""></div>
    <div style=""></div>
	<div class="footer">6part</div>
  </div>
</div>
	<!--
	<label class="label-deg">
	  最大旋转角度
	  <input type="number" class="deg" value="10" />
	</label>
	<label class="label-text-hover">
	  鼠标滑过时文本移动速度 (px per second)
	  <input type="number" class="text-px" value="100" />
	</label>
-->
	
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

function opengrid(gridid){
window.open('./griddtl.jsp');
}


/**
$(function() {
    var $objbox = $("#masonry");
    var gutter = 25;
    var centerFunc, $top0;
    $objbox.imagesLoaded(function() {
        $objbox.masonry({
            itemSelector: "#masonry > .box",
            gutter: gutter,
            isAnimated: true
        });
        centerFunc = function() {
            $top0 = $objbox.children("[style*='top: 0']");
            $objbox.css("left", ($objbox.width() - ($top0.width() * $top0.length + gutter * ($top0.length - 1))) / 2).parent().css("overflow", "hidden");
        };
        centerFunc();
    });
    var tur = true;
    $(window).resize(function() {
        if (tur) {
            setTimeout(function() {
                tur = true;
                centerFunc();
            },
            1000);
            tur = false;
        }
    });
});
*/
	</script>
</body>
</html>
