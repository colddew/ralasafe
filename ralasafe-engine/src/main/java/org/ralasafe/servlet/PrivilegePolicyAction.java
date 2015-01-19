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
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;

public class PrivilegePolicyAction extends Action {
	private static final Log log=LogFactory.getLog( PrivilegePolicyAction.class );
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		int id=WebUtil.getIntParameter( req, "id", -23 );
		
		PrivilegeManager pvlgMng=WebUtil.getPrivilegeManager( req );
		Privilege pvlg=pvlgMng.getPrivilege( id );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", id=" + id + ", name=" + pvlg.getName() );
		}
		
		EntitleManager entitleMng=WebUtil.getEntitleManager( req );
		
		boolean isQueryEntitle=false;
		boolean isDecisionEntitle=false;
		Collection queryEntitlements=entitleMng.getQueryEntitlements( id );
		Collection decisionEntitlements=null;
		if( queryEntitlements!=null
				&& queryEntitlements.size()>0 ) { 
			// this is a query privilege
			isQueryEntitle=true;
		} else {
			decisionEntitlements=entitleMng.getDecisionEntitlements( id );
			if( decisionEntitlements!=null 
					&& decisionEntitlements.size()>0 ) {
				// this is a decison privilege
				isDecisionEntitle=true;
			} 
		}
		
		// no entitlement is set for this privilege
		if( !isQueryEntitle&&!isDecisionEntitle ) {
			req.setAttribute( "privilege", pvlg );
			WebUtil.forward( req, resp, "/ralasafe/privilege/chooseType.jsp?id="+id );
			return;
		}
		
		if( "testPolicy".equals( oper ) ) {
			if( isDecisionEntitle ) {
				resp.sendRedirect( "./decisionEntitlementTest.rls?oper=loadFresh&id="+id );
				return;
			} else if( isQueryEntitle ) {
				resp.sendRedirect( "./queryEntitlementTest.rls?oper=loadFresh&id="+id );
				return;
			}
		} else if( "editPolicy".equals( oper ) ) {		
			if( isDecisionEntitle ) {
				resp.sendRedirect( "./decisionEntitlement.rls?oper=loadFresh&id="+id );
				return;
			} else if( isQueryEntitle ) {
				resp.sendRedirect( "./queryEntitlement.rls?oper=loadFresh&id="+id );
				return;
			}
		}
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost( req, resp );
	}
}
