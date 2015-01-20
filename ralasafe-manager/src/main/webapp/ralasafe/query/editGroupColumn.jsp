<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryDesignHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.Table"%>
<%@page import="org.ralasafe.db.TableView"%>
<%@page import="org.ralasafe.db.ColumnView"%>
<%@page import="org.ralasafe.db.sql.xml.Column"%>
<%@page import="org.ralasafe.util.StringUtil"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
String id=request.getParameter( "id" );
String index=request.getParameter( "index" );

Table[] tables=handler.getQuery().getQueryTypeSequence().getFrom().getTable();
Column column=(Column) request.getAttribute( "column" );
String tableAlias=null;
String columnName=null;
if( column!=null ) {
	tableAlias=column.getTableAlias();
	columnName=column.getName();
}
%> 
<input type="hidden" name="index" value="<%=index %>"/> 
<input type="hidden" name="tableAlias" value="ddd"/>
<input type="hidden" name="columnName" value=""/>
<input type="hidden" name="oper" value="<%=column==null?"addGroupColumn":"editGroupColumn" %>"/>
<label><%=i18n.say( "Column" )%></label>
<select name="aliasColumn" id="aliasColumn">
<% for( int i=0; i<tables.length; i++ ) {
	Table table=tables[i];
	String alias=table.getAlias();
	TableView tableView=handler.getTableView( alias );
	Collection columnViews=tableView.getColumnViews();
	
	for( Iterator iter=columnViews.iterator(); iter.hasNext(); ) {
		ColumnView columnView=(ColumnView) iter.next();
		String value=tableView.getName()+"["+alias+"]."+columnView.getName();
		boolean selected=alias.equals( tableAlias ) && columnView.getName().equals( columnName );
	%>
	<option value="<%=value %>" <%=selected?"selected":"" %>><%=value %></option>	
<% 	}
}
%>
</select>