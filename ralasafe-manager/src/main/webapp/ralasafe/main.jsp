<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.servlet.DesignerAction"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta stylename="safe ring" />
<meta designer="leegorous" />
<link rel="shortcut icon" href="favicon.ico"> 
<link rel="stylesheet" type="text/css" media="screen" href="./css/ralasafe.css" />
<link rel="stylesheet" href="./css/960gs.css" />
<link rel="stylesheet" href="./css/default.css" />
<%
org.ralasafe.util.I18N i18n=org.ralasafe.util.I18N.getWebInstance( request );
%>
<!--[if IE]>
<script src="./js/html5.js"></script>
<![endif]-->
<script src="./js/jquery.js"></script>
<title><%=i18n.say( "Ralasafe_Console" )%></title>
</head>
<body>
<div class="page container_12">

    <div id="content"  class="container_12 clearfix">
        <div class="block-tl">
            <div class="head"><%=i18n.say( "Assign_Personnel" )%></div>
            <div class="body">
                <a href="user/userMng.rls"><%=i18n.say( "Assign_roles_to_user" )%></a>
            </div>
        </div>
        <div class="block-tr">
            <div class="head"><%=i18n.say( "Role_Builder" ) %></div>
            <div class="body">
                <a href="privilege/roleMng.rls"><%=i18n.say( "Role" )%></a>
            </div>
        </div>
        <div class="block-bl">
            <div class="head"><%=i18n.say( "Infrastructure_Builder" ) %></div>
            <div class="body">
                <a href="userTypeMng.rls"><%=i18n.say( "User_metadata" )%></a>
				<a href="privilege/privilegeMng.rls"><%=i18n.say( "Privilege" )%></a>
				<a href="privilege/nonRolePrivilegeMng.rls"><%=i18n.say( "Non_role_privilege" )%></a>
				<a href="privilege/privilegeExport.rls"><%=i18n.say( "Export_privilege_constants" )%></a>
            </div>
        </div>
        <div class="block-br">
            <div class="head"><%=i18n.say( "Policy_Developer" ) %></div>
            <div class="body">
                <a href="userCategory/userCategoryMng.rls"><%=i18n.say( "User_category" )%></a>
				<a href="businessData/businessDataMng.rls"><%=i18n.say( "Business_data_category" )%></a>
				<a href="query/queryMng.rls"><%=i18n.say( "Query" )%></a>
            </div>
        </div>
		<div class="banner">
            <span class="point"><%=i18n.say( "ralasafe_vision1" )%></span>
            <span class="point"><%=i18n.say( "ralasafe_vision2" )%></span>
            <span class="point"><%=i18n.say( "ralasafe_vision3" )%></span>
        </div>
    </div>

    <div id="footer"  class="container_12 clearfix">
		<a href="http://www.ralasafe.org">The Ralasafe Project</a><br/>
		Copyright ©2004-2011 <a href="license.txt">License</a>
    </div>
</div>
<script>
$(document).ready(function() {
	var ps = $('.point').remove();
	var i = 0;
	function showPoint() {
		var p = $(ps[i]);console.log(i);
		i++;
		if (i > 2) i = 0;
		// p.css('opacity', 0);
		$('.banner').append(p);
		p.fadeIn(300, function() {
			setTimeout(function() {
				p.fadeOut(300, function() {
					p.remove();
					setTimeout(showPoint, 1 * 500);
				});
			}, 10 * 500);
		});
	}
	
	showPoint();
});
</script>

</body>
</html>