<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
    
<% if( !searchName.equals("")||totalNumber>15 ) { %>
<form id="searchForm">
<input type="text" name="name" value="<%=searchName %>" />
<input type="submit" value="<%=i18n.say( "search" )%>" />
</form>
<% } %>