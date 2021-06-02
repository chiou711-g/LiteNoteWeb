package view;

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
import pageBean.PageBean;
import pageBean.PageService;

@WebServlet("/dataMgr/view/ViewPage.do")
public class ViewPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public ViewPage() {
	    super();
	  	System.out.println("ViewPage constructor");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String page_pos_number = request.getParameter("page_pos_number"); 
		System.out.println("ViewPage / _doPost / page_pos_number = " + page_pos_number);

		// for JavaScript
		PrintWriter out = response.getWriter();
		// pages
		JSONArray pages = new JSONArray();
		
		// categories
		JSONArray cates = new JSONArray();
		
		CategoryService cateService = new CategoryService();
		int count = cateService.getCategories_count();
		
		for(int i=1;i<=count;i++)
		{
			CategoryBean cateBean;
			try {
				cateBean = cateService.selectCategory(i);
				JSONObject cate = new JSONObject();
				cate.put("category_id", String.valueOf(cateBean.getCategory_id()));
				cate.put("category_name", cateBean.getCategory_name());
				cates.put(cate);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PageService pageService = new PageService();
		PageBean pageBean = new PageBean();
		
		int totalPagesCount = -1;
		totalPagesCount = pageService.getPages_count();
		
		// get each page: 
		// - data_title_id
		// - titleViewPage 
		// - category_id
		for(int i=1;i<=totalPagesCount;i++)
		{
			pageService = new PageService();
			
			int pageTableId = pageService.getPageIdByPosition(i);
			try {
				pageBean = pageService.selectPage(String.valueOf(pageTableId));
				
				JSONObject pageObj = new JSONObject();
				pageObj.put("data_table_id", pageTableId);
				pageObj.put("title", pageBean.getPage_title());
				pageObj.put("category_id", pageBean.getPage_category_id());
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
		
		// get current page title
		if((page_pos_number!= null) && (!page_pos_number.isEmpty()) )
		{
			pageService = new PageService();

			int pageId = pageService.getPageIdByPosition(Integer.valueOf(page_pos_number));
			
			try {
				pageBean = pageService.selectPage(String.valueOf(pageId));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		JSONObject pageOut = new JSONObject();
		if (totalPagesCount >= 0 ) {
			try {
				pageOut.put("success", true);
				pageOut.put("totalPagesCount", totalPagesCount );
				pageOut.put("page_title", pageBean.getPage_title() );
				pageOut.put("page_category_id", pageBean.getPage_category_id() );
				// all pages
				pageOut.put("pages", pages);
				// all categories
				pageOut.put("categories", cates);
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
