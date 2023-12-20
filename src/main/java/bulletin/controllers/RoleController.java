package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;
import bulletin.common.Message;
import bulletin.dao.Repositories.RoleRepository;
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
			String editId = request.getParameter("editId");
			String delId = request.getParameter("deleteId");
			
			if(session == null) {
		        request.getRequestDispatcher("/login.jsp").include(request, response);
			}
			
			if(url.contentEquals("/RoleController") && editId == null && delId == null) {
				this.GetAll(request,response);
			} 
			
			if(editId != null) {
				this.GetRole(request,response);
			}
			
			if(delId != null) {
				this.DeleteRole(request,response);
			}
	}
	
	private void GetAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Role roleList = _roleService.GetAll();
		request.setAttribute("roles", roleList);
        request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
	}

	private void GetRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String editId = request.getParameter("editId");
		Role role = _roleService.GetRole(editId);
		request.setAttribute("roles", role);
        request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("editId");
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
		boolean hasError = Validate(request);
		HttpSession session = request.getSession(false);

		if(hasError) {
	        request.getRequestDispatcher("/Views/Role/index.jsp").include(request, response);	
	        GetRole(request,response);
		}else {
			String name = request.getParameter("roleName");
			String id = request.getParameter("editId");
			Timestamp updateDate = new Timestamp(System.currentTimeMillis());
			User user = (User) session.getAttribute("userManager");
			
			Role role = new Role();
			role.setName(name);
			role.setId(id.toString());
			role.setUpdatedDate(updateDate);
			role.setUpdatedUserId(user.getId());
			
			Role roles = _roleService.Update(role);
			request.setAttribute("roles", roles);
			request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
		}
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

	private void DeleteRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			String id = request.getParameter("deleteId");
			Timestamp deletedDate = new Timestamp(System.currentTimeMillis());
			
			Role model = _roleService.Delete(new Role(id,deletedDate,user.getId(),true));
			request.setAttribute("roles", model);
			request.getRequestDispatcher("/Views/Role/index.jsp").forward(request, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
