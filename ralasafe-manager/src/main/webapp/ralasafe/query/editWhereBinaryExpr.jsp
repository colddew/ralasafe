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

String[] userFields=(String[]) request.getAttribute( "userFields" );
ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) request.getAttribute( "item" );
BinaryExpression expr=null;

Column column1=null;
String contextValue1="";
String simpleValue1="";
String userValue1="";
Column column2=null;
String contextValue2="";
String simpleValue2="";
String userValue2="";

if( item!=null ) {
	expr=item.getBinaryExpression();
}
if( expr!=null&&expr.getOperand1().getOperand().getColumn()!=null ) {
	column1=expr.getOperand1().getOperand().getColumn();
}
if( expr!=null&&expr.getOperand1().getOperand().getValue()!=null ) {
	if(	expr.getOperand1().getOperand().getValue().getContextValue()!=null ) {
	contextValue1=expr.getOperand1().getOperand().getValue().getContextValue().getKey();
	}

	if(	expr.getOperand1().getOperand().getValue().getSimpleValue()!=null ) {
		simpleValue1=expr.getOperand1().getOperand().getValue().getSimpleValue().getContent();
	}
	
	if(	expr.getOperand1().getOperand().getValue().getUserValue()!=null ) {
		userValue1=expr.getOperand1().getOperand().getValue().getUserValue().getKey();
	}
}

if( expr!=null&&expr.getOperand2().getOperand().getColumn()!=null ) {
	column2=expr.getOperand2().getOperand().getColumn();
}
if( expr!=null&&expr.getOperand2().getOperand().getValue()!=null ) {
	if(	expr.getOperand2().getOperand().getValue().getContextValue()!=null ) {
	contextValue2=expr.getOperand2().getOperand().getValue().getContextValue().getKey();
	}

	if(	expr.getOperand2().getOperand().getValue().getSimpleValue()!=null ) {
		simpleValue2=expr.getOperand2().getOperand().getValue().getSimpleValue().getContent();
	}
	
	if(	expr.getOperand2().getOperand().getValue().getUserValue()!=null ) {
		userValue2=expr.getOperand2().getOperand().getValue().getUserValue().getKey();
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
<input type="hidden" name="exprType" value="binary" />
<input type="hidden" name="operand1Type" value="column" />
<input type="hidden" name="operand2Type" value="column" />

<div id="whereExprDiv_1_column">
<label><%=i18n.say( "Column" )%></label>
	<select name="column1">
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
				
				boolean selected=(column1!=null
						&&tableAlias.equals( column1.getTableAlias() )
						&&columnView.getName().equals( column1.getName() ) );
		%>
		<option value="<%=value %>" <%=selected?"selected":"" %>><%=value %></option>
		<%		
			}
		}
		%>
	</select> 
	<br/><font size="-1">
	<a href="javascript:showOperand('1', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
	<a href="javascript:showOperand('1', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a><br/>
	<a href="javascript:showOperand('1', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
	</font>
</div>

<div id="whereExprDiv_1_contextValue">
<label><%=i18n.say( "Context_value" )%></label>
<input type="text" name="contextValue1" value="<%=contextValue1%>" title="value key in context" />
<br/><font size="-1">
<a href="javascript:showOperand('1', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
<a href="javascript:showOperand('1', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a><br/>
<a href="javascript:showOperand('1', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
</font>	
</div>

<div id="whereExprDiv_1_simpleValue">
<label><%=i18n.say( "Simple_value" )%></label>
<select name="simpleValueType1">
	<option value="string">String</option>
	<option value="integer">Integer</option>
	<option value="float">Float</option>
	<option value="boolean">Boolean</option>
	<option value="datetime">Datetime</option>
</select>
<input type="text" name="simpleValue1" value="<%=simpleValue1%>" />	
<br/><font size="-1">
<a href="javascript:showOperand('1', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
<a href="javascript:showOperand('1', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
<a href="javascript:showOperand('1', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a>
</font>
</div>

<div id="whereExprDiv_1_userValue">
<label><%=i18n.say( "User_value" )%></label>
<select name="userValue1">
<% for( int i=0; i<userFields.length; i++ ) { 
		String userField=userFields[i];
		boolean selected=userValue1!=null&&userValue1.equals( userField );
%>
	<option value="<%=userField %>" <%=selected?"selected":"" %>><%=userField %></option>
<% } %>
</select>
<br/><font size="-1">
<a href="javascript:showOperand('1', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
<a href="javascript:showOperand('1', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
<a href="javascript:showOperand('1', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
</font>
</div>

<div>
<label><%=i18n.say( "Operator" )%></label>
<select name="operator">
	<option value="=">=</option>
	<option value="!=">!=</option>
	<option value="&gt;">&gt;</option>
	<option value="&gt;=">&gt;=</option>
	<option value="&lt;">&lt;</option>
	<option value="&lt;=">&lt;=</option>
</select>
</div>

<div id="whereExprDiv_2_column">
<label><%=i18n.say( "Column" )%></label> 
	<select name="column2">
		<%
		for( int i=0; i<tables.length; i++ ) {
			Table table=tables[i];
			String tableName=table.getName();
			String tableAlias=table.getAlias();
			Collection columns=handler.getTableView( tableAlias ).getColumnViews();
			
			for( Iterator iter=columns.iterator(); iter.hasNext(); ) {
				ColumnView columnView=(ColumnView) iter.next();
				String value=tableName+"["+tableAlias+"]."+columnView.getName();
				
				boolean selected=(column2!=null
						&&tableAlias.equals( column2.getTableAlias() )
						&&columnView.getName().equals( column2.getName() ) );
		%>
		<option value="<%=value %>" <%=selected?"selected":"" %>><%=value %></option>
		<%		
			}
		}
		%>
	</select> 
	<br/><font size="-1">
	<a href="javascript:showOperand('2', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
	<a href="javascript:showOperand('2', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a><br/>
	<a href="javascript:showOperand('2', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
	</font>
</div>

<div id="whereExprDiv_2_contextValue">
<label><%=i18n.say( "Context_value" )%></label>
<input type="text" name="contextValue2" value="<%=contextValue2%>" title="value key in context" />	
<br/><font size="-1">
	<a href="javascript:showOperand('2', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
	<a href="javascript:showOperand('2', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a><br/>
	<a href="javascript:showOperand('2', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
</font>
</div>

<div id="whereExprDiv_2_simpleValue">
<label><%=i18n.say( "Simple_value" )%></label>
<select name="simpleValueType2">
	<option value="string">String</option>
	<option value="integer">Integer</option>
	<option value="float">Float</option>
	<option value="boolean">Boolean</option>
	<option value="datetime">Datetime</option>
</select>
<input type="text" name="simpleValue2" value="<%=simpleValue2%>" />	
<br/><font size="-1">
	<a href="javascript:showOperand('2', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
	<a href="javascript:showOperand('2', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
	<a href="javascript:showOperand('2', 'userValue')"><%=i18n.say( "Replace_with_user_value" )%></a>
</font>
</div>

<div id="whereExprDiv_2_userValue">
<label><%=i18n.say( "User_value" )%></label>
<select name="userValue2">
<% for( int i=0; i<userFields.length; i++ ) { 
		String userField=userFields[i];
		boolean selected=userValue2!=null&&userValue2.equals( userField );
%>
	<option value="<%=userField %>" <%=selected?"selected":"" %>><%=userField %></option>
<% } %>
</select>
<br/><font size="-1">
	<a href="javascript:showOperand('2', 'column')"><%=i18n.say( "Replace_with_column" )%></a><br/>
	<a href="javascript:showOperand('2', 'contextValue')"><%=i18n.say( "Replace_with_context_value" )%></a><br/>
	<a href="javascript:showOperand('2', 'simpleValue')"><%=i18n.say( "Replace_with_simple_value" )%></a>
</font>
</div>

<script language="javascript">
//jQuery( "#whereExprDiv_1_column" ).hide();
jQuery( "#whereExprDiv_1_contextValue" ).hide();
jQuery( "#whereExprDiv_1_userValue" ).hide();
jQuery( "#whereExprDiv_1_simpleValue" ).hide();
//jQuery( "#whereExprDiv_2_column" ).hide();
jQuery( "#whereExprDiv_2_contextValue" ).hide();
jQuery( "#whereExprDiv_2_userValue" ).hide();
jQuery( "#whereExprDiv_2_simpleValue" ).hide();

<% if( column1!= null ) { %>
showOperand( '1', 'column' );
<% } else if( !contextValue1.equals( "") ) { %>
showOperand( '1', 'contextValue' );
<% } else if( !simpleValue1.equals( "" ) ) { %>
showOperand( '1', 'simpleValue' );
<% } else if( !userValue1.equals( "" ) ) { %>
showOperand( '1', 'userValue' );
<% } %>

<%if( column2!=null ) { %>
showOperand( '2', 'column' );
<% } else if( !contextValue2.equals( "") ) { %>
showOperand( '2', 'contextValue' );
<% } else if( !simpleValue2.equals( "" ) ) { %>
showOperand( '2', 'simpleValue' );
<% } else if( !userValue2.equals( "" ) ) { %>
showOperand( '2', 'userValue' );
<% } %>

</script>