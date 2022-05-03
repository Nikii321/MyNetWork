<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 24.03.2022
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
  <title>Change your data</title>
</head>
<body >
<div>
  <form:form method="POST" modelAttribute="UserDetails">
    <h2>Changes</h2>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="nameUser" placeholder="${UserDetails.nameUser==null? 'Name':UserDetails.nameUser}"
                  autofocus="true"></form:input>
      <form:errors path="nameUser"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="surnameUser" placeholder="${UserDetails.surnameUser ==null? 'Surname':UserDetails.surnameUser}"
                  autofocus="true"></form:input>
      <form:errors path="surnameUser"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="birthday" placeholder="${UserDetails.birthday==null? '01.01.2003':UserDetails.birthday}"
                  autofocus="true"></form:input>
      <form:errors path="birthday"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="city" placeholder="${UserDetails.city==null? 'City':UserDetails.city}"
                  autofocus="true"></form:input>
      <form:errors path="city"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="university" placeholder="${UserDetails.university==null? 'University':UserDetails.university}"
                  autofocus="true"></form:input>
      <form:errors path="university"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" name="username" type="text" path="work" placeholder="${UserDetails.work==null? 'Work':UserDetails.work}"
                  autofocus="true"></form:input>
      <form:errors path="work"></form:errors>
    </div>
    <div>
      <form:input style="border-radius: 6.5px; height: 30px; width: 250px;background-color: seashell;" type="text" path="phone" placeholder="${UserDetails.phone==null? '7(999)-439-67-67':UserDetails.phone}"
                  autofocus="true"></form:input>
      <form:errors path="phone"></form:errors>
    </div>
    <button type="submit" style="width: 190px; height: 30px; background-color: skyblue; border-radius:6.5px;margin-left: 0px">Confirm</button>
  </form:form>
  <button  style="width: 190px; height: 30px; background-color: red; border-radius:6.5px;margin-left: 0px"><a href="/page/${I}" style="text-decoration: none">Close</a></button>
</div>
</body>
</html>