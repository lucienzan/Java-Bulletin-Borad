package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IRoleRepository;
import bulletin.models.ResponseModel;
import bulletin.models.Role;

public class RoleRepository implements IRoleRepository {
	private String sqlQuery = "";
	
	public Role GetAll() {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Role role = new Role();
		List <Role> roleList = new ArrayList<Role>();
		
		try {
			sqlQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
				role.setRoleList(roleList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, statement, resultSet);
		return role;
	}

	public Role GetRole(String roleId) {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Role role = new Role();
		List <Role> roleList = new ArrayList<Role>();
		
		try {
			sqlQuery = "SELECT * FROM role WHERE Id = ? AND DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			statement.setString(1, roleId);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				role.setId(id);
				role.setName(name);
			}
			
			sqlQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
				role.setRoleList(roleList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, statement, resultSet);
		return role;
	}

	public Role Create(Role obj) {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Role role = new Role();
		ResponseModel model = new ResponseModel();
		List<Role> roleList = new ArrayList<Role>();

		boolean isExist = false;

		try {
			String roleQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			preparedStatement = con.prepareStatement(roleQuery);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
			}
			
			sqlQuery = "SELECT * FROM role WHERE Name = ? AND DeletedFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getName());
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				String count = resultSet.getString(1);
			    if (count.length() > 0) {
			    	isExist = true;
			    	role.setRoleList(roleList);
			        model.setMessageName(Message.RoleExist);
			        model.setMessageType(Message.EXIST);
			        role.setModel(model);
			    }
			};
			
			if(isExist == false) {
				roleList.clear();
				sqlQuery = "INSERT INTO role (Id,Name,CreatedUserId,CreatedDate,DeletedFlag) VALUES ";
				sqlQuery += "(?,?,?,?,?)";
				
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getId());
				preparedStatement.setString(2, obj.getName());
				preparedStatement.setString(3, obj.getCreatedUserId());
				preparedStatement.setTimestamp(4, obj.getCreatedDate());
				preparedStatement.setBoolean(5, obj.isDeletedFlag());
				int result = preparedStatement.executeUpdate();
				
				// Update role list
				preparedStatement = con.prepareStatement(roleQuery);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					String id = resultSet.getString("Id");
					String name = resultSet.getString("Name");
					roleList.add(new Role(id,name));
				}

				if (result == Message.SUCCESS) {
			    	role.setRoleList(roleList);
					model.setMessageName(Message.AccountSuccess);
					model.setMessageType(Message.SUCCESS);
			        role.setModel(model);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
			role.setRoleList(roleList);
	        role.setModel(model);
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return role;
	}
	
	public Role Update(Role obj) {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Role role = new Role();
		ResponseModel model = new ResponseModel();
		List<Role> roleList = new ArrayList<Role>();

		boolean isExist = false;

		try {
			String roleQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			preparedStatement = con.prepareStatement(roleQuery);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
			}
			
			sqlQuery = "SELECT * FROM role WHERE Name = ? AND Id != ? AND DeletedFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getName());
			preparedStatement.setString(2, obj.getId());
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				String count = resultSet.getString(1);
			    if (count.length() > 0) {
			    	isExist = true;
			    	role.setId(obj.getId());
			    	role.setName(obj.getName());
			    	role.setRoleList(roleList);
			        model.setMessageName(Message.RoleExist);
			        model.setMessageType(Message.EXIST);
			        role.setModel(model);
			    }
			};
			
			if(isExist == false) {
				roleList.clear();
				sqlQuery = "UPDATE role SET Name = ?, UpdatedUserId = ?, UpdatedDate = ? WHERE Id = ?";
				
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, obj.getName());
				preparedStatement.setString(2, obj.getUpdatedUserId());
				preparedStatement.setTimestamp(3, obj.getUpdatedDate());
				preparedStatement.setString(4, obj.getId());
				int result = preparedStatement.executeUpdate();
				
				// Update role list
				preparedStatement = con.prepareStatement(roleQuery);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					String id = resultSet.getString("Id");
					String name = resultSet.getString("Name");
					roleList.add(new Role(id,name));
				}

				if (result == Message.SUCCESS) {
			    	role.setRoleList(roleList);
					model.setMessageName(Message.UpdateSuccess);
					model.setMessageType(Message.SUCCESS);
			        role.setModel(model);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
	        role.setModel(model);
			role.setId(obj.getId());
	    	role.setName(obj.getName());
			role.setRoleList(roleList);
	        role.setModel(model);
		}
		DbConnection.CloseConnection(con, preparedStatement, resultSet);
		return role;
	}

	public Role Delete(Role obj) {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		ResponseModel model = new ResponseModel();
		ResultSet resultSet = null;
		Role role = new Role();
		List<Role> roleList = new ArrayList<Role>();
		
		try {
			sqlQuery = "UPDATE role SET DeletedUserId = ? , DeletedDate = ?, DeletedFlag = ? WHERE Id = ?";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setString(1, obj.getDeletedUserId());
			preparedStatement.setTimestamp(2, obj.getDeletedDate());
			preparedStatement.setBoolean(3, obj.isDeletedFlag());
			preparedStatement.setString(4, obj.getId());
			int result = preparedStatement.executeUpdate();
			
			sqlQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			preparedStatement = con.prepareStatement(sqlQuery);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
			}
			
			if (result == Message.SUCCESS) {
				model.setMessageType(Message.SUCCESS);
				model.setMessageName(Message.DeleteSuccess);
				role.setModel(model);
				role.setRoleList(roleList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
			role.setModel(model);
			role.setRoleList(roleList);
		}
		
		DbConnection.CloseConnection(con, preparedStatement,resultSet);
		return role;
	}
}
