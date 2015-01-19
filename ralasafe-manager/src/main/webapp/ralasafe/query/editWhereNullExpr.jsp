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
<%@page import="org.ralasafe.db.sql.xml.ExpressionGroupTypeItem"%>
<%@page import="org.ralasafe.db.sql.xml.BinaryExpression"%>
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
String nodeId=request.getParameter( "nodeId" );
String pId=request.getParameter( "pId" );
String exprType=request.getParameter( "exprType" );

ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) request.getAttribute( "item" );

Column column=null;
String nullType="";
if( item!=null ) {
	if( item.getIsNotNullExpression()!=null ) {
		column=item.getIsNotNullExpression().getColumn();
		nullType="NOT NULL";
	} else if( item.getIsNullExpression()!=null ) {
		column=item.getIsNullExpression().getColumn();
		nullType="NULL";
	}
}
%> 

<% if( nodeId!=null ) { %>
<input type="hidden" name="nodeId" value="<%=nodeId %>" />
<input type="hidden" name="oper" value="editWhereExpr" />
<% } %>
<% if( pId!=null ) { %>
<input type="hidden" name="pId" value="<%=pId %>" />
<input type="hidden" name="oper" value="addWhereExpr" />
<% } %>
<input type="hidden" name="exprType" value="null" />

<div id="whereExprDiv_1_column">
<label><%=i18n.say( "Column" )%></label> 
	<select name="column">
		<%
		Table[] tables=handler.getQuery().getQueryTypeSequence().getFrom().getTable();
		for( int i=0; i<tables.length; i++ ) {
			Table table=tables[i];
			String tableName=table.getName();
			String tableAlias=table.getAlias();
			Collection columns=handler.getTableView( tableAlias ).getColumnViews();
			
			for( Iterator iter=columns.iterator(); iter.hasNext(); ) {
				ColumnView columnView=(ColumnView) iter.next();
				String value=tableName+"["+tableAlias+"]."+columnView.getName();
				
				boolean selected=(column!=null
						&&tableAlias.equals( column.getTableAlias() )
						&&columnView.getName().equals( column.getName() ) );
		%>
		<option value="<%=value %>" <%=selected?"selected":"" %>><%=value %></option>
		<%		
			}
		}
		%>
	</select> 
	
</div>


<div>
<label><%=i18n.say( "Operator" )%></label>
<select name="operator">
	<option value="NULL" <%=nullType.equals("NULL")?"selected":"" %>>NULL</option>
	<option value="NOT NULL" <%=nullType.equals("NOT NULL")?"selected":"" %>>NOT NULL</option>
</select>
</div>


<script language="javascript">

</script>