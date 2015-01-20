<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.ralasafe.db.sql.xml.DefineVariable" %>
<%@ page import="org.ralasafe.servlet.AbstractPolicyDesignHandler" %>
<%@ page import="org.ralasafe.db.sql.xml.QueryRef" %>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

DefineVariable var=(DefineVariable) request.getAttribute( "variable" );
String oper="addVariable";
String name="";
String queryId="";
String queryName="";
String varIndex="-1";
String id=request.getParameter( "id" );

if( var!=null ) {
	QueryRef value=var.getQueryRef();
	
	varIndex=request.getParameter( "index" );
	oper="updateVariable";
	queryId=""+value.getId();
	name=var.getName();
	queryName=value.getName();
}
%>


	<input type="hidden" name="oper" value="<%=oper%>"/>
	<input type="hidden" name="id" value="<%=id%>"/>
	<input type="hidden" name="type" value="queryRef"/>
	<input type="hidden" name="index" value="<%=varIndex%>"/>
	<label><%=i18n.say( "Variable_name" )%></label>
	<input type="text" name="name" value="<%=name%>"  class="required" style="width:400px;"/>
	<br/>
	<input type="hidden" name="queryId" id="queryId" value="<%=queryId%>"/>
	<label><%=i18n.say( "Query" )%></label>
	<input type="text" name="queryName" readonly id="queryName" value="<%=queryName%>" onclick="showMenu(); return false;" class="required"  style="width:400px;"/>
	<div id="dropdownMenuBackground" style="display:none; height:200px; width:400px; background-color:white;border:1px solid;overflow-y:auto;overflow-x:auto;">
		<ul id="dropdownMenu" class="tree"></ul>
	</div>

<script type="text/javascript">
var queryTree;
jQuery( document ).ready( function() {
	var setting = {
		async: true,
		isSimpleData: true,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../query/queryMng?oper=loadTree",
		callback: {
			//beforeExpand: function(){return false;},
			//beforeCollapse: function(){return false;},
			beforeClick: zTreeOnBeforeClick,
			click: zTreeOnClick,
			asyncSuccess: afterQueryTreeLoad
		},
		asyncParam: ["name", "id","isLeaf"]
	};

	var treeNodes=[];
	queryTree=jQuery("#dropdownMenu").zTree(setting, treeNodes);	
} );

function afterQueryTreeLoad(event, treeId, treeNode, msg) {
	queryTree.expandAll(true);
}

function showMenu() {
	jQuery("#dropdownMenuBackground").slideDown("fast");
	
}

function hideMenu() {
	jQuery("#dropdownMenuBackground").fadeOut("fast");
}

function zTreeOnBeforeClick(treeId, treeNode) {
	var checked = (treeNode && (treeNode.isLeaf==1));
	if (!checked) {
		alert( "Please choose a query, don't choose query group" );
	}
	return checked;
}

function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		var queryIdObj = jQuery("#queryId");
		queryIdObj.attr("value", treeNode.id);
		var queryNameObj = jQuery("#queryName");
		queryNameObj.attr("value", treeNode.name);
		hideMenu();
	}
}
</script>	