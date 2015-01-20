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
<%@ page import="org.ralasafe.db.sql.xml.Formula"%>
<%@ page import="org.ralasafe.db.sql.xml.ExprGroupTypeItem"%>
<%@ page import="org.ralasafe.db.sql.xml.BinaryExpr"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

String nodeId=request.getParameter( "nodeId" );
String pId=request.getParameter( "pId" );

ExprGroupTypeItem item=(ExprGroupTypeItem) request.getAttribute( "item" );
BinaryExpr expr=null;
String nullType="";
String varName="";

if( item!=null ) {
	if( item.getIsNotNullExpr()!=null ) {
		varName=item.getIsNotNullExpr().getVariable().getName();
		nullType="NOT NULL";
	} else if( item.getIsNullExpr()!=null ) {
		varName=item.getIsNullExpr().getVariable().getName();
		nullType="NULL";
	}
}

AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
DefineVariable[] variables=handler.getVariables();
%>

<% if( nodeId!=null ) { %>
<input type="hidden" name="nodeId" value="<%=nodeId %>" />
<input type="hidden" name="oper" value="editExpr" />
<% } %>
<% if( pId!=null ) { %>
<input type="hidden" name="pId" value="<%=pId %>" />
<input type="hidden" name="oper" value="addExprChild" />
<% } %>
<input type="hidden" name="exprType" value="null" />

<label><%=i18n.say( "Variable" )%></label> 
<select name="varName">
<% for( int i=0; i<variables.length; i++ ) { 
		String tempName=variables[i].getName();		
		boolean selected=tempName.equals( varName );
%>
	<option value="<%=tempName%>" <%=selected?"selected":"" %>><%=tempName %></option>
<% } %>
</select> 
<br/>

<label><%=i18n.say( "Operator" )%></label> 
<select name="operator">
	<option value="NULL" <%=nullType.equals("NULL")?"selected":"" %>>NULL</option>
	<option value="NOT NULL" <%=nullType.equals("NOT NULL")?"selected":"" %>>NOT NULL</option>
</select>