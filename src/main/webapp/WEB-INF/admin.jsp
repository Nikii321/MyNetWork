<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Admin panel</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">
</head>

<body>
<div>
    <table>
        <thead>
        <th>ID</th>
        <th>UserName</th>
        <th>Roles</th>
        </thead>
        <c:forEach items="${allUsers}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>
                    <c:forEach items="${user.roles}" var="role">${role.name}; </c:forEach>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <input type="hidden" name="action" value="delete"/>
                        <button type="submit">Ban</button>
                    </form>

                </td>

            </tr>
        </c:forEach>
    </table>

</div>
<p/>
<p/>
<p/>
<div>
    <table>
        <thead>
        <th>ID</th>
        <th>UserName</th>
        <th>Roles</th>
        </thead>
        <c:forEach items="${allBaned}" var="user">
            <tr style="color: red">
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>
                    <c:forEach items="${user.roles}" var="role">${role.name}; </c:forEach>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <input type="hidden" name="action" value="Unban"/>
                        <button type="submit">Unban</button>
                    </form>

                </td>

            </tr>
        </c:forEach>
    </table>
    <a href="/">Main</a>

</div>
</body>
</html>