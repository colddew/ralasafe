<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>
    
<label><%=i18n.say( "Select_user" )%></label>
<input type="text" name="userName" id="userName" class="required"/>
<input type="hidden" name="userId" id="userId"/> 
<input type="button" value="<%=i18n.say( "Select" )%>" onclick="javascript:showSelectUserPanel()"/>

<div id="selectUserDialog" title="Select user">
</div>

<script language="javascript">
jQuery( "#userName" ).focus(function(){
	  //jQuery(this).blur();
	  showSelectUserPanel();
});

jQuery( "#selectUserDialog" ).dialog({ 
	autoOpen: false, 
	modal: true,
	width: 'auto',
	position: 'middle',
	buttons: {
		"OK": function() {
			userSelected();
		}
	}
});
	

function showSelectUserPanel() {
	jQuery( "#selectUserDialog" ).load( '<%=request.getContextPath()%>/ralasafe/user/userMng.rls?oper=showUserWidget',
				function() {
		//jQuery( "#selectUserDialog" ).dialog( "option", "position", 'middle');
		jQuery( "#selectUserDialog" ).dialog( 'open' );
	} );
}

function userSelected() {
	if( jQuery( "#selectUserDialog :input[name=selectUserId][checked]" ).length>0 ) {
		jQuery("#userId").val(widgetSelectUserId);
		jQuery("#userName").val(widgetSelectUserName);
		jQuery( "#selectUserDialog" ).dialog( 'close' );
	} else {
		alert( "Please select a user" );
	}
}




</script>