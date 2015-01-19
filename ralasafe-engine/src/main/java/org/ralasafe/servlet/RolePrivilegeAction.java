/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.privilege.Role;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class RolePrivilegeAction extends Action {
	private static final Log log=LogFactory.getLog( RolePrivilegeAction.class );
	
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter("oper");
		int roleId=WebUtil.getIntParameter( req, "roleId", -23 );
		String rawPvlgIds=req.getParameter( "pvlgIds" );
		String[] assignPvlgIds=StringUtil.split( rawPvlgIds, "," );
		Collection pvlgIds=Util.convert2IntegerCollection( assignPvlgIds );
		pvlgIds.remove( new Integer(0) ); // remove root node
		
		// log
		if( log.isDebugEnabled() ) {
			log.debug( "oper="+oper+" ,roleId="+roleId+", assignPrivilegeIds="+rawPvlgIds );
		}
		
		RoleManager roleMng=WebUtil.getRoleManager( req );

		roleMng.assignPrivileges( roleId, pvlgIds );
	}

	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		int roleId=WebUtil.getIntParameter( req, "roleId", -23 );
		String oper=req.getParameter("oper");
		// log
		if( log.isDebugEnabled() ) {
			log.debug( "oper="+oper+" ,roleId="+roleId );
		}
		
		RoleManager roleMng=WebUtil.getRoleManager( req );
		Role role=roleMng.getRole( roleId );
		
		Collection assignedPvlgs=roleMng.getPrivileges( roleId );
		
		req.setAttribute( "role", role );
		req.setAttribute( "assignedPvlgs", assignedPvlgs );
		
		WebUtil.forward( req, resp, "/ralasafe/privilege/rolePrivilege.jsp" );
	}
}
