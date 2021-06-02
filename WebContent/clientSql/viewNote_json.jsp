<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="org.json.JSONArray"%>

<%
String connectionURL = "jdbc:mysql://localhost:3306/LiteNoteWeb";
Connection connection = null;
Statement statement = null;
%>

<% 
	String sql_cmd ;
	String id; 
	id=request.getParameter("note_id"); 
	String pageNumber = request.getParameter("PageNumber");
	System.out.println("pageNumber = " + pageNumber);
	
	System.out.println("id = " + id);
	
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	connection = DriverManager.getConnection(connectionURL, "guest", "1234");
	statement = connection.createStatement();
	
	// select by Id
	if((id != null) && (id.length() > 0) )
	{
		sql_cmd = "SELECT note_id,note_link_uri,note_title FROM data_table_"+ pageNumber +" WHERE note_id="+id+";" ; 
		String Id;
		String Uri;
		String Title=null;
		final ResultSet rs = statement.executeQuery(sql_cmd);
		JSONObject json = new JSONObject();
		
		//Get one note
		while(rs.next()){
			Id = rs.getString(1);
			Uri = rs.getString(2);
	 		Title = rs.getString(3);
			
		    json.put("note_id", Id);
		    json.put("note_link_uri", Uri);
		    json.put("note_title", Title);
		}
		
	    out.print(json);
	    out.flush();
	}
	else //select all
	{
		if(pageNumber == null)
			sql_cmd = "SELECT * FROM data_table";
		else
			sql_cmd = "SELECT * FROM data_table_" + pageNumber; 
		String Id;
		String Uri;
		String Title=null;
		final ResultSet rs = statement.executeQuery(sql_cmd);
		int idx = 0;
		JSONArray jsonArray = new JSONArray();
		
		// get all notes
		while(rs.next()){
			JSONObject jsonObject = new JSONObject();
			Id = rs.getString(1);
			Uri = rs.getString(2);
	 		Title = rs.getString(3);
	 		jsonObject.put("note_id",Id);
	 		jsonObject.put("note_link_uri",Uri);
	 		jsonObject.put("note_title",Title);
	 		jsonArray.put(jsonObject);
		}
	    
	    out.print(jsonArray);
	    out.flush();		
	}
%>
