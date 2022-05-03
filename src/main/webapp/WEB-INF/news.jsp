<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>News</title>
    <style>
        body {
            background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
            color: #f1f1f1;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">

</head>
<body>
<div>
    <p>${Error}</p>
    <c:forEach items="${NewPost}" var="post">

        <div class="brd">
            <a class=".titl" href="http://localhost:8081/page/${post.authorName}"><p>${post.authorName}</p></a>
            <c:if test="${post.path!=null}">
                <p><img src="${contextPath}/resources/image/post/${post.path}.jpg" class="img"></p>
            </c:if>
            <p>${post.text}</p>

        </div>

    </c:forEach>

    <a href="/" style="color: #448AFF">Main</a>
</div>
</body>
</html>