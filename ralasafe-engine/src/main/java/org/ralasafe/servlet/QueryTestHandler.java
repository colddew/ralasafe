/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.db.sql.Query;
import org.ralasafe.db.sql.QueryFactory;
import org.ralasafe.db.sql.UserValue;
import org.ralasafe.db.sql.Value;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.QueryResult;
import org.ralasafe.user.User;
import org.ralasafe.util.Util;

public class QueryTestHandler {

	private final QueryType xmlQuery;
	private final Query sqlQuery;
	
	public QueryTestHandler( QueryType xmlQuery ) {
		this.xmlQuery=xmlQuery;
		this.sqlQuery=QueryFactory.getQuery( xmlQuery );
	}

	public QueryType getQuery() {
		return xmlQuery;
	}
	
	public String getSql() {
		return sqlQuery.toSQL();
	}
	
	public boolean isTestUserNeeded() {
		ArrayList values=sqlQuery.getValues();
		for( Iterator iter=values.iterator(); iter.hasNext(); ) {
			Value v=(Value) iter.next();
			
			if( v instanceof UserValue ) {
				return true;				
			}
		}
		
		return false;
	}
	
	public String[] getTestContextFields() {
		ArrayList values=sqlQuery.getValues();
		ArrayList result=new ArrayList( values.size() );
		Util.extractContextValueFields( sqlQuery, result );
		
		return (String[]) result.toArray( new String[0] );
	}
	
	public QueryResult run( User testUser, Map testCtx, int first, int max ) {
		return sqlQuery.execute( testUser, testCtx, first, max );
	}

	public String getManagePage() {
		return "./queryMng.rls";
	}

	public void save( int id, QueryManager queryManager ) throws EntityExistException {
		queryManager.updateQuery( id, (org.ralasafe.db.sql.xml.Query) xmlQuery );
	}
}
