<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.db.sql.xml.ExprGroupTypeItem"%>
<%@page import="org.ralasafe.db.sql.xml.ExprGroup"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

String id=request.getParameter( "id" );
String nodeId=request.getParameter( "nodeId" );
Object obj=request.getAttribute( "item" );
ExprGroupTypeItem item=null;
ExprGroup group=null;

if( obj instanceof ExprGroupTypeItem ) {
	item=(ExprGroupTypeItem) obj;
	group=item.getExprGroup();
} else if( obj instanceof ExprGroup ) {
	group=(ExprGroup) obj;
}
%> 


<%if( group!=null ) { %>
<a href="javascript:addExprChildExpr('<%=nodeId%>','binary')"><%=i18n.say( "Add_binary_expression" )%></a><br/>
<a href="javascript:addExprChildExpr('<%=nodeId%>','in')"><%=i18n.say( "Add_in_expression" )%></a><br/>
<a href="javascript:addExprChildExpr('<%=nodeId%>','null')"><%=i18n.say( "Add_null_expression" )%></a><br/>
<a href="javascript:addExprChildExprGroup('<%=nodeId%>')"><%=i18n.say( "Add_expression_group" )%></a><br/>
<a href="javascript:editExprGroupType('<%=nodeId%>','<%=group.getLinker().toString()%>')"><%=i18n.say( "Edit" )%></a><br/>
<% } else {%>
<a href="javascript:editExpr('<%=nodeId%>')"><%=i18n.say( "Edit" )%></a><br/>
<% } %>

<% if( !"0".equals( nodeId ) ) { %>
<a href="javascript:deleteExpr('<%=nodeId%>')"><%=i18n.say( "Delete" )%></a>
<% } %>
