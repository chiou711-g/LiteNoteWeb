$(document).ready(function() {
	
	$("#move_page_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varMovePage").click(function(e) {
		formCheck(movePageForm);
		function formCheck(inFm){
			str="";

			if(inFm.move_page_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.move_page_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.move_page_pos1.focus();
				return false;
			}
			else
			{
				doMoveAjax();
			}
		}
	
		// move ajax
		function doMoveAjax(){
			var page_pos1 = $("input#move_page_pos1").val();
			var page_pos2 = $("input#move_page_pos2").val();
			var varMovePage = $("input#varMovePage").val();
			// call movePage HttpServlet
			$.ajax({
			type : "POST",
			url : "MovePage", 
			data : {
				page_pos1,
				page_pos2,
				varMovePage
			},
			dataType : "json",
			success : function(showPage,textStatus, jqXHR) {
				if (showPage.success)
				{
					$("#ajaxMovePageInfo").html("");
					$("#ajaxMovePageInfo").append("<b>Pages are moved.</b> <br><br>");
				}
				else
				{
					$("#ajaxMovePageInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxMovePageInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varMovePage').attr("disabled",true); //avoid Move page 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varMovePage').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("move_page_pos1").value = "";
				document.getElementById("move_page_pos2").value = "";
			}	
		);	
	
});
