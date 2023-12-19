package bulletin.models;

import java.sql.Timestamp;
import java.util.List;

public class Role {
	private String Id;
	private String Name;
	private Timestamp CreatedDate;
	private String CreatedUserId;
	private Timestamp UpdatedDate;
	private String UpdatedUserId;
	private Timestamp DeletedDate;
	private String DeletedUserId;
	private boolean DeletedFlag;
	private List<Role> RoleList;
	private ResponseModel model;
	
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

	public List<Role> getRoleList() {
		return RoleList;
	}

	public void setRoleList(List<Role> roleList) {
		RoleList = roleList;
	}
	
	public Timestamp getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		CreatedDate = createdDate;
	}

	public String getCreatedUserId() {
		return CreatedUserId;
	}

	public void setCreatedUserId(String createdUserId) {
		CreatedUserId = createdUserId;
	}

	public Timestamp getUpdatedDate() {
		return UpdatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		UpdatedDate = updatedDate;
	}

	public String getUpdatedUserId() {
		return UpdatedUserId;
	}

	public void setUpdatedUserId(String updatedUserId) {
		UpdatedUserId = updatedUserId;
	}

	public Timestamp getDeletedDate() {
		return DeletedDate;
	}

	public void setDeletedDate(Timestamp deletedDate) {
		DeletedDate = deletedDate;
	}

	public String getDeletedUserId() {
		return DeletedUserId;
	}

	public void setDeletedUserId(String deletedUserId) {
		DeletedUserId = deletedUserId;
	}

	public boolean isDeletedFlag() {
		return DeletedFlag;
	}

	public void setDeletedFlag(boolean deletedFlag) {
		DeletedFlag = deletedFlag;
	}

	public ResponseModel getModel() {
		return model;
	}

	public void setModel(ResponseModel model) {
		this.model = model;
	}
}
