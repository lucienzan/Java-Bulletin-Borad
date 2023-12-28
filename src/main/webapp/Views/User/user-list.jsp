<title>Bulletin Board | Users</title>

<%@ include file="/Layout/header.jsp" %>
<% 
if(userInfo.getRoleName() != null){
	if(userInfo.getRoleName().contentEquals("User"))
	response.sendRedirect(request.getContextPath());
}
%>
		<div>
		<div class="mb-3 d-flex justify-content-between align-items-center">
			<h3>User List</h3>
			<a class="btn btn-primary" href="<%= request.getContextPath()+"/UserController/user-create" %>">Create</a>
		</div>
		<table id="userList" class="table">
			<thead>
				<tr>
					<th class="text-center">No</th>
					<th>FullName</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Address</th>
                    <th>Date Of Birth</th>
                    <th>Active</th>
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
                <h3 class="modal-title fs-5" id="modalTtl"></h3>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                   <div class="d-flex justify-content-center align-items-center">
                       <img src="" class="profileImg py-2" />
                   </div>
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
                <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            	</div>
            </div>
          </div>
       </div>
	</div>
<%@ include file="/Layout/footer.jsp" %>