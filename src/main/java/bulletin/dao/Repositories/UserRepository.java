package bulletin.dao.Repositories;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bulletin.common.BCrypt;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IUserRepository;
import bulletin.models.ResponseModel;
import bulletin.models.Role;
import bulletin.models.User;

public class UserRepository implements IUserRepository {

	private String sqlQuery = "";

	public List<User> GetAll() {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<User> userList = new ArrayList<User>();
		Role roleModel = new Role();
		Map<String, String> roleIdToNameMap = new HashMap<>();
		
		try {
			sqlQuery = "SELECT * FROM user WHERE user.Active = true AND user.DeleteFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getString("Id"));
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				user.setFullName(resultSet.getString("FirstName") + " " + lastName);
				user.setEmail(resultSet.getString("Email"));
			    String roleId = resultSet.getString("RoleId");
				user.setRoleId(roleId);
				user.setAddress(resultSet.getString("Address"));
				user.setPhone(resultSet.getString("Phone"));
				user.setDOB(resultSet.getTimestamp("DOB"));
				user.setActive(resultSet.getBoolean("Active"));
				user.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
				userList.add(user);
				
				 // Add role ID and name to the map if not already present
			    if (!roleIdToNameMap.containsKey(roleId)) {
			    	for (Role role : roleModel.getRoleList() ) {
				        if(role.getId().contentEquals(roleId)) {
				        	roleIdToNameMap.put(roleId, role.getName());
				        }
					}
			    }
			}
			
			if(userList.size() > 0) {
				for (User user : userList) {
					 String roleId = user.getRoleId();
					    if (roleIdToNameMap.containsKey(roleId)) {
					        user.setRoleId(roleIdToNameMap.get(roleId));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, statement, resultSet);
		return userList;
	}

	public User Get(String id) {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		User model = new User();
		List<Role> roleList = new ArrayList<Role>();

		try {
			Role roleModel = new Role();
			roleList = roleModel.getRoleList();
			
			sqlQuery = "SELECT * FROM user WHERE user.Id = ? AND user.DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				model.setId(resultSet.getString("Id"));
				model.setFullName(resultSet.getString("FirstName") + " " + lastName);
				model.setFirstName(resultSet.getString("FirstName"));
				model.setLastName(lastName);
				model.setEmail(resultSet.getString("Email"));
				model.setProfile(resultSet.getString("Profile"));
				model.setOldProfile(resultSet.getString("Profile"));
				model.setDOB(resultSet.getTimestamp("DOB"));
				model.setPhone(resultSet.getString("Phone"));
				model.setRoleName(resultSet.getString("RoleId"));
				model.setRoleId(resultSet.getString("RoleId"));
				model.setAddress(resultSet.getString("Address"));
				model.setRoleList(roleList);
			}
			
			for (Role role : roleList) {
				if(model.getRoleId() != null) {
					if(model.getRoleId().contentEquals(role.getId())) {
						model.setRoleName(role.getName());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return model;
	}
	
	public ResponseModel Create(User obj) {
		ResponseModel model = new ResponseModel();

		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String password = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt(12));

		try {
			sqlQuery = "SELECT COUNT(*) FROM user WHERE Email = ?";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getEmail());

			resultSet = preparedStatement.executeQuery();
			resultSet.next();

			int count = resultSet.getInt(1);
			if (count > 0) {
				
				// Email already exists, handle accordingly
				model.setMessageName(Message.AccountExist);
				model.setMessageType(Message.EXIST);

			} else {
				sqlQuery = "INSERT INTO user (Id,FirstName,LastName,Email,Password,Address,Profile,Phone,RoleId,DOB,Active,CreatedUserId,CreatedDate,DeleteFlag)";
				sqlQuery += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getId());
				preparedStatement.setString(2, obj.getFirstName());
				preparedStatement.setString(3, obj.getLastName());
				preparedStatement.setString(4, obj.getEmail());
				preparedStatement.setString(5, password);
				preparedStatement.setString(6, obj.getAddress());
				preparedStatement.setString(7, obj.getProfile());
				preparedStatement.setString(8, obj.getPhone());
				preparedStatement.setString(9, obj.getRoleId());
				preparedStatement.setTimestamp(10, obj.getDOB());
				preparedStatement.setBoolean(11, true);
				preparedStatement.setString(12, obj.getCreatedUserId());
				preparedStatement.setTimestamp(13, obj.getCreatedDate());
				preparedStatement.setBoolean(14, false);

				int result = preparedStatement.executeUpdate();

				if (result == Message.SUCCESS) {
					model.setMessageName(Message.CreateSuccess);
					model.setMessageType(Message.SUCCESS);
				}
			}

		} catch (SQLException e) {
			model.setMessageName(Message.AccountFail);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);

		return model;
	}

	public ResponseModel Update(User obj) {
		ResponseModel model = new ResponseModel();

		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean isExist = false;
		
		try {
			sqlQuery = "SELECT * FROM user WHERE Email = ? AND Id != ? AND DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getEmail());
			preparedStatement.setString(2, obj.getId());
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				String count = resultSet.getString("Id");
			    if (count.length() > 0) {
			    	isExist = true;
			    	model.setMessageName(Message.AccountExist);
					model.setMessageType(Message.EXIST);
			    }
			};
			
			sqlQuery = "SELECT * FROM user WHERE Id = ? AND DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getId());
			resultSet = preparedStatement.executeQuery();
			String oldFile = null;
			if(resultSet.next()) {
				oldFile = resultSet.getString("Profile");
			};
			
			if(isExist == false) {
				sqlQuery = "UPDATE user SET FirstName = ?, LastName = ?, Email = ?, Address = ?, Profile = ?, Phone = ?, RoleId = ?";
				sqlQuery += ", DOB = ?, Active = ?, UpdatedUserId = ?, UpdatedDate = ?, DeleteFlag = ? WHERE Id = ?";
				if(!obj.getOldProfile().contentEquals(obj.getProfile()) && !obj.getProfile().contentEquals("user.png")){
					DeleteFile(oldFile);
				}
				
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getFirstName());
				preparedStatement.setString(2, obj.getLastName());
				preparedStatement.setString(3, obj.getEmail());
				preparedStatement.setString(4, obj.getAddress());
				if(!oldFile.contentEquals(obj.getProfile()) && !obj.getProfile().contentEquals("user.png")){
					preparedStatement.setString(5, obj.getProfile());
				}else {
					preparedStatement.setString(5, oldFile);
				}
				preparedStatement.setString(6, obj.getPhone());
				preparedStatement.setString(7, obj.getRoleId());
				preparedStatement.setTimestamp(8, obj.getDOB());
				preparedStatement.setBoolean(9, true);
				preparedStatement.setString(10, obj.getUpdatedUserId());
				preparedStatement.setTimestamp(11, obj.getUpdatedDate());
				preparedStatement.setBoolean(12, false);
				preparedStatement.setString(13, obj.getId());
				int result = preparedStatement.executeUpdate();

				if (result == Message.SUCCESS) {
					model.setMessageName(Message.UpdateSuccess);
					model.setMessageType(Message.SUCCESS);
				}
			}

		} catch (SQLException | IOException e) {
			model.setMessageName(Message.AccountFail);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);

		return model;
	}
	
	public ResponseModel Delete(String id,String currentUser) {
		ResponseModel model = new ResponseModel();
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			sqlQuery = "SELECT * FROM user WHERE Id = ? ";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			
			String fileName  = null;
			while (resultSet.next()) {
				fileName = resultSet.getString("Profile");
			}
			
            Timestamp deleteDate = new Timestamp(System.currentTimeMillis());
			sqlQuery = "UPDATE user SET Profile = ?, DeleteFlag = ?, DeletedUserId = ?, DeletedDate = ? WHERE Id = ? ";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, "user.png");
			preparedStatement.setBoolean(2, true);
			preparedStatement.setString(3, currentUser);
			preparedStatement.setTimestamp(4, deleteDate);
			preparedStatement.setString(5, id);
			int result = preparedStatement.executeUpdate();
			
			if (result == Message.SUCCESS) {
				model.setMessageType(Message.SUCCESS);
				model.setMessageName(Message.AccountDelete);
				
				DeleteFile(fileName);
			}

		} catch (Exception e) {
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return model;
	}
	
	private boolean DeleteFile(String fileName) throws IOException {
		if(fileName != null && fileName != "user.png" && !fileName.isEmpty()) {
			String filePath = "D:\\Java\\Java EE\\BulletinOJT\\src\\main\\webapp\\assets\\img\\profile";
			
        	File fileUploadDirectory = new File(filePath + "\\" + fileName);
        	if(fileUploadDirectory.exists()) {
        		Path fileToDelete = Paths.get(filePath + "\\" + fileName);
	            // Delete the file
	            Files.delete(fileToDelete);
	            return true;
        	}
		}
		return false;
	}
}
