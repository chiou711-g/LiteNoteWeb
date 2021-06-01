package addNew;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import categoryBean.CategoryBean;
import categoryBean.CategoryService;

@WebServlet("/dataMgr/addNew/AddCategory.do")
public class AddCategory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddCategory() {
        super();
    	System.out.println("AddCategory constructor");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("AddCategory / _doPost");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		CategoryBean categoryBn = new CategoryBean();
		CategoryService categorySrvc = new CategoryService();

		String addCategoryId = null;
		try {
			addCategoryId = String.valueOf(categorySrvc.getCategories_last_id()+1);
			System.out.println("new addCategoryId = " + addCategoryId);
			categoryBn.setCategory_id(Integer.valueOf(addCategoryId) );
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String addCategoryName = request.getParameter("addCategoryName");
		System.out.println("new addCategoryName = " + addCategoryName);

		categoryBn.setCategory_name(addCategoryName);
		
		// insert new category row
		try {
			categorySrvc.insertCategory(addCategoryId,addCategoryName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// get categories count
		int totalCategoriesCount = 0;
		totalCategoriesCount = categorySrvc.getCategories_count();

		
		// for js
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();

		
		if (Integer.valueOf(addCategoryId) > 0 ) {
			jsonObj.addProperty("success", true);
			jsonObj.addProperty("totalCategoriesCount", totalCategoriesCount );
		} else {
			jsonObj.addProperty("success", false);
		}

		JsonElement sBnJsonElement = gson.toJsonTree(categoryBn);
		jsonObj.add("categoryInfo", sBnJsonElement);

		out.println(jsonObj.toString());
		out.close();
	}
}
