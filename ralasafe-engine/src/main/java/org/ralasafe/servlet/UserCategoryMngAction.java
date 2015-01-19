/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.entitle.UserCategoryManager;

public class UserCategoryMngAction extends AbstractTreeAction {
	public AbstractTreeHandler createTreeHandler( HttpServletRequest req ) {
		UserCategoryManager manager=WebUtil.getUserCategoryManager( req );
		
		return new UserCategoryTreeHandler( manager );
	}
}
