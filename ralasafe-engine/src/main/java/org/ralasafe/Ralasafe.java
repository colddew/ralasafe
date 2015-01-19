/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ralasafe.SystemConstant;
import org.ralasafe.entitle.CustomizedWhere;
import org.ralasafe.entitle.Decision;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.QueryResult;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;

/**
 * Ralasafe Service.
 * 
 * <p>
 * This class contains three major methods for application.
 * <ol>
 * 	<li>User-Privilege relationship methods</li>
 * 	<li>User query related methods</li>
 * 	<li>User decision related methods</li>
 * </ol>
 * </p>
 * 
 * <p>
 * In ralasafe Philosophy, ACL is divided into:
 * <ol>
 *     <li>Function level ACL</li>
 *     <li>Data leve ACL
 *        	<ol>
 *            	<li>User queries data from system</li>
 *            	<li>User operte data to system</li>
 *        	</ol>
 *     </li>
 * </ol>
 * </p>
 */
public class Ralasafe {
	private static final String appName = "ralasafe";

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param privilegeId       privilegeId
	 * @param user              who queries for data
	 * @param context           context
	 * @param where             customeized where conditions
	 * @return  query result
	 */
	public static QueryResult query(int privilegeId, User user, Map context,
			CustomizedWhere where) {

		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.query(privilegeId, user, context, where);
	}

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param privilegeId       privilegeId
	 * @param user              who queries for data
	 * @param context           context
	 * @return    query result
	 */
	public static QueryResult query(int privilegeId, User user, Map context) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.query(privilegeId, user, context);
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param privilegeId     privilegeId
	 * @param user            who queries for data
	 * @param context         context
	 * @param where           customized where condition 
	 * @param first           first index, count from 0,1,2...
	 * @param max             max records
	 * @return         query result
	 */
	public static QueryResult query(int privilegeId, User user, Map context,
			CustomizedWhere where, int first, int max) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.query(privilegeId, user, context, where, first,
				max);
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param privilegeId       privilegeId
	 * @param user              who queries for data
	 * @param context           context
	 * @param first             first index, count from 0,1,2...
	 * @param max               max returned records
	 * @return     query result
	 */
	public static QueryResult query(int privilegeId, User user, Map context,
			int first, int max) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.query(privilegeId, user, context, first, max);
	}

	/**
	 * Eval query policy, return result count.
	 * 
	 * @param privilegeId            privilegeId
	 * @param user                   who queries for data
	 * @param context                context
	 * @param where                  customized where condition
	 * @return     result count
	 */
	public static int queryCount(int privilegeId, User user, Map context,
			CustomizedWhere where) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.queryCount(privilegeId, user, context, where);
	}

	/**
	 * Eval query policy, return result count.
	 * 
	 * @param privilegeId            privilegeId
	 * @param user                   who queries for data
	 * @param context                context
	 * @return     result count
	 */
	public static int queryCount(int privilegeId, User user, Map context) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		return entitleManager.queryCount(privilegeId, user, context);
	}

	/**
	 * Eval decision policy, return decision result.
	 * 
	 * @param privilegeId                 privilegeId
	 * @param user                        who requests for this operation
	 * @param businessData                the business data
	 * @param context                     context
	 * @return        decision result
	 */
	public static Decision permit(int privilegeId, User user,
			Object businessData, Map context) {
		if (context == null) {
			context = new HashMap();
		}
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		context.put(SystemConstant.BUSINESS_DATA, businessData);
		return entitleManager.permit(Locale.US, privilegeId, user, context);
	}

	/**
	 * Does this user be granted for the privilege?
	 * 
	 * @param privilegeId               privilege
	 * @param user                      who
	 * @return true--be granted;   false--not be granted
	 */
	public static boolean hasPrivilege(int privilegeId, User user) {
		UserRoleManager userRoleMng = Factory.getUserRoleManager(appName,
				"ralasafe");
		Object userId=null;
		if( user!=null ) {
			userId=user.get(User.idFieldName);
		}
		return userRoleMng.hasPrivilege(userId, privilegeId);
	}
	
	/**
	 * Get the user's privilege tree, often for display operate menu.
	 * @param user     who
	 * @return         privilege tree(menu tree)
	 */
	public static Privilege getBusinessPrivilegeTree(User user) {
		UserRoleManager userRoleMng = Factory.getUserRoleManager(appName, "ralasafe");
		Object userId=user.get(User.idFieldName);
		
		return userRoleMng.getBusinessPrivilegeTree( userId );
	}
}
