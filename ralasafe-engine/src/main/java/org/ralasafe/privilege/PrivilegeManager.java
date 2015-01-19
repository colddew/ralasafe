/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.util.Collection;
import java.util.Locale;

import org.ralasafe.EntityExistException;

public interface PrivilegeManager {

	public abstract Privilege addPrivilege(Privilege pvlg)
			throws EntityExistException;

	public abstract void updatePrivilege(Privilege pvlg)
			throws EntityExistException;

	public abstract void deletePrivilege(int id);

	public abstract Privilege getPrivilege(int id);

	public abstract Collection getLikelyPrivileges(String name);
	public abstract Collection getLikelyPrivilegesByUrl(String url);
	
	public abstract Collection getAllBusinessPrivileges();

	public abstract Collection getAllNonRolePrivileges();

	/**
	 * Move privilge to target, and new position is newOrderNum.
	 * Nodes before newOrderNum keep their position, while nodes after newOrderNum should move backword(orderNum++)
	 * 
	 * @param privilege
	 * @param target
	 * @param newOrderNum
	 */
	public abstract void movePrivilege(Privilege privilege, Privilege target,
			int newOrderNum);

	public abstract void deletePrivilegeCascade(int id);

	public abstract Privilege getParent(Privilege privilege);

	public abstract Collection getChildren(Privilege privilege);

	public abstract boolean isChild(int pId, int id);

	public abstract boolean isCascadeChild(int pId, int id);

	public abstract Privilege getBusinessPrivilegeTree();

	public abstract Privilege getNonRolePrivilegeTree();

	public abstract Privilege getTree(int id);

	public abstract void addReservedPrivilege(Locale locale);
}