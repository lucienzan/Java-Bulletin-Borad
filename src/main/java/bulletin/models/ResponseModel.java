package bulletin.models;

import java.util.List;

public class ResponseModel {
	
	private int MessageType;
	private String MessageName;
	private User UserModel;
	private List<Role> Roles;
	
	public int getMessageType() {
		return MessageType;
	}
	
	public void setMessageType(int messageType) {
		MessageType = messageType;
	}
	
	public String getMessageName() {
		return MessageName;
	}
	
	public void setMessageName(String messageName) {
		MessageName = messageName;
	}

	public User getUserModel() {
		return UserModel;
	}

	public void setUserModel(User userModel) {
		UserModel = userModel;
	}

	public List<Role> getRoles() {
		return Roles;
	}

	public void setRoles(List<Role> roles) {
		Roles = roles;
	}
}
