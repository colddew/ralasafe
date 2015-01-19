/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.ScriptTestResult;
import org.ralasafe.user.User;

public abstract class AbstractPolicyTestAction extends AbstractTestAction {
	private static final Log log=LogFactory.getLog( AbstractPolicyTestAction.class );
	
	public abstract AbstractPolicyDesignHandler createPolicyHandler( HttpServletRequest req );
	public abstract String getPolicyHandlerAttributeKey( HttpServletRequest req );
	
	private AbstractPolicyDesignHandler getPolicyHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getPolicyHandlerAttributeKey( req );
		AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) req.getSession().getAttribute( key );
		
		if( handler==null||"loadFresh".equals(oper) ) {
			handler=createPolicyHandler( req );
		
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		}
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractPolicyDesignHandler handler=getPolicyHandler( req );
		req.setAttribute( "handler", handler );
		
		if( "return".equals( oper ) ) {
			String gotoPage=handler.getManagePage();
			
			// remove handler from session
			req.getSession().removeAttribute( getPolicyHandlerAttributeKey( req ) );
			
			// goto manage page
			resp.sendRedirect( gotoPage );
			return;
		} else {		
			// prepare test
			QueryManager queryMng=WebUtil.getQueryManager( req );
			handler.prepareTest( queryMng );

			WebUtil.forward( req, resp, handler.getTestMainPage() );
		}
	}

	

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractPolicyDesignHandler handler=getPolicyHandler( req );
		req.setAttribute( "handler", handler );
		
		
		if( "runTest".equals( oper ) ) {
			boolean testUserNeeded=handler.isTestUserNeeded();
			String[] testBusinessDataFields=handler.getTestBusinessDataFields();
			String[] testContextFields=handler.getTestContextFields();
			
			User testUser=buildTestUser( req, testUserNeeded );
			
			Object testBdData=null;
			Map testCtx=null;
			try {
				testBdData=buildTestBusinessData( req, testBusinessDataFields );
				testCtx=buildTestContext( req, testContextFields );
			} catch( Exception e ) {
				PrintWriter writer=resp.getWriter();
				writer.write( "<font color='red'><pre>" );
				e.printStackTrace( writer );
				writer.write( "</pre></font>" );
				return;
			}
			
			ScriptTestResult testResult=handler.run( testUser, testBdData, testCtx );
			req.setAttribute( "testResult", testResult );
			
			WebUtil.forward( req, resp, handler.getShowTestResultPage() );			
		} else if( "save".equals( oper ) ) {
			// remove handler from session
			req.getSession().removeAttribute( getPolicyHandlerAttributeKey( req ) );
			
			int id=WebUtil.getIntParameter( req, "id", -23 );
			try {
				handler.save( id );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			return;
		} else if( "setBusinessDataClass".equals( oper ) ) {
			String bdClass=req.getParameter( "businessDataClass" );			
			handler.setBusinessDataClass( bdClass );
			
			req.setAttribute("testBusinessDataFields", handler.getTestBusinessDataFields() );
			req.setAttribute("testBusinessDataFieldTypes", handler.getTestBusinessDataFieldTypes() );
			req.setAttribute( "bdClass", bdClass );
			
			WebUtil.forward( req, resp, "/ralasafe/common/inputTestBusinessData.jsp" );
			return;
		} 
	}
}
