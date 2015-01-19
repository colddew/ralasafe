/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

import java.util.Iterator;
import java.util.Map;

import org.ralasafe.application.ApplicationManager;
import org.ralasafe.application.ApplicationManagerImpl;
import org.ralasafe.entitle.BackupManager;
import org.ralasafe.entitle.BackupManagerImpl;
import org.ralasafe.entitle.BusinessDataManager;
import org.ralasafe.entitle.BusinessDataManagerImpl;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.EntitleManagerImpl;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.QueryManagerImpl;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.entitle.UserCategoryManagerImpl;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.privilege.PrivilegeManagerImpl;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.privilege.RoleManagerImpl;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.privilege.UserRoleManagerImpl;
import org.ralasafe.user.UserManager;
import org.ralasafe.userType.UserType;
import org.ralasafe.userType.UserTypeManager;
import org.ralasafe.userType.UserTypeManagerImpl;

public class Factory {
	private static java.util.Map privilegeManagers = new java.util.HashMap();
	private static java.util.Map roleManagers = new java.util.HashMap();
	private static java.util.Map userRoleManagers = new java.util.HashMap();
	private static final ApplicationManager applicationManager;
	private static final BackupManager backupManager;
	private static final UserTypeManager userTypeManager;
	private static java.util.Map userManagers = new java.util.HashMap();
	private static java.util.Map filterManagers = new java.util.HashMap();
	private static java.util.Map queryManagers = new java.util.HashMap();
	private static java.util.Map userCategoryManagers = new java.util.HashMap();
	private static java.util.Map businessDataManagers = new java.util.HashMap();
	private static java.util.Map entitlementManagers = new java.util.HashMap();

	static {
		applicationManager = new ApplicationManagerImpl();
		userTypeManager = new UserTypeManagerImpl();
		backupManager = new BackupManagerImpl();
	}

	public static BackupManager getBackupManager() {
		return backupManager;
	}

	public static UserTypeManager getUserTypeManager() {
		return userTypeManager;
	}

	public static UserManager getUserManager(String userTypeName) {
		if (userManagers.get(userTypeName) == null) {
			UserType userType = Factory.getUserTypeManager().getUserType(
					userTypeName);
			if (userType != null) {
				UserManager userManager = new UserManager(userType);
				userManagers.put(userTypeName, userManager);
			}
		}
		return (UserManager) userManagers.get(userTypeName);
	}

	/**
	 * Update other managers when application changed.
	 * 
	 * @param appName
	 */
	public static synchronized void applicationChanged(String appName) {
		privilegeManagers.remove(appName);
		roleManagers.remove(appName);
		userRoleManagers.remove(appName);
		// commonElementManagers.remove( commonElementManagers.get( appName ) );
		// tableElementManagers.remove( tableElementManagers.get( appName ) );
		filterManagers.remove(appName);
		queryManagers.remove(appName);
		userCategoryManagers.remove(appName);
		businessDataManagers.remove(appName);
		entitlementManagers.remove(appName);
	}

	public static ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public static PrivilegeManager getPrivilegeManager(String appName) {
		if (privilegeManagers.get(appName) == null) {
			synchronized (privilegeManagers) {
				privilegeManagers.put(appName,
						new PrivilegeManagerImpl(appName));
			}
		}
		return (PrivilegeManager) privilegeManagers.get(appName);
	}

	public static synchronized RoleManager getRoleManager(String appName) {
		if (roleManagers.get(appName) == null) {
			synchronized (roleManagers) {
				roleManagers.put(appName, new RoleManagerImpl(appName));
			}
		}
		return (RoleManager) roleManagers.get(appName);
	}

	public static synchronized QueryManager getQueryManager(String appName) {
		if (queryManagers.get(appName) == null) {
			synchronized (queryManagers) {
				queryManagers.put(appName, new QueryManagerImpl(appName));
			}
		}
		return (QueryManager) queryManagers.get(appName);
	}

	public static synchronized UserCategoryManager getUserCategoryManager(
			String appName) {
		if (userCategoryManagers.get(appName) == null) {
			synchronized (userCategoryManagers) {
				userCategoryManagers.put(appName, new UserCategoryManagerImpl(
						appName));
			}
		}
		return (UserCategoryManager) userCategoryManagers.get(appName);
	}

	public static synchronized BusinessDataManager getBusinessDataManager(
			String appName) {
		if (businessDataManagers.get(appName) == null) {
			synchronized (businessDataManagers) {
				businessDataManagers.put(appName, new BusinessDataManagerImpl(
						appName));
			}
		}
		return (BusinessDataManager) businessDataManagers.get(appName);
	}

	public static synchronized EntitleManager getEntitleManager(String appName) {
		if (entitlementManagers.get(appName) == null) {
			synchronized (entitlementManagers) {
				entitlementManagers.put(appName,
						new EntitleManagerImpl(appName));
			}
		}
		return (EntitleManager) entitlementManagers.get(appName);
	}

	public static synchronized UserRoleManager getUserRoleManager(
			String appName, String userTypeName) {
		if (userRoleManagers.get(appName) == null) {
			synchronized (userRoleManagers) {
				userRoleManagers.put(appName, new java.util.HashMap());
			}
		}
		Map userRoleManagerMap = (Map) userRoleManagers.get(appName);
		if (userRoleManagerMap.get(userTypeName) == null) {
			synchronized (userRoleManagerMap) {
				userRoleManagerMap.put(userTypeName, new UserRoleManagerImpl(
						appName, userTypeName));
			}
		}
		return (UserRoleManager) ((Map) userRoleManagers.get(appName))
				.get(userTypeName);
	}

	/**
	 * Update other managers when userType changed.
	 * 
	 * @param appName
	 */
	public static synchronized void userTypeChanged(String userTypeName) {
		Iterator itr = userRoleManagers.values().iterator();
		while (itr.hasNext()) {
			Map userRoleManagerMap = (Map) itr.next();
			userRoleManagerMap.remove(userTypeName);
		}
	}

	/**
	 * Update other managers when applicationUserType changed.
	 * 
	 * @param appName
	 */
	public static synchronized void applicationUserTypeChanged(String appName,
			String userTypeName) {
		Map userRoleManagerMap = (Map) userRoleManagers.get(appName);
		if (userRoleManagerMap != null) {
			userRoleManagerMap.remove(userTypeName);
		}
	}
}
