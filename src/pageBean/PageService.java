package pageBean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import categoryBean.CategoryService;
import myDB.MyDB;


public class PageService implements Serializable {
	MyDB myDB;
	public PageService() {
		//System.out.println("PageService / _constructor");
		myDB = new MyDB();
	}

	private static final long serialVersionUID = 1L;
	Connection conn;

	public static String clickPageCategoryPos;
	
	public String getClickPageCategoryPos() {
		return clickPageCategoryPos;
	}

	public void setClickPageCategoryPos(String number) {
		PageService.clickPageCategoryPos = number;
	}	
	
	public static String pagePositionNumber;
	
	public String getPagePositionNumber() {
		return pagePositionNumber;
	}

	public void setPagePositionNumber(String number) {
		PageService.pagePositionNumber = number;
	}	
	
	public static String pageTableId;
	
	public String getPageTableId() {
		return pageTableId;
	}

	public void setPageTableId(String id) {
		PageService.pageTableId = id;
	}

	public int pages_count;

	public int getPages_count() {
		int count = 0;
		myDB.DbConnect();
		PreparedStatement prepStmt = null;
		
		// get page table count
		try {
			// query 1
//			prepStmt = myDB.conn.prepareStatement("SELECT data_table_id FROM title_table");
			// query 2
			prepStmt = myDB.conn.prepareStatement("SHOW tables LIKE 'data_table_%'");
			ResultSet rSet = prepStmt.executeQuery();
	
			while (rSet.next()) 
			{
				count++;
			}
			System.out.println("PageService / _getPages_count / pages count: " + count);
			
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		setPages_count(count);
		
		return this.pages_count;
	}

	public void setPages_count(int pagesCount) {
		this.pages_count = pagesCount;
	}

	public int getPageIdByPosition(int pos)
	{
		int pageId = 0;
		
		
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		
		// get data_table_id by position
		try {
			prepStmt = myDB.conn.prepareStatement("SELECT data_table_id FROM title_table");
			ResultSet rSet = prepStmt.executeQuery();
			
			int row_count = 0;
	
			//query PageId by row count
			while (rSet.next()) 
			{
				row_count++;
				if(pos == row_count)
				{
					pageId = rSet.getInt("data_table_id");
					//System.out.println("PageService / _getPageIdByPosition / pageId = " + pageId);
					break;
				}
			} 
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
				
		
		return pageId;
	}
	

	//
	// Insert page table
	//
	public void insertPage(String page_id) throws SQLException
	{
		PreparedStatement prepStmt = null;
		System.out.println("PageService / _insertPage / page_id = " + page_id);
		
		// create page table
		myDB.DbConnect();
		prepStmt = myDB.conn.prepareStatement("CREATE TABLE IF NOT EXISTS data_table_"+ page_id  
				+ "(note_id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT, "
				+ "note_link_uri VARCHAR(1024) NULL DEFAULT NULL, "
				+ "note_image_uri VARCHAR(1024) NULL DEFAULT NULL, "
				+ "note_title VARCHAR(1024) NULL DEFAULT NULL, "
				+ "note_created VARCHAR(120) NULL DEFAULT NULL)");

		int result = prepStmt.executeUpdate();
		System.out.println("PageService / _insertPage / result (page table): " + result);
		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
		
	}
	
	
	//
	// insert page title and page category
	//
	public void insertPage_title_category(int page_num, String page_title, int category_id)
	{
		
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		// create prepare statement
		try {
			prepStmt = myDB.conn.prepareStatement(
					"INSERT INTO title_table (data_table_id,title,category_id) VALUES(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			prepStmt.setInt(1, page_num);
			prepStmt.setString(2, page_title);
			prepStmt.setInt(3, category_id);
			
			prepStmt.executeUpdate();
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return;
	};
	
	//
	// Select
	//
	public PageBean selectPage(String page_id) throws IOException, ClassNotFoundException, SQLException {
		PageBean pageBean = new PageBean();
		
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		
		try {
			// create prepare statement
			prepStmt = myDB.conn.prepareStatement("SELECT data_table_id,title,category_id FROM title_table WHERE data_table_id=?");
	
			prepStmt.setInt(1, Integer.valueOf(page_id));
			ResultSet rs = prepStmt.executeQuery();
			
			String na = "";
	
			if (rs.next()) 
			{
				pageBean.setPage_id(rs.getInt("data_table_id"));
				pageBean.setPage_title(rs.getString("title"));
				pageBean.setPage_category_id(rs.getInt("category_id") );
			} else {
				//
				pageBean.setPage_id(0);
				pageBean.setPage_title(na);
				pageBean.setPage_category_id(0);
			}
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return pageBean;
	}
	
	//
	// Update
	//
	public void updatePage(String page_id, String page_title,String page_category_id) 
			throws ClassNotFoundException, SQLException, IOException {
		
		myDB.DbConnect();
		
		PreparedStatement prepStmt = null;
		// Query MySQL
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("UPDATE title_table SET "
				+ "title=?,category_id=?" + " WHERE data_table_id = ? ");

		prepStmt.setString(1, page_title); //title
		prepStmt.setInt(2, Integer.valueOf(page_category_id)); //category_id
		prepStmt.setInt(3, Integer.valueOf(page_id)); // ID 

		prepStmt.executeUpdate();
		
		prepStmt.clearParameters();
		prepStmt.close();
		
		myDB.DbClose();
	}	
		

	//
	// Update page with new page id
	//
	public void updatePageWithNewPageId(String new_page_id,String old_page_id, String page_title,String page_category_id) 
			throws ClassNotFoundException, SQLException, IOException {
		
		myDB.DbConnect();
		
		PreparedStatement prepStmt = null;
		// Query MySQL
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("UPDATE title_table SET "
				+ "data_table_id=?,title=?,category_id=?" + " WHERE data_table_id = ? ");

		prepStmt.setInt(1, Integer.valueOf(new_page_id));
		prepStmt.setString(2, page_title); //title
		prepStmt.setInt(3, Integer.valueOf(page_category_id)); //category_id
		prepStmt.setInt(4, Integer.valueOf(old_page_id)); // ID 

		prepStmt.executeUpdate();
		
		prepStmt.clearParameters();
		prepStmt.close();
		
		myDB.DbClose();
	}	

	//
	// Delete
	//
	public void deletePage(String page_id) throws SQLException, ClassNotFoundException, IOException {
		
		System.out.println("PageService / _deletePage / page_id = " + page_id);
		PreparedStatement prepStmt = null;
		
		// delete page table
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn
				.prepareStatement("DROP TABLE data_table_" + page_id);
		
		prepStmt.executeUpdate();
		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
		
		// delete page_id
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn
				.prepareStatement("DELETE FROM title_table WHERE data_table_id=?");
		

		prepStmt.setInt(1, Integer.valueOf(page_id));

		int iCount = prepStmt.executeUpdate();
		//System.out.println("iCount=" + iCount);
		
		// print
		if (iCount > 0) {
			System.out.println("Delete a page: OK!");
		} else {
			System.out.println("Delete a page: NG!");
		}

		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
	}	
	
	// get last page id
	public int getLastPageId()
	{
		int total_pages_count = getPages_count();
		int last_page_id = 0;
		
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		
		// get data_table_id by position
		try {
			prepStmt = myDB.conn.prepareStatement("SELECT data_table_id FROM title_table");
			ResultSet rSet = prepStmt.executeQuery();
			
			int row_count = 0;
	
			//query PageId by row count
			while (rSet.next()) 
			{
				row_count++;
				if(total_pages_count == row_count)
				{
					last_page_id = rSet.getInt("data_table_id");
					System.out.println("PageService / _getLastPageId / last_page_id = " + last_page_id);
					break;
				}
			} 
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("PageService / _getLastPageId / last_page_id 2 = " + last_page_id);
		
		return last_page_id;
	}	
	
	//
	// check position boundary
	//
	public boolean isBoundaryOK(String page_pos1,String page_pos2) {
		
		if((page_pos1 == null) || (page_pos2 == null) || page_pos1.contentEquals(page_pos2))
			return false;
		
		// check position boundary
        CategoryService cateService = new CategoryService();
        int cat_id = cateService.getCurrent_category_id();
		int pageId1 = getPageIdByPosition(Integer.valueOf(page_pos1));
		int pageId2 = getPageIdByPosition(Integer.valueOf(page_pos2));

        PageBean pageBean1 = null;
		PageBean pageBean2 = null;
		try {
			pageBean1 = selectPage(String.valueOf(pageId1));
			pageBean2 = selectPage(String.valueOf(pageId2));
		} catch (ClassNotFoundException|SQLException | IOException e) {
			e.printStackTrace();
		}
        
        int pageBean1_cat = pageBean1.getPage_category_id();
        int pageBean2_cat = pageBean2.getPage_category_id();
    	
		boolean boundaryOK =  (cat_id == pageBean1_cat) && 
							  (cat_id == pageBean2_cat) &&
							  (pageBean1_cat == pageBean2_cat);   	
		
		return boundaryOK;
	}
	
	
	//
	// pages are swapped
	//
	public boolean arePagesSwapped(String page_pos1,String page_pos2) {
		boolean areSwapped = false;
		PageBean pageBean1 = null;
		PageBean pageBean2 = null;
		
    	if(page_pos1 != null)
        {

			int page_id1 = getPageIdByPosition(Integer.valueOf(page_pos1));
			int page_id2 = getPageIdByPosition(Integer.valueOf(page_pos2));
    		try {
				pageBean1 = selectPage(String.valueOf(page_id1));
				pageBean2 = selectPage(String.valueOf(page_id2));
			} catch (ClassNotFoundException | SQLException |IOException e) {
				e.printStackTrace();
			}

			int ori_page_id1 = page_id1;
			int ori_page_id2 = page_id2;
        
        	// move data in title table

        	int newLastId = getLastPageId()+1; 
        	
        	// data_table_id
        	// title
        	int tempId = pageBean1.getPage_id();
    		String tempTitle = pageBean1.getPage_title();

    		// new page beans
    		pageBean1.setPage_id(pageBean2.getPage_id());
    		pageBean1.setPage_title(pageBean2.getPage_title());
    		pageBean2.setPage_id(tempId);
    		pageBean2.setPage_title(tempTitle);

    		
	        try {
	        	
	        	// update new page bean 1 with unused Id for avoiding page ID duplication
				updatePageWithNewPageId(String.valueOf(newLastId), 
						String.valueOf(ori_page_id1),
						pageBean1.getPage_title(), 
						String.valueOf(pageBean1.getPage_category_id()));
				
				// update new page bean 2
				updatePageWithNewPageId(String.valueOf(pageBean2.getPage_id()),
						String.valueOf(ori_page_id2), 
						pageBean2.getPage_title(),
						String.valueOf(pageBean2.getPage_category_id()));
				
				// update new page bean 1
				updatePageWithNewPageId(String.valueOf(pageBean1.getPage_id()), 
						String.valueOf(newLastId),
						pageBean1.getPage_title(), 
						String.valueOf(pageBean1.getPage_category_id()));					
				
				areSwapped = true;
			} catch (NumberFormatException | ClassNotFoundException | SQLException |IOException e) {
				e.printStackTrace();
			}

        } 
		return areSwapped;
	}		
	
}
