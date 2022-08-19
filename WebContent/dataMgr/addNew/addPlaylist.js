
	function authenticate() {
		return gapi.auth2.getAuthInstance()
        	.signIn({scope: "https://www.googleapis.com/auth/youtube.readonly"})
        	.then(function() { console.log("Sign-in successful"); },
              function(err) { console.error("Error signing in", err); });
	}
  
	function loadClient() {
		//	    gapi.client.setApiKey("YOUR_API_KEY");
			var varApiKey = apiAuth.keyStr;
	    	gapi.client.setApiKey(varApiKey);
	    
	    	return gapi.client.load("https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest")
        			  .then(function() { console.log("GAPI client loaded for API"); },
              				function(err) { console.error("Error loading GAPI client for API", err); });
	}
  
    // called when load web page 
	gapi.load("client:auth2", function() {
		//	    gapi.auth2.init({client_id: "YOUR_CLIENT_ID"});
	     gapi.auth2.init({client_id: apiAuth.client_id});
	});
	
	
	// for GIS
	var client;
	var access_token;
	
	// for GIS
	function initClient() {
	  client = google.accounts.oauth2.initTokenClient({
	    client_id: apiAuth.client_id,
	    scope: 'https://www.googleapis.com/auth/youtube.readonly',
	    callback: (tokenResponse) => {
	    access_token = tokenResponse.access_token;
	      loadClient();
	      alert('--- Access Token --- \n' + '--- Load client ---');
	    },
	  });
	
	}
	
	// for GIS  
	function getToken() {
	  client.requestAccessToken();
	}
	
  
	function execute(nextPageToken) {
	    var playlist_id = document.getElementById("playlist_id").value;
	    console.log("execute / playlist_id = ", playlist_id);
	    return gapi.client.youtube.playlistItems.list({
		  "part": "snippet",
	      "maxResults": 25,
	      "pageToken":nextPageToken,
		  "playlistId": playlist_id
	    })
	    .then(function(response) {
              // Handle the results here (response.result has the parsed body).
              console.log("execute / Response", response);
              
              var page_id = document.getElementById('page_id').value;
              var items = response.result.items;

           	  for(i=0;i<items.length;i++)
              {
            	var number = items[i].snippet.position+1;
            	var video_title = items[i].snippet.title;
              	var video_id = items[i].snippet.resourceId.videoId;

              	console.log( "execute / No.", number + " / ", video_title);
              	console.log( "        / Uri:", "https://www.youtube.com/watch?v=" + video_id);

              	doAjax(page_id,video_id,video_title);
              }
              
              var nextPageToken = response.result.nextPageToken;
              console.log( "execute /nextPageToken = ", nextPageToken);
              if((nextPageToken != null) && (nextPageToken.length>0) && (nextPageToken != "undefined"))
            	  execute(nextPageToken);
          	
            },
            function(err) { console.error("execute / Execute error", err); });
	}  
  
var idArray;
var count;

$(document).ready(function() {
  	
	count = 0;
	list_pages();
	
	$("#addPlaylistForm").submit(function(e) {
		e.preventDefault();
		});
	$("#addPlaylistBtn").click(function(e) {
		formCheck(addPlaylistForm);
		function formCheck(inFm){
			str="";
			if(inFm.playlist_id.value.length<13)
			{
				str+="- Playlist ID不完整\n";
			}
			
			if(inFm.page_id.value.length<1)
			{
				str+="- PAGE ID不完整\n";
			}			
			
			if(str.length>0)
			{
				alert(str);
				inFm.playlist_id.focus();
				return false;
			}
			else
			{
				execute(null)
			}
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

//cf: https://stackoverflow.com/questions/23754973/submit-ajax-request-one-by-one-with-each
function doAjax(page_id,video_id,video_title){
	
	// call addPlaylist HttpServlet
	$.ajax({
	type : "POST",
	url : "AddPlaylist", 
	data : {page_id,video_id,video_title},
	async : false,
	dataType : "json",
	success : function(showPlaylist,textStatus, jqXHR) {
		if (showPlaylist.success)
		{
			$("#ajaxAddPlaylistInfo").html("");
			$("#ajaxAddPlaylistInfo").append("<b>Count:</b> "
					+ showPlaylist.totalRowsCount + "<br/>");
		}
		else
		{
			$("#ajaxAddPlaylistInfo").html("<div><b>Input is Invalid!</b></div>");
		}
	},
	error : function(jqXHR,textStatus,errorThrown)
	{
		console.log("Something really bad happened "
				+ textStatus);
		$("#ajaxAddPlaylistInfo").html(jqXHR.responseText);
	},
	beforeSend : function(jqXHR, settings) {
		settings.data += "&dummyData=whatever";
		$('#addPlaylistBtn').attr("disabled",true); //avoid Add data 2 次
	},
	complete : function(jqXHR,textStatus) {
		$('#addPlaylistBtn').attr("disabled",false); //可按button
	}
	});
}	
