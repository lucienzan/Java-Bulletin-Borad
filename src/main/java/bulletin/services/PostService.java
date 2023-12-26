package bulletin.services;

import java.util.List;

import bulletin.dao.IRepositories.IPostRepository;
import bulletin.dao.Repositories.PostRepository;
import bulletin.models.Post;
import bulletin.models.ResponseModel;

public class PostService {
private IPostRepository _repository;

public PostService(PostRepository postSeRepository) {
	_repository = postSeRepository;
}

public List<Post> GetAll(){
	List<Post> postList = _repository.GetAll();
	return postList;
}

public Post Get(String id){
	Post model = _repository.Get(id);
	return model;
}

public ResponseModel Create(Post obj)
{
	ResponseModel model = _repository.Create(obj);
	return model;
}

public ResponseModel Update(Post obj)
{
	ResponseModel model = _repository.Update(obj);
	return model;
}

public ResponseModel Delete(String postId, String userId)
{
	ResponseModel model = _repository.Delete(postId,userId);
	return model;
}
}
