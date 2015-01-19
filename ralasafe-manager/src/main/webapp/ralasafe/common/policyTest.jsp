<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.AbstractPolicyDesignHandler"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link type="text/css" rel="stylesheet" href="../css/syntaxhighlighter/shCoreDefault.css"/>

<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shCore.js"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shBrushCss.js"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shBrushJScript.js"></script>
<script type="text/javascript" src="../js/syntaxhighlighter/shBrushJava.js"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<script src="../js/jquery.validate.js" type="text/javascript"></script>
<script src="../js/validate/<%=i18n.getValidateMessageFile()%>" type="text/javascript"></script>

<title><%=i18n.say( "Test_Policy" )%> -- <%=handler.getPolicy().getName() %></title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>

<%
boolean testUserNeeded=handler.isTestUserNeeded();
String[] bdFields=handler.getTestBusinessDataFields();
String[] ctxFields=handler.getTestContextFields();
String script=handler.getScript();
String id=request.getParameter( "id" );

String oper=request.getParameter( "oper" );

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
String edit=i18n.say( "Continue_edit" );
if( "loadFresh".equals( oper ) ) {
	edit=i18n.say( "Edit" );
}
String editUrl="./policyDesign.rls?id="+id;
if( handler.getPolicy().isRawScript() ) {
	editUrl="./policyRaw.rls?id="+id;
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

<%@include file="../menu.jsp" %>

<div class="smallContainer">
	<label><%=i18n.say( "Script" )%></label>
	<div id="script">
	<pre class="brush: java;"><%=script%></pre>
	</div>
	
	<form id="testForm" method="post" action="?oper=runTest&id=<%=id%>">
	<%
	if( testUserNeeded ) {
	%>
	<jsp:include page="inputTestUser.jsp"></jsp:include>
	<br/>
	<% } %>
	
	<%if( ctxFields.length>0 ) { 
		request.setAttribute( "testContextFields", ctxFields );
	%>
	<jsp:include page="inputTestContext.jsp"></jsp:include>
	<br/>
	<% } %>
	
	<div id="bdDiv">
	<%if( bdFields.length>0 ) { 
		request.setAttribute("testBusinessDataFields", handler.getTestBusinessDataFields() );
		request.setAttribute("testBusinessDataFieldTypes", handler.getTestBusinessDataFieldTypes() );
	%>
	<jsp:include page="inputTestBusinessData.jsp"></jsp:include>
	<% } %>
	</div>
	
	<p>
	<input type="submit" value="<%=i18n.say( "Run" )%>"/>
	</p>
	</form>
	
	<div id="result">
	</div>
	
	<div id="output2">
	</div>

</div>

<jsp:include page="../footer.jsp"></jsp:include>

<script language="javascript">
jQuery( document ).ready( function() {
	SyntaxHighlighter.all();

	var options = { 
		beforeSubmit:  checkTestForm,
		success:       showResponse 
	}; 
	jQuery('#testForm').submit(function() { 
        jQuery(this).ajaxSubmit(options); 
 		return false; 
    }); 

    // validate
    jQuery('#testForm').validate();
} );

function checkTestForm() {
	return jQuery( "#testForm" ).valid();
}

function showResponse(responseText, statusText, xhr, $form)  { 
	jQuery( "#result" ).html( responseText );
} 

function loadJavabeanProperty() {
	var bdClass=jQuery( "#testForm :input[name=bdClass]" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setBusinessDataClass',
	    type: 'POST',
	    data: {"id":"<%=id%>","businessDataClass":bdClass},
	    error: function(){
	        alert('Error business data class');
	    },
	    success: function(resp){
	    	jQuery( "#bdDiv" ).html( resp );
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

function setDataPicker( name ) {
	var bdClass=jQuery( "#testForm :input[name="+name+"]" ).datepicker({ dateFormat: 'yy-mm-dd' });
}
</script>
</body>
</html>