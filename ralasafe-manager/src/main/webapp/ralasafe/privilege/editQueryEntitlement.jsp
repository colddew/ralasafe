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
<%@page import="org.ralasafe.entitle.QueryEntitlement"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
String id=request.getParameter("id");
String index=request.getParameter( "index" );

QueryEntitlement entitle=(QueryEntitlement) request.getAttribute( "entitlement" );
String ucName="";
String ucId="";
String queryName="";
String queryId="";
String description="";

if( entitle!=null ) {
	queryId=entitle.getQuery().getId()+"";
	queryName=entitle.getQuery().getName();
	ucName=entitle.getUserCategory().getName();
	ucId=entitle.getUserCategory().getId()+"";
	description=entitle.getDescription();
}
%>

<% if( entitle==null ) { %>
<input type="hidden" name="oper" value="addEntitle"/>
<% } else { %>
<input type="hidden" name="oper" value="editEntitle"/>
<input type="hidden" name="index" value="<%=index %>"/>
<% } %>

<input type="hidden" name="queryId" id="queryId" value="<%=queryId%>"/>
<input type="hidden" name="ucId" id="ucId" value="<%=ucId%>"/>

<label><%=i18n.say( "User_category" )%></label>
<input type="text" name="ucName" readonly id="ucName" value="<%=ucName%>" onclick="showUcTree(); return false;" style="width:400px;"/>
<div id="ucTreeDiv" style="display:none; height:200px; width:400px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;">
	<ul id="ucTree" class="tree"></ul>
</div>
<label><%=i18n.say( "Query" )%></label>
<input type="text" name="queryName" readonly id="queryName" value="<%=queryName%>" onclick="showQueryTree(); return false;" style="width:400px;"/>
<div id="queryTreeDiv" style="display:none; height:200px; width:400px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;">
	<ul id="queryTree" class="tree"></ul>
</div>
<label><%=i18n.say( "Description" )%></label>
<input type="text" name="description" value="<%=description %>" style="width:400px;"/>

<script type="text/javascript">
var queryTree;
var ucTree;

jQuery( document ).ready( function() {
	//--- query tree
	var querySetting = {
		async: true,
		isSimpleData: true,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../query/queryMng.rls?oper=loadTree",
		callback: {
			//beforeExpand: function(){return false;},
			//beforeCollapse: function(){return false;},
			beforeClick: beforeClickQueryNode,
			click: clickQueryNode,
			asyncSuccess: afterQueryTreeLoad
		},
		asyncParam: ["name", "id","isLeaf"]
	};

	var queryTreeNodes=[];
	queryTree=jQuery("#queryTree").zTree(querySetting, queryTreeNodes );	

	//--- user category tree
	var ucSetting = {
		async: true,
		isSimpleData: true,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../userCategory/userCategoryMng.rls?oper=loadTree",
		callback: {
			//beforeExpand: function(){return false;},
			//beforeCollapse: function(){return false;},
			beforeClick: beforeClickUcNode,
			click: clickUcNode,
			asyncSuccess: afterUcTreeLoad
		},
		asyncParam: ["name", "id","isLeaf"]
	};

	var ucTreeNodes=[];
	ucTree=jQuery("#ucTree").zTree(ucSetting, ucTreeNodes );	
} );

//------- query functions
function afterQueryTreeLoad(event, treeId, treeNode, msg) {
	queryTree.expandAll(true);
}

function showQueryTree() {
	jQuery("#queryTreeDiv").slideDown("fast");
	
}

function hideQueryTree() {
	jQuery("#queryTreeDiv").fadeOut("fast");
}

function beforeClickQueryNode(treeId, treeNode) {
	var checked = (treeNode && (treeNode.isLeaf==1));
	if (!checked) {
		alert( "Please choose a query, don't choose query group" );
	}
	return checked;
}

function clickQueryNode(event, treeId, treeNode) {
	if (treeNode) {
		var queryIdObj = jQuery("#queryId");
		queryIdObj.attr("value", treeNode.id);
		var queryNameObj = jQuery("#queryName");
		queryNameObj.attr("value", treeNode.name);
		hideQueryTree();
	}
}

//-------- user category functions
function afterUcTreeLoad(event, treeId, treeNode, msg) {
	ucTree.expandAll(true);
}

function showUcTree() {
	jQuery("#ucTreeDiv").slideDown("fast");
	
}

function hideUcTree() {
	jQuery("#ucTreeDiv").fadeOut("fast");
}

function beforeClickUcNode(treeId, treeNode) {
	var checked = (treeNode && (treeNode.isLeaf==1));
	if (!checked) {
		alert( "Please choose a user category, don't choose group" );
	}
	return checked;
}

function clickUcNode(event, treeId, treeNode) {
	if (treeNode) {
		var ucIdObj = jQuery("#ucId");
		ucIdObj.attr("value", treeNode.id);
		var ucNameObj = jQuery("#ucName");
		ucNameObj.attr("value", treeNode.name);
		hideUcTree();
	}
}
</script>