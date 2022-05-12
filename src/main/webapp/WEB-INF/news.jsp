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
    <c:forEach items="${NewPost}" var="post">

        <div class="brd">
            <p><a class=".titl" href="http://localhost:8081/page/${post.authorName}" >${post.authorName}</a><h4
                    style="margin-left: 80%">${post.dateFormat()}</h4></p>
            <c:if test="${post.path!=null}">
                <div><p><img src="${contextPath}/resources/image/post/${post.path}.jpg" class="img"></p></div>
            </c:if>

            <p>${post.text}</p>
            <div style="margin-right: 70%">
                <form method="post">
                    <c:if test="${!Like.contains(post.id)}">
                        <input type="hidden" name="action" value="add"/>
                        <input type="hidden" name="id" value="${post.id}"/>
                        <p>
                            <button style="color: #448AFF">Like</button>
                        </p>
                    </c:if>
                    <c:if test="${Like.contains(post.id)}">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${post.id}"/>
                        <p>
                            <button style="color: #dddddd">Like</button>
                        </p>
                    </c:if>
                </form>
                <p>${post.countLike}</p>
            </div>


        </div>

    </c:forEach>

    <a href="/" style="color: #448AFF">Main</a>
</div>
</body>
</html>