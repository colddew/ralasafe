<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.ralasafe.util.I18N"%>
<%
I18N i18n=I18N.getWebInstance( request );
%>    
<div class="header">
	<div class="subTitle">
> <%=i18n.say( "ralasafe_title" ) %>
	</div>
	
	<div class="prodTitle">
		<p>
		<b><%=i18n.say( "ralasafe_vision" ) %></b>
		</p>
	</div>
</div>