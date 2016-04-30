<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="./js/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript">
    function onAccountTextBlur() {
        var account = document.getElementById("account").value;
        if (account == null || account == "") {
            alert("账号为空，请输入正确的账号");
            return;
        }
        htmlobj = $.ajax({
            url : 'servlet/CheckAccountLegalServlet',// 跳转到 action  
            data : {
                account : account,
            },
            type : 'post',
            async : true,
            success : function(data) {
                var bean = JSON.parse(data);
                if (bean.statusCode == 1000 || bean.statusCode == 1001) {
                    alert(bean.msg);
                } else {
                    alert(bean.msg);
                }
            }
        });
    }
    

    function onConfirmPasswordBlur() {
        var password = document.getElementById("password").value;
        var conpawo = document.getElementById("conpawo").value;

        if (password === conpawo) {
            return;
        } else {
            alert("两次密码输入不同，请重新输入");
        }
    }

    function onSendAuthCode() {
        var phone = document.getElementById("phone").value;
        if (phone === null || phone === "") {
            alert("请输入正确的手机号");
            return;
        }

        htmlobj = $.ajax({
            url : 'servlet/SendRegisterPhoneAuthCodeServlet',
            data : {
                phone : phone,
            },
            type : 'post',
            async : true,
            success : function(data) {
                var bean = JSON.parse(data);
                if (bean.statusCode == 1000) {
                    alert(bean.msg);
                } else if (bean.statusCode === 6003) {

                } else {
                    alert(bean.msg);
                }
            }
        });
    }
</script>
<title>注册</title>
</head>
<body>
    <form
        action="${pageContext.request.contextPath}/servlet/RegisterAccountServlet"
        method="post">
        <table class="table">
            <tbody>
                <tr>
                    <td><input class="form-control" type="text" name="account" id="account" placeholder="用户名" onblur="onAccountTextBlur()"></td>
                </tr>
                <tr>
                    <td><input type="password" name="password" id="password" placeholder="密码" ></td>
                </tr>
                <tr>
                    <td><input type="password" name="conpawo" id="conpawo" placeholder="确认密码" onblur="onConfirmPasswordBlur()" ></td>
                </tr>
                <tr>
                    <td><input type="text" name="phone" id="phone" placeholder="手机号码"></td><td><input type="button" value="发送验证码" onclick="onSendAuthCode()" ></td>
                </tr>
                <tr>
                    <td><input type="text" name="authcode" id="authcode" placeholder="验证码"></td>
                </tr>
                <tr>
                    <td><input type="button" value="注册" onclick="" ></td>
                    <td><input type="button" value="清空" onclick="" ></td>
                </tr>
            </tbody>
        </table>
    </form>
</body>
</html>