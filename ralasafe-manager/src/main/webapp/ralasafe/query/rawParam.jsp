<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryRawHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.Parameter"%>        


<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryRawHandler handler=(QueryRawHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
Parameter[] params=query.getRawSQL().getParameter();
String id=request.getParameter( "id" );
%>

<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Type" )%></th>
		<th><%=i18n.say( "Value" )%></th>
		<th><%=i18n.say( "Action" )%>
		<a href="javascript:addParam()" title="Add"><img src="../images/add.gif"/></a></th>
	</tr>
<%
if( params!=null ) {
	for( int i=0; i<params.length; i++ ) {
		Parameter param=params[i];
		String[] s=handler.format( param );
%>
	<tr>
		<td><%=s[0]%></td>
		<td><%=s[1]%></td>
		<td>
			<a href="javascript:moveParam('top', <%=i%>);" title="Top"><img src="../images/top.gif"/></a>
			<a href="javascript:moveParam('up', <%=i%>);" title="Up"><img src="../images/up.gif"/></a>
			<a href="javascript:moveParam('down', <%=i%>);" title="Down"><img src="../images/down.gif"/></a>
			<a href="javascript:moveParam('bottom', <%=i%>);" title="Bottom"><img src="../images/bottom.gif"/></a>
			<a href="javascript:editParam(<%=i %>, <%=i%>);" title="Edit"><img src="../images/edit.gif"/></a>
			<a href="javascript:deleteParam(<%=i%>);" title="Delete"><img src="../images/delete.gif"/></a>
		</td>
	</tr>
<%
	}
}
%>
</table>
