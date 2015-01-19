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
import org.ralasafe.db.sql.xml.BusinessDataType;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.UserCategory;
import org.ralasafe.db.sql.xml.UserCategoryType;
import org.ralasafe.entitle.DecisionEntitlement;
import org.ralasafe.entitle.DecisionEntitlementTestResult;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.user.User;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class DecisionEntitlementHandler {
	private Privilege privilege;
	private List decisionEntitlements=new ArrayList();
	private QueryManager queryManager;
	private EntitleManager entitleManager;
	private String businessDataClass;
	
	public DecisionEntitlementHandler( Privilege privilege,
			Collection entitlements, QueryManager queryManager,
			EntitleManager entitleManager ) {
		this.privilege=privilege;
		this.decisionEntitlements.addAll( entitlements );
		this.queryManager=queryManager;
		this.entitleManager=entitleManager;
	}

	public String getBusinessDataClass() {
		return businessDataClass;
	}

	public void setBusinessDataClass( String businessDataClass ) {
		this.businessDataClass=businessDataClass;
	}

	public Privilege getPrivilege() {
		return privilege;
	}
	
	public List getDecisionEntitlements() {
		return decisionEntitlements;
	}
	
	public void addEntitle( DecisionEntitlement entitle ) {
		decisionEntitlements.add( entitle );
	}
	
	public void updateEntitle( int index, DecisionEntitlement entitle ) {
		decisionEntitlements.set( index, entitle );
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
			if( moveIndex!=(decisionEntitlements.size()-1) ) {
				changeIndex=moveIndex+1;
			}
		} else if( "bottom".equals( direct ) ) {
			if( moveIndex!=(decisionEntitlements.size()-1) ) {
				changeIndex=decisionEntitlements.size()-1;
			}
		}
		
		if( changeIndex!=-1 ) {
			Object moveOne=decisionEntitlements.get( moveIndex );
			
			if( "top".equals( direct ) ) {
				decisionEntitlements.remove( moveIndex );
				decisionEntitlements.add( 0, moveOne );
			} else if( "bottom".equals( direct ) ) {
				decisionEntitlements.remove( moveIndex );
				decisionEntitlements.add( decisionEntitlements.size(), moveOne );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {	
				Object changeOne=decisionEntitlements.get( changeIndex );
				
				decisionEntitlements.set( changeIndex, moveOne );
				decisionEntitlements.set( moveIndex, changeOne );
			}
		}
	}
	
	public void deleteEntitle( int index ) {
		decisionEntitlements.remove( index );
	}

	public void save() throws EntityExistException {
		Collection oldEntitles=entitleManager.getDecisionEntitlements( privilege.getId() );
		
		// delete old entries
		for( Iterator iter=oldEntitles.iterator(); iter.hasNext(); ) {
			DecisionEntitlement entitle=(DecisionEntitlement) iter.next();
			entitleManager.deleteDecisionEntitlement( entitle.getId() );
		}
		
		// add new entries
		for( Iterator iter=decisionEntitlements.iterator(); iter.hasNext(); ) {
			DecisionEntitlement entitle=(DecisionEntitlement) iter.next();
			entitleManager.addDecisionEntitlement( entitle );
		}
	}

	public String[] getTestContextFields() throws MarshalException, ValidationException {
		Set fields=new HashSet();
		
		for( Iterator iter=decisionEntitlements.iterator(); iter.hasNext(); ) {
			DecisionEntitlement entitle=(DecisionEntitlement) iter.next();
			
			String xmlContent=entitle.getBusinessData().getXmlContent();
			BusinessDataType bdt=BusinessDataType.unmarshal( new StringReader( xmlContent ) );
			DefineVariable[] defineVariables=bdt.getDefineVariable();
			Util.extractContextValueFields( defineVariables, queryManager, fields );
			
			xmlContent=entitle.getUserCategory().getXmlContent();
			UserCategoryType unmarshal;
			unmarshal=UserCategory.unmarshal( new StringReader( xmlContent ) );
				
			DefineVariable[] variables=unmarshal.getDefineVariable();
			Util.extractContextValueFields( variables, queryManager, fields );
		}
		
		return (String[]) fields.toArray( new String[0] );
	}

	public DecisionEntitlementTestResult run( Locale locale, User testUser, Map testCtx ) {
		DecisionEntitlementTestResult testDecisionEntitlement=entitleManager.testDecisionEntitlement( locale, privilege.getId(), decisionEntitlements,
				testUser, testCtx );
		return testDecisionEntitlement;
	}
	
	public String[] getTestBusinessDataFieldTypes() throws MarshalException, ValidationException {
		String[] fields=getTestBusinessDataFields();
		String[] types=new String[fields.length];
		
		if( !StringUtil.isEmpty( businessDataClass ) ) {
			String[][] reflectJavaBean=Util.reflectJavaBean( businessDataClass );
			
			for( int i=0; i<fields.length; i++ ) {
				String field=fields[i];
				
				for( int j=0; j<reflectJavaBean.length; j++ ) {
					String[] strings=reflectJavaBean[j];
					
					if( strings[0].equals( field ) ) {
						types[i]=strings[1];
						j=reflectJavaBean.length;
					}
				}
			}
		}
		
		return types;
	}

	public String[] getTestBusinessDataFields() throws MarshalException, ValidationException {
		Set fields=new HashSet();
		
		for( Iterator iter=decisionEntitlements.iterator(); iter.hasNext(); ) {
			DecisionEntitlement entitle=(DecisionEntitlement) iter.next();
			
			String xmlContent=entitle.getBusinessData().getXmlContent();
			BusinessDataType bdt=BusinessDataType.unmarshal( new StringReader( xmlContent ) );
			DefineVariable[] defineVariables=bdt.getDefineVariable();
			Util.extractBusinessDataFields( defineVariables, fields );
		}
		
		return (String[]) fields.toArray( new String[0] );
	}
}
