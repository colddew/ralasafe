<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryRawHandler"%>
<%@page import="org.ralasafe.db.sql.xml.Column"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.RawSQL"%>        


<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryRawHandler handler=(QueryRawHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String id=request.getParameter( "id" );
RawSQL rawSql=query.getRawSQL();
String mappingClass="";
if( rawSql.getSelect().getMappingClass()!=null ) {
	mappingClass=rawSql.getSelect().getMappingClass();
}

Column[] columns=rawSql.getSelect().getColumn();
%>
<label><%=i18n.say( "Mapping_class" )%></label> 
<input type="text" id="mappingClass" value="<%=mappingClass%>" size="40" />
<input type="button" value="OK" onclick="javascript:setMappingClass()"/>

<table class="ralaTable">
	<tr>
		<th><%=i18n.say( "Column" )%></th>
		<th><%=i18n.say( "Java_Property" )%></th>
		<th><%=i18n.say( "Read_Only_Label" )%></th>
		<th><%=i18n.say( "Action" )%>
		<a href="javascript:addProperty()" title="Add"><img src="../images/add.gif"/></a>
		</th>
	</tr>
<% if( columns!=null ) {
	for( int i=0; i<columns.length; i++ ) {
		Column column=columns[i];
%>
	<tr>
		<td><%=column.getName()%></td>
		<td><%=column.getProperty() %> &lt;<%=column.getJavaType()%>&gt;</td>
		<td><%=column.getReadOnly() %></td>
		<td>
			<a href="javascript:moveProperty('top', <%=i%>);" title="Top"><img src="../images/top.gif"/></a>
			<a href="javascript:moveProperty('up', <%=i%>);" title="Up"><img src="../images/up.gif"/></a>
			<a href="javascript:moveProperty('down', <%=i%>);" title="Down"><img src="../images/down.gif"/></a>
			<a href="javascript:moveProperty('bottom', <%=i%>);" title="Bottom"><img src="../images/bottom.gif"/></a>
			<a href="javascript:editProperty(<%=i %>, <%=i%>);" title="Edit"><img src="../images/edit.gif"/></a>
			<a href="javascript:deleteProperty(<%=i%>);" title="Delete"><img src="../images/delete.gif"/></a>
		</td>
	</tr>
<%		
	}
}	
%>
</table>

<script type="text/javascript">
jQuery( document ).ready( function() {
	jQuery( "#mappingClass" ).keydown(function(event){  
		if(event.keyCode==13){
			setMappingClass(); 
		}  
	});
} );
</script>