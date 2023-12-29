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
@MultipartConfig
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
				this.getUserList(request, response);
			} else if (url.endsWith("user-create")) {
				this.getUser(request, response);
			} else if (url.endsWith("user-detail")) {
				this.getUserDetail(request, response);
			} else if (url.endsWith("user-edit")) {
				this.editUser(request, response);
			} else if (url.endsWith("profile")) {
				this.profile(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getUserDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		User user = _userService.Get(id);
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(user);

		response.getWriter().write(json);
	}

	private void profile(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String id = request.getParameter("userId");
		User user = _userService.Get(id);
		request.setAttribute("userModel", user);
		request.getRequestDispatcher("/Views/Account/profile.jsp").forward(request, response);
	}

	private void getUserList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		List<User> userList = _userService.GetAll();
		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(userList);

		// Send the JSON response to the client
		response.getWriter().write(json);
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/Views/User/create.jsp").forward(request, response);
	}

	private void editUser(HttpServletRequest request, HttpServletResponse response) {
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
		if (url.endsWith("updateUser")) {
			this.updateUser(request, response);
		} else {
			this.createUser(request, response);
		}
	}

	private void createUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			boolean errorExist = this.validation(request, response);
			String fileName = "user.png";

			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");

			if (errorExist) {
				request.getRequestDispatcher("/Views/User/create.jsp").include(request, response);
			} else {
				Part part = request.getPart("profile");
				fileName = "img_"+UUID.randomUUID().toString()+part.getSubmittedFileName();

				String id = UUID.randomUUID().toString();
				String email = request.getParameter("email");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String roleId = request.getParameter("roleId");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String password = request.getParameter("password");
				String profile = fileName;
				Timestamp createdDate = new Timestamp(System.currentTimeMillis());
				Timestamp dob =  request.getParameter("dob").isEmpty() ? null : convertStringToTimestamp(request.getParameter("dob"));

				ResponseModel model = _userService.Create(new User(id, firstName, lastName, email, password, address,
						profile, phone, roleId, dob, createdDate, user.getId()));
				ServletContext context = getServletContext();
				String dir = context.getInitParameter("fileDir");

				if (model.getMessageType() == Message.SUCCESS) {
					if (!fileName.contentEquals("user.png")) {
						File fileUploadDirectory = new File(dir);
						if (!fileUploadDirectory.exists()) {
							fileUploadDirectory.mkdirs();
						}
						String savePath = dir + File.separator + fileName;
						part.write(savePath);
					}

					request.setAttribute("model", model);
					request.getRequestDispatcher("/Views/User/create.jsp").forward(request, response);
				} else {
					request.setAttribute("model", model);
					request.getRequestDispatcher("/Views/User/create.jsp").include(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String fileName = "user.png";
			boolean errorExist = this.validation(request, response);
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");

			if (errorExist) {
					request.getRequestDispatcher("/Views/User/edit.jsp").include(request, response);
			} else {
				Part part = request.getPart("profile");
				fileName = part.getSize() != 0 ? "img_"+UUID.randomUUID().toString()+part.getSubmittedFileName() : fileName;
				String id = request.getParameter("id");
				String email = request.getParameter("email");
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String roleId = request.getParameter("roleId");
				String address = request.getParameter("address");
				String phone = request.getParameter("phone");
				String oldProfile = request.getParameter("oldProfile");
				Timestamp updateDate = new Timestamp(System.currentTimeMillis());
				Timestamp dob = request.getParameter("dob").isEmpty() ? null : convertStringToTimestamp(request.getParameter("dob"));

				ResponseModel model = _userService.Update(new User(id, firstName, lastName, email, address, fileName,
						phone, roleId, dob, updateDate, user.getId(), oldProfile));
				ServletContext context = getServletContext();
				String dir = context.getInitParameter("fileDir");
				
				if (model.getMessageType() == Message.SUCCESS) {
					if (!fileName.contentEquals("user.png")) {
						File fileUploadDirectory = new File(dir);
						if (!fileUploadDirectory.exists()) {
							fileUploadDirectory.mkdirs();
						}
						String savePath = dir + File.separator + fileName;
						part.write(savePath);
					}

					request.setAttribute("model", model);
					request.getRequestDispatcher("/Views/User/edit.jsp").forward(request, response);
				} else {
					request.setAttribute("model", model);
					request.getRequestDispatcher("/Views/User/edit.jsp").include(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Timestamp convertStringToTimestamp(String inputDateStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsedDate = dateFormat.parse(inputDateStr);
			return new Timestamp(parsedDate.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean validation(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
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
		Part part = request.getPart("profile");
		String url = request.getRequestURL().toString();

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
		if (!dob.isEmpty() && !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
			System.out.println("hi");
			request.setAttribute("dobError", Message.FDOB);
			error = true;
		}

		// address check
		if (!address.contentEquals("") && address.length() > 100) {
			request.setAttribute("addressError", Message.LAddress);
			error = true;
		}

		// profile check
		if (part.getSize() == 0 && url.endsWith("user-create")) {
			request.setAttribute("fileError", Message.RFile);
			error = true;
		} else if (part.getSize() > 1024 * 1024 * 10) {
			request.setAttribute("fileError", Message.FileSize);
			error = true;
		} else if (part.getSize() != 0 && part.getSize() < 1024 * 1024 * 10) {
			String fileName = part.getSubmittedFileName();
			boolean isAllowExtension = checkExtension(fileName);
			if (isAllowExtension == false) {
				request.setAttribute("fileError", Message.FileTypeError);
				error = true;
			}
		}

		return error;
	}
	
	private boolean checkExtension(String fileName) {
		String[] allowExtesnions = { "png", "jpeg", "jpg" };
		String getExtension = null;
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
			getExtension = fileName.substring(lastDotIndex + 1).toLowerCase();
		}
		for (String extension : allowExtesnions) {
			if (extension.contentEquals(getExtension.toLowerCase())) {
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
			User user = (User) session.getAttribute("userManager");

			ResponseModel model = _userService.Delete(userId, user.getId());
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
