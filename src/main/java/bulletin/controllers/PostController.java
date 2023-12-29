package bulletin.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import bulletin.common.Message;
import bulletin.dao.Repositories.PostRepository;
import bulletin.models.Post;
import bulletin.models.ResponseModel;
import bulletin.models.Role;
import bulletin.models.User;
import bulletin.services.PostService;

@MultipartConfig
public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostService _postService;

	public PostController() {
		super();
	}

	public void init() {
		_postService = new PostService(new PostRepository());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		String contentType = request.getContentType();

		if (contentType == null && url.endsWith("PostController")) {
			request.getRequestDispatcher("/Views/Post/post-list.jsp").forward(request, response);
		} else if (contentType != null && url.endsWith("PostController")) {
			this.getAll(request, response);
		} else if (url.endsWith("post-detail")) {
			this.showPost(request, response);
		} else if (url.endsWith("post-create")) {
			this.createPost(request, response);
		} else if (url.endsWith("post-edit")) {
			this.getPost(request, response);
		} else if (url.endsWith("excel-export")) {
			this.excelExport(request, response);
		}
	}

	private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		Role role = new Role();
		var userRoleId = role.getRoleList().stream().filter(r -> "User".equals(r.getName())).map(Role::getId)
				.findFirst().orElse(null);
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("userManager");

		List<Post> postList = _postService.GetAll();
		if (user.getRoleId().contentEquals(userRoleId)) {
			postList = postList.stream().filter(post -> post.getCreatedUserId().contentEquals(user.getId()))
					.collect(Collectors.toList());
		}

		Gson gson = new GsonBuilder().serializeNulls().create();
		String json = gson.toJson(postList);

		// Send the JSON response to the client
		response.getWriter().write(json);
	}

	private void getPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("postId");
		Post post = _postService.Get(id);
		request.setAttribute("post", post);
		request.getRequestDispatcher("/Views/Post/edit.jsp").forward(request, response);
	}

	private void createPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/Views/Post/create.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getRequestURL().toString();

		if (url.endsWith("excel-import")) {
			this.excelImport(request, response);
		} else if (url.endsWith("post-create")) {
			this.insertPost(request, response);
		} else if (url.endsWith("post-edit")) {
			this.updatePost(request, response);
		}
	}

	private void insertPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean hasError = validation(request);
		if (hasError) {
			request.getRequestDispatcher("/Views/Post/create.jsp").include(request, response);
		} else {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			String id = UUID.randomUUID().toString();
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			boolean isPublished = request.getParameter("isPublished") != null ? true : false;
			Timestamp createdDate = new Timestamp(System.currentTimeMillis());
			ResponseModel model = _postService.Create(new Post(id, title, description, isPublished, createdDate, user.getId()));
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Post/create.jsp").forward(request, response);
		}
	}

	private void updatePost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean hasError = validation(request);
		if (hasError) {
			request.getRequestDispatcher("/Views/Post/edit.jsp").include(request, response);
		} else {
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("userManager");
			Post post = new Post();

			post.setId(request.getParameter("id"));
			post.setTitle(request.getParameter("title"));
			post.setDescription(request.getParameter("description"));
			boolean isPublished = request.getParameter("isPublished") != null ? true : false;
			Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
			post.setIsPublished(isPublished);
			post.setUpdatedDate(updatedDate);
			post.setUpdatedUserId(user.getId());

			ResponseModel model = _postService.Update(post);
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Post/edit.jsp").forward(request, response);
		}
	}

	private void showPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		Post post = _postService.Get(id);

		Gson gson = new GsonBuilder().serializeNulls().create();
		String obj = gson.toJson(post);
		response.getWriter().write(obj);
	}

	private void excelImport(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Part part = request.getPart("importFile");

		if (part != null && part.getSize() > 0) {
			String fileName = extractFileName(part);
			boolean isAllowExtension = hasExcelExtension(fileName);
			if (isAllowExtension == false) {
				request.setAttribute("fileError", Message.FileTypeError);
			} else if (part.getSize() > 1024 * 1024 * 2) {
				request.setAttribute("fileError", Message.FileTypeError);
			} else {
				Post post = new Post();
				HttpSession session = request.getSession(false);
				User user = (User) session.getAttribute("userManager");
				Timestamp createdDate = new Timestamp(System.currentTimeMillis());

				InputStream fileContent = part.getInputStream();
				XSSFWorkbook workbook = new XSSFWorkbook(fileContent);
				XSSFSheet sheet = workbook.getSheet("Sheet1");
				XSSFRow row = null;
				ResponseModel model = new ResponseModel();

				int i = 1;
				while ((row = sheet.getRow(i)) != null) {
					Cell titleCell = row.getCell(0);
					Cell descriptionCell = row.getCell(1);
					Cell statusCell = row.getCell(2);
					boolean status = false;

					if (titleCell == null) {
						model.setMessageType(Message.FAIL);
						model.setMessageName(Message.RTitle);
						break;
					}
					if (descriptionCell == null) {
						model.setMessageType(Message.FAIL);
						model.setMessageName(Message.RDescription);
						break;
					}
					if (statusCell != null) {
						status = row.getCell(2).getStringCellValue().equalsIgnoreCase("Published") ? true : false;
					}

					post.setId(UUID.randomUUID().toString());
					post.setTitle(row.getCell(0).getStringCellValue().toString());
					post.setDescription(row.getCell(1).getStringCellValue().toString());
					post.setIsPublished(status);
					post.setCreatedDate(createdDate);
					post.setCreatedUserId(user.getId());
					model = _postService.Create(post);
					i++;
				}
				workbook.close();

				request.setAttribute("model", model);
				request.getRequestDispatcher("/Views/Post/post-list.jsp").forward(request, response);
			}
		} else {
			request.setAttribute("fileError", Message.RFile);
		}
		request.getRequestDispatcher("/Views/Post/post-list.jsp").include(request, response);
	}

	private void excelExport(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Role role = new Role();
		ResponseModel model = new ResponseModel();
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("userManager");
		var userRoleId = role.getRoleList().stream().filter(r -> "User".equals(r.getName())).map(Role::getId)
				.findFirst().orElse(null);

		List<Post> posts = _postService.GetAll();
		if (user.getRoleId().contentEquals(userRoleId)) {
			posts = posts.stream().filter(post -> post.getCreatedUserId().contentEquals(user.getId()))
					.collect(Collectors.toList());
		}
		
		if(posts.size() > 0) {
			try (OutputStream out = response.getOutputStream(); XSSFWorkbook workbook = new XSSFWorkbook()) {
				XSSFSheet sheet = workbook.createSheet("Sheet1");
				XSSFRow headerRow = sheet.createRow(0);

				// Set header values
				headerRow.createCell(0).setCellValue("Title");
				headerRow.createCell(1).setCellValue("Description");
				headerRow.createCell(2).setCellValue("Status");

				// Populate the sheet with data
				int rowNum = 1;
				for (Post post : posts) {
					Row row = sheet.createRow(rowNum++);
					int colNum = 0;

					Cell cellTitle = row.createCell(colNum++);
					cellTitle.setCellValue(post.getTitle());

					Cell cellDescription = row.createCell(colNum++);
					cellDescription.setCellValue(post.getDescription());

					Cell cellStatus = row.createCell(colNum++);
					cellStatus.setCellValue(post.isIsPublished() ? "Published" : "Unpublished");
				}
				
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition", "attachment; filename=example.xlsx");
				workbook.write(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.setMessageType(Message.FAIL);
			model.setMessageName(Message.ExportFail);
			request.setAttribute("model", model);
			request.getRequestDispatcher("/Views/Post/post-list.jsp").forward(request, response);
		}
	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		UUID uuid = UUID.randomUUID();
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return "img_" + uuid + s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	private boolean hasExcelExtension(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
		return extension.equals("xls") || extension.equals("xlsx");
	}

	private boolean validation(HttpServletRequest request) {
		boolean hasError = false;
		String title = request.getParameter("title");
		String description = request.getParameter("description");

		// Title
		if (title.isBlank()) {
			request.setAttribute("titleError", Message.RTitle);
			hasError = true;
		} else if (title.length() > 225) {
			request.setAttribute("titleError", Message.LTitle);
			hasError = true;
		}

		// Description
		if (description.isBlank()) {
			request.setAttribute("describeError", Message.RDescription);
			hasError = true;
		} else if (description.length() > 1000) {
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
			User user = (User) session.getAttribute("userManager");

			ResponseModel model = _postService.Delete(postId, user.getId());
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
