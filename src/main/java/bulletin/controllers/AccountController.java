package bulletin.controllers;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.UUID;
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
		HttpSession session = request.getSession(true);
		if(session.getAttribute("userManager") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		} else {
			response.sendRedirect(request.getContextPath() +"/Layout/index.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		
		if (url.endsWith("login")) {
			login(request, response);
		} else if (url.endsWith("register")) {
			register(request, response);
		} else if (url.endsWith("change-password")) {
			changePassword(request, response);
		} else if (url.endsWith("forgot-password")) {
			forgotPassword(request, response);
		} else if (url.endsWith("reset-password")) {
			resetPassword(request, response);
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = new User();
		boolean errorExist = this.validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/login.jsp").include(request, response);
		} else {
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));

			ResponseModel model = _accountService.Login(user);
			if (model.getMessageType() == Message.SUCCESS) {
				
				// set the session with its expiration time in 30 minutes
				HttpSession session = request.getSession(true);
				session.setAttribute("userManager", model.getUserModel());
				session.setMaxInactiveInterval(30 * 60);
				response.sendRedirect(request.getContextPath() + "/Layout/index.jsp");
			} else {
				request.setAttribute("model", model);
				request.getRequestDispatcher("/login.jsp").include(request, response);
			}
		}
	}

	private void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = new User();
		boolean errorExist = this.validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/register.jsp").include(request, response);
		} else {
			Timestamp createDate = new Timestamp(System.currentTimeMillis());
			String id = UUID.randomUUID().toString();
			int getTarget = request.getParameter("email").toString().lastIndexOf("@");
			String firstName = request.getParameter("email").toString().substring(0, getTarget);

			user.setId(id);
			user.setFirstName(firstName);
			user.setEmail(request.getParameter("email"));
			user.setPassword(request.getParameter("password"));
			user.setCreatedDate(createDate);
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

	private void changePassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean errorExist = this.validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/Views/Account/change-password.jsp").include(request, response);
		} else {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");

			User userModel = new User();
			userModel.setId(user.getId());
			userModel.setOldPassword(request.getParameter("opassword"));
			userModel.setPassword(request.getParameter("password"));

			ResponseModel model = _accountService.ChangePassword(userModel);
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Account/change-password.jsp").forward(request, response);
		}
	}

	private void forgotPassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean errorExist = this.validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/Views/Account/forgot-password.jsp").include(request, response);
		} else {
			String email = request.getParameter("email");
			ResponseModel model = _accountService.CheckEmail(email);
			if (model.getMessageType() == Message.FAIL) {
				request.setAttribute("model", model);
				request.getRequestDispatcher("/Views/Account/forgot-password.jsp").include(request, response);
				return;
			} else {
				String to = email;
				String from = "bulletinBoradTeam@gmail.com";
				
				// your mail trap username and password
				final String username = "b175e99d9a1a8e";
				final String password = "f40868ff58681c";
				String host = "smtp.mailtrap.io";
				Properties props = new Properties();
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.port", "587");
				Session session = Session.getInstance(props, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
				try {
					int index = email.indexOf("@gmail.com");
					String name = email.substring(0, index);
					ServletContext context = getServletContext();
					String dir = context.getInitParameter("templateDir");
					
					// to get base url
					String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getRequestURI().length()) + request.getContextPath();
					String fileContent = readFile(dir);
					String mailBody = fileContent.replace("#name#", name)
							.replace("#route_link#", baseUrl + "/Views/Account/reset-password.jsp")
							.replace("#home_route#", baseUrl + "/login.jsp");
					jakarta.mail.Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));
					message.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(to));
					message.setSubject("Testing Mailtrap");

					MimeMultipart multipart = new MimeMultipart();
					MimeBodyPart htmlPart = new MimeBodyPart();
					htmlPart.setContent(mailBody, "text/html");
					multipart.addBodyPart(htmlPart);
					message.setContent(multipart);
					Transport.send(message);

					// set the session with its expiration time in 15 minutes.
					HttpSession resetSession = request.getSession(true);
					resetSession.setAttribute("resetManager", email);
					resetSession.setMaxInactiveInterval(15 * 60);
					request.setAttribute("model", model);
					request.getRequestDispatcher("/Views/Account/forgot-password.jsp").forward(request, response);
				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean errorExist = this.validation(request, response);
		if (errorExist) {
			request.getRequestDispatcher("/Views/Account/reset-password.jsp").include(request, response);
		} else {
			HttpSession session = request.getSession(false);
			String mail = (String) session.getAttribute("resetManager");
			String password = request.getParameter("password");
			ResponseModel model = _accountService.ResetPassword(mail,password);
			
			// remove session
			session.removeAttribute("resetManager");
			session.invalidate();
			request.setAttribute("model", model);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

	// html file read for email template
	private String readFile(String filePath) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// input validation
	private boolean validation(HttpServletRequest request, HttpServletResponse response) {
		boolean error = false;
		String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String url = request.getRequestURL().toString();
		if (!url.endsWith("change-password")
				&& !url.endsWith("reset-password")) {
			// email check
			if (email == null) {
				request.setAttribute("emailError", Message.REmail);
				error = true;
			} else if (!email.matches(emailPattern)) {
				request.setAttribute("emailError", Message.FEmail);
				error = true;
			}
		}

		if (!url.endsWith("forgot-password")) {
			if (password.isEmpty()) {
				request.setAttribute("passwordError", Message.RPassword);
				error = true;
			} else if (!password.matches(passwordPattern)) {
				request.setAttribute("passwordError", Message.FPassword);
				error = true;
			}
		}

		if (url.endsWith("register")
				|| url.endsWith("change-password")
				|| url.endsWith("reset-password")) {
			String cPassword = request.getParameter("cpassword");

			// password check
			if (cPassword.isEmpty()) {
				request.setAttribute("cpasswordError", Message.RCPassword);
				error = true;
			} else if (!password.endsWith(cPassword)) {
				request.setAttribute("cpasswordError", Message.PasswordMatch);
				error = true;
			}
		}

		if (url.endsWith("change-password")) {
			String oPassword = request.getParameter("opassword");
			
			// old password check
			if (oPassword.isEmpty()) {
				request.setAttribute("opasswordError", Message.ROPassword);
				error = true;
			} else if (!oPassword.matches(passwordPattern)) {
				request.setAttribute("opasswordError", Message.FPassword);
				error = true;
			}

		}

		return error;
	}
}
