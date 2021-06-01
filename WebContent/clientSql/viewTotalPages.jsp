<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="org.json.JSONObject"%>
<%@ page import="org.json.JSONArray"%>
<%
	String connectionURL = "jdbc:mysql://localhost:3306/LiteNoteWeb";
	Connection connection = null;
	Statement statement = null;
	int cmd_flag = 0 ;

	String sql_cmd ;

	Class.forName("com.mysql.jdbc.Driver").newInstance();
	connection = DriverManager.getConnection(connectionURL, "guest", "1234");
	statement = connection.createStatement();
	sql_cmd = "SHOW tables LIKE 'data_table_%'";
	int count = statement.executeUpdate(sql_cmd);//TODO ??? should be executeQuery()?
	
	JSONArray jsonArray = new JSONArray();
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("totalPagesCount",count);
	jsonArray.put(jsonObject);
    
    out.print(jsonArray);
    out.flush();
%>



	    
	