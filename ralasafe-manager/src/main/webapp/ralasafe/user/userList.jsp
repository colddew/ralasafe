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
<%@page import="org.ralasafe.user.User"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>
<title><%=i18n.say( "User_list" ) %></title>
<body>

<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<%
Collection coll=(Collection) request.getAttribute("users");
int totalNumber =((Integer)request.getAttribute( "totalNumber" )).intValue();
int first=((Integer)request.getAttribute( "first" ) ).intValue();
int size=((Integer)request.getAttribute( "size" ) ).intValue();
String searchName=(String)request.getAttribute( "name" );
String[] showUserFields=(String[])request.getAttribute( "showUserFields" );
String[] showUserFieldDisplayNames=(String[])request.getAttribute( "showUserFieldDisplayNames" );

if( searchName==null ) {
	searchName="";
}
%>

<div class="smallContainer">

<%@ include file="../common/searchForm.jsp"%>

<label><%=i18n.say( "User_list" ) %></label>
<table class="ralaTable">
	<tr>
		<% for( int i=0; i<showUserFieldDisplayNames.length; i++ ) { %>
		<th><%=showUserFieldDisplayNames[i] %></th>
		<% } %>
		<th><%=i18n.say( "Action" ) %></th>
	</tr>
<%
for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
	User user=(User) iter.next();
%>
	<tr>
		<% for( int i=0; i<showUserFields.length; i++ ) { %>
		<td><%=user.get(showUserFields[i]) %></td>
		<% } %>
		<td>
		<a href="userRole.rls?userId=<%=user.getId() %>" title="<%=i18n.say( "Set_roles" ) %>"><img src="../images/role.gif" /></a>
		</td>
	</tr>
<% } %>
</table>

<form id="navForm">	
<table class="ralaTable">
	<%@ include file="../common/nav.jsp"%>
</table>
</form>

</div>


<jsp:include page="../footer.jsp"></jsp:include>

<script type="text/javascript">
jQuery(document).ready(function(){	
	var navOptions = { 
		beforeSubmit:  calFirst
	}; 
	
	jQuery('#navForm').ajaxForm(navOptions);
});

function calFirst(formData, jqForm, options) {
	var totalNumber=<%=totalNumber%>;
	var size=<%=size%>;
	
	var page2=jQuery( "#navForm :input[name=page]" ).val();
	var page=page2-1;
	if( page*size>=0 && page*size<totalNumber ) {
		window.location="?name=<%=searchName%>&first="+(page*size);
		return true;
	} else {
		return false;
	}
}

function gotoRecord(first) {
	window.location="?name=<%=searchName%>&first="+first;
}
</script>	

</body>
</html>