<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryTestHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />

<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/jTreeTheme/classic/style.css" />
<link type="text/css" rel="stylesheet" href="../css/syntaxhighlighter/shCoreDefault.css"/> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
		
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shCore.js"></script> 
<script type="text/javascript" src="../js/syntaxhighlighter/shBrushSql.js"></script> 

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryTestHandler handler=(QueryTestHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String id=request.getParameter( "id" );
boolean testUserNeeded=handler.isTestUserNeeded();
String[] testContextFields=handler.getTestContextFields();

String oper=request.getParameter( "oper" );

String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String[] mainMenuTexts=new String[] {
		i18n.say( "Query" )
};
String edit=i18n.say( "Continue_edit" );
if( "loadFresh".equals( oper ) ) {
	edit=i18n.say( "Edit" );
}
String editUrl="./queryDesign.rls?id="+id;
if( query.getIsRawSQL() ) {
	editUrl="./queryRaw.rls?id="+id;
}
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		editUrl
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		edit
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>
<title><%=i18n.say( "Test_query" )%> -- <%=query.getName() %> </title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<div class="smallContainer">
	<div id="sql">
		<label><%=i18n.say( "SQL" )%></label>
		<pre class="brush: sql;"><%=handler.getSql() %></pre>
	</div>
	
	<form id="testForm" method="post" action="?oper=runTest&id=<%=id%>">
		<input type="hidden" name="first" value="0" />
	<% if( testUserNeeded ) { %>
	<jsp:include page="../common/inputTestUser.jsp"></jsp:include>
	<% } %>
	
	<%if( testContextFields.length>0 ) { 
			request.setAttribute( "testContextFields", testContextFields );
	%>
	<jsp:include page="../common/inputTestContext.jsp"></jsp:include>
	<% } %>
	
	
	<p>
	<input type="button" value="<%=i18n.say( "Run" )%>" onclick="javascript:showResult('0')"/>
	</p>
	</form>

	<div id="result">
	</div>
</div>	

<jsp:include page="../footer.jsp"></jsp:include>
<script>
jQuery( document ).ready( function() {
	SyntaxHighlighter.all();

	var options = { 
		success:       showResponse 
	}; 
	jQuery('#testForm').submit(function() { 
        jQuery(this).ajaxSubmit(options); 
 		return false; 
    }); 	
});

function showResponse(responseText, statusText, xhr, $form)  { 
	jQuery( "#result" ).html( responseText );
} 

function showResult( first ) {
	jQuery( "#testForm :input[name=first]" ).val(first);
	jQuery( "#testForm" ).submit();
}

function setDataPicker( name ) {
	var bdClass=jQuery( "#testForm :input[name="+name+"]" ).datepicker({ dateFormat: 'yy-mm-dd' });
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