/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.WebConstants;
import org.ralasafe.privilege.Role;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class RoleMngAction extends Action {
	private static final Log log=LogFactory.getLog( RoleMngAction.class );
	
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		RoleManager roleMng=WebUtil.getRoleManager( req );

		// get value and construct role
		String name=req.getParameter("name");
		String description=req.getParameter("description");
		String oper=req.getParameter("oper");
		int id=WebUtil.getIntParameter( req, "id", 0 );

		Role role=new Role();
		role.setDescription( description );
		role.setName( name );
		role.setId( id );
		
		// log
		if( log.isDebugEnabled() ) {
			log.debug( oper + " to role:" + role );
		}
		
		// operate
		if( "addRole".equals( oper ) ) {
			try {
				roleMng.addRole( role );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
		} else if( "updateRole".equals( oper ) ) {
			try {
				roleMng.updateRole( role );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
		} else if( "deleteRole".equals( oper ) ) {
			roleMng.deleteRole( role.getId() );
		}
	}

	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		RoleManager roleMng=WebUtil.getRoleManager( req );
		
		String oper=req.getParameter("oper");
		// log
		if( log.isDebugEnabled() ) {
			log.debug( "oper="+oper );
		}
		
		if( "addRole".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/privilege/editRole.jsp" );
		} else if( "getRole".equals( oper ) ) {
			int id=WebUtil.getIntParameter( req, "id", 0 );
			Role role=roleMng.getRole( id );
			req.setAttribute( "role", role );			
			WebUtil.forward( req, resp, "/ralasafe/privilege/editRole.jsp" );
		} else if( "isNameValid".equals( oper ) ) {
			String name=req.getParameter( "name" );
			String strId=req.getParameter( "id" );
			
			Role existRole=null; 
			Collection roles=roleMng.getAllRoles();
			for( Iterator iter=roles.iterator(); iter.hasNext(); ) {
				Role role=(Role) iter.next();
				if( role.getName().equals( name ) ) {
					existRole=role;
					break;
				}
			}
			
			boolean valid=false;
			if( existRole==null || (existRole.getId()+"").equals( strId ) ) {
				valid=true;
			}
			
			resp.setContentType("application/json;charset=UTF-8");
			PrintWriter writer=resp.getWriter();
			writer.write( valid+"" );			
			writer.flush();
			return;
		} else {
			// display from
			int first=WebUtil.getIntParameter( req, "first", 0 );
			// display max size
			int size=WebUtil.getIntParameter( req, "size", WebConstants.DEFAULT_SHOW_ENTITY_SIZE );
			// search key
			String searchName=req.getParameter( "name" );
/*			if(searchName!=null){
				searchName=new String(searchName.getBytes("ISO-8859-1"),"UTF-8");		        
			}*/
			
			Collection allRoles=null;
			int totalNumber=0;
			if( StringUtil.isEmpty( searchName ) ) {
				allRoles=roleMng.getAllRoles();
			} else {
				allRoles=roleMng.getLikelyRoles( searchName );
			}
			
			totalNumber=allRoles.size();
			Collection roles=Util.sub( allRoles, first, size );
			
			req.setAttribute( "totalNumber", new Integer(totalNumber) );
			req.setAttribute( "roles", roles );
			req.setAttribute( "first", new Integer(first) );
			req.setAttribute( "size", new Integer(size) );
			req.setAttribute( "name", searchName );
	
			WebUtil.forward( req, resp, "/ralasafe/privilege/roleList.jsp" );
		}
	}
}
