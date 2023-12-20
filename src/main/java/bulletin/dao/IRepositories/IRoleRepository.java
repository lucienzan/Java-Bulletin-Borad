package bulletin.dao.IRepositories;

import bulletin.models.Role;

public interface IRoleRepository {
	public Role GetAll();
	public Role GetRole(String id);
	public Role Create(Role obj);
	public Role Update(Role obj);
	public Role Delete(Role obj);
}
