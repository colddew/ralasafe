/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.entitle.CustomizedWhere;
import org.ralasafe.entitle.Decision;
import org.ralasafe.entitle.QueryResult;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;

/**
 * Ralasafe Web Service.
 * 
 * <p>
 * This class contains three major methods for web application.
 * <ol>
 * 	<li>User-Privilege relationship methods</li>
 * 	<li>User query related methods</li>
 * 	<li>User decision related methods</li>
 * </ol>
 * </p>
 * 
 * @see Ralasafe
 */
public class WebRalasafe {
	/**
	 * Store login user into HttpSession with this key
	 */
	private static final String USER_KEY = "_$user";

	/**
	 * Store deny reason into HttpServletRequest with this key
	 */
	private static final String DENY_REASON = "_$denyReason";

	/**
	 * Store fields into HttpServletRequest with this key
	 */
	private static final String FIELDS = "_$fields";

	/**
	 * Store readonly fields into HttpServletRequest with this key
	 */
	private static final String READ_ONLY_FIELDS = "_$readOnlyFields";

	/**
	 * Eval decision policy, return decision result.
	 * 
	 * @param req	    HttpRequest, the login user be read from HttpSession with key USER_KEY 
	 * @param privilegeId               privilegeId
	 * @param businessObject            business data
	 * @return     decision result. true--permit; false--deny, deny reasion be store in request with key DENY_REASON
	 */
	public static boolean permit(HttpServletRequest req, int privilegeId,
			Object businessObject) {
		return permit(req, privilegeId, businessObject, new HashMap());
	}

	/**
	 * Eval decision policy, return decision result.
	 * 
	 * @param req	    HttpRequest, the login user be read from HttpSession with key USER_KEY 
	 * @param privilegeId               privilegeId
	 * @param businessObject            business data
	 * @param context                   context
	 * @return     decision result. true--permit; false--deny, deny reasion be store in request with key DENY_REASON
	 */
	public static boolean permit(HttpServletRequest req, int privilegeId,
			Object businessObject, Map context) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		
		Decision decision = Ralasafe.permit(privilegeId, user,
				businessObject, context);
		if (!decision.isPermit()) {
			req.setAttribute(DENY_REASON, decision.getDenyReason());
		}
		return decision.isPermit();
	}

	/**
	 * Eval query policy, return query count.
	 * 
	 * @param req              HttpRequest, the login user be read from HttpSession with key USER_KEY 
	 * @param privilegeId
	 * @return     query count
	 */
	public static int queryCount(HttpServletRequest req, int privilegeId) {
		return queryCount(req, privilegeId, new HashMap());
	}

	/**
	 * Eval query policy, return query count.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param where              customized where condition
	 * @return             query count
	 */
	public static int queryCount(HttpServletRequest req, int privilegeId,
			CustomizedWhere where) {
		return queryCount(req, privilegeId, new HashMap(), where);
	}

	/**
	 * Eval query policy, return query count.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param context
	 * @return             query count
	 */
	public static int queryCount(HttpServletRequest req, int privilegeId,
			Map context) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		return Ralasafe.queryCount(privilegeId, user, context);
	}

	/**
	 * Eval query policy, return query count.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param context
	 * @param where               customized where condition
	 * @return 
	 */
	public static int queryCount(HttpServletRequest req, int privilegeId,
			Map context, CustomizedWhere where) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		return Ralasafe.queryCount(privilegeId, user, context, where);
	}

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param req      HttpRequest, the login user be read from HttpSession with key USER_KEY
	 * @param privilegeId
	 * @return   query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId) {
		return query(req, privilegeId, new HashMap());
	}

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param req      HttpRequest, the login user be read from HttpSession with key USER_KEY
	 * @param privilegeId
	 * @param where    customized where condition
	 * @return    query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			CustomizedWhere where) {
		return query(req, privilegeId, new HashMap(), where);
	}

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param req     HttpRequest, the login user be read from HttpSession with key USER_KEY
	 * @param privilegeId
	 * @param context           context
	 * @return     query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			Map context) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		QueryResult result = Ralasafe.query(privilegeId, user, context);
		req.setAttribute(FIELDS, result.getFields());
		req.setAttribute(READ_ONLY_FIELDS, result.getReadOnlyFields());
		return result.getData();
	}

	/**
	 * Eval query policy, return query result.
	 * 
	 * @param req      HttpRequest, the login user be read from HttpSession with key USER_KEY
	 * @param privilegeId
	 * @param context  context
	 * @param where    customized where condition
	 * @return     query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			Map context, CustomizedWhere where) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		QueryResult result = Ralasafe.query(privilegeId, user, context,
				where);
		req.setAttribute(FIELDS, result.getFields());
		req.setAttribute(READ_ONLY_FIELDS, result.getReadOnlyFields());
		return result.getData();
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param req	          
	 * @param privilegeId
	 * @param first    first index, count from 0,1,2...
	 * @param max      max returned records
	 * @return         query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			int first, int max) {
		Collection query = query(req, privilegeId, new HashMap(), first, max);
		return query;
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param where     customized where condtion
	 * @param first     first index, count from 0,1,2...
	 * @param max       max returned records
	 * @return          query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			CustomizedWhere where, int first, int max) {
		return query(req, privilegeId, new HashMap(), where, first, max);
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param context
	 * @param first         first index, count from 0,1,2...
	 * @param max           max returned records
	 * @return              query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			Map context, int first, int max) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		QueryResult result = Ralasafe.query(privilegeId, user, context,
				first, max);
		req.setAttribute(FIELDS, result.getFields());
		req.setAttribute(READ_ONLY_FIELDS, result.getReadOnlyFields());
		return result.getData();
	}

	/**
	 * Eval query policy, return query result pagination.
	 * 
	 * @param req
	 * @param privilegeId
	 * @param context
	 * @param where     customized where condition
	 * @param first     first index, count from 0,1,2...
	 * @param max       max returned records
	 * @return          query result, collection of yourbean
	 */
	public static Collection query(HttpServletRequest req, int privilegeId,
			Map context, CustomizedWhere where, int first, int max) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		// String name = WebUtil.getCurrentApplication(req).getName();
		QueryResult result = Ralasafe.query(privilegeId, user, context,
				where, first, max);
		req.setAttribute(FIELDS, result.getFields());
		req.setAttribute(READ_ONLY_FIELDS, result.getReadOnlyFields());
		return result.getData();
	}

	/**
	 * Does this user be granted for the privilege?
	 * 
	 * @param req
	 * @param privilegeId
	 * @return    true--be granted;   false--not be granted
	 */
	public static boolean hasPrivilege(HttpServletRequest req, int privilegeId) {
		User user = (User) req.getSession().getAttribute(USER_KEY);
		if (user == null) {
			return false;
		}
		return Ralasafe.hasPrivilege(privilegeId, user);
	}

	/**
	 * Set current login user into httpsession with key <code>USER_KEY</code>
	 * 
	 * @param req
	 * @param user          the login user
	 */
	public static void setCurrentUser(HttpServletRequest req, User user) {
		req.getSession().setAttribute(USER_KEY, user);
	}

	/**
	 * Get current login user from httpsession.
	 * 
	 * @param req
	 * @return    current login user
	 */
	public static User getCurrentUser(HttpServletRequest req) {
		return (User) req.getSession().getAttribute(USER_KEY);
	}

	/**
	 * Get current operation deny reason.
	 * 
	 * @param req
	 * @return        deny reason
	 */
	public static String getDenyReason(HttpServletRequest req) {
		return (String) req.getAttribute(DENY_REASON);
	}

	/**
	 * Get query result's fields info.
	 * 
	 * @param req
	 * @return      Collection< String > field names of your bean class
	 */
	public static Collection getFields(HttpServletRequest req) {
		return (Collection) req.getAttribute(FIELDS);
	}

	/**
	 * Get query result's readonly fields info.
	 * 
	 * @param req
	 * @return      Collection< String > readonly field names of your bean class
	 */
	public static Collection getReadOnlyFields(HttpServletRequest req) {
		return (Collection) req.getAttribute(READ_ONLY_FIELDS);
	}
	
	/**
	 * Get the user's privilege tree, often for display operate menu.
	 * @param req   
	 * @return     privilege tree(menu tree)
	 */
	public static Privilege getBusinessPrivilegeTree(HttpServletRequest req) {
		User user=getCurrentUser( req );
		return Ralasafe.getBusinessPrivilegeTree( user );
	}
}
