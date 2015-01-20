<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryRawHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.RawSQL"%>        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />

<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/jTreeTheme/classic/style.css" />
	
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryRawHandler handler=(QueryRawHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
RawSQL raw=query.getRawSQL();
String id=request.getParameter( "id" );

String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String[] mainMenuTexts=new String[] {
		i18n.say( "Query" )
};
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		"./queryTest.rls?id="+id,
		"./queryDesign.rls?id="+id
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
<title><%=i18n.say( "Manual_Query" )%> -- <%=query.getName() %> </title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>


<div id="sql" class="smallContainer">
<label><%=i18n.say( "SQL" )%></label>
<textarea rows="7" cols="71" name="rawSql" id="rawSql"><%=raw.getContent()%></textarea><br/>
<input type="button" value="OK" onclick="javascript:setRawSql()" />
</div>

<div id="param" class="smallContainer">
</div>

<div id="mapping" class="smallContainer">
</div>

<div id="paramDialog" title="Add/Edit parameter">
	<form id="paramForm" method="post" action="?id=<%=id %>">
	</form>
</div>

<div id="propertyDialog" title="Add/Edit property">
	<form id="propertyForm" method="post" action="?id=<%=id %>">
	</form>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
<script>

jQuery( document ).ready( function() {
	jQuery( "#param" ).load( "?oper=param&id=<%=id%>" );
	jQuery( "#mapping" ).load( "?oper=mapping&id=<%=id%>" );

	//--- param dialog and form
	// dialog
	jQuery( "#paramDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#paramForm').submit();
			}
		}
	});

	var paraFormOptions = { 
		beforeSubmit:  checkParamForm,
		success:       afterUpdateParamForm 
	}; 
	    
	jQuery('#paramForm').submit(function() { 
	     jQuery(this).ajaxSubmit(paraFormOptions); 
	 	 return false; 
	});

	//--- property dialog and form
	// dialog
	jQuery( "#propertyDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#propertyForm').submit();
			}
		}
	});

	var propertyFormOptions = { 
		beforeSubmit:  checkPropertyForm,
		success:       afterUpdatePropertyForm 
	}; 
	    
	jQuery('#propertyForm').submit(function() { 
	     jQuery(this).ajaxSubmit(propertyFormOptions); 
	 	 return false; 
	});

	
});

function setRawSql() {
	var rawSql=jQuery( "#rawSql" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setRawSql',
	    type: 'POST',
	    data: {"id":"<%=id%>","rawSql":rawSql},
	    error: function(){
	        alert('Error set raw sql');
	    },
	    success: function(xml){
	    	//jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});
}

function setMappingClass() {
	var mappingClass=jQuery( "#mappingClass" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setMappingClass',
	    type: 'POST',
	    data: {"id":"<%=id%>","mappingClass":mappingClass},
	    error: function(){
	        alert('Error mapping class');
	    },
	    success: function(xml){
	    	//jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});
}

//-------- param functions
function addParam() {
	jQuery( "#paramForm" ).load( "?oper=editParam&id=<%=id%>",
		function() {
			jQuery( "#paramDialog" ).dialog( "open" );
	});
}

function checkParamForm() {
	
}

function afterUpdateParamForm() {
	jQuery( "#paramDialog" ).dialog( "close" );
	jQuery( "#param" ).load( "?oper=param&id=<%=id%>" );
}

function editParam( index ) {
	jQuery( "#paramForm" ).load( "?oper=editParam&id=<%=id%>&index="+index,
			function() {
		jQuery( "#paramDialog" ).dialog( "open" );
	});
}

function moveParam( direct, index ) {
	jQuery.ajax({
	    url: '?oper=moveParam',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index,"direct":direct},
	    error: function(){
	        alert('Error move parameter');
	    },
	    success: function(xml){
	    	jQuery( "#param" ).load( "?oper=param&id=<%=id%>" );
	    }
	});	
}

function deleteParam( index ) {
	jQuery.ajax({
	    url: '?oper=deleteParam',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index},
	    error: function(){
	        alert('Error delete parameter');
	    },
	    success: function(xml){
	    	jQuery( "#param" ).load( "?oper=param&id=<%=id%>" );
	    }
	});	
}

//-------- property functions
function addProperty() {
	jQuery( "#propertyForm" ).load( "?oper=editProperty&id=<%=id%>",
			function() {
		jQuery( "#propertyDialog" ).dialog( "open" );
	});
}

function checkPropertyForm() {
	
}

function afterUpdatePropertyForm() {
	jQuery( "#propertyDialog" ).dialog( "close" );
	jQuery( "#mapping" ).load( "?oper=mapping&id=<%=id%>" );
}

function editProperty( index ) {
	jQuery( "#propertyForm" ).load( "?oper=editProperty&id=<%=id%>&index="+index,
			function() {
		jQuery( "#propertyDialog" ).dialog( "open" );
	});
}

function moveProperty( direct, index ) {
	jQuery.ajax({
	    url: '?oper=moveProperty',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index,"direct":direct},
	    error: function(){
	        alert('Error move property');
	    },
	    success: function(xml){
	    	jQuery( "#mapping" ).load( "?oper=mapping&id=<%=id%>" );
	    }
	});	
}

function deleteProperty( index ) {
	jQuery.ajax({
	    url: '?oper=deleteProperty',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index},
	    error: function(){
	        alert('Error delete property');
	    },
	    success: function(xml){
	    	jQuery( "#mapping" ).load( "?oper=mapping&id=<%=id%>" );
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
	    	alert( "<%=i18n.say( "saved" )%>" );
	    }
	});
}
</script>
</body>
</html>