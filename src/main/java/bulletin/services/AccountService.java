package bulletin.services;

import bulletin.dao.IRepositories.IAuthRepository;
import bulletin.dao.Repositories.AuthRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;

public class AccountService {

	private IAuthRepository _authRepository;
	public AccountService(AuthRepository authRepository)
	{
		this._authRepository = authRepository;
	}
	
	public ResponseModel Register(User obj) {
		ResponseModel model = _authRepository.Register(obj);
		 return model;
	}

	public ResponseModel Login(User obj) {
		ResponseModel model = _authRepository.Login(obj);
		 return model;
	}
	
	public ResponseModel ChangePassword(User obj) {
		ResponseModel model = _authRepository.ChangePassword(obj);
		 return model;
	}
	
	public ResponseModel CheckEmail(String email) {
		ResponseModel model = _authRepository.CheckEmail(email);
		 return model;
	}
	
	public ResponseModel ResetPassword(String email, String password) {
		ResponseModel model = _authRepository.ResetPassword(email,password);
		 return model;
	}
	
}
