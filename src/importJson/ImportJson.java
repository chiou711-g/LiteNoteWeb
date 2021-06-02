package importJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import categoryBean.CategoryBean;
import categoryBean.CategoryService;
import noteBean.NoteService;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pageBean.PageService;

@WebServlet("/importJson/ImportJson.do")
public class ImportJson extends HttpServlet {
	

	private static final long serialVersionUID = 1L;
	String copyPath = "./Java-workspace/LiteNoteWeb/WebContent/ExportJson.html";

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		boolean importJsonIsOK = true;
		
		String jsonString = request.getParameter("import_json_str");
		System.out.println("ImportJson / _doPost / jsonString = " + jsonString);

		CategoryService categorySrvc = new CategoryService();
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonString);
		}
		catch(JSONException e)
		{
			importJsonIsOK = false;
			e.printStackTrace();
		}
		
		if(jsonObj != null)
		{
			JSONArray categories = new JSONArray();
			try {
				categories = jsonObj.getJSONArray("content");
			} catch (JSONException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			int cate_last_id = 0;
			try {
				cate_last_id = categorySrvc.getCategories_last_id();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
			for(int i=0; i<categories.length(); i++)
			{
				JSONObject cateObj = new JSONObject();
				try {
					cateObj = categories.getJSONObject(i);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	
				//category id
				int addCategoryId = cate_last_id+i+1;
				System.out.println("ImportJson / _doPost / addCategoryId = "+ addCategoryId);
				
				//category name
				String addCategoryName = null;
				try {
					addCategoryName = cateObj.getString("category");
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("ImportJson / _doPost / addCategoryName = "+ addCategoryName);
				
				// insert new category row
				CategoryBean categoryBn = new CategoryBean();
				categoryBn.setCategory_id(addCategoryId);
				categoryBn.setCategory_name(addCategoryName);
				try {
					categorySrvc.insertCategory(String.valueOf(addCategoryId),addCategoryName);
				} catch (SQLException e) {
					importJsonIsOK = false;
					e.printStackTrace();
				}
				
				JSONArray pages = null;
				try {
					pages = cateObj.getJSONArray("link_page");
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				int pagesCnt = pages.length();
				System.out.println("ImportJson / _doPost / pagesCnt = "+ pagesCnt);
				
				PageService pageSrvc = new PageService();
				int lastPageId = pageSrvc.getLastPageId();
				System.out.println("ImportJson / _doPost / lastPageId = "+ lastPageId);
				for(int j=0;j<pagesCnt;j++)
				{
					// page table Id
					lastPageId++;
					String page_tableId = String.valueOf(lastPageId);
					System.out.println("ImportJson / _doPost / page_tableId = "+ page_tableId);
					
					// page title
					JSONObject pageObj = null;
					try {
						pageObj = pages.getJSONObject(j);
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					String page_title = null;
					try {
						page_title = pageObj.getString("title");
					} catch (JSONException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					System.out.println("ImportJson / _doPost / page_title = "+ page_title);
	
					// category Id
					int page_category_id = addCategoryId;
					System.out.println("ImportJson / _doPost / page_category_id = "+ page_category_id);
	
					// insert page title and category
					pageSrvc.insertPage_title_category(Integer.valueOf(page_tableId),page_title,page_category_id);
	
					// add new page table
					try {
						pageSrvc.insertPage(page_tableId);
					} catch (SQLException e) {
						importJsonIsOK = false;
						e.printStackTrace();
					}
					
					// add new link
					JSONArray links = null;
					try {
						links = pageObj.getJSONArray("links");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int linksCnt = links.length();
					
					for(int k=0;k<linksCnt;k++)
					{
						JSONObject linkObj = null;
						try {
							linkObj = links.getJSONObject(k);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						NoteService noteBeanService = new NoteService();
	
						// note link URI
						String note_link_uri = null;
						try {
							note_link_uri = linkObj.getString("note_link_uri");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						String note_image_uri = null;
						
						// note image URI, could be null
						try {
							note_image_uri = linkObj.getString("note_image_uri");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						String note_title = null;
						try {
							note_title = linkObj.getString("note_title");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("ImportJson / _doPost / note_title = "+ note_title);
						try 
						{
							// insert new link
							noteBeanService.insertNote(page_tableId,note_link_uri,note_image_uri,note_title );
							
						} catch (SQLException e1) {
							importJsonIsOK = false;
							e1.printStackTrace();
						}
						
					}
				}
				
			}
		}
		else
			importJsonIsOK = false;
		
		// for js
		PrintWriter out = response.getWriter(); //CW: set print writer
		JsonObject obj = new JsonObject();
		System.out.println("ImportJson / _doPost / importJsonIsOK = "+ importJsonIsOK);
		if(importJsonIsOK ) 
			obj.addProperty("success", true);
		else
			obj.addProperty("success", false);

		out.println(obj.toString());
		out.close();
		
	}//end of doPost	
}
