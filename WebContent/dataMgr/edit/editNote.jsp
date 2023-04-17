<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setCharacterEncoding("utf-8");%>
<% response.setContentType("text/html;charset=utf-8"); %>
<jsp:useBean id="noteBean" class="noteBean.NoteBean" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="noteBean" />

<%@ page import="java.sql.*"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="../../jquery-3.3.1.js"></script>
<script type="text/javascript" src="../apiAuth.js"></script>
<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<title>Edit note</title>

<script type="text/javascript" src="editNote.js"></script>

<script>
function showStatus(x,y)
{
	document.getElementById(x).innerHTML = y;
}
</script>
</head>
<body>
	<br> &emsp;
	<input type="button" name="back" value="Back"
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="history.back()">
	<br>
	<br>

	<fieldset>
		<legend>Edit link</legend>
		<!-- <form action="EditNote.do" method="post" > -->
		<form id="editNoteForm" name="editForm">
			<ul>
				<li><b>Page ID:</b> <input type="text" id="page_number"
					name="pageNumber" value="<%=request.getParameter("table_number")%>"
					size="10" readonly=readonly
					style="color: white; background-color: #142b2b; border: solid 0px;">
					<br></li>
				<li><b>Note ID:</b> <input type="text" id="note_id"
					name="note_id" value="<%=request.getParameter("note_id")%>"
					size="10" readonly=readonly
					style="color: white; background-color: #142b2b; border: solid 0px;">
					<br></li>
				<li><b>Link URI:</b> <input type="text" id="note_link_uri"
					name="note_link_uri"
					value="<%=request.getParameter("note_link_uri")%>" size="60">
					<input type="button" id="btnGetTitle" value="Get YouTube Title"
					class="button_size"
					style="background-color: #490400; font-weight: bold; color: #ffffff;" />
					<br></li>
				<li><b>Image URI:</b> <textarea id="note_image_uri"
						name="note_image_uri" rows="4" cols="60">
 		<%=request.getParameter("note_image_uri")%>
 		</textarea> <br></li>
				<li><b>TITLE:</b> <input type="text" id="note_title"
					name="note_title" value="<%=request.getParameter("note_title")%>"
					size="120"><br></li>
			</ul>

			<!-- Back button -->
			<input type="button" id="clean_data" value="Reset"
				style="background-color: #49743D; font-weight: bold; color: #ffffff;">
			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Update button -->
			<input type="button" value="Modify" id="varModifyOne" name="varModifyOne"
				style="background-color: #490400; font-weight: bold; color: #ffffff;">

			&nbsp;&nbsp;&nbsp;&nbsp;

			<!-- Delete button -->
			<input type="button" value="Delete" id="varDeleteOne" name="varDeleteOne"
				style="background-color: #990400; font-weight: bold; color: #ffffff;">

		</form>
	</fieldset>
	<br>

	<div id="editNoteInfo">
		<fieldset>
			<legend>Status</legend>
			<div id="ajaxEditNoteInfo"></div>
		</fieldset>
	</div>

</body>
</html>