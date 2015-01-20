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
<%@ page import="org.ralasafe.db.sql.xml.InExpr"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

String nodeId=request.getParameter( "nodeId" );
String pId=request.getParameter( "pId" );

ExprGroupTypeItem item=(ExprGroupTypeItem) request.getAttribute( "item" );
String operator="";
String varName1="";
String varName2="";

if( item!=null ) {
	if( item.getInExpr()!=null ) {
		varName1=item.getInExpr().getVariable1().getName();
		varName2=item.getInExpr().getVariable2().getName();
		operator="IN";
	} else if( item.getNotInExpr()!=null ) {
		varName1=item.getNotInExpr().getVariable1().getName();
		varName2=item.getNotInExpr().getVariable2().getName();
		operator="NOT IN";
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
<input type="hidden" name="exprType" value="in" />

<label><%=i18n.say( "Variable" )%> 1</label>
<select name="varName1">
<% for( int i=0; i<variables.length; i++ ) { 
		String varName=variables[i].getName();		
		boolean selected=varName.equals( varName1 );
%>
	<option value="<%=varName%>" <%=selected?"selected":"" %>><%=varName %></option>
<% } %>
</select> 
<br/>

<label><%=i18n.say( "Operator" )%></label> 
<select name="operator">
	<option value="IN">IN</option>
	<option value="NOT IN" <%="NOT IN".equals(operator)?"selected":"" %>>NOT IN</option>
</select>
	
	<br/>
<label><%=i18n.say( "Variable" )%> 2</label>
<select name="varName2">
<% for( int i=0; i<variables.length; i++ ) { 
		String varName=variables[i].getName();		
		boolean selected=varName.equals( varName2 );
%>
	<option value="<%=varName%>" <%=selected?"selected":"" %>><%=varName %></option>
<% } %>
</select> 
