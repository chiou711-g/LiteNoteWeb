<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setCharacterEncoding("utf-8");%>
<% response.setContentType("text/html;charset=utf-8"); %>
<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean"
	scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService"
	scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<jsp:useBean id="pageBean" class="pageBean.PageBean" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="pageBean" />

<%@ page import="java.sql.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Swap category</title>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<script type="text/javascript" src="../../jquery-3.3.1.js">
$(document).ready(function()
{

});
</script>
<script type="text/javascript" src="swapCategory.js"></script>
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
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="parent.postMessage('swap category close', '*');history.back();">
	<br>
	<br>

	<fieldset>
		<legend>Swap category</legend>

		<form id="swap_category_form" name="swapCategoryForm">

			<input type="button" id="clean_data" value="重新輸入">
			<ol>
				<li><b>Category Position 1:</b> <input type="text"
					id="swap_category_pos1" name="swapCategoryPos1" size="10"
					style="background-color: Aquamarine;"></li>
				<li><b>Category Position 2:</b> <input type="text"
					id="swap_category_pos2" name="swapCategoryPos2" size="10"
					style="background-color: Aquamarine;"></li>
			</ol>
			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Swap button -->
			<input type="button" value="互換" id="varSwapCategory"
				name="varSwapCategory"
				style="background-color: #490400; font-weight: bold; color: #ffffff; width: 5em;">

		</form>
	</fieldset>

	<br>

	<div id="swapCategoryInfo">
		<fieldset>
			<legend>Status</legend>
			<div id="ajaxSwapCategoryInfo"></div>
		</fieldset>
	</div>


</body>
</html>