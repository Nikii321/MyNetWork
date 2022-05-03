<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 21.03.2022
  Time: 22:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>My page</title>
    <link rel="script" type="javascript" href="${contextPath}/resources/js/main.js">

    <link rel="stylesheet" type="text/css" href="${contextPath}/resources/css/style.css">

</head>
<body style="text-align: center; font-size: 20px">

<div>
    <img src="${contextPath}/resources/image/${username}.jpg" style="height: 400px; width: 600px"/>
    <p/>
    <h>Name: ${UserDetails.nameUser}</h>


    <p></p>
    <h>Surname: ${UserDetails.surnameUser}</h>

    <p></p>
    <h>Birthday: ${UserDetails.birthday}</h>

    <p></p>
    <h>City: ${UserDetails.city}</h>


    <p></p>
    <h>University: ${UserDetails.university}</h>


    <p></p>
    <h>Work: ${UserDetails.work}</h>

    <p></p>
    <p></p>
    <h>Phone: ${UserDetails.phone}</h>
    <p></p>
    <c:if test="${!username.equals(I)}">

        <form action="${pageContext.request.contextPath}/page/${username}" method="post">
            <button type="submit"> <c:if test="${!SubscribeButton}">Subscribe</c:if>
                <c:if test="${SubscribeButton}">Unsubscribe</c:if></button>



        </form>
    </c:if>
    <a href="/subscriptions/${username}" style="text-decoration: none; color: black">Subscriptions</a>: ${SubscriptionsCount}          <a href="/subscribers/${username}" style="text-decoration: none; color: black">Subscribers</a>: ${SubscribersCount}

    <p/>
    <p/>
    <c:if test="${username.equals(I)}">

        <botton><a href="/change"> Change</a></botton>
    <botton><a href="/post/${username}">Posts</a></botton>

        <p/>

        <a href="/fileUpload">Upload Image </a>
    </c:if>
    <p/>
    <p/>

    <a href="/">Main</a>


</div>

</body>
</html>