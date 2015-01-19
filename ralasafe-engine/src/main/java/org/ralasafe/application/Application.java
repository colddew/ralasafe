/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.application;

import java.util.Collection;

/**
 * Application means your EPR/OA/CRM/eHR systems, etc.
 * 
 */
public class Application {
 
	private String name;
	private String description;
//	private Set applicationUserTypes = new HashSet(0);
	/** Collection< org.ralasafe.userType.UserType > */
	private Collection userTypes = new java.util.ArrayList();

	public Application() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public Set getApplicationUserTypes() {
//		return this.applicationUserTypes;
//	}
//
//	public void setApplicationUserTypes(Set applicationUserTypes) {
//		this.applicationUserTypes = applicationUserTypes;
//	}

	public Collection getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(Collection userTypes) {
		this.userTypes = userTypes;
	}
}

