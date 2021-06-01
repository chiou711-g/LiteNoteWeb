package addNew;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pageBean.PageBean;
import pageBean.PageService;

//@WebServlet("/addNew/AddPage.do")
public class AddPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddPage() {
        super();
    	System.out.println("AddPage constructor");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("AddPage / _doPost");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PageBean pageBn = new PageBean();
		PageService pageSrvc = new PageService();

		String addPageNum = String.valueOf(pageSrvc.getLastPageId() + 1);
		String page_title =  request.getParameter("add_page_title");
		int page_category_id = Integer.valueOf(request.getParameter("addPageCategoryId"));

		pageBn.setPage_id(Integer.valueOf(addPageNum) );
		System.out.println("AddPage / _doPost / new add_page_number= " + addPageNum);
		pageBn.setPage_title(page_title);
		pageBn.setPage_category_id(page_category_id);
		
		// add new page
		try {
			pageSrvc.insertPage(addPageNum);//??? return -1
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// insert page title and category
		pageSrvc.insertPage_title_category(Integer.valueOf(addPageNum),page_title,page_category_id);
		
		
		// get pages count
		int totalPagesCount = 0;
		totalPagesCount = pageSrvc.getPages_count();

		
		// for js
		PrintWriter out = response.getWriter(); //CW: set print writer
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();

		
		if (Integer.valueOf(addPageNum) > 0 ) {
			jsonObj.addProperty("success", true);
			jsonObj.addProperty("totalPagesCount", totalPagesCount );
		} else {
			jsonObj.addProperty("success", false);
		}

		JsonElement sBnJsonElement = gson.toJsonTree(pageBn);
		jsonObj.add("pageInfo", sBnJsonElement);

		out.println(jsonObj.toString());
		out.close();
	}
}
