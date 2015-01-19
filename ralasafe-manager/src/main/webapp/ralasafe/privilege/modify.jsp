<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.privilege.Privilege"%>
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
Privilege node=(Privilege) request.getAttribute( "node" );
%> 
<form id="modifyForm" action="?oper=update" method="post">
	<input type="hidden" name="id" value="n<%=node.getId()%>"/>
	<input type="hidden" name="parentId" value="n<%=node.getPid()%>"/>
	<input type="hidden" name="isLeaf" value="<%=node.getIsLeaf()%>"/>
	
	<label><%=i18n.say( "Name" ) %></label>
	<input name="name" type="text" size="50" value="<%=node.getName()%>"><br/>
	<label><%=i18n.say( "Description" ) %></label>
	<input name="description" type="text" size="50" value="<%=node.getDescription()%>"/><br/>
	<label><%=i18n.say( "Display_in_menu" ) %></label>
	<input name="display" type="radio" value="true" <%=node.getDisplay()?"checked":"" %>/> Yes
	<input name="display" type="radio" value="false" <%=node.getDisplay()?"":"checked" %>/> No<br/>
	<%if( node.getIsLeaf() ) { %>
	<label><%=i18n.say( "Constant_name" ) %></label>
	<input name="constantName" type="text" size="50" value="<%=node.getConstantName()%>"/><br/>
	<label><%=i18n.say( "Target" ) %></label>
	<input name="target" type="text" size="50" value="<%=node.getTarget() %>"/><br/>
	<label><%=i18n.say( "URL" ) %></label>
	<input name="url" type="text" size="50" value="<%=node.getUrl() %>"/><br/>
	<% } %>
	<input type="submit" value="OK" />
</form>
<script language="javascript">
var modifyOptions = { 
    beforeSubmit:  beforeModify, 
    success:       afterModify,
    clearForm:     true
}; 

// validate
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
		<%if( node.getIsLeaf() ) {%>
		,constantName: {
			required: true
		}
		<%}%>
	}
});

// bind form using 'ajaxForm' 
jQuery('#modifyForm').ajaxForm(modifyOptions); 
</script>