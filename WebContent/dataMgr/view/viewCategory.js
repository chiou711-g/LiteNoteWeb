$(document).ready(function() {
	view_category();
});

function select_category(new_value) {
	  document.getElementById("category_pos").value = new_value;
	  view_category();
}

function view_category()
{
	var category_pos = $("input#category_pos").val();

	// call viewCategory HttpServlet
	$.ajax({
	type : "POST",
	url : "ViewCategory", 
	data : {category_pos},
	dataType : "json",
	success : function(showCategory,textStatus, jqXHR) {
		if (showCategory.success)
		{
			
			var i;
			for(i =1; i<= showCategory.totalCategoriesCount; i++)
			{
				$("#category_name"+ i).html("");
				$("#category_id"+ i).html("");
				
				var getCategoryId;
				var getCategoryName;
				
				getCategoryId = showCategory.categories[i-1].category_id;
				getCategoryName = showCategory.categories[i-1].category_name;
				
				$("#category_name"+ i).append("<b>" + getCategoryName + "</b>");				
				$("#category_id"+ i).append("<b>(c." + getCategoryId + ")</b>");
			}
			
			$("#total_categories").html("");
			$("#total_categories").append(showCategory.totalCategoriesCount);	
			
			
			// category name in 相關Pages
			$("#current_category_name").html("");
			$("#current_category_name").append("<b>" + showCategory.category_name + "</b>");
			
			// category id in 相關Pages
			$("#current_category_id").html("");
			$("#current_category_id").append("(c." + showCategory.category_id + ")");
			
			// for Edit category
			document.getElementById("category_pos").value = showCategory.category_pos;
			document.getElementById("category_id").value = showCategory.category_id;
		}
		else
		{
			$("#total_categories").html("<div><b>Input is Invalid!</b></div>");
		}
	},
	error : function(jqXHR,textStatus,errorThrown)
	{
		console.log("Something really bad happened "
				+ textStatus);
		$("#total_categories").html(jqXHR.responseText);
	},
	beforeSend : function(jqXHR, settings) {
		settings.data += "&dummyData=whatever";
	},
	complete : function(jqXHR,textStatus) {
		view_page();
	}
	});	

}
