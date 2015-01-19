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
import org.ralasafe.EntityExistException;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryEntitlement;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.UserCategory;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;

public class QueryEntitlementAction extends Action {
	private static final Log log=LogFactory.getLog( QueryEntitlementAction.class );
	
	public QueryEntitlementHandler createHandler( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", -23 );
		
		PrivilegeManager pvlgMng=WebUtil.getPrivilegeManager( req );
		EntitleManager entitleMng=WebUtil.getEntitleManager( req );
		QueryManager queryMng=WebUtil.getQueryManager( req );
		
		Privilege pvlg=pvlgMng.getPrivilege( id );
		Collection entitlements=entitleMng.getQueryEntitlements( id );
		
		if( log.isDebugEnabled() ) {
			log.debug( "id=" + id + ", name=" + pvlg.getName() );
		}
		
		return new QueryEntitlementHandler( pvlg, entitlements, queryMng, entitleMng );
	}
	
	public String getHandlerAttributeKey( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		return "_$ralasafeQueryEntitlement_" + id;
	}
	
	private QueryEntitlementHandler getHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getHandlerAttributeKey( req );
		QueryEntitlementHandler handler=null;
		Object obj=req.getSession().getAttribute( key );
		
		if( obj==null||"loadFresh".equals(oper) ) {
			handler=createHandler( req );
		
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		} else {
			handler=(QueryEntitlementHandler) obj;
		}
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		QueryEntitlementHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( "loadEntitlements".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/privilege/queryEntitlementTable.jsp" );
			return;
		} else if( "getEntitlement".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -23 );
			if( index>=0 ) {
				QueryEntitlement entitlement=(QueryEntitlement) handler.getQueryEntitlements().get( index );
				req.setAttribute( "entitlement", entitlement );				
			}
			
			WebUtil.forward( req, resp, "/ralasafe/privilege/editQueryEntitlement.jsp" );
			return;
		} else if( "return".equals( oper ) ) {
			String gotoUrl="./nonRolePrivilegeMng.rls";
			if( handler.getPrivilege().getType()==0 ) {
				gotoUrl="./privilegeMng.rls";
			} 
			String key=getHandlerAttributeKey( req );
			req.getSession().removeAttribute( key );
			
			resp.sendRedirect( gotoUrl );
			return;
		} else {
			WebUtil.forward( req, resp, "/ralasafe/privilege/queryEntitlement.jsp" );
			return;
		}
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		QueryEntitlementHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( "addEntitle".equals( oper ) ) {
			QueryEntitlement entitle=getQueryEntitlement( req );
			handler.addEntitle( entitle );
			return;
		} else if( "editEntitle".equals( oper) ) {
			int index=WebUtil.getIntParameter( req, "index", 0 );
			QueryEntitlement entitle=getQueryEntitlement( req );
			handler.updateEntitle( index, entitle );
			return;
		} else if( "moveEntitle".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", 0 );
			String direct=req.getParameter( "direct" );
			handler.moveEntitle( index, direct );
			return;
		} else if( "deleteEntitle".equals( oper) ) {
			int index=WebUtil.getIntParameter( req, "index", 0 );
			handler.deleteEntitle( index );
			return;
		} else if( "save".equals( oper ) ) {
			try {
				handler.save();
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			// clean session
			String key=getHandlerAttributeKey( req );
			req.getSession().removeAttribute( key );
			return;
		}
	}

	private QueryEntitlement getQueryEntitlement( HttpServletRequest req ) {
		String description=req.getParameter( "description" );
		int queryId=WebUtil.getIntParameter( req, "queryId", -23 );
		int ucId=WebUtil.getIntParameter( req, "ucId", -23 );
		int id=WebUtil.getIntParameter( req, "id", -23 );
		
		QueryManager queryManager=WebUtil.getQueryManager( req );
		Query query=queryManager.getQuery( queryId );
		UserCategoryManager userCategoryManager=WebUtil.getUserCategoryManager( req );
		UserCategory userCategory=userCategoryManager.getUserCategory( ucId );
		
		QueryEntitlement entitle=new QueryEntitlement();
		entitle.setDescription( description );
		entitle.setPrivilegeId( id );
		entitle.setQueryId( queryId );
		entitle.setUserCategoryId( ucId );
		entitle.setQuery( query );
		entitle.setUserCategory( userCategory );
		
		return entitle;
	}
}
