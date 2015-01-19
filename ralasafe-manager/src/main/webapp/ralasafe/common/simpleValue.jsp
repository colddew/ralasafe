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
<%@ page import="org.ralasafe.db.sql.xml.SimpleValue"%>
<%@ page import="org.ralasafe.db.sql.xml.SimpleValueType"%>
<%@ page import="org.ralasafe.db.sql.xml.types.SimpleValueTypeTypeType"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

DefineVariable var=(DefineVariable) request.getAttribute( "variable" );
String oper="addVariable";
String name="";
String content="";
String varIndex="-1";
String id=request.getParameter( "id" );
String type=""+SimpleValueTypeTypeType.STRING;

if( var!=null ) {
	SimpleValue value=var.getSimpleValue();
	
	varIndex=request.getParameter( "index" );
	oper="updateVariable";
	content=value.getContent();
	type=""+value.getType();
	name=var.getName();
}
%>


	
<input type="hidden" name="oper" value="<%=oper%>"/>
	<input type="hidden" name="id" value="<%=id%>"/>
	<input type="hidden" name="type" value="simpleValue"/>
	<input type="hidden" name="index" value="<%=varIndex%>"/>
	<label><%=i18n.say( "Variable_name" )%></label>
	<input type="text" name="name" value="<%=name%>" class="required"/>
	<br/>
	<label><%=i18n.say( "Type" )%></label> 
	<select name="stype">
		<option value="<%=SimpleValueTypeTypeType.STRING%>" <%=type.equals(""+SimpleValueTypeTypeType.STRING)?"selected":"" %>>String</option> 
		<option value="<%=SimpleValueTypeTypeType.INTEGER%>" <%=type.equals(""+SimpleValueTypeTypeType.INTEGER)?"selected":"" %>>Integer</option>
		<option value="<%=SimpleValueTypeTypeType.FLOAT%>" <%=type.equals(""+SimpleValueTypeTypeType.FLOAT)?"selected":"" %>>Float</option>
		<option value="<%=SimpleValueTypeTypeType.BOOLEAN%>" <%=type.equals(""+SimpleValueTypeTypeType.BOOLEAN)?"selected":"" %>>Boolean</option>
		<option value="<%=SimpleValueTypeTypeType.DATETIME%>" <%=type.equals(""+SimpleValueTypeTypeType.DATETIME)?"selected":"" %>>Datetime(y-m-d h:s)</option>
	</select>
	<br/>
	<label><%=i18n.say( "Value" )%></label>
	<input type="text" name="content" value="<%=content %>" class="required"/>
