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
<%@ page import="org.ralasafe.db.sql.xml.ContextValue" %>

<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) request.getAttribute( "handler" );
String id=request.getParameter( "id" );
%>

<div class="treeDiv">
	<label><%=i18n.say( "Expression" )%></label>
	<ul id="exprGroup" class="tree"></ul>
</div>
<div class="editDiv">
	<div id="exprContextMenu"></div>
</div>

<div class="clearDiv"></div>
<script type="text/javascript">
jQuery( document ).ready( function() {
	var exprTreeSetting={
		async: true,
		/**editable: true,
		edit_renameBtn: false,
		edit_removeBtn: false,
		dragMove: true,*/
		isSimpleData: true,		
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "?oper=loadExprTree&id=<%=id%>",
		callback:{
			click: clickExprTree
		},
		asyncParam: ["name", "id"]
	};

	var treeNodes=[];
	jQuery("#exprGroup").zTree(exprTreeSetting, treeNodes);
} );


</script>