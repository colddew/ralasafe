<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="java.util.Collection,java.util.Iterator"%>
<%@page import="org.ralasafe.userType.UserType"%>
<%@page import="org.ralasafe.util.I18N"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%
I18N i18n=I18N.getWebInstance( request );
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="shortcut icon" href="./favicon.ico"> 
<link type="text/css" rel="stylesheet" href="./css/syntaxhighlighter/shCoreDefault.css"/> 
<link rel="stylesheet" type="text/css" media="screen" href="./css/ralasafe.css" />

<script src="./js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="./js/syntaxhighlighter/shCore.js"></script> 
<script type="text/javascript" src="./js/syntaxhighlighter/shBrushXml.js"></script> 

<title><%=i18n.say( "User_Metadata_Management" )%></title>
</head>
<body>
<script language="javascript">
	
<%
Collection userTypes = (Collection) request
					.getAttribute("userTypes");
			StringBuffer buff = new StringBuffer();
			int i = 0;
			for (Iterator iter = userTypes.iterator(); iter.hasNext();) {
				if (i != 0) {
					buff.append(",");
				}
				i++;
				UserType userType = (UserType) iter.next();
				buff.append("\"").append(userType.getName()).append("\"");
			}%>
	function submitUserTypeForm() {
		var names = new Array(
<%=buff.toString()%>
	);

		var oForm = document.getElementById("userTypeForm");
		var oName = oForm.name.value;
		var oDesc = oForm.desc.value;
		var oFile = oForm.userDefineFile.value;

		if (oName == null || oName.length == 0) {
			alert("Please input name");
			return false;
		}
		if (oDesc == null || oDesc.length == 0) {
			alert("Please input description");
			return false;
		}
		if (oFile == null || oFile.length == 0) {
			alert("Please select a definition file");
			return false;
		}
		for ( var i = 0; i < names.length; i++) {
			if (names[i] == oName) {
				oForm.op.value = "update";
				var oConfirm = window.confirm("Are you sure to update this user metadata definition?");
				if (oConfirm) {
					//oForm.submit();
					return true;
				} else {
					return false;
				}
			}
		}

		oForm.op.value = "add";
		//oForm.submit();
		return true;
	}

	function ensureDelete( url ) {
		var toDelete=window.confirm( "Are you sure to delete this user metadata? (Related sql tables of ralasafe will be deleted!!!" );
		if( toDelete ) {
			window.location=url;
		} 
	}
</script>

<jsp:include page="../header.jsp"></jsp:include>
<%@include file="../menu.jsp" %>


<div class="smallContainer">
<form action="userTypeInstall.rls" method="post" id="userTypeForm"
	ENCTYPE="multipart/form-data" onsubmit="return submitUserTypeForm()"><input type="hidden" name="op"
	value="add" />
	<INPUT TYPE="hidden" NAME="name" value="ralasafe"/>
	<label><%=i18n.say( "Description" )%></label>
	<INPUT TYPE="text" NAME="desc"/>
	<label><%=i18n.say( "File" )%></label>
	<INPUT TYPE="file" NAME="userDefineFile"/>
	<br/>
	<INPUT TYPE="submit" value="Submit"/>
</form>

<p>
<a href="javascript:toggleExample()"><%=i18n.say( "User_metadata_example" )%></a>
<div id="usermetadataExample" style="display:none">
<pre class="brush:xml">&lt;?xml version="1.0"?>
&lt;user>
	&lt;table ds="app" name="mainTable" sqlName="UserView"
		uniqueFields="loginName">
		&lt;field name="id" columnName="id" sqlType="int" javaType="java.lang.Integer" />
		&lt;field name="name" columnName="name" sqlType="varchar(40)"
			javaType="java.lang.String" displayName="Name" show="true" />
		&lt;field name="companyName" columnName="companyName" sqlType="varchar(100)"
			javaType="java.lang.String" displayName="Company" show="true" />
		&lt;field name="loginName" columnName="loginName" sqlType="varchar(40)"
			javaType="java.lang.String" />
		&lt;field name="password" columnName="password" sqlType="varchar(40)"
			javaType="java.lang.String" />
		&lt;field name="isManager" columnName="isManager" sqlType="int"
			javaType="java.lang.Boolean" />
		&lt;field name="companyId" columnName="companyId" sqlType="int"
			javaType="java.lang.Integer" />
		&lt;field name="departmentId" columnName="departmentId" sqlType="int"
			javaType="java.lang.Integer" />
		&lt;field name="companyLevel" columnName="companyLevel" sqlType="int"
			javaType="java.lang.Integer" />
	&lt;/table>
&lt;/user>

</pre>
</div>
</p>

<h1><%=i18n.say( "User_metadata_list" )%></h1>
<table class="ralaTable">
	<tr bgcolor="#F4F4F4">
		<!--th>Name</th-->
		<th><%=i18n.say( "Description" )%></th>
		<th><%=i18n.say( "Action" )%></th>
	</tr>
	<%
		for (Iterator iter = userTypes.iterator(); iter.hasNext();) {
			UserType userType = (UserType) iter.next();
	%>
	<tr bgcolor="#F4F4F4">
		<!--td><%=userType.getName()%></td-->
		<td><%=userType.getDesc()%></td>
		<td><a href="?op=view&name=<%=userType.getName()%>"><%=i18n.say( "Detail" )%></a>
		<a href="javascript:ensureDelete('?op=delete&name=<%=userType.getName()%>');"><%=i18n.say( "Delete" )%></a></td>
	</tr>
	<%
		}
	%>
</table>
</div>

<jsp:include page="../footer.jsp"></jsp:include>

<script>
jQuery( document ).ready( function() {
	SyntaxHighlighter.all();
});

function toggleExample() {
	jQuery( "#usermetadataExample" ).toggle();
}
</script>

</body>
</html>