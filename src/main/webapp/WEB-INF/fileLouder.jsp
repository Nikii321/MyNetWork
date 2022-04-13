<%--
  Created by IntelliJ IDEA.
  User: nikolajvereschagin
  Date: 27.03.2022
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
</head>
<body>
<form action="${Name}" method="post" enctype="multipart/form-data">
  <p>Choose file:<input type="file" name="file" /></p>
  <p><input type="submit"  value=«Send»/></p>
  <a href="/page/${Name}"> Comeback </a>
</form>

</body>
</html>