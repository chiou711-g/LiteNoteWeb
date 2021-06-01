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

@WebServlet("/operation/SwapCategory.do")
public class SwapCategory extends HttpServlet {
    /**
	 *  Swap Category
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
        System.out.println("SwapCategory / _doPost / category_pos1 = " + category_pos1);
        System.out.println("SwapCategory / _doPost / category_pos2 = " + category_pos2);
        
		boolean isUpdated = false;

		// check position boundary
		CategoryService catService= new CategoryService();
		boolean boundaryOK = catService.isBoundaryOK(category_pos1, category_pos2);
		
    	if(boundaryOK && (category_pos1 != null) && (category_pos2 != null) )
        {
    		isUpdated = catService.areCategoriesSwapped(category_pos1,category_pos2);
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

