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

import categoryBean.CategoryService;

@WebServlet("/operation/MoveCategory.do")
public class MoveCategory extends HttpServlet {
    /**
	 *  Move Category
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
        String category_pos1 = req.getParameter("category_pos1");
        String category_pos2 = req.getParameter("category_pos2");
        System.out.println("MoveCategory / _doPost / category_pos1 = " + category_pos1);
        System.out.println("MoveCategory / _doPost / category_pos2 = " + category_pos2);
        
		CategoryService catService= new CategoryService();

		int from = Integer.valueOf(category_pos2);
        int to = Integer.valueOf(category_pos1);

		boolean isUpdated = false;

		// check position boundary
		boolean boundaryOK = catService.isBoundaryOK(category_pos1, category_pos2);
		
        if( boundaryOK &&
        	(null != req.getParameter("varMoveCategory")) && 
        	(req.getParameter("varMoveCategory").equals("搬動Category")) ) {
            
            if(from > to) { // from > to : move to Before to
	            for(int i=from;i>to;i--)
	            	 isUpdated = catService.areCategoriesSwapped(String.valueOf(i),String.valueOf(i-1));
            } else { // from < to : move to After to
	            for(int i=from;i<to;i++)
	            	isUpdated = catService.areCategoriesSwapped(String.valueOf(i),String.valueOf(i+1));
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

		if(category_pos1 != null)
		{
			JsonElement sBnJsonElement = gson.toJsonTree(category_pos1);
			jsonObj.add("categoryInfo", sBnJsonElement);
		}

		out.println(jsonObj.toString());
		out.close();		
    }
	
}

