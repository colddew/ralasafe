<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryRawHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.Column"%> 

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryRawHandler handler=(QueryRawHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
Column column=(Column) request.getAttribute( "column" );
String id=request.getParameter( "id" );

String[][] mcpats=(String[][]) request.getAttribute( "mappingClassPropertyAndTypes" );
String prop="";
String columnName="";
boolean readOnly=false;
if( column!=null ) {
	prop=column.getProperty();
	readOnly=column.getReadOnly();
	columnName=column.getName();
}
%>


<input type="hidden" name="oper" value="<%=column==null?"addProperty":"editProperty" %>"/>
<input type="hidden" name="index" value="<%=request.getParameter("index")%>"/>

<label><%=i18n.say( "Column" )%></label>
<input type="text" name="columnName" value="<%=columnName%>"/>
<label><%=i18n.say( "Java_Property" )%></label>
<select name="property">
<%for( int i=0; i<mcpats.length; i++ ) {
	String[] mcpat=mcpats[i];
	boolean selected=mcpat[0].equals( prop );
%>
	<option value="<%=mcpat[0]%> &lt;<%=mcpat[1] %>&gt;" <%=selected?"selected":"" %>><%=mcpat[0]%> &lt;<%=mcpat[1] %>&gt;</option>					
<% } %>
</select>
<br/>
<label><%=i18n.say( "Read_Only_Label" )%></label>
<input type="radio" name="readOnly" value="false" <%=readOnly?"":"checked" %>/>False
<input type="radio" name="readOnly" value="true" <%=readOnly?"checked":"" %>/>True  
