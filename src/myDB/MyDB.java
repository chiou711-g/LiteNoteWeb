package myDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MyDB {
	private static DataSource dataSource;
	public Connection conn;
	
	public MyDB()
	{
		//System.out.println("MyDB / constructor");
		PreparedStatement prepStmt = null;
		
		// check if category table existed
		try {
			// create category table
			prepStmt = null;
			DbConnect();
			prepStmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS category_table"  
												+ "(category_id INT(10) UNSIGNED NOT NULL, "
												+ "category_name VARCHAR(50) NULL DEFAULT NULL)");
			prepStmt.executeUpdate();
			prepStmt.clearParameters();
			prepStmt.close();
			DbClose();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		// check if title table existed
		try {
			// create title_table if not exists
			prepStmt = null;
			DbConnect();
			prepStmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS title_table"  
					+ "(data_table_id INT(10) UNSIGNED NOT NULL, "
					+ "title VARCHAR(100) NULL DEFAULT NULL, "
					+ "category_id INT(10) UNSIGNED NOT NULL)");

			prepStmt.executeUpdate();
			prepStmt.clearParameters();
			prepStmt.close();
			DbClose();		
		}
		catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	//
	// Get connection
	//
	public void DbConnect() {
		//System.out.println("MyDB / _DbConnect");
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/LiteNoteWeb_2023");
			conn = dataSource.getConnection();
		} catch (NamingException ex) {
			throw new RuntimeException(ex);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//
	// Close connection
	//
	public void DbClose() throws SQLException {
		//System.out.println("MyDB / _DbClose");
		conn.close();
	}

}
