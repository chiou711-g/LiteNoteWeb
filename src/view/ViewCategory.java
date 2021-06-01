package view;

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

@WebServlet("/dataMgr/view/ViewCategory.do")
public class ViewCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public ViewCategory() {
	    super();
	  	System.out.println("ViewCategory / constructor");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("ViewCategory / _doPost");
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String category_pos = request.getParameter("category_pos"); 
		System.out.println("ViewCategory / _doPost / category_pos = " + category_pos);
		
		// none input case
		if(category_pos == null)
			return;
		
		// for Java Script
		PrintWriter out = response.getWriter(); 
		JSONArray categories = new JSONArray();
		
		CategoryService categoryService = new CategoryService();
		
		// keep category position of current selection 
		categoryService.setCurrent_category_pos(Integer.valueOf(category_pos));
		CategoryBean categoryBean = new CategoryBean();
		
		int totalCategoriesCount = -1;
		totalCategoriesCount = categoryService.getCategories_count();
		System.out.println("ViewCategory / _doPost / totalCategoriesCount = " + totalCategoriesCount);
		
		for(int i=1;i<=totalCategoriesCount;i++)
		{
			JSONObject category = new JSONObject();
			categoryService = new CategoryService();
			
			
			try {
				categoryBean = categoryService.selectCategory(i);
				category.put("category_id", categoryBean.getCategory_id());
				category.put("category_name", categoryBean.getCategory_name());
				categories.put(category);

				if(i==Integer.valueOf(category_pos))
					categoryService.setCurrent_category_id(categoryBean.getCategory_id());
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		// get current category title
		if(!category_pos.isEmpty())
		{
			categoryService = new CategoryService();
			try {
				categoryBean = categoryService.selectCategory(Integer.valueOf(category_pos));
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
			categoryObj.put("category_pos", category_pos );
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
