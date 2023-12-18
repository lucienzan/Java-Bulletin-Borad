package bulletin.models;

import java.sql.Date;

public class User {

	public User() {
		this.Email = "";
		this.Address = "";
	};

	public User(String email, String password, String confirmPassword) {
		this.Email = email;
		this.Password = password;
		this.ConfirmPassword = confirmPassword;
	}

	public User(String email, String password) {
		this.Email = email;
		this.Password = password;
	}

	public User(String id, String firstName, String lastName, String email, String password, String address,
			String profile, String phone, String role, Date dob, Date createdDate, String createdUserId) {
		this.Id = id;
		this.FirstName = firstName;
		this.LastName = lastName;
		this.Email = email;
		this.Password = password;
		this.Address = address;
		this.Profile = profile;
		this.Phone = phone;
		this.RoleId = role;
		this.DOB = dob;
		this.CreatedDate = createdDate;
		this.CreatedUserId = createdUserId;
	}

	private String Id;
	private String FirstName;
	private String LastName;
	private String FullName;
	private String Email;
	private String Password;
	private String ConfirmPassword;
	private String Address;
	private String Phone;
	private String RoleId;
	private Date DOB;
	private String Profile;
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

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
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

	public String getRoleId() {
		return RoleId;
	}

	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
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

	public String getProfile() {
		return Profile;
	}

	public void setProfile(String profile) {
		Profile = profile;
	}
}
