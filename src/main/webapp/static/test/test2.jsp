<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>测试</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
<style type="text/css">
    *{margin:0;padding: 0;}
    .hovertree{
        width: 120px;
        height: 180px;
        margin: 150px auto 0;
        position: relative;
        /*transform 旋转元素*/
        transform-style:preserve-3d;
        transform:perspective(800px) rotateX(-10deg) rotateY(0deg);
    }
    body{background-color: #66677c;}

    .hovertree p{
        position: absolute;
        width: 120px;
        height: 180px;
        border-radius: 5px;
        box-shadow: 0px 0px 10px #fff;
        /*倒影的设置*/
        -webkit-box-reflect:below 10px -webkit-linear-gradient(top,rgba(0,0,0,0) 50%,rgba(0,0,0,.5) 100%);
    }

</style>

<body>
<h4>327库房示意图</h4>
<div class="hovertree" >
<p style="width:120px;height:180px;background:url(../images/testimg/01.jpg)"  onclick="openrowdtl(1);">
第1排
 </p>
 <p style="width:120px;height:180px;background:url(../images/testimg/02.jpg)"  onclick="openrowdtl(1);">
 第2排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/03.jpg)"  onclick="openrowdtl(1);">
 第3排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/04.jpg)"  onclick="openrowdtl(1);">
 第4排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/05.jpg)"  onclick="openrowdtl(1);">
 第5排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/06.jpg)"  onclick="openrowdtl(1);">
 第6排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/07.jpg)"  onclick="openrowdtl(1);">
 第7排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/08.jpg)"  onclick="openrowdtl(1);">
 第8排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/09.jpg)"  onclick="openrowdtl(1);">
 第9排
 </p>
  <p style="width:120px;height:180px;background:url(../images/testimg/10.jpg)"  onclick="openrowdtl(1);">
 第10排
 </p>
   <p style="width:120px;height:180px;background:url(../images/testimg/11.jpg)"  onclick="openrowdtl(1);">
 第11排
 </p>
   <p style="width:120px;height:180px;background:url(../images/testimg/12.jpg)"  onclick="openrowdtl(1);">
 第12排
 </p>
<!-- <p style="width:120px;height:180px;">    
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/02.jpg"  onclick="alert(2);"/>44</p> -->
    
    <!-- <img src="http://cms.hovertree.com/hvtimg/201511/9rour12a.jpg"   onclick="alert(3);"/>
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/04.jpg"   onclick="alert(4);"/>
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/05.jpg"   onclick="alert(5);"/>
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/06.jpg"   onclick="alert(6);"/>
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/07.jpg"   onclick="alert(7);"/>
    <img src="http://hovertree.com/texiao/css/14/hovertreepic/08.jpg"   onclick="alert(8);"/>
    <img src="http://hovertree.com/hvtimg/201511/6j9j6tk5.png"   onclick="alert(9);"/>
    <img src="http://cms.hovertree.com/hvtimg/201512/wfevf6yh.jpg"   onclick="alert(10);"/>
	<img src="https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=369315132,4087115857&fm=116&gp=0.jpg"   onclick="alert(11);"/>
    <img src="https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2830623800,3287877147&fm=116&gp=0.jpg"   onclick="alert(12);"/> -->

</div>

<script type="text/javascript">
    $(function(){

var imgL=$(".hovertree p").size();
var deg=360/imgL;
var roY=0,roX=-10;
var xN=0,yN=0;
var play=null;

 $(".hovertree p").each(function (i) {
    $(this).css({
        //<!--translateZ 定义2d旋转沿着z轴-->
        "transform":"rotateY("+i*deg+"deg) translateZ(400px)"    });
        //<!--防止图片被拖拽-->
        $(this).attr('ondragstart','return false');
    });

    $(document).mousedown(function(ev){
        var x_=ev.clientX;
        var y_=ev.clientY;
        clearInterval(play);
        console.log('我按下了');
        $(this).bind('mousemove',function(ev){
            /*获取当前鼠标的坐标*/
            var x=ev.clientX;
            var y=ev.clientY;
            /*两次坐标之间的距离*/
              xN=x-x_;
              yN=y-y_;

             roY+=xN*0.2;
            roX-=yN*0.1;
            console.log('移动');
            //$('body').append('<div style="width:5px;height:5px;position:absolute;top:'+y+'px;left:'+x+'px;background-color:red"></div>');

            $('.hovertree').css({
                 transform:'perspective(800px) rotateX('+roX+'deg) rotateY('+roY+'deg)'
            });
            /*之前的鼠标坐标*/
         x_=ev.clientX;
         y_=ev.clientY;

        });
    }).mouseup(function(){
          $(this).unbind('mousemove');
          var play=setInterval(function(){
           
           xN*=0.95;
           yN*=0.95
           if(Math.abs(xN)<1 && Math.abs(yN)<1){
              clearInterval(play);
           }
            roY+=xN*0.2;
            roX-=yN*0.1;
            $('.hovertree').css({
                 transform:'perspective(800px) rotateX('+roX+'deg) rotateY('+roY+'deg)'
            });
          },30);
    });
});
</script>

</body>
</html>