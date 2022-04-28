<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>News</title>
    <style>
        body{
            background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
            color: #f1f1f1;
        }
    </style>
</head>
<body>
<div>
    <p>${Error}</p>
    <c:forEach items="${NewPost}" var="post">
        <p>${post.fullName}</p>
        <p>-----------------------------------</p>
        <p>${post.text}</p>
        <p>-----------------------------------</p>
        <p></p>
        <p/><p/>
    </c:forEach>

    <a href="/" style="color: #448AFF">Main</a>
</div>
</body>
</html>