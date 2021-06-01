$(document).ready(function() {
	
	$("#edit_category_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varModifyCategory").click(function(e) {
		formCheck(editCategoryForm);
		function formCheck(inFm){
			str="";

			if(inFm.edit_category_name.value.length<1)
			{
				str+="- NAME不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.edit_category_pos.focus();
				return false;
			}
			else
			{
				doModifyAjax();
			}
		}
	
		// modify ajax
		function doModifyAjax(){
			var category_pos = $("input#edit_category_pos").val();
			var category_name = $("input#edit_category_name").val();
			var varModifyCategory = $("input#varModifyCategory").val();
			// call editCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "EditCategory", 
			data : {
				category_pos,
				category_name,
				varModifyCategory
			},
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxEditCategoryInfo").html("");
					
					$("#ajaxEditCategoryInfo").append("<b>Category is modified.</b> <br><br>");

					$("#ajaxEditCategoryInfo").append("&nbsp;&nbsp;&nbsp;&nbsp;<b>Category Id:</b> "
							+ showCategory.categoryInfo.category_id + "<br/>");
					$("#ajaxEditCategoryInfo").append("&nbsp;&nbsp;&nbsp;&nbsp;<b>Category Name:</b> "
							+ showCategory.categoryInfo.category_name + "<br/>");
				}
				else
				{
					$("#ajaxEditCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varModifyCategory').attr("disabled",true); //avoid Edit category 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varModifyCategory').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// Ajax: delete category 
	$("#varDeleteCategory").click(function(e) {
		var r = confirm("確定要刪除這category?")
		if( r== true)
			doDeleteAjax();
	
		function doDeleteAjax(){
			var category_pos = $("input#edit_category_pos").val();
			var varDeleteCategory = $("input#varDeleteCategory").val();

			// call editCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "EditCategory", 
			data : {
				category_pos,
				varDeleteCategory
			},
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxEditCategoryInfo").html("");
					$("#ajaxEditCategoryInfo").append("<b>Category is deleted.</b> <br><br>");
					$("#ajaxEditCategoryInfo").append("&nbsp;&nbsp;&nbsp;&nbsp; <b>ID:</b> "
							+ showCategory.categoryInfo.category_id + "<br/>");
				}
				else
				{
					$("#ajaxEditCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varDeleteCategory').attr("disabled",true); //avoid Edit category 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varDeleteCategory').attr("disabled",false); //可按button
			}
			});
		}
	});	

	// Ajax: delete all categories 
	$("#varDeleteAllCategory").click(function(e) {
		var r = confirm("確定要刪除All categories?")
		if( r== true)
			doDeleteAllAjax();
	
		function doDeleteAllAjax(){
			//var category_pos = $("input#edit_category_pos").val();
			var varDeleteAllCategory = $("input#varDeleteAllCategory").val();

			// call editCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "EditCategory", 
			data : {
				//category_pos,
				varDeleteAllCategory
			},
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxEditCategoryInfo").html("");
					$("#ajaxEditCategoryInfo").append("<b>All categories are deleted.</b> <br><br>");
				}
				else
				{
					$("#ajaxEditCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varDeleteAllCategory').attr("disabled",true); //avoid Edit category 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varDeleteAllCategory').attr("disabled",false); //可按button
			}
			});
		}
	});	

	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("edit_category_name").value = "";
			}	
		);	
	
});
