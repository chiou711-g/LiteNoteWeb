package export;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import myDB.MyDB;
import pageBean.PageBean;
import pageBean.PageService;

@WebServlet("/export/ViewPages.do")
public class ViewPages extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MyDB myDB;
     
	public ViewPages() {
	    super();
	  	//System.out.println("ViewPages constructor");
	    myDB = new MyDB();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ViewPages / _doPost");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String[] category_ids = request.getParameterValues("category_ids[]"); 
		String[] category_positions = request.getParameterValues("category_ids[]"); 
		String currentCategoryPos = request.getParameter("currentCategoryPos"); 
		String currentCategoryId = null;
		String currentCategoryName = null;
		
		for(int i=0;i<category_positions.length;i++)
		{
			// get category_id by position
			try {
				PreparedStatement prepStmt = null;
				myDB.DbConnect();
				prepStmt = myDB.conn.prepareStatement("SELECT category_id,category_name FROM category_table");
				ResultSet rSet = prepStmt.executeQuery();
				
				int row_count = 0;
		
				while (rSet.next()) 
				{
					row_count++;
					if(Integer.valueOf(category_positions[i]) == row_count)
					{
						category_ids[i] = String.valueOf(rSet.getInt("category_id"));
						System.out.println("ViewPages / _doPost / category_ids[" + i +"] = " + category_ids[i]);
					}
					
					if(Integer.valueOf(currentCategoryPos) == row_count)
					{
						currentCategoryId = String.valueOf(rSet.getInt("category_id"));
						System.out.println("ViewPages / _doPost / currentCategoryId = " + currentCategoryId);
						currentCategoryName = rSet.getString("category_name");
						System.out.println("ViewPages / _doPost / currentCategoryName = " + currentCategoryName);
					}
				} 
				prepStmt.clearParameters();
				prepStmt.close();
				myDB.DbClose();					
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		System.out.println("ViewPages / _doPost / currentCategoryId = " + currentCategoryId);
		
		if(category_ids == null)
		{
			System.out.println("ViewPages / _doPost / category_ids = null");
			return;
		}
		
		for(int i=0;i<category_ids.length;i++)
			System.out.println("ViewPages / _doPost / category_id[" + i + "] = " + category_ids[i]);
		
		// for js
		PrintWriter out = response.getWriter(); 

		
		PageService pageService = new PageService();
		PageBean pageBean = new PageBean();
		
		int totalPagesCount = -1;
		totalPagesCount = pageService.getPages_count();
		
		JSONArray pages = new JSONArray();
		
		// get each page title
		for(int i=1;i<=totalPagesCount;i++)
		{
			pageService = new PageService();
			JSONObject page = new JSONObject();
			
			String pageId = String.valueOf(pageService.getPageIdByPosition(i));
			try {
				pageBean = pageService.selectPage(pageId);
				
				System.out.println("ViewPages / _doPost / page i = " + i);
				for(int j=0;j<category_ids.length;j++)
				{
					System.out.println("ViewPages / _doPost / j = " + j);
					if(String.valueOf(pageBean.getPage_category_id()).equalsIgnoreCase(category_ids[j]))
					{
						page.put("title", pageBean.getPage_title());
						System.out.println("ViewPages / _doPost / pageBean.getPage_title() = " + pageBean.getPage_title());
						page.put("category_id", String.valueOf(pageBean.getPage_category_id()));
						System.out.println("ViewPages / _doPost / pageBean.getPage_category_id() = " + pageBean.getPage_category_id());
						page.put("page_id", String.valueOf(pageBean.getPage_id()));
						page.put("category_checked_id", true);
					}
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pages.put(page);
		}
		
		
		JSONObject pageObj = new JSONObject();
		if (totalPagesCount >= 0 ) {
			try {
				pageObj.put("success", true);
				pageObj.put("totalPagesCount", totalPagesCount );
				pageObj.put("pages", pages);
				pageObj.put("currentCategoryId", currentCategoryId);
				pageObj.put("currentCategoryName", currentCategoryName);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				pageObj.put("success", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// out
		out.println(pageObj.toString());
		out.close();
	}
		
}
