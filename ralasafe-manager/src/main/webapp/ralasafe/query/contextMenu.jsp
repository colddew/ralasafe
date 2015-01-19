<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.entitle.Query"%>
<% 
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
Query node=(Query) request.getAttribute( "node" );
int id=node.getId();
boolean isLeaf=node.getIsLeaf();
String name=node.getName();
String description=node.getDescription();
%>

<%=isLeaf?i18n.say( "Query" ):i18n.say( "Query_group" )%>  <i><%=name%></i>:<p/>

<%if( id<0 ) { %>
The reserved node is not editable.
<% } %>

<%
if( !isLeaf ) {
%>
<a href="javascript:createNode('<%=id%>')"><%=i18n.say( "Create" ) %></a>&nbsp;&nbsp;&nbsp;
<% } else { %>
<a href="javascript:copyNode('<%=id%>')"><%=i18n.say( "Copy" ) %></a>&nbsp;&nbsp;&nbsp;
<% } %>

<%
if( id>0 ) {
%>
<a href="javascript:deleteNode('<%=id %>')"><%=i18n.say( "Delete" ) %></a>&nbsp;&nbsp;&nbsp;
<a href="javascript:modifyNode('<%=id %>')"><%=i18n.say( "Edit" ) %></a>&nbsp;&nbsp;&nbsp;
<%
}
%>

<%
if( isLeaf&&id>0 ) {
%>
<a href="./policy.rls?oper=editPolicy&id=<%=id %>"><%=i18n.say( "Edit_policy" ) %></a>&nbsp;&nbsp;&nbsp;
<a href="./policy.rls?oper=testPolicy&id=<%=id %>"><%=i18n.say( "Test" ) %></a>
<%
}
%>
