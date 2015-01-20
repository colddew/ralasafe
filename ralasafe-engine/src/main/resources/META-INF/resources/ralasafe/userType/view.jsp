<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Collection,java.util.Iterator"%>    
<%@page import="org.ralasafe.util.I18N"%>
<%@page import="org.ralasafe.userType.UserType,org.ralasafe.metadata.user.*;"%>

<%
I18N i18n=I18N.getWebInstance( request );
String[] mainMenuHrefs=new String[]{
		"/ralasafe/userTypeMng.rls"		
};
String[] mainMenuTexts=new String[] {
		i18n.say("User_metadata"),
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="shortcut icon" href="./favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="./css/ralasafe.css" />
<title><%=i18n.say( "User_metadata_definition" )%></title>
</head>
<body>


<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>


<div class="smallContainer">
<h1><%=i18n.say( "User_metadata_definition" )%></h1>

<%
UserType userType=(UserType) request.getAttribute("userType" );
UserMetadata umd=userType.getUserMetadata();
TableMetadata tmd=umd.getMainTableMetadata();
Boolean editable=(Boolean) request.getAttribute( "editable" );
%>

<table class="ralaTable">
	<tr>
	
	<tr>
	<th><%=i18n.say( "Description" )%></th>
	<td><%=userType.getDesc()%></td>
	</tr>
	
	<tr>
	<th><%=i18n.say( "SQL_Table" )%></th>
	<td><%=tmd.getSqlTableName()%></td>
	</tr>	
</table>


<h1><%=i18n.say( "Property_column_mapping" )%></h1>

<table class="ralaTable">
	<tr>
	<% if( editable.booleanValue() ) { %>
	<th>Check</th>
	<% } %>
	<th><%=i18n.say( "Name" )%></th>
	<th><%=i18n.say( "Column" )%></th>
	<th><%=i18n.say( "Type" )%></th>
	</tr>
	
	
		<%
		FieldMetadata[] fmds=tmd.getFields();
		for( int i=0; i<fmds.length; i++ ) { %>
		<tr>
			<% if( editable.booleanValue() ) { %>
			<td><input type="checkbox" name="fields" value="<%=fmds[i].getName() %>"/></td>
			<%}%>
			<td><%=fmds[i].getName() %></td>
			<td><%=fmds[i].getColumnName() %></td>
			<td><%=fmds[i].getSqlType() %></td>
		</tr>
		<%} %>
</table>

	<% if( editable.booleanValue() ) {
	%>
	<input type="submit" value="Submit"/>&nbsp;&nbsp;
	<input type="reset"/>
	<% } %>
	<input type="button" value="<%=i18n.say( "Return" )%>" onclick="javascript:window.location='userTypeMng.rls'"/>

</div>

<jsp:include page="../footer.jsp"></jsp:include>

</body>
</html>