$(document).ready(function() {
	
	$("#swap_category_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varSwapCategory").click(function(e) {
		formCheck(swapCategoryForm);
		function formCheck(inFm){
			str="";

			if(inFm.swap_category_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.swap_category_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.swap_category_pos.focus();
				return false;
			}
			else
			{
				doSwapAjax();
			}
		}
	
		// swap ajax
		function doSwapAjax(){
			var category_pos1 = $("input#swap_category_pos1").val();
			var category_pos2 = $("input#swap_category_pos2").val();
			var varSwapCategory = $("input#varSwapCategory").val();
			// call swapCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "SwapCategory", 
			data : {
				category_pos1,
				category_pos2,
				varSwapCategory
			},
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxSwapCategoryInfo").html("");
					$("#ajaxSwapCategoryInfo").append("<b>2 categories are swaped.</b> <br><br>");
				}
				else
				{
					$("#ajaxSwapCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxSwapCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varSwapCategory').attr("disabled",true); //avoid Swap category 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varSwapCategory').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("swap_category_pos1").value = "";
				document.getElementById("swap_category_pos2").value = "";
			}	
		);	
	
});
