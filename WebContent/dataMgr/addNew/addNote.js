$(document).ready(function() {
	list_pages();
	
	$("#addNoteForm").submit(function(e) {
		e.preventDefault();
		});
	$("#addNoteBtn").click(function(e) {
		formCheck(addNoteForm);
		function formCheck(inFm){
			str="";
			if(inFm.note_link_uri.value.length<9)
			{
				str+="- URI不完整\n";
			}
			
			if(inFm.note_title.value.length<1)
			{
				str+="- TITLE不完整\n";
			}
			
			if(inFm.page_id.value.length<1)
			{
				str+="- PAGE ID不完整\n";
			}			
			
			if(str.length>0)
			{
				alert(str);
				inFm.note_id.focus();
				return false;
			}
			else
			{
				doAjax();
			}
		}
	
		function doAjax(){
			dataString = $("#addNoteForm").serialize();
			
			// call addNote HttpServlet
			$.ajax({
			type : "POST",
			url : "AddNote", 
			data : dataString,
			dataType : "json",
			success : function(showNote,textStatus, jqXHR) {
				if (showNote.success)
				{
					$("#ajaxAddNoteInfo").html("");
					$("#ajaxAddNoteInfo").append("<b>Link URI:</b> "
							+ showNote.noteInfo.note_link_uri + "<br/>");
					$("#ajaxAddNoteInfo").append("<b>Image URI:</b> "
							+ showNote.noteInfo.note_image_uri + "<br/>");
					$("#ajaxAddNoteInfo").append("<b>TITLE:</b> "
							+ showNote.noteInfo.note_title + "<br/>");
					$("#ajaxAddNoteInfo").append("<b>Count:</b> "
							+ showNote.totalRowsCount + "<br/>");
				}
				else
				{
					$("#ajaxAddNoteInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxAddNoteInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#addNoteBtn').attr("disabled",true); //avoid Add data 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#addNoteBtn').attr("disabled",false); //可按button
				
				// clear form
				//document.getElementById("addNoteForm").reset();
				// clear input
				document.getElementById("note_link_uri").value="";
				document.getElementById("note_title").value="";
			}
			});
		}
	});
});

function list_pages()
{
	// call ListPages HttpServlet
	$.ajax({
	type : "POST",
	url : "ListPages", 
	data : {},
	dataType : "json",
	success : function(showPage,textStatus, jqXHR) {
		if (showPage.success)
		{
			var i;
			for(i =1; i<= showPage.totalPagesCount; i++)
			{
				$("#page_description"+ i).html("");
				
				var getPageId;
				var getPageName;
				
				getPageId = showPage.pages[i-1].data_table_id;
				getPageName = showPage.pages[i-1].page_title;
				
				$("#page_description"+ i).append("<b>" + i + ". " + getPageName + "  (p." + getPageId + ")</b>");
				document.getElementById("page_id" + i).value = getPageId;
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

function getPageInfo()
{
	var sel = document.getElementById("page_selection");
	var selPos =  sel.selectedIndex + 1;
	var page_id = document.getElementById("page_id"+selPos).value;
	
	document.getElementById('page_id').value   = page_id;
}