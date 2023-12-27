jQuery.noConflict();
jQuery(document).ready(function($) {

// user table
$("#userList").DataTable({
	"processing": true,
	"bDestroy": true,
	"pageLength": 10,
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

				if (full.RoleId == "Admin" || full.RoleId == "admin") {
					str = `<span class='badge rounded-pill bg-primary'>${full.RoleId}</span>`;
				} else if (full.RoleId == "User" || full.RoleId == "user") {
					str = `<span class='badge rounded-pill bg-secondary'>${full.RoleId}</span>`;
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
                <li><a href='/BulletinOJT/UserController/user-edit?userId=${full.Id}' id='editUserBtn' class='dropdown-item'>Edit</a></li>
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

// user delete
$("table tbody").on("click", "#deleteUserBtn", function() {
	let userId = $(this).parent().parent().parent().siblings("input").val();
	let table = $("#userList").DataTable();
	let url = "/BulletinOJT/UserController";
	deleteConfirm(table, userId, url);
});
	
// user detail
$("table tbody").on("click", "#detailUserBtn", function() {
	let id = $(this).parent().parent().parent().siblings("input").val();
	$.get("/BulletinOJT/UserController/user-detail", { id: id }, function(data) {
		let {
			FullName,
			Profile,
			Address,
			Email,
			FirstName,
			LastName,
			Phone,
			DOB,
			RoleName
		} = JSON.parse(data);
		$("#modalTtl").text(`${FullName}` + " - information");
		$(".profileImg").attr("src", "/BulletinOJT/assets/img/profile/" + Profile);
		$(".firstName").text(FirstName);
		$(".lastName").text(LastName);
		if (RoleName == "Admin" || RoleName == "admin") {
			$(".role").html("<span class='badge rounded-pill bg-primary'>Admin</span>");
		} else if (RoleName == "User" || RoleName == "user") {
			$(".role").html("<span class='badge rounded-pill bg-secondary'>User</span>");
		}
		$(".address").text(Address == null ? "Unknown" : Address);
		$(".email").text(Email);
		$(".phone").text(Phone == null ? "Unknown" : Phone);
		$(".dob").text(DOB == null ? "Unknown" : formatDate(DOB));
		$("#exampleModal").modal("show");
	});
});

// post table
$("#postList").DataTable({
	"processing": true,
	"bDestroy": true,
	"pageLength": 10,
	"ajax": {
		"url": "/BulletinOJT/PostController",
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
		{ "data": "Title", "name": "Title", "width": "20%" },
		{
			"render": function(data, type, full, meta)  {
				var describe = full.Description.length > 120 ? full.Description.substring(0,120).concat("...") : full.Description;
				return describe;
			}
		},
		{
			"render": function(data, type, full, meta)  {

				var str = "";

				if (full.IsPublished) {
					str = `<span class='badge rounded-pill bg-primary'>Published</span>`;
				} else {
					str = `<span class='badge rounded-pill bg-secondary'>Unpublished</span>`;
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
                <div class="dropdown-center text-center">
                <button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                   Control
                </button>`;
				actionsDropdown += `
                <ul class="dropdown-menu">
                <li><a id='viewPostBtn' class='dropdown-item'>View</a></li>
                <li><a href='/BulletinOJT/PostController/post-edit?postId=${full.Id}' class='dropdown-item'>Edit</a></li>
                <li><a id='deletePostBtn' class='dropdown-item text-danger'>Delete</a></li>
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

// post detail
$("table tbody").on("click", "#viewPostBtn", function () {
    let id = $(this).parent().parent().parent().siblings("input").val();
    console.log(id);
    $.get("/BulletinOJT/PostController/post-detail", { id: id }, function (data) {
		console.log(data);
        let {
            Title,
            Description,
            IsPublished,
            CreatedUserId,
            CreatedDate,
        } = JSON.parse(data);
        $("#modalTtl").text(Title);
        $(".ttl").text(Title);
        $(".describe").text(Description);
        $(".author").text(CreatedUserId);
        if (IsPublished == false) {
            $(".status").html("<span class='badge rounded-pill bg-danger'>Unpublished</span>")
        } else {
            $(".status").html("<span class='badge rounded-pill bg-success'>Published</span>")
        }
        $(".date").text(formatDate(CreatedDate));
        $("#postModal").modal("show");
    });
})

// post delete
$("table tbody").on("click", "#deletePostBtn", function () {
    let id = $(this).parent().parent().parent().siblings("input").val();
    let table = $("#postList").DataTable();
	let url = "/BulletinOJT/PostController";
	deleteConfirm(table, id, url);
})

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
				data: JSON.stringify({ id: id }),
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

});

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

function fileSelect() {
    const fileInput = document.getElementById('profile');
    const previewImage = document.getElementById('previewImage');

    if (fileInput.files && fileInput.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            previewImage.src = e.target.result;
            previewImage.style.display = "block";
        };

        reader.readAsDataURL(fileInput.files[0]);
    }
}

try{
	profile();
	function profile() {
	jQuery(document).ready(function($){
		let id = $("#hiddenId").val();
		$.get("/BulletinOJT/UserController/user-detail", { id: id }, function(data) {
		let {
			Address,
			Email,
			FirstName,
			LastName,
			Phone,
			DOB,
			RoleName
		} = JSON.parse(data);
		$(".firstName").text(FirstName);
		$(".lastName").text(LastName);
		if (RoleName == "Admin" || RoleName == "admin") {
			$(".role").html("<span class='badge rounded-pill bg-success'>Admin</span>");
		} else if(RoleName == "User" || RoleName == "user") {
			$(".role").html("<span class='badge rounded-pill bg-primary'>User</span>");
		} else {
			$(".role").html("<span class='badge rounded-pill bg-secondary'>Guest</span>");
		}
		$(".address").text(Address == null ? "Unknown" : Address);
		$(".email").text(Email);
		$(".phone").text(Phone == null ? "Unknown" : Phone);
		$(".dob").text(DOB == null ? "Unknown" : formatDate(DOB));
	});
	}) 
}
}catch(ex){
	console.log(ex);
}
