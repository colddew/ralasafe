<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>
     
<label><%=i18n.say( "Context_value" )%></label>
<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Property" )%></th>
		<th><%=i18n.say( "Type" )%></th>
		<th><%=i18n.say( "Value" )%></th>
	</tr>
<%
//AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
String[] testContextFields=(String[])request.getAttribute( "testContextFields" );

for( int i=0; i<testContextFields.length; i++ ) {
	String name=testContextFields[i];
%>
	<tr>
		<td><%=name%></td>
		<td>
			<select name="ctx<%=name%>type">
				<option value="java.lang.String">String</option>
				<option value="java.lang.Integer">Integer</option>
				<option value="java.lang.Double">Double</option>
				<option value="java.lang.Boolean">Boolean</option>
				<option value="java.util.Date">Date(y-m-d)</option>
			</select>
		</td>
		<td><input type="text" name="ctx<%=name%>"/></td>
	</tr>
<% } %>
</table>