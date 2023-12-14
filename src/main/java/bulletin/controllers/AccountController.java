package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = new User();

		user.setEmail(email);
		user.setPassword(password);

		_accountService.Login(user);
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
			request.setAttribute("emailError", "Email field is required.");
			error = true;
		} else if (!email.matches(emailPattern)) {
			request.setAttribute("emailError", "Email format is incorret.");
			error = true;
		}

		if (password == null) {
			request.setAttribute("passwordError", "Password field is required.");
			error = true;
		} else if (!password.matches(passwordPattern)) {
			request.setAttribute("passwordError", "Password format is incorret.");
			error = true;
		}

		if (request.getParameter("action").contentEquals("register")) {
			String cPassword = request.getParameter("cpassword");

			// password check
			if (cPassword.toString() == null) {
				request.setAttribute("cpasswordError", "Confirm password field is required.");
				error = true;
			} else if (!password.contentEquals(cPassword)) {
				request.setAttribute("cpasswordError", "Password and confirm password must be the same.");
				error = true;
			}
		}

		return error;
	}
}
