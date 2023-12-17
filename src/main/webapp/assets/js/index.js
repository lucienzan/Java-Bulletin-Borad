jQuery.noConflict();
jQuery(document).ready(function($) {
	$("#userList").DataTable({
		"processing": true,
		"serverSide": false,
		"ajax": {
			"url": "/BulletinOJT/UserController",
			"type": "GET",
			"datatype": "json",
			"contentType": "application/json",
			"dataSrc": ""
		},
		"columns": [
			{
				"render": function(meta) {
					return meta.row + meta.settings._iDisplayStart + 1;
				}
			},
			{ "data": "FullName", "name": "FullName", "width": "20%" },
			{ "data": "Email", "name": "Email", "width": "20%" },
			{
				"render": function(full) {

					var str = "";

					if (full.Role == "Admin") {
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
				"render": function(full) {
					if (full.DOB != null) {
						var date = formatDate(full.DOB);
						return date;
					}
					return "-"
				}

			},
			{
				"render": function(full) {

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
				"render": function(full) {
					var date = formatDate(full.CreatedDate);
					return date;
				}
			},
			{
				render: function(full) {

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
});

function userRoute(route) {
	jQuery(document).ready(function($) {
		$.ajax({
			url: route,
			type: 'POST',
			success: function(response) {
				document.title = "Bulletin Board | User List";
				window.history.pushState(" ", " ", route);
				$('script').html("");
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
						table.ajax.reload();
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
