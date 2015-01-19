<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@page import="java.util.Collection,java.util.Iterator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="org.ralasafe.demo.DemoUtil"%>
<%@page import="java.util.Locale"%><html>
<head>
<%
	Locale locale = request.getLocale();
%>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<title>访问被禁止</title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>


<h2><font color="red">访问被禁止</font></h2>

<input type="button"
	value="返回"
	onclick="javascript:history.go(-1)" />


<jsp:include page="../footer.jsp"></jsp:include>


</body>
</html>