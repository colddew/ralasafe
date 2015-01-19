/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.group.Node;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;

public class NonRolePrivilegeTreeHandler extends PrivilegeTreeHandler{
	
	public NonRolePrivilegeTreeHandler(PrivilegeManager manager) {
		super(manager); 
	}
 
	public Node getNode(HttpServletRequest req){ 
		Privilege priv=(Privilege)super.getNode(req);
		priv.setType(1);
		return priv;
	}
	
	public Collection getAllNodes(){
		Collection pvlgs=manager.getAllNonRolePrivileges();
		List allPvlgs=new ArrayList(pvlgs.size()+1);
		allPvlgs.addAll( pvlgs );
		return allPvlgs;
	}
	
	 
	
}
