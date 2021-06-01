$(document).ready(function() {
	list_categories();
	
	$("#edit_page_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varModifyPage").click(function(e) {
		formCheck(editPageForm);
		function formCheck(inFm){
			str="";

			if(inFm.edit_page_title.value.length<1)
			{
				str+="- Page title 不正確\n";
			}
			
			if(inFm.edit_page_category_id.value.length<1)
			{
				str+="- Category selection 不正確\n";
			}
						
			
			if(str.length>0)
			{
				alert(str);
				inFm.edit_page_id.focus();
				return false;
			}
			else
			{
				doModifyAjax();
			}
		}
	
		// modify ajax
		function doModifyAjax(){
			var page_id = $("input#edit_page_id").val();
			var page_title = $("input#edit_page_title").val();
			var page_category_id = $("input#edit_page_category_id").val();
			var varModifyPage = $("input#varModifyPage").val();

			// call editPage HttpServlet
			$.ajax({
			type : "POST",
			url : "EditPage", 
			data : {
				page_id,
				page_title,
				page_category_id,
				varModifyPage
			},
			dataType : "json",
			success : function(showPage,textStatus, jqXHR) {
				if (showPage.success)
				{
					$("#ajaxEditPageInfo").html("");
					
					$("#ajaxEditPageInfo").append("<b>Page is modified.</b> <br><br>");

					$("#ajaxEditPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp;<b>Page Id:</b> "
							+ showPage.pageInfo.page_id + "<br/>");
					$("#ajaxEditPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp;<b>Page Title:</b> "
							+ showPage.pageInfo.page_title + "<br/>");
					$("#ajaxEditPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp;<b>Page Category Id:</b> "
							+ showPage.pageInfo.page_category_id + "<br/>");
				}
				else
				{
					$("#ajaxEditPageInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditPageInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varModifyPage').attr("disabled",true); //avoid Edit page 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varModifyPage').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// Ajax: delete page 
	$("#varDeletePage").click(function(e) {
		var r = confirm("確定要刪除這page?")
		if( r== true)
			doDeleteAjax();
	
		function doDeleteAjax(){
			var page_id = $("input#edit_page_id").val();
			var varDeletePage = $("input#varDeletePage").val();

			// call editPage HttpServlet
			$.ajax({
			type : "POST",
			url : "EditPage", 
			data : {
				page_id,
				varDeletePage
			},
			dataType : "json",
			success : function(showPage,textStatus, jqXHR) {
				if (showPage.success)
				{
					$("#ajaxEditPageInfo").html("");
					$("#ajaxEditPageInfo").append("<b>Page is deleted.</b> <br><br>");
					$("#ajaxEditPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp; <b>ID:</b> "
							+ showPage.pageInfo.page_id + "<br/>");
				}
				else
				{
					$("#ajaxEditPageInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditPageInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varDeletePage').attr("disabled",true); //avoid Edit page 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varDeletePage').attr("disabled",false); //可按button
			}
			});
		}
	});	


	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("edit_page_title").value = "";
				document.getElementById("edit_page_category_id").value = "";
			}	
		);	
	
});

function list_categories()
{
	// call ListCategories HttpServlet
	$.ajax({
	type : "POST",
	url : "ListCategories", 
	data : {},
	dataType : "json",
	success : function(showCategory,textStatus, jqXHR) {
		if (showCategory.success)
		{
			
			var i;
			for(i =1; i<= showCategory.totalCategoriesCount; i++)
			{
				$("#category_description"+ i).html("");
				
				var getCategoryId;
				var getCategoryName;
				
				getCategoryId = showCategory.categories[i-1].category_id;
				getCategoryName = showCategory.categories[i-1].category_name;
				
				$("#category_description"+ i).append("<b>" + i + ". " + getCategoryName + "  (c." + getCategoryId + ")</b>");
				document.getElementById("category_id" + i).value = getCategoryId;
			}
		}
	},
	error : function(jqXHR,textStatus,errorThrown)
	{
		console.log("Something really bad happened "
				+ textStatus);
	},
	beforeSend : function(jqXHR, settings) {
		settings.data += "&dummyData=whatever";
	},
	complete : function(jqXHR,textStatus) {
	}
	});	
}

function getCategoryInfo()
{
	var sel = document.getElementById("category_selection");
	var selPos =  sel.selectedIndex + 1;
	document.getElementById('edit_page_category_id').value   = document.getElementById("category_id"+selPos).value;
}
