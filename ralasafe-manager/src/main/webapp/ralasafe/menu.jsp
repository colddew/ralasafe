<%
/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
%>
<%
String[] _mainMenuHrefs=(String[]) request.getAttribute( "mainMenuHrefs" );
String[] _mainMenuTexts=(String[]) request.getAttribute( "mainMenuTexts" );
String[] _subMenuHrefs=(String[]) request.getAttribute( "subMenuHrefs" );
String[] _subMenuTexts=(String[]) request.getAttribute( "subMenuTexts" );
%>
<div class="menu">
	<img src="<%=request.getContextPath()%>/ralasafe/images/navmenu.gif" title="Quick link" alt="Quick link"/>
	<a href="<%=request.getContextPath()%>/ralasafe/designer.rls"><%=i18n.say( "Ralasafe_Console_Page" )%></a>
	<%
	if( _mainMenuHrefs!=null ) {
		for( int _iii=0; _iii<_mainMenuHrefs.length; _iii++ ) {
			String _menuUrl=_mainMenuHrefs[_iii];
			if( _menuUrl.startsWith( "/" ) ) {
				_menuUrl=request.getContextPath()+_menuUrl;
			}
		%>
	&nbsp;&gt;&gt;
	<a href="<%=_menuUrl%>"><%=_mainMenuTexts[_iii] %></a>			
	<%	}
	}
	%>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%
	if( _subMenuHrefs!=null ) {
		for( int _iii=0; _iii<_subMenuHrefs.length; _iii++ ) {
			String _menuUrl=_subMenuHrefs[_iii];
			if( _menuUrl.startsWith( "/" ) ) {
				_menuUrl=request.getContextPath()+_menuUrl;
			}
		%>
	&nbsp;&nbsp;
	<a href="<%=_menuUrl%>"><%=_subMenuTexts[_iii] %></a>			
	<%	}
	}
	%>
</div>