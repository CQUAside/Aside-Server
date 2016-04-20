<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="./js/bootstrap.min.css" rel="stylesheet">
<title>注册</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/servlet/RegisteSimpleServlet" method="post">
	<table class="table">
		<tbody>
			<tr><td><input class="form-control" type="text" name="username" placeholder="用户名"></td></tr>
			<tr><td><input type="password" name="password" placeholder="密码"></td></tr>
			<tr><td>邮箱、电话。。省略</td></tr>
			<tr><td><input type="submit" value="注册"></td><td><input type="button" value="清空"></td></tr>
		</tbody>
	</table>
</form>
</body>
</html>