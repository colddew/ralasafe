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
<%@page import="org.ralasafe.privilege.Privilege"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
Privilege pvlg=(Privilege)request.getAttribute( "privilege" );

String pvlgUrl="nonRolePrivilegeMng.rls";
String pvlgText=i18n.say( "Non_role_privilege" );
if( pvlg.getType()==Privilege.BUSINESS_PRIVILEGE ) {
	pvlgUrl="privilegeMng.rls";
	pvlgText=i18n.say( "Privilege" );
}

String[] mainMenuHrefs=new String[]{
		pvlgUrl
};
String[] mainMenuTexts=new String[] {
		pvlgText
};
request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
%>

<title><%=i18n.say( "Choose_Privilege_Type" )%></title>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<%
String id=request.getParameter("id");
%>

<div class="smallContainer">
	<form id="typeForm">
		<label><%=i18n.say( "Choose_Privilege_Type" )%></label>
		<input type="radio" name="type" value="query"/><%=i18n.say( "Query_Privilege" )%>
		<input type="radio" name="type" value="decision"/><%=i18n.say( "Decision_Privilege" )%>
		<input type="submit" value="OK"/>
	</form>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
jQuery(document).ready(function(){	
	jQuery('#typeForm').submit(function() { 
		var type=jQuery( "#typeForm :input[name=type][checked]" ).val();
		
		if( "query"==type ) {
			window.location="./queryEntitlement.rls?id=<%=id%>";
		} else {
			window.location="./decisionEntitlement.rls?id=<%=id%>";
		}
		
		return false;
	}); 
});

</script>	

</body>
</html>