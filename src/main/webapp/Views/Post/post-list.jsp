<%@ include file="/Layout/header.jsp"%>
<title>Bulletin Board | Posts</title>
<div>
		<div class="mb-3 d-flex justify-content-between align-items-center">
			<h3>Post List</h3>
			<a class="btn btn-primary" href="<%= request.getContextPath()+"/PostController/post-create" %>">Create</a>
		</div>
		<table id="postList" class="table">
			<thead>
				<tr>
					<th class="text-center">No</th>
					<th>Title</th>
                    <th>Description</th>
                    <th>IsPublished</th>
                    <th>Created At</th>
                    <th class="text-center">Actions</th>
				</tr>
			</thead>
		</table>
		<!-- Modal -->
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
                            <p class="text-primary author"></p>
                        </div>
                        <div>
                        <div>
                            <span class="fw-bold ">Created Date</span>
                            <p class="date"></p>
                        </div>
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
