<%@page import="bulletin.models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
		crossorigin="anonymous">
<body>
<jsp:useBean id="userInfo" class="bulletin.models.User" scope="session"></jsp:useBean>
<%
	User userManager = null;
	if(session.getAttribute("userManager") == null){
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}else{
		userManager = (User) session.getAttribute("userManager");
	}
%>
<jsp:setProperty property="id" name="userInfo" value="<% userManager.getId(); %>"/>
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
          <a class="nav-link" aria-current="page" href="#">User</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#">Post</a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li><a class="dropdown-item" href="#">Role</a></li>
            <li><hr class="dropdown-divider"></li>
            <li>
				<form action="<%= request.getContextPath() + "/HomeController" %>" method="get">
            		<button type="submit" class="border-none">Logout</button>
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
