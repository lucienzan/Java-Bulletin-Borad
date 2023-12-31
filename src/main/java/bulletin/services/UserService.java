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
	
	public User Get(String id){
		User model = _useRepository.Get(id);
		return model;
	}
	
	public ResponseModel Create(User obj){
		ResponseModel model = _useRepository.Create(obj);
		 return model;
	}
	
	public ResponseModel Update(User obj,String path){
		ResponseModel model = _useRepository.Update(obj,path);
		 return model;
	}
	
	public ResponseModel Delete(String id,String userId,String path){
		ResponseModel model = _useRepository.Delete(id,userId,path);
		 return model;
	}
}
