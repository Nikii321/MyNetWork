<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 29.03.2022
  Time: 16:14
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>Sub</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">

</head>
<body>
<div>
    <table>
        <thead>
        <th>Name</th>
        <th>Surname</th>
        <th>subscribe</th>
        </thead>
        <c:forEach items="${Subscriptions}" var="user">
            <tr>
                <td>${user.usDetails.nameUser}</td>
                <td>${user.usDetails.surnameUser}</td>
                <td><a href="http://localhost:8081/page/${user.username}">${user.username}</a></td>


            </tr>
        </c:forEach>
    </table>

</div>

</body>
</html>