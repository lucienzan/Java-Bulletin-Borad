package bulletin.dao.Repositories;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
		try {
			sqlQuery = "SELECT * FROM user LEFT JOIN role ON user.RoleId = role.Id WHERE user.Active = true AND user.DeleteFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getString("Id"));
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				user.setFullName(resultSet.getString("FirstName") + " " + lastName);
				user.setEmail(resultSet.getString("Email"));
				user.setRoleId(resultSet.getString("Name"));
				user.setAddress(resultSet.getString("Address"));
				user.setPhone(resultSet.getString("Phone"));
				user.setDOB(resultSet.getTimestamp("DOB"));
				user.setActive(resultSet.getBoolean("Active"));
				user.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
				userList.add(user);
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
		try {
			sqlQuery = "SELECT * FROM user LEFT JOIN role ON user.RoleId = role.Id WHERE user.Id = ? AND user.DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				model.setFullName(resultSet.getString("FirstName") + " " + lastName);
				model.setFirstName(resultSet.getString("FirstName"));
				model.setLastName(lastName);
				model.setEmail(resultSet.getString("Email"));
				model.setProfile(resultSet.getString("Profile"));
				model.setDOB(resultSet.getTimestamp("DOB"));
				model.setPhone(resultSet.getString("Phone"));
				model.setRoleId(resultSet.getString("Name"));
				model.setAddress(resultSet.getString("Address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public ResponseModel Create(User obj) {
		ResponseModel model = new ResponseModel();

		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String password = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt(12));
		List<Role> roleList = new ArrayList<Role>();

		try {
			sqlQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
			}
			
			sqlQuery = "SELECT COUNT(*) FROM user WHERE Email = ?";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getEmail());

			resultSet = preparedStatement.executeQuery();
			resultSet.next();

			int count = resultSet.getInt(1);
			if (count > 0) {
				
				// Email already exists, handle accordingly
				model.setRoles(roleList);
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
					model.setRoles(roleList);
					model.setMessageName(Message.AccountSuccess);
					model.setMessageType(Message.SUCCESS);
				}
			}

		} catch (SQLException e) {
			model.setRoles(roleList);
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
				
				if(fileName != null && fileName != "user.png") {
					String filePath = "D:\\Java\\Java EE\\BulletinOJT\\src\\main\\webapp\\assets\\img\\profile";
					
		        	File fileUploadDirectory = new File(filePath + "\\" + fileName);
		        	if(fileUploadDirectory.exists()) {
		        		Path fileToDelete = Paths.get(filePath + "\\" + fileName);
			            // Delete the file
			            Files.delete(fileToDelete);
		        	}
				}
			}

		} catch (Exception e) {
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return model;
	}
}