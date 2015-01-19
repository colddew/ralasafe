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
<script src="../js/jquery.validate.js" type="text/javascript"></script>
<script src="../js/validate/<%=i18n.getValidateMessageFile()%>" type="text/javascript"></script>

<title><%=handler.getDesignPageTitle()%></title>
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
		"./policyRaw.rls?id="+id
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		i18n.say( "Test" ),
		i18n.say( "Goto_manual_mode" )
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>
<%@include file="../menu.jsp" %>


<div id="tabs">
	<ul>
		<li><a href="?oper=designVariables&id=<%=id%>" title="variable-tab"><%=i18n.say( "Varibles" )%></a></li>
		<li><a href="?oper=designExpr&id=<%=id%>" title="expr-tab"><%=i18n.say( "Expression" )%></a></li>
	</ul>
</div>

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

<div id="editExprDialog">
	<form id="exprForm" method="post" action="?id=<%=id%>">
	</form>
</div>

<div id="exprGroupTypeDialog">
	<input type="hidden" name="addGrp" value="true" />
	<input type="radio" name="exprGroupType" value="AND" checked="checked"/>AND
	<input type="radio" name="exprGroupType" value="OR"/>OR
</div>


<jsp:include page="../footer.jsp"></jsp:include>

<script language="javascript">
jQuery( document ).ready( function() {
	jQuery.ajaxSetup({cache:false});
	
	jQuery( "#tabs" ).tabs({
		
		ajaxOptions: {
			error: function( xhr, status, index, anchor ) {
				$( anchor.hash ).html(
					"Couldn't load this tab. Contact administrator please. " );
			}
		}
	});

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

	//--- expr dialog
	var exprFormOptions = { 
		//beforeSubmit:  checkPropertyForm,
		success:       doAfterEditExpr 
	}; 
	    
	jQuery('#exprForm').submit(function() { 
	     jQuery(this).ajaxSubmit(exprFormOptions); 
	 	 return false; 
	});
		
	// dialog
	jQuery( "#editExprDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#exprForm').submit();
			}
		}
	});

	jQuery( "#exprGroupTypeDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				doAfterEditExprGroup();
			}
		}
	});
} );

//--- variable functions
function selectAddVariableType() {
	jQuery("#variableTypeDialog").dialog( "open" );
}

function showAddVariableDialog() {
	jQuery("#variableTypeDialog").dialog( "close" );
	var type=jQuery( "#variableTypeDialog :input[name=varType][checked]" ).val();
	if(type == 'query'){
		jQuery("#variableDialog").dialog( "option", "width", 460);
		jQuery("#variableDialog").dialog( "option", "height", 450);
	}
	//jQuery("#variableDialog").dialog( "option", "position", 'middle');
	jQuery('#variableForm').load('?oper=newVariable&id=<%=id%>&type='+type, 
			function() {
		jQuery("#variableDialog").dialog( "open" );
	});
}

function editVariable(index) {
	jQuery('#variableForm').load('?oper=getVariable&id=<%=id%>&index='+index,
			function() {
		jQuery("#variableDialog").dialog( "open" );
		jQuery("#variableForm").validate();
	});
}

function checkVariableForm() {
	return jQuery( "#variableForm" ).valid();
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

//------------expr functions
function clickExprTree( event, treeId, treeNode ) {
	var nodeId=treeNode.id;

	// clear context menu
	jQuery( "#exprContextMenu" ).html();

	// load dynamic context menu
	jQuery( "#exprContextMenu" ).load( "?oper=exprContextMenu&id=<%=id%>&nodeId="+nodeId ); 
}

function doAfterEditExpr() {
	jQuery( "#editExprDialog" ).dialog( "close" );
	jQuery( "#expr-tab" ).load( "?oper=designExpr&id=<%=id%>" );
}

var exprNodeId;
function addExprChildExprGroup( nodeId ) {
	exprNodeId=nodeId;
	
	jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val("true");
	jQuery( "#exprGroupTypeDialog" ).dialog( "open" );	
}

function doAfterEditExprGroup() {
	jQuery( "#exprGroupTypeDialog" ).dialog( "close" );

	var type=jQuery( "#exprGroupTypeDialog :input[name=exprGroupType][checked]" ).val();
	var addGrp=jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val();

	if( "true"==addGrp ) {
		jQuery.ajax({
		    url: '?oper=addExprChildExprGroup',
		    type: 'POST',
		    async: false,
		    data: {"id":"<%=id%>","nodeId":exprNodeId,"type":type},
		    error: function(){
		        alert('Error add expression group');
		    },
		    success: function(xml){
		    	jQuery( "#expr-tab" ).load( "?oper=designExpr&id=<%=id%>" );
		    }
		});	
	} else {
		jQuery.ajax({
		    url: '?oper=editExprGroup',
		    type: 'POST',
		    async: false,
		    data: {"id":"<%=id%>","nodeId":exprNodeId,"type":type},
		    error: function(){
		        alert('Error update expression group');
		    },
		    success: function(xml){
		    	jQuery( "#expr-tab" ).load( "?oper=designExpr&id=<%=id%>" );
		    }
		});	
	}
}

function deleteExpr( nodeId ) {
	jQuery.ajax({
	    url: '?oper=deleteExpr',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","nodeId":nodeId},
	    error: function(){
	        alert('Error delete expression');
	    },
	    success: function(xml){
	    	jQuery( "#expr-tab" ).load( "?oper=designExpr&id=<%=id%>" );
	    }
	});	
}

function editExprGroupType( nodeId, linkedType ) {
	exprNodeId=nodeId;
	
	jQuery( "#exprGroupTypeDialog :input[name=exprGroupType][value="+linkedType+"]" ).attr( "checked", linkedType );
	jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val("false");
	
	jQuery( "#exprGroupTypeDialog" ).dialog( "open" );
}

function addExprChildExpr( nodeId, exprType ) {
	jQuery( "#exprForm" ).load( "?oper=loadExprForm&id=<%=id%>&exprType="+exprType+"&pId="+nodeId,
			function() {
		jQuery( "#editExprDialog" ).dialog( "open" );
	});
}

function editExpr( nodeId ) {
	jQuery( "#exprForm" ).load( "?oper=loadExprForm&id=<%=id%>&nodeId="+nodeId, 
			function() {
		jQuery( "#editExprDialog" ).dialog( "open" );
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
</script>
</body>
</html>