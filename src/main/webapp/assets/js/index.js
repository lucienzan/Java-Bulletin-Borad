jQuery.noConflict();
jQuery(document).ready(function($) {
	$("#userList").DataTable({
		"processing": true,
		"bDestroy": true,
		"ajax": {
			"url": "/BulletinOJT/UserController",
			"type": "GET",
			"datatype": "json",
			"contentType": "application/json",
			"dataSrc": ""
		},
		"columns": [
			{
				"render": function(data, type, full, meta) {
					return meta.row + meta.settings._iDisplayStart + 1;
				}
			},
			{ "data": "FullName", "name": "FullName", "width": "20%" },
			{ "data": "Email", "name": "Email", "width": "20%" },
			{
				"render": function(data, type, full, meta)  {

					var str = "";

					if (full.RoleId == "Admin") {
						str = "<span class='badge rounded-pill bg-primary'>Admin</span>";
					} else if (full.Role == null) {
						str = "<span class='badge rounded-pill bg-danger'>User</span>";
					} else {
						str = "<span class='badge rounded-pill bg-danger'>User</span>";
					}

					return str;
				}
			},
			{ "data": "Address", "name": "Address", "width": "20%" },
			{
				"render": function(data, type, full, meta)  {
					if (full.DOB != null) {
						var date = formatDate(full.DOB);
						return date;
					}
					return "-"
				}

			},
			{
				"render": function(data, type, full, meta)  {

					var str = "";

					if (full.Active == true) {
						str = "<span class='badge rounded-pill bg-success'>Active</span>";
					} else {
						str = "<span class='badge rounded-pill bg-danger'>In-Active</span>";
					}

					return str;
				}
			},
			{
				"render": function(data, type, full, meta)  {
					var date = formatDate(full.CreatedDate);
					return date;
				}
			},
			{
				render: function(data, type, full, meta)  {

					var actionsDropdown = `
                    <div class="dropdown-center">
                    <button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                       Control
                    </button>`;
					actionsDropdown += `
                    <ul class="dropdown-menu">
                    <li><a id='detailUserBtn' class='dropdown-item'>View</a></li>
                    <li><a id='editUserBtn' class='dropdown-item'>Edit</a></li>
                    <li><a id='deleteUserBtn' class='dropdown-item text-danger'>Delete</a></li>
                    </ul>
                    </div>`;
					var hdn = "<input type='hidden' id='hdnUserId' value=" + full.Id + " />";
					var actions = actionsDropdown + hdn;

					return actions;
				},
				"orderable": false,

			}
		],
	});

	$("table tbody").on("click", "#deleteUserBtn", function() {
		let userId = $(this).parent().parent().parent().siblings("input").val();
		let table = $("#userList").DataTable();
		let url = "/BulletinOJT/UserController";
		deleteConfirm(table, userId, url);
	});
	
	// Delete confirm pop up box
function deleteConfirm(table, id, route) {
	Swal.fire({
		title: 'Are you sure to delete?',
		text: "You won't be able to revert this!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: route,
				type: "DELETE",
				data: JSON.stringify({ userId: id }),
				contentType: "application/json",
				success: function(response) {
					if (response.status == 1) {
						Swal.fire(
							'Deleted!',
							'Your file has been deleted.',
							'success'
						)

                        table.draw();
						table.ajax.reload(null, false);
						showToast("success", response.message);
					}
				},
				error: function(error) {
					console.error("Error:", error);
				}
			});
		}
	})
}

	//user detail
	$("table tbody").on("click", "#detailUserBtn", function() {
		let id = $(this).parent().parent().parent().siblings("input").val();
		$.get("/BulletinOJT/UserController/GetDetail", { id: id }, function(data) {
			let {
				FullName,
				Profile,
				Address,
				Email,
				FirstName,
				LastName,
				Phone,
				DOB,
				Role
			} = JSON.parse(data);
			$("#modalTtl").text(`${FullName}` + " - information");
			$(".profileImg").attr("src", "/BulletinOJT/assets/img/profile/" + Profile);
			$(".firstName").text(FirstName);
			$(".lastName").text(LastName);
			if (Role == "Admin") {
				$(".role").html("<span class='badge rounded-pill bg-success'>Admin</span>");
			} else {
				$(".role").html("<span class='badge rounded-pill bg-primary'>User</span>");
			}
			$(".address").text(Address == "" ? "Unknown" : Address);
			$(".email").text(Email);
			$(".phone").text(Phone == null ? "Unknown" : Phone);
			$(".dob").text(DOB == null ? "Unknown" : formatDate(DOB));
			$("#exampleModal").modal("show");
		});
	});

});

function route(route) {
	jQuery(document).ready(function($) {
		$.ajax({
			url: route,
			type: 'GET',
			success: function(response) {
				window.history.pushState(" ", " ", route);
				$('body').html(response);
			},
			error: function(error) {
				console.error('Error:', error);
			}
		});
	})
}

function formatDate(inputDate) {
	const dateObj = new Date(inputDate);
	const day = dateObj.getDate();
	const month = dateObj.getMonth() + 1;
	const year = dateObj.getFullYear();
	const formattedDay = day < 10 ? '0' + day : day;

	return `${formattedDay}/${month}/${year}`;
}


//toast
function showToast(icon, mesg) {
	const Toast = Swal.mixin({
		toast: true,
		position: 'top-end',
		showConfirmButton: false,
		timer: 3000,
		timerProgressBar: true,
		didOpen: (toast) => {
			toast.addEventListener('mouseenter', Swal.stopTimer)
			toast.addEventListener('mouseleave', Swal.resumeTimer)
		}
	})
	Toast.fire({
		icon: icon,
		title: mesg
	})
}


