<%@page import="bulletin.models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.7/css/jquery.dataTables.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.7/css/dataTables.bootstrap5.min.css">
<link rel="stylesheet" href="/BulletinOJT/assets/css/style.css">
<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
		crossorigin="anonymous">
<link rel="shortcut icon" href="<%= request.getContextPath() + "/assets/img/img_favicon.png" %>" type="image/x-icon" sizes='16x16'>
<body>
<jsp:useBean id="userInfo" class="bulletin.models.User" scope="session"></jsp:useBean>
<%
	User userManager = null;
	if(session.getAttribute("userManager") == null){
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}else{
		userManager = (User) session.getAttribute("userManager");
	    userInfo.setId(userManager.getId());
	    userInfo.setFirstName(userManager.getFirstName());
	    userInfo.setRoleName(userManager.getRoleName());
	}
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1
%>
<div class="container-fluid bg-dark">
	<header class="container-fluid">
	<nav class="container navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container-fluid">
    <a class="navbar-brand" href="<%= request.getContextPath()+"/Layout/index.jsp" %>">Bulletin Board</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
        <li class="nav-item">
    		<a class="nav-link <%= request.getRequestURI().endsWith("user-list.jsp") ? "active" : "" %>" aria-current="page"  href="<%= request.getContextPath() + "/Views/User/user-list.jsp" %>" >User</a>
        </li>
        <li class="nav-item">
          <a class="nav-link <%= request.getRequestURI().endsWith("post-list.jsp") ? "active" : "" %>" href="<%= request.getContextPath() + "/Views/Post/post-list.jsp" %>">Post</a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
          <%= userInfo.getFirstName() %>
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" id="profileRoute" href="<%= request.getContextPath() + "/profile?userId=" + userInfo.getId() %>">Profile</a></li>
            <li><a class="dropdown-item" href="<%= request.getContextPath() + "/Views/Account/change-password.jsp" %>">Change Password</a></li>
            <li><hr class="dropdown-divider"></li>
            <li>
				<form action="<%= request.getContextPath() + "/HomeController" %>" method="get">
            		<button type="submit" class="border-0 bg-transparent ps-3">
            		  Logout
            		</button>
            	</form>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>
	</header>
</div>
<div class="container mt-4">
	<div class="row justify-content-center align-items-center">
		<div class="col-12 col-md-8 col-lg-10">
		