package pageBean;

public class PageBean {
	
	private  Integer page_id;
	private  String page_title;
	private  Integer page_category_id;
	
	private  String page_created;

	// for being Java bean
	public PageBean() {
	}


	// id
	public Integer getPage_id() {
		return this.page_id;
	}
	
	public void setPage_id(Integer id) {
		this.page_id = id;
	}
	
	// title
	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	// category id
	public Integer getPage_category_id() {
		return page_category_id;
	}


	public void setPage_category_id(Integer page_category_id) {
		this.page_category_id = page_category_id;
	}	

	// created
	public String getPage_created() {
		return page_created;
	}
	
	public void setPage_created(String created) {
		this.page_created = created;
	}
	
}
