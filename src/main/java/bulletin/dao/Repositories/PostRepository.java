package bulletin.dao.Repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import bulletin.common.DbConnection;
import bulletin.common.Message;
import bulletin.dao.IRepositories.IPostRepository;
import bulletin.models.Post;
import bulletin.models.ResponseModel;

public class PostRepository implements IPostRepository{
	private String sqlQuery = "";

	public List<Post> GetAll(){
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Post> postList = new ArrayList<Post>();
		
		try {
			sqlQuery = "SELECT * FROM post WHERE post.DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Post post = new Post();
				post.setId(resultSet.getString("Id"));
				post.setTitle(resultSet.getString("Title"));
				post.setDescription(resultSet.getString("Description"));
				post.setIsPublished(resultSet.getBoolean("IsPublished"));
				post.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
				post.setCreatedUserId(resultSet.getString("CreatedUserId"));
				postList.add(post);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, statement, resultSet);
		return postList;
	}

	public Post Get(String id){
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Post post = new Post();
		
		try {
			sqlQuery = "SELECT * FROM post INNER JOIN user ON post.CreatedUserId = user.Id WHERE post.Id = ? AND post.DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			statement.setString(1, id);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String lastName = resultSet.getString("LastName") == null ? "" : resultSet.getString("LastName");
				post.setId(resultSet.getString("Id"));
				post.setTitle(resultSet.getString("Title"));
				post.setDescription(resultSet.getString("Description"));
				post.setIsPublished(resultSet.getBoolean("IsPublished"));
				post.setCreatedDate(resultSet.getTimestamp("CreatedDate"));
				post.setCreatedUserId(resultSet.getString("FirstName") + " " + lastName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, statement, resultSet);
		return post;
	}
	
	public ResponseModel Create(Post obj) {
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResponseModel model = new ResponseModel();
		try {
			sqlQuery = "SELECT COUNT(*) FROM post WHERE Title = ? AND DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			statement.setString(1, obj.getTitle());
			resultSet = statement.executeQuery();
			resultSet.next();
			
		    if (resultSet.getInt(1) > 0) {
		    	model.setMessageName(Message.TitleExist);
				model.setMessageType(Message.EXIST);
		    } else {
		    	sqlQuery = "INSERT INTO post (Id,Title,Description,IsPublished,DeletedFlag,CreatedUserId,CreatedDate) VALUES (?,?,?,?,?,?,?)";
				statement = con.prepareStatement(sqlQuery);
				statement.setString(1, obj.getId());
				statement.setString(2, obj.getTitle());
				statement.setString(3, obj.getDescription());
				statement.setBoolean(4, obj.isIsPublished());
				statement.setBoolean(5, obj.isDeletedFlag());
				statement.setString(6, obj.getCreatedUserId());
				statement.setTimestamp(7, obj.getCreatedDate());
				int result = statement.executeUpdate();
				
				if (result == Message.SUCCESS) {
					model.setMessageName(Message.CreateSuccess);
					model.setMessageType(Message.SUCCESS);
				}
		    }
		} catch (Exception e) {
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		
		DbConnection.CloseConnection(con, statement, resultSet);
		return model;
	}
	
	public ResponseModel Update(Post obj) {
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResponseModel model = new ResponseModel();
		
		try {
			sqlQuery = "SELECT COUNT(*) FROM post WHERE Title = ? AND Id != ? AND DeletedFlag = false";
			statement = con.prepareStatement(sqlQuery);
			statement.setString(1, obj.getTitle());
			statement.setString(2, obj.getId());
			resultSet = statement.executeQuery();
			resultSet.next();
			
		    if (resultSet.getInt(1) > 0) {
		    	model.setMessageName(Message.AccountExist);
				model.setMessageType(Message.EXIST);
		    } else {
		    	sqlQuery = "UPDATE post SET Title = ?,Description = ?,IsPublished = ?,DeletedFlag = ?,UpdatedUserId = ?,UpdatedDate = ? WHERE Id = ?";
				statement = con.prepareStatement(sqlQuery);
				statement.setString(1, obj.getTitle());
				statement.setString(2, obj.getDescription());
				statement.setBoolean(3, obj.isIsPublished());
				statement.setBoolean(4, obj.isDeletedFlag());
				statement.setString(5, obj.getUpdatedUserId());
				statement.setTimestamp(6, obj.getUpdatedDate());
				statement.setString(7, obj.getId());
				
				int result = statement.executeUpdate();
				
				if (result == Message.SUCCESS) {
					model.setMessageName(Message.UpdateSuccess);
					model.setMessageType(Message.SUCCESS);
				}
		    }
		} catch (Exception e) {
			model.setMessageName(Message.SError);
			model.setMessageType(Message.FAIL);
			e.printStackTrace();
		}
		
		DbConnection.CloseConnection(con, statement, resultSet);
		return model;
	}

	public ResponseModel Delete(String id,String currentUser) {
		ResponseModel model = new ResponseModel();
		Connection con = DbConnection.GetDbConnection();
		PreparedStatement preparedStatement = null;
		
		try {
            Timestamp deleteDate = new Timestamp(System.currentTimeMillis());
			sqlQuery = "UPDATE post SET DeletedFlag = ?, DeletedUserId = ?, DeletedDate = ? WHERE Id = ?";
			preparedStatement = con.prepareStatement(sqlQuery);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setString(2, currentUser);
			preparedStatement.setTimestamp(3, deleteDate);
			preparedStatement.setString(4, id);
			int result = preparedStatement.executeUpdate();
			
			if (result == Message.SUCCESS) {
				model.setMessageType(Message.SUCCESS);
				model.setMessageName(Message.DeleteSuccess);				
			}

		} catch (Exception e) {
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.SError);
			e.printStackTrace();
		}

		DbConnection.CloseConnection(con, preparedStatement);
		return model;
	}
}
