$(document).ready(function() {
	
	$("#editNoteForm").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varModifyOne").click(function(e) {
		formCheck(editForm);
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
			
			if(str.length>0)
			{
				alert(str);
				inFm.note_id.focus();
				return false;
			}
			else
			{
				doModifyAjax();
			}
		}
	
		// modify ajax
		function doModifyAjax(){
			var page_number = $("input#page_number").val();
			var note_id = $("input#note_id").val();
			var note_link_uri = $("input#note_link_uri").val();
			var note_image_uri = $("textarea#note_image_uri").val();
			var note_title = $("input#note_title").val();
			var varModifyOne = $("input#varModifyOne").val();

			// call editNote HttpServlet
			$.ajax({
			type : "POST",
			url : "EditNote", 
			data : {
				page_number,
				note_id,
				note_link_uri,
				note_image_uri,
				note_title,
				varModifyOne
			},
			dataType : "json",
			success : function(showNote,textStatus, jqXHR) {
				if (showNote.success)
				{
					$("#ajaxEditNoteInfo").html("");
					$("#ajaxEditNoteInfo").append("<b>ID:</b> "
							+ showNote.noteInfo.note_id + "<br/>");
					$("#ajaxEditNoteInfo").append("<b>link URI:</b> "
							+ showNote.noteInfo.note_link_uri + "<br/>");
					$("#ajaxEditNoteInfo").append("<b>image URI:</b> "
							+ showNote.noteInfo.note_image_uri + "<br/>");
					$("#ajaxEditNoteInfo").append("<b>TITLE:</b> "
							+ showNote.noteInfo.note_title + "<br/>");
					$("#ajaxEditNoteInfo").append("<b>Note is modified.</b> <br/>");
				}
				else
				{
					$("#ajaxEditNoteInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditNoteInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varModifyOne').attr("disabled",true); //avoid Edit data 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varModifyOne').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// delete ajax
	$("#varDeleteOne").click(function(e) {
		var r = confirm("Sure to delete this link?")
		if( r== true)
			doDeleteAjax();
	
		function doDeleteAjax(){
			var page_number = $("input#page_number").val();
			var note_id = $("input#note_id").val();
			var note_link_uri = $("input#note_link_uri").val();
			var note_image_uri = $("textarea#note_image_uri").val();
			var note_title = $("input#note_title").val();
			var varDeleteOne = $("input#varDeleteOne").val();

			// call editNote HttpServlet
			$.ajax({
			type : "POST",
			url : "EditNote", 
			data : {
				page_number,
				note_id,
				note_link_uri,
				note_image_uri,
				note_title,
				varDeleteOne
			},
			dataType : "json",
			success : function(showNote,textStatus, jqXHR) {
				if (showNote.success)
				{
					$("#ajaxEditNoteInfo").html("");
					$("#ajaxEditNoteInfo").append("<b>ID:</b> "
							+ showNote.noteInfo.note_id + "<br/>");
					$("#ajaxEditNoteInfo").append("<b>Note is deleted.</b> <br/>");
				}
				else
				{
					$("#ajaxEditNoteInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxEditNoteInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varDeleteOne').attr("disabled",true); //avoid Edit data 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varDeleteOne').attr("disabled",false); //可按button
			}
			});
		}
	});	

	// get YouTube title
	//var varApiKey = apiAuth.keyStr;
	$("#btnGetTitle").click(
		function () {
			// with YouTube API
//	      	var url =  document.getElementById("note_link_uri").value;
//	      	var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
//	      	var match = url.match(regExp);
//	      	if (match && match[2].length == 11) {
//	      		getVids (match[2]);
//	      	}

			// without YouTube API
			var url = document.getElementById("note_link_uri").value;
			$.getJSON('https://noembed.com/embed',
			    {format: 'json', url: url}, function (data) {
			    ChangeTitle(data.title);
			    getImageUri();
			});
		}
	);

//	function getVids(vid){
//		$.get(
//		"https://www.googleapis.com/youtube/v3/videos",{
//		part: 'snippet', 
//		id: vid, 
//		key: varApiKey},
//		  function(data){
//		  ChangeTitle(data.items[0].snippet.title);
//		  }
//		)
//	}

	function getImageUri(){			
		var url =  document.getElementById("note_link_uri").value;
		var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
		var match = url.match(regExp);
		var imageUri;
		if (match && match[2].length == 11) {
			//getVids (match[2]);
			imageUri = "https://img.youtube.com/vi/"+ match[2] + "/0.jpg";
		}
		document.getElementById("note_image_uri").value = imageUri;
		return imageUri;
	}

	function ChangeTitle(titleText) {
	   document.getElementById("note_title").value = titleText;
	}
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("note_link_uri").value = "";
				document.getElementById("note_image_uri").value = "";
				document.getElementById("note_title").value = "";
			}	
		);	
	
});

