/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.ExprGroup;
import org.ralasafe.db.sql.xml.UserCategory;
import org.ralasafe.db.sql.xml.UserCategoryType;
import org.ralasafe.entitle.ScriptTestResult;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.script.AbstractPolicy;
import org.ralasafe.script.ScriptFactory;
import org.ralasafe.user.User;

public class UserCategoryPolicyDesignHandler extends AbstractPolicyDesignHandler {
	private final UserCategoryManager manager;
	final UserCategoryType xmlUc;
	
	public String getPolicyType() {
		return "userCategory";
	}

	public String getManagePage() {
		return "./userCategoryMng.rls";
	}

	public UserCategoryPolicyDesignHandler( UserCategoryManager manager,
			UserCategoryType xmlUc ) {
		this.manager=manager;
		this.xmlUc=xmlUc;
	}

	public String getDesignPageTitle() {
		return "Design User Category: " + xmlUc.getName();
	}
	
	public String getRawPageTitle() {
		return "Edit User Category manually: " + xmlUc.getName();
	}
	
	public DefineVariable[] getVariables() {
		return xmlUc.getDefineVariable();
	}

	public void deleteVariable( int id ) {
		xmlUc.removeDefineVariableAt( id );
	}

	public void addVariable( DefineVariable var ) {
		xmlUc.addDefineVariable( var );
	}

	public void updateVariable( int varIndex, DefineVariable var ) {
		xmlUc.setDefineVariable( varIndex, var );
	}

	public ExprGroup getExprGroup() {
		return xmlUc.getExprGroup();
	}

	public void save( int id ) throws EntityExistException {
		manager.updateUserCategory( id, (UserCategory) xmlUc );
	}

	public void setDesignMode() {
		xmlUc.setIsRawScript( false );
	}
	
	public void setRawMode() {
		xmlUc.setIsRawScript( true );
	}

	public void setRawScript( String script ) {
		xmlUc.getRawScript().setContent( script );
	}

	public String getRawScript() {
		return xmlUc.getRawScript().getContent();
	}

	public ScriptTestResult run( User user, Object businessData, Map context ) {
		AbstractPolicy policy2=getPolicy();
		org.ralasafe.script.UserCategory script=(org.ralasafe.script.UserCategory) policy2;
		
		return script.test( user, context, getQueryManager() );
	}

	public AbstractPolicy transferXml2Policy() {
		return ScriptFactory.getUserCategory( xmlUc, getQueryManager() );
	}
}
