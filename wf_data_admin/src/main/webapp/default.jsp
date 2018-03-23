<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page language="java" import="org.jasig.cas.client.util.AssertionHolder" %>
<%
  String path = request.getContextPath();
  String loginName = AssertionHolder.getAssertion().getPrincipal().getName();
  //String loginName = "admin";
  request.getSession().setAttribute("userName", loginName);
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <link rel="shortcut icon" href="" type="image/x-icon"/>
  <title>玩蜂大数据分析管理平台</title>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body id="ext-body">
<div id="defaultLoading"><span class="title"><span id="defaultLoading-msg">正在加载首页,请稍后...</span>
        </span><span class="logo"></span></div>
<div id="viewport">
  <link href="resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
  <link href="resources/css/icon.css" rel="stylesheet" type="text/css"/>
  <link href="resources/css/app.css" rel="stylesheet" type="text/css"/>
  <link href="resources/css/content.css" rel="stylesheet" type="text/css"/>
  <script src="resources/ext-all.js" type="text/javascript"></script>
  <script src="resources/jquery.min.js" type="text/javascript"></script>
  <script src="resources/ext-lang-zh_CN.js" type="text/javascript"></script>
  <script src="app/app-all.js" type="text/javascript"></script>
  <script src="app/echarts.js" type="text/javascript"></script>
  <script src="app/MonthField.js" type="text/javascript"></script>
  <script src="app/app-base.js" type="text/javascript"></script>
  <script src="app/commFunction.js" type="text/javascript"></script>
  <script src="config.js" type="text/javascript"></script>
  <link href="resources/kindeditor/themes/default/default.css" rel="stylesheet"/>
  <script src="resources/kindeditor/kindeditor-all-min.js"></script>
  <script src="resources/kindeditor/lang/zh_CN.js"></script>
</div>
</body>
</html>
<script type="text/javascript">
  window.baseUrl = "<%=basePath%>";
  var $gridColumn = 'background-color:#d9edfe; background-image: none;';
  var $USER = {
    longName: "<%=loginName%>"
  }
  //禁止后退键 作用于Firefox、Opera
  document.onkeypress = forbidBackSpace;
  //禁止后退键  作用于IE、Chrome
  document.onkeydown = forbidBackSpace;
</script>
