package view;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noteBean.NoteBean;
import noteBean.NoteService;
import pageBean.PageService;

@WebServlet("/dataMgr/view/ViewNote.do")
public class ViewNote extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=utf-8");
		
		//System.out.println("ViewNote / _doPost / req.getParameter(\"varReadAll\") = " + req.getParameter("varReadAll"));
		//System.out.println("ViewNote / _doPost / req.getParameter(\"varReadOne\") = " + req.getParameter("varReadOne"));

		PageService pageService= new PageService();
		
		//set click category position
		String click_page_category_pos = req.getParameter("clickPageCategoryPos");
		System.out.println("ViewNote / _doPost / click_page_category_pos = " + click_page_category_pos);
		pageService.setClickPageCategoryPos(click_page_category_pos);
		
		// set page position number
		String page_pos_number = req.getParameter("pagePosNumber");
		pageService.setPagePositionNumber(page_pos_number);
		System.out.println("ViewNote / _doPost / page_pos_number = " + page_pos_number);

		// set page table id
		int page_table_id = pageService.getPageIdByPosition(Integer.valueOf(page_pos_number));
		pageService.setPageTableId(String.valueOf(page_table_id));
		
		NoteService noteBeanService = new NoteService(String.valueOf(page_table_id));
		NoteBean noteBean = new NoteBean();
		
		String note_id = req.getParameter("note_id");
		if ((null != req.getParameter("varReadOne")) &&
			(req.getParameter("varReadOne").equals("讀取單項")) ) 
		{
			if((null == note_id) || note_id.isEmpty())
					return;
			try {
				noteBeanService.selectNote(String.valueOf(page_table_id),note_id);
				noteBean.setReadBy("byId");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		}
		else if ((null != req.getParameter("varReadAll")) &&
				(req.getParameter("varReadAll").equals("讀取全頁")) ) 
		{
			try {
				noteBeanService.selectNoteBeanList();
				noteBean.setReadBy("byAll");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		else if((null == req.getParameter("varReadAll")) && 
				(null == req.getParameter("varReadOne"))    )
		{
			try {
				noteBeanService.selectNoteBeanList();
				noteBean.setReadBy("byAll");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}			
		}
		
		getServletContext().getRequestDispatcher("/dataMgr/view/viewAll.jsp").forward(req, resp);
	}
}
