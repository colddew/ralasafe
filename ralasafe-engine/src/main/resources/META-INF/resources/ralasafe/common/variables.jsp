<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.ralasafe.db.sql.xml.DefineVariable" %>
<%@ page import="org.ralasafe.servlet.AbstractPolicyDesignHandler" %>
<%@ page import="org.ralasafe.db.sql.xml.ContextValue" %>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

DefineVariable[] variables=(DefineVariable[])request.getAttribute("variables");
AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
String id=request.getParameter( "id" );
%>

<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Name" )%></th>
		<th><%=i18n.say( "Type" )%></th>
		<th><%=i18n.say( "Value" )%></th>
		<th><%=i18n.say( "Action" )%>
		<a href="javascript:selectAddVariableType()" title="Add"><img src="../images/add.gif"/></a>
		</th>
	</tr>
	
	<% for( int i=0; i<variables.length; i++ ) {
		DefineVariable var=variables[i];
		String[] format=handler.format( var );
	%>	
	<tr>
		<td><%=format[1]%></td>
		<td><%=i18n.say(format[0])%></td>
		<td><%=format[2]%></td>
		<td>
			<a href="javascript:editVariable(<%=i %>);" title="<%=i18n.say( "Edit" )%>"><img src="../images/edit.gif"/></a>
			<a href="javascript:deleteVariable(<%=i %>);" title="<%=i18n.say( "Delete" )%>"><img src="../images/delete.gif"/></a>
		</td>
	</tr>		
	<% } %>
</table>