package noteBean;

public class NoteBean {
	
	private static String readBy = "";
	private  Integer note_id;
	private  String note_link_uri;
	private  String note_link_uri_embed;
	private  String note_image_uri;
	private  String note_title;
	private  String note_duration;
	private  String note_created;

	// for being Java bean
	public NoteBean() {
	}


	public String getReadBy() {
		return readBy;
	}

	public void setReadBy(String readBy) {
		System.out.println("NoteBean / _setReadBy / readBy = " + readBy);
		NoteBean.readBy = readBy;
	}		
	
	public Integer getNote_id() {
		return note_id;
	}
	
	public void setNote_id(Integer id) {
		this.note_id = id;
	}
	
	public String getNote_link_uri() {
		return note_link_uri;
	}
	
	public void setNote_link_uri(String link_uri) {
		this.note_link_uri = link_uri;
	}
	
	public String getNote_image_uri() {
		return note_image_uri;
	}
	
	public void setNote_image_uri(String image_uri) {
		this.note_image_uri = image_uri;
	}
	
	public String getNote_title() {
		return note_title;
	}
	
	public void setNote_title(String title) {
		this.note_title = title;
	}
	
	public String getNote_duration() {
		return note_duration;
	}
	
	public void setNote_duration(String duration) {
		this.note_duration = duration;
	}
	
	public String getNote_created() {
		return note_created;
	}
	
	public void setNote_created(String created) {
		this.note_created = created;
	}

	public String getNote_link_uri_embed() {
		return note_link_uri_embed;
	}

	public void setNote_link_uri_embed(String link_uri) {
		//example:
		//phone: https://youtu.be/GUzdWPWllJA
		//pc: https://www.youtube.com/watch?v=GUzdWPWllJA
		//embed: https://www.youtube.com/embed/GUzdWPWllJA
		if(link_uri == null)
			return;
		
		if(link_uri.contains("youtu.be"))
			link_uri = link_uri.replace("youtu.be","www.youtube.com");
		if(link_uri.contains("watch?v="))
			link_uri = link_uri.replace("watch?v=","");
		this.note_link_uri_embed =  link_uri.replace("www.youtube.com","www.youtube.com/embed");
	}
}
