package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import bulletin.common.BCrypt;
import bulletin.common.Message;
import bulletin.dao.Repositories.AuthRepository;
import bulletin.models.ResponseModel;
import bulletin.models.User;
import bulletin.services.AccountService;

public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AccountService _accountService;

	@Override
	public void init() {
		_accountService = new AccountService(new AuthRepository());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.contentEquals("login")) {
			Login(request, response);
		} else {
			Register(request, response);
		}
	}

	private void Login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		User user = new User();
		boolean errorExist = this.Validation(request, response);
		if(errorExist) {
			request.getRequestDispatcher("/login.jsp").include(request, response);
		} else {
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			
			ResponseModel model = _accountService.Login(user);
			if(model.getMessageType() == Message.SUCCESS) {
				HttpSession session = request.getSession(true);
				session.setAttribute("userManager", model.getUserModel());
				session.setMaxInactiveInterval(30 * 60);
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			} else {
				request.setAttribute("model", model);
				request.getRequestDispatcher("/login.jsp").include(request, response);
			}
		}
	}

	private void Register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = new User();

		boolean errorExist = this.Validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/register.jsp").include(request, response);
		} else {
			UUID randomIdUuid = UUID.randomUUID();
			java.util.Date date = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			String generatedSecuredPasswordHash = BCrypt.hashpw(request.getParameter("password"), BCrypt.gensalt(12));
			String id = randomIdUuid.toString();
			int getTarget = request.getParameter("email").toString().indexOf("@");
			String firstName = request.getParameter("email").toString().substring(0, getTarget);

			user.setId(id);
			user.setFirstName(firstName);
			user.setEmail(request.getParameter("email"));
			user.setPassword(generatedSecuredPasswordHash);
			user.setCreatedDate(sqlDate);
			user.setCreatedUserId(id);
			ResponseModel model = _accountService.Register(user);
			if (model.getMessageType() == Message.SUCCESS) {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} else {
				request.setAttribute("model", model);
				request.getRequestDispatcher("/register.jsp").include(request, response);
			}
		}
	}

	private boolean Validation(HttpServletRequest request, HttpServletResponse response) {
		boolean error = false;
		String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		// email check
		if (email == null) {
			request.setAttribute("emailError", Message.REmail);
			error = true;
		} else if (!email.matches(emailPattern)) {
			request.setAttribute("emailError", Message.FEmail);
			error = true;
		}

		if (password == null) {
			request.setAttribute("passwordError", Message.RPassword);
			error = true;
		} else if (!password.matches(passwordPattern)) {
			request.setAttribute("passwordError", Message.FPassword);
			error = true;
		}

		if (request.getParameter("action").contentEquals("register")) {
			String cPassword = request.getParameter("cpassword");

			// password check
			if (cPassword.toString() == null) {
				request.setAttribute("cpasswordError", Message.RCPassword);
				error = true;
			} else if (!password.contentEquals(cPassword)) {
				request.setAttribute("cpasswordError", Message.PasswordMatch);
				error = true;
			}
		}

		return error;
	}
}
