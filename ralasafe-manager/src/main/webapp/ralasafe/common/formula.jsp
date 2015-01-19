<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.ralasafe.db.sql.xml.DefineVariable" %>
<%@ page import="org.ralasafe.servlet.AbstractPolicyDesignHandler" %>
<%@ page import="org.ralasafe.db.sql.xml.Formula"%>
<%@ page import="org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType"%>
<%@ page import="org.ralasafe.db.sql.xml.types.FormulaTypeTypeType"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

DefineVariable var=(DefineVariable) request.getAttribute( "variable" );
String oper="addVariable";
String name="";
String index="";
String operName1="";
String operName2="";
String id=request.getParameter( "id" );
String operator="";
String returnType="";

if( var!=null ) {
	Formula value=var.getFormula();
	
	index=request.getParameter( "index" );
	oper="updateVariable";
	
	operName1=value.getVariable(0).getName();
	operName2=value.getVariable(1).getName();
	operator=value.getOperator().toString();
	returnType=value.getType().toString();
	
	name=var.getName();
}

AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
DefineVariable[] variables=handler.getVariables();
%>


	
<input type="hidden" name="oper" value="<%=oper%>"/>
	<input type="hidden" name="id" value="<%=id%>"/>
	<input type="hidden" name="type" value="formula"/>
	<input type="hidden" name="index" value="<%=index%>"/>
	<label><%=i18n.say( "Variable_name" )%></label>
	<input type="text" name="name" value="<%=name%>" class="required"/>
	<br/>
	<label><%=i18n.say( "Variable" )%> 1</label> 
	<select name="operName1">
	<% for( int i=0; i<variables.length; i++ ) { 
			String varName=variables[i].getName();
			if( varName.equals( name ) ) {
				continue;
			}
			boolean selected=varName.equals( operName1 );
	%>
		<option value="<%=varName%>" <%=selected?"selected":"" %>><%=varName %></option>
	<% } %>
	</select> 
	<br/>
	<label><%=i18n.say( "Operator" )%></label> 
	<select name="operator">
		<option value="<%=FormulaTypeOperatorType.VALUE_0.toString()%>"
			<%=(FormulaTypeOperatorType.VALUE_0.toString()).equals( operator )?"selected":""%>>
			<%=FormulaTypeOperatorType.VALUE_0.toString()%>
		</option>
		<option value="<%=FormulaTypeOperatorType.VALUE_1.toString()%>"
			<%=(FormulaTypeOperatorType.VALUE_1.toString()).equals( operator )?"selected":""%>>
			<%=FormulaTypeOperatorType.VALUE_1.toString()%>
		</option>
		<option value="<%=FormulaTypeOperatorType.VALUE_2.toString()%>"
			<%=(FormulaTypeOperatorType.VALUE_2.toString()).equals( operator )?"selected":""%>>
			<%=FormulaTypeOperatorType.VALUE_2.toString()%>
		</option>
		<option value="<%=FormulaTypeOperatorType.VALUE_3.toString()%>"
			<%=(FormulaTypeOperatorType.VALUE_3.toString()).equals( operator )?"selected":""%>>
			<%=FormulaTypeOperatorType.VALUE_3.toString()%>
		</option>
	</select>
	<br/>
	<label><%=i18n.say( "Variable" )%> 2</label> 
	<select name="operName2">
	<% for( int i=0; i<variables.length; i++ ) { 
			String varName=variables[i].getName();
			if( varName.equals( name ) ) {
				continue;
			}
			boolean selected=varName.equals( operName2 );
	%>
		<option value="<%=varName%>" <%=selected?"selected":"" %>><%=varName %></option>
	<% } %>
	</select> 
	<br/>
	<label><%=i18n.say( "Return_type" )%></label> 
	<select name="returnType">
		<option value="<%=FormulaTypeTypeType.FLOAT%>" <%=(FormulaTypeTypeType.FLOAT.toString()).equals(returnType)?"selected":""%>>
			<%=FormulaTypeTypeType.FLOAT%>
		</option>
		<option value="<%=FormulaTypeTypeType.INTEGER%>" <%=(FormulaTypeTypeType.INTEGER.toString()).equals(returnType)?"selected":""%>>
			<%=FormulaTypeTypeType.INTEGER%>
		</option>
	</select>
