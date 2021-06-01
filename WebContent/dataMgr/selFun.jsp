<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean" scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService" scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<jsp:useBean id="pageBean" class="pageBean.PageBean" scope="application" />
<jsp:setProperty property="*" name="pageBean" />
<jsp:useBean id="pageService" class="pageBean.PageService" scope="application" />
<jsp:setProperty property="*" name="pageService" />

<jsp:useBean id="noteBean" class="noteBean.NoteBean" scope="application" />
<jsp:setProperty property="*" name="noteBean" />
<jsp:useBean id="noteService" class="noteBean.NoteService" scope="application" />
<jsp:setProperty property="*" name="noteService" />

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Select function</title>

<link rel="stylesheet" type="text/css" href="../myStyle.css">
</head>
<body>

<table border=0 width=100% align=right >
    <tr align="center"><td><a href="../index.html" target="_top">
        <font size="5">[更新資料]</font></a></td>
    </tr>
        
	<tr><td><hr></td></tr>

	<tr><td > <div align="center">Add new </div></td></tr>	
	
	<!-- Add new -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/addNew/addCategory.html">
		<div align="center"><input type="submit" value="Category" onclick="parent.postMessage('add new', '*');"/></div>
	</form>
	</td></tr>
	
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/addNew/addPage.jsp">
		<div align="center"><input type="submit" value="Page" onclick="parent.postMessage('add new', '*');"/></div>
	</form>
	</td></tr> 	
	
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/addNew/addNote.jsp">
		<div align="center"><input type="submit" value="Link" onclick="parent.postMessage('add new', '*');"/></div>
	</form>
	</td></tr>  
	
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/addNew/addPlaylist.jsp">
		<div align="center"><input type="submit" value="Playlist" onclick="parent.postMessage('add new', '*');"/></div>
	</form>
	</td></tr>  	
		
	<tr><td><hr></td></tr>		
	
	<!-- Edit category -->
	<tr><td > <div align="center">Edit </div></td></tr>
    <tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/edit/editCategory.jsp">
 		<input type="hidden" id="edit_category_id" name="editCategoryId" value="${categoryService.current_category_id}">
		<input type="hidden" id="edit_category_name" name="editCategoryName">
		<div align="center"><input type="submit" value="Category" onclick="parent.postMessage('edit', '*');"/></div>
	</form> 
	</td></tr>   
	
	<!-- Edit page -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/edit/editPage.jsp">
 		<input type="hidden" name="editPageId" value="${pageService.pageTableId}">
		<input type="hidden" id="edit_page_title" name="editPageTitle">
		<input type="hidden" id="edit_page_category_id" name="editPageCategoryId">
		<div align="center"><input type="submit" value="Page" onclick="parent.postMessage('edit', '*');"/></div>
	</form>
	</td></tr>  
	
	<tr><td><hr></td></tr>	
	<tr><td > <div align="center">Swap</div></td></tr>

	<!-- Swap category -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/swapCategory.jsp">
		<div align="center"><input type="submit" value="Category" onclick="parent.postMessage('swap', '*');"/></div>
	</form>
	</td></tr>  
	
	<!-- Swap page -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/swapPage.jsp">
		<div align="center"><input type="submit" value="Page" onclick="parent.postMessage('swapPage', '*');"/></div>
	</form>
	</td></tr>  
	
	<!-- Swap note -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/swapNote.jsp">
		<div align="center"><input type="submit" value="Link" onclick="parent.postMessage('swapLink', '*');"/></div>
	</form>
	</td></tr>  	

	<tr><td><hr></td></tr>	
	<tr><td > <div align="center">Move</div></td></tr>

	<!-- Move category -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/moveCategory.jsp">
		<div align="center"><input type="submit" value="Category" onclick="parent.postMessage('moveCategory', '*');"/></div>
	</form>
	</td></tr>  
	
	<!-- Move page -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/movePage.jsp">
		<div align="center"><input type="submit" value="Page" onclick="parent.postMessage('movePage', '*');"/></div>
	</form>
	</td></tr>  
	
	<!-- Move note -->
	<tr><td>
 	<form action="../../../LiteNoteWeb/dataMgr/operation/moveNote.jsp">
		<div align="center"><input type="submit" value="Link" onclick="parent.postMessage('moveLink', '*');"/></div>
	</form>
	</td></tr>  	
	
</table>
 
</body>
</html>