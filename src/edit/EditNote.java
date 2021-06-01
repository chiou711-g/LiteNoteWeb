package edit;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import noteBean.NoteBean;
import noteBean.NoteService;
import pageBean.PageService;

@WebServlet("/edit/EditNote.do")
public class EditNote extends HttpServlet {
    /**
	 *  Edit Note
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
        System.out.println("EditNote / _doPost");
        
		String table_number = req.getParameter("page_number");
        System.out.println("EditNote / _doPost / table_number = " + table_number);
		PageService pageService= new PageService();
		pageService.setPageTableId(table_number);
		NoteService noteBeanService = new NoteService(table_number);
		NoteBean noteBean = new NoteBean();        

        String note_id = req.getParameter("note_id");
        String note_link_uri = req.getParameter("note_link_uri");
//        String note_image_uri = req.getParameter("note_image_uri").replaceAll("\\s","");
        String note_image_uri = req.getParameter("note_image_uri").trim();
        if(note_image_uri.isEmpty())
        	note_image_uri = null;

        String note_title = req.getParameter("note_title");
        
        noteBean.setNote_id(Integer.valueOf(note_id));
        noteBean.setNote_link_uri(note_link_uri);
        noteBean.setNote_image_uri(note_image_uri);
        noteBean.setNote_title(note_title);
        
        System.out.println("EditNote / _doPost / note_id = " + note_id);
        System.out.println("EditNote / _doPost / note_link_uri = " + note_link_uri);
        System.out.println("EditNote / _doPost / note_image_uri = " + note_image_uri);
        System.out.println("EditNote / _doPost / note_title = " + note_title);
        System.out.println("EditNote / _doPost / req.getParameter(\"varModifyOne\") = " + req.getParameter("varModifyOne"));
        System.out.println("EditNote / _doPost / req.getParameter(\"varDeleteOne\") = " + req.getParameter("varDeleteOne"));
        
    	boolean isUpdated = false;
    	boolean isDeleted = false;
        if ((null != req.getParameter("varModifyOne")) && (req.getParameter("varModifyOne").equals("修改")) ) 
        {
	        try {
				noteBeanService.updateNote(table_number, note_link_uri, note_image_uri, note_title, Integer.valueOf(note_id));
				isUpdated = true;
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        }
        else if ((null != req.getParameter("varDeleteOne")) && (req.getParameter("varDeleteOne").equals("刪除")) )
        {
			try {
				noteBeanService.deleteNote(table_number,note_id);
				isDeleted = true;
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		}        

		// for js
		PrintWriter out = resp.getWriter(); //CW: set print writer
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();
		
		if (isUpdated || isDeleted) 
			jsonObj.addProperty("success", true);
		else
			jsonObj.addProperty("success", false); 

		JsonElement sBnJsonElement = gson.toJsonTree(noteBean);
		jsonObj.add("noteInfo", sBnJsonElement);

		out.println(jsonObj.toString());
		out.close();		
    }
}

