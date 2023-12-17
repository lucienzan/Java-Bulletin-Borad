package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bulletin.dao.Repositories.UserRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;
import bulletin.services.UserService;


public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private UserService _userService;
	    
    public void init()
    {
    	this._userService = new UserService(new UserRepository());
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String contentType = request.getContentType();

		if(contentType == null ) {
	        request.getRequestDispatcher("/Views/User/user-list.jsp").forward(request, response);
		}else {
			response.setContentType("application/json");
			List<User> userList=  _userService.GetAll();
			Gson gson = new GsonBuilder().serializeNulls().create();
	        String json = gson.toJson(userList);
	        // Send the JSON response to the client
	        response.getWriter().write(json);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (BufferedReader reader = request.getReader()) {
	        StringBuilder requestBody = new StringBuilder();
	        String line;

	        while ((line = reader.readLine()) != null) {
	            requestBody.append(line);
	        }

	        JSONObject jsonData = new JSONObject(requestBody.toString());
	        String userId = jsonData.getString("userId");
	        ResponseModel model = _userService.Delete(userId);
	        
	        JSONObject jsonResponse = new JSONObject();
	        jsonResponse.put("status", model.getMessageType());
	        jsonResponse.put("message", model.getMessageName());
	        response.setContentType("application/json");
	        response.getWriter().write(jsonResponse.toString());

        }catch (Exception e) {
        	e.printStackTrace();
		}
	}
}
