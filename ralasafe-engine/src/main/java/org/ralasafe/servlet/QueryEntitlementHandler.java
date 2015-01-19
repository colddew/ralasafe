/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.ralasafe.EntityExistException;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.UserCategory;
import org.ralasafe.db.sql.xml.UserCategoryType;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryEntitlement;
import org.ralasafe.entitle.QueryEntitlementTestResult;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.user.User;
import org.ralasafe.util.Util;

public class QueryEntitlementHandler {
	private Privilege privilege;
	private List queryEntitlements=new ArrayList();
	private QueryManager queryManager;
	private EntitleManager entitleManager;
	
	public QueryEntitlementHandler( Privilege privilege,
			Collection entitlements, QueryManager queryManager,
			EntitleManager entitleManager ) {
		this.privilege=privilege;
		this.queryEntitlements.addAll( entitlements );
		this.queryManager=queryManager;
		this.entitleManager=entitleManager;
	}

	public Privilege getPrivilege() {
		return privilege;
	}
	
	public List getQueryEntitlements() {
		return queryEntitlements;
	}
	
	public void addEntitle( QueryEntitlement entitle ) {
		queryEntitlements.add( entitle );
	}
	
	public void updateEntitle( int index, QueryEntitlement entitle ) {
		queryEntitlements.set( index, entitle );
	}
	
	public void moveEntitle( int index, String direct ) {
		int moveIndex=index;
		
		// change column with it
		int changeIndex=-1;
		if( "top".equals( direct ) ) {
			if( moveIndex!=0 ) {
				changeIndex=0;
			}
		} else if( "up".equals( direct ) ) {
			if( moveIndex!=0 ) { 
				changeIndex=moveIndex-1;
			}
		} else if( "down".equals( direct ) ) {
			if( moveIndex!=(queryEntitlements.size()-1) ) {
				changeIndex=moveIndex+1;
			}
		} else if( "bottom".equals( direct ) ) {
			if( moveIndex!=(queryEntitlements.size()-1) ) {
				changeIndex=queryEntitlements.size()-1;
			}
		}
		
		if( changeIndex!=-1 ) {
			Object moveOne=queryEntitlements.get( moveIndex );
			
			if( "top".equals( direct ) ) {
				queryEntitlements.remove( moveIndex );
				queryEntitlements.add( 0, moveOne );
			} else if( "bottom".equals( direct ) ) {
				queryEntitlements.remove( moveIndex );
				queryEntitlements.add( queryEntitlements.size(), moveOne );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {	
				Object changeOne=queryEntitlements.get( changeIndex );
				
				queryEntitlements.set( changeIndex, moveOne );
				queryEntitlements.set( moveIndex, changeOne );
			}
		}
	}
	
	public void deleteEntitle( int index ) {
		queryEntitlements.remove( index );
	}

	public void save() throws EntityExistException {
		Collection oldEntitles=entitleManager.getQueryEntitlements( privilege.getId() );
		
		// delete old entries
		for( Iterator iter=oldEntitles.iterator(); iter.hasNext(); ) {
			QueryEntitlement entitle=(QueryEntitlement) iter.next();
			entitleManager.deleteQueryEntitlement( entitle.getId() );
		}
		
		// add new entries
		for( Iterator iter=queryEntitlements.iterator(); iter.hasNext(); ) {
			QueryEntitlement entitle=(QueryEntitlement) iter.next();
			entitleManager.addQueryEntitlement( entitle );
		}
	}

	public String[] getTestContextFields() throws MarshalException, ValidationException {
		Set fields=new HashSet();
		
		for( Iterator iter=queryEntitlements.iterator(); iter.hasNext(); ) {
			QueryEntitlement entitle=(QueryEntitlement) iter.next();
			Query query=entitle.getQuery();
			
			org.ralasafe.db.sql.Query sqlQuery=query.getSqlQuery();
			Util.extractContextValueFields( sqlQuery, fields );
			
			String xmlContent=entitle.getUserCategory().getXmlContent();
			UserCategoryType unmarshal;
			unmarshal=UserCategory.unmarshal( new StringReader( xmlContent ) );
				
			DefineVariable[] variables=unmarshal.getDefineVariable();
			Util.extractContextValueFields( variables, queryManager, fields );
		}
		
		return (String[]) fields.toArray( new String[0] );
	}

	public QueryEntitlementTestResult run( Locale locale, User testUser, Map testCtx, int first,
			int max ) {
		QueryEntitlementTestResult testQueryEntitlement=entitleManager.testQueryEntitlement( locale, privilege.getId(), queryEntitlements,
				testUser, testCtx, first, max );
		return testQueryEntitlement;
	}
}
