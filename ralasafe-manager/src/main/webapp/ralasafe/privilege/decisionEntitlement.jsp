<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.privilege.Privilege"%>
<%@page import="org.ralasafe.servlet.DecisionEntitlementHandler"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">  

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
<script src="../js/ztree/jquery.ztree-2.6.min.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
DecisionEntitlementHandler handler=(DecisionEntitlementHandler) request.getAttribute( "handler" );
String id=request.getParameter("id");

Privilege pvlg=handler.getPrivilege();

String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String[] mainMenuTexts=new String[] {
		pvlg.getType()==0?i18n.say( "Privilege" ):i18n.say( "Non_role_privilege" )
};
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		"./decisionEntitlementTest.rls?id="+id
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		i18n.say( "Test" )
};
request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>

<title><%=i18n.say( "Decision_Privilege" )%> -- <%=pvlg.getName() %></title>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>

<div class="smallContainer" id="entitleDiv">
</div>

<div id="editEntitleDialog">
	<form id="entitleForm" method="post" action="?id=<%=id %>">
	</form>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
jQuery(document).ready(function(){	
	loadEntitle();
	
	// dialog
	jQuery( "#editEntitleDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		width:450,
		height:350,
		buttons: {
			"OK": function() {
				jQuery('#entitleForm').submit();
			}
		}
	});
	
	var entitleFormOptions = { 
			//beforeSubmit:  checkOrderColumnForm,
			success:       afterUpdateEntitle 
		}; 
	
	jQuery('#entitleForm').submit(function() { 
	     jQuery(this).ajaxSubmit(entitleFormOptions); 
	 	 return false; 
	}); 
});

function loadEntitle() {
	jQuery( "#entitleDiv" ).load( "?oper=loadEntitlements&id=<%=id%>" );
}

function afterUpdateEntitle() {
	jQuery( "#editEntitleDialog" ).dialog( "close" );
	loadEntitle();
}

function moveEntitle( direct, index ) {
	jQuery.ajax({
	    url: '?oper=moveEntitle',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index,"direct":direct},
	    error: function(){
	        alert('Error move group column');
	    },
	    success: function(xml){
	    	loadEntitle();
	    }
	});	
}

function deleteEntitle( index ) {
	jQuery.ajax({
	    url: '?oper=deleteEntitle',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","index":index},
	    error: function(){
	        alert('Error delete group column');
	    },
	    success: function(xml){
	    	loadEntitle();
	    }
	});	
}

function editEntitle( index ) {
	jQuery( "#entitleForm" ).load( "?oper=getEntitlement&id=<%=id%>&index="+index,
			function() {
		jQuery( "#editEntitleDialog" ).dialog( "option", "title", '<%=i18n.say("Edit_policy")%>' );
		jQuery( "#editEntitleDialog" ).dialog( "open" );
	});
}

function addEntitle() {
	jQuery( "#entitleForm" ).load( "?oper=getEntitlement&id=<%=id%>",
			function() {
		jQuery( "#editEntitleDialog" ).dialog( "option", "title", '<%=i18n.say("Add_policy")%>' );
		jQuery( "#editEntitleDialog" ).dialog( "open" );
	});
}

//------- save
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