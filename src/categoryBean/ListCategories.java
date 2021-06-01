package categoryBean;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


import categoryBean.CategoryBean;
import categoryBean.CategoryService;

//@WebServlet("/dataMgr/addNew/ListCategories.do")
public class ListCategories extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public ListCategories() {
	    super();
	  	System.out.println("categoryBean / ListCategories / constructor");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("categoryBean / ListCategories / _doPost");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// for Java Script
		PrintWriter out = response.getWriter(); 
		JSONArray categories = new JSONArray();
		
		CategoryService categoryService = new CategoryService();
		
		// keep category position of current selection 
		CategoryBean categoryBean = new CategoryBean();
		
		int totalCategoriesCount = -1;
		totalCategoriesCount = categoryService.getCategories_count();
		System.out.println("categoryBean / ListCategories / _doPost / totalCategoriesCount = " + totalCategoriesCount);
		
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
			}
		}
		
		


		JSONObject categoryObj = new JSONObject();
		if (totalCategoriesCount >= 0 ) {
			categoryObj.put("success", true);
			categoryObj.put("totalCategoriesCount", totalCategoriesCount );
			categoryObj.put("category_id", categoryBean.getCategory_id() );
			categoryObj.put("category_name", categoryBean.getCategory_name() );
			categoryObj.put("categories",categories);
		} else {
			categoryObj.put("success", false);
		}
		
		// out
		out.println(categoryObj.toString());
		out.close();
	}
}
