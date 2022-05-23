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


<div>
    <c:forEach items="${NewPost}" var="post">
        <c:if test="${I.username.equals(username)}">
            <form method="post">
                <input type="hidden" name="action" value="remove"/>
                <input type="hidden" name="id" value="${post.id}"/>
                <button type="submit" style="color: red">Delete</button>
            </form>
        </c:if>

        <div class="brd">
            <p><a class=".titl" href="http://localhost:8081/page/${post.authorName}">${post.authorName}</a><h4
                style="margin-left: 80%">${post.dateFormat()}</h4></p>
            <c:if test="${post.path!=null}">
                <div><p><img src="${contextPath}/resources/image/post/${post.path}.jpg" class="img"></p></div>
            </c:if>

            <p>${post.text}</p>
            <div style="margin-right: 70%">
                <form method="post">
                    <c:if test="${!Like.contains(post.id)}">

                        <p>
                            <input type="hidden" name="action" value="add"/>
                            <input type="hidden" name="id" value="${post.id}"/>
                            <button style="color: #448AFF">Like</button>
                        </p>
                    </c:if>
                    <c:if test="${Like.contains(post.id)}">

                        <p>
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="id" value="${post.id}"/>
                            <button style="color: #dddddd">Like</button>
                        </p>
                    </c:if>
                </form>

                <p>${post.countLike}</p>
            </div>
            <div class="brd_comment">
                <p>
                <form method="post">


                    <p><input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;"
                              name="text"></p>

                    <input type="hidden" name="action" value="addComment"/>
                    <input type="hidden" name="id" value="${post.id}"/>
                    <button type="submit" style="color: #448AFF">Comment</button>


                </form>
                </p>
                <c:forEach items="${Comment}" var="comment">
                    <c:if test="${comment.postId.equals(post.id)}">
                        <div>
                            <p>
                                    ${comment.commentAuthor}
                                <c:if test="${comment.userId ==I.id}">
                            <form method="post">
                                <input type="hidden" name="action" value="removeComment"/>
                                <input type="hidden" name="id" value="${comment.commentId}"/>
                                <button type="submit" style="color: red;width: 50px;height: 13px;text-align: center; font-size: 10px">Delete</button>
                            </form>
                            </c:if>
                            </p>
                            <p>${comment.commentText}</p>
                        </div>
                    </c:if>
                </c:forEach>
            </div>


        </div>

    </c:forEach>

    <a href="/" style="color: #448AFF">Main</a>
</div>
</body>
</html>