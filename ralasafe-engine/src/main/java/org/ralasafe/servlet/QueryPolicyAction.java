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
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;

/**
 * Template servlet for delegation policy edit and test.
 * 
 * @author Wang Jinbao(Julian Wong)
 *
 */
public class QueryPolicyAction extends Action {
	private static final Log log=LogFactory.getLog( QueryPolicyAction.class );
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		int id=WebUtil.getIntParameter( req, "id", 0 );
		
		if( "testPolicy".equals( oper ) ) {
			resp.sendRedirect( "./queryTest.rls?oper=loadFresh&id="+id );
		} else {
			// edit policy
			// If a user clicks "policy?oper=...&id=..." url, it means user want to edit it
			// for the first time. So we need load query from database, not from session.
			// If a user want to continue edit query cached in session, should direct to
			// url "designQuery?id=..." or "rawQuery?id=..."
			QueryManager queryMng=WebUtil.getQueryManager( req );
			Query query=queryMng.getQuery( id );
			org.ralasafe.db.sql.Query sqlQuery=query.getSqlQuery();
			if( sqlQuery.isRawSQL() ) {
				resp.sendRedirect( "./queryRaw.rls?oper=loadFresh&id="+id );
			} else {
				resp.sendRedirect( "./queryDesign.rls?oper=loadFresh&id="+id );
			}
		}
	}
}
