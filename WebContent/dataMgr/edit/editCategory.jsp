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
<title>Edit category</title>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<script type="text/javascript" src="../../jquery-3.3.1.js">
$(document).ready(function()
{

});
</script>
<script type="text/javascript" src="editCategory.js"></script>
<script>
function showStatus(x,y)
{
	document.getElementById(x).innerHTML = y;
}
</script>

</head>

<body>

	<%  int category_pos = categoryService.getCurrent_category_pos();
			categoryBean = categoryService.selectCategory(category_pos);
			String category_name = categoryBean.getCategory_name();
		%>
	<br>

	<!-- Back button -->
	&emsp;&emsp;
	<input type="button" name="back" value="返回"
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="parent.postMessage('add new close', '*');history.back();">
	<br>
	<br>

	<fieldset>
		<legend>Edit category</legend>

		<form id="edit_category_form" name="editCategoryForm">

			<input type="button" id="clean_data" value="重新輸入"> <br>
			<br> &emsp; <b>Category Position:</b> <input type="text"
				id="edit_category_pos" name="editCategoryPos"
				value="<%=category_pos%>" size="10" readonly
				style="color: white; background-color: #142b2b; border: solid 0px;">
			<ol>
				<li><b>Category Name:</b> <input type="text"
					id="edit_category_name" name="editCategoryName"
					value="<%=category_name%>" size="10"
					style="background-color: Aquamarine;"></li>
			</ol>
			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Update button -->
			<input type="button" value="修改" id="varModifyCategory"
				name="varModifyCategory"
				style="background-color: #490400; font-weight: bold; color: #ffffff; width: 5em;">

			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Delete button -->
			<input type="button" value="刪除" id="varDeleteCategory"
				name="varDeleteCategory"
				style="background-color: #990400; font-weight: bold; color: #ffffff; width: 5em;">

			&nbsp;&nbsp;&nbsp;&nbsp;
			<!-- Delete all button -->
			<input type="button" value="刪除ALL" id="varDeleteAllCategory"
				name="varDeleteAllCategory"
				style="background-color: #999900; font-weight: bold; color: #ffffff; width: 5em;">

		</form>
	</fieldset>

	<br>

	<div id="editCategoryInfo">
		<fieldset>
			<legend>Status</legend>
			<div id="ajaxEditCategoryInfo"></div>
		</fieldset>
	</div>


</body>
</html>