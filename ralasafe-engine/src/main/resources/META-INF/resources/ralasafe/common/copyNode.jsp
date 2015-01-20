<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.ralasafe.group.Node" %>
<%@page import="org.ralasafe.entitle.Query"%>
<%@page import="org.ralasafe.entitle.BusinessData"%>
<%@page import="org.ralasafe.entitle.UserCategory"%>
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
String id=request.getParameter("id");
Node node=(Node)request.getAttribute( "node" );

String name="";
String description="";
if( node instanceof Query ) {
	Query q=(Query) node;
	name=q.getName();
	description=q.getDescription();
} else if( node instanceof BusinessData ) {
	BusinessData bd=(BusinessData) node;
	name=bd.getName();
	description=bd.getDescription();
} else if( node instanceof UserCategory ) {
	UserCategory uc=(UserCategory) node;
	name=uc.getName();
	description=uc.getDescription();
}
%>    

<form id="createForm" action="?oper=copy" method="post">
	<input type="hidden" name="id" value="<%=id%>"/>
	<label><%=i18n.say( "Name" ) %></label>
	<input name="name" type="text" size="25" value="Copy of <%=name%>"/><br/>
	<label><%=i18n.say( "Description" ) %></label>
	<input name="description" type="text" size="25" value="Copy of <%=description%>"/><br/>
	<input type="submit" value="OK" />
</form>
<script language="javascript">
var createOptions = { 
    beforeSubmit:  beforeCreate, 
    success:       afterCopy,
    resetForm:     true
}; 

//validate
jQuery('#createForm').validate({
	rules: {
		name: {
			required: true,
			remote: {
				url: "?oper=isNameValid",
	        	type: "GET",
	        	dataType: "json",
	        	data: {
	        		name: function() {
	           			return jQuery( "#createForm :input[name=name]" ).val();
	        		}
	        	}
			}
		}
	}
});

// bind form using 'ajaxForm' 
jQuery('#createForm').ajaxForm(createOptions); 
</script>