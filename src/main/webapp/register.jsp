<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up</title>
<link rel="stylesheet" href="/BulletinOJT/assets/css/style.css">
<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
		crossorigin="anonymous"></head>
<body>
	<div class="container">
		<div class="row main align-items-center justify-content-center">
			<div class="col-8 col-lg-10 bx-shadow p-5 p-lg-0">
			<div class="row align-items-center justify-content-between ">
				<div class="col-10 col-lg-4 m-auto">
					<h3 class="text-center">Sign Up</h3>
					<form action="LoginController" method="post">
						<div class="form-group mb-3">
							<label class="form-label" for="email">Email address</label> 
							<input class="form-control" type="email" placeholder="email" required id="email" name="email">
						</div>
						<div class="form-group mb-3">
							<label class="form-label" for="password">Password</label> 
							<input class="form-control" type="password" placeholder="password" required id="password" name="password">
						</div>
						<div class="form-group mb-3">
							<label class="form-label" for="cpassword">Confirm Password</label> 
							<input class="form-control" type="password" placeholder="confirm password" required id="cpassword" name="cpassword">
						</div>
						<button type="submit" class="btn btn-dark w-100 p-2">Sign Up</button>
						<div class="text-center mt-4">
							<span class="text-secondary">Already have an account?</span>
							<a href="/BulletinOJT/login.jsp" class="cc-tag text-decoration-none text-dark">Log In</a>
						</div>
					</form>
				</div>
				<div class="d-none d-lg-block col-lg-6 p-0">
				<img alt="login photo" class="login-img w-100" src="http://localhost:8080/BulletinOJT/assets/img/img_gradient.png">
				</div>
			</div>
			</div>
		</div>
	</div>
</body>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
	crossorigin="anonymous"></script>
</html>