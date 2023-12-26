package bulletin.controllers;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bulletin.common.Message;
import bulletin.dao.Repositories.UserRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;
import bulletin.services.UserService;

@WebServlet("/")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50)
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService _userService;

	public void init() {
		this._userService = new UserService(new UserRepository());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String contentType = request.getContentType();
		String url = request.getServletPath().toString();
		try {
			if (contentType == null && url.endsWith("UserController")) {
				request.getRequestDispatcher("/Views/User/user-list.jsp").forward(request, response);
			} else if (contentType != null && url.endsWith("UserController")) {
				this.GetUserList(request, response);
			} else if (url.endsWith("user-create")) {
				this.GetUser(request, response);
			} else if(url.endsWith("user-detail")) {
				this.GetUserDetail(request,response);
			} else if(url.endsWith("user-edit")) {
				this.EditUser(request,response);
			} else if(url.endsWith("profile")) {
				this.Profile(request,response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void GetUserDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		User user = _userService.Get(id);
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(user);
		
		response.getWriter().write(json);
	}
	
	private void Profile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String id = request.getParameter("userId");
		User user = _userService.Get(id);
		request.setAttribute("userModel", user);
		request.getRequestDispatcher("/Views/Account/profile.jsp").forward(request, response);
	}

	private void GetUserList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<User> userList = _userService.GetAll();
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(userList);

		// Send the JSON response to the client
		response.getWriter().write(json);
	}

	private void GetUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/Views/User/create.jsp").forward(request, response);
	}
	
	private void EditUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			String userId = request.getParameter("userId");
			User model = _userService.Get(userId);
			model.setProfile("");
			
			request.setAttribute("userModel", model);
			request.getRequestDispatcher("/Views/User/edit.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getServletPath().toString(); 
		if(url.endsWith("updateUser")) {
			this.UpdateUser(request, response);
		}else {
			this.CreateUser(request,response);
		}
	}
	
	private void CreateUser(HttpServletRequest request, HttpServletResponse response) {
		try {
            response.setContentType("text/html;charset=UTF-8");
            
        	 Part part = request.getPart("profile");
             String fileName = "user.png";
             
     		if(part != null && part.getSize() > 0) {
     			 fileName = ExtractFileName(part);
     			 boolean isAllowExtension =  CheckExtension(fileName);
     		     if(isAllowExtension == false) {
     		    	request.setAttribute("fileError", Message.FileTypeError);
     				request.getRequestDispatcher("/Views/User/create.jsp").forward(request, response);
     				return;
     		     }
     		}
     		
     		boolean errorExist = this.Validation(request, response);
     		
     		HttpSession session = request.getSession(false);
     		User user= (User) session.getAttribute("userManager");

    		if (errorExist) {
    			request.getRequestDispatcher("/Views/User/create.jsp").include(request, response);
    		} else {
    			String email = request.getParameter("email");
    			String firstName = request.getParameter("firstName");
    			String lastName = request.getParameter("lastName");
    			String roleId = request.getParameter("roleId");
    			String address = request.getParameter("address");
    			String phone = request.getParameter("phone");
    			String password = request.getParameter("password");
    			String profile = fileName;

    			UUID randomIdUuid = UUID.randomUUID();
	            Timestamp createdDate = new Timestamp(System.currentTimeMillis());
    			Timestamp dob = ConvertStringToTimestamp(request.getParameter("dob"));
    			String id = randomIdUuid.toString();

    			ResponseModel model = _userService.Create(new User(id, firstName, lastName, email, password, address,
    					profile, phone, roleId, dob, createdDate, user.getId()));
    			ServletContext context = getServletContext();  
    			String dir = context.getInitParameter("fileDir");  
    			
    			if(model.getMessageType() == Message.SUCCESS) {
    				
    		        if(!fileName.contentEquals("user.png")) {
    		        	File fileUploadDirectory = new File(dir);
        		        if (!fileUploadDirectory.exists()) {
        		            fileUploadDirectory.mkdirs();
        		        }
        		        
        		        String savePath = dir + File.separator + fileName;
        		        part.write(savePath);
    		        }
    		        
    		        request.setAttribute("model", model);
    				request.getRequestDispatcher("/Views/User/create.jsp").forward(request, response); 
    			}else {
    				request.setAttribute("model", model);
    				request.getRequestDispatcher("/Views/User/create.jsp").include(request, response); 
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void UpdateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
            response.setContentType("text/html;charset=UTF-8");
            
        	 Part part = request.getPart("profile");
             String fileName = "user.png";
             
 			String id = request.getParameter("id");
 			String isProfileRoute = request.getParameter("isProfileRoute");
             
     		if(part != null && part.getSize() > 0) {
     			 fileName = ExtractFileName(part);
     			 boolean isAllowExtension =  CheckExtension(fileName);
     		     if(isAllowExtension == false) {
     		    	request.setAttribute("fileError", Message.FileTypeError);
     	 			if(!isProfileRoute.contentEquals("true"))
     	 				request.getRequestDispatcher("/Views/User/edit.jsp").include(request, response);
     	 			else
         		    	request.getRequestDispatcher("/Views/Account/profile.jsp").include(request, response);	
     		    	return;
     		     }
     		}
     		
     		boolean errorExist = this.Validation(request, response);
     		
     		HttpSession session = request.getSession(false);
     		User user= (User) session.getAttribute("userManager");

    		if (errorExist) {
 	 			if(isProfileRoute == null)
 	 				request.getRequestDispatcher("/Views/User/edit.jsp").include(request, response);
 	 			else
     		    	request.getRequestDispatcher("/Views/Account/profile.jsp").include(request, response);	
    		} else {
    			String email = request.getParameter("email");
    			String firstName = request.getParameter("firstName");
    			String lastName = request.getParameter("lastName");
    			String roleId = request.getParameter("roleId");
    			String address = request.getParameter("address");
    			String phone = request.getParameter("phone");
    			String oldProfile = request.getParameter("oldProfile");
     			String profile = fileName;

	            Timestamp updateDate = new Timestamp(System.currentTimeMillis());
    			Timestamp dob = ConvertStringToTimestamp(request.getParameter("dob"));

    			ResponseModel model = _userService.Update(new User(id, firstName, lastName, email, address,
    					profile, phone, roleId, dob, updateDate, user.getId(), oldProfile));
    			ServletContext context = getServletContext();  
    			String dir = context.getInitParameter("fileDir");  
    			
    			if(model.getMessageType() == Message.SUCCESS) {
    				
    		        if(!fileName.contentEquals("user.png")) {
    		        	File fileUploadDirectory = new File(dir);
        		        if (!fileUploadDirectory.exists()) {
        		            fileUploadDirectory.mkdirs();
        		        }
        		        
        		        String savePath = dir + File.separator + fileName;
        		        part.write(savePath);
    		        }
    		        
    		        request.setAttribute("model", model);
     	 			if(isProfileRoute == null)
     	 				request.getRequestDispatcher("/Views/User/edit.jsp").forward(request, response);
     	 			else
         		    	request.getRequestDispatcher("/Views/Account/profile.jsp").include(request, response);	
    			}else {
    				request.setAttribute("model", model);
     	 			if(isProfileRoute == null)
     	 				request.getRequestDispatcher("/Views/User/edit.jsp").include(request, response);
     	 			else
         		    	request.getRequestDispatcher("/Views/Account/profile.jsp").include(request, response);	
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private String ExtractFileName(Part part) {
	        String contentDisp = part.getHeader("content-disposition");
	        String[] items = contentDisp.split(";");
	        UUID uuid = UUID.randomUUID();
	        for (String s : items) {
	            if (s.trim().startsWith("filename")) {
	                return "img_"+uuid+s.substring(s.indexOf("=") + 2, s.length() - 1);
	            }
	        }
	        return "";
	    }
	
	private static Timestamp ConvertStringToTimestamp(String inputDateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate =  dateFormat.parse(inputDateStr);
            return new Timestamp(parsedDate.getTime());
            
        } catch (ParseException e) {
            e.printStackTrace();
            return null; 
        }
    }

	private boolean Validation(HttpServletRequest request, HttpServletResponse response) {
		boolean error = false;
		String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		String email = request.getParameter("email");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String roleId = request.getParameter("roleId");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");

		// fistName check
		if (firstName.contentEquals("")) {
			request.setAttribute("firstNameError", Message.RFirstname);
			error = true;
		} else if (firstName.length() > 20) {
			request.setAttribute("firstNameError", Message.LengthCheck);
			error = true;
		}

		// lastName check
		if (lastName.contentEquals("") && lastName.length() > 20) {
			request.setAttribute("lastNameError", Message.LengthCheck);
			error = true;
		}

		// phone check
		if (phone.contentEquals("")) {
			request.setAttribute("phoneError", Message.RPhone);
			error = true;
		} else if (!phone.matches("^\\d{11}$")) {
			request.setAttribute("phoneError", Message.FPhone);
			error = true;
		}

		// email check
		if (email.contentEquals("")) {
			request.setAttribute("emailError", Message.REmail);
			error = true;
		} else if (!email.matches(emailPattern)) {
			request.setAttribute("emailError", Message.FEmail);
			error = true;
		}

		// password check
		if (request.getParameter("id") == null) {
			String cPassword = request.getParameter("cpassword");
			String password = request.getParameter("password");

			// password check
			if (password.contentEquals("")) {
				request.setAttribute("passwordError", Message.RPassword);
				error = true;
			} else if (!password.matches(passwordPattern)) {
				request.setAttribute("passwordError", Message.FPassword);
				error = true;
			}

			// confirm password check
			if (cPassword.contentEquals("")) {
				request.setAttribute("cpasswordError", Message.RCPassword);
				error = true;
			} else if (!password.contentEquals(cPassword)) {
				request.setAttribute("cpasswordError", Message.PasswordMatch);
				error = true;
			}
		}

		// role check
		if (roleId == null) {
			request.setAttribute("roleError", Message.RRole);
			error = true;
		}

		// birthday check
		if (dob.contentEquals("")) {
			request.setAttribute("dobError", Message.RDOB);
			error = true;
		}
		
		// address check
		if (!lastName.contentEquals("") && address.length() > 100) {
			request.setAttribute("addressError", Message.LAddress);
			error = true;
		}

		return error;
	}
	
	private boolean CheckExtension(String fileName) {
		String [] allowExtesnions = {"png","jpeg","jpg"};
		String getExtension = null;
		int lastDotIndex = fileName.lastIndexOf('.');
	    if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
	    	getExtension = fileName.substring(lastDotIndex + 1).toLowerCase();
	    }
	   for (String extension : allowExtesnions) {
		   if(extension.contentEquals(getExtension.toLowerCase())) {
			   return true;
		   }
	   }
	   return false;
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
			String userId = jsonData.getString("id");
     		HttpSession session = request.getSession(false);
     		User user= (User) session.getAttribute("userManager");

			ResponseModel model = _userService.Delete(userId,user.getId());

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
