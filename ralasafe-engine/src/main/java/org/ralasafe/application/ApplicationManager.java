/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.application;

import java.util.Collection;
import java.util.Locale;

import org.ralasafe.EntityExistException;
import org.ralasafe.userType.UserType;

public interface ApplicationManager {

	public abstract void addApplication(Locale locale, Application app)
			throws EntityExistException;

	public abstract void updateApplication(Application app);

	public abstract void deleteApplication(String name);

	public abstract Application getApplication(String name);

	public abstract Collection getAllApplications();

	public abstract void addApplicationUserType(Locale locale, String appName,
			UserType userType);

	/**
	 * Update an ${@link UserType} of an application, update by usertype's name.
	 * @param appName
	 * @param userType
	 */
	public abstract void updateApplicatonUserType(String appName,
			UserType userType);

	/**
	 * Delete an ${@link UserType} from an application, it will delete some related tables
	 * @param appName
	 * @param userTypeName
	 */
	public abstract void deleteApplicationUserType(String appName,
			String userTypeName);
}
