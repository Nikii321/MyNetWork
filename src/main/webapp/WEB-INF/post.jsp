<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 19.04.2022
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><html>
<head>
    <title>Change your data</title>
</head>
<body >
<div>
    <form:form method="POST" modelAttribute="NewPost">
        <h2>Changes</h2>
        <div>
            <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="text" type="text" path="text" placeholder="Text"
                        autofocus="true"></form:input>
        </div>
        <button type="submit" style="width: 190px; height: 30px; background-color: skyblue; border-radius:6.5px;margin-left: 0px">Confirm</button>
    </form:form>
    <button  style="width: 190px; height: 30px; background-color: red; border-radius:6.5px;margin-left: 0px"><a href="/page/${Name}" style="text-decoration: none">Close</a></button>
</div>
</body>
</html>