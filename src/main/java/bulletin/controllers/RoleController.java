package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import bulletin.common.Message;
import bulletin.dao.Repositories.RoleRepository;
import bulletin.models.ResponseModel;
import bulletin.models.Role;
import bulletin.models.User;
import bulletin.services.RoleService;

public class RoleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private RoleService _roleService;

	public void init() {
		this._roleService = new RoleService(new RoleRepository());
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String url = request.getServletPath().toString();
			HttpSession session = request.getSession(false);

			if(session == null) {
		        request.getRequestDispatcher("/login.jsp").include(request, response);
			}
			
			if(url.contentEquals("/RoleController")) {
				this.GetAll(request,response);
			}
	}
	
	private void GetAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Role roleList = _roleService.GetAll();
		request.setAttribute("roles", roleList);
        request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String id = request.getParameter("id");
			if(id.isEmpty()) {
				CreateRole(request,response);
			}else {
				UpdateRole(request,response);
			}
	}
	
	private void CreateRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean hasError = Validate(request);
		HttpSession session = request.getSession(false);

		if(hasError) {
	        request.getRequestDispatcher("/Views/Role/index.jsp").include(request, response);	
	        GetAll(request,response);
		}else {
			String name = request.getParameter("roleName");
			UUID id = UUID.randomUUID();
			Timestamp createdDate = new Timestamp(System.currentTimeMillis());
			User user = (User) session.getAttribute("userManager");
			
			Role role = new Role();
			role.setName(name);
			role.setId(id.toString());
			role.setCreatedDate(createdDate);
			role.setCreatedUserId(user.getId());
			role.setDeletedFlag(false);
			
			Role roles = _roleService.Create(role);
			request.setAttribute("roles", roles);
			request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
		}
	}
	
	private void UpdateRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Role roleList = _roleService.GetAll();
		request.setAttribute("roles", roleList);
        request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
	}

	private boolean Validate(HttpServletRequest request) throws ServletException, IOException {
		boolean hasError = false;
		String name = request.getParameter("roleName");
		
		if(name.contentEquals("")) {
			request.setAttribute("roleNameError", Message.RRole);
			hasError = true;
		}else if(name.length() > 20) {
			request.setAttribute("roleNameError", Message.LengthCheck);
			hasError = true;
		}
		
		return hasError;
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try(BufferedReader reader = request.getReader()) {
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			JSONObject object = new JSONObject(builder.toString());
			String id = object.getString("id");
			Timestamp deletedDate = new Timestamp(System.currentTimeMillis());
			
			ResponseModel model = _roleService.Delete(new Role(id,deletedDate,user.getId(),true));
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("status", model.getMessageType());
			jsonResponse.put("message",model.getMessageName());
			response.setContentType("application/json");
			response.getWriter().write(jsonResponse.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
