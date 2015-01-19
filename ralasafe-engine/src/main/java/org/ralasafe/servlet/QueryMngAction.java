/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.entitle.QueryManager;

public class QueryMngAction  extends AbstractTreeAction{
	public AbstractTreeHandler createTreeHandler(HttpServletRequest req){
		QueryManager  manager=WebUtil.getQueryManager(req);
		return new QueryTreeHandler(manager);
	}
	
	
}
