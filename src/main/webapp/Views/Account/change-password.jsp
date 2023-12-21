<%@ include file="/Layout/header.jsp" %>
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
            <h3 class="my-4">Change Password</h3>
            <form action="<%= request.getContextPath() + "/AccountController" %>" method="POST" class="g-3 ">
            	<input type="hidden" name="action" value="changePwd">
                <div class="row align-items-center flex-column">
                    <div class="col-6 mb-3">
                        <label for="opassword" class="form-label">Old Password</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="password" id="opassword" name="opassword" class="form-control rounded ${not empty requestScope.opasswordError || model.getMessageType() == 3 ? 'is-invalid' : ''}" placeholder="Enter your old password.">
						<c:choose>
							<c:when test="${requestScope.opasswordError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.opasswordError}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="invalid-feedback">
									<c:out value="${model.getMessageName()}" />
								</div>
							</c:otherwise>
						</c:choose>
                    </div>
                    <div class="col-6 mb-3">
                        <label for="password" class="form-label">New Password</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="password" id="password" class="form-control rounded ${not empty requestScope.passwordError ? 'is-invalid' : ''}" name="password" placeholder="Enter your new password.">
                        <c:if test="${requestScope.passwordError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.passwordError}" />
							</div>
						</c:if>
                    </div>
                    <div class="col-6 mb-3">
                        <label for="cpassword" class="form-label">Confirm Password</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="password" id="cpassword" name="cpassword" class="form-control rounded ${not empty requestScope.cpasswordError ? 'is-invalid' : ''}" placeholder="Enter your confirm password.">
                    	<c:if test="${requestScope.cpasswordError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.cpasswordError}" />
							</div>
						</c:if>
                    </div>
                </div>
                <div class="d-flex align-items-center justify-content-center">
                    <button type="submit" class="btn btn-primary me-4 ">Update</button>
                    <a class="btn btn-secondary " href="<%= request.getContextPath() + "/UserController" %>">Cancel</a>
                </div>
            </form>
<%@ include file="/Layout/footer.jsp" %>
