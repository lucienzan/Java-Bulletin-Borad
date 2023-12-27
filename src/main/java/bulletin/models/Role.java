package bulletin.models;

import java.util.ArrayList;
import java.util.List;

public class Role {
	private String Id;
	private String Name;
	private List<Role> RoleList;

	public Role() {
		List<Role> roles = new ArrayList<Role>();
		roles.add(new Role("056e4f7d-927a-498c-898a-5dbe68480c1c", "Admin"));
		roles.add(new Role("06e3fda9-3b4a-4393-8c9c-b454851d98fa", "User"));
		this.setRoleList(roles);
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

	public List<Role> getRoleList() {
		return RoleList;
	}

	public void setRoleList(List<Role> roles) {
		RoleList = roles;
	}

	public Role(String id, String name) {
		Id = id;
		Name = name;
	}

}
