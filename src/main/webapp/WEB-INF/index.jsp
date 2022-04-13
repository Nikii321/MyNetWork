<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
<head>
  <title>Main</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
  <style>
    body{
      background-image: url("${pageContext.request.contextPath}/resources/image/main.jpg");
      background-color: #f1f1f1;
    }
  </style>
</head>
<body>
<div>
  <h3 style="color: #f1f1f1;text-decoration: none">${pageContext.request.userPrincipal.name}</h3>
  <sec:authorize access="!isAuthenticated()">
    <h4 style="color: #f1f1f1"><a href="/login" style="color: #f1f1f1">Log in</a></h4>
    <h4 style="color: #f1f1f1"><a href="/registration" style="color: #f1f1f1">Registration</a></h4>
  </sec:authorize>
  <sec:authorize access="isAuthenticated()">
    <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_USER')">
      <h4><a href="/page/${pageContext.request.userPrincipal.name}" style="color: #f1f1f1">page</a></h4>
    </sec:authorize>
  </sec:authorize>
  <sec:authorize access="hasRole('ROLE_BANED')">
    <big style="color: red"> BANED </big>
  </sec:authorize>
  <sec:authorize access="isAuthenticated()">
    <h4><a href="/logout" style="color: #f1f1f1">log out</a></h4>
  </sec:authorize>
  <sec:authorize access="hasRole('ROLE_USER')">
    <h4><a href="/news" style="color: #f1f1f1">News</a></h4>
  </sec:authorize>
  <sec:authorize access="hasRole('ROLE_ADMIN')">
    <h4><a href="/admin" style="color: #f1f1f1">Users</a></h4>
  </sec:authorize>


</div>
</body>
</html>