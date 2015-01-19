/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

public class RolePrivilege {
	private int roleId;
	private int privilegeId;
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getPrivilegeId() {
		return privilegeId;
	}
	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}
	
	public RolePrivilege() {}

	public RolePrivilege(int roleId, int privilegeId) 
	{
		this.roleId = roleId;
		this.privilegeId = privilegeId;
	}
}
