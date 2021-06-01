package operation;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import noteBean.NoteService;

@WebServlet("/operation/MoveNote.do")
public class MoveNote extends HttpServlet {
    /**
	 *  Move Note
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
		String table_number = req.getParameter("page_number");
        String note_pos1 = req.getParameter("note_pos1");
        String note_pos2 = req.getParameter("note_pos2");
        System.out.println("MoveNote / _doPost / table_number = " + table_number);
        System.out.println("MoveNote / _doPost / note_pos1 = " + note_pos1);
        System.out.println("MoveNote / _doPost / note_pos2 = " + note_pos2);
        
        boolean isUpdated = false;
        NoteService noteService= new NoteService();
        
        int from = Integer.valueOf(note_pos2);
        int to = Integer.valueOf(note_pos1);

		// check position boundary
		boolean boundaryOK = noteService.isBoundaryOK(table_number,note_pos1, note_pos2);
		
        if(boundaryOK) {
	        if(from > to) { // from > to : move to Before to
	            for(int i=from;i>to;i--)
	            	 isUpdated = noteService.areNotesSwapped(table_number,String.valueOf(i),String.valueOf(i-1));
	        } else { // from < to : move to After to
	            for(int i=from;i<to;i++)
	            	 isUpdated = noteService.areNotesSwapped(table_number,String.valueOf(i),String.valueOf(i+1));
	        }
        }

		// for JavaScript
		PrintWriter out = resp.getWriter(); //CW: set print writer
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();
		
		if (isUpdated) 
			jsonObj.addProperty("success", true);
		else
			jsonObj.addProperty("success", false); 

		if(note_pos1 != null)
		{
			JsonElement sBnJsonElement = gson.toJsonTree(note_pos1);
			jsonObj.add("noteInfo", sBnJsonElement);
		}

		out.println(jsonObj.toString());
		out.close();		
    }
	
}