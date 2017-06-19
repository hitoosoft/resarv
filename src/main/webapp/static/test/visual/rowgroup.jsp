<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>排组</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/commons/head.jspf"%>
</head>
    <body>
       
        <!-- panel with buttons -->
        <div class="photos">
            <!-- 
			<div>
                <img src="images/pic1.jpg" />
                <div></div>
            </div>
            <div>
                <img src="images/pic2.jpg" />
                <div></div>
            </div>
            <div>
                <img src="images/pic3.jpg" />
                <div></div>
            </div>
		
            <div>
                <img src="images/pic4.jpg" />
                <div></div>
            </div>
            <div>
                <img src="images/pic5.jpg" />
                <div></div>
            </div>
            <div>
                <img src="images/pic6.jpg" />
                <div></div>
            </div>
				
            <div>
                <img src="images/pic7.jpg" />
                <div></div>
            </div>
			-->
            <div>
                <img src="images/pic8.jpg" onclick="rowclick('1')"/>
                <div></div>
            </div>
            <div class="pair">
                <img src="images/pic9.jpg"  onclick="rowclick('1')"/>
                <div></div>
                <div></div>
            </div>
        </div>
	 <script language="JavaScript">
	  function rowclick(rowid){
	       window.open('../3partandcell/singlerowdtl.html');
	  }
     </script>
    </body>

</html>