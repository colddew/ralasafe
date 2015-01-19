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
<%@page import="org.ralasafe.privilege.Role"%>

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
<script src="../js/jquery.validate.js" type="text/javascript"></script>
<script src="../js/validate/<%=i18n.getValidateMessageFile()%>" type="text/javascript"></script>

<title><%=i18n.say( "Roles" )%></title>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<%
Collection coll=(Collection) request.getAttribute("roles");
int totalNumber =((Integer)request.getAttribute( "totalNumber" )).intValue();
int first=((Integer)request.getAttribute( "first" ) ).intValue();
int size=((Integer)request.getAttribute( "size" ) ).intValue();
String searchName=(String)request.getAttribute( "name" );

if( searchName==null ) {
	searchName="";
}
%>

<div class="smallContainer">

<%@ include file="../common/searchForm.jsp"%>

<label><%=i18n.say( "Role_list" )%></label>
<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Name" )%></th>
		<th><%=i18n.say( "Description" )%></th>
		<th><%=i18n.say( "Action" )%>
		<a href="javascript:addRole()" title="Add"><img src="../images/add.gif"/></a></th>
	</tr>
<%
for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
	Role role=(Role) iter.next();
%>
	<tr>
		<td><%=role.getName() %></td>
		<td><%=role.getDescription()==null?"":role.getDescription() %></td>
		<td>
		<a href="javascript:editRole(<%=role.getId() %>);" title="<%=i18n.say( "Edit" )%>"><img src="../images/edit.gif"/></a>
		<a href="javascript:deleteRole(<%=role.getId() %>);" title="<%=i18n.say( "Delete" )%>"><img src="../images/delete.gif"/></a>
		<a href="rolePrivilege.rls?roleId=<%=role.getId() %>" title="<%=i18n.say( "Set_privileges" )%>"><img src="../images/privilege.png"/></a>
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

<div id="roleDialog" title="Add/Edit role">
	<form id="roleForm" method="post">
		
	</form>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
jQuery(document).ready(function(){	
	var navOptions = { 
		beforeSubmit:  calFirst
	}; 
	
	jQuery('#navForm').ajaxForm(navOptions); 

	jQuery( "#roleDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#roleForm').submit();
			}
		}
	});

	var roleOptions = { 
		 beforeSubmit:  checkRoleForm,
		 success:       afterUpdateRole 
	}; 
	    
	jQuery('#roleForm').submit(function() { 
	     jQuery(this).ajaxSubmit(roleOptions); 
	 	 return false; 
	}); 
});

function checkRoleForm() {
	return jQuery('#roleForm').valid();
}

function setValidate( roleId ) {
	var url="?oper=isNameValid";
	if( roleId!="__" ) {
		url=url+"&id="+roleId;
	}
	
	//validate
	jQuery('#roleForm').validate({
		rules: {
			name: {
				required: true,
				remote: {
					url: url,
		        	type: "GET",
		        	dataType: "json",
		        	data: {
		        		name: function() {
		           			return jQuery( "#roleForm :input[name=name]" ).val();
		        		}
		        	}
				}
			}
		}
	});
}

function afterUpdateRole() {
	window.location="roleMng.rls";
}

function calFirst(formData, jqForm, options) {
	var totalNumber=<%=totalNumber%>;
	var size=<%=size%>;
	
	var page2=jQuery( "#navForm :input[name=page]" ).val();
	var page=page2-1;
	if( page*size>=0 && page*size<totalNumber ) {
		window.location="roleMng?name=<%=searchName%>&first="+(page*size);
		return true;
	} else {
		return false;
	}
}

function gotoRecord(first) {
	window.location="roleMng?name=<%=searchName%>&first="+first;
}

function deleteRole(id) {
	jQuery.ajax({
	    url: '?oper=deleteRole',
	    type: 'POST',
	    async: false,
	    data: {"id":id},
	    error: function(){
	        alert('Error delete role');
	    },
	    success: function(xml){
	    	window.location="roleMng.rls?name=<%=searchName%>&first=<%=first%>";
	    }
	});	
}

function addRole() {
	jQuery( '#roleForm' ).load( "?oper=addRole", 
			function() {
		jQuery( "#roleDialog" ).dialog( "open" );
	});
}

function editRole(id) {
	jQuery( '#roleForm' ).load( "?oper=getRole&id="+id,
			function() {
		jQuery( "#roleDialog" ).dialog( "open" );
	});
}
</script>	

</body>
</html>