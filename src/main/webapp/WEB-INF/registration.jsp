<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Registration</title>
</head>

<body>
<div>
  <form:form method="POST" modelAttribute="userForm">
    <h2>Registration</h2>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="username" placeholder="Username"
                  autofocus="true"></form:input>
      <form:errors path="username"></form:errors>
        ${usernameError}
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="email" placeholder="email"
                  autofocus="true"></form:input>
      <form:errors path="email"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="password" path="password" placeholder="Password"></form:input>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="password" path="passwordConfirm"
                  placeholder="Confirm your password"></form:input>
      <form:errors path="password"></form:errors>
        ${passwordError}
    </div>
    <button type="submit" style="width: 190px; height: 30px; background-color: skyblue; border-radius:6.5px;margin-left: 0px">Register</button>
  </form:form>
  <a href="/">Главная</a>
</div>
</body>
</html>