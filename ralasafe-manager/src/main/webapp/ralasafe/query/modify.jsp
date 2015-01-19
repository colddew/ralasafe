<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.entitle.Query"%>
<%
Query node=(Query) request.getAttribute( "node" );
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>    


<form id="modifyForm" action="?oper=update" method="post">
	<input type="hidden" name="id" value="n<%=node.getId()%>"/>
	<input type="hidden" name="parentId" value="n<%=node.getPid()%>"/>
	<input type="hidden" name="isLeaf" value="<%=node.getIsLeaf()%>"/>
	<label><%=i18n.say( "Name" )%></label>
	<input name="name" type="text" value="<%=node.getName() %>" size="50"/><br/>
	<label><%=i18n.say( "Description" )%></label>
	<input name="description" type="text" value="<%=node.getDescription() %>" size="50"/><br/>
	
	<input type="submit" value="OK" />
</form>
<script language="javascript">
var modifyOptions = { 
    beforeSubmit:  beforeModify, 
    success:       afterModify,
    clearForm:     true
}; 

//validate
jQuery('#modifyForm').validate({
	rules: {
		name: {
			required: true,
			remote: {
				url: "?oper=isNameValid&id=n<%=node.getId()%>",
	        	type: "GET",
	        	dataType: "json",
	        	data: {
	        		name: function() {
	           			return jQuery( "#modifyForm :input[name=name]" ).val();
	        		}
	        	}
			}
		}
	}
});

// bind form using 'ajaxForm' 
jQuery('#modifyForm').ajaxForm(modifyOptions); 
</script>