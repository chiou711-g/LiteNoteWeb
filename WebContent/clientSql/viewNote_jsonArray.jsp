<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="org.json.JSONObject"%>
<%@ page import="org.json.JSONArray"%>
<%
String connectionURL = "jdbc:mysql://localhost:3306/LiteNoteWeb";
Connection connection = null;
Statement statement = null;
int cmd_flag = 0 ;
%>

<% 
	String sql_cmd ;

	Class.forName("com.mysql.jdbc.Driver").newInstance();
	connection = DriverManager.getConnection(connectionURL, "guest", "1234");
	statement = connection.createStatement();
	sql_cmd = "SELECT * FROM data_table"; 
	String Id;
	String Uri;
	String Title=null;
	final ResultSet rs = statement.executeQuery(sql_cmd);
	JSONArray jsonArray = new JSONArray();
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
%>