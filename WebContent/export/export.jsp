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

<title>Export</title>
<!-- <script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.js"></script> -->
<script	src="../jquery-3.3.1.js"></script>
<script src="export.js"></script>

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

<h1 align="center">Export JSON / HTML </h1>

<form id="export_links_form" name="exportLinksForm"> 
<!-- Categories -->
<div align="center" style="border: 1px solid white">
	<p><u><b>Categories:</b></u></p>
	<div class="nav" align="center">
	<table>
		<tr>
			<td><!-- categories: select all button -->
				<input id="check_all" type="button" value="全選" onclick="checkAll()" class="small_button" style="background-color:LightBlue;">
			</td>
			<td><!-- categories: select none button -->
				<input id="reset" type="reset" value="全不選" class="small_btn">				
			</td>
			<td><!-- categories: toggle all button -->
				<input id="toggle_all" type="button" value="Toggle All" onclick="toggleAll()" class="small_button">
			</td>
		</tr>
	</table>
	
	<!-- category table -->
	<p><table style="border:2px #FFD382 solid;color:Black; " cellpadding="5" border='0'>
		<!-- Category rows-->
		<c:forEach var="i" begin="1" end="${categoryService.categories_count}" step="1" varStatus="status">
	    <c:if test="${status.count %5 == 1}">
		<tr>
		</c:if>
			<td>
			<fieldset style="border:2px #FFD382 solid;color:Black;background-color:DarkGreen;width:160px;">
			<legend>
				<!-- category check box -->
 				<input type="checkbox" name="category_selection" id="check_box" value="${i}"  class="big_checkbox" onchange='categoryToggle(this,${i});'>
			</legend>
	 		<font size="3">
	 			<!-- category count -->
	 			<b style="color:#FFD382;">${status.count}.</b>
	 			<!-- category name -->
	 			<b id="category_name${i}" style="color:white;"></b>
	 		</font> 
			</fieldset>
			</td>	
		<c:if test="${status.count %5 == 0} ">
		</tr>
		</c:if>		
		</c:forEach>
	</table></p>
	</div>
</div><br>

<!-- Pages -->
<div align="center" style="border: 1px solid white">	
	<input type="hidden" id="categories_cnt" value="${categoryService.categories_count}">
	<input type="hidden" id="pages_cnt" name="pagesCnt" value="${pageService.pages_count}">
	
	<p><b><u>Pages:</u></b></p>
	<table style="border:1px Cornsilk solid;" cellpadding="10" border="0" width="100%">
	<c:forEach var="cate_row_id" begin="1" end="${categoryService.categories_count}" step="1" varStatus="table_status">
	<tr>
		<!-- count -->
		<td id="row_num${cate_row_id}"><b style="font-size:30px;color:Gold;">${table_status.count}.</b></td>
		<!-- category name and id  -->
		<td>
			<b id="row_num${cate_row_id}category_name" style="font-size:30px;color:Gold;"></b>
			<span id="row_num${cate_row_id}category_id" style="font-size:15px;color:Gold;"></span>
		</td>
		<!-- page rows -->
		<c:forEach var="i" begin="1" end="${pageService.pages_count}" step="1" varStatus="status">
		<!-- page check box -->
		<td  id="row_num${cate_row_id}cell${i}">
			<fieldset style="border:2px #a0a0a0 solid;color:Black;background-color:#142b2b;width:120px;">
			<legend>
				<input type="checkbox" id="row_num${cate_row_id}checkbox${i}" name="row_num${cate_row_id}page_selection" value="${i}"  class="big_checkbox">
			</legend>
	 			<span id="row_num${cate_row_id}id${i}" style="color:#ff7070; font-size:20px; width:30px;" ></span>
	 			<b id="row_num${cate_row_id}title${i}" style="color:#f0f0f0; font-size:20px;"></b>&emsp;
	 			<span id="row_num${cate_row_id}page_id${i}" style="color:#f0f0f0; font-size:15px;"></span>
			</fieldset>
		</td>
		
		</c:forEach>
	</tr>	
	</c:forEach>
	</table>
	<!-- total pages count -->
	<b>(總頁數 : <span id="total_pages">${pageService.pages_count})</span></b>
</div><br>

<!-- Select export file -->
<br><div align="center">
  <p><b><u>Select Export File:</u></b></p>	
  <input type="checkbox" id="sel_JSON" name="selJSON" value="JSON" onclick="selJson()">
  <label for="sel_JSON"> Export JSON</label>
  &emsp;&emsp;
  <input type="checkbox" id="sel_HTML" name="selHTML" value="HTML" onclick="selHtml()">
  <label for="sel_HTML"> Export HTML</label><br>
</div><br>
  
<!-- Export JSON -->
<div id="export_json" align="center" style="border: 1px solid white;background:#774444;">
	<p><b><u>Export JSON:</u></b></p>	

	<!-- file name input -->
	<b>Project selection:</b>
	<!-- project list view  -->
	<select id="project_selection" 
		onchange="getFileName();this.size=1; " 
		onfocus="this.selectedIndex=-1;this.size=0;" 
		style="height:2.0em;">
		<option id="project_id1" value="Default_YouLite">YouLite</option>
		<option id="project_id2" value="Default_TV-youtube">TV-youtube</option>
	</select>
	
	<!-- show file name -->	
	&emsp;JSON File name:
	<input type="text" id="json_file_name" name="fileName" value="" style="color:black;background-color:white;border:solid 0px;" class="small_button">
	
	&emsp;&emsp;
	<!-- Save JSON button -->
	<input type="button" value="Save GDrive Json" id="save_json_output_btn" style="background-color:LightBlue;" class="small_button">

	<!-- ready JSON files list -->
	<p>Ready JSON files: 
	<input type="button" value="View only" onclick="document.getElementById('file-check').click();" class="small_button"/>
	<input type="file" style="display:none;" id="file-check" name="file"/></p>
	
	<!-- JSON string output -->
	<fieldset id="json_output"  style="height:300px; width:98%;background-color:black;overflow:auto;font-size:15px;" >
		<legend>JSON Output</legend>
	</fieldset>

	<!-- Copy JSON button -->
	<input type="button" value="Copy output" onClick="CopyToClipboard('json_output')" style="background-color:LightBlue;" class="small_button">
	&emsp;&emsp;
	<!-- Clear JSON button -->
	<input type="button" value="Clear output" onClick="ClearJSON('json_output')" style="background-color:LightBlue;" class="small_button">
	<br><br>
</div>

<br>
<!-- Export HTML -->
<div id="export_html" align="center" style="border: 1px solid white;background:#447744;">
	<p><b><u>Export HTML:</u></b></p>	

	<!-- Table View file name input -->
	Table View file name:&emsp;
	<input type="text" id="table_view_file_name" name="tableViewFileName" value="EXPORT_TABLE_VIEW" style="background-color:White;" class="small_button">
	&emsp;&emsp;
	<!-- Export Table View button -->
	<input type="button" id="export_table_view_btn" value="Export Table View" style="background-color:LightBlue;" class="small_button">
	<br>

	<!-- List View file name input -->
	List View file name:&emsp;
	<input type="text" id="list_view_file_name" name="listViewFileName" value="EXPORT_LIST_VIEW" style="background-color:White;" class="small_button">
	&emsp;&emsp;
	<!-- Export List view button -->
	<input type="button" id="export_list_view_btn" value="Export List View" style="background-color:LightBlue;" class="small_button">

	<!-- ready HTML files list -->
	<p>Ready HTML files: 
	<input type="button" value="View only" onclick="document.getElementById('list_file_check').click();" class="small_button"/>
	<input type="file" style="display:none;" id="list_file_check" name="file"/></p>

	<!-- links list output -->
	<fieldset id="links_list_output"  style="height:300px; width:98%;background-color:black;overflow:auto;font-size:15px;" >
		<legend>Links list output</legend>
	</fieldset>

	<!-- Copy links list button -->
	<input type="button" value="Copy links list" onClick="CopyListToClipboard('links_list_output')" style="background-color:LightBlue;" class="small_button">
	&emsp;&emsp;
	<!-- Clear links list button -->
	<input type="button" value="Clear links list" onClick="ClearList('links_list_output')" style="background-color:LightBlue;" class="small_button">
	<br><br>
</div>



</form>

</body>
</html>
