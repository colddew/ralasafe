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
<%@page import="org.ralasafe.db.TableView"%>
<%@page import="org.ralasafe.db.ColumnView"%>
<%@page import="org.ralasafe.db.sql.xml.Column"%>
<%@page import="org.ralasafe.util.StringUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
    
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();
String dsName=query.getDs();
String id=request.getParameter( "id" );
String mappingClass=query.getQueryTypeSequence().getSelect().getMappingClass();
if( mappingClass==null ) {
	mappingClass="";
}
Table[] tables=query.getQueryTypeSequence().getFrom().getTable();
%> 


<div class="treeDiv">
	<label><%=i18n.say( "Data_source" )%></label>
	<select id="appDsName">
	</select>
	<ul id="dbview" class="tree"></ul>
</div>

<div class="editDiv">
	<div id="tables">
	<label><%=i18n.say( "Mapping_class" )%></label>
	<input type="text" id="mappingClass" value="<%=mappingClass%>" size="40" />
	<input type="button" value="OK" onclick="javascript:setMappingClass()"/>
	<br/><br/>
			<%
			for( int i=0; i<tables.length; i++ ) {
				Table table=tables[i];
				String tableAlias=table.getAlias();
				TableView tableView=handler.getTableView( tableAlias );
			%>
		<div id="t__<%=tableAlias%>"></div>
		<br/>
			<% 
			}
			%>		
	</div>
</div>

<div class="clearDiv"></div>

<script type="text/javascript">
jQuery( document ).ready( function() {
	// load data source selector and its tables and views tree
	loadAppDsNames();
	
	jQuery( "#mappingClass" ).keydown(function(event){  
		if(event.keyCode==13){
			setMappingClass(); 
		}  
	});  

	// load tables;
	<%
	for( int i=0; i<tables.length; i++ ) {
		Table table=tables[i];
		String tableAlias=table.getAlias();
		TableView tableView=handler.getTableView( tableAlias );
	%>
	jQuery ( "#t__<%=tableAlias%>" ).load( "?oper=viewTable&id=<%=id%>&tableAlias=<%=tableAlias%>" );
	<% 
	}
	%>	
} );

function loadAppDsNames() {
	jQuery.ajax( {
		url: "../util.rls?oper=getAppDsNames",
		success: function(data) {
			var names=data;
			for( var i=0; i<names.length; i++ ) {
				var name=names[i];
				if( name==queryDsName ) {
					jQuery("#appDsName").append("<option value='"+name+"' selected>"+name+"</option>");
				} else {
					jQuery("#appDsName").append("<option value='"+name+"'>"+name+"</option>");
				}
			}

			<%if( StringUtil.isEmpty(dsName) ) { %>
			var queryDsName=jQuery("#appDsName").val();	
			<% } else { %>
			var queryDsName="<%=dsName%>";
			<% } %>
			
			// load current datasource tables and views
			loadDbView(queryDsName);
		},
		error: function(data) {
			alert( "Error happened while getting application datasource names" );
		}
	} );
	
	// bind onchange event
	jQuery("#appDsName").change(function() { 
		var dsName=jQuery("#appDsName option:selected").val();
		
		loadDbView(dsName);
	});
}

var dbTree;
function loadDbView( dsName) {
	var dbTreeSetting={
		async: true,
		isSimpleData: false,		
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		asyncUrl: "../util.rls?oper=loadDbView&dsName="+dsName,
		callback:{
			dblclick: function(event, treeId, treeNode) {
				var type=treeNode.type;
				
				if( type=='view' || type=='table' ) {
					var schema=treeNode.schema;
					var name=treeNode.name;
		
					addTable( schema, name );
				}
				return;
			},
			asyncSuccess: expandAllDbViews
		},
		asyncParam: ["name", "id"]
	};

	var dbNodes=[];
	dbTree=jQuery("#dbview").zTree(dbTreeSetting, dbNodes);
}

function expandAllDbViews(event, treeId, treeNode, msg) {
	dbTree.expandAll(true);
}
</script>