package categoryBean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import myDB.MyDB;
import pageBean.PageBean;
import pageBean.PageService;

public class CategoryService implements Serializable {
	MyDB mDB;

	public CategoryService() {
		mDB = new MyDB();
	}

	private static final long serialVersionUID = 1L;
	public static int current_category_id;
	public static int current_category_pos;

	public int getCurrent_category_id() {
		return current_category_id;
	}

	public void setCurrent_category_id(int current_category_id) {
		CategoryService.current_category_id = current_category_id;
	}
	
	public int getCurrent_category_pos() {
		return current_category_pos;
	}

	public void setCurrent_category_pos(int current_category_pos) {
		CategoryService.current_category_pos = current_category_pos;
	}

	public int categories_count;

	public int getCategories_count() {
		PreparedStatement prepStmt = null;
		int count = 0;
		
		if(mDB == null)
			mDB = new MyDB();
		
		// get categories count
		try {
			mDB.DbConnect();
			prepStmt = mDB.conn.prepareStatement("SELECT COUNT(*) FROM category_table");
			ResultSet rs = prepStmt.executeQuery();// .executeUpdate();
			while(rs.next()){
		        count = rs.getInt("count(*)");
		    }
			//System.out.println("CategoryService / _getCategories_count / Categories count: " + count);
			prepStmt.clearParameters();
			prepStmt.close();
			mDB.DbClose();		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		setCategories_count(count);
		
		return this.categories_count;
	}

	public void setCategories_count(int categoriesCount) {
		this.categories_count = categoriesCount;
	}

	//
	// Select
	//
	public CategoryBean selectCategory(int position) throws IOException, ClassNotFoundException, SQLException {
		CategoryBean categoryBean = new CategoryBean();
//		System.out.println("CategoryService / _selectCategory / position = " + position);
		
		int category_id = 0;
		PreparedStatement prepStmt = null;
		mDB.DbConnect();
		
		// get category_id by position
		prepStmt = mDB.conn.prepareStatement("SELECT category_id FROM category_table");
		ResultSet rSet = prepStmt.executeQuery();
		
		int row_count = 0;

		while (rSet.next()) 
		{
			row_count++;
			if(position == row_count)
			{
				category_id = rSet.getInt("category_id");
//				System.out.println("CategoryService / _selectCategory / category_id = " + category_id);
				break;
			}
		} 
		prepStmt.clearParameters();
		prepStmt.close();
		mDB.DbClose();
		
		
		mDB.DbConnect();
		try {
			// create prepare statement
			prepStmt = mDB.conn.prepareStatement("SELECT category_id,category_name FROM category_table WHERE category_id=?");
	
			prepStmt.setInt(1, category_id);
			ResultSet rs = prepStmt.executeQuery();
			
			String na = "";
	
			if (rs.next()) 
			{
				categoryBean.setCategory_id(rs.getInt("category_id"));
				categoryBean.setCategory_name(rs.getString("category_name"));
			} else {
				//
				categoryBean.setCategory_id(0);
				categoryBean.setCategory_name(na);
			}
			prepStmt.clearParameters();
			prepStmt.close();
			mDB.DbClose();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return categoryBean;
	}	
	
	
	
	//
	// Insert category table
	//
	public void insertCategory(String category_id,String category_name) throws SQLException
	{
		PreparedStatement prepStmt = null;
		
		// create category table
		prepStmt = null;
		mDB.DbConnect();
		prepStmt = mDB.conn.prepareStatement("CREATE TABLE IF NOT EXISTS category_table"  
				+ "(category_id INT(10) UNSIGNED NOT NULL, "
				+ "category_name VARCHAR(50) NULL DEFAULT NULL)");

		int result = prepStmt.executeUpdate();
		System.out.println("result (category_table): " + result);
		prepStmt.clearParameters();
		prepStmt.close();
		mDB.DbClose();		
		
		
		// insert new row
		mDB.DbConnect();
		try {
			prepStmt = mDB.conn.prepareStatement(
					"INSERT INTO category_table (category_id,category_name) VALUES(?,?)",
					Statement.RETURN_GENERATED_KEYS);
			prepStmt.setInt(1, Integer.valueOf(category_id));
			prepStmt.setString(2, category_name);
			
			prepStmt.executeUpdate();
			prepStmt.clearParameters();
			prepStmt.close();
			mDB.DbClose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return;		
	}
	
	public void updateCategory(String category_id, String category_name) {
		mDB.DbConnect();
		
		PreparedStatement prepStmt = null;
		// Query MySQL
		// create prepare statement
		try {
			prepStmt = mDB.conn.prepareStatement("UPDATE category_table SET "
												+ "category_name=?" + " WHERE category_id = ? ");
			prepStmt.setString(1, category_name); //name
			prepStmt.setInt(2, Integer.valueOf(category_id)); //category_id

			prepStmt.executeUpdate();
			
			prepStmt.clearParameters();
			prepStmt.close();
			
			mDB.DbClose();				
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}	

	public void deleteCategory(String category_id) throws SQLException {
		System.out.println("CategoryService / _deleteCategory / category_id = " + category_id);
		PreparedStatement prepStmt = null;
		
		// delete category_id
		mDB.DbConnect();
		// create prepare statement
		prepStmt = mDB.conn.prepareStatement("DELETE FROM category_table WHERE category_id=?");

		prepStmt.setInt(1, Integer.valueOf(category_id));

		int iCount = prepStmt.executeUpdate();

		System.out.println("iCount=" + iCount);
		
		// print
		if (iCount > 0) {
			System.out.println("Delete a category: OK!");
		} else {
			System.out.println("Delete a category: NG!");
		}

		prepStmt.clearParameters();
		prepStmt.close();
		mDB.DbClose();
	}
	
	public int getCategories_last_id() throws SQLException {
		int last_category_id = 0;
		PreparedStatement prepStmt = null;
		mDB.DbConnect();
		
		// get category_id by position
		prepStmt = mDB.conn.prepareStatement("SELECT category_id FROM category_table");
		ResultSet rSet = prepStmt.executeQuery();
		
		int total = getCategories_count();
		int row_count = 0;

		while (rSet.next()) 
		{
			row_count++;
			if(total == row_count)
			{
				last_category_id = rSet.getInt("category_id");
				System.out.println("CategoryService / _getCategories_last_id / last_category_id = " + last_category_id);
			}
		} 
		prepStmt.clearParameters();
		prepStmt.close();
		mDB.DbClose();
		
		return last_category_id;
	}
	
	//
	// check position boundary
	//
	public boolean isBoundaryOK(String category_pos1,String category_pos2) {
		
		if((category_pos1 == null) || (category_pos2 == null) || category_pos1.contentEquals(category_pos2))
			return false;
		
        int from = Integer.valueOf(category_pos2);
        int to = Integer.valueOf(category_pos1);
        
		int catsCount = 0;
		try {
    		catsCount = getCategories_count();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		boolean boundaryOK =  (from <= catsCount) && (from > 0) &&
							  (to <= catsCount) && (to > 0) ;   
		return boundaryOK;
	}
	
	//
	// Categories are swapped
	//
	public boolean areCategoriesSwapped(String category_pos1,String category_pos2) {
		boolean areSwapped = false;
		
		CategoryBean categoryBean1 = null;
		CategoryBean categoryBean2 = null;
    	if(category_pos1 != null)
        {
    		try {
				categoryBean1 = selectCategory(Integer.valueOf(category_pos1));
				categoryBean2 = selectCategory(Integer.valueOf(category_pos2));
			} catch (ClassNotFoundException|SQLException |NumberFormatException|IOException e) {
				e.printStackTrace();
			}
			String category_id1 = String.valueOf(categoryBean1.getCategory_id());
			String category_name1 = String.valueOf(categoryBean1.getCategory_name());
			String category_id2 = String.valueOf(categoryBean2.getCategory_id());
			String category_name2 = String.valueOf(categoryBean2.getCategory_name());

			int ori_category_id1 = Integer.valueOf(category_id1);
			int ori_category_id2 = Integer.valueOf(category_id2);
        
        	// move data in category table
        	String tempStr = category_name1;
    		categoryBean1.setCategory_name(category_name2);
    		categoryBean2.setCategory_name(tempStr);
	        try {
				updateCategory(category_id1, categoryBean1.getCategory_name());
				updateCategory(category_id2, categoryBean2.getCategory_name());
				areSwapped = true;
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
	        
        	// move data in page title table
	        PageService pageService = new PageService();
	        int pageCount = pageService.getPages_count();
	        
	        for(int pos=1;pos<=pageCount;pos++)
	        {
	        	int page_id = 0;
				try {
					page_id = pageService.getPageIdByPosition(pos);
					PageBean pageBean = pageService.selectPage(String.valueOf(page_id));
		        	String page_title = pageBean.getPage_title();
					
					// move category Id
					if(pageBean.getPage_category_id() == Integer.valueOf(ori_category_id1))
						pageBean.setPage_category_id(Integer.valueOf(ori_category_id2));
					else if(pageBean.getPage_category_id() == Integer.valueOf(ori_category_id2))
						pageBean.setPage_category_id(Integer.valueOf(ori_category_id1));
					
					pageService.updatePage(String.valueOf(page_id), page_title, String.valueOf(pageBean.getPage_category_id()));
					
				} catch (ClassNotFoundException | SQLException | IOException e) {
					e.printStackTrace();
				}
	        }
        } 
		return areSwapped;
	}

}
