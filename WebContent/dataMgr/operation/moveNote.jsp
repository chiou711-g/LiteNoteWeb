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
<jsp:useBean id="pageService" class="pageBean.PageService"
	scope="application" />
<jsp:setProperty property="*" name="pageService" />

<jsp:useBean id="noteBean" class="noteBean.NoteBean" scope="application" />
<jsp:setProperty property="*" name="noteBean" />
<jsp:useBean id="noteService" class="noteBean.NoteService"
	scope="application" />
<jsp:setProperty property="*" name="noteService" />

<%@ page import="java.sql.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Swap link</title>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<script type="text/javascript" src="../../jquery-3.3.1.js">
$(document).ready(function()
{

});
</script>
<script type="text/javascript" src="moveNote.js"></script>
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
	<input type="button" name="back" value="Back"
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="parent.postMessage('move link close', '*');history.back();">
	<br>
	<br>

	<fieldset>
		<legend>Move link</legend>

		<%
			String pageId = pageService.getPageTableId();
			String title = null;
			if(pageId != null) {
				pageBean = pageService.selectPage(pageId);
				title = pageBean.getPage_title();
			}
		%>

		<form id="move_note_form" name="moveNoteForm">

			<input type="button" id="clean_data" value="Reset"> <br>
			<br> <b>Page ID:</b> <input type="text" id="page_number"
				name="pageNumber" value="<%=pageId%>" size="10" readonly=readonly
				style="color: white; background-color: #142b2b; border: solid 0px;">
			<br>
			<br> <b>Page Title:</b> <input type="text" id="page_title"
				name="pageTitle" value="<%=title%>" size="10" readonly=readonly
				style="color: white; background-color: #142b2b; border: solid 0px;">
			<br>
			<br>

			<ul>
				<li><b>From link Position 2:</b> <input type="text"
					id="move_note_pos2" name="moveNotePos2" size="10"
					style="background-color: Aquamarine;"></li>
				<li><b>To link Position 1:</b> <input type="text"
					id="move_note_pos1" name="moveNotePos1" size="10"
					style="background-color: Aquamarine;"></li>
			</ul>
			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Move button -->
			<input type="button" value="搬動Link" id="varMoveNote"
				name="varMoveNote"
				style="background-color: #490400; font-weight: bold; color: #ffffff; width: 5em;">

		</form>
	</fieldset>

	<br>

	<div id="moveNoteInfo">
		<fieldset>
			<legend>Status</legend>
			<div id="ajaxMoveNoteInfo"></div>
		</fieldset>
	</div>


</body>
</html>