$(document).ready(function() {
	view_page();
});

function select_page(new_value) {
	document.getElementById("page_pos_number").value = new_value;
	view_page();
	read_all();
}

function select_page_at_all(new_value1,new_value2) {
	document.getElementById("click_page_category_pos").value = new_value1;
	document.getElementById("page_pos_number").value = new_value2;
	view_page();
	read_all();
}

function view_page()
{
	var click_page_category_pos = $("input#click_page_category_pos").val();
	var page_pos_number = $("input#page_pos_number").val();

	// call viewPage HttpServlet
	$.ajax({
	type : "POST",
	url : "ViewPage", 
	data : {page_pos_number},
	dataType : "json",
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.success)
		{
			// pages by category selection
			var i;
			for(i =1; i<= showPage.totalPagesCount; i++)
			{	
				$("#page_category_id"+ i).html("");

				$("#title"+ i).html("");
				$("#page_id"+ i).html("");
				
				var getPageTitle = showPage.pages[i-1].title;
				var getCategory_id = showPage.pages[i-1].category_id;
				var getPageId = showPage.pages[i-1].data_table_id;
			
				var x = document.getElementById("category_id").value;
				
				if(x == getCategory_id)
				{
					// page title
					$("#title"+ i).append("<b>" + getPageTitle + "</b>");
					$("#page_id"+ i).append("<b>(p." + getPageId + ")</b>");
					
					document.getElementById("cell"+i).style.display = "table-cell";			
					document.getElementById("count"+i).style.display = "table-cell";			
				}
				else
				{
					 document.getElementById("cell"+i).style.display = "none";	
					 document.getElementById("count"+i).style.display = "none";	
				}
			}
			
			// all pages
			var cates = showPage.categories;
			var cat_count = cates.length;
			
			var j;
			for(j=1; j<= cat_count; j++)
			{
				// category id
				$("#category_id_a"+ j).html("");
				$("#category_id_a"+ j).append("<b>" + "(c." + cates[j-1].category_id + ")" + "</b>");
				
				// category name
				$("#category_name_a"+ j).html("");
				$("#category_name_a"+ j).append("<b>" + cates[j-1].category_name + "</b>");

				var i;
				for(i =1; i<= showPage.totalPagesCount; i++)
				{	
					var getPageId = showPage.pages[i-1].data_table_id;
					var getTitle = showPage.pages[i-1].title;
					var getCategory_id = showPage.pages[i-1].category_id;
					
					if(cates[j-1].category_id == getCategory_id)
					{
						// page id
						$("#cat"+ j + "page_id_a"+ i).html("");
						$("#cat"+ j + "page_id_a"+ i).append("<b>" + "(p." + getPageId + ")" + "</b>");
						
						// page title
						$("#cat"+ j + "title_a"+ i).html("");
						$("#cat" + j + "title_a"+ i).append("<b>" + getTitle + "</b>");
						
						// show cell
						document.getElementById("cat"+ j + "page_id_a"+i).style.display = "table-cell";			
						document.getElementById("cat"+ j + "cell_a"+i).style.display = "table-cell";			
						document.getElementById("cat"+ j + "count_a"+i).style.display = "table-cell";	
					}
					else
					{
						// hide cell
						document.getElementById("cat"+ j +"page_id_a"+i).style.display = "none";			
						document.getElementById("cat"+ j +"cell_a"+i).style.display = "none";			
						document.getElementById("cat"+ j + "count_a"+i).style.display = "none";						
					}
				}
			}
			
			
			$("#total_pages").html("");
			$("#total_pages").append( showPage.totalPagesCount);	
			
			$("#page_title").html("");
			$("#page_title").append( showPage.page_title);
			
			$("#page_category_id").html("");
			$("#page_category_id").append( showPage.page_category_id);	
			
			document.getElementById("edit_page_title").value = showPage.page_title;
			document.getElementById("edit_page_category_id").value = showPage.page_category_id;			
		
			
			var hlPageNum = document.getElementById("page_pos_number").value;
			var cate_pos = document.getElementById("click_page_category_pos").value;
			
			// highlight page in selected category pages
			if(document.getElementById(hlPageNum) != null){
				document.getElementById("title" + hlPageNum).style.background = "blue";
			}
			
			// highlight page in  all pages
			if(document.getElementById(cate_pos,hlPageNum) != null){
				document.getElementById("cat" + cate_pos + "title_a" + hlPageNum).style.background = "blue";
			}			
		}
		else
		{
			$("#total_pages").html("<div><b>Input is Invalid!</b></div>");
		}
	},
	error : function(jqXHR,textStatus,errorThrown)
	{
		console.log("Something really bad happened "
				+ textStatus);
		$("#total_pages").html(jqXHR.responseText);
	},
	beforeSend : function(jqXHR, settings) {
		settings.data += "&dummyData=whatever";
	},
	complete : function(jqXHR,textStatus) {
	}
	});	
}

//submit form
function read_all()
{
	document.getElementById("view_note_form").submit();

}

//submit form by ID
function read_item()
{
	document.getElementById("view_note_form").submit();
}
