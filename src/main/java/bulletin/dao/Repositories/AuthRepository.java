package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bulletin.common.BCrypt;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IAuthRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;

public class AuthRepository implements IAuthRepository {

	private String sqlQuery = "";

	public ResponseModel Register(User obj) {
		ResponseModel model = new ResponseModel();

		DbConnection.GetInstance();
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
				sqlQuery = "INSERT INTO user (Id,FirstName,Email,Password,CreatedUserId,CreatedDate,DeleteFlag) ";
				sqlQuery += "VALUES (?,?,?,?,?,?,?)";

				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getId());
				preparedStatement.setString(2, obj.getFirstName());
				preparedStatement.setString(3, obj.getEmail());
				preparedStatement.setString(4, obj.getPassword());
				preparedStatement.setString(5, obj.getCreatedUserId());
				preparedStatement.setDate(6, obj.getCreatedDate());
				preparedStatement.setBoolean(7, false);

				int result = preparedStatement.executeUpdate();
				System.out.println(result);

				if (result == Message.SUCCESS) {
					model.setMessageName(Message.AccountSuccess);
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
		
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			sqlQuery = "SELECT * FROM user WHERE Email = ?";
			
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getEmail());

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				user.setId(resultSet.getString("Id"));
				user.setEmail(resultSet.getString("Email"));
				user.setFirstName(resultSet.getString("FirstName"));
				user.setDeletedFlag(resultSet.getBoolean("DeleteFlag"));
				user.setActive(resultSet.getBoolean("Active"));
				
				if(user.isDeletedFlag()) {
					model.setMessageName(Message.AccountNotFound);
					model.setMessageType(Message.EXIST);
				}else if(!user.isActive())
				{
					model.setMessageName(Message.NotActive);
					model.setMessageType(Message.FAIL);
				}else {
					boolean matched = BCrypt.checkpw(obj.getPassword(), resultSet.getString("Password"));
					if(matched) {
						model.setUserModel(user);
						model.setMessageName(Message.LoginSuccess);
						model.setMessageType(Message.SUCCESS);
					}else {
						model.setMessageName(Message.IsAccountMatch);
						model.setMessageType(Message.FAIL);
					}
				}
			}
		}catch (Exception e) {
			model.setMessageName(Message.LoginFail);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		    DbConnection.CloseConnection(con, preparedStatement,resultSet);
		
		return model;
	}
}
