<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.ralasafe.servlet.DecisionEntitlementHandler"%>
<%@page import="org.ralasafe.privilege.Privilege"%>
<%@page import="org.ralasafe.entitle.DecisionEntitlement"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafeTree.css" />

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/ztree/jquery.ztree-2.6.min.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
DecisionEntitlementHandler handler=(DecisionEntitlementHandler) request.getAttribute( "handler" );
String id=request.getParameter("id");
String oper=request.getParameter( "oper" );

Privilege pvlg=handler.getPrivilege();
Collection entitlements=handler.getDecisionEntitlements();
String[] testContextFields=handler.getTestContextFields();
String[] testBusinessDataFields=handler.getTestBusinessDataFields();

String edit=i18n.say( "Continue_edit" );
if( "loadFresh".equals( oper ) ) {
	edit=i18n.say( "Edit" );
}

String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String[] mainMenuTexts=new String[] {
		pvlg.getType()==0?i18n.say( "Privilege" ):i18n.say( "Non_role_privilege" )
};
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		"./decisionEntitlement.rls?id="+id
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		edit
};
request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>

<title><%=i18n.say( "Test_Decision_Privilege" )%> -- <%=pvlg.getName() %></title>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<div class="smallContainer">
	<table class="ralaTable" id="entitleTable">
		<tr>
			<th><%=i18n.say( "Effect" )%></th>
			<th><%=i18n.say( "User_category" )%></th>
			<th><%=i18n.say( "Business_data" )%></th>
			<th><%=i18n.say( "Deny_reason" )%></th>
			<th><%=i18n.say( "Status" )%></th>
		</tr>
		<%
		int i=-1;
		for( Iterator iter=entitlements.iterator(); iter.hasNext(); ) {
			DecisionEntitlement entitle=(DecisionEntitlement) iter.next();
			i++;
			%>
		<tr>
			<td><%=i18n.say(entitle.getEffect()) %></td>
			<td><%=entitle.getUserCategory().getName() %></td>
			<td><%=entitle.getBusinessData().getName() %></td>
			<td><%=entitle.getDenyReason() %></td>
			<td>&nbsp;</td>
		</tr>			
		<% } %>
	</table>
	<p>&nbsp;</p>
	
	<form id="testForm" method="post" action="?oper=runTest&id=<%=id%>">
		<input type="hidden" name="first" value="0" />
	
	<jsp:include page="../common/inputTestUser.jsp"></jsp:include>
	
	
	<%if( testContextFields.length>0 ) { 
			request.setAttribute( "testContextFields", testContextFields );
	%>
	<jsp:include page="../common/inputTestContext.jsp"></jsp:include>
	<% } %>
	
	<%if( testBusinessDataFields.length>0 ) { 
		request.setAttribute("testBusinessDataFields", testBusinessDataFields );
		request.setAttribute("testBusinessDataFieldTypes", handler.getTestBusinessDataFieldTypes() );
	%>
	<div id="bdDiv">
	<jsp:include page="../common/inputTestBusinessData.jsp"></jsp:include>
	</div>
	<% } %>
	
	<p>
	<input type="submit" value="<%=i18n.say( "Run" )%>"/>
	</p>
	</form>
	
	<div id="result">
	</div>
</div>



<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
jQuery( document ).ready( function() {
	var options = { 
		beforeSubmit:  resetStatus,
		success:       showResponse 
	}; 
	jQuery('#testForm').submit(function() { 
        jQuery(this).ajaxSubmit(options); 
 		return false; 
    }); 	
});

function resetStatus() {
	// reset status
	jQuery('#entitleTable tr').each(function() {
	    jQuery( this ).find("td:last").html( "&nbsp;" );    
	});
}

function showResponse(responseText, statusText, xhr, $form)  {	
	jQuery( "#result" ).html( responseText );
} 

function loadJavabeanProperty() {
	var bdClass=jQuery( "#testForm :input[name=bdClass]" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setBusinessDataClass',
	    type: 'POST',
	    data: {"id":"<%=id%>","businessDataClass":bdClass},
	    error: function(){
	        alert('Error business data class');
	    },
	    success: function(resp){
	    	jQuery( "#bdDiv" ).html( resp );
	    }
	});
}

//------- save
function savePolicy() {
	jQuery.ajax({
	    url: '?oper=save',
	    type: 'POST',
	    data: {"id":"<%=id%>"},
	    error: function(){
	        alert('Error save policy');
	    },
	    success: function(xml){
	    	alert( "<%=i18n.say( "saved" )%>" );
	    }
	});
}

function setDataPicker( name ) {
	var bdClass=jQuery( "#testForm :input[name="+name+"]" ).datepicker({ dateFormat: 'yy-mm-dd' });
}
</script>	

</body>
</html>