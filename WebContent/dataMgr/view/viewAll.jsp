<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="categoryBean" class="categoryBean.CategoryBean"
	scope="application" />
<jsp:setProperty property="*" name="categoryBean" />
<jsp:useBean id="categoryService" class="categoryBean.CategoryService"
	scope="application" />
<jsp:setProperty property="*" name="categoryService" />

<jsp:useBean id="pageBean" class="pageBean.PageBean" scope="application" />
<jsp:setProperty property="*" name="pageBean" />
<jsp:useBean id="pageService" class="pageBean.PageService"
	scope="application" />
<jsp:setProperty property="*" name="pageService" />

<jsp:useBean id="noteBean" class="noteBean.NoteBean" scope="application" />
<jsp:setProperty property="*" name="noteBean" />
<jsp:useBean id="noteService" class="noteBean.NoteService"
	scope="application" />
<jsp:setProperty property="*" name="noteService" />

<html>

<!-- head start -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View ALL</title>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>

<!-- style for links of selected page -->
<style>
/* all links in a single row */
/* 
.container {
  width: 100%; 
  height: 400px; 
  border: 1px dotted gray;
  color:#fff;
  background-color: #000;
  overflow-x: scroll;
  overflow-y: hidden;
}

 .inner {
  height: 100%;
  white-space:nowrap; 
}

.floatLeft {
  width: 260px;
  height:300px; 
  margin:1px 10px 1px 10px; 
  display: inline-block;
  border: 1px solid white;
}  
*/

/* all links in multi rows */
.inner {
	background: gray;
	width: 100%;
	height: 100%; /* height given for illustration */
	display: flex;
	flex-flow: row wrap;
	position: relative;
}

.floatLeft {
	background: black;
	margin: 2px;
	display: table-cell;
	vertical-align: middle;
	flex: 0 1 calc(8% - 2px); /* <-- adjusting for margin */
}
</style>

<link rel="stylesheet" type="text/css" href="../../myStyle.css">
<link rel="stylesheet" type="text/css" href="../../snack_bar.css">

<!-- script -->
<script src="../../jquery-3.3.1.js"></script>
<script type="text/javascript" src="viewPage.js"></script>
<script type="text/javascript" src="viewCategory.js"></script>
<script>
$(document).ready(function () {
	var $nava = $(".nav a");
	 $nava.click(function(e) {
	  $nava.removeClass('highlight')
	  $(this).addClass('highlight');
	}); 
});
</script>

</head>

<!-- body start -->
<body>
	&emsp;&emsp;
	<button id="button_menu"
		onclick="parent.postMessage('menu toggle', '*');">Toggle menu</button>
	<br>
	<br>

	<!-- View categories -->
	<fieldset>
		<legend>
			<b style="font-size: 30px; color: #7FFFD4;">All categories
				(total:<span id="total_categories"></span>)
			</b>
		</legend>
		<!-- hidden input for edit category -->
		<input type="hidden" id="category_pos"
			value="${categoryService.current_category_pos}"> <input
			type="hidden" id="category_id"
			value="${categoryService.current_category_id}">

		<!-- Show categories -->
		<div class="nav" align="center">
			<table
				style="border: 2px #FFD382 solid; color: Black; background-color: DarkGreen;"
				cellpadding="5" border='0'>
				<c:forEach var="i" begin="1"
					end="${categoryService.categories_count}" step="1"
					varStatus="status">
					<c:if test="${status.count %10 == 1}">
						<tr>
					</c:if>
					<th scope="row" style="color: #FFD382;">${status.count}</th>
					<td><font size="1"><span><a
								href="javascript:select_category(${i})" id="cat${i}"> <b
									id="category_name${i}"> </b> <b id="category_id${i}"
									style="font-size: 15px;"> </b>
							</a></span></font>
					<td><c:if test="${status.count %10 == 0} ">
							</tr>
						</c:if>
				</c:forEach>
			</table>
		</div>
	</fieldset>

	<!-- Pages in selected category -->
	<div class="nav" align="center">
		<!-- hidden input for edit page -->
		<input type="hidden" id="edit_page_title" name="editPageTitle">
		<input type="hidden" id="edit_page_category_id"
			name="editPageCategoryId">
	</div>

	<!-- 所選Category的 相關 Pages -->
	<div class="nav" align="center">
		<fieldset>
			<legend>
				<b style="font-size: 30px; color: #7FFFD4;">Related pages of Selected Category</b>
			</legend>
			<!-- Show pages by category -->
			<table cellpadding="5" border="0" width="100%">
				<tr>
					<!-- current category name -->
					<td style="font-size: 30px; color: Gold;" width="300px"><b
						id="current_category_name" style="font-size: 30px; color: Gold;"></b>
						<b id="current_category_id" style="font-size: 15px; color: Gold;"></b>
					</td>

					<c:forEach var="i" begin="1" end="${pageService.pages_count}"
						step="1" varStatus="status">
						<!-- page position number -->
						<td id="count${i}" scope="row" style="color: #ff7070;"
							width="40px" align="center">${status.count}.</td>

						<!-- page title -->
						<td id="cell${i}"><font size="1"> <a
								href="javascript:select_page(${i})" class="small" id="page${i}">
									<u><b id="title${i}"> </b></u>
							</a> <b id="page_id${i}"> </b>
						</font></td>
					</c:forEach>
				</tr>
			</table>
		</fieldset>
	</div>
	<br>
	<br>

	<!-- show snack bar -->
	<div id="snackbar"> Reading Data ...</div>

	<%--links list --%>
	<c:choose>
		<c:when test="${noteBean.readBy == 'byId'}">
			<fieldset>
				<legend align="left">
					<b style="font-size: 30px; color: #7FFFD4;">所選Page的相關Link</b>
				</legend>
				<!-- by Id -->
				<c:forEach var="noteBean" items="${noteService.noteBeanList}">
					<table id="${noteBean.note_id}" align="center"
						style="text-align: center; background-color: black" border="1">
						<tbody>
							<!-- Id -->
							<tr>
								<td>${noteBean.note_id}:${noteBean.note_title}</td>
							</tr>
							<!-- embedded image -->
							<tr>
								<td><iframe name="framename" width="640" height="360"
										src="${noteBean.note_link_uri_embed}" frameborder="0"
										allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
										allowfullscreen></iframe></td>
							</tr>
							<!-- full screen -->
							<tr>
								<td><a href="${noteBean.note_link_uri}"
									target="_self|_parent|_top|framename|_blank"> [Launch] </a></td>
							</tr>
							<!-- submit button -->
							<tr>
								<td>
									<form
										action="../../../../LiteNoteWeb/dataMgr/edit/editNote.jsp">
										<input type="hidden" name="table_number"
											value="${pageService.pageTableId}"> <input
											type="hidden" name="note_id" value="${noteBean.note_id}">
										<input type="hidden" name="note_link_uri"
											value="${noteBean.note_link_uri}"> <input
											type="hidden" name="note_title"
											value="${noteBean.note_title}"> <input type="submit"
											value="Edit" />
									</form>
								</td>
							</tr>
						</tbody>
					</table>

				</c:forEach>
			</fieldset>
		</c:when>

		<%-- by All --%>
		<c:when test="${noteBean.readBy == 'byAll'}">
			<div class="container">
				<fieldset>
					<legend>
						<b style="font-size: 30px; color: #7FFFD4;">Related links of Selected Page</b>
					</legend>
					<div class="inner">
						<c:forEach var="noteBean" items="${noteService.noteBeanList}"
							varStatus="status">
							<div class="floatLeft">
								<table border="0">
									<!-- Id -->
									<tr>
										<td><div align="left"
												style="color: #ffffff; font-size: 20px;">${status.count}</div></td>
									</tr>

									<!-- thumb nail -->
									<tr>
										<td align="left"><a target="_blank"
											href="${noteBean.note_link_uri}"> <img
												src="${noteBean.note_image_uri}" style="width: 206px">
										</a></td>
									</tr>

									<!-- full screen -->
									<tr>
										<td>
											<div align="left">
												<a href="${noteBean.note_link_uri}"
													target="_self|_parent|_top|framename|_blank"
													style="color: #ffffff; font-size: 20px;"> [Launch]</a>
											</div> <textarea rows="3" cols="29" readonly disabled wrap="hard"
												style="resize: none; color: #ffffff; background-color: #000000">${noteBean.note_title}</textarea>
											<textarea rows="3" cols="29" readonly disabled wrap="hard"
												style="resize: none; color: #ffffff; background-color: #000000">${noteBean.note_duration}</textarea>
										</td>
									</tr>

									<!-- edit button -->
									<tr>
										<td>
											<form
												action="../../../../LiteNoteWeb/dataMgr/edit/editNote.jsp">
												<input type="hidden" name="table_number"
													value="${pageService.pageTableId}"> <input
													type="hidden" name="note_id" value="${noteBean.note_id}">
												<input type="hidden" name="note_link_uri"
													value="${noteBean.note_link_uri}"> <input
													type="hidden" name="note_image_uri"
													value="${noteBean.note_image_uri}"> <input
													type="hidden" name="note_title"
													value="${noteBean.note_title}">
												<div align="left">
													<input type="submit"
														value="Modify Link (ID:${noteBean.note_id})"
														class="small_button" />
												</div>
											</form>
										</td>
									</tr>

								</table>
							</div>
						</c:forEach>
					</div>
				</fieldset>
			</div>
		</c:when>

		<c:otherwise>
			<div>No selection yet</div>
		</c:otherwise>
	</c:choose>
	<br>
	<br>

	<!-- Show all pages -->
	<fieldset>
		<legend align="top">
			<b style="font-size: 30px; color: #7FFFD4;">All pages (total:<span
				id="total_pages"></span>)
			</b>
		</legend>
		<div class="nav" align="center">
			<table cellpadding="5" border="0" width="100%">
				<c:forEach var="j" begin="1"
					end="${categoryService.categories_count}" step="1"
					varStatus="cat_status">
					<tr>
						<!-- category position number -->
						<td id="category_pos_a${j}" style="font-size: 30px; color: Gold;">${cat_status.count}</td>

						<!-- category name -->
						<td id="category_name_a${j}" style="font-size: 30px; color: Gold;"></td>

						<!-- category id -->
						<td id="category_id_a${j}" style="font-size: 15px; color: Gold;"></td>

						<!-- all pages -->
						<c:forEach var="i" begin="1" end="${pageService.pages_count}"
							step="1" varStatus="status">
							<!-- page position number  -->
							<td id="cat${j}count_a${i}" scope="row" style="color: #ff7070;"
								align="center" width="40px">${status.count}.</td>

							<!-- page name -->
							<td id="cat${j}cell_a${i}"><font size="1"><u> <a
										href="javascript:select_page_at_all(${j},${i})" class="small"
										id="${i}"> <b id="cat${j}title_a${i}"></b>
									</a></u> </font></td>

							<!-- page id  -->
							<td id="cat${j}page_id_a${i}" scope="row"
								style="color: #ffffff; font-size: 10px;" align="center"
								width="40px"></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</div>
	</fieldset>

	<!-- Current page info： -->
	<table align="center" border="1">
		<tr>
			<td><b>Page Info:</b></td>
			<td>&emsp;&emsp;</td>
			<td>Current category Id:&emsp;<span id="page_category_id"></span></td>
			<td>&emsp;&emsp;</td>
			<td>Current page Id:&emsp;<span>${pageService.pageTableId}</span></td>
			<td>&emsp;&emsp;</td>
			<td>Current page name:&emsp;<span id="page_title"></span></td>
		</tr>
	</table>

	<div align="center"></div>
	<fieldset>
		<legend align="right">View links by input:</legend>
		<form action="ViewNote.do" id="view_note_form" method="post">
			<!-- hidden input for get category position of selected page  -->
			<input type="hidden" id="click_page_category_pos"
				name="clickPageCategoryPos"
				value="${pageService.clickPageCategoryPos}">

			<div align="center">
				輸入頁Position編號： <input type="text" id="page_pos_number"
					name="pagePosNumber" size="5"
					value="${pageService.pagePositionNumber}"
					style="background-color: aqua;">

				<!-- Read All -->
				<input type="submit" name="varReadAll" value="讀取全頁"
					class="small_button"> &emsp;&emsp;&emsp;&emsp;
				<!-- Read One -->
				輸入單項ID： <input type="text" id="note_id" name="note_id"
					value="${noteBean.note_id}" size="5"
					style="background-color: aqua;"> <input type="submit"
					name="varReadOne" value="讀取單項" class="small_button">
			</div>
		</form>
	</fieldset>

</body>
</html>