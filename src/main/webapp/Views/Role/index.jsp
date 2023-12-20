<%@ include file="/Layout/header.jsp" %>
<jsp:useBean id="roleModel" class="bulletin.models.Role" scope="request"></jsp:useBean>
<jsp:setProperty property="*" name="roleModel"/>

	<c:choose>
		<c:when test="${ roles.model.getMessageType() == 2 }">
		  <div class="alert alert-danger col-12" role="alert">
	  		<c:out value="${ roles.model.getMessageName() }"></c:out>
		  </div>
		</c:when>
		<c:when test="${ roles.model.getMessageType() == 1 }">
			<% request.removeAttribute("roleName"); %>
		  <div class="alert alert-success col-12" role="alert">
	  		<c:out value="${ roles.model.getMessageName() }"></c:out>
		  </div>
		</c:when>
	</c:choose>
		<div class="role-form mb-4">
			<h3 id="roleTtl">
			<c:choose>
            	<c:when test="${empty roles.getId() }">
            		Role Create
            	</c:when>
            	<c:otherwise>
            		Role Update
            	</c:otherwise>
            </c:choose>
			</h3>
			<form action="<%= request.getContextPath()+"/RoleController"%>" method="POST">
				<input type="hidden" name="editId" value="${ roles.getId() }">
        		<div class="row">
       			<div class="col-12 col-md-6">
                       <label for="roleName" class="form-label">Role Name</label>
                       <span class="text-danger fw-bold">*</span>
                       <input required type="text" name="roleName" id="roleName" value="${empty roles.getName() ? roles.getName() : roles.getName()}" class="form-control rounded ${not empty requestScope.roleNameError || roles.model.getMessageType() == 3 ? 'is-invalid' : ''}" placeholder="guest">
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
                   <div class="mt-3">
                   	<button type="submit" class="btn btn-primary me-2" id="roleBtn">
                   		<c:choose>
                   			<c:when test="${empty roles.getId() }">
                   				Create
                   			</c:when>
                   			<c:otherwise>
                   				Update
                   			</c:otherwise>
                   		</c:choose>
                   	</button>
                    <a class="btn btn-secondary" href="<%= request.getContextPath()+"/RoleController"%>">Cancel</a>
                </div>
        		</div>
			</form>		
		</div>
		<c:if test="${ roles.getRoleList().size() > 0 }" >
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
							<a href="/BulletinOJT/RoleController?editId=<c:out value='${role.getId()}' />"
								class="btn btn-warning" id="roleEdit">Edit</a> 
							<a onclick="roleDelBtn(`${role.getId()}`)"
							class="btn btn-danger">Delete</a>
							</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:if>
<%@ include file="/Layout/footer.jsp" %>
