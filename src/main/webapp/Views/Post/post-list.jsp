<%@ include file="/Layout/header.jsp"%>
<title>Bulletin Board | Posts</title>
<div>
<c:choose>
	<c:when test="${ model.getMessageType() == 1 }">
	  <div class="alert alert-success col-12" role="alert">
  		<c:out value="${ model.getMessageName() }"></c:out>
	  </div>
	</c:when>
	<c:when test="${ model.getMessageType() == 2 || model.getMessageType() == 3 }">
		<div class="alert alert-danger col-12" role="alert">
  		<c:out value="${ model.getMessageName() }"></c:out>
	  </div>
	</c:when>
</c:choose>
		<div class="mb-3 d-flex justify-content-between align-items-center">
			<h3>Post List</h3>
			<div class="d-flex">
                <div class="dropdown-cente me-2">
                <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                   Excel
                </button>
                <ul class="dropdown-menu">
                <li> 
                <button type="button" class="border-0 bg-transparent ps-3" data-bs-toggle="modal" data-bs-target="#exampleModal">
                    Import
                </button>
                </li>
                <li><a href='<%= request.getContextPath() + "/PostController/excel-export" %>' id='editUserBtn' class='dropdown-item'>Export</a></li>
                </ul>
                </div>
                <a class="btn btn-primary" href="<%= request.getContextPath()+"/PostController/post-create" %>">Create</a>
			</div>
		</div>
		<table id="postList" class="table">
			<thead>
				<tr>
					<th class="text-center">No</th>
					<th>Title</th>
                    <th>Description</th>
                    <th>IsPublished</th>
                    <th>Author</th>
                    <th>Created At</th>
                    <th class="text-center">Actions</th>
				</tr>
			</thead>
		</table>
		<!-- Modal -->
		<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Import excel file.</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
               <form action="<%= request.getContextPath() + "/PostController/excel-import"%>" method="post" enctype="multipart/form-data" id="importForm">
                    <input accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" type="file" id="importFile" name="importFile" class="form-control rounded ${not empty requestScope.fileError ? 'is-invalid' : ''}">
                     <c:if test="${requestScope.fileError != null}">
						<div class="invalid-feedback">
							<c:out value="${requestScope.fileError}" />
						</div>
					</c:if>
               </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary"  data-bs-dismiss="modal">Close</button>
                <button form="importForm" type="submit"  class="btn btn-primary">Import</button>
            </div>
        </div>
    </div>
</div>
		<div class="modal fade" id="postModal" tabindex="-1" aria-labelledby="postModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title fs-5" id="modalTtl"></h3>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                    <div>
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                            <span class="fw-bold d-block">Title</span>
                            <p class="ttl"></p>
                            </div>
                            <div class="status"></div> 
                        </div>
                        <div>
                        <span class="fw-bold d-block">Description</span>
                            <p class="describe"></p>
                        </div>
                        <div class="mt-2">
                        <span class="fw-bold d-block">Author</span>
                            <p class="author"></p>
                        </div>
                        <div>
                            <span class="fw-bold ">Created Date</span>
                            <p class="date"></p>
                        </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
	</div>
</div>
	</div>
<%@ include file="/Layout/footer.jsp"%>
