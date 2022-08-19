<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="pageBean" class="pageBean.PageBean" scope="application" />
<jsp:setProperty property="*" name="pageBean" />
<jsp:useBean id="pageService" class="pageBean.PageService"
	scope="application" />
<jsp:setProperty property="*" name="pageService" />

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Add Link</title>

<!-- 
<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
<script	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js" type="text/javascript"></script>
<script	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js" type="text/javascript"></script>
-->
<script type="text/javascript" src="../../jquery-3.3.1.js"></script>
<script type="text/javascript" src="../apiAuth.js"></script>
<link rel="stylesheet" type="text/css" href="../../myStyle.css">

<style>
.my_text {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 40px;
	font-weight: bold;
}

#outer {
	width: 100%;
	text-align: center;
}

#content {
	display: inline-block;
}

input.button_size {
	width: 10em;
	height: 2em;
}
</style>

</head>

<body id="htmlbody">

	<!-- Add Note start -->
	<br> &emsp;
	<input type="button" name="back" value="返回"
		style="background-color: #49743D; font-weight: bold; color: #ffffff;"
		onclick="parent.postMessage('add new close', '*');history.back();">
	<br>
	<br>
	<!-- <hr size=2 color=blue width=100%> -->

	<fieldset>
		<legend>Add link</legend>
		<form id="addNoteForm" name="addNoteForm">
			<input type="reset" value="重新輸入">
			<script type="text/javascript" src="addNote.js"></script>
			<ol>
				<li>
					<ul>
						<li><b>Link URI:</b> <input type="text" id="note_link_uri"
							placeholder="網址" name="noteLinkUri" size="48" value=""></li>
						<li><input type="button" id="btnGetTitle"
							value="導入 YouTube Title"
							style="background-color: #490400; font-weight: bold; color: #ffffff;" />
						</li>
					</ul>
				</li>

				<li><b>Title:</b> <input type="text" id="note_title"
					name="noteTitle" size="48" value=""></li>

				<li><b>Page selection:</b> <!-- page list view  --> <select
					id="page_selection" onchange="getPageInfo();this.size=1; "
					onfocus="this.selectedIndex=-1;this.size=0;" style="height: 2.0em;">
						<c:forEach var="i" begin="1" end="${pageService.pages_count}"
							step="1" varStatus="status">
							<option id="page_description${i}"></option>
						</c:forEach>
				</select> <!-- hidden for keeping page id --> <c:forEach var="i" begin="1"
						end="${pageService.pages_count}" step="1" varStatus="status">
						<input type="hidden" id="page_id${i}">
					</c:forEach> <!-- show page info --> &emsp;&nbsp; Page ID: <input type="text"
					id="page_id" name="pageId" readonly size="25" value=""
					style="color: white; background-color: #142b2b; border: solid 0px; height: 2.0em;">
					<input type="hidden" id="add_page_id" name="addPageId"></li>

				<li><b>指定 Image URI:</b> <textarea id="note_image_uri"
						name="noteImageUri" rows="4" cols="60">
			</textarea></li>

			</ol>
			&emsp;<input id="addNoteBtn" type="button" value="新增Link"
				style="background-color: #497440; font-weight: bold; color: #ffffff;">
		</form>
	</fieldset>

	<div id="outer">
		<div class="my_text" id="content"></div>
	</div>

	<div id="addNoteInfo">
		<fieldset>
			<legend>新加入的Link</legend>
			<div id="ajaxAddNoteInfo"></div>
		</fieldset>
	</div>


	<script type="text/javascript">
		var varApiKey = apiAuth.keyStr;
		$(document).ready(function () {
			$("#btnGetTitle").click(
		    	function () {
		          	var url =  document.getElementById("note_link_uri").value;
		          	var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
		          	var match = url.match(regExp);
		          	if (match && match[2].length == 11) {
		          		getVids (match[2]);
	        	}
	         }
	     );
	});

	function getVids(vid){
	  $.get(
	    "https://www.googleapis.com/youtube/v3/videos",{
	    	part: 'snippet,contentDetails', 
	    	id: vid, 
	    	key: varApiKey },
	      function(data){
	    	//alert(data.items[0].snippet.title)
	    	//alert(data.items[0].contentDetails.duration)
	      	ChangeTitle(data.items[0].snippet.title);
	      }
	  )
	}

	function ChangeTitle(titleText) {
		   document.getElementById("note_title").value = titleText;
	}
</script>
	<!-- Add Note end -->

</body>
</html>