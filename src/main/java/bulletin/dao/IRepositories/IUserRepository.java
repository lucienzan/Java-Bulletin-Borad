package bulletin.dao.IRepositories;

import java.util.List;
import bulletin.models.ResponseModel;
import bulletin.models.User;

public interface IUserRepository {
	public List<User> GetAll();
	public User Get(String id);
	public ResponseModel Create(User obj);
	public ResponseModel Update(User obj, String filePath);
	public ResponseModel Delete(String id,String currentUser,String filePath);
}
