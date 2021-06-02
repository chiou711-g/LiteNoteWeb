package export;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import categoryBean.CategoryBean;
import categoryBean.CategoryService;
import myDB.MyDB;

import java.io.*;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pageBean.PageBean;
import pageBean.PageService;
import util.Util;

@WebServlet("/export/ExportJson.do")
public class ExportJson extends HttpServlet {

	private static final long serialVersionUID = 1L;
	MyDB myDB;
	
	public ExportJson() {
		myDB = new MyDB();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		//ref https://stackoverflow.com/questions/11266163/pass-array-jsp-to-jsp
		
		// copy path
		String copyPath = null;
		
		String osName = System.getProperty("os.name");
		//System.out.println(" / _doPost / osName = " + osName);

		// check box for categories selection array
		String[] category_pos_selection = request.getParameterValues("category_selection");
		String[] category_selection = request.getParameterValues("category_selection");
		if(category_pos_selection == null)
		{
			System.out.println(" / category_selection = null");
			JSONObject clientObj = new JSONObject();
			PrintWriter out = response.getWriter();
			try {
				clientObj.put("client","none");
				clientObj.put("content","No selection yet, please go back and select again.");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.print(clientObj.toString());
		    out.flush();
			return;
		}

		System.out.println(" / category_selection != null");

		JSONArray categories = new JSONArray();
		CategoryService categoryService = new CategoryService();
		
		// verify
		int categoriesCount = category_pos_selection.length;
		int category_id = 0;
		
		// get category_id array by position array
		for(int i=0;i<categoriesCount;i++)
		{
			// get category_id by position
			try {
				PreparedStatement prepStmt = null;
				myDB.DbConnect();
				prepStmt = myDB.conn.prepareStatement("SELECT category_id FROM category_table");
				ResultSet rSet = prepStmt.executeQuery();
				
				int row_count = 0;
		
				while (rSet.next()) 
				{
					row_count++;
					if(Integer.valueOf(category_pos_selection[i]) == row_count)
					{
						category_selection[i] = String.valueOf(rSet.getInt("category_id"));
						System.out.println(" / _doPost / category_selection[" + i +"] = " + category_selection[i]);
						break;
					}
				} 
				prepStmt.clearParameters();
				prepStmt.close();
				myDB.DbClose();					
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}		
		
		
		int[] pagesInCategory = new int[categoriesCount];
		int pagesCount = Integer.valueOf(request.getParameter("pagesCnt"));

		System.out.println(" / _doPost / categoriesCount = " + categoriesCount);
		System.out.println(" / _doPost / pagesCount = " + pagesCount);
		
		String[][] page_selection = new String[categoriesCount][pagesCount]; 
		if(category_selection != null)
		{
			int c;
			for(c=0;c<category_selection.length;c++)
			{
				System.out.print(" / _doPost / categories: ");
				System.out.print(category_selection[c] + " => ");
				
				page_selection[c] = request.getParameterValues("row_num"+ category_pos_selection[c] + "page_selection");
				
				pagesInCategory[c] = 0;
				
				// count pages in each category
				int pageSelCnt = 0;
				
				if(page_selection[c] != null)
					pageSelCnt = page_selection[c].length;
				
				System.out.print("(pageSelCnt=" + pageSelCnt+")");
				
				for(int p=0;p<pageSelCnt;p++)
				{
					String page_category_id = getCategoryId(page_selection[c][p]);
					System.out.print("(page_category_id=" + page_category_id+")");
					
					if(page_category_id.equalsIgnoreCase(category_selection[c]))
					{
						System.out.print("p");
						System.out.print(page_selection[c][p]);
						System.out.print(" ");

						pagesInCategory[c]++;
					}
							
				}
				System.out.print(" (" + pagesInCategory[c] + ") pages in category" + category_selection[c]  );
				System.out.print("\n");	
				
				// from small to large:
				//	JSONObject: link , page (= links + title), category (= pages + category_name) , client ( = categories + clientName)
				//	JSONArray: links, pages, categories
				
				String categoryName = null;
				category_id = 0;
				
				JSONObject	category = null;
				JSONArray pages = new JSONArray();
				PageService pageService = new PageService();
				
				for(int p=0;p<pageSelCnt;p++)
				{
//					System.out.println(" / _doPost / c = " + c  );
//					System.out.println(" / _doPost / p = " + p  );
//					System.out.println(" / _doPost / page_selection[c][p] = " + page_selection[c][p]  );
					int pageTableId = pageService.getPageIdByPosition(Integer.valueOf(page_selection[c][p]));
					String sql_cmd = "SELECT * FROM data_table_" + String.valueOf(pageTableId); 
					String Id;
					String Uri,image_uri;
					String Title=null;
					ResultSet rs = null;
					try {
						myDB.DbConnect();
						Statement statement = myDB.conn.createStatement();
						rs = statement.executeQuery(sql_cmd);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					JSONArray links = new JSONArray();
					// get all notes
					try {			
						while(rs.next())
						{
							JSONObject link = new JSONObject();
							Id = rs.getString(1);
							Uri = rs.getString(2);
							image_uri = rs.getString(3);
							Title = rs.getString(4);
							link.put("note_id",Id);
							link.put("note_link_uri",Uri);
							link.put("note_image_uri",image_uri);
							link.put("note_title",Title);
							links.put(link);
						}
						myDB.DbClose();
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
					
					// handle page info
					PageBean pageBean = getPageBean(String.valueOf(pageTableId));
	
					JSONObject page = new JSONObject();
					try {
						page.put("title",pageBean.getPage_title());
						page.put("links",links);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					category_id = pageBean.getPage_category_id();

					if(category_id == Integer.valueOf(category_selection[c]))
					{
						category = new JSONObject();
						
						try {
							CategoryBean categoryBn = categoryService.selectCategory(Integer.valueOf(category_pos_selection[c]));
							categoryName  = categoryBn.getCategory_name();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						pages.put(page);
						System.out.println(" / _doPost / add categoryName = " + categoryName);
					}
				}
					
				if(category != null)
				{
					try {
						category.put("category",categoryName );
						category.put("link_page",pages);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	
					categories.put(category);
				}					
				
			}
			System.out.print("\n");
		}
		
		// Json to Ajax
		JSONObject clientObj = new JSONObject();
		try {
			clientObj.put("client","TV YouTube" );
			clientObj.put("content",categories.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter out = response.getWriter();
		System.out.println(" / clientObj = " + clientObj);
	    out.print(clientObj);
	    out.flush();	
	    
		// write Json file
		JSONObject fileObj = new JSONObject();
		try {
			fileObj.put("client","TV YouTube" );
			fileObj.put("content",categories);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String jsonStr = fileObj.toString();
	    
		String fileName =  request.getParameter("fileName");
		String dirPath;
		
		// write GDrive: APP JSON
		if(osName.contains("Windows"))
			dirPath = "C:/Users/cwchi/Google 雲端硬碟/1Files/JSON/";
		else
			dirPath = "./doc/LiteNoteWeb_JSON/";
		
	    copyPath = dirPath.concat(fileName).concat(".json");
	    
	    BufferedWriter writer = new BufferedWriter(new FileWriter(copyPath));
	    writer.write(jsonStr);
	    writer.close();
		
		
		// write GDrive: release track JSON
		if(osName.contains("Windows"))
			dirPath = "C:/Users/cwchi/Google 雲端硬碟/1Files/" + "LiteNoteWeb_export/";
		else
			dirPath = "./doc/LiteNoteWeb_JSON/";
		
	    copyPath = dirPath.concat(Util.getCurrentTimeString()).concat("_").concat(fileName).concat(".json");
	    
	    writer = new BufferedWriter(new FileWriter(copyPath));
	    writer.write(jsonStr);
	    writer.close();

		// local JSON
		if(osName.contains("Windows"))
		    copyPath = "D:/Eclipse_ee_workspace/LiteNoteWeb/WebContent/EXPORT_JSON.json";
		else
			copyPath = "./Java-workspace/LiteNoteWeb/WebContent/EXPORT_JSON.json";

		BufferedWriter writer1 = new BufferedWriter(new FileWriter(copyPath));
	    writer1.write(jsonStr);
	    writer1.close();
		System.out.println(" / _doPost / copyPath = " + copyPath);	    

	    //getServletContext().getRequestDispatcher("//.jsp").forward(request, response);
	}
	
	// get category Id by pageId
	String getCategoryId(String pageId)
	{
		PageService pageService = new PageService();//TODO: change to position?
		pageId = String.valueOf(pageService.getPageIdByPosition(Integer.valueOf(pageId)));
		
		
		int category_id = getPageBean(pageId).getPage_category_id();
		return String.valueOf(category_id);
	}
	
	// get page bean by pageId
	PageBean getPageBean(String pageId)
	{
		PageService pageService = new PageService();
		PageBean pageBean = null;
		try {
			try {
				pageBean = pageService.selectPage(pageId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}				
		return pageBean;
	}	
	
}
