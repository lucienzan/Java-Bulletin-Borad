<%@ include file="/Layout/header.jsp" %>
<jsp:useBean id="role" class="bulletin.models.Role" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="role"/>

	<c:choose>
		<c:when test="${ roles.model.getMessageType() == 2 }">
		  <div class="alert alert-danger col-12" role="alert">
	  		<c:out value="${ roles.model.getMessageName() }"></c:out>
		  </div>
		</c:when>
		<c:when test="${ roles.model.getMessageType() == 1 }">
		  <div class="alert alert-success col-12" role="alert">
	  		<c:out value="${ roles.model.getMessageName() }"></c:out>
		  </div>
		</c:when>
	</c:choose>
		<div class="role-form mb-4">
			<h3 id="roleTtl">Role Create</h3>
			<form action="<%= request.getContextPath()+"/RoleController"%>" method="POST">
				<input type="hidden" name="id" value="${ roles.getId() }">
        		<div class="row">
       			<div class="col-12 col-md-6">
                       <label for="roleName" class="form-label">Role Name</label>
                       <span class="text-danger fw-bold">*</span>
                       <input  type="text" name="roleName" id="roleName" value="${empty role.getName() ? param.roleName : role.getName()  }" class="form-control rounded ${not empty requestScope.roleNameError || roles.model.getMessageType() == 3 ? 'is-invalid' : ''}" placeholder="guest">
						<c:choose>
								<c:when test="${requestScope.roleNameError != null}">
									<div class="invalid-feedback">
										<c:out value="${requestScope.roleNameError}" />
									</div>
								</c:when>
								<c:otherwise>
									<div class="invalid-feedback">
										<c:out value="${roles.model.getMessageName()}" />
									</div>
								</c:otherwise>
						</c:choose>
                   </div>
                  <div class="col-4 mt-lg-1 d-flex justify-content-center align-items-center">
                   	<button type="submit" class="btn btn-primary me-4" id="roleBtn">Create</button>
                    <a class="btn btn-secondary" href="<%= request.getContextPath()+"/RoleController"%>">Cancel</a>
                </div>
        		</div>
			</form>		
		</div>
		<div class="role-table">
			<h3>Role List</h3>
			<table class="table">
				<thead>
					<tr>
						<th class="text-center">No</th>
						<th>Name</th>
						<th class="text-center">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="role" items="${roles.getRoleList()}" varStatus="loop">
						<tr>
							<td class="text-center"><c:out value="${loop.count}" /></td>
							<td><c:out value="${role.getName()}" /></td>
							<td class="text-center">
							<a href="edit?id=<c:out value='${role.getId()}' />"
								class="btn btn-warning" id="roleEdit">Edit</a> 
							<a href="delete?id=<c:out value='${role.getId()}' />"
							class="btn btn-danger">Delete</a>
							</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
<%@ include file="/Layout/footer.jsp" %>
