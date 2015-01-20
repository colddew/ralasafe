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
<%@page import="org.ralasafe.user.User"%>


<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );

Collection coll=(Collection) request.getAttribute("users");
int totalNumber =((Integer)request.getAttribute( "totalNumber" )).intValue();
int first=((Integer)request.getAttribute( "first" ) ).intValue();
int size=((Integer)request.getAttribute( "size" ) ).intValue();
String searchName=(String)request.getAttribute( "name" );
String[] showUserFields=(String[])request.getAttribute( "showUserFields" );
String[] showUserFieldDisplayNames=(String[])request.getAttribute( "showUserFieldDisplayNames" );

if( searchName==null ) {
	searchName="";
}
%>

<div>

<%@ include file="../common/searchForm.jsp"%>

<label><%=i18n.say( "User_list" ) %></label>
<table>
	<tr>
		<th>Select</th>
		<% for( int i=0; i<showUserFieldDisplayNames.length; i++ ) { %>
		<th><%=showUserFieldDisplayNames[i] %></th>
		<% } %>		
	</tr>
<%
for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
	User user=(User) iter.next();
%>
	<tr>
		<td><input type="radio" name="selectUserId" 
		onclick="javascript:updateWidgetUserValues('<%=user.getId() %>','<%=user.get("name") %>');"/></td>
		<% for( int i=0; i<showUserFields.length; i++ ) { %>
		<td><%=user.get(showUserFields[i]) %></td>
		<% } %>
	</tr>
<% } %>
</table>

<form id="navForm">	
<table>
	<%@ include file="../common/nav.jsp"%>
</table>
</form>

</div>

<script type="text/javascript">
var widgetSelectUserId;
var widgetSelectUserName;

jQuery(document).ready(function(){	
	var navOptions = { 
		beforeSubmit:  calFirst
	}; 
	
	jQuery('#navForm').submit(function() {
		jQuery(this).ajaxSubmit(navOptions);
		return false;
	});

	<% if( !searchName.equals("")||totalNumber>15 ) { %>
	var searchOptions = { 
		beforeSubmit:  beforeSearch
	}; 
	jQuery('#searchForm').submit(function() {
		jQuery(this).ajaxSubmit(searchOptions);
		return false;
	});
	<% } %>
});

function updateWidgetUserValues( _userId, _userName ) {
	widgetSelectUserId=_userId;
	widgetSelectUserName=_userName;
}

function beforeSearch() {
	var name=jQuery( "#searchForm :input[name=name]" ).val();
	jQuery( "#selectUserDialog" ).load( "<%=request.getContextPath()%>/ralasafe/user/userMng.rls?oper=showUserWidget&name="+name );
	return false;
}

function calFirst() {
	var totalNumber=<%=totalNumber%>;
	var size=<%=size%>;
	
	var page2=jQuery( "#navForm :input[name=page]" ).val();
	var page=page2-1;
	if( page*size>=0 && page*size<totalNumber ) {
		jQuery( "#selectUserDialog" ).load( "<%=request.getContextPath()%>/ralasafe/user/userMng.rls?oper=showUserWidget&name=<%=searchName%>&first="+(page*size) );
		return false;
	} else {
		return false;
	}
}
function gotoRecord(first) {
	jQuery( "#selectUserDialog" ).load( '<%=request.getContextPath()%>/ralasafe/user/userMng.rls?oper=showUserWidget&name=<%=searchName%>&first='+first );
	//return false;
}
</script>	
