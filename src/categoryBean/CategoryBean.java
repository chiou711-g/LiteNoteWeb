package categoryBean;

public class CategoryBean {
	
	private  Integer category_id;
	private  String category_name;

	// for being Java bean
	public CategoryBean() {
	}


	// id
	public Integer getCategory_id() {
		return this.category_id;
	}
	
	public void setCategory_id(Integer id) {
		this.category_id = id;
	}
	
	// name
	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

}
