<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Log in with your account</title>
  <style>
    body{
      background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
    }
  </style>
</head>

<body>
<sec:authorize access="isAuthenticated()">
  <% response.sendRedirect("/"); %>
</sec:authorize>
<div>
  <div>
    <form method="POST" action="/login">
      <h2>
        <center style="color: #f1f1f1">Вход в систему</center>

      </h2>
      <div>
        <center>
          <p>

            <input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;"
                   name="username" type="text" placeholder="Username"
                   autofocus="true"/>
          </p>
          <p style="margin-top: -15px">

            <input style="border-radius: 6.5px; height: 30px; width: 250px;background-color:seashell"
                   name="password" type="password" placeholder="Password"/>
          </p>
          <button type="submit"
                  style="border-radius: 6.5px;background-color: green;margin-left: 10px; height: 30px; width: 80px">
            Log In
          </button>
          <button style="width: 190px; height: 30px; background-color: skyblue; border-radius:6.5px;margin-left: 0px">

            <h><a href="/registration" style="text-decoration: none">Зарегистрироваться</a></h>
          </button>

        </center>

        <p style="margin-left: 43%;margin-top: 5px">


        </p>

      </div>
    </form>
  </div>
</div>

</body>
</html>