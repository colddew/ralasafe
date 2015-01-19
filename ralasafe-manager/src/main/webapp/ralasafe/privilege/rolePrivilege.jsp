<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.ralasafe.privilege.Role"%>
<%@page import="org.ralasafe.privilege.Privilege"%>

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
%>

<title><%=i18n.say( "Role_privilege_mapping" )%></title>
<body>
<%
Collection assignedPvlgs=(Collection) request.getAttribute("assignedPvlgs");
Role role=(Role) request.getAttribute( "role" );

String[] mainMenuHrefs=new String[]{
		"/ralasafe/privilege/roleMng.rls"		
};
String[] mainMenuTexts=new String[] {
		i18n.say( "Role" ),
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
%>


<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>


<div class="smallContainer">
	<label><%=i18n.say( "Role" )%> </label>&nbsp;&nbsp;&nbsp;---&nbsp;&nbsp;&nbsp;<%=role.getName()%>
	<ul id="tree" class="tree"></ul><br/>
	<input type="button" value="OK" onclick="javascript:assignRvlgs()"/>
</div>

<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
var tree;

jQuery(document).ready(function(){	
	var treeSetting={
		async: true,
		checkable : true,
		isSimpleData: true,		
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../privilege/privilegeMng.rls?oper=loadTree",
		callback:{
			asyncSuccess: afterTreeLoad
		},
		asyncParam: ["name", "id","isLeaf"]
	};

	var treeNodes=[];
	tree=jQuery("#tree").zTree(treeSetting, treeNodes);
});

function afterTreeLoad(event, treeId, treeNode, msg) {
	tree.expandAll(true);

	// check up nodes
	var node;
	<%for( Iterator iter=assignedPvlgs.iterator(); iter.hasNext(); ) {
		Privilege pvlg=(Privilege) iter.next();
	%>	
	node=tree.getNodeByParam("id", <%=pvlg.getId()%>);
	node.checked=true;
	if( node.isLeaf=='1' ) {
		tree.updateNode( node, true );
	} else {
		tree.updateNode( node, false );
	}
	<% } %>
}

function assignRvlgs() {
	var checkedNodes=tree.getCheckedNodes(true);
	var selectedItems = new Array();
	for( var i=0; i<checkedNodes.length; i++ ) {
		selectedItems.push( checkedNodes[i].id );
	}
	
	jQuery.ajax( {
		url: "?roleId=<%=role.getId()%>",
		type: "POST",
		data: {"pvlgIds": selectedItems.join(',')},
		success: function(msg) { 
			alert( "<%=i18n.say( "saved" ) %>" );
		}
	} );
}
</script>	

</body>
</html>