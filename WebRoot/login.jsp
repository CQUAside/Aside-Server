<i><%@page language="java" import="java.util.*"
        contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
    <%
        String path = request.getContextPath();
    			String basePath = request.getScheme() + "://"
    					+ request.getServerName() + ":" + request.getServerPort()
    					+ path + "/";
    %> <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <html>
<head>
<base href="<%=basePath%>">

<title>Login</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">

    function onLoginClick() {
        var account = document.getElementById("account").value;
        var password = document.getElementById("password").value;
        if (account == null || account == "") {
            alert("账号为空，请输入正确的账号");
            return;
        }
        htmlobj = $.ajax({
            url : 'servlet/LoginServlet',// 跳转到 action  
            data : {
                account : account,
                password : password,
            },
            type : 'post',
            async : true,
            success : function(data) {
                var bean = JSON.parse(data);
                alert(bean.msg);
            }
        });
    }
</script>

</head>

<body>
    <form
        action="${pageContext.request.contextPath}/servlet/LoginServlet"
        method="post">
        <table class="table">
            <tbody>
                <tr>
                    <td><input class="form-control" type="text" name="account" id="account" placeholder="用户名"></td>
                </tr>
                <tr>
                    <td><input type="password" name="password" id="password" placeholder="密码"></td>
                </tr>
                <tr>
                    <td><input type="button" value="登陆" onclick="onLoginClick()"></td>
                    <td><input type="button" value="关闭" onclick=""></td>
                </tr>
            </tbody>
        </table>
    </form>
</body>
    </html> </i>