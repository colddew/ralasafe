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
<%@page import="org.ralasafe.db.sql.xml.Column"%>
<%@page import="org.ralasafe.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String id=request.getParameter( "id" );

Column[] columns=query.getQueryTypeSequence().getOrderBy().getColumn();
Map aliasNameMap=new HashMap();
Table[] tables=query.getQueryTypeSequence().getFrom().getTable();
for( int i=0; i<tables.length; i++ ) {
	aliasNameMap.put( tables[i].getAlias(), tables[i].getName() );
}
%> 



<table class="ralaTable">
<tr>
	<th><%=i18n.say( "Table" )%>[<%=i18n.say( "alias" )%>]</th>
	<th><%=i18n.say( "Column" )%></th>
	<th><%=i18n.say( "Order" )%></th>
	<th><%=i18n.say( "Action" )%> 
	<a href="javascript:addOrderColumn()" title="Add"><img src="../images/add.gif"/></a></th>
</tr>
<% for( int i=0; i<columns.length; i++ ) {
	Column column=columns[i];
	String alias=column.getTableAlias();
	String tableName=(String)aliasNameMap.get( alias );
	String name=column.getName();
	String displayName=tableName+"["+alias+"]";
	String order=column.getOrder().toString();
%>
<tr>
	<td><%=displayName%></td>
	<td><%=name%></td>
	<td><%=order %></td>
	<td><a href="javascript:moveOrderColumn('top', '<%=alias %>','<%=name %>');" title="Top"><img src="../images/top.gif"/></a>
		<a href="javascript:moveOrderColumn('up', '<%=alias %>','<%=name %>');" title="Up"><img src="../images/up.gif"/></a>
		<a href="javascript:moveOrderColumn('down', '<%=alias %>','<%=name %>');" title="Down"><img src="../images/down.gif"/></a>
		<a href="javascript:moveOrderColumn('bottom', '<%=alias %>','<%=name %>');" title="Bottom"><img src="../images/bottom.gif"/></a>
		<a href="javascript:editOrderColumn(<%=i %>, '<%=alias %>','<%=name %>');" title="Edit"><img src="../images/edit.gif"/></a>
		<a href="javascript:deleteOrderColumn('<%=alias %>','<%=name %>');" title="Delete"><img src="../images/delete.gif"/></a>
		</td>
</tr>
<%	
}
%>
<tr>
</tr>
</table>



<script type="text/javascript">
jQuery( document ).ready( function() {
	
} );


</script>