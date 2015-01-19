/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.application;

/**
 * An application may be used by many usertype.
 * Example: ERP would be used by employee, customer, supplier
 */
public class ApplicationUserType {

	private String appName;
	private String userTypeName;
	private String userMetadataStr;

	public ApplicationUserType() {
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName( String appName ) {
		this.appName=appName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName( String userTypeName ) {
		this.userTypeName=userTypeName;
	}

	public String getUserMetadataStr() {
		return userMetadataStr;
	}

	public void setUserMetadataStr( String userMetadataStr ) {
		this.userMetadataStr=userMetadataStr;
	}
}
