package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IUserRepository;
import bulletin.models.ResponseModel;
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
			
			while(resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getString("Id"));
				String lastName =  resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				user.setFullName(resultSet.getString("FirstName")+" "+ lastName);
				user.setEmail(resultSet.getString("Email"));
				user.setRoleId(resultSet.getString("Name"));
				user.setAddress(resultSet.getString("Address"));
				user.setPhone(resultSet.getString("Phone"));
				user.setDOB(resultSet.getDate("DOB"));
				user.setActive(resultSet.getBoolean("Active"));
				user.setCreatedDate(resultSet.getDate("CreatedDate"));
				userList.add(user);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		DbConnection.CloseConnection(con, statement, resultSet);
		return userList;
	}

	public ResponseModel Delete(String id) {
		ResponseModel model = new ResponseModel();
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			sqlQuery = "UPDATE user SET DeleteFlag = ? WHERE Id = ? ";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setString(2, id);
			
			int result = preparedStatement.executeUpdate();
			if (result == Message.SUCCESS) {
				model.setMessageType(Message.SUCCESS);
				model.setMessageName(Message.AccountDelete);
			}
			
		} catch (Exception e) {
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
			e.printStackTrace();
		}
		
		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return model;
	}
	
	public ResponseModel Create(User obj) {
		return null;
	}
}
