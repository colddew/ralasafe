/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.QueryResult;
import org.ralasafe.user.User;

public class QueryTestAction extends AbstractTestAction {
	private static final Log log=LogFactory.getLog( QueryTestAction.class );
	private static final int DEFAULT_QUERY_RESULT_PAGE_SIZE=15;
	
	public QueryTestHandler createHandler( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		QueryManager queryManager=WebUtil.getQueryManager( req );
		Query query=queryManager.getQuery( id );
		
		org.ralasafe.db.sql.xml.QueryType xmlQuery;
		try {
			xmlQuery = org.ralasafe.db.sql.xml.Query
					.unmarshal(new StringReader(query.getXmlContent()));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		
		return new QueryTestHandler( xmlQuery );
	}
	
	public String getHandlerAttributeKey( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		return "_$ralasafeQuery_" + id;
	}
	
	private QueryTestHandler getHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getHandlerAttributeKey( req );
		Object obj=req.getSession().getAttribute( key );
		QueryTestHandler handler=null;
		
		if( obj==null||"loadFresh".equals(oper) ) {
			handler=createHandler( req );
			
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		} else if( obj instanceof QueryDesignHandler ) {
			QueryDesignHandler designHandler=(QueryDesignHandler) obj;
			QueryType query=designHandler.getQuery();
			handler=new QueryTestHandler( query );
			req.getSession().setAttribute( key, handler );
		} else if( obj instanceof QueryRawHandler ) {
			QueryRawHandler rawHandler=(QueryRawHandler) obj;
			QueryType query=rawHandler.getQuery();
			handler=new QueryTestHandler( query );
			req.getSession().setAttribute( key, handler );
		} else {
			handler=(QueryTestHandler) obj;
		}
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );		
		QueryTestHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() );
		}
		
		if( "return".equals( oper ) ) {
			String gotoPage=handler.getManagePage();
			
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			// goto manage page
			resp.sendRedirect( gotoPage );
			return;
		} else {
			WebUtil.forward( req, resp, "/ralasafe/query/test.jsp" );
		}
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );		
		QueryTestHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() );
		}
		
		boolean testUserNeeded=handler.isTestUserNeeded();
		String[] testContextFields=handler.getTestContextFields();
		
		if( "runTest".equals( oper ) ) {
			User testUser=buildTestUser( req, testUserNeeded );
			
			Map testCtx=null;
			try {
				testCtx=buildTestContext( req, testContextFields );
			} catch( Exception e ) {
				log.error( e );
				throw new ServletException( e );
			}
			
			int first=WebUtil.getIntParameter( req, "first", 0 );
			QueryResult testResult;
			try {
				testResult=handler.run( testUser, testCtx, first, DEFAULT_QUERY_RESULT_PAGE_SIZE );
				req.setAttribute( "testResult", testResult );
				
				WebUtil.forward( req, resp, "/ralasafe/query/testResult.jsp" );		
			} catch( Exception e ) {
				PrintWriter writer=resp.getWriter();
				writer.write( "<font color='red'><pre>" );
				e.printStackTrace( writer );
				writer.write( "</pre></font>" );
			}
		} else if( "save".equals( oper ) ) {
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			int id=WebUtil.getIntParameter( req, "id", -23 );
			QueryManager queryManager=WebUtil.getQueryManager( req );
			try {
				handler.save( id, queryManager );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			return;
		} 
	}
}
