package noteBean;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.services.youtube.YouTube;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import myDB.MyDB;
import util.Util;


public class NoteService implements Serializable {

	MyDB myDB;
	public static YouTube youtube;
    boolean isGotDuration;
    String acquiredDuration;
    String duration;
    
	public NoteService() {
		myDB = new MyDB();
	};
	
	public NoteService(String tableNumber) {
		this.table_number = tableNumber;
		myDB = new MyDB();
	};
	
	private static final long serialVersionUID = 1L;
	Connection conn;
	
	private static List<NoteBean> noteBeanList = new ArrayList<NoteBean>();
	public String table_number;

	public List<NoteBean> getNoteBeanList() {
		return noteBeanList;
	}

	public static void setNoteBeanList(List<NoteBean> noteBeanList) {
		NoteService.noteBeanList = noteBeanList;
	}

	public List<NoteBean> selectNoteBeanList() throws IOException, ClassNotFoundException, SQLException {		

		if(noteBeanList != null)
			noteBeanList.clear();
		else
			noteBeanList = new ArrayList<NoteBean>(); 
			
		PreparedStatement prepStmt = null;
		
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("SELECT note_id,note_link_uri,note_image_uri,note_title FROM data_table_" + table_number);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			NoteBean noteListBean = new NoteBean();
			noteListBean.setNote_id(rs.getInt("note_id"));
			noteListBean.setNote_link_uri(rs.getString("note_link_uri"));
			
			// image Uri is null for YouTube
			if(rs.getString("note_image_uri") == null)
			{
				String imageUri = "https://img.youtube.com/vi/"+ getYoutubeId(rs.getString("note_link_uri")) + "/0.jpg";
				noteListBean.setNote_image_uri(imageUri);
			}
			else
				noteListBean.setNote_image_uri(rs.getString("note_image_uri"));
			
			
			if(rs.getString("note_image_uri") != null)
				noteListBean.setNote_link_uri_embed(rs.getString("note_image_uri"));
			else
				noteListBean.setNote_link_uri_embed(rs.getString("note_link_uri"));
			
			noteListBean.setNote_title(rs.getString("note_title"));
			
			// get duration thread
	        isGotDuration = false;
	        getDurationThread(noteListBean.getNote_link_uri());
	        
	        //wait for buffering
	        int time_out_count = 0;
	        while ((!isGotDuration) && time_out_count< 10)
	        {
	            try {
	                Thread.sleep(10);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            time_out_count++;
	        }
	        duration = acquiredDuration;	
			noteListBean.setNote_duration(duration);

			noteBeanList.add(noteListBean);
		}
		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
		return noteBeanList;
	}
	
	// note_id
	// note_link_uri
	// note_image_uri
	// note_title
	// note_created

	//
	// Insert
	//
	public int insertNote(String table_number,String note_link_uri,String note_image_uri,String note_title) throws SQLException
	{
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement(
				"INSERT INTO data_table_"+ table_number + " (note_link_uri,note_image_uri,note_title,note_created) VALUES(?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		prepStmt.setString(1, note_link_uri);
		prepStmt.setString(2, note_image_uri);
		prepStmt.setString(3, note_title);
		
		Date now = new Date();
		Timestamp ts = new Timestamp(now.getTime());
		prepStmt.setTimestamp(4, ts);

		int iRowCount = prepStmt.executeUpdate();
		
		// print
		if (iRowCount > 0) {
			NoteBean noteBean = new NoteBean();
			noteBean.setNote_link_uri(note_link_uri);
			noteBean.setNote_image_uri(note_image_uri);
			noteBean.setNote_title(note_title);
			System.out.println("Insert a note: OK!");
			System.out.println("URI: " + note_link_uri);
			System.out.println("TITLE: " + note_title);
		} else {
			System.out.println("Insert a note: NG!");
		}

		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
		return iRowCount;
	}
	
	//
	// Delete
	//
	public void deleteNote(String table_number,String note_id) throws SQLException, ClassNotFoundException, IOException {
		
		System.out.println("NoteService / _deleteNote / note_id = " + note_id);
		PreparedStatement prepStmt = null;
		myDB.DbConnect();

		// create prepare statement
		prepStmt = myDB.conn
				.prepareStatement("DELETE FROM data_table_"+ table_number +" WHERE note_id = ?");

		prepStmt.setInt(1, Integer.valueOf(note_id));

		int iCount = prepStmt.executeUpdate();

		System.out.println("iCount=" + iCount);
		
		// print
		if (iCount > 0) {
			System.out.println("Delete a note: OK!");
		} else {
			System.out.println("Delete a note: NG!");
		}

		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
	}
	
	
	//
	// Select
	//
	public NoteBean selectNote(String table_number,String note_id) throws IOException, ClassNotFoundException, SQLException {
		NoteBean noteBean = new NoteBean();
		
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("SELECT note_id,note_link_uri,note_image_uri,note_title FROM data_table_"+ table_number+" WHERE note_id = ? ");

		prepStmt.setInt(1, Integer.valueOf(note_id));
		ResultSet rs = prepStmt.executeQuery();
		String na = "";

		if (rs.next()) {
			noteBean.setNote_id(rs.getInt("note_id"));
			noteBean.setNote_link_uri(rs.getString("note_link_uri"));
			noteBean.setNote_image_uri(rs.getString("note_image_uri"));
			noteBean.setNote_title(rs.getString("note_title") );
		} else {
			noteBean.setNote_id(0);
			noteBean.setNote_link_uri(na);
			noteBean.setNote_image_uri(na);
			noteBean.setNote_title(na);
		}
		
		if(rs.getString("note_image_uri") != null)
			noteBean.setNote_link_uri_embed(rs.getString("note_image_uri"));
		else
			noteBean.setNote_link_uri_embed(rs.getString("note_link_uri"));
		
		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();

		// Check
//		System.out.println("NoteService / _selectNote / noteBean.getNote_id() = " + noteBean.getNote_id());
//		System.out.println("NoteService / _selectNote / noteBean.getNote_link_uri() = " + noteBean.getNote_link_uri());
//		System.out.println("NoteService / _selectNote / noteBean.getNote_title() = " + noteBean.getNote_title());
		return noteBean;
	}
	
	//
	// get note_id by position
	//
	public int getNoteIdByPosition(String table_number,String _position) throws IOException, ClassNotFoundException, SQLException {
		
		int note_id = 0;
		int position = Integer.valueOf(_position);
		PreparedStatement prepStmt = null;
		myDB.DbConnect();
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("SELECT note_id FROM data_table_"+ table_number);
		ResultSet rSet = prepStmt.executeQuery();
		
		int row_count = 0;

		while (rSet.next()) 
		{
			row_count++;
			if(position == row_count)
			{
				note_id = rSet.getInt("note_id");
				System.out.println("NoteService / _getNoteIdByPosition / note_id = " + note_id);
				break;
			}
		} 
		prepStmt.clearParameters();
		prepStmt.close();
		myDB.DbClose();
		
	
		return note_id;
	}
	

	
	//
	// Update
	//
	public void updateNote(String table_number, String note_link_uri, String note_image_uri, String note_title, int note_id) 
			throws ClassNotFoundException, SQLException, IOException {
		
		myDB.DbConnect();
		
		PreparedStatement prepStmt = null;
		// Query MySQL
		// create prepare statement
		prepStmt = myDB.conn.prepareStatement("UPDATE data_table_" + table_number +" SET "
				+ "note_link_uri=?," + "note_image_uri=?," + "note_title=?," + "note_created=?"
				+ "WHERE note_id = ? ");

		prepStmt.setString(1, note_link_uri); //link URI
		prepStmt.setString(2, note_image_uri); //image URI
		prepStmt.setString(3, note_title); //TITLE
		Date now = new Date();
		Timestamp ts = new Timestamp(now.getTime());
		prepStmt.setTimestamp(4, ts);//created TIME
		prepStmt.setInt(5, note_id); // ID 

		prepStmt.executeUpdate();
		
		prepStmt.clearParameters();
		prepStmt.close();
		
		myDB.DbClose();
	}	
	
	
	
	//
	// Get rows count
	//
	int rowsCount;
	public int getRowsCount(String table_number)  throws SQLException 
	{
		    System.out.println("_getRowsCount");
	        PreparedStatement prepStmt = null;
	        myDB.DbConnect();	
			
			// create prepare statement
			prepStmt = myDB.conn.prepareStatement("SELECT COUNT(*) FROM data_table_"+ table_number);
			ResultSet res = prepStmt.executeQuery();
			rowsCount = 0;
			while (res.next()){
				rowsCount = res.getInt(1);
            }
			System.out.println("rowCount=" + rowsCount);
			
			prepStmt.clearParameters();
			prepStmt.close();
			myDB.DbClose();
			return rowsCount;
	}
	
    // Get YouTube Id
	public static String getYoutubeId(String url) {
	    String videoId = "";
		// format 1: https://www.youtube.com/watch?v=_sQSXwdtxlY
		// format 2: https://youtu.be/V7MfPD7kZuQ (notice: start with V)
	    if (url != null && url.trim().length() > 0 && url.startsWith("http")) {
            String expression = "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??(v=)?([^#\\&\\?]*).*";
	        CharSequence input = url;
	        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(input);
	        if (matcher.matches()) {
				String groupIndex1 = matcher.group(8);
	            if (groupIndex1 != null && groupIndex1.length() == 11)
	                videoId = groupIndex1;
	        }
	    }
	    return videoId;
	}	
	
	
	//
	// check position boundary
	//
	public boolean isBoundaryOK(String table_number,String note_pos1,String note_pos2) {
		
		if((table_number == null) || (note_pos1 == null) || (note_pos2 == null) || note_pos1.contentEquals(note_pos2))
			return false;
		
        int from = Integer.valueOf(note_pos2);
        int to = Integer.valueOf(note_pos1);

		// check position boundary

		int notesCount = 0;
		try {
    		notesCount = getRowsCount(table_number);
		} catch (NumberFormatException|SQLException e1) {
			e1.printStackTrace();
		}
		boolean boundaryOK =  (from <= notesCount) && (from > 0) &&
							  (to <= notesCount) && (to > 0) ; 
		return boundaryOK;
	}
	
	//
	// notes are swapped
	//
	public boolean areNotesSwapped(String table_number,String note_pos1,String note_pos2) {
		boolean isUpdated = false;
		NoteBean noteBean1 = null;
		NoteBean noteBean2 = null;
		
    	if(note_pos1 != null){

			int note_id1 = 0;
			int note_id2 = 0;
			try {
				note_id1 = getNoteIdByPosition(table_number,note_pos1);
				note_id2 = getNoteIdByPosition(table_number,note_pos2);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
			
    		try {
				noteBean1 = selectNote(table_number,String.valueOf(note_id1));
				noteBean2 = selectNote(table_number,String.valueOf(note_id2));
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			} 
    		
			try {
				updateNote(table_number, noteBean1.getNote_link_uri(), 
						noteBean1.getNote_image_uri(), noteBean1.getNote_title(), note_id2);

				updateNote(table_number, noteBean2.getNote_link_uri(), 
						noteBean2.getNote_image_uri(), noteBean2.getNote_title(), note_id1);
				
				isUpdated = true;
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
        
        } 		
    	return isUpdated;
	}
    
	// get duration thread
	public void getDurationThread(final String uri) {
		System.out.println("----- _getDurationThread / uri = " + uri);
        // Call the API and print results.
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
        			String duration = getYouTubeDuration(uri); 
        			if(duration == null) 
        				acquiredDuration = "na";
        			else if(duration.equalsIgnoreCase("P0D"))
        				acquiredDuration = "Live";
        			else
        				acquiredDuration = Util.convertYouTubeDuration(duration);

        			//System.out.println("----- acquiredDuration = " + acquiredDuration);
                    isGotDuration = true;
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        });
    }	

	//private static final Pattern YOUTUBE_ID_PATTERN = Pattern.compile("(?<=v\\=|youtu\\.be\\/)\\w+");

	static String expression = "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??(v=)?([^#\\&\\?]*).*";
	private static final Pattern YOUTUBE_ID_PATTERN = Pattern.compile(expression);

	private static final Gson GSON = new GsonBuilder().create();

	// get YouTube duration
	public static String getYouTubeDuration(String url) {
		
	    Matcher m = YOUTUBE_ID_PATTERN.matcher(url);
	    
	    if (!m.find())
	        throw new IllegalArgumentException("Invalid YouTube URL.");
	    
	    JsonElement element = null;
		try {
//			element = getYoutubeInfo(m.group()); //something wrong when https://www.youtube.com/watch?v=USZkX--cXTM 
			element = getYoutubeInfo(Util.getYoutubeId(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = null;
		JSONArray jsonArray = null;
		try {
			jsonObject = new JSONObject(element.toString());
			jsonArray = jsonObject.getJSONArray("items");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String duration = null;
		
		if(jsonArray.length() >0) { // skip private URI
			try {
				duration = jsonArray.getJSONObject(0).getJSONObject("contentDetails").getString("duration");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println("------ duration = " + duration);
		return duration;
	}
	
	// get YouTube title
//	public static String getYouTubeTitle(String url) {
//		
//	    Matcher m = YOUTUBE_ID_PATTERN.matcher(url);
//	    
//	    if (!m.find())
//	        throw new IllegalArgumentException("Invalid YouTube URL.");
//	    
//	    JsonElement element = null;
//		try {
//			element = getYoutubeInfo(m.group());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		JSONObject jsonObject = null;
//		JSONArray jsonArray = null;
//		try {
//			jsonObject = new JSONObject(element.toString());
//			jsonArray = jsonObject.getJSONArray("items");
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		String title = null;
//		try {
//			title = jsonArray.getJSONObject(0).getJSONObject("snippet").getString("title");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return title;
//	}	

	// get YouTube Json info
	public static JsonElement getYoutubeInfo(String youtubeID) throws MalformedURLException, IOException {
		System.out.println("----- youtubeID = " + youtubeID);
		//	String url = "https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet&id=" + youtubeID +
		//  String url = "https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet,contentDetails&id=" + youtubeID +
				String url = "https://www.googleapis.com/youtube/v3/videos?part=id%2C+contentDetails&id=" + youtubeID +
				"&key=" + ApiKey.DEVELOPER_KEY;
		
		//System.out.println("---- ulr = " + url);
		
	    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
	    connection.addRequestProperty("user-agent",
	            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
	    connection.connect();

	    String content = IOUtils.toString(connection.getInputStream()); // Apache commons. Use whatever you like 
	    connection.disconnect();

	    String[] queryParams = content.split("\\&"); // It's query string format, so split on ampterstands
	    for (String param : queryParams) {
	        param = URLDecoder.decode(param, StandardCharsets.UTF_8.name()); // It's encoded, so decode it
	        String[] parts = param.split("\\=", 2); // Again, query string format. Split on the first equals character
	        
	        if (parts[0].contains("videoListResponse") ) // We want the player_response parameter. This has all the info
	        	return GSON.fromJson(parts[0], JsonElement.class); // It's in JSON format, so you use a JSON deserializer to deserialize it
	    }

	    throw new RuntimeException("Failed to get info for video: " + youtubeID);
	}		
	 
}
