<%@ include file="/Layout/header.jsp"%>
<title>Bulletin Board | Profile</title>
<h3>User Profile</h3>
<jsp:useBean id="roles" class="bulletin.models.Role"
	scope="page"></jsp:useBean>
<jsp:useBean id="model" class="bulletin.models.ResponseModel"
	scope="request"></jsp:useBean>
<jsp:useBean id="userModel" class="bulletin.models.User"
scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="userModel" />
<div class="mt-3">
	<ul class="nav nav-tabs" id="myTab" role="tablist">
		<li class="nav-item" role="presentation">
			<button class="nav-link active" id="home-tab" data-bs-toggle="tab"
				data-bs-target="#home-tab-pane" type="button" role="tab"
				aria-controls="home-tab-pane" aria-selected="true">Information</button>
		</li>
		<li class="nav-item" role="presentation">
			<button class="nav-link" id="profile-tab" data-bs-toggle="tab"
				data-bs-target="#profile-tab-pane" type="button" role="tab"
				aria-controls="profile-tab-pane" aria-selected="false">Edit Profile</button>
		</li>
	</ul>
	<div class="tab-content" id="myTabContent">
		<div class="tab-pane fade show active" id="home-tab-pane"
			role="tabpanel" aria-labelledby="home-tab" tabindex="0">
<div class="row mt-4">
				<div class="col-7">
					<div class="row">
						<div class="col-6">
							<span class="fw-bold">First Name</span>
							<p class="firstName"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Last Name</span>
							<p class="lastName"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Role</span>
							<p class="role"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Email</span>
							<p class="email"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold ">Address</span>
							<p class="address"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold ">Mobile</span>
							<p class="phone"></p>
						</div>
						<div class="col-6">
							<span class="fw-bold">Date Of Birth</span>
							<p class="dob"></p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="tab-pane fade" id="profile-tab-pane" role="tabpanel"
			aria-labelledby="profile-tab" tabindex="0">
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
			</c:choose>
			<form id="myForm"
				action="<%= request.getContextPath()+"/updateUser" %>" method="POST"
				enctype="multipart/form-data" class="g-3 mt-4">
				<input type="hidden" name="id" id="hiddenId" value="${ userModel.getId() }">
				<input type="hidden" name="isProfileRoute" value=true> <input
					type="hidden" name="oldProfile"
					value="${ userModel.getOldProfile() }">

				<div class="row">
					<div class="col-12 col-md-6 mb-3">
						<label for="firstName" class="form-label">First Name</label> <span
							class="text-danger fw-bold">*</span> <input required type="text"
							name="firstName" id="firstName"
							value="${ userModel.getFirstName() }"
							class="form-control rounded ${not empty requestScope.firstNameError ? 'is-invalid' : ''}"
							placeholder="John">
						<c:if test="${requestScope.firstNameError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.firstNameError}" />
							</div>
						</c:if>
					</div>
					<div class="col-12 col-md-6 mb-3">
						<label for="lastName" class="form-label">Last Name</label> <input
							type="text" name="lastName" class="form-control rounded"
							value="${ userModel.getLastName() }" placeholder="Doe"
							id="lastName">
					</div>
				</div>
				<div class="row">
					<div class="col-12 col-md-6 mb-3">
						<label for="phone" class="form-label">Phone</label> <span
							class="text-danger fw-bold">*</span> <input type="number"
							name="phone" value="${ userModel.getPhone() }"
							class="form-control rounded ${not empty requestScope.phoneError ? 'is-invalid' : ''}"
							placeholder="09123456789" id="phone">
						<c:if test="${requestScope.phoneError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.phoneError}" />
							</div>
						</c:if>
					</div>
					<div class="col-12 col-md-6 mb-3">
						<label for="email" class="form-label">Email</label> <span
							class="text-danger fw-bold">*</span> <input required type="email"
							name="email" value="${ userModel.getEmail() }"
							class="form-control rounded ${not empty requestScope.emailError || model.getMessageType() == 3  ? 'is-invalid' : ''}"
							placeholder="name@example.com" id="email">
						<c:choose>
							<c:when test="${requestScope.emailError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.emailError}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="invalid-feedback">
									<c:out value="${model.getMessageName()}" />
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="row">
					<div class="col-12 col-md-6 mb-3">
						<label for="role" class="form-label">Role</label> <span
							class="text-danger fw-bold">*</span> <select
							class="form-select ${not empty requestScope.roleError ? 'is-invalid' : ''}"
							name="roleId">
							<option selected disabled value="">Choose a role</option>
							<c:forEach var="role" items="${roles.getRoleList()}">
								<option value="${ role.getId() }"
									${userModel.getRoleId() eq role.getId() ? 'selected' : ''}><c:out
										value="${role.getName()}"></c:out></option>
							</c:forEach>
						</select>
						<c:if test="${requestScope.roleError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.roleError}" />
							</div>
						</c:if>
					</div>
					<div class="col-12 col-md-6 mb-3">
						<label for="dob" class="form-label">Birthday Date</label> <span
							class="text-danger fw-bold">*</span>
						<c:choose>
							<c:when test="${empty userModel.getDOB()}">
								<input type="date" required name="dob" id="dob"
									value="${param.dob}" class="form-control rounded">
							</c:when>
							<c:otherwise>
								<input type="date" required name="dob" id="dob"
									value='<fmt:formatDate pattern="yyyy-MM-dd" value="${userModel.getDOB()}"/>'
									class="form-control rounded">
							</c:otherwise>
						</c:choose>
						<c:if test="${requestScope.dobError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.dobError}" />
							</div>
						</c:if>
					</div>
				</div>
				<div class="row">
					<div class="col-12 col-md-6 mb-3">
						<label for="profile" class="form-label">Profile</label> <input
							accept="image/*, image/png, image/jpg, image/jpeg" type="file"
							class="form-control rounded ${not empty requestScope.fileError ? 'is-invalid' : ''}"
							type="file" name="profile" id="profile">
						<c:if test="${requestScope.fileError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.fileError}" />
							</div>
						</c:if>
						<c:if test="${not empty userModel.getOldProfile() }">
							<span class="d-block"> <small>current profile - </small> <small>${userModel.getOldProfile()}</small>
							</span>
						</c:if>
					</div>
					<div class="col-12 col-md-6">
						<div class="mb-4">
							<label for="address" class="form-label">Address</label>
							<textarea name="address"
								class="form-control rounded ${not empty requestScope.addressError ? 'is-invalid' : ''}"
								id="address">${userModel.getAddress()}</textarea>
							<c:if test="${requestScope.addressError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.addressError}" />
								</div>
							</c:if>
						</div>
					</div>
				</div>
				<div class="row justify-content-center align-items-center">
					<button type="submit"
						class="btn btn-primary me-4 col-3 col-md-2 col-lg-1">Update</button>
					<a class="btn btn-secondary col-3 col-md-2 col-lg-1"
						href="<%= request.getContextPath()+"/UserController"%>">Cancel</a>
				</div>
			</form>
		</div>
	</div>
</div>
<%@ include file="/Layout/footer.jsp"%>
