<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean" scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService" scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<jsp:useBean id="pageService" class="pageBean.PageService" scope="application" />
<jsp:setProperty property="*" name="pageService" />


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="icon" href="../lite_note_web.png">
<link rel="stylesheet" type="text/css" href="../myStyle.css">

<title>Import JSON</title>
<!-- <script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script> -->
<script	src="../jquery-3.3.1.js"></script>
<script src="importJson.js"></script>

<style>
#progressBackground {
  width: 90%;
  background-color: #ddd;
}

#progressBar {
  width: 0%;
  height: 30px;
  background-color: #4CAF50;
}
</style>


</head>

<body>

<h1 align="center">Import JSON</h1>

<!-- Import JSON -->
<div align="center"  style="border: 1px solid white">
	<!-- JSON file list -->
	<p>file: <input type="button" value="Select JSON" onclick="document.getElementById('file-input').click();" class="small_button"/>
	<input type="file" style="display:none;" id="file-input" name="file"/>
	<!-- JSON file content -->
	<fieldset style="height:100px; width:98%;background-color:black;overflow:auto;font-size:15px;">
		<legend >JSON file 內容:</legend>
		<pre id="json-file-content"></pre>
		<!-- <span id="json-file-content"></span> -->
	</fieldset>
	<p><input type="button" id="import-json" onclick="importJsonContent(),showBar()" value="Run Import" class="small_button"/></p>
	
	<!-- progress -->
	<div  align="left" id="progressBackground">
	  <div id="progressBar"></div>
	</div>
	<br>
  	
</div><br>

</body>
</html>
