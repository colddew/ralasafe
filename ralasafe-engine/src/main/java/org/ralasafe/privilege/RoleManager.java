/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.util.Collection;
import java.util.Locale;

import org.ralasafe.EntityExistException;

public interface RoleManager {

	public abstract Role addRole(Role role) throws EntityExistException;

	public abstract void updateRole(Role role) throws EntityExistException;

	public abstract void deleteRole(int id);

	public abstract Role getRole(int id);

	public abstract Collection getLikelyRoles(String name);

	public abstract Collection getAllRoles();

	public abstract void assignPrivileges(int roleId, Collection pvlgIds);

	public abstract Collection getPrivileges(int roleId);

	public abstract boolean hasPrivilege(int roleId, int pvlgId);

	public abstract void deleteRolePrivilegeByPrivilege(int pvlgId);

	public abstract void addReservedRole(Locale locale);
}
