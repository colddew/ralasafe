<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.util.Util"%>
<%@page import="java.util.Collection"%>    
<%@page import="org.ralasafe.entitle.QueryResult"%>
<%@page import="org.ralasafe.servlet.QueryTestHandler"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
int first=Integer.parseInt( request.getParameter("first") );
QueryResult testResult=(QueryResult) request.getAttribute( "testResult" );
String[] fields=(String[]) testResult.getFields().toArray( new String[0] );
Collection data=testResult.getData();
int totalCount=testResult.getTotalCount();

String mappingClass="";
QueryTestHandler handler=(QueryTestHandler) request.getAttribute( "handler" );
if( handler.getQuery().getIsRawSQL() ) {
	mappingClass=handler.getQuery().getRawSQL().getSelect().getMappingClass();
} else {
	mappingClass=handler.getQuery().getQueryTypeSequence().getSelect().getMappingClass();
}

String[][] testData=Util.formatJavabeans( data, mappingClass, fields );
%>    

<label><%=i18n.say( "Total_records" )%>: <%=totalCount %> </label>
<% if( first!=0 ) { %>
<a href="javascript:showResult('<%=(first-15)<0?0:(first-15) %>')"><%=i18n.say( "Previous" )%></a>
<% } else { %>
&nbsp;&nbsp;&nbsp;
<% } %>
<% if( first+15<totalCount ) { %>
<a href="javascript:showResult('<%=first+15%>')"><%=i18n.say( "Next" )%></a>
<% } %>
<table>
	<tr>
<% for( int i=0; i<fields.length; i++ ) { 
%>
		<th><%=fields[i] %></th>	
<%	
}
%>
	</tr>
	
<% for( int i=0; i<testData.length; i++ ) {
	String[] rowData=testData[i];
%>
	<tr>	
	<%	for( int j=0; j<rowData.length; j++ ) { %>
		<td><%=rowData[j] %></td>
	<%  } %>
	</tr>
<% } %>
</table>