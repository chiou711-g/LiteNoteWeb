$(document).ready(function() {
	view_category();
	view_pages_init();
	document.getElementById("reset").click();
	
	// initial selection
	document.getElementById("sel_JSON").checked = false;
	selJson();
	document.getElementById("sel_HTML").checked = false;
	selHtml();
});


$(document).ready(function() {
	
	$("#export_links_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#save_json_output_btn").click(function(e) {
		str="";
		if(export_links_form.json_file_name.value.length<1)
		{
			str+="file name不完整\n";
		}			
		
		if(str.length>0)
		{
			alert(str);
			export_links_form.json_file_name.focus();
			return false;
		}
		else
		{
			exportJson();
		}
		
	});
	
	$("#export_table_view_btn").click(function(e) {
		str="";
		if(export_links_form.table_view_file_name.value.length<1)
		{
			str+="list file name不完整\n";
		}			
		
		if(str.length>0)
		{
			alert(str);
			export_links_form.list_view_file_name.focus();
			return false;
		}
		else
		{
			exportTableView();
		}
	});
	
	$("#export_list_view_btn").click(function(e) {
		str="";
		if(export_links_form.list_view_file_name.value.length<1)
		{
			str+="list file name不完整\n";
		}			
		
		if(str.length>0)
		{
			alert(str);
			export_links_form.list_view_file_name.focus();
			return false;
		}
		else
		{
			exportListView();
		}
		
	});
});

function view_pages_init()
{
	var cats_cnt = document.getElementById("categories_cnt").value;
	var pgs_cnt = document.getElementById("pages_cnt").value;
	var j;
	var i;
	for(j =1; j<= cats_cnt; j++)
	{
		for(i =1; i<= pgs_cnt; i++)
		{
			$("#row_num"+j+"id"+ i).html("");
			$("#row_num"+j+"title"+ i).html("");
	
			document.getElementById("row_num"+j+"cell"+i).style.display = "none";				
		}
		
		// show category Id
		$("#row_num"+j+"category_id").html("");
		document.getElementById("row_num"+j+"category_id").style.display = "none";
	}	
}

function checkAll()
{
	//console.log("test toggle all");
	var items = document.getElementsByName("category_selection");
	var check_count = 0;
	for(var i=0; i<items.length; i++){
		if(items[i].type=='checkbox' && items[i].checked==false)
		{
			items[i].checked=true;
		}
		
		categoryToggle(items[i].checked,i+1) 
	}
	console.log("check_count = " + check_count);
	console.log("items.length = " + items.length);
	if(check_count == items.length)
		document.getElementById("check_all").value =  "全不選";
	else if(check_count == 0)
		document.getElementById("check_all").value =  "全選";
	else
		document.getElementById("check_all").value =  "Toggle";
}

function toggleAll()
{
	//console.log("test toggle all");
	var items = document.getElementsByName("category_selection");
	var check_count = 0;
	for(var i=0; i<items.length; i++){
		if(items[i].type=='checkbox' && items[i].checked==true)
		{
			items[i].checked=false;
		}
		else
		{
			items[i].checked=true;
		}
		
		categoryToggle(items[i].checked,i+1) 
	}
}

function view_category()
{
	var total_categories_count;
	// call viewCategories HttpServlet
	$.ajax({
	type : "POST",
	url : "ViewCategories", 
	data : null,
	dataType : "json",
	success : function(showCategory,textStatus, jqXHR) {
		if (showCategory.success)
		{
			total_categories_count = showCategory.totalCategoriesCount;
			var i;
			for(i =1; i<= showCategory.totalCategoriesCount; i++)
			{
				$("#category_id"+ i).html("");
				$("#category_name"+ i).html("");
				
				var getCategoryId;
				var getCategoryName;
				
				getCategoryId = showCategory.categories[i-1].category_id;
				getCategoryName = showCategory.categories[i-1].category_name;
				
				$("#category_name"+ i).append("<b>" + getCategoryName + "</b>");
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

function categoryToggle(checkbox,currentCategoryPos) {
	var items = document.getElementsByName("category_selection");
	var selectedItems = [];
	for(var i=0; i<items.length; i++){
		if(items[i].type=='checkbox' && items[i].checked==true)
			selectedItems[i] =items[i].value;
		else
			selectedItems[i] =0;
	}
	viewPages(selectedItems,currentCategoryPos);
}

function viewPages(category_ids,currentCategoryPos)
{
	// call ViewPages HttpServlet
	$.ajax({
	type : "POST",
	url : "ViewPages", 
	dataType : "json",
	data : {category_ids:category_ids,currentCategoryPos},
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.success)
		{
			var catId = showPage.currentCategoryId;
			var catName = showPage.currentCategoryName;
			var j = currentCategoryPos;

			$("#row_num"+j+"category_id").html("");
			$("#row_num"+j+"category_name").html("");
			var items = document.getElementsByName("category_selection");
			if(items[j-1].type=='checkbox' && items[j-1].checked==true)
			{
				// show row number
				document.getElementById("row_num"+j).style.display = "table-cell";	

				// show category Id
				$("#row_num"+j+"category_id").append("<b>" + "(c." + catId + ")</b>");
				document.getElementById("row_num"+j+"category_id").style.display = "table-cell";	
				
				// show category name
				$("#row_num"+j+"category_name").append("<b>" + catName + "</b>");
				document.getElementById("row_num"+j+"category_name").style.display = "table-cell";	
			}
			else
			{
				// hide row number
				document.getElementById("row_num"+j).style.display = "none";
				
				// hide category Id
				document.getElementById("row_num"+j+"category_id").style.display = "none";	
				
				// hide category name
				document.getElementById("row_num"+j+"category_name").style.display = "none";	
			}
			
			// show pages
			var i;
			for(i =1; i<= showPage.totalPagesCount; i++)
			{
				$("#row_num"+j+"id"+ i).html("");
				$("#row_num"+j+"title"+ i).html("");
				$("#row_num"+j+"page_id"+ i).html("");
				
				var getTitle;
				var getCategory_id;
				var getPage_table_id;
				getTitle = showPage.pages[i-1].title;
				getCategory_id = showPage.pages[i-1].category_id;
				getPage_table_id = showPage.pages[i-1].page_id;
				document.getElementById("row_num"+j+"checkbox"+i).checked = showPage.pages[i-1].category_checked_id;
				
				if( (getTitle != null) &&  (getCategory_id == catId))
				{
					$("#row_num"+j+"title"+ i).append("<b>" + getTitle + "</b>");
					$("#row_num"+j+"id"+ i).append("<b>" + i + ".</b>");
					$("#row_num"+j+"page_id"+ i).append("<b>(p." + getPage_table_id + ")</b>");
					document.getElementById("row_num"+j+"cell"+i).style.display = "table-cell";			
				}
				else
				{
					document.getElementById("row_num"+j+"cell"+i).style.display = "none";				
				}
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

function exportJson()
{
	// clear previous content
	$("#json_output").html("");
	
	dataString = $("#export_links_form").serialize();
	// call exportJson HttpServlet
	$.ajax({
	type : "POST",
	url : "ExportJson", 
	dataType : "json",
	data : dataString,
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.client != "none")
		{
			$("#json_output").html("{\"client\":");
			$("#json_output").append("\""+ showPage.client+ "\"")
					.append(",\"content\":")
					.append(showPage.content)
					.append("}");
		}
		else
		{
			document.getElementById("json_output").html
			$("#json_output").html("");
			$("#json_output").append(showPage.content);			
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

function exportListView()
{
	// clear previous content
	$("#links_list_output").html("");
	
	dataString = $("#export_links_form").serialize();
	// call ExportHtml HttpServlet
	$.ajax({
	type : "POST",
	url : "ExportListView", 
	dataType : "json",
	data : dataString,
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.client != "none")
		{
			var i;
			for(i=0;i<showPage.categories.length;i++)
			{
				var j;
				for(j=0;j<showPage.categories[i].pages.length;j++)
				{
					var k;
					for(k=0;k<showPage.categories[i].pages[j].links.length;k++)
					{
						var link =showPage.categories[i].pages[j].links[k];
						$("#links_list_output").append(link.note_link_uri+"<br>");
					}
				}
			}
		}
		else
		{
			document.getElementById("links_list_output").html
			$("#links_list_output").html("");
			$("#links_list_output").append(showPage.content);			
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

function exportTableView()
{
	// clear previous content
	$("#links_list_output").html("");
	
	dataString = $("#export_links_form").serialize();
	// call ExportHtml HttpServlet
	$.ajax({
	type : "POST",
	url : "ExportTableView", 
	dataType : "json",
	data : dataString,
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.client != "none")
		{
			var i;
			for(i=0;i<showPage.categories.length;i++)
			{
				var j;
				for(j=0;j<showPage.categories[i].pages.length;j++)
				{
					var k;
					for(k=0;k<showPage.categories[i].pages[j].links.length;k++)
					{
						var link =showPage.categories[i].pages[j].links[k];
						$("#links_list_output").append(link.note_link_uri+"\n\r");
					}
				}
			}
		}
		else
		{
			document.getElementById("links_list_output").html
			$("#links_list_output").html("");
			$("#links_list_output").append(showPage.content);			
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


function CopyToClipboard(containerid) {
	if (document.selection) {
		var range = document.body.createTextRange();
		range.moveToElementText(document.getElementById(containerid));
		range.select().createTextRange();
		document.execCommand("copy");
	} else if (window.getSelection) {
		var range = document.createRange();
		range.selectNode(document.getElementById(containerid));
		window.getSelection().removeAllRanges();
		window.getSelection().addRange(range);
		document.execCommand("copy");
		alert("已複製Json");
	}
}

function CopyListToClipboard(containerid) {
	if (document.selection) {
		var range = document.body.createTextRange();
		range.moveToElementText(document.getElementById(containerid));
		range.select().createTextRange();
		document.execCommand("copy");
	} else if (window.getSelection) {
		var range = document.createRange();
		range.selectNode(document.getElementById(containerid));
		window.getSelection().removeAllRanges();
		window.getSelection().addRange(range);
		document.execCommand("copy");
		alert("已複製List");
	}
}

function ClearJSON(containerid) {
	$("#json_output").html("");
}

function ClearList(containerid) {
	//document.getElementById("links_list_output").innerHTML="";
	$("#links_list_output").html("");
}

function getFileName()
{
	var sel = document.getElementById("project_selection");
	var selPos =  sel.selectedIndex + 1;
	var file_name = document.getElementById("project_id"+selPos).value;
	
	document.getElementById('json_file_name').value = file_name;
}

function selJson() {
	// Get the checkbox
	  var checkBox = document.getElementById("sel_JSON");
	  // Get the div
	  var div = document.getElementById("export_json");

	  // If the checkbox is checked, display div
	  if (checkBox.checked == true){
	    div.style.display = "block";
	  } else {
	    div.style.display = "none";
	  }
	}

function selHtml() {
	// Get the checkbox
	  var checkBox = document.getElementById("sel_HTML");
	  // Get the div
	  var div = document.getElementById("export_html");

	  // If the checkbox is checked, display div
	  if (checkBox.checked == true){
	    div.style.display = "block";
	  } else {
	    div.style.display = "none";
	  }
	}
