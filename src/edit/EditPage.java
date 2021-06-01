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

import pageBean.PageBean;
import pageBean.PageService;

@WebServlet("/edit/EditPage.do")
public class EditPage extends HttpServlet {
    /**
	 *  Edit Page
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
        System.out.println("EditPage / _doPost");
        

        String page_id = req.getParameter("page_id");
        System.out.println("EditPage / _doPost / page_id = " + page_id);
        
		PageService pageService= new PageService();
		PageBean pageBean = null;
		try {
			pageBean = pageService.selectPage(page_id);
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}        

		pageBean.setPage_id(Integer.valueOf(page_id));
        System.out.println("EditPage / _doPost / req.getParameter(\"varModifyPage\") = " + req.getParameter("varModifyPage"));
        System.out.println("EditPage / _doPost / req.getParameter(\"varDeletePage\") = " + req.getParameter("varDeletePage"));
        
    	boolean isUpdated = false;
    	boolean isDeleted = false;
        if ((null != req.getParameter("varModifyPage")) && (req.getParameter("varModifyPage").equals("修改")) ) 
        {
            String page_title = req.getParameter("page_title");
            String page_category_id = req.getParameter("page_category_id");
            System.out.println("EditPage / _doPost / page_title = " + page_title);
            System.out.println("EditPage / _doPost / page_category_id = " + page_category_id);

    		pageBean.setPage_title(page_title);
    		pageBean.setPage_category_id(Integer.valueOf(page_category_id));
    		
	        try {
				pageService.updatePage(page_id, page_title, page_category_id);
				isUpdated = true;
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        }
        else if ((null != req.getParameter("varDeletePage")) && (req.getParameter("varDeletePage").equals("刪除")) )
        {
			try {
				pageService.deletePage(page_id);
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

		JsonElement sBnJsonElement = gson.toJsonTree(pageBean);
		jsonObj.add("pageInfo", sBnJsonElement);

		out.println(jsonObj.toString());
		out.close();		
    }
}

