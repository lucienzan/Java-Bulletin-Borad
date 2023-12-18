package bulletin.services;
import java.util.List;
import bulletin.dao.IRepositories.IRoleRepository;
import bulletin.dao.Repositories.RoleRepository;
import bulletin.models.Role;

public class RoleService {
	private IRoleRepository _roleRepository;
	
	public RoleService(RoleRepository repository) {
		this._roleRepository = repository;
	}
	
	public List<Role> GetAll(){
		List<Role> roleList = _roleRepository.GetAll();
		return roleList;
	}
}
