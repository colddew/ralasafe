/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.privilege.Role;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;
import org.ralasafe.user.UserManager;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class UserRoleAction extends Action {
	private static final Log log=LogFactory.getLog( UserRoleAction.class );
	
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String strUserId=req.getParameter( "userId" );
		String[] assignRoleIds=req.getParameterValues( "roleId" );
		Collection roleIds=Util.convert2IntegerCollection( assignRoleIds );
		
		Object userId=WebUtil.convertUserId( req, strUserId );
		
		// log
		if( log.isDebugEnabled() ) {
			log.debug( "userId="+strUserId+", assignRoleIds="+assignRoleIds );
		}
		
		UserRoleManager userRoleManager=WebUtil.getUserRoleManager( req );
		userRoleManager.assignRoles( userId, roleIds );
	}

	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String strUserId=req.getParameter( "userId" );
		Object userId=WebUtil.convertUserId( req, strUserId );
		
		UserManager userManager=WebUtil.getUserManager( req );
		User user=new User();
		user.setId( userId );
		user=userManager.selectById( user );
		
		UserRoleManager userRoleManager=WebUtil.getUserRoleManager( req );
		Collection assignedRoles=userRoleManager.getRoles( userId );
		
		Set assignedRoleIds=new HashSet();
		for( Iterator iter=assignedRoles.iterator(); iter.hasNext(); ) {
			Role role=(Role) iter.next();
			assignedRoleIds.add( new Integer(role.getId()) );
		}
		
		// search key
		String searchName=req.getParameter( "name" );
/*		if(searchName!=null){
			searchName=new String(searchName.getBytes("ISO-8859-1"),"UTF-8");		        
		}*/
		
		RoleManager roleMng=WebUtil.getRoleManager( req );
		Collection roles=null;
		if( StringUtil.isEmpty( searchName ) ) {
			roles=roleMng.getAllRoles();
		} else {
			roles=roleMng.getLikelyRoles( searchName );
		}
		
		req.setAttribute( "roles", roles );
		req.setAttribute( "assignedRoleIds", assignedRoleIds );
		req.setAttribute( "user", user );
		
		WebUtil.forward( req, resp, "/ralasafe/user/userRoles.jsp" );
	}
}
