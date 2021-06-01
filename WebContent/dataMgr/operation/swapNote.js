$(document).ready(function() {
	
	$("#swap_note_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varSwapNote").click(function(e) {
		formCheck(swapNoteForm);
		function formCheck(inFm){
			str="";

			if(inFm.swap_note_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.swap_note_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.swap_note_pos1.focus();
				return false;
			}
			else
			{
				doSwapAjax();
			}
		}
	
		// swap ajax
		function doSwapAjax(){
			var page_number = $("input#page_number").val();
			var note_pos1 = $("input#swap_note_pos1").val();
			var note_pos2 = $("input#swap_note_pos2").val();
			var varSwapNote = $("input#varSwapNote").val();
			// call swapNote HttpServlet
			$.ajax({
			type : "POST",
			url : "SwapNote", 
			data : {
				page_number,
				note_pos1,
				note_pos2,
				varSwapNote
			},
			dataType : "json",
			success : function(showNote,textStatus, jqXHR) {
				if (showNote.success)
				{
					$("#ajaxSwapNoteInfo").html("");
					$("#ajaxSwapNoteInfo").append("<b>2 links are swaped.</b> <br><br>");
				}
				else
				{
					$("#ajaxSwapNoteInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxSwapNoteInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varSwapNote').attr("disabled",true); //avoid Swap note 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varSwapNote').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("swap_note_pos1").value = "";
				document.getElementById("swap_note_pos2").value = "";
			}	
		);	
	
});
