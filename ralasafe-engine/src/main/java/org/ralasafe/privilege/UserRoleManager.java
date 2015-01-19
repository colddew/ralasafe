/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.util.Collection;

public interface UserRoleManager {
 
	public abstract void assignRoles(Object userId, Collection roleIds);
	public abstract void assignRoles(Collection userIds, Collection roleIds);

	/**
	 * return Collection< Role >
	 * @param userId
	 * @return
	 */
	public abstract Collection getRoles(Object userId);
	
	/**
	 * delete user's roles
	 * @param userId
	 */
	public abstract void deleteUserRoles(Object userId);
	
	public abstract boolean hasPrivilege(Object userId, int pvlgId);
	
	/**
	 * when delete a role, delete related user-role records 
	 * @param roleId
	 */
	public void deleteUserRoles(int roleId);
	
	/**
	 * Get current user's menu
	 * @param userId
	 * @return
	 */
	public Privilege getBusinessPrivilegeTree(Object userId);
}
 
