/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.user.User;

public interface EntitleManager {

	/**
	 * 
	 * @param pvlgId
	 * @return Collection< {@link DecisionEntitlement} >
	 */
	public abstract Collection getDecisionEntitlements(int pvlgId);

	/**
	 * 
	 * @param pvlgId
	 * @return Collection< {@link QueryEntitlement} >
	 */
	public abstract Collection getQueryEntitlements(int pvlgId);

	/**
	 * Check this user has fine grained(data level) access to this given data(in context).
	 * If this privilege is a normal one, check this user has function access to this privilege first!
	 * @param locale
	 * @param pvlgId
	 * @param user
	 * @param ctx
	 * @return
	 */
	public abstract Decision permit(Locale locale, int pvlgId, User user,
			Map ctx);

	/**
	 * Execute for this user, return fine grained(data level, row and column level) data.
	 * If this user has no privilege to execute privilege, return empty Collection. 
	 * @param pvlgId
	 * @param user
	 * @param ctx
	 * @return  Collection< YourJavaBean >
	 */
	public abstract QueryResult query(int pvlgId, User user, Map ctx);

	/**
	 * Execute for this user, return fine grained(data level, row and column level) data.
	 * If this user has no privilege to execute privilege, return empty Collection. 
	 * @param pvlgId
	 * @param user
	 * @param ctx
	 * @param where     Customized where conditions
	 * @return  Collection< YourJavaBean >
	 */
	public abstract QueryResult query(int pvlgId, User user, Map ctx,
			CustomizedWhere where);

	/**
	 * Query by pagnation.
	 * 
	 * @param first           Return from this index, index counts from 0 
	 * @param max             The max records be returned
	 */
	public abstract QueryResult query(int pvlgId, User user, Map ctx,
			int first, int max);

	public abstract QueryResult query(int pvlgId, User user, Map ctx,
			CustomizedWhere where, int first, int max);

	public abstract int queryCount(int pvlgId, User user, Map ctx);

	public abstract int queryCount(int pvlgId, User user, Map ctx,
			CustomizedWhere where);

	public abstract void addEntitlements(int privilegeId,
			Collection decisionEntitlements, Collection queryEntitlements);

	public abstract QueryEntitlement addQueryEntitlement(
			QueryEntitlement queryEntitlement) throws EntityExistException;

	public abstract void deleteQueryEntitlement(int queryEntitlementId);

	public abstract void updateQueryEntitlement(
			QueryEntitlement queryEntitlement) throws EntityExistException;

	public abstract DecisionEntitlement addDecisionEntitlement(
			DecisionEntitlement decisionEntitlement)
			throws EntityExistException;

	public abstract void updateDecisionEntitlement(
			DecisionEntitlement decisionEntitlement)
			throws EntityExistException;

	public abstract void deleteDecisionEntitlement(int decisionEntitlementId);

	public void deleteCascadeEntitlementByUserCategory(int userCategoryId);

	public void deleteCascadeEntitlementByBusinessData(int businessDataId);

	public void deleteCascadeEntitlementByQuery(int queryId);

	public abstract DecisionEntitlementTestResult testDecisionEntitlement(
			Locale locale, int privilegeId, List decisionEntitlements,
			User user, Map context);

	public abstract QueryEntitlementTestResult testQueryEntitlement(Locale locale,
			int privilegeId, List queryEntitlements, User user,
			Map context, int first, int max);

	public abstract Query getQuery(int privilegeId, User user, Map context);
}
