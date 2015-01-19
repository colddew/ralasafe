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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

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

QueryDesignHandler handler=(QueryDesignHandler) request.getAttribute( "handler" );
QueryType query=handler.getQuery();

String id=request.getParameter( "id" );

String[] mainMenuHrefs=new String[]{
		"?oper=return&id="+id
};
String[] mainMenuTexts=new String[] {
		i18n.say( "Query" )
};
String[] subMenuHrefs=new String[]{
		"javascript:savePolicy();",
		"./queryTest.rls?id="+id,
		"./queryRaw.rls?id="+id
};
String[] subMenuTexts=new String[]{
		i18n.say( "Save" ),
		i18n.say( "Test" ),
		i18n.say( "Goto_manual_mode" )
};

request.setAttribute( "mainMenuHrefs", mainMenuHrefs );
request.setAttribute( "mainMenuTexts", mainMenuTexts );
request.setAttribute( "subMenuHrefs", subMenuHrefs );
request.setAttribute( "subMenuTexts", subMenuTexts );
%>
<title><%=i18n.say( "Design_Query" ) %> -- <%=query.getName() %> </title>
</head>
<body>
<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>


<div id="tabs">
	<ul>
		<li><a href="?oper=designTables&id=<%=id%>" title="select-tab">Select</a></li>
		<li><a href="?oper=designWhere&id=<%=id%>" title="where-tab">Where</a></li>
		<li><a href="?oper=designOrder&id=<%=id%>" title="order-tab">Order</a></li>
		<li><a href="?oper=designGroup&id=<%=id%>" title="group-tab">Group</a></li>
	</ul>
</div>

<div id="editColumnDiglog" title="Edit Mapping Column">
	<form id="tableColumnForm" method="post" action="?oper=updateTableColumn&id=<%=id %>">
	</form>
</div>

<div id="editGroupDiglog" title="Edit Group Column">
	<form id="groupColumnForm" method="post" action="?id=<%=id %>">
	</form>
</div>

<div id="editOrderDiglog" title="Edit Order Column">
	<form id="orderColumnForm" method="post" action="?id=<%=id %>">
	</form>
</div>

<div id="editWhereExprDialog">
	<form id="whereExprForm" method="post" action="?id=<%=id%>">
	</form>
</div>

<div id="exprGroupTypeDialog">
	<input type="hidden" name="addGrp" value="true" />
	<input type="radio" name="exprGroupType" value="AND" checked="checked"/>AND
	<input type="radio" name="exprGroupType" value="OR"/>OR
</div>

<jsp:include page="../footer.jsp"></jsp:include>
<script>
// does this query changed?
var changed;
var currentTableAlias;

jQuery( document ).ready( function() {
	jQuery.ajaxSetup({cache:false});
	
	jQuery( "#tabs" ).tabs({
		
		ajaxOptions: {
			error: function( xhr, status, index, anchor ) {
				$( anchor.hash ).html(
					"Couldn't load this tab. Contact administrator please. " );
			}
		}
	});

	// dialog
	jQuery( "#editColumnDiglog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#tableColumnForm').submit();
			}
		}
	});

	var tableColumnOptions = { 
		beforeSubmit:  checkTableColumnForm,
		 success:       afterUpdateTableColumn 
	}; 
	    
	jQuery('#tableColumnForm').submit(function() { 
	     jQuery(this).ajaxSubmit(tableColumnOptions); 
	 	 return false; 
	}); 

	// dialog
	jQuery( "#editGroupDiglog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#groupColumnForm').submit();
			}
		}
	});

	var groupColumnOptions = { 
			beforeSubmit:  checkGroupColumnForm,
			success:       afterUpdateGroupColumn 
		}; 
	
	jQuery('#groupColumnForm').submit(function() { 
	     jQuery(this).ajaxSubmit(groupColumnOptions); 
	 	 return false; 
	}); 

	// dialog
	jQuery( "#editOrderDiglog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#orderColumnForm').submit();
			}
		}
	});
	
	var orderColumnOptions = { 
			beforeSubmit:  checkOrderColumnForm,
			success:       afterUpdateOrderColumn 
		}; 
	
	jQuery('#orderColumnForm').submit(function() { 
	     jQuery(this).ajaxSubmit(orderColumnOptions); 
	 	 return false; 
	}); 

	var whereExprFormOptions = { 
		//beforeSubmit:  checkPropertyForm,
		success:       doAfterEditWhereExpr 
	}; 
	    
	jQuery('#whereExprForm').submit(function() { 
	     jQuery(this).ajaxSubmit(whereExprFormOptions); 
	 	 return false; 
	});
		
	// dialog
	jQuery( "#editWhereExprDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				jQuery('#whereExprForm').submit();
			}
		}
	});

	jQuery( "#exprGroupTypeDialog" ).dialog({ 
		autoOpen: false,
		modal: true,
		hide: "fadeOut",
		position: 'middle',
		buttons: {
			"OK": function() {
				doAfterEditWhereExprGroup();
			}
		}
	});
	
});

//------------ select tab javascripts ------------
function checkTableColumns( tableAlias ) {
	jQuery.ajax({
	    url: '?oper=checkTableColumns',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias},
	    error: function(){
	        alert('Error check table columns');
	    },
	    success: function(xml){
	    	// load div content
			jQuery ( "#t__"+tableAlias ).load( "?oper=viewTable&id=<%=id%>&tableAlias="+tableAlias );
		    //jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});
}

function unCheckTableColumns( tableAlias ) {
	jQuery.ajax({
	    url: '?oper=unCheckTableColumns',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias},
	    error: function(){
	        alert('Error uncheck table columns');
	    },
	    success: function(xml){
	    	// load div content
			jQuery ( "#t__"+tableAlias ).load( "?oper=viewTable&id=<%=id%>&tableAlias="+tableAlias );
	    	//jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});
}

function deleteTable( tableAlias ) {
	jQuery.ajax({
	    url: '?oper=deleteTable',
	    type: 'POST',
	    data: {"id":"<%=id%>","tableAlias":tableAlias},
	    error: function(){
	        alert('Error delete table');
	    },
	    success: function(xml){
	    	jQuery( "#t__"+tableAlias ).remove();
	    }
	});
}

function addTable( schema, tableName ) {
	jQuery.ajax({
	    url: '?oper=addTable',
	    type: 'POST',
	    data: {"id":"<%=id%>","schema":schema,"tableName":tableName},
	    dataType: "json",
	    error: function(){
	        alert('Error add table');
	    },
	    success: function(data){
			var alias=data;

			// append div
			jQuery( "#tables" ).append( "<div id='t__"+alias+"'></div>" );
			// load div content
			jQuery ( "#t__"+alias ).load( "?oper=viewTable&id=<%=id%>&tableAlias="+alias );
	    }
	});
}

function setMappingClass() {
	var mappingClass=jQuery( "#mappingClass" ).attr( "value" );

	jQuery.ajax({
	    url: '?oper=setMappingClass',
	    type: 'POST',
	    data: {"id":"<%=id%>","mappingClass":mappingClass},
	    error: function(){
	        alert('Error mapping class');
	    },
	    success: function(xml){
	    	jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});
}

function checkTableColumn( tableAlias, columnName ) {
	var checked=jQuery( "#"+tableAlias+"__"+columnName ).attr( "checked" );

	var url="?oper=deleteColumn";
	if( checked ) {
		url="?oper=addColumn";
	} 

	jQuery.ajax({
	    url: url,
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias,"columnName":columnName},
	    error: function(){
	        alert('Error (un)check table column');
	    },
	    success: function(xml){
	    	jQuery ( "#t__"+tableAlias ).load( "?oper=viewTable&id=<%=id%>&tableAlias="+tableAlias );
	    	//jQuery( "#select-tab" ).load( "?oper=designTables&id=<%=id%>" );
	    }
	});	
}

function editTableColumn( tableAlias, columnName ) {
	jQuery( "#tableColumnForm" ).load( "?oper=getColumn&id=<%=id%>&tableAlias="+tableAlias+"&columnName="+columnName,
			function() {
		jQuery( "#editColumnDiglog" ).dialog( "open" );
	});
}

function checkTableColumnForm(formData, jqForm, options) {
	currentTableAlias = jQuery('#tableColumnForm :input[@name=tableAlias]').fieldValue()[0];
}

function afterUpdateTableColumn() {
	jQuery( "#editColumnDiglog" ).dialog( "close" );
	jQuery ( "#t__"+currentTableAlias ).load( "?oper=viewTable&id=<%=id%>&tableAlias="+currentTableAlias );
}

//------------ group tab javascripts ------------
function checkGroupColumnForm(formData, jqForm, options) {	
	/**alert( jQuery('#groupColumnForm :input[name=tableAlias]').val() );
	var aliasColumn = jQuery('#aliasColumn').val();
	var firstIndex=aliasColumn.indexOf( "[" );
	var lastIndex=aliasColumn.lastIndexOf( "]" );

	var tableAlias=aliasColumn.substring( firstIndex+1,lastIndex );
	var columnName=aliasColumn.substring( lastIndex+2 );

	jQuery('#groupColumnForm :input[name=tableAlias]').val( tableAlias );
	jQuery('#groupColumnForm :input[name=columnName]').val( columnName );*/
}

function afterUpdateGroupColumn() {
	jQuery( "#editGroupDiglog" ).dialog( "close" );
	jQuery( "#group-tab" ).load( "?oper=designGroup&id=<%=id%>" );
}

function moveGroupColumn( direct, tableAlias, columnName ) {
	jQuery.ajax({
	    url: '?oper=moveGroupColumn',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias,"columnName":columnName,"direct":direct},
	    error: function(){
	        alert('Error move group column');
	    },
	    success: function(xml){
	    	jQuery( "#group-tab" ).load( "?oper=designGroup&id=<%=id%>" );
	    }
	});	
}

function deleteGroupColumn( tableAlias, columnName ) {
	jQuery.ajax({
	    url: '?oper=deleteGroupColumn',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias,"columnName":columnName},
	    error: function(){
	        alert('Error delete group column');
	    },
	    success: function(xml){
	    	jQuery( "#group-tab" ).load( "?oper=designGroup&id=<%=id%>" );
	    }
	});	
}

function editGroupColumn( index, tableAlias, columnName ) {
	jQuery( "#editGroupDiglog" ).attr( "title", "Edit group column" );
	jQuery( "#groupColumnForm" ).load( "?oper=getGroupColumn&id=<%=id%>&tableAlias="+tableAlias+"&columnName="+columnName+"&index="+index,
			function() {
		jQuery( "#editGroupDiglog" ).dialog( "open" );
	});
}

function addGroupColumn( tableAlias, columnName ) {
	jQuery( "#editGroupDiglog" ).attr( "title", "Add group column" );
	jQuery( "#groupColumnForm" ).load( "?oper=getGroupColumn&id=<%=id%>",
			function() {
		jQuery( "#editGroupDiglog" ).dialog( "open" );
	});
}

//------------ order tab javascripts ------------
function checkOrderColumnForm(formData, jqForm, options) {	
}

function afterUpdateOrderColumn() {
	jQuery( "#editOrderDiglog" ).dialog( "close" );
	jQuery( "#order-tab" ).load( "?oper=designOrder&id=<%=id%>" );
}

function moveOrderColumn( direct, tableAlias, columnName ) {
	jQuery.ajax({
	    url: '?oper=moveOrderColumn',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias,"columnName":columnName,"direct":direct},
	    error: function(){
	        alert('Error move order column');
	    },
	    success: function(xml){
	    	jQuery( "#order-tab" ).load( "?oper=designOrder&id=<%=id%>" );
	    }
	});	
}

function deleteOrderColumn( tableAlias, columnName ) {
	jQuery.ajax({
	    url: '?oper=deleteOrderColumn',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","tableAlias":tableAlias,"columnName":columnName},
	    error: function(){
	        alert('Error delete order column');
	    },
	    success: function(xml){
	    	jQuery( "#order-tab" ).load( "?oper=designOrder&id=<%=id%>" );
	    }
	});	
}

function editOrderColumn( index, tableAlias, columnName ) {
	jQuery( "#editOrderDiglog" ).attr( "title", "Edit order column" );
	jQuery( "#orderColumnForm" ).load( "?oper=getOrderColumn&id=<%=id%>&tableAlias="+tableAlias+"&columnName="+columnName+"&index="+index );
	jQuery( "#editOrderDiglog" ).dialog( "open" );
}

function addOrderColumn( tableAlias, columnName ) {
	jQuery( "#editOrderDiglog" ).attr( "title", "Add order column" );
	jQuery( "#orderColumnForm" ).load( "?oper=getOrderColumn&id=<%=id%>",
			function() {
		jQuery( "#editOrderDiglog" ).dialog( "open" );
	});
}

//------------ where tab javascripts ------------
function clickWhereExprTree( event, treeId, treeNode ) {
	var nodeId=treeNode.id;

	// clear context menu
	jQuery( "#whereExprContextMenu" ).html();

	// load dynamic context menu
	jQuery( "#whereExprContextMenu" ).load( "?oper=whereExprContextMenu&id=<%=id%>&nodeId="+nodeId ); 
}

function doAfterEditWhereExpr() {
	jQuery( "#editWhereExprDialog" ).dialog( "close" );
	jQuery( "#where-tab" ).load( "?oper=designWhere&id=<%=id%>" );
}

var whereExprNodeId;
var whereExprDirect;
function addWhereChildExprGroup( nodeId ) {
	whereExprNodeId=nodeId;
	
	jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val("true");
	jQuery( "#exprGroupTypeDialog" ).dialog( "open" );	
}

function doAfterEditWhereExprGroup() {
	jQuery( "#exprGroupTypeDialog" ).dialog( "close" );

	var type=jQuery( "#exprGroupTypeDialog :input[name=exprGroupType][checked]" ).val();
	var addGrp=jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val();

	if( "true"==addGrp ) {
		jQuery.ajax({
		    url: '?oper=addWhereChildExprGroup',
		    type: 'POST',
		    async: false,
		    data: {"id":"<%=id%>","nodeId":whereExprNodeId,"type":type},
		    error: function(){
		        alert('Error add expression group');
		    },
		    success: function(xml){
		    	jQuery( "#where-tab" ).load( "?oper=designWhere&id=<%=id%>" );
		    }
		});	
	} else {
		jQuery.ajax({
		    url: '?oper=editWhereExprGroup',
		    type: 'POST',
		    async: false,
		    data: {"id":"<%=id%>","nodeId":whereExprNodeId,"type":type},
		    error: function(){
		        alert('Error update expression group');
		    },
		    success: function(xml){
		    	jQuery( "#where-tab" ).load( "?oper=designWhere&id=<%=id%>" );
		    }
		});	
	}
}

function deleteWhereExpr( nodeId ) {
	jQuery.ajax({
	    url: '?oper=deleteWhereExpr',
	    type: 'POST',
	    async: false,
	    data: {"id":"<%=id%>","nodeId":nodeId},
	    error: function(){
	        alert('Error delete expression');
	    },
	    success: function(xml){
	    	jQuery( "#where-tab" ).load( "?oper=designWhere&id=<%=id%>" );
	    }
	});	
}

function editWhereExprGroupType( nodeId, linkedType ) {
	whereExprNodeId=nodeId;
	
	jQuery( "#exprGroupTypeDialog :input[name=exprGroupType][value="+linkedType+"]" ).attr( "checked", linkedType );
	jQuery( "#exprGroupTypeDialog :input[name=addGrp]" ).val("false");
	
	jQuery( "#exprGroupTypeDialog" ).dialog( "open" );
}

function addWhereChildExpr( nodeId, exprType ) {
	jQuery( "#whereExprForm" ).load( "?oper=loadWhereExprForm&id=<%=id%>&exprType="+exprType+"&pId="+nodeId,
			function() {
		jQuery( "#editWhereExprDialog" ).dialog( "open" );
	});
}

function editWhereExpr( nodeId ) {
	jQuery( "#whereExprForm" ).load( "?oper=loadWhereExprForm&id=<%=id%>&nodeId="+nodeId,
			function() {
		jQuery( "#editWhereExprDialog" ).dialog( "open" );
	});
}

function showOperand( index, type ) {
	// hide all
	jQuery( "#whereExprDiv_"+index+"_column" ).hide();
	jQuery( "#whereExprDiv_"+index+"_contextValue" ).hide();
	jQuery( "#whereExprDiv_"+index+"_userValue" ).hide();
	jQuery( "#whereExprDiv_"+index+"_simpleValue" ).hide();

	// show special type div
	jQuery( "#whereExprDiv_"+index+"_"+type ).show();

	// set hidden operand value
	jQuery( "#whereExprForm :input[name=operand"+index+"Type]" ).val(type);
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
	    	alert( "<%=i18n.say( "saved" )%>" );
	    }
	});
}
</script>
</body>
</html>