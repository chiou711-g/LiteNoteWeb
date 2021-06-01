$(document).ready(function() {
	
	$("#addCategoryForm").submit(function(e) {
		e.preventDefault();
		});
	$("#addCategoryBtn").click(function(e) {
		formCheck(addCategoryForm);
		function formCheck(inFm){
			str="";
			
			if(inFm.add_category_name.value.length<1)	{
				str+="- Category name 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.add_category_id.focus();
				return false;
			}
			else
			{
				doAjax();
			}
		}
	
		function doAjax(){
			dataString = $("#add_category_form").serialize();
			
			// call addCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "AddCategory", 
			data : dataString,
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxAddCategoryInfo").html("");
					$("#ajaxAddCategoryInfo").append("<b>ADD Category:</b> "
							+ showCategory.categoryInfo.category_id + "<br/>");
					$("#ajaxAddCategoryInfo").append("<b>Categories Count:</b> "
							+ showCategory.totalCategoriesCount + "<br/>");					
				}
				else
				{
					$("#ajaxAddCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxAddCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#addCategoryBtn').attr("disabled",true); //avoid Add 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#addCategoryBtn').attr("disabled",false); //可按button
				
				// clear form
				//document.getElementById("addCategoryForm").reset();
				// clear input
				document.getElementById("add_category_id").value="";
			}
			});
	}
	});
});