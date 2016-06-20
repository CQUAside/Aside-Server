<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'TestPicUpload.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <script language="javascript" src="<%=basePath%>js/jquery-1.9.1.js" ></script>
    <script language="javascript" src="<%=basePath%>js/ajaxfileupload.js" > </script>
    <script type="text/javascript">
        function ajaxPicUpload(){
            var userid = 10000;
            var token = 'YTU4NTA5YWFmYzQ2ZmQ3YjJhMTY1OWU3MGU0N2M3Yjc=';

            $.ajaxFileUpload({
                url:'<%=basePath %>servlet/PicUploadServlet',
                data : {
                    userid : userid,
                    token : token
                },
                type : 'post',
                async : true,
                secureuri : false,
                fileElementId : 'uploadFileInput',                         //文件选择框的id属性
                dataType : 'json',
                success : function (data, status)
                {
                    $('#result').html(data.msg);
                },
                error : function (data, status, e)
                {
                    $('#result').html('上传图片失败');
                }
            });
        }
    </script>
</head>

<body>
    <div id="result" style="FONT:12px 宋体"></div>
    <br />
    <form name="form_uploadImg" action="" method="POST" enctype="multipart/form-data">
        <input id="uploadFileInput" type="file" size="45" name="uploadFileInput" class="input" />
        <input type="button" id="buttonUpload" onclick="ajaxPicUpload()" value="上传图片" />
    </form>
</body>
</html>