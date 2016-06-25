<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>TestGetAd</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <script language="javascript" src="<%=basePath%>js/jquery-1.9.1.js" ></script>
    <script type="text/javascript">

    function onViewAdClick() {
        var adID = document.getElementById('adID').value;
        
        htmlobj = $.ajax({
            url : 'servlet/ViewAdDetailsServlet',
            data : {
                adID : adID
            },
            type : 'post',
            async : true,
            success : function(data) {
                var bean = JSON.parse(data);
                $('#result').html(bean.msg);
            }
        });
    }

    </script>

  </head>
  
  <body>
    <div id="result" style="FONT:12px 宋体"></div>
    <br>
    <form action="${pageContext.request.contextPath}/servlet/ViewAdDetailsServlet" method="post">
        <table class="table">
            <tbody>
                <tr>
                    <td><label>广告ID</label></td><td><input type="text" name="adID" id="adID" value="8" ></td>
                </tr>
                <tr>
                    <td><input type="button" value="查看" onclick="onViewAdClick()"></td>
                </tr>
            </tbody>
        </table>
    </form>
  </body>
</html>
