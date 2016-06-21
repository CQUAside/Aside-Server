<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>TestPublishAd.jsp</title>
    
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
    function onPublishAdClick() {
        var userid = 10000;
        var token = 'YTU4NTA5YWFmYzQ2ZmQ3YjJhMTY1OWU3MGU0N2M3Yjc=';
        
        var adTitle = document.getElementById('adTitle').value;
        var adLogoImgID = '2016062017034511700000100005477';
        var adImgIDSetStr = '2016062017034511700000100005477-2016062017034625700000100005026';
        var adDescription = document.getElementById('adDescription').value;
        var adAreaSetStr = '1001-1002-1003';
        var mDate = new Date();
        mDate.setFullYear(2016, 6, 20);
        mDate.setHours(12, 0, 0, 0);
        var adStartTime = mDate.getTime();
        var adEndTime = adStartTime + 1000 * 60 * 60 * 24 * 10;
        
        htmlobj = $.ajax({
            url : 'servlet/PublishAdServlet',
            data : {
                userid : userid,
                token : token,
                adTitle : adTitle,
                adLogoImgID : adLogoImgID,
                adImgIDSetStr : adImgIDSetStr,
                adDescription : adDescription,
                adAreaSetStr : adAreaSetStr,
                adStartTime : adStartTime + '',
                adEndTime : adEndTime + '',
                listPriority : 'false',
                carousel : 'false'
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
    <form action="${pageContext.request.contextPath}/servlet/PublishAdServlet" method="post">
        <table class="table">
            <tbody>
                <tr>
                    <td><label>广告标题</label></td><td><input type="text" name="adTitle" id="adTitle" value="慢出一辆挑战者600，暑假骑了一遍308，大刀慎入" ></td>
                </tr>
                <tr>
                    <td><label>广告描述</label></td><td><input type="text" name="adDescription" id="adDescription" value="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean euismod bibendum laoreet. Proin gravida dolor sit amet lacus accumsan et viverra justo commodo. Proin sodales pulvinar tempor. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam fermentum, nulla luctus pharetra vulputate, felis tellus mollis orci, sed rhoncus sapien nunc eget odio."></td>
                </tr>
                <tr>
                    <td><input type="button" value="发布" onclick="onPublishAdClick()"></td>
                </tr>
            </tbody>
        </table>
    </form>
  </body>
</html>
