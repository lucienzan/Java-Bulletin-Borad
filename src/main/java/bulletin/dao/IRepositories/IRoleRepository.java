package bulletin.dao.IRepositories;

import bulletin.models.ResponseModel;
import bulletin.models.Role;

public interface IRoleRepository {
	public Role GetAll();
	public Role Create(Role obj);
	public ResponseModel Delete(Role obj);
}
