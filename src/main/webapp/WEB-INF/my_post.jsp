<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Post</title>
    <style>
        body {
            background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
            color: #f1f1f1;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">

</head>
<body>
<c:if test="${username.equals(I)}">

    <botton><a href="/post" style="text-align: right; color: #f1f1f1;text-decoration: none">Add Post</a></botton>


</c:if>

<c:forEach items="${NewPost}" var="post">


    <div class="brd">
        <c:if test="${I.equals(username)}">
            <form method="post">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="id" value="${post.id}"/>
                <button type="submit" style="color: red">Delete</button>
            </form>


        </c:if>
        <p></p>
        <a class=".titl" href="http://localhost:8081/page/${post.authorName}">${post.authorName}</a>
        <c:if test="${post.path!=null}">
            <img src="${contextPath}/resources/image/post/${post.path}.jpg" class="img">
        </c:if>
        <p>${post.text}</p>

    </div>

</c:forEach>

<a href="/" style="color: #448AFF">Main</a>
</div>
</body>
</html>