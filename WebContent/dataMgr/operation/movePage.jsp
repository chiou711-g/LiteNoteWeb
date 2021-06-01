<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setCharacterEncoding("utf-8");%>
<% response.setContentType("text/html;charset=utf-8"); %>
<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean" scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService" scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<jsp:useBean id="pageBean" class="pageBean.PageBean" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="pageBean"/>
<jsp:useBean id="pageService" class="pageBean.PageService" scope="application" />
<jsp:setProperty property="*" name="pageService" />

<%@ page import="java.sql.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Swap page</title>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">	

<script type="text/javascript" src="../../jquery-3.3.1.js">
$(document).ready(function()
{

});
</script>
<script type="text/javascript" src="movePage.js"></script>
<script>
function showStatus(x,y)
{
	document.getElementById(x).innerHTML = y;
}
</script>

</head>

<body>
		<br>
		<!-- Back button -->
		&emsp;&emsp;
		<input type="button" name="back" value="返回" 
			style="background-color:#49743D;font-weight:bold;color:#ffffff;" 
			onclick="parent.postMessage('move page close', '*');history.back();">		
		<br><br>
		
		<fieldset>
		<legend>Move page</legend>

		<form id="move_page_form" name="movePageForm">
				
		<input type = "button" id="clean_data" value = "重新輸入"> 
		<ul>
 		<li>
			<b>From page position 2:</b>
			<input type = "text" id="move_page_pos2" name = "movePagePos2" size="10" style="background-color:Aquamarine;">
		</li> 
		<li>
			<b>To page position 1:</b>
	 		<input type = "text" id="move_page_pos1" name = "movePagePos1" size="10" style="background-color:Aquamarine;" >
		</li>
		</ul>
		&nbsp;&nbsp;&nbsp;&nbsp;
		
		<!-- Move button -->
		<input type = "button" value = "搬動page" id="varMovePage" name="varMovePage" 
			style="background-color:#490400;font-weight:bold;color:#ffffff;width: 6em;" >			
			
		</form>
		</fieldset>
		
		<br>
		
	    <div id="movePageInfo">
			<fieldset>
				<legend >Status</legend>
				<div id="ajaxMovePageInfo"></div>
			</fieldset>
		</div>			


		</body>
</html>