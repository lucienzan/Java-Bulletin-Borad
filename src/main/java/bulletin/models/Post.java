package bulletin.models;

import java.sql.Timestamp;

public class Post {
	private String Id;
	private String Title;
	private String Description;
	private boolean IsPublished;
	private boolean DeletedFlag;
	private Timestamp CreatedDate;
	private String CreatedUserId;
	private Timestamp UpdatedDate;
	private String UpdatedUserId;
	private Timestamp DeletedDate;
	private String DeletedUserId;
	
	public Post() {}
	
	public Post(String id, String title, String description, Boolean isPublished, Timestamp createdDate, String createdId ) {
		this.Id = id;
		this.Title = title;
		this.Description = description;
		this.IsPublished = isPublished;
		this.CreatedDate = createdDate;
		this.CreatedUserId = createdId;
	}
	
	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public String getTitle() {
		return Title;
	}
	
	public void setTitle(String title) {
		Title = title;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String description) {
		Description = description;
	}
	
	public boolean isIsPublished() {
		return IsPublished;
	}
	
	public void setIsPublished(boolean isPublished) {
		IsPublished = isPublished;
	}
	
	public boolean isDeletedFlag() {
		return DeletedFlag;
	}
	
	public void setDeletedFlag(boolean deletedFlag) {
		DeletedFlag = deletedFlag;
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
}
