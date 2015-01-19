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
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String dsName=query.getDs();
String id=request.getParameter( "id" );
String mappingClass=query.getQueryTypeSequence().getSelect().getMappingClass();

Table table=(Table)request.getAttribute( "table" );
String tableAlias=table.getAlias();
TableView tableView=handler.getTableView( tableAlias );
%> 
    
		<table class="ralaTable">
			<tr>
				<td colspan="8">Schema:<%=table.getSchema().equals("")?"[Default]":table.getSchema() %>, 
				
					<%=i18n.say( "Table" )%>:<%=table.getName()%>, 
					<%=i18n.say( "alias" )%>:<%=table.getAlias() %><br/>
					
					<a href="javascript:checkTableColumns('<%=tableAlias %>');" ><%=i18n.say( "Check_all" )%></a>
					<a href="javascript:unCheckTableColumns('<%=tableAlias %>');" ><%=i18n.say( "Uncheck_all" )%></a>
					<a href="javascript:deleteTable('<%=tableAlias %>');" ><%=i18n.say( "Delete" )%></a>
				</td>
			</tr>
			<tr>
				<th><%=i18n.say( "Column" )%></th>
				<th><%=i18n.say( "Function" )%></th>
				<th><%=i18n.say( "Java_Property" )%></th>
				<th><%=i18n.say( "Read_Only_Label" )%></th>
				<th><%=i18n.say( "Status" )%></th>	
				<th><%=i18n.say( "Action" )%></th>		
			</tr>
			
			<%
				if( tableView==null ) {
					// this table undefined in database
					List checkedColumns=handler.getCheckedColumns( tableAlias );
					int size=checkedColumns.size();
					%>
			<%
					for( int j=0; j<size; j++ ) {
						Column column=(Column) checkedColumns.get( j );
			%>
			<tr>
				<td>
				<%=column.getName() %>
				<span class="aidWord">&lt;<%=column.getSqlType() %>&gt;</span></td>
				<td><%=column.getFunction() %></td>
				<td><%=column.getProperty() %>
				<span class="aidWord">&lt;<%=column.getJavaType() %>&gt;</span></td>
				<td><%=column.getReadOnly() %></td>
				<td><b><i><font color="red">Table Undefined</font></i></b></td>
				<td><input type="checkbox" 
							id="<%=tableAlias %>__<%=column.getName() %>" 
							title="<%=i18n.say( "Unmap_column" )%>"
							onclick="javascript:checkTableColumn('<%=tableAlias %>','<%=column.getName() %>');"
							checked="checked" /><%=printEdit( mappingClass, tableAlias, column.getName(), i18n ) %></td>
			</tr>
			<%						
					}
				} else {
					// table defined, display all columns in this table
					for( Iterator iter=tableView.getColumnViews().iterator(); iter.hasNext(); ) {
						ColumnView columnView=(ColumnView) iter.next();
						// column maybe null
						Column column=handler.getColumn( tableAlias, columnView.getName() );
			%>
			<tr>				
				<td>
				<%=columnView.getName() %>
				<span class="aidWord">&lt;<%=columnView.getSqlType() %>&gt;</span></td>
				<td><%=printFunction(column) %></td>
				<td><%=column==null||StringUtil.isEmpty(column.getProperty())?"&nbsp;":column.getProperty()+"&nbsp;<span class=\"aidWord\">&lt;"+column.getJavaType()+"&gt;</span>" %>
				</td>
				<td><%=column==null?"&nbsp;":column.getReadOnly()+"" %></td>
				<td>&nbsp;</td>
				<td><input type="checkbox" 
							title="<%=i18n.say( "Unmap_column" )%>"
							id="<%=tableAlias %>__<%=columnView.getName() %>" 
							onclick="javascript:checkTableColumn('<%=tableAlias %>','<%=columnView.getName() %>');" 
							<%=column==null?"":"checked='checked'" %>/>
					<%=column==null?"&nbsp;":printEdit( mappingClass, tableAlias, columnView.getName(), i18n ) %></td>
			</tr>
			<%			
					}
					
					// display undefined columns in this table
					if( handler.getUndefinedColumns( tableAlias )!=null ) {
						for( Iterator iter=handler.getUndefinedColumns( tableAlias ).iterator(); iter.hasNext(); ) {
							Column column=(Column) iter.next();
			%>
			<tr>
				<td>
				<%=column.getName() %>
				<span class="aidWord">&lt;<%=column.getSqlType() %>&gt;</span></td>
				<td><%=printFunction(column) %></td>
				<td><%=column.getProperty() %>
				<span class="aidWord">&lt;<%=column.getJavaType() %>&gt;</span></td>
				<td><%=column.getReadOnly() %></td>
				<td><b><i><font color="red">Column Undefined</font></i></b></td>
				<td><input type="checkbox" 
							title="<%=i18n.say( "Unmap_column" )%>"
							id="<%=tableAlias %>__<%=column.getName() %>" 
							onclick="javascript:checkTableColumn('<%=tableAlias %>','<%=column.getName() %>');"
							checked="checked" />
					<%=printEdit( mappingClass, tableAlias, column.getName(), i18n ) %></td>
			</tr>
			<%						
						}
					}
				}
			%>
		</table>
<%!
String printFunction(Column col) {
	if( col==null || StringUtil.isEmpty( col.getFunction() ) ) {
		return "&nbsp;";
	} else {
		return col.getFunction();
	}
}

String printEdit( String mapping, String alias, String column, org.ralasafe.util.I18N i18n ) {
	if( StringUtil.isEmpty( mapping ) ) {
		return "&nbsp;";
	} else {
		return "<a title='"+i18n.say( "Edit_column_mapping" ) 
		+ "' href=\"javascript:editTableColumn( '"+alias+"', '"+column+"' )\"><img src='../images/edit.gif'/></a>";
	}
}
%>		