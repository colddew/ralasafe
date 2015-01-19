/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.userType;

import java.util.Collection;

import org.ralasafe.EntityExistException;

public interface UserTypeManager {
 
	public abstract Collection getAllUserTypes();
	public abstract UserType getUserType(String name);
	public abstract UserType getUserTypeCopy(String name);
	public abstract void addUserType(UserType userType) throws EntityExistException;
	public abstract void deleteUserType(String name);
	public abstract void updateUserType(UserType userType);
	public abstract Collection getApplications(String userTypeName);
}

