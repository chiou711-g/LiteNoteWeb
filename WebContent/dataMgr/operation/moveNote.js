$(document).ready(function() {
	
	$("#move_note_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varMoveNote").click(function(e) {
		formCheck(moveNoteForm);
		function formCheck(inFm){
			str="";

			if(inFm.move_note_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.move_note_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.move_note_pos1.focus();
				return false;
			}
			else
			{
				doMoveAjax();
			}
		}
	
		// move ajax
		function doMoveAjax(){
			var page_number = $("input#page_number").val();
			var note_pos1 = $("input#move_note_pos1").val();
			var note_pos2 = $("input#move_note_pos2").val();
			var varMoveNote = $("input#varMoveNote").val();
			// call moveNote HttpServlet
			$.ajax({
			type : "POST",
			url : "MoveNote", 
			data : {
				page_number,
				note_pos1,
				note_pos2,
				varMoveNote
			},
			dataType : "json",
			success : function(showNote,textStatus, jqXHR) {
				if (showNote.success)
				{
					$("#ajaxMoveNoteInfo").html("");
					$("#ajaxMoveNoteInfo").append("<b>Links are moved.</b> <br><br>");
				}
				else
				{
					$("#ajaxMoveNoteInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxMoveNoteInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varMoveNote').attr("disabled",true); //avoid Move note 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varMoveNote').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("move_note_pos1").value = "";
				document.getElementById("move_note_pos2").value = "";
			}	
		);	
	
});
