<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.AbstractPolicyDesignHandler"%>
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
String id=request.getParameter( "id" );
%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />

<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafeTree.css" />

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<script src="../js/ztree/jquery.ztree-2.6.min.js" type="text/javascript"></script>

<title><%=handler.getRawPageTitle()%></title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%
String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String mngText=i18n.say( "User_category" );
if( "businessData".equals( handler.getPolicyType() ) ) {
	mngText=i18n.say( "Business_data" );
}
String[] mainMenuTexts=new String[] {
		mngText
};
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		"./policyTest.rls?id="+id,
		"./policyDesign.rls?id="+id
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		i18n.say( "Test" ),
		i18n.say( "Goto_design_mode" )
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>
<%@include file="../menu.jsp" %>


<div class="clearDiv"></div>

<div class="smallContainer">
	<label><%=i18n.say( "Varibles" )%></label>
</div>
<div id="variable-tab" class="smallContainer">
</div>


<div id="script" class="smallContainer">
	<label><%=i18n.say( "Script" )%></label><br/>
	<textarea rows="7" cols="71" name="rawSql" id="rawScript"><%=handler.getRawScript()%></textarea><br/>
	<input type="button" value="OK" onclick="javascript:setRawScript()" />
</div>

<div class="clearDiv"></div>

<div id="variableTypeDialog" title="Choose variable type">
	<input type="radio" name="varType" value="userValue" checked="checked" /><%=i18n.say( "User_value" )%><br/>
	<input type="radio" name="varType" value="simpleValue" /><%=i18n.say( "Simple_value" )%><br/>
	<input type="radio" name="varType" value="contextValue" /><%=i18n.say( "Context_value" )%><br/>
	<%if( "businessData".equals( handler.getPolicyType() ) ) { %>
	<input type="radio" name="varType" value="hintValue" /><%=i18n.say( "Business_data_attribute" )%><br/>
	<% } %>
	<input type="radio" name="varType" value="query" /><%=i18n.say( "Query" )%><br/>
	<input type="radio" name="varType" value="formula" /><%=i18n.say( "Formula" )%><br/>
</div>

<div id="variableDialog" title="Add/Edit variable">
	<form id="variableForm" method="POST" action="?id=<%=id %>"></form>
</div>



<jsp:include page="../footer.jsp"></jsp:include>

<script language="javascript">
jQuery( document ).ready( function() {
	jQuery( "#variable-tab" ).load( "?oper=designVariables&id=<%=id%>" );
	
	//--- select variable dialog
	jQuery('#variableTypeDialog').dialog( {
		modal: true, 
		autoOpen: false,
		position: 'middle',
		buttons: {
			"OK": function() {
				showAddVariableDialog();
			}
		}
	} );

	//--- edit variable dialog
	jQuery('#variableDialog').dialog( {
		modal: true, 
		autoOpen: false,
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#variableForm').submit();
			}
		}
	} );

	var variableFormOptions = { 
			beforeSubmit:  checkVariableForm,
			success:       afterUpdateVariableForm 
		}; 
	
	jQuery('#variableForm').submit(function() { 
	     jQuery(this).ajaxSubmit(variableFormOptions); 
	 	 return false; 
	}); 
} );

//--- variable functions
function selectAddVariableType() {
	jQuery("#variableTypeDialog").dialog( "open" );
}

function showAddVariableDialog() {
	jQuery("#variableTypeDialog").dialog( "close" );
	var type=jQuery( "#variableTypeDialog :input[name=varType][checked]" ).val();
	
	jQuery('#variableForm').load('?oper=newVariable&id=<%=id%>&type='+type,
			function() {
		jQuery("#variableDialog").dialog( "open" );
	});
}

function editVariable(index) {
	jQuery('#variableForm').load('?oper=getVariable&id=<%=id%>&index='+index, 
			function() {
		jQuery("#variableDialog").dialog( "open" );
	});
}

function checkVariableForm() {
	
}

function afterUpdateVariableForm() {
	jQuery("#variableDialog").dialog( "close" );
	jQuery( "#variable-tab" ).load( "?oper=designVariables&id=<%=id%>" );
}

function deleteVariable( index ) {
	jQuery.ajax({
	    url: '?oper=deleteVariable',
	    type: 'POST',
	    data: {"id":"<%=id%>","index":index},
	    error: function(){
	        alert('Error delete variable');
	    },
	    success: function(xml){
	    	jQuery( "#variable-tab" ).load( "?oper=designVariables&id=<%=id%>" );
	    }
	});
}

function savePolicy() {
	jQuery.ajax({
	    url: '?oper=save',
	    type: 'POST',
	    data: {"id":"<%=id%>"},
	    error: function(){
	        alert('Error save policy');
	    },
	    success: function(xml){
	    	alert( "<%=i18n.say("saved")%>" );
	    }
	});
}

function setRawScript() {
	var rawScript=jQuery( "#rawScript" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setRawScript',
	    type: 'POST',
	    data: {"id":"<%=id%>","rawScript":rawScript},
	    error: function(){
	        alert('Error set raw script');
	    },
	    success: function(xml){
	    }
	});
}
</script>
</body>
</html>