package pageBean;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import categoryBean.CategoryBean;
import categoryBean.CategoryService;
import pageBean.PageBean;
import pageBean.PageService;

public class ListPages extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public ListPages() {
	    super();
	  	System.out.println("ListPages constructor");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		System.out.println("ListPages / _doPost");
		
		// for JavaScript
		PrintWriter out = response.getWriter();
		// pages
		JSONArray pages = new JSONArray();
		
		PageService pageService = new PageService();
		PageBean pageBean = new PageBean();
		
		int totalPagesCount = -1;
		totalPagesCount = pageService.getPages_count();
		
		// get each page: 
		// - data_title_id
		// - titleViewPage 
		for(int i=1;i<=totalPagesCount;i++)
		{
			pageService = new PageService();
			
			int pageTableId = pageService.getPageIdByPosition(i);
			try {
				pageBean = pageService.selectPage(String.valueOf(pageTableId));
				
				JSONObject pageObj = new JSONObject();
				pageObj.put("data_table_id", pageTableId);
				pageObj.put("page_title", pageBean.getPage_title());
				pages.put(pageObj);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JSONObject pageOut = new JSONObject();
		if (totalPagesCount >= 0 ) {
			try {
				pageOut.put("success", true);
				pageOut.put("totalPagesCount", totalPagesCount );
				// all pages
				pageOut.put("pages", pages);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				pageOut.put("success", false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// out
		out.println(pageOut.toString());
//		System.out.println("ViewPage / pageOut = " + pageOut.toString());
		out.close();
	}
}
