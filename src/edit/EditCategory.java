package edit;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import categoryBean.CategoryBean;
import categoryBean.CategoryService;
import noteBean.NoteService;
import pageBean.PageBean;
import pageBean.PageService;

@WebServlet("/edit/EditCategory.do")
public class EditCategory extends HttpServlet {
    /**
	 *  Edit Category
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        
        String category_pos = req.getParameter("category_pos");
        System.out.println("EditCategory / _doPost / category_pos = " + category_pos);
        
		boolean isUpdated = false;
    	boolean isDeleted = false;

		CategoryBean categoryBean = null;
    	if(category_pos != null)
        {
    		CategoryService categoryService= new CategoryService();

    		try {
				categoryBean = categoryService.selectCategory(Integer.valueOf(category_pos));
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			String category_id = String.valueOf(categoryBean.getCategory_id());
	        System.out.println("EditCategory / _doPost / req.getParameter(\"varModifyCategory\") = " + req.getParameter("varModifyCategory"));
	        System.out.println("EditCategory / _doPost / req.getParameter(\"varDeleteCategory\") = " + req.getParameter("varDeleteCategory"));

        
	        if ((null != req.getParameter("varModifyCategory")) && (req.getParameter("varModifyCategory").equals("修改")) ) 
	        {
	            String category_name = req.getParameter("category_name");
	            System.out.println("EditCategory / _doPost / category_name = " + category_name);
	
	    		categoryBean.setCategory_name(category_name);
	    		
		        try {
					categoryService.updateCategory(category_id, category_name);
					isUpdated = true;
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				}
	        }
	        else if ((null != req.getParameter("varDeleteCategory")) && (req.getParameter("varDeleteCategory").equals("刪除")) )
	        {
				try {
					categoryService.deleteCategory(category_id);
					//delete related title_table row and its data_table
					PageService pageService= new PageService();
					int pagesCnt = pageService.getPages_count();
					
					System.out.println("EditCategory / _doPost / varDeleteCategory / pagesCnt = " + pagesCnt);
					
					// prepare pageId array to delete
					List<Boolean> pageIdDelArray = new ArrayList<Boolean>();
					List<Integer> pageIdValueArray = new ArrayList<Integer>();
					for(int i=0;i<pagesCnt;i++)
					{
						int page_id = pageService.getPageIdByPosition(i+1);
						pageIdValueArray.add(page_id);
						try {
							PageBean pageBean = pageService.selectPage(String.valueOf(page_id));
							if(pageBean.getPage_category_id() == Integer.valueOf(category_id))
							{
								pageIdDelArray.add(true);
								//can not delete page id here, since cursor will be changed after Delete
								// and cause _getPageIdByPosition wrong value 
							}
							else
								pageIdDelArray.add(false);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					// delete selected pageId array
					for(int i=0;i<pagesCnt;i++)
					{
						if(pageIdDelArray.get(i)  )
						{
							try {
								pageService.deletePage(String.valueOf(pageIdValueArray.get(i)));
								System.out.println("EditCategory / page_id should be deleted = " + pageIdValueArray.get(i));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				isDeleted = true;
	
			}        
        } 
    	
    	// delete all categories
    	if((null != req.getParameter("varDeleteAllCategory")) && (req.getParameter("varDeleteAllCategory").equals("刪除ALL")) )
        {
    		System.out.println("EditCategory / _doPost / delete all categories");
    		CategoryService categoryService= new CategoryService();

    		int categoriesCount = categoryService.getCategories_count();
    		
			// prepare categoryId array to delete
			List<Integer> cateIdValueArray = new ArrayList<Integer>();
			
			// prepare category id array
    		for(int h=1;h<=categoriesCount;h++)
    		{
	    		try {
					categoryBean = categoryService.selectCategory(Integer.valueOf(h));
				} catch (ClassNotFoundException e2) {
					e2.printStackTrace();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				int cate_id = categoryBean.getCategory_id();
				cateIdValueArray.add(cate_id);
    		}
			
    		for(int h=1;h<=categoriesCount;h++)
    		{
    			String category_id = String.valueOf(cateIdValueArray.get(h-1));
				try {
					categoryService.deleteCategory(category_id);
					//delete related title_table row and its data_table
					PageService pageService= new PageService();
					int pagesCnt = pageService.getPages_count();
					
					System.out.println("EditCategory / _doPost / varDeleteAllCategory / pagesCnt = " + pagesCnt);
					
					// prepare pageId array to delete
					List<Boolean> pageIdDelArray = new ArrayList<Boolean>();
					List<Integer> pageIdValueArray = new ArrayList<Integer>();
					for(int i=0;i<pagesCnt;i++)
					{
						int page_id = pageService.getPageIdByPosition(i+1);
						pageIdValueArray.add(page_id);
						try {
							PageBean pageBean = pageService.selectPage(String.valueOf(page_id));
							if(pageBean.getPage_category_id() == Integer.valueOf(category_id))
							{
								pageIdDelArray.add(true);
								//can not delete page id here, since cursor will be changed after Delete
								// and cause _getPageIdByPosition wrong value 
							}
							else
								pageIdDelArray.add(false);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					// delete selected pageId array
					for(int i=0;i<pagesCnt;i++)
					{
						if(pageIdDelArray.get(i)  )
						{
							try {
								pageService.deletePage(String.valueOf(pageIdValueArray.get(i)));
								System.out.println("EditCategory / page_id should be deleted = " + pageIdValueArray.get(i));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
					
					NoteService.setNoteBeanList(null);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}		
			isDeleted = true;
			// note: after deleting all categories, there are 2 tables still existed: category_table, title_table
		}     		

		// for JavaScript
		PrintWriter out = resp.getWriter(); //CW: set print writer
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonObject();
		
		if (isUpdated || isDeleted) 
			jsonObj.addProperty("success", true);
		else
			jsonObj.addProperty("success", false); 

		if(categoryBean != null)
		{
			JsonElement sBnJsonElement = gson.toJsonTree(categoryBean);
			jsonObj.add("categoryInfo", sBnJsonElement);
		}

		out.println(jsonObj.toString());
		out.close();		
    }
}

