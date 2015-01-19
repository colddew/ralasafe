/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.privilege.PrivilegeManager;

public class NonRolePrivilegeMngAction extends AbstractTreeAction{

	 
	public AbstractTreeHandler createTreeHandler(HttpServletRequest req) {
		PrivilegeManager  manager=WebUtil.getPrivilegeManager(req);
		return new NonRolePrivilegeTreeHandler(manager);
	}

}
