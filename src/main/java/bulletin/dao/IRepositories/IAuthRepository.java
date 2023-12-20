package bulletin.dao.IRepositories;

import bulletin.models.ResponseModel;
import bulletin.models.User;

public interface IAuthRepository {
	public ResponseModel Register(User model);
	public ResponseModel Login(User model);
	public ResponseModel ChangePassword (User model);
}
