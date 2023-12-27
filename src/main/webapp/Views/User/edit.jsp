<%@ include file="/Layout/header.jsp" %>
<% 
if(userInfo.getRoleName() != null){
	if(userInfo.getRoleName().contentEquals("User"))
	response.sendRedirect(request.getContextPath());
}
%>
<jsp:useBean id="userModel" class="bulletin.models.User" scope="request"></jsp:useBean>
<jsp:useBean id="roles" class="bulletin.models.Role" scope="page"></jsp:useBean>
<jsp:useBean id="model" class="bulletin.models.ResponseModel" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="userModel"/>
<div>
	<c:choose>
		<c:when test="${ model.getMessageType() == 2 }">
		  <div class="alert alert-danger col-12" role="alert">
	  		<c:out value="${ model.getMessageName() }"></c:out>
		  </div>
		</c:when>
		<c:when test="${ model.getMessageType() == 1 }">
		  <div class="alert alert-success col-12" role="alert">
	  		<c:out value="${ model.getMessageName() }"></c:out>
		  </div>
		</c:when>
	</c:choose>
		<div class="d-flex justify-content-between align-items-center">
			<h3>User Edit</h3>
		</div>
		<div>
			<form id="myForm" action="<%= request.getContextPath()+"/updateUser" %>" method="POST" enctype="multipart/form-data" class="g-3">
			<input type="hidden" name="id" value="${ userModel.getId() }">
			<input type="hidden" name="oldProfile" value="${ userModel.getOldProfile() }">

                <div class="row">
                    <div class="col-12 col-md-6 mb-3">
                        <label for="firstName" class="form-label">First Name</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="text" name="firstName" id="firstName" value="${ userModel.getFirstName() }" class="form-control rounded ${not empty requestScope.firstNameError ? 'is-invalid' : ''}" placeholder="John">
                    	<c:if test="${requestScope.firstNameError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.firstNameError}" />
							</div>
						</c:if>
                    </div>
                    <div class="col-12 col-md-6 mb-3">
                        <label for="lastName" class="form-label">Last Name</label>
                        <input type="text" name="lastName" class="form-control rounded" value="${ userModel.getLastName() }" placeholder="Doe" id="lastName">
                    </div>
                </div>
                <div class="row">
                    <div class="col-12 col-md-6 mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <span class="text-danger fw-bold">*</span>
                        <input type="number" name="phone" value="${ userModel.getPhone() }" class="form-control rounded ${not empty requestScope.phoneError ? 'is-invalid' : ''}" placeholder="09123456789" id="phone">
                    	<c:if test="${requestScope.phoneError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.phoneError}" />
							</div>
						</c:if>
                    </div>
                    <div class="col-12 col-md-6 mb-3">
                        <label for="email" class="form-label">Email</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="email" name="email" value="${ userModel.getEmail() }" class="form-control rounded ${not empty requestScope.emailError || model.getMessageType() == 3  ? 'is-invalid' : ''}" placeholder="name@example.com" id="email">
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
                        <label for="role" class="form-label">Role</label>
                        <span class="text-danger fw-bold">*</span>
                        <select ${ userinfo.getRoleName() != "admin" ? "disabled" : "" } class="form-select ${not empty requestScope.roleError ? 'is-invalid' : ''}" name="roleId">
                            <option selected disabled value="">Choose a role</option>
                        	<c:forEach var="role" items="${roles.getRoleList()}">
                        	  <option value="${ role.getId() }"  ${userModel.getRoleId() eq role.getId() || param.roleId eq role.getId() ? 'selected' : ''}><c:out value="${role.getName()}"></c:out></option>
                        	</c:forEach>
                        </select>
                        <c:if test="${ userinfo.getRoleName() != 'admin' }">
                        	<input type="hidden" name="roleId" value="${ userModel.getRoleId() }">
                        </c:if>
                        <c:if test="${requestScope.roleError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.roleError}" />
							</div>
						</c:if>
                    </div>
                    <div class="col-12 col-md-6 mb-3">
                        <label for="dob" class="form-label">Birthday Date</label>
						<c:choose>
						       <c:when test="${empty userModel.getDOB()}">
						           <input type="date" name="dob" id="dob" value="${param.dob}" class="form-control rounded">
						       </c:when>
						       <c:otherwise>
						           <input type="date" name="dob" id="dob" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${userModel.getDOB()}"/>' class="form-control rounded">
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
                        <label for="profile" class="form-label">Profile</label>
                        <input accept="image/*, image/png, image/jpg, image/jpeg" type="file" class="form-control rounded ${not empty requestScope.fileError ? 'is-invalid' : ''}" type="file" name="profile" id="profile" onchange="fileSelect()">
                        <c:if test="${requestScope.fileError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.fileError}" />
							</div>
						</c:if>
						<img alt="review picture" id="previewImage" width="100" height="100" class="rounded mt-2" src="">
						<c:if test="${not empty userModel.getOldProfile() }">
                       	 <span class="d-block">
                       	 <small>current profile - </small>
                         <small>${userModel.getOldProfile()}</small>
                       	 </span>
                        </c:if>
                    </div>
                    <div class="col-12 col-md-6">
                        <div class="mb-4">
                            <label for="address" class="form-label">Address</label>
                            <textarea name="address"  class="form-control rounded ${not empty requestScope.addressError ? 'is-invalid' : ''}" id="address" >${userModel.getAddress()}</textarea>
                        	<c:if test="${requestScope.addressError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.addressError}" />
								</div>
							</c:if>
                        </div>
                    </div>
                </div>
                <div class="row justify-content-center align-items-center mb-3">
                    <button type="submit" class="btn btn-primary me-4 col-3 col-md-2 col-lg-1">Update</button>
                    <a class="btn btn-secondary col-3 col-md-2 col-lg-1" href="<%= request.getContextPath()+"/UserController"%>">Cancel</a>
                </div>
            </form>
		</div>
	</div>
<%@ include file="/Layout/footer.jsp" %>
