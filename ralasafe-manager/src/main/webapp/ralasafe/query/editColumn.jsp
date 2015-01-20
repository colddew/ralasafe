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
String id=request.getParameter( "id" );

Column column=(Column) request.getAttribute( "column" );
String[][] mcpats=(String[][]) request.getAttribute( "mappingClassPropertyAndTypes" );
%> 
<input type="hidden" name="tableAlias" value="<%=column.getTableAlias() %>"/>
<input type="hidden" name="columnName" value="<%=column.getName() %>"/>    
		<label><%=i18n.say( "Column" )%></label>
		<%=column.getName()%> &lt;<%=column.getSqlType() %>&gt;
		<label><%=i18n.say( "Function" )%></label>
		<input type="text" name="function" value="<%=column.getFunction()==null?"":column.getFunction() %>"/>
		<label><%=i18n.say( "Java_Property" )%></label>
		<select name="property">
			<%
			for( int i=0; i<mcpats.length; i++ ) {
				String[] mcpat=mcpats[i];
				boolean selected=mcpat[0].equals( column.getProperty() );
			%>
				<option value="<%=mcpat[0]%> &lt;<%=mcpat[1] %>&gt;" <%=selected?"selected":"" %>><%=mcpat[0]%> &lt;<%=mcpat[1] %>&gt;</option>					
			<%
			}
			%>
		</select>
		<label><%=i18n.say( "Read_Only_Label" )%></label>
		<%boolean readOnly=column.getReadOnly(); %>
		<input type="radio" name="readOnly" value="true" <%=readOnly?"checked":"" %>>True
		<input type="radio" name="readOnly" value="false" <%=readOnly?"":"checked" %>>False
		