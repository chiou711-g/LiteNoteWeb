<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.sql.*"%>

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
<jsp:useBean id="pageService" class="pageBean.PageService"
	scope="application" />
<jsp:setProperty property="*" name="pageService" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Edit page</title>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<script type="text/javascript" src="../../jquery-3.3.1.js"></script>
<script type="text/javascript" src="editPage.js"></script>
<script>
$(document).ready(function()
{
});
</script>

<script>
function showStatus(x,y)
{
	document.getElementById(x).innerHTML = y;
}
</script>

</head>

<body>
	<!-- Back button -->
	<br> &emsp;
	<input type="button" name="back" value="Back"
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="parent.postMessage('add new close', '*');history.back();">
	<br>
	<br>

	<fieldset>
		<legend>Edit page</legend>
		<%
			String pageId = pageService.getPageTableId();
			String title = null;
			int category_id = 0;
			if(pageId != null)
			{
				pageBean = pageService.selectPage(pageId);
				title = pageBean.getPage_title();
				category_id = pageBean.getPage_category_id();
			}
		%>
		<form id="edit_page_form" name="editPageForm">
			<input type="button" id="clean_data" value="Reset"> <br>
			<br> &emsp;<b>Page Id:</b> <input type="text" id="edit_page_id"
				name="editPageId" value="<%=pageId%>" size="10" readonly
				style="color: white; background-color: #142b2b; border: solid 0px;">
			<ol>
				<li><b>Page Title:</b> <input type="text" id="edit_page_title"
					name="editPageTitle" value="<%=title%>" size="10"></li>
				<li><b>Category selection:</b> <br> <!-- show category info -->
					使用 category Id: <input type="text" id="edit_page_category_id"
					readonly name="editPageCategoryId" value="<%=category_id%>"
					size="10"
					style="color: white; background-color: #142b2b; border: solid 0px;">
					<br> <!-- category list view  --> 重選: &nbsp; <select
					id="category_selection" onchange="getCategoryInfo()"
					onfocus="this.selectedIndex=-1"
					onchange="getCategoryInfo();this.size=1; "
					onfocus="this.selectedIndex=-1;this.size=0;" style="height: 2.0em;">
						<c:forEach var="i" begin="1"
							end="${categoryService.categories_count}" step="1"
							varStatus="status">
							<option id="category_description${i}"></option>
						</c:forEach>
				</select> <!-- hidden for keeping category id --> <c:forEach var="i"
						begin="1" end="${categoryService.categories_count}" step="1"
						varStatus="status">
						<input type="hidden" id="category_id${i}">
					</c:forEach></li>

			</ol>
			<br> &nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Update button -->
			<input type="button" value="Modify" id="varModifyPage"
				name="varModifyPage"
				style="background-color: #490400; font-weight: bold; color: #ffffff;">

			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Delete button -->
			<input type="button" value="Delete" id="varDeletePage"
				name="varDeletePage"
				style="background-color: #990400; font-weight: bold; color: #ffffff;">

		</form>
	</fieldset>
	<br>

	<div id="editPageInfo">
		<fieldset>
			<legend>Status</legend>
			<div id="ajaxEditPageInfo"></div>
		</fieldset>
	</div>

</body>
</html>