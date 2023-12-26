package bulletin.dao.IRepositories;

import java.util.List;
import bulletin.models.Post;
import bulletin.models.ResponseModel;

public interface IPostRepository {
	public List<Post> GetAll();
	public Post Get(String id);
	public ResponseModel Create(Post obj);
	public ResponseModel Update(Post obj);
	public ResponseModel Delete(String postId,String userId);
}
