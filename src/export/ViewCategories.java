package export;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import categoryBean.CategoryBean;
import categoryBean.CategoryService;

@WebServlet("/export/ViewCategories.do")
public class ViewCategories extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public ViewCategories() {
	    super();
	  	System.out.println("ViewCategory constructor");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ViewCategory / _doPost");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// for js
		PrintWriter out = response.getWriter(); 
		JSONArray categories = new JSONArray();
		
		CategoryService categoryService = new CategoryService();
		CategoryBean categoryBean = new CategoryBean();
		
		int totalCategoriesCount = -1;
		totalCategoriesCount = categoryService.getCategories_count();
		System.out.println("ViewCategory / _doPost / totalCategoriesCount = " + totalCategoriesCount);
		
		// get each category name
		for(int i=1;i<=totalCategoriesCount;i++)
		{
			JSONObject category = new JSONObject();
			categoryService = new CategoryService();
			
			try {
				categoryBean = categoryService.selectCategory(i);
				category.put("category_id", categoryBean.getCategory_id());
				category.put("category_name", categoryBean.getCategory_name());
				categories.put(category);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		JSONObject categoryObj = new JSONObject();
		if (totalCategoriesCount >= 0 ) {
			try {
				categoryObj.put("success", true);
				categoryObj.put("totalCategoriesCount", totalCategoriesCount );
				categoryObj.put("categories", categories);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				categoryObj.put("success", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// out
		out.println(categoryObj.toString());
		out.close();
	}
}
