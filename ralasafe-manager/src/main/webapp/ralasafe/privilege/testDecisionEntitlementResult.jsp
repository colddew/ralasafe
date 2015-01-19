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
<%@page import="org.ralasafe.entitle.DecisionEntitlementTestResult"%>
<%@page import="org.ralasafe.servlet.DecisionEntitlementHandler"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
DecisionEntitlementTestResult testResult=(DecisionEntitlementTestResult) request.getAttribute( "testResult" );
DecisionEntitlementHandler handler=(DecisionEntitlementHandler) request.getAttribute( "handler" );
%>    

<% if( testResult.isFailed() ) { %>
<font color="red">
<%=i18n.say( "Test_failed" )%><br/>
<pre><%=testResult.getErrorMessage() %></pre>
</font>
<% } else { 
	boolean permit=testResult.getDecision().isPermit();
	testResult.getDecision().getDenyReason();
	
	if( permit ) {
%>
<font color="green"><b><%=i18n.say( "Decision" )%>: <%=i18n.say( "Permit" )%> </b></font>
<% 	} else { %>
<font color="red"><b><%=i18n.say( "Decision" )%>: <%=i18n.say( "Deny" )%> </b>	
<pre><%=testResult.getDecision().getDenyReason() %></pre></font>
	<% } 
} %>

<script type="text/javascript">
<% if( !testResult.isFailed() ) { 
	// set column status
	List ucResults=testResult.getUserCategoryTestResults();
	List bdResults=testResult.getBusinessDataTestResults();
	for( int i=0,size=ucResults.size(); i<size; i++ ) {
		ScriptTestResult ucResult=(ScriptTestResult) ucResults.get( i );
		ScriptTestResult bdResult=(ScriptTestResult) bdResults.get( i );
		if( ucResult.isValid()&&bdResult.isValid() ) {%>
jQuery('#entitleTable tr:eq(<%=i+1%>) td:last').html('<img src="../images/yes.gif"/>');			
<%		} else { %>
jQuery('#entitleTable tr:eq(<%=i+1%>) td:last').html('<img src="../images/fail.gif"/>');			
<%		}
	}
}%>
</script>