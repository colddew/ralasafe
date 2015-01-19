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
<%@ page import="org.ralasafe.db.sql.xml.UserValue" %>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

DefineVariable var=(DefineVariable) request.getAttribute( "variable" );
String oper="addVariable";
String name="";
String key="";
String varIndex="";
String id=request.getParameter( "id" );
String[] userFields=(String[]) request.getAttribute( "userFields" );

if( var!=null ) {
	UserValue userValue=var.getUserValue();
	
	varIndex=request.getParameter( "index" );
	oper="updateVariable";
	key=userValue.getKey();
	name=var.getName();
}
%>


	<input type="hidden" name="oper" value="<%=oper%>"/>
	<input type="hidden" name="id" value="<%=id%>"/>
	<input type="hidden" name="type" value="userValue"/>
	<input type="hidden" name="index" value="<%=varIndex%>"/>
	<label><%=i18n.say( "Variable_name" )%></label>
	<input type="text" name="name" value="<%=name%>" class="required"/>
	<br/>
	<label><%=i18n.say( "User_attribute_value" )%></label>
	<select name="key">
	<% for( int i=0; i<userFields.length; i++ ) { 
			String userField=userFields[i];
			boolean selected=userField.equals( key );
	%>
		<option value="<%=userField %>" <%=selected?"selected":"" %>><%=userField %></option>
	<% } %>
	</select> 
