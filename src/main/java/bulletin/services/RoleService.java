package bulletin.services;
import java.util.List;
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
}
