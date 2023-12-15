package bulletin.models;

import java.sql.Date;

public class User {
	
	public User() {
		this.Email = "";
	};
	
	public User(String email,String password,String confirmPassword) {
		this.Email = email;
		this.Password = password;
		this.ConfirmPassword = confirmPassword;
	}
	
	public User(String email,String password) {
		this.Email = email;
		this.Password = password;
	}
	
	private String Id;
	private String FirstName;
	private String LastName;
	private String Email;
	private String Password;
	private String ConfirmPassword;
	private String Phone;
	private Date DOB;
	private boolean Active;
	private String CreatedUserId;
	private Date CreatedDate;
	private String UpdatedUserId;
	private Date UpdatedDate;
	private String DeletedUserId;
	private Date DeletedDate;
	private boolean DeletedFlag;
		
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public String getFirstName() {
		return FirstName;
	}
	
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	
	public String getLastName() {
		return LastName;
	}
	
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String password) {
		Password = password;
	}
	
	public String getConfirmPassword() {
		return ConfirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		ConfirmPassword = confirmPassword;
	}
	
	public String getPhone() {
		return Phone;
	}
	
	public void setPhone(String phone) {
		Phone = phone;
	}
	
	public Date getDOB() {
		return DOB;
	}
	
	public void setDOB(Date dOB) {
		DOB = dOB;
	}
	
	public String getCreatedUserId() {
		return CreatedUserId;
	}
	
	public void setCreatedUserId(String createdUserId) {
		CreatedUserId = createdUserId;
	}
	
	public Date getCreatedDate() {
		return CreatedDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}
	
	public String getUpdatedUserId() {
		return UpdatedUserId;
	}
	
	public void setUpdatedUserId(String updatedUserId) {
		UpdatedUserId = updatedUserId;
	}
	
	public Date getUpdatedDate() {
		return UpdatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		UpdatedDate = updatedDate;
	}
	
	public String getDeletedUserId() {
		return DeletedUserId;
	}
	
	public void setDeletedUserId(String deletedUserId) {
		DeletedUserId = deletedUserId;
	}
	
	public Date getDeletedDate() {
		return DeletedDate;
	}
	
	public void setDeletedDate(Date deletedDate) {
		DeletedDate = deletedDate;
	}
	
	public boolean isDeletedFlag() {
		return DeletedFlag;
	}
	
	public void setDeletedFlag(boolean deletedFlag) {
		DeletedFlag = deletedFlag;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}
}
