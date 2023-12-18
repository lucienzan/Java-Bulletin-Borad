package bulletin.services;

import java.util.List;
import bulletin.dao.IRepositories.IUserRepository;
import bulletin.dao.Repositories.UserRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;

public class UserService {
	
	private IUserRepository _useRepository;
	
	public UserService(UserRepository repository) {
		this._useRepository = repository;
	}
	
	public List<User> GetAll(){
		List<User> userList = _useRepository.GetAll();
		 return userList;
	}
	
	public ResponseModel Create(User obj){
		ResponseModel model = _useRepository.Create(obj);
		 return model;
	}
	
	public ResponseModel Delete(String id){
		ResponseModel model = _useRepository.Delete(id);
		 return model;
	}
}
