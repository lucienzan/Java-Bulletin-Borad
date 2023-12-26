<%@page import="org.apache.jasper.tagplugins.jstl.core.Param"%>
<%@ include file="/Layout/header.jsp"%>
<title>Bulletin Board | Posts</title>
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
<div class="container">
    <div class="row justify-content-center algin-items-center">
        <div class="col col-12 col-md-8 col-lg-6">
            <h3 class="my-4">Create New Post</h3>
            <form method="Post" class="g-3" action="<%= request.getContextPath() + "/PostController"%>">
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <span class="text-danger fw-bold">*</span>
                        <input required type="text" id="title" name="title" value="${ param.title }" class="form-control rounded  ${not empty requestScope.titleError || model.getMessageType() == 3 ? 'is-invalid' : ''}">
						<c:choose>
							<c:when test="${requestScope.titleError != null}">
								<div class="invalid-feedback">
									<c:out value="${requestScope.titleError}" />
								</div>
							</c:when>
							<c:otherwise>
								<div class="invalid-feedback">
									<c:out value="${model.getMessageName()}" />
								</div>
							</c:otherwise>
						</c:choose>                    
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <span class="text-danger fw-bold">*</span>
                        <textarea required id="description" name="description" class="form-control rounded ${not empty requestScope.describeError ? 'is-invalid' : ''}" >${ param.description }</textarea>
                    	<c:if test="${requestScope.describeError != null}">
							<div class="invalid-feedback">
								<c:out value="${requestScope.describeError}" />
							</div>
						</c:if>
                    </div>
                <div class="form-check form-switch mb-4">
                    <input class="form-check-input rounded-5" type="checkbox" id="isPublished" ${ param.isPublished == 'on' ? "checked" : "" }  name="isPublished">
                    <label class="form-check-label" for="isPublished">Publish?</label>
                </div>
                <div class="d-flex align-items-center justify-content-center">
                    <button type="submit" class="btn btn-primary me-4">Create</button>
                    <a class="btn btn-secondary" href="<%= request.getContextPath() + "/Views/Post/post-list.jsp"%>">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
<%@ include file="/Layout/footer.jsp"%>