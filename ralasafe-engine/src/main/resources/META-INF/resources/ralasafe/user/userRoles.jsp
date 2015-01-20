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
<%@page import="java.util.Set"%>
<%@page import="org.ralasafe.privilege.Role"%>
<%@page import="org.ralasafe.user.User"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<title><%=i18n.say( "User_role_mapping" ) %></title>
<body>

<%
Collection roles=(Collection) request.getAttribute("roles");
Set assignedRoleIds=(Set) request.getAttribute("assignedRoleIds");
int totalNumber =roles.size();
String searchName=(String)request.getAttribute( "name" );
User user=(User)request.getAttribute( "user" );

if( searchName==null ) {
	searchName="";
}

String[] mainMenuHrefs=new String[]{
		"/ralasafe/user/userMng.rls"		
};
String[] mainMenuTexts=new String[] {
		i18n.say( "User" ),
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
%>

<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<div class="smallContainer">

<%@ include file="../common/searchForm.jsp"%>

<form id="userRoleForm" method="post">
<input type="hidden" name="userId" value="<%=user.getId() %>" />
<label><%=i18n.say( "User" ) %></label>&nbsp;&nbsp;&nbsp;---&nbsp;&nbsp;&nbsp;<%=user.get("name") %>
<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Check" ) %></th>
		<th><%=i18n.say( "Name" ) %></th>
		<th><%=i18n.say( "Description" ) %></th>
	</tr>
<%
for( Iterator iter=roles.iterator(); iter.hasNext(); ) {
	Role role=(Role) iter.next();
	boolean checked=assignedRoleIds.contains( new Integer(role.getId() ) );
%>
	<tr>
		<td><input type="checkbox" name="roleId" value="<%=role.getId() %>" <%=checked?"checked='checked'":"" %>/></td>
		<td><%=role.getName() %></td>
		<td><%=role.getDescription()==null?"":role.getDescription() %></td>
	</tr>
<% } %>
</table>

<input type="submit" value="OK"/>
</form>

</div>

<jsp:include page="../footer.jsp"></jsp:include>

<script type="text/javascript">
jQuery(document).ready(function(){	
	var userRoleOptions = { 
		success:       afterAssignUserRole 
	}; 
	jQuery('#userRoleForm').submit(function() { 
	     jQuery(this).ajaxSubmit(userRoleOptions); 
	 	 return false; 
	});
});

function afterAssignUserRole() {
	alert( "<%=i18n.say( "saved" ) %>" );
}
</script>	

</body>
</html>