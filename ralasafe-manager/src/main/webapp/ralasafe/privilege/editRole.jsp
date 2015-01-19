<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@page import="org.ralasafe.privilege.Role"%>    
<%
Role role=(Role) request.getAttribute( "role" );
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>     

<input type="hidden" name="oper" value="<%=role==null?"addRole":"updateRole" %>"/>
<input type="hidden" name="id" value="<%=role==null?"":role.getId()+""%>" />

<label><%=i18n.say( "Name" )%></label>
<input type="text" name="name" value="<%=role==null?"":role.getName()%>" />
<label><%=i18n.say( "Description" )%></label>
<input type="text" name="description" value="<%=role==null?"":role.getDescription()%>" />

<script>
setValidate(<%=role==null?"'__'":role.getId()+""%>);
</script>