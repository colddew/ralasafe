<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.ralasafe.servlet.AbstractTreeHandler" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="../favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafe.css" />

<link rel="stylesheet" type="text/css" media="screen" href="../css/flick/jquery-ui-1.8.5.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../css/ralasafeTree.css" />

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

AbstractTreeHandler handler=(AbstractTreeHandler) request.getAttribute( "treeHandler" );
String contextPath=request.getContextPath();
String type=handler.getTreeType();
%>

<style type="text/css">
	<% if( "query".equals( type ) ) { %>	
	.tree li button.ico_docu{ background:url('../images/query.gif');}
	<% } else if( "businessData".equals( type ) ) { %>
	.tree li button.ico_docu{ background:url('../images/businessData.gif');}
	<% } else if( "userCategory".equals( type ) ) { %>
	.tree li button.ico_docu{ background:url('../images/userCategory.gif');}
	<% } else if( "privilege".equals( type ) ) { %>
	.tree li button.ico_docu{ background:url('../images/privilege.png');}
	<% } %>
</style>
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/jquery-ui-1.8.5.custom.js" type="text/javascript"></script>
<script src="../js/ztree/jquery.ztree-2.6.min.js" type="text/javascript"></script>
<script src="../js/jquery.form.js" type="text/javascript"></script>
<script src="../js/jquery.validate.js" type="text/javascript"></script>
<script src="../js/validate/<%=i18n.getValidateMessageFile()%>" type="text/javascript"></script>

<title><%=i18n.say( handler.getPageTitle() )%></title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>

<%@include file="../menu.jsp" %>

<div class="treeDiv">
	<!-- here is the tree -->
	<ul id="tree" class="tree"></ul>
	<!-- tree ends here -->
</div>

<div class="ctxMenuDiv">
	<!-- context menu dialog -->
	<div id="contextMenu"></div>
	<!-- dialog end -->
</div>
<div class="editDiv">
	<!-- edit dialog -->
	<div id="edit"></div>
	<!-- dialog end -->
</div>		
<div class="clearDiv"></div>	

<jsp:include page="../footer.jsp"></jsp:include>
	
<script type="text/javascript">
var tree; 

jQuery( document ).ready( function() {	
	initTree();
} );

function initTree()
{
	var treeSetting={
			async: true,
			editable: true, 
			edit_renameBtn: false,
			edit_removeBtn: false,
			dragMove: <%="privilege".equals( type )%>, // only privilege tree is draggable
			isSimpleData: true,		
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			asyncUrl: "?oper=loadTree",
			callback:{
				click: clickTreeNode,
				asyncSuccess: afterTreeLoad,
				beforeDrag:beforeDragTreeNode,
				beforeDrop:beforeDropTreeNode
			},
			asyncParam: ["name", "id","isLeaf"]
		};

		var treeNodes=[];
		tree=jQuery("#tree").zTree(treeSetting, treeNodes);
}

function beforeDragTreeNode(treeId,treeNode){
	// reserved privilege is not drap and dropable
	if( treeNode.id<=0 ) {
		return false;
	} else {
		return true;
	}
}
 
function beforeDropTreeNode(treeId,treeNode,targetNode,moveType){
	// leaf nodes cann't have any children
	if( targetNode.isLeaf=="1"&&moveType=="inner" ) {
		return false;
	}

	var targetId=targetNode.id;
	
	var newOrderNum=0;
	if( moveType!="inner" ) {
		targetId=targetNode.parentNode.id;
		newOrderNum=tree.getNodeIndex( targetNode );
		
		if( moveType=="after"&&treeNode.parentNode.id!=targetNode.parentNode.id ) {
			newOrderNum=newOrderNum+1;
		}
	}
	
	jQuery.ajax({            
		type: "Post", 
		url:"?oper=move",  
		data: {"id":"n"+treeNode.id,"parentId":"n"+targetId,"position":newOrderNum},
		success: function(data) {  
		    initTree();
		    return true;
		},  
		error: function(err) {  
		    alert(err); 
		    return false; 
		}  
    });
}

function afterTreeLoad(event, treeId, treeNode, msg) {
	tree.expandAll(true);
}

function clickTreeNode( event, treeId, treeNode ) {
	var id=treeNode.id;

	// clear div content
	jQuery("#contextMenu").html("");
	jQuery("#edit").html("");
	
	// load context menu
	jQuery("#contextMenu").load("?oper=menu&id=n"+id);
}

function createNode( pid ) {
	// clear div content
	jQuery("#edit").html("");
	jQuery("#edit").load("?oper=preCreate&parentId=n"+pid);
}

function beforeCreate( formData, jqForm, options ) {
	// TODO check the entity exist or not?
}

function afterCreate( responseText, statusText, xhr, jqForm ) { 
	 initTree(); 
}

function deleteNode( id ) {
	if( id==0 ) {
		alert( "Can't delete ROOT node" );
		return;
	}

	jQuery("#edit").html("");
	jQuery.ajax( {
		url: "?oper=delete&id=n"+id,
		type: "POST",
		success: function(msg) { 
			initTree(); 
			jQuery("#contextMenu").html("");
			jQuery("#edit").html("Entity is deleted successfully!");
		}
	} );
}

function modifyNode( id ) {
	if( id==0 ) {
		alert( "Can't modify ROOT node" );
		return;
	}
 
	jQuery("#edit").html("");
	jQuery("#edit").load("?oper=preModify&id=n"+id);
}

function beforeModify( formData, jqForm, options ) {
	// TODO check the entity exist or not?
}

function afterModify( responseText, statusText, xhr, jqForm ) {
	initTree(); 
 
	jQuery("#contextMenu").html("");
	jQuery("#edit").html("Entity is updated successfully!");
}

function copyNode( id ) {
	// clear div content
	jQuery("#edit").html("");
	jQuery("#edit").load("?oper=preCopy&id=n"+id);
}

function afterCopy( responseText, statusText, xhr, jqForm ) {
	initTree(); 
 
	jQuery("#contextMenu").html("");
	jQuery("#edit").html("Entity is copied successfully!");
}
</script>
</body>
</html>