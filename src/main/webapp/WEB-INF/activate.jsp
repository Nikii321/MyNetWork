<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
    Welcome to MyNetWork
    <style>
        body{
            background-image: url("${pageContext.request.contextPath}/resources/image/defoult.jpg");
        }
    </style>
</head>
<body>
<p>
    ${message}
</p>
<p>Thanks</p>
<p></p>
<a href="/news">Comeback</a>

</body>
</html>