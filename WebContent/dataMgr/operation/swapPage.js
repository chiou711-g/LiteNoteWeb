$(document).ready(function() {
	
	$("#swap_page_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varSwapPage").click(function(e) {
		formCheck(swapPageForm);
		function formCheck(inFm){
			str="";

			if(inFm.swap_page_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.swap_page_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.swap_page_pos1.focus();
				return false;
			}
			else
			{
				doSwapAjax();
			}
		}
	
		// swap ajax
		function doSwapAjax(){
			var page_pos1 = $("input#swap_page_pos1").val();
			var page_pos2 = $("input#swap_page_pos2").val();
			var varSwapPage = $("input#varSwapPage").val();
			// call swapPage HttpServlet
			$.ajax({
			type : "POST",
			url : "SwapPage", 
			data : {
				page_pos1,
				page_pos2,
				varSwapPage
			},
			dataType : "json",
			success : function(showPage,textStatus, jqXHR) {
				if (showPage.success)
				{
					$("#ajaxSwapPageInfo").html("");
					$("#ajaxSwapPageInfo").append("<b>2 pages are swaped.</b> <br><br>");
				}
				else
				{
					$("#ajaxSwapPageInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxSwapPageInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varSwapPage').attr("disabled",true); //avoid Swap page 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varSwapPage').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("swap_page_pos1").value = "";
				document.getElementById("swap_page_pos2").value = "";
			}	
		);	
	
});
