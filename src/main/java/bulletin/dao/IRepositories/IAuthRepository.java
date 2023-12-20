package bulletin.dao.IRepositories;

import bulletin.models.ResponseModel;
import bulletin.models.User;

public interface IAuthRepository {
	public ResponseModel Register(User model);
	public ResponseModel Login(User model);
	public ResponseModel ChangePassword (User model);
	public ResponseModel CheckEmail(String mail);
	public ResponseModel ResetPassword(String mail,String password);
}
