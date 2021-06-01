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

import noteBean.NoteBean;
import noteBean.NoteService;
import pageBean.PageService;

//@WebServlet("/addNew/AddPlaylist.do")
public class AddPlaylist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddPlaylist() {
        super();
    	System.out.println("AddPlaylist constructor");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("AddPlaylist / _doPost");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");	

		NoteBean noteBean = new NoteBean();
		NoteService noteBeanService = new NoteService();

		//video id
		String videoId = request.getParameter("video_id");
		System.out.println("AddPlaylist / _doPost / videoId = " + videoId);
		
		// video link
		String link = "https://www.youtube.com/watch?v=" + videoId;
		noteBean.setNote_link_uri(link);

		//video title
		String videoTitle = request.getParameter("video_title");
		noteBean.setNote_title(videoTitle);
		System.out.println("AddPlaylist / _doPost / videoTitle = " + videoTitle);
		

//		String note_image_uri = request.getParameter("noteImageUri").replaceAll("\\s","");
//        if(note_image_uri.isEmpty())
//        	note_image_uri = null;
//		noteBean.setNote_image_uri(note_image_uri);
		
//		String noteTitle = request.getParameter("noteTitle");
//		System.out.println("AddNote / _doPost / noteTitle = " + noteTitle);
//		noteBean.setNote_title(noteTitle);
		
		
		//table number
		String pageNumber = request.getParameter("page_id");
		System.out.println("AddNote / _doPost / pageNumber = " + pageNumber);
		PageService pageService = new PageService();
		pageService.setPageTableId(pageNumber); 

		int addRowCount = 0;
		try 
		{
//			addRowCount = noteBeanService.insertNote(pageNumber,noteBean.getNote_link_uri(),noteBean.getNote_image_uri(),noteBean.getNote_title() );
			addRowCount = noteBeanService.insertNote(pageNumber,noteBean.getNote_link_uri(),null,noteBean.getNote_title() );
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		int totalRowsCount = 0;
		try {
			totalRowsCount = noteBeanService.getRowsCount(pageNumber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// for js
		PrintWriter out = response.getWriter(); //CW: set print writer
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();

		
		if (addRowCount > 0 ) {
			jsonObj.addProperty("success", true);
			jsonObj.addProperty("totalRowsCount", totalRowsCount );
		} else {
			jsonObj.addProperty("success", false);
		}

		JsonElement sBnJsonElement = gson.toJsonTree(noteBean);
		jsonObj.add("noteInfo", sBnJsonElement);

		out.println(jsonObj.toString());
		out.close();
	}
}
