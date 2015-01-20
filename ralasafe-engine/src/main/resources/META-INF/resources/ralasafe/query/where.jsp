<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.QueryDesignHandler"%>
<%@page import="org.ralasafe.db.sql.xml.QueryType"%>
<%@page import="org.ralasafe.db.sql.xml.Table"%>
<%@page import="org.ralasafe.db.sql.xml.Column"%>
<%@page import="org.ralasafe.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String id=request.getParameter( "id" );

Column[] columns=query.getQueryTypeSequence().getGroupBy().getColumn();
Map aliasNameMap=new HashMap();
Table[] tables=query.getQueryTypeSequence().getFrom().getTable();
for( int i=0; i<tables.length; i++ ) {
	aliasNameMap.put( tables[i].getAlias(), tables[i].getName() );
}
%> 

<div class="treeDiv">
	<label><%=i18n.say( "Expression" )%></label>
	<ul id="exprGroup" class="tree"></ul>
</div>
<div class="editDiv">
	<div id="whereExprContextMenu"></div>
</div>

<div class="clearDiv"></div>
<script type="text/javascript">
jQuery( document ).ready( function() {
	var whereTreeSetting={
		async: true,
		/**editable: true,
		edit_renameBtn: false,
		edit_removeBtn: false,
		dragMove: true,*/
		isSimpleData: true,		
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "?oper=loadWhereExprGroup&id=<%=id%>",
		callback:{
			click: clickWhereExprTree
		},
		asyncParam: ["name", "id"]
	};

	var whereNodes=[];
	jQuery("#exprGroup").zTree(whereTreeSetting, whereNodes);
} );


</script>