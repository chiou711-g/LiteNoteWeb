$(document).ready(function() {
	
	$("#move_category_form").submit(function(e) {
		e.preventDefault();
		});
	
	$("#varMoveCategory").click(function(e) {
		formCheck(moveCategoryForm);
		function formCheck(inFm){
			str="";

			if(inFm.move_category_pos1.value.length<1)
			{
				str+="- position 1 不完整\n";
			}

			if(inFm.move_category_pos2.value.length<1)
			{
				str+="- position 2 不完整\n";
			}
			
			if(str.length>0)
			{
				alert(str);
				inFm.move_category_pos.focus();
				return false;
			}
			else
			{
				doMoveAjax();
			}
		}
	
		// move ajax
		function doMoveAjax(){
			var category_pos1 = $("input#move_category_pos1").val();
			var category_pos2 = $("input#move_category_pos2").val();
			var varMoveCategory = $("input#varMoveCategory").val();
			// call moveCategory HttpServlet
			$.ajax({
			type : "POST",
			url : "MoveCategory", 
			data : {
				category_pos1,
				category_pos2,
				varMoveCategory
			},
			dataType : "json",
			success : function(showCategory,textStatus, jqXHR) {
				if (showCategory.success)
				{
					$("#ajaxMoveCategoryInfo").html("");
					$("#ajaxMoveCategoryInfo").append("<b>Categories are moved.</b> <br><br>");
				}
				else
				{
					$("#ajaxMoveCategoryInfo").html("<div><b>Input is Invalid!</b></div>");
				}
			},
			error : function(jqXHR,textStatus,errorThrown)
			{
				console.log("Something really bad happened "
						+ textStatus);
				$("#ajaxMoveCategoryInfo").html(jqXHR.responseText);
			},
			beforeSend : function(jqXHR, settings) {
				settings.data += "&dummyData=whatever";
				$('#varMoveCategory').attr("disabled",true); //avoid Move category 2 次
			},
			complete : function(jqXHR,textStatus) {
				$('#varMoveCategory').attr("disabled",false); //可按button
			} 
			});
		}
	});
	
	// clean data
	$("#clean_data").click(
			function() {
				document.getElementById("move_category_pos1").value = "";
				document.getElementById("move_category_pos2").value = "";
			}	
		);	
	
});
