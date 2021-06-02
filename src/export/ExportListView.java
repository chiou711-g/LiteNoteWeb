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
import noteBean.NoteService;

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

@WebServlet("/export/ExportListView.do")
public class ExportListView extends HttpServlet {

	private static final long serialVersionUID = 1L;
	MyDB myDB;
	
	public ExportListView() {
		myDB = new MyDB();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		//ref https://stackoverflow.com/questions/11266163/pass-array-jsp-to-jsp
		
		String osName = System.getProperty("os.name");

		// get list view file name
		String fileName =  request.getParameter("listViewFileName"); 
		
		// check box for categories selection array
		String[] category_pos_selection = request.getParameterValues("category_selection");
		String[] category_selection = request.getParameterValues("category_selection");
		if(category_pos_selection == null)
		{
			System.out.println("ExportListView / category_selection = null");
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

		System.out.println("ExportListView / category_selection != null");

		// make JSON from UI selection
		JSONArray categories = new JSONArray();
		CategoryService categoryService = new CategoryService();
		
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
						System.out.println("ExportListView / _doPost / category_selection[" + i +"] = " + category_selection[i]);
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

		System.out.println("ExportListView / _doPost / categoriesCount = " + categoriesCount);
		System.out.println("ExportListView / _doPost / pagesCount = " + pagesCount);
		
		String[][] page_selection = new String[categoriesCount][pagesCount]; 
		if(category_selection != null)
		{
			int c;
			for(c=0;c<category_selection.length;c++)
			{
				System.out.print("ExportListView / _doPost / categories: ");
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
					int pageTableId = pageService.getPageIdByPosition(Integer.valueOf(page_selection[c][p]));
					String sql_cmd = "SELECT note_link_uri,note_image_uri,note_title FROM data_table_" + String.valueOf(pageTableId); 
					String link_uri,image_uri,note_title;
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
							link_uri = rs.getString(1);
							link.put("note_link_uri",link_uri);
							image_uri = rs.getString(2);
							System.out.println("ExportListView / _doPost / image_uri = " + image_uri);
							if(image_uri != null)
								link.put("note_image_uri",image_uri);
							else
								link.put("note_image_uri","");
							
							note_title = rs.getString(3);
							link.put("note_title",note_title);
							links.put(link);
						}
						myDB.DbClose();
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
					
					// handle page info
					PageBean pageBean = getPageBean(String.valueOf(pageTableId));
					System.out.println("ExportListView / _doPost / page title = " + pageBean.getPage_title());

					JSONObject page = new JSONObject();
					try {
						page.put("links",links);
						page.put("title",pageBean.getPage_title());
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
						System.out.println("ExportListView / _doPost / add categoryName = " + categoryName);
					}
				}
					
				if(category != null)
				{
					try {
						category.put("pages",pages);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					categories.put(category);
				}					
			}
			System.out.print("\n");
		}
		
		// output Json to Ajax
		JSONObject clientObj = new JSONObject();
		try {
			clientObj.put("categories",categories);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ExportListView / clientObj = " + clientObj);
		
		PrintWriter out = response.getWriter();
	    out.print(clientObj);
	    out.flush();	
	    
    	// make list view file name
	    String listStr = "";
    	listStr = listStr.concat("<div align=\"center\" style=\"border: 1px solid white;background:#dfffdf;\">");
	    for(int i=0;i<categories.length();i++  )
	    {
	    	JSONArray pages = null;
			try {
				pages = ((JSONObject)categories.get(i)).getJSONArray("pages");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	for(int j=0;j<pages.length();j++)
	    	{
	    		JSONArray links = null;
				try {
					links = ((JSONObject)pages.get(j)).getJSONArray("links");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		String title = null;
				try {
					title = ((JSONObject)pages.get(j)).getString("title");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		listStr = listStr.concat("<b style=\"background:#afffff;\">[" + title + "]</b><br><br>");
	    		for(int k=0;k<links.length();k++)
	    		{

					String linkUriStr = null;
					String imageUriStr = null;
					String noteTitle = null;
					try {
		    			// note Uri
						linkUriStr = ((JSONObject)links.get(k)).getString("note_link_uri");
						// note Uri
						imageUriStr = ((JSONObject)links.get(k)).getString("note_image_uri"); 
						
						// note title
						noteTitle =  ((JSONObject)links.get(k)).getString("note_title"); 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 


					if(linkUriStr.contains("youtu.be"))
	    				linkUriStr = linkUriStr.replace("youtu.be","www.youtube.com");
	    			
	    			if(!linkUriStr.contains("watch?v="))
	    				linkUriStr =  linkUriStr.replace("www.youtube.com/","www.youtube.com/watch?v=");
	    			
					// img href
	    			String pictureUri;
	    			if(!imageUriStr.isEmpty())
	    				pictureUri = imageUriStr;
	    			else
	    				pictureUri = "https://img.youtube.com/vi/"+NoteService.getYoutubeId(linkUriStr)+"/0.jpg";
	    			
	    			listStr = listStr.concat("<a target=\"_blank\" href=" + linkUriStr + ">");
	    			listStr = listStr.concat("<img src=" + pictureUri + " style=\"width:150px\"><br>");
	    			listStr = listStr.concat("</a>");
	    			

	    			if(noteTitle.isEmpty())
	    				listStr = listStr.concat( "?" + "<br><hr>");
	    			else
	    				listStr = listStr.concat(noteTitle + "<br><hr>");
	    		}
	    	}
	    }
	    listStr = listStr.concat("</div>");
	    System.out.println("ExportListView / listStr = " + listStr);	
	    
		// copy path
		String copyPath = null;
		System.out.println("ExportListView / listStr / fileName = " + fileName);
		
		// save GDrive list view file (keep release track) 
		String trackFileName = Util.getCurrentTimeString().concat("_").concat(fileName);
		copyPath = "C:/Users/cwchi/Google 雲端硬碟/1Files/"+
				"LiteNoteWeb_export/";
		copyPath = copyPath.concat("/").concat(trackFileName).concat(".html");
		
    	BufferedWriter writer = getWriter(copyPath);
	    writer.write(getListString(listStr));
	    writer.close();

		// save local list view file
		if(osName.contains("Windows"))
		{
			copyPath = "D:/Eclipse_ee_workspace/LiteNoteWeb/WebContent/EXPORT_LIST_VIEW.html";
	    	BufferedWriter writer1 = getWriter(copyPath);
		    writer1.write(getListString(listStr));
		    writer1.close();
		}
		else
		{
			copyPath = "./eclipse-workspace/LiteNoteWeb/WebContent/EXPORT_LIST_VIEW.html";
	    	BufferedWriter writer2 = getWriter(copyPath);
	    	listStr = getListString(listStr);
		    writer2.write(listStr);
		    writer2.close();
		}
		System.out.println("ExportListView / _doPost / copyPath = " + copyPath);
	    
	    //getServletContext().getRequestDispatcher("/export/exportJson.jsp").forward(request, response);
	}
	
	// Get buffered writer
	BufferedWriter getWriter(String path) throws IOException
	{
	    File file = new File(path);

        // Create file if not already existent. 
        if (!file.exists()) {
            file.createNewFile();
        }

        // add UTF-8 encoding
        FileOutputStream fileOutputStream = new FileOutputStream( file );
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter( fileOutputStream, "UTF-8" );
        BufferedWriter writer = new BufferedWriter( outputStreamWriter );
        return writer;
	}
	
	// Get thumb nails string
	String getHtmlString(String listStr)
	{
    	listStr = listStr.replace("\n\r", "<br>");
	    String before = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
	    		"<html>\n" + 
	    		"<head>\n" + 
	    		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" + 
	    		"<script type=\"text/javascript\">" +
	    		"function reloadPage()" +
	    		"{" +
	    		"window.location.reload(true);" +
	    		"}" +
	    		"</script>\r\n" +
	    		"<style>\r\n" + 
	    		".container {\r\n" + 
	    		"  background: gray;\r\n" + 
	    		"  width: 100%;\r\n" + 
	    		"  height: 100%; /* height given for illustration */\r\n" + 
	    		"  display: flex;\r\n" + 
	    		"  flex-flow: row wrap;\r\n" + 
	    		"  position: relative;\r\n" + 
	    		"}\r\n" + 
	    		"\r\n" + 
	    		".item {\r\n" + 
	    		"  background: black;\r\n" + 
	    		"  margin: 2px;\r\n" + 
	    		"  display:table-cell;vertical-align:middle;\r\n" +
	    		"  flex: 0 1 calc(8% - 2px); /* <-- adjusting for margin */\r\n" + 
	    		"}\r\n" + 
	    		"</style>\r\n" + 
	    		"<script src=\"/scripts/snippet-javascript-console.min.js?v=1\"></script>" +
	    		"</head>\n" + 
	    		"<body>\n" + 
	    		"<input type='button' value='reload 重新加载页面' onclick='reloadPage()' /><br><br>";
	    String after =  "\n</body>\n" +
	    		"</html>";
	    
	    return before.concat(listStr).concat(after);
	}
	
	// Get list string
	String getListString(String listStr)
	{
    	listStr = listStr.replace("\n\r", "<br>");
	    String before = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
	    		"<html>\n" + 
	    		"<head>\n" + 
	    		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" + 
	    		"<script type=\"text/javascript\">" +
	    		"function reloadPage()" +
	    		"{" +
	    		"window.location.reload(true);" +
	    		"}" +
	    		"</script>\r\n" +
	    		"</head>\n" + 
	    		"<body>\n" + 
	    		"<input type='button' value='reload 重新加载页面' onclick='reloadPage()' /><br><br>";
	    String after =  "\n</body>\n" + 
	    		"</html>";
	    
	    return before.concat(listStr).concat(after);
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
