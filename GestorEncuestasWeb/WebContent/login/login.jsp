<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2 align="center">Login(Gestion Productos)</h2>
	<form method="POST" action='<%= response.encodeURL("j_security_check") %>' >
  		Username:<input type="text" name="j_username">
  		Password:<input type="password" name="j_password">
  		<input type="submit" value="Log In">
	</form>
</body>
</html>