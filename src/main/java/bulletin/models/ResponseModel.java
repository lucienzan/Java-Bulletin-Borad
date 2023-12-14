package bulletin.models;

public class ResponseModel {
		
	private int MessageType;
	private String MessageName;
	private User UserModel;
	
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
}
