package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bulletin.common.Message;
import bulletin.dao.Repositories.PostRepository;
import bulletin.models.Post;
import bulletin.models.ResponseModel;
import bulletin.models.User;
import bulletin.services.PostService;

public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private PostService _postService;

    public PostController() {
        super();
    }

    public void init() {
    	_postService = new PostService(new PostRepository());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if(action != null) {
			if(action.contentEquals("postCreate")) {
				this.CreatePost(request,response);
			}
		}
		
		String id = request.getParameter("postId");
		if(id != null) {
			this.GetPost(request,response);
		}
			this.GetAll(request,response);
	}

	private void GetAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		List<Post> postList = _postService.GetAll();
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(postList);

		// Send the JSON response to the client
		response.getWriter().write(json);
	}
	
	private void GetPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("postId");
		Post post = _postService.Get(id);
		request.setAttribute("post", post);
		request.getRequestDispatcher("/Views/Post/edit.jsp").forward(request, response);
	}

	private void CreatePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/Views/Post/create.jsp").forward(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if(id != null) {
			this.UpdatePost(request,response);
		}else {
			this.InsertPost(request,response);
		}
	}
	
	private void UpdatePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean hasError = Validation(request);
		if(hasError) {
			request.getRequestDispatcher("/Views/Post/edit.jsp").include(request, response);
		}else {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
			Post post = new Post();
			
			post.setId(request.getParameter("id"));
			post.setTitle(request.getParameter("title"));
			post.setDescription(request.getParameter("description"));
			boolean isPublished = request.getParameter("isPublished") != null ? true : false;
			post.setIsPublished(isPublished);
			post.setUpdatedDate(updatedDate);
			post.setUpdatedUserId(user.getId());

			ResponseModel model = _postService.Update(post);
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Post/edit.jsp").forward(request, response);
		}
	}

	
	private void InsertPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean hasError = Validation(request);
		if(hasError) {
			request.getRequestDispatcher("/Views/Post/create.jsp").include(request, response);
		}else {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			String id = UUID.randomUUID().toString();
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			boolean isPublished = request.getParameter("isPublished") != null ? true : false;
			Timestamp createdDate = new Timestamp(System.currentTimeMillis());
			ResponseModel model = _postService.Create(new Post(id,title,description,isPublished,createdDate,user.getId()));
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Post/create.jsp").forward(request, response);
		}
	}
	
	private boolean Validation(HttpServletRequest request) {
		boolean hasError = false;
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		
		// Title
		if(title.isBlank()) {
			request.setAttribute("titleError", Message.RTitle);
			hasError = true;
		}else if(title.length() > 150) {
			request.setAttribute("titleError", Message.LTitle);
			hasError = true;
		}
		
		// Description
		if(description.isBlank()) {
			request.setAttribute("describeError", Message.RDescription);
			hasError = true;
		}else if(description.length() > 350) {
			request.setAttribute("describeError", Message.LDescription);
			hasError = true;
		}
		
		return hasError;
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (BufferedReader reader = request.getReader()) {
			StringBuilder requestBody = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				requestBody.append(line);
			}

			JSONObject jsonData = new JSONObject(requestBody.toString());
			String postId = jsonData.getString("id");
     		HttpSession session = request.getSession(false);
     		User user= (User) session.getAttribute("userManager");

			ResponseModel model = _postService.Delete(postId,user.getId());

			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("status", model.getMessageType());
			jsonResponse.put("message", model.getMessageName());
			response.setContentType("application/json");
			response.getWriter().write(jsonResponse.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
