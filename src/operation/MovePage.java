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

import pageBean.PageService;

@WebServlet("/operation/MovePage.do")
public class MovePage extends HttpServlet {
    /**
	 *  Move Page
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
        String page_pos1 = req.getParameter("page_pos1");
        String page_pos2 = req.getParameter("page_pos2");
        System.out.println("MovePage / _doPost / page_pos1 = " + page_pos1);
        System.out.println("MovePage / _doPost / page_pos2 = " + page_pos2);
        
        PageService pageService = new PageService();
		boolean boundaryOK = pageService.isBoundaryOK(page_pos1, page_pos2);
		boolean isUpdated = false;
    	
        int from = Integer.valueOf(page_pos2);
        int to = Integer.valueOf(page_pos1);

        if( boundaryOK && 
        	(null != req.getParameter("varMovePage")) 
        	&& (req.getParameter("varMovePage").equals("搬動page")) ) {
            if(from > to) { // from > to : move to Before to
	            for(int i=from;i>to;i--) 
	            	 isUpdated = pageService.arePagesSwapped(String.valueOf(i),String.valueOf(i-1));
            } else { // from < to : move to After to
	            for(int i=from;i<to;i++) 
	            	 isUpdated = pageService.arePagesSwapped(String.valueOf(i),String.valueOf(i+1));
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

		if(page_pos1 != null)
		{
			JsonElement sBnJsonElement = gson.toJsonTree(page_pos1);
			jsonObj.add("pageInfo", sBnJsonElement);
		}

		out.println(jsonObj.toString());
		out.close();		
    }
	
}

