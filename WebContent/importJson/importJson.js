
$(document).ready(function() {
	var id;
	
	// for import Json file content
	function readSingleFile(e) {
		var file = e.target.files[0];
		if (!file) {
			return;
		}
		
		var reader = new FileReader();
		reader.readAsText(file);
		reader.onload = function(e) {
			var json_string = e.target.result;
			displayContents(json_string);
		};
	}

	// display Json content
	function displayContents(contents) {
		var element = document.getElementById('json-file-content');
		element.textContent = contents;
	}

	document.getElementById('file-input').addEventListener('change', readSingleFile, false);		
});

//import Json content
function importJsonContent()
{
	var import_json_str = document.getElementById('json-file-content').textContent;
	// call importJson HttpServlet
	$.ajax({
	type : "POST",
	url : "ImportJson", 
	data : {import_json_str},
	dataType : "json",
	success : function(importJson,textStatus, jqXHR) {
		
		if (importJson.success)
		{
			// clear interval
			clearInterval(id);
			// show 100% progress bar
			document.getElementById("progressBar").style.width = '100%'; 

			document.getElementById('json-file-content').textContent = "Import JSON file content is OK :)"
		}
		else
		{
			clearInterval(id);
			document.getElementById("progressBar").style.width = 0 + '%'; 

			document.getElementById('json-file-content').textContent = "Import JSON file content is NG :("
		}
	},
	error : function(jqXHR,textStatus,errorThrown)
	{
		console.log("Something really bad happened " + textStatus);
	},
	beforeSend : function(jqXHR, settings) {
		settings.data += "&dummyData=whatever";
	},
	complete : function(jqXHR,textStatus) {
	}
	});	
}

function showBar() {
	var elem = document.getElementById("progressBar");   
	
	// initial
	var width = 1;
	id = setInterval(frame, 1000);
	
	function frame() {
		if (width >= 50) {
			width = 1;
			elem.style.width = width + '%';
		} else {
			width++; 
			elem.style.width = width + '%'; 
		}			
	}

}

