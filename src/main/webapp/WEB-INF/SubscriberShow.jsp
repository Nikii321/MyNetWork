<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 29.03.2022
  Time: 22:33
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
        <th>Subscriptions Count</th>
        <th>Subscribe Count</th>
        <th>is Subscribe</th>
        </thead>
        <c:forEach items="${Subscriber}" var="user">

            <tr>
                <c:if test="${!user.username.equals(I.username)}">
                    <td><a href="http://localhost:8081/page/${user.username}">${user.username}</a></td>
                    <td>${user.subscribers.size()}</td>
                    <td>${user.subscriptions.size()}</td>
                    <td>${user.subscribers.contains(I)}</td>
                </c:if>

            </tr>

        </c:forEach>
    </table>

</div>

</body>
</html>