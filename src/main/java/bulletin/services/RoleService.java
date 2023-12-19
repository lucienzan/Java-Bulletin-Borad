package bulletin.services;
import bulletin.dao.IRepositories.IRoleRepository;
import bulletin.dao.Repositories.RoleRepository;
import bulletin.models.ResponseModel;
import bulletin.models.Role;

public class RoleService {
	private IRoleRepository _roleRepository;
	
	public RoleService(RoleRepository repository) {
		this._roleRepository = repository;
	}
	
	public Role GetAll(){
		Role role = _roleRepository.GetAll();
		return role;
	}
	
	public Role Create(Role obj){
		Role model = _roleRepository.Create(obj);
		return model;
	}
	
	public ResponseModel Delete(Role obj) {
		ResponseModel model = _roleRepository.Delete(obj);
		return model;
	}
}
