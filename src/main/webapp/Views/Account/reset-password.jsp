<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Password</title>
<link rel="stylesheet" href="/BulletinOJT/assets/css/style.css">
<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
		crossorigin="anonymous">
<link rel="shortcut icon" href="<%= request.getContextPath() + "/assets/img/img_favicon.png" %>" type="image/x-icon" sizes='16x16'>
</head>
<body>
<jsp:useBean id="user" class="bulletin.models.User" scope="page"></jsp:useBean>
<jsp:useBean id="model" class="bulletin.models.ResponseModel" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="user"/>
<%
String resetManager = null;
if(session.getAttribute("resetManager") == null){
	response.sendRedirect(request.getContextPath()+"/login.jsp");
}else{
	resetManager = (String) session.getAttribute("resetManager");
	}	

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1

%>
	<div class="container main-container">
		<div class="row main align-items-center justify-content-center">
			<c:if test="${ model.getMessageType() == 3 || model.getMessageType() == 2 }" >
				<div class="alert alert-danger col-8" role="alert">
			  		<c:out value="${ model.getMessageName() }"></c:out>
				</div>
			</c:if>
			<div class="col-8 col-lg-10 bx-shadow p-5 p-lg-0">
			<div class="row align-items-center justify-content-between ">
				<div class="col-10 col-lg-4 m-auto">
					<h3 class="text-center">Create New Password</h3>
					<p class="text-center text-secondary">Set your new password, so you can login and access Bulletin Board.</p>
					<form action="<%= request.getContextPath() + "/AccountController/reset-password" %>" method="post">
						<div class="form-group mb-3">
							<div class="d-flex justify-content-between">
							<label class="form-label" for="password">Create New Password</label> 
							</div>
							<input required class="form-control ${not empty requestScope.passwordError ? 'is-invalid' : ''}" type="password" placeholder="********" required id="password" name="password">
							<c:if test="${requestScope.passwordError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.passwordError}" />
								</div>
							</c:if>	
						</div>
						<div class="form-group mb-3">
							<div class="d-flex justify-content-between">
							<label class="form-label" for="cpassword">Confirm Password</label> 
							</div>
							<input required class="form-control ${not empty requestScope.cpasswordError ? 'is-invalid' : ''}" type="password" placeholder="********" required id="cpassword" name="cpassword">
							<c:if test="${requestScope.cpasswordError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.cpasswordError}" />
								</div>
							</c:if>	
						</div>
						<button type="submit" class="btn btn-dark w-100 p-2">Reset Password</button>
						<div class="text-center mt-4">
							<a href="<%= request.getContextPath() + "/login.jsp" %>" class="cc-tag text-decoration-none text-dark">Back to login?</a>
						</div>
					</form>
				</div>
				<div class="d-none d-lg-block col-lg-6 p-0">
				<img alt="login photo" class="login-img w-100" src="<%= request.getContextPath() + "/assets/img/img_gradient.png" %>">
				</div>
			</div>
			</div>
		</div>
	</div>
</body>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
	crossorigin="anonymous"></script>
</html>
