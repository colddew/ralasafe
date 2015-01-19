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

String[] testBusinessDataFields=(String[])request.getAttribute("testBusinessDataFields");
String[] testBusinessDataFieldTypes=(String[])request.getAttribute("testBusinessDataFieldTypes");

String bdClass=(String) request.getAttribute( "bdClass" );
if( bdClass==null ) {
	bdClass="";
}
%>

<label><%=i18n.say( "Business_data" )%></label>
<label>Class</label>
<input type="text" name="bdClass" value="<%=bdClass %>" size="56" class="required" onkeypress="if(event.keyCode == 13) bdClassEnter()"/>
<input type="button" value="Load" onclick="javascript:loadJavabeanProperty()" />

<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Property" )%></th>
		<th><%=i18n.say( "Type" )%></th>
		<th><%=i18n.say( "Value" )%></th>
	</tr>
<%


for( int i=0; i<testBusinessDataFields.length; i++ ) {
	String name=testBusinessDataFields[i];
%>
	<tr>
		<td><%=name%></td>
		<td><%=testBusinessDataFieldTypes[i]%>
		<input type="hidden" name="bd<%=name%>type" value="<%=testBusinessDataFieldTypes[i] %>"/></td>
		<td><input type="text" name="bd<%=name%>" <%="".equals(bdClass)?"disabled":"" %>/></td>
	</tr>
<% } %>
</table>

<script>
function bdClassEnter() {
	loadJavabeanProperty(); 
	return false;
}

// set datapicker
<% if( !"".equals(bdClass) ) {
	for( int i=0; i<testBusinessDataFieldTypes.length; i++ ) {
		String type=testBusinessDataFieldTypes[i];
		
		if( "java.util.Date".equals( type )
				|| "java.sql.Date".equals( type ) ) {%>
setDataPicker( "bd<%=testBusinessDataFields[i]%>" );
<%		}
	}
} %>
</script>