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
<%@page import="java.util.List"%>        
<%@page import="org.ralasafe.entitle.ScriptTestResult"%>
<%@page import="org.ralasafe.entitle.QueryEntitlementTestResult"%>
<%@page import="org.ralasafe.servlet.QueryEntitlementHandler"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
int first=Integer.parseInt( request.getParameter("first") );
QueryEntitlementTestResult testResult=(QueryEntitlementTestResult) request.getAttribute( "testResult" );
QueryEntitlementHandler handler=(QueryEntitlementHandler) request.getAttribute( "handler" );

%>    

<% if( testResult.isFailed() ) { %>
<font color="red">
<%=i18n.say( "Test_failed" )%><br/>
<pre><%=testResult.getErrorMessage() %></pre>
</font>
<% } else { 
	String[] fields=(String[]) testResult.getQueryTestResult().getProperties();
	int totalCount=testResult.getQueryTestResult().getTotalRecords();
	String[][] testData=testResult.getQueryTestResult().getData();
%>
<font color="green"><b><%=i18n.say( "Matched_query" )%>: </b>   <%=testResult.getMatchedQuery().getName() %></font>

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

<% } %>

<script type="text/javascript">
<% if( !testResult.isFailed() ) { 
	// set yes column
	int matchedQueryId=testResult.getMatchedQuery().getId();
	
	//handler.getQueryEntitlements();
	List ucResults=testResult.getUserCategoryTestResults();
	for( int i=0,size=ucResults.size(); i<size; i++ ) {
		ScriptTestResult ucResult=(ScriptTestResult) ucResults.get( i );
		
		if( ucResult.isValid() ) {%>
jQuery('#entitleTable tr:eq(<%=i+1%>) td:last').html('<img src="../images/yes.gif"/>');			
<%		} else { %>
jQuery('#entitleTable tr:eq(<%=i+1%>) td:last').html('<img src="../images/fail.gif"/>');			
<%		}
	}
}%>
</script>