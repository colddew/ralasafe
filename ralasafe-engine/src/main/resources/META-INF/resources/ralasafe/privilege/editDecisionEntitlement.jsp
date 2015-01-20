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
<%@page import="org.ralasafe.entitle.DecisionEntitlement"%>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
String id=request.getParameter("id");
String index=request.getParameter( "index" );

DecisionEntitlement entitle=(DecisionEntitlement) request.getAttribute( "entitlement" );
String ucName="";
String ucId="";
String bdName="";
String bdId="";
String denyReason="";
String effect="";

if( entitle!=null ) {
	bdId=entitle.getBusinessData().getId()+"";
	bdName=entitle.getBusinessData().getName();
	ucName=entitle.getUserCategory().getName();
	ucId=entitle.getUserCategory().getId()+"";
	denyReason=entitle.getDenyReason();
	effect=entitle.getEffect();
}
%>

<% if( entitle==null ) { %>
<input type="hidden" name="oper" value="addEntitle"/>
<% } else { %>
<input type="hidden" name="oper" value="editEntitle"/>
<input type="hidden" name="index" value="<%=index %>"/>
<% } %>

<input type="hidden" name="bdId" id="bdId" value="<%=bdId%>"/>
<input type="hidden" name="ucId" id="ucId" value="<%=ucId%>"/>
<label><%=i18n.say( "Effect" ) %></label>
<select name="effect">
	<option value="permit"><%=i18n.say("Permit") %></option>
	<option value="deny" <%=effect.equals( "deny" )?"selected":"" %>><%=i18n.say("Deny") %></option>
</select>
<label><%=i18n.say( "User_category" )%></label>
<input type="text" name="ucName" readonly id="ucName" value="<%=ucName%>" onclick="showUcTree(); return false;" style="width:400px;"/>
<div id="ucTreeDiv" style="display:none; height:200px; width:400px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;">
	<ul id="ucTree" class="tree"></ul>
</div>
<label><%=i18n.say( "Business_data" )%></label>
<input type="text" name="bdName" readonly id="bdName" value="<%=bdName%>" onclick="showBdTree(); return false;" style="width:400px;"/>
<div id="bdTreeDiv" style="display:none; height:200px; width:400px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;">
	<ul id="bdTree" class="tree"></ul>
</div>
<label><%=i18n.say( "Deny_reason" )%></label>
<input type="text" name="denyReason" value="<%=denyReason%>" style="width:400px;"/>

<script type="text/javascript">
var bdTree;
var ucTree;

jQuery( document ).ready( function() {
	//--- bd tree
	var bdSetting = {
		async: true,
		isSimpleData: true,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../businessData/businessDataMng.rls?oper=loadTree",
		callback: {
			//beforeExpand: function(){return false;},
			//beforeCollapse: function(){return false;},
			beforeClick: beforeClickBdNode,
			click: clickBdNode,
			asyncSuccess: afterBdTreeLoad
		},
		asyncParam: ["name", "id","isLeaf"]
	};

	var bdTreeNodes=[];
	bdTree=jQuery("#bdTree").zTree(bdSetting, bdTreeNodes );	

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

//------- bd functions
function afterBdTreeLoad(event, treeId, treeNode, msg) {
	bdTree.expandAll(true);
}

function showBdTree() {
	jQuery("#bdTreeDiv").slideDown("fast");
	
}

function hideBdTree() {
	jQuery("#bdTreeDiv").fadeOut("fast");
}

function beforeClickBdNode(treeId, treeNode) {
	var checked = (treeNode && (treeNode.isLeaf==1));
	if (!checked) {
		alert( "Please choose a business data, don't choose group" );
	}
	return checked;
}

function clickBdNode(event, treeId, treeNode) {
	if (treeNode) {
		var queryIdObj = jQuery("#bdId");
		queryIdObj.attr("value", treeNode.id);
		var queryNameObj = jQuery("#bdName");
		queryNameObj.attr("value", treeNode.name);
		hideBdTree();
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