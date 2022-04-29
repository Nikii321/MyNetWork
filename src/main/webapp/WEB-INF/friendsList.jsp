<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 29.04.2022
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>List Friends </title>
    <style>
        body{
            background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
            color: #f1f1f1;
        }
    </style>
</head>
<body>

    <h2>Find user</h2>
    <form method="post">
    <div>
        <input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="name">
        <button type="submit" style="width: 70px; height: 30px; background-color: skyblue; border-radius:6.5px;margin-left: 0px">Find</button>
    </div>
    </form>

<p/>
<div>
    <c:forEach items="${Users}" var="user">
        <p/>
        <p><td><a style="color: skyblue" href="http://localhost:8081/page/${user.username}">Username: ${user.username}</a></td>  Count subscribers: ${user.subscribers.size()};  Count subscriptions:  ${user.subscriptions.size()}  </p>
        <p/>
    </c:forEach>
</div>

</body>
</html>
