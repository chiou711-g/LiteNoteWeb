$(document).ready(function() {
	list_categories();
	
	$("#addPageForm").submit(function(e) {
		e.preventDefault();
		});
	$("#addPageBtn").click(function(e) {
		formCheck(addPageForm);
		function formCheck(inFm){
			str="";
			
			if(inFm.add_page_title.value.length<1)	{
				str+="- Page Title不完整\n";
			}
			else if(inFm.add_page_category_id.value.length<1)	{
				str+=inFm.add_page_category_id.value + "- Page Category Id不完整\n" ;
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.add_page_number.focus();
				return false;
			}
			else
			{
				doAjax();
			}
		}
	
		function doAjax(){
			dataString = $("#addPageForm").serialize();
			
			// call addPage HttpServlet
			$.ajax({
			type : "POST",
			url : "AddPage", 
			data : dataString,
			dataType : "json",
			success : function(showPage,textStatus, jqXHR) {
				if (showPage.success)
				{
					$("#ajaxAddPageInfo").html("");
					$("#ajaxAddPageInfo").append("<b>Page is added.</b> <br><br>");
					$("#ajaxAddPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp; <b>Page Id:</b> "
							+ showPage.pageInfo.page_id + "<br/>");
					$("#ajaxAddPageInfo").append("&nbsp;&nbsp;&nbsp;&nbsp; <b>Pages Count:</b> "
							+ showPage.totalPagesCount + "<br/>");					
				}
				else
				{
					$("#ajaxAddPageInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxAddPageInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#addPageBtn').attr("disabled",true); //avoid Add page 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#addPageBtn').attr("disabled",false); //可按button
				
				// clear input
				document.getElementById("add_page_number").value="";
			}
			});
	}
	});
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
	var cate_id = document.getElementById("category_id"+selPos).value;
	
	document.getElementById('add_page_category_id').value   = cate_id;
	document.getElementById('add_page_category_info').value   = "現在選用Category Id: "+ cate_id;
}
