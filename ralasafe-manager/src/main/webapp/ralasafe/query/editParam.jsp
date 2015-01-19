<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryRawHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.Parameter"%>        
<%@page import="org.ralasafe.db.sql.xml.ContextValue"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryRawHandler handler=(QueryRawHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
Parameter param=(Parameter) request.getAttribute( "param" );
String id=request.getParameter( "id" );

String key="";
String type="";
if( param!=null ) {
	String[] s=handler.format(param);
	key=s[1];
	
	if( param.getChoiceValue() instanceof ContextValue ) {
		type="contextValue";
	}
}
%>


<input type="hidden" name="oper" value="<%=param==null?"addParam":"editParam" %>"/>
<input type="hidden" name="index" value="<%=request.getParameter("index")%>"/>

<label><%=i18n.say( "Parameter_type" )%></label><br/>
<input type="radio" name="type" value="userValue" checked/>User value
<input type="radio" name="type" value="contextValue" <%=type.equals("contextValue")?"checked":"" %>/>Context value
<br/>
<label><%=i18n.say( "Key" )%></label><input type="text" name="key" value="<%=key%>"/><br/>
