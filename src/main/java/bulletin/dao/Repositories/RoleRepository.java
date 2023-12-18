package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import bulletin.common.DbConnection;
import bulletin.dao.IRepositories.IRoleRepository;
import bulletin.models.Role;

public class RoleRepository implements IRoleRepository {
	private String sqlQuery = "";
	
	public List<Role> GetAll() {
		DbConnection.GetInstance();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		List<Role> roleList = new ArrayList<Role>();
		try {
			sqlQuery = "SELECT * FROM role WHERE DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				roleList.add(new Role(id,name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbConnection.CloseConnection(con, statement, resultSet);
		return roleList;
	}

}
