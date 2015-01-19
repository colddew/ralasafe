/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Template servlet for delegation policy edit and test.
 * 
 * @author Wang Jinbao(Julian Wong)
 *
 */
public abstract class AbstractPolicyAction extends Action {
	private static final Log log=LogFactory.getLog( AbstractPolicyAction.class );
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		int id=WebUtil.getIntParameter( req, "id", 0 );
		
		// edit policy
		// If a user clicks "policy?oper=...&id=..." url, it means user want to edit it
		// for the first time. So we need load policy from database, not from session.
		// If a user want to continue edit policy cached in session, should direct to
		// url "designPolicy?id=..." or "rawPolicy?id=..."
		
		if( "testPolicy".equals( oper ) ) {
			resp.sendRedirect( "./policyTest.rls?oper=loadFresh&id="+id );
		} else {
			// edit policy
			boolean rawScript=isRawScript( req );
			if( rawScript ) {
				resp.sendRedirect( "./policyRaw.rls?oper=loadFresh&id="+id );
			} else {
				resp.sendRedirect( "./policyDesign.rls?oper=loadFresh&id="+id );
			}
		}
	}
	
	public abstract boolean isRawScript( HttpServletRequest req );
}
