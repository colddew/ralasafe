<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.ralasafe.privilege.Privilege"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  


<%@page import="org.ralasafe.util.StringUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link type="text/css" rel="stylesheet" href="../css/syntaxhighlighter/shCoreDefault.css"/> 

<script src="../js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shCore.js"></script> 
<script type="text/javascript" src="../js/syntaxhighlighter/shBrushJava.js"></script> 

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>

<title><%=i18n.say( "Privilege_Constants" )%></title>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<%
Collection businessPrivileges=(Collection) request.getAttribute("businessPrivileges");
Collection nonRolePrivileges=(Collection) request.getAttribute("nonRolePrivileges");
%>

<div class="smallContainer">
<label><%=i18n.say( "Export_privilege_constants" )%></label>
<pre class="brush: java;">
//------------ COPY THESE CODE TO YOUR CONSTANTS CLASS --
//------------ business privilege constants -------------
<%
for( Iterator iter=businessPrivileges.iterator(); iter.hasNext(); ) {
	Privilege pvlg=(Privilege) iter.next();
	if( pvlg.getIsLeaf() ) {
%>
	<% if( !StringUtil.isEmpty( pvlg.getDescription() ) ) { %>
/**
  * <%=pvlg.getDescription() %>
  */ 	<% } %>
public static final int <%=pvlg.getConstantName()%>=<%=pvlg.getId() %>;
<%  }
}%>

//------------ non role privilege constants -------------
<%
for( Iterator iter=nonRolePrivileges.iterator(); iter.hasNext(); ) {
	Privilege pvlg=(Privilege) iter.next();
	if( pvlg.getIsLeaf() ) {
%>
	<% if( !StringUtil.isEmpty( pvlg.getDescription() ) ) { %>
/**
  * <%=pvlg.getDescription() %>
  */<% } %>
public static final int <%=pvlg.getConstantName()%>=<%=pvlg.getId() %>;
<%  }
}%>
</pre>
</div>


<jsp:include page="../footer.jsp"></jsp:include>

<script>
jQuery( document ).ready( function() {
	SyntaxHighlighter.all();
});
</script>
</body>
</html>