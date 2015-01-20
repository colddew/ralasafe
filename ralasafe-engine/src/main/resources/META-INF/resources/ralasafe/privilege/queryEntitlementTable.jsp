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
<%@page import="org.ralasafe.servlet.QueryEntitlementHandler"%>
<%@page import="org.ralasafe.privilege.Privilege"%>
<%@page import="org.ralasafe.entitle.QueryEntitlement"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryEntitlementHandler handler=(QueryEntitlementHandler) request.getAttribute( "handler" );
String id=request.getParameter("id");

Privilege pvlg=handler.getPrivilege();
Collection entitlements=handler.getQueryEntitlements();
%>

	<table class="ralaTable">
		<tr>
			<th><%=i18n.say( "User_category" )%></th>
			<th><%=i18n.say( "Query" )%></th>
			<th><%=i18n.say( "Description" )%></th>
			<th><%=i18n.say( "Action" )%>
			<a href="javascript:addEntitle()" title="<%=i18n.say("Add") %>"><img src="../images/add.gif"/></a>
			</th>
		</tr>
		<%
		int i=-1;
		for( Iterator iter=entitlements.iterator(); iter.hasNext(); ) {
			QueryEntitlement entitle=(QueryEntitlement) iter.next();
			i++;
			%>
		<tr>
			<td><%=entitle.getUserCategory().getName() %></td>
			<td><%=entitle.getQuery().getName() %></td>
			<td><%=entitle.getDescription() %></td>
			<td><a href="javascript:moveEntitle('top', <%=i%>);" title="<%=i18n.say("Top") %>"><img src="../images/top.gif"/></a>
				<a href="javascript:moveEntitle('up', <%=i%>);" title="<%=i18n.say("Up") %>"><img src="../images/up.gif"/></a>
				<a href="javascript:moveEntitle('down', <%=i%>);" title="<%=i18n.say("Down") %>"><img src="../images/down.gif"/></a>
				<a href="javascript:moveEntitle('bottom', <%=i%>);" title="<%=i18n.say("Bottom") %>"><img src="../images/bottom.gif"/></a>
				<a href="javascript:editEntitle(<%=i %>, <%=i%>);" title="<%=i18n.say("Edit") %>"><img src="../images/edit.gif"/></a>
				<a href="javascript:deleteEntitle(<%=i%>);" title="<%=i18n.say("Delete") %>"><img src="../images/delete.gif"/></a>			</td>
		</tr>			
		<% } %>
	</table>