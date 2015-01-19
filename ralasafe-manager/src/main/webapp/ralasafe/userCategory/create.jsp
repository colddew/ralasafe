<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
String parentId=request.getParameter("parentId");
%>    

<form id="createForm" action="?oper=add" method="post">
	<input type="hidden" name="parentId" value="<%=parentId%>"/>
	<label><%=i18n.say( "Group" ) %></label>
	<input name="isLeaf" type="radio" value="false"/> Yes 	
	<input name="isLeaf" type="radio" value="true" checked="checked"/>No <br/>
	
	<label><%=i18n.say( "Name" ) %></label>
	<input name="name" type="text" size="50"/><br/>
	<label><%=i18n.say( "Description" ) %></label>
	<input name="description" type="text" size="50"/><br/>
	<input type="submit" value="OK" />
</form>
<script language="javascript">
var createOptions = { 
    beforeSubmit:  beforeCreate, 
    success:       afterCreate,
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