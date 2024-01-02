package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IAuthRepository;
import bulletin.models.ResponseModel;
import bulletin.models.Role;
import bulletin.models.User;

public class AuthRepository implements IAuthRepository {

	private String sqlQuery = "";

	public ResponseModel Register(User obj) {
		ResponseModel model = new ResponseModel();
		Role role =  new Role();
		var userRoleId = role.getRoleList().stream()
                .filter(r -> "User".equals(r.getName()))
                .map(Role::getId)
                .findFirst()
                .orElse(null);

		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

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
				sqlQuery = "INSERT INTO user (Id,FirstName,LastName,Email,Profile,RoleId,Password,CreatedUserId,CreatedDate,DeleteFlag) ";
				sqlQuery += "VALUES (?,?,?,?,?,?,?,?,?,?)";
				String generatedSecuredPasswordHash = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt(12));

				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getId());
				preparedStatement.setString(2, obj.getFirstName());
				preparedStatement.setString(3, obj.getLastName());
				preparedStatement.setString(4, obj.getEmail());
				preparedStatement.setString(5, "user.png");
				preparedStatement.setString(6, userRoleId);
				preparedStatement.setString(7, generatedSecuredPasswordHash);
				preparedStatement.setString(8, obj.getCreatedUserId());
				preparedStatement.setTimestamp(9, obj.getCreatedDate());
				preparedStatement.setBoolean(10, false);
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
		    DbConnection.CloseConnection(con, preparedStatement,resultSet);
		    
		return model;
	}

	public ResponseModel Login(User obj) {
		ResponseModel model = new ResponseModel();
		User user = new User();
		Role role = new Role();
		
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			sqlQuery = "SELECT * FROM user WHERE Email = ?";
			
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getEmail());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				user.setId(resultSet.getString("Id"));
				user.setEmail(resultSet.getString("Email"));
				user.setFullName(resultSet.getString("FirstName") + " " + lastName);
				user.setDeletedFlag(resultSet.getBoolean("DeleteFlag"));
				user.setActive(resultSet.getBoolean("Active"));
				user.setRoleId(resultSet.getString("RoleId"));
				user.setPassword(resultSet.getString("Password"));
				var roleName = role.getRoleList().stream()
		                .filter(r -> user.getRoleId().equals(r.getId()))
		                .map(Role::getName)
		                .findFirst()
		                .orElse(null);
				user.setRoleName(roleName);
			}
			
				if(user.isDeletedFlag() || user.getId() == null) {
					model.setMessageName(Message.AccountNotFound);
					model.setMessageType(Message.EXIST);
				}else if(!user.isActive())
				{
					model.setMessageName(Message.NotActive);
					model.setMessageType(Message.FAIL);
				}else {

					boolean matched = BCrypt.checkpw(obj.getPassword(), user.getPassword());
					if(matched) {
						user.setPassword("");
						model.setUserModel(user);
						model.setMessageName(Message.LoginSuccess);
						model.setMessageType(Message.SUCCESS);
					}else {
						model.setMessageName(Message.IsAccountMatch);
						model.setMessageType(Message.FAIL);
					}
				}
			
		}catch (Exception e) {
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		    DbConnection.CloseConnection(con, preparedStatement,resultSet);
		
		return model;
	}

	public ResponseModel ChangePassword(User obj)
	{
		ResponseModel model = new ResponseModel();		
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			sqlQuery = "SELECT * FROM user WHERE ID = ? AND DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getId());
			resultSet = preparedStatement.executeQuery();
			boolean isVerify = false;
			while (resultSet.next()) {
				isVerify = BCrypt.checkpw(obj.getOldPassword(), resultSet.getString("Password"));
			}
			
			if(isVerify == false) {
				model.setMessageType(Message.EXIST);
				model.setMessageName(Message.OPasswordMatch);
			}else {
				sqlQuery = "UPDATE user SET Password = ? WHERE ID = ? AND DeleteFlag = false";
				preparedStatement = con.prepareStatement(sqlQuery);
				String hashPwd = BCrypt.hashpw(obj.getPassword(), BCrypt.gensalt(12));
				preparedStatement.setString(1, hashPwd);
				preparedStatement.setString(2, obj.getId());				
				preparedStatement.executeUpdate();
				
				model.setMessageType(Message.SUCCESS);
				model.setMessageName(Message.UpdateSuccess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);	
		return model;
	}

	public ResponseModel CheckEmail(String mail) {
		ResponseModel model = new ResponseModel();		
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			sqlQuery = "SELECT COUNT(*) FROM user WHERE Email = ? AND DeleteFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, mail);
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			
			int count = resultSet.getInt(1);
			if (count == 0) {
				model.setMessageName(Message.AccountNotFound);
				model.setMessageType(Message.FAIL);
			}else {
				model.setMessageName(Message.SendSuccess);
				model.setMessageType(Message.SUCCESS);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return model;
	}

	public ResponseModel ResetPassword(String mail,String passwrod) {
		ResponseModel model = new ResponseModel();		
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		
		try {
			String hashPwd =BCrypt.hashpw(passwrod, BCrypt.gensalt(12));
			sqlQuery = "UPDATE user SET Password = ? WHERE Email = ?";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, hashPwd);
			preparedStatement.setString(2, mail);
			preparedStatement.executeUpdate();
			
			model.setMessageType(Message.SUCCESS);
			model.setMessageName(Message.UpdateSuccess);

		} catch (Exception e) {
			e.printStackTrace();
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
		}
		DbConnection.CloseConnection(con, preparedStatement);
		return model;
	}
}
