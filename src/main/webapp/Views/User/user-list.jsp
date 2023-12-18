<title>Bulletin Board | User List</title>
<%@ include file="/Layout/header.jsp" %>
		<div>
		<div class="mb-3 d-flex justify-content-between align-items-center">
			<h3>User List</h3>
			<a class="btn btn-primary" href="<%= request.getContextPath()+"/UserController/create" %>">Create</a>
		</div>
		<table id="userList" class="table">
			<thead>
				<tr>
					<th class="text-center">No</th>
					<th>FullName</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Address</th>
                    <th>Birthday</th>
                    <th>Active</th>
                    <th>Created At</th>
                    <th class="text-center">Actions</th>
				</tr>
			</thead>
		</table>
	</div>
<%@ include file="/Layout/footer.jsp" %>
	<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
	crossorigin="anonymous"></script>