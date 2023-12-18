package bulletin.models;

public class Role {
	private String Id;
	private String Name;
	
	public Role() {}
	
	public Role(String id, String name) {
		Id = id;
		Name = name;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
}
