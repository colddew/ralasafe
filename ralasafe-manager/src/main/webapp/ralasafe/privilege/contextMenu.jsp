<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.privilege.Privilege"%>
<% 
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

Privilege node=(Privilege) request.getAttribute( "node" );
int id=node.getId();
boolean isLeaf=node.getIsLeaf();
String name=node.getName();
String description=node.getDescription();

int assignUserRolePvlgId=Privilege.ASSIGN_ROLE_TO_USER_ID;
%>

<%=isLeaf?i18n.say( "Privilege" ):i18n.say( "Privilege_group" )%>  <i><%=name%></i>:<p/>

<%if( id<-1&&id!=assignUserRolePvlgId ) { %>
The reserved node is not editable.
<% } else if( id==assignUserRolePvlgId ) { %>
<a href="./policy.rls?oper=editPolicy&id=<%=id %>"><%=i18n.say( "Edit_policy" ) %></a>&nbsp;&nbsp;&nbsp;
<a href="./policy.rls?oper=testPolicy&id=<%=id %>"><%=i18n.say( "Test" ) %></a>
<% } %>

<%
if( !isLeaf ) {
%>
  <a href="javascript:createNode('<%=id%>')"><%=i18n.say( "Create" ) %></a>&nbsp;&nbsp;&nbsp;  
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
