<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean" scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService" scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8;">
<title>Add Page</title>

<script type="text/javascript" src="../../jquery-3.3.1.js"></script>	
<script type="text/javascript" src="addPage.js"></script>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">		

<style>
    .my_text {
        font-family:    Arial, Helvetica, sans-serif;
        font-size:      40px;
        font-weight:    bold;
    }
       
    #outer {
 		width: 100%;
		text-align: center;
	}
	 
	#content {
		display: inline-block;
	}	
	 
	input.button_size {
		width: 6em;  height: 2.5em;
	}	 

</style>



    
</head>

<body id="htmlbody">

	<!-- Add Page start -->
	<br>
	&emsp;
	<input type="button" name="back" value="返回" 
		style="background-color:#49743D;font-weight:bold;color:#ffffff;" 
		onclick="parent.postMessage('add new close', '*');history.back();">
	<br><br>
			
	<fieldset>
	<legend >Add page</legend>
	<form id="addPageForm" name="addPageForm">
	<input type = "reset" value = "重新輸入" >
	<ol >
		<li><b>Page Title:</b>
			<input type="text" id="add_page_title" name="add_page_title" size="30" value="" style="background-color:#D6D6D6;">
		</li>
		
		<li><b>Category selection:</b>
		<!-- category list view  -->
		<select id="category_selection" 
			onchange="getCategoryInfo();this.size=1; " 
			onfocus="this.selectedIndex=-1;this.size=0;" 
			style="height:2.0em;">
			<c:forEach var="i" begin="1" end="${categoryService.categories_count}" step="1" varStatus="status">
				<option id="category_description${i}"></option>
			</c:forEach>			
		</select>
		
		<!-- hidden for keeping category id -->
		<c:forEach var="i" begin="1" end="${categoryService.categories_count}" step="1" varStatus="status">
			<input type="hidden" id="category_id${i}">
		</c:forEach>
		
		<!-- show category info -->	
		&emsp;&nbsp;
		<input type = "text" id="add_page_category_info" name="addPageCategoryInfo" readonly size="25" value="" style="color:white;background-color:#142b2b;border:solid 0px;">
		<input type = "hidden" id="add_page_category_id" name="addPageCategoryId">
		</li>
		
	</ol>
		&emsp;&emsp;&emsp;<input type = "button" id="addPageBtn" value = "新增Page" 
					style="background-color:#497440;font-weight:bold;color:#ffffff;">
	</form>
	</fieldset>
	<br>
	<div id="outer">  
    <div class="my_text" id="content"></div>
	</div>
	
    <div id="addPageInfo">
		<fieldset>
			<legend >新加入的Page</legend>
			<div id="ajaxAddPageInfo"></div>
		</fieldset>
	</div>
	<!-- Add page end -->

</body>
</html>