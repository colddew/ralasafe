/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.QueryEntitlementTestResult;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.user.User;

public class QueryEntitlementTestAction extends AbstractTestAction {
	private static final Log log=LogFactory.getLog( QueryEntitlementTestAction.class );
	private static final int DEFAULT_QUERY_RESULT_PAGE_SIZE=15;
	
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
		
		if( "return".equals( oper ) ) {
			String gotoUrl="./nonRolePrivilegeMng.rls";
			if( handler.getPrivilege().getType()==0 ) {
				gotoUrl="./privilegeMng.rls";
			} 
			
			String key=getHandlerAttributeKey( req );
			req.getSession().removeAttribute( key );
			
			resp.sendRedirect( gotoUrl );
			return;
		} else {
			WebUtil.forward( req, resp, "/ralasafe/privilege/queryEntitlementTest.jsp" );
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
		
		if( "runTest".equals( oper ) ) {
			User testUser=buildTestUser( req, true );
			
			Map testCtx=null;
			try {
				testCtx=buildTestContext( req, handler.getTestContextFields() );
			} catch( Exception e ) {
				log.error( e );
				PrintWriter writer=resp.getWriter();
				writer.write( "<font color='red'><pre>" );
				e.printStackTrace( writer );
				writer.write( "</pre></font>" );
				return;
			}
			
			int first=WebUtil.getIntParameter( req, "first", 0 );
			QueryEntitlementTestResult testResult;
			try {
				testResult=handler.run( req.getLocale(), testUser, testCtx, first, DEFAULT_QUERY_RESULT_PAGE_SIZE );
				req.setAttribute( "testResult", testResult );
				
				WebUtil.forward( req, resp, "/ralasafe/privilege/testQueryEntitlementResult.jsp" );		
			} catch( Exception e ) {
				PrintWriter writer=resp.getWriter();
				writer.write( "<font color='red'><pre>" );
				e.printStackTrace( writer );
				writer.write( "</pre></font>" );
			}
		}
	}
}
