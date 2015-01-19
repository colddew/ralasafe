<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.db.sql.xml.ExpressionGroupTypeItem"%>
<%@page import="org.ralasafe.db.sql.xml.ExpressionGroup"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
String id=request.getParameter( "id" );
String nodeId=request.getParameter( "nodeId" );
Object obj=request.getAttribute( "item" );
ExpressionGroupTypeItem item=null;
ExpressionGroup group=null;

if( obj instanceof ExpressionGroupTypeItem ) {
	item=(ExpressionGroupTypeItem) obj;
	group=item.getExpressionGroup();
} else if( obj instanceof ExpressionGroup ) {
	group=(ExpressionGroup) obj;
}
%> 


<%if( group!=null ) { %>
<a href="javascript:addWhereChildExpr('<%=nodeId%>','binary')"><%=i18n.say( "Add_binary_expression" )%></a><br/>
<a href="javascript:addWhereChildExpr('<%=nodeId%>','in')"><%=i18n.say( "Add_in_expression" )%></a><br/>
<a href="javascript:addWhereChildExpr('<%=nodeId%>','null')"><%=i18n.say( "Add_null_expression" )%></a><br/>
<a href="javascript:addWhereChildExprGroup('<%=nodeId%>')"><%=i18n.say( "Add_expression_group" )%></a><br/>
<a href="javascript:editWhereExprGroupType('<%=nodeId%>','<%=group.getLinker().toString()%>')"><%=i18n.say( "Edit" )%></a><br/>
<% } else {%>
<a href="javascript:editWhereExpr('<%=nodeId%>')"><%=i18n.say( "Edit" )%></a><br/>
<% } %>

<% if( !"0".equals( nodeId ) ) { %>
<a href="javascript:deleteWhereExpr('<%=nodeId%>')"><%=i18n.say( "Delete" )%></a>
<% } %>
