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
import org.ralasafe.WebConstants;
import org.ralasafe.WebRalasafe;
import org.ralasafe.db.sql.Query;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.user.User;
import org.ralasafe.user.UserManager;
import org.ralasafe.userType.UserType;

public class UserMngAction extends Action {

	private static final Log log=LogFactory.getLog( UserMngAction.class );
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		UserManager userMng=WebUtil.getUserManager( req );
		
		// log
		if( log.isDebugEnabled() ) {
			log.debug( "oper="+oper );
		}
		
		Collection coll=null;
		int totalNumber=0;
		
		// display from
		int first=WebUtil.getIntParameter( req, "first", 0 );
		// display max size
		int size=WebUtil.getIntParameter( req, "size", WebConstants.DEFAULT_SHOW_ENTITY_SIZE );
		// search key
	    
		String searchName=req.getParameter( "name" );
/*		if(searchName!=null){
			searchName=new String(searchName.getBytes("ISO-8859-1"),"UTF-8");		        
		}*/
		if( RalasafeController.isSecured() ) {
			log.debug( "Ralasafe is secured. Query granted users..." );
			User user=WebRalasafe.getCurrentUser( req );
			
			EntitleManager entitleMng=WebUtil.getEntitleManager( req );
			Query query=entitleMng.getQuery( Privilege.ASSIGN_ROLE_TO_USER_ID, 
					user, null ).getSqlQuery();
			coll=userMng.selectUsersByCustomizedJavaBean( user, null, searchName, query, first, size );
			totalNumber=userMng.selectUserCounts( user, null, searchName, query );
		} else {
			log.debug( "Query all users..." );
			coll=userMng.selectUsers( null, null, searchName, null, first, size );
			totalNumber=userMng.selectUserCounts( null, null, searchName, null );
		}
		
		UserType userType=WebUtil.getUserType( req );
		String[] showUserFields=userType.getUserMetadata().getShowUserFields();
		String[] showUserFieldDisplayNames=userType.getUserMetadata().getShowUserFieldDisplayNames();
		
		req.setAttribute( "totalNumber", new Integer(totalNumber) );
		req.setAttribute( "users", coll );
		req.setAttribute( "first", new Integer(first) );
		req.setAttribute( "size", new Integer(size) );
		req.setAttribute( "name", searchName );
		req.setAttribute( "showUserFields", showUserFields );
		req.setAttribute( "showUserFieldDisplayNames", showUserFieldDisplayNames );
		
		if( "showUserWidget".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/user/userListWidget.jsp" );
		} else {
			WebUtil.forward( req, resp, "/ralasafe/user/userList.jsp" );
		}
	}
}
