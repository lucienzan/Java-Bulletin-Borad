<%@page import="bulletin.common.Message"%>
<%@ include file="/Layout/header.jsp"%>
<title>Bulletin Board | Profile</title>
<jsp:useBean id="model" class="bulletin.models.ResponseModel"
	scope="request"></jsp:useBean>
<jsp:useBean id="userModel" class="bulletin.models.User" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="userModel" />
<c:choose>
	<c:when test="${ model.getMessageType() == 2 }">
		<div class="alert alert-danger col-12 mt-2" role="alert">
			<c:out value="${ model.getMessageName() }"></c:out>
		</div>
	</c:when>
	<c:when test="${ model.getMessageType() == 1 }">
		<div class="alert alert-success col-12 mt-2" role="alert">
			<c:out value="${ model.getMessageName() }"></c:out>
		</div>
	</c:when>
	<c:when test="${not empty param.status }">
		<div class="alert alert-success col-12 mt-2" role="alert">
			<span><%= Message.UpdateSuccess %></span>
		</div>
	</c:when>
</c:choose>
<div class="d-flex justify-content-between align-items-center mb-3">
	<h3>Profile</h3>
	<a class="btn btn-primary col-3 col-md-2 col-lg-1"
		href="<%= request.getContextPath()+"/UserController/user-edit?userId=" + userModel.getId() + "&action=profile-route" %>">Edit</a>
</div>
<div class="col-10">
	<div class="row">
		<div class="col-8">
			<div class="row">
				<div class="col-6">
							<span class="fw-bold">First Name</span>
							<p class="firstName"><%= userModel.getFirstName() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Last Name</span>
							<p class="lastName"><%= userModel.getLastName() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Role</span>
							<p class="role"><%= userModel.getRoleName() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Email</span>
							<p class="email"><%= userModel.getEmail() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold ">Address</span>
							<p class="address"><%= userModel.getAddress() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold ">Mobile</span>
							<p class="phone"><%= userModel.getPhone() %></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Date Of Birth</span>
							<p class="dob">
								<fmt:formatDate pattern="dd/MM/yyyy"
									value="<%= userModel.getDOB() %>" />
							</p>
						</div>
			</div>
		</div>
		<div class="col-4">
			<img alt="profile" src="<%= request.getContextPath() + "/assets/img/profile/" + userModel.getProfile() %>" class="imgProfile">
		</div>
	</div>
</div>
<%@ include file="/Layout/footer.jsp"%>
