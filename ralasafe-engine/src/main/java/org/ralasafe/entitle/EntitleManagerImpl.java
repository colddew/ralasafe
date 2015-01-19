/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
import org.ralasafe.ResourceConstants;
import org.ralasafe.application.ApplicationManager;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.OrderPart;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.SingleValueComparator;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableDeletor;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.TableSaver;
import org.ralasafe.db.TableSelector;
import org.ralasafe.db.TableUpdator;
import org.ralasafe.db.WhereElement;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;
import org.ralasafe.userType.UserType;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class EntitleManagerImpl implements EntitleManager {
	private static Log log=LogFactory.getLog( EntitleManagerImpl.class );
	
	private String appName;
	private TableSaver decisionSaver;
	private TableSelector decisionSelector;
	private TableDeletor decisionDeletor;
	private TableUpdator decisionUpdator;
	private TableSaver querySaver;
	private TableSelector querySelector;
	private TableDeletor queryDeletor;
	private TableUpdator queryUpdator;
	private WhereElement decisionPvlgIdWhereEmt;
	private WhereElement queryPvlgIdWhereEmt;
	private SelectCondition decisionPvlgIdCdtn;
	private SelectCondition queryPvlgIdCdtn;
	private Table decisionTable;
	private Table queryTable;
	private WhereElement decisionUserCategoryIdWhereEmt;
	private WhereElement decisionBusinessDataIdWhereEmt;
	private WhereElement queryUserCategoryIdWhereEmt;
	private WhereElement queryQueryIdWhereEmt;

	public EntitleManagerImpl(String appName) {
		super();
		this.appName = appName;

		initDecision();
		initQuery();
	}

	private void initDecision() {
		// define table
		TableNewer newer = new TableNewer();
		newer.setTableName(appName + "_decision_entitlement");
		newer.setColumnNames(new String[] { "id", "privilegeId",
				"userCategoryId", "businessDataId", "effect", "denyReason" });
		newer.setIdColumnNames(new String[] { "id" });
		newer.setUniqueColumnNames(new String[] { "privilegeId",
				"userCategoryId", "businessDataId" });
		newer.setMappingClass(DecisionEntitlement.class.getName());
		newer.setColumnJavaBeanAttributeClazz(new String[] { "int", "int",
				"int", "int", "java.lang.String", "java.lang.String" });

		newer.setId(DBPower.getTableId(null, newer.getTableName()));

		decisionTable = newer.getTable();
		decisionSelector = new TableSelectorImpl();
		decisionSelector.setObjectNewer(new JavaBeanObjectNewer(newer
				.getMappingClass()));
		decisionSaver = new TableSaverImpl();
		decisionDeletor = new TableDeletorImpl();
		decisionUpdator = new TableUpdatorImpl();

		decisionSelector.setTable(decisionTable);
		decisionSaver.setTable(decisionTable);
		decisionDeletor.setTable(decisionTable);
		decisionUpdator.setTable(decisionTable);

		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(decisionTable.getColumns()[1]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);
		emt.setValue("privilegeId");

		decisionPvlgIdWhereEmt = emt;

		decisionPvlgIdCdtn = new SelectCondition();
		decisionPvlgIdCdtn.setWhereElement(decisionPvlgIdWhereEmt);
		OrderPart orderPart = new OrderPart();
		orderPart.setColumnNames(new String[] { "id" });
		decisionPvlgIdCdtn.setOrderPart(orderPart);
	}

	private void initQuery() {
		// define table
		TableNewer newer = new TableNewer();
		newer.setTableName(appName + "_query_entitlement");
		newer.setColumnNames(new String[] { "id", "privilegeId",
				"userCategoryId", "queryId", "description" });
		newer.setIdColumnNames(new String[] { "id" });
		newer.setUniqueColumnNames(new String[] { "privilegeId",
				"userCategoryId" });
		newer.setMappingClass(QueryEntitlement.class.getName());
		newer.put("id", new JavaBeanColumnAdapter("id", "int"));
		newer.put("privilegeId",
				new JavaBeanColumnAdapter("privilegeId", "int"));
		newer.put("userCategoryId", new JavaBeanColumnAdapter("userCategoryId",
				"int"));
		newer.put("queryId", new JavaBeanColumnAdapter("queryId", "int"));
		newer.put("description", new JavaBeanColumnAdapter("description",
				"java.lang.String"));
		newer.setId(DBPower.getTableId(null, newer.getTableName()));

		queryTable = newer.getTable();
		querySelector = new TableSelectorImpl();
		querySelector.setObjectNewer(new JavaBeanObjectNewer(newer
				.getMappingClass()));
		querySaver = new TableSaverImpl();
		queryDeletor = new TableDeletorImpl();
		queryUpdator = new TableUpdatorImpl();

		querySelector.setTable(queryTable);
		querySaver.setTable(queryTable);
		queryDeletor.setTable(queryTable);
		queryUpdator.setTable(queryTable);

		FieldWhereElement pvlgIdEmt = new FieldWhereElement();
		pvlgIdEmt.setColumn(queryTable.getColumns()[1]);
		pvlgIdEmt.setCompartor(SingleValueComparator.EQUAL);
		pvlgIdEmt.setContextValue(true);
		pvlgIdEmt.setValue("privilegeId");

		queryPvlgIdWhereEmt = pvlgIdEmt;

		queryPvlgIdCdtn = new SelectCondition();
		queryPvlgIdCdtn.setWhereElement(queryPvlgIdWhereEmt);
		OrderPart orderPart = new OrderPart();
		orderPart.setColumnNames(new String[] { "id" });
		queryPvlgIdCdtn.setOrderPart(orderPart);
	}

	public Collection getDecisionEntitlements(int pvlgId) {
		DecisionEntitlement hint = new DecisionEntitlement();
		hint.setPrivilegeId(pvlgId);

		Collection entitlements = decisionSelector.select(decisionPvlgIdCdtn,
				hint);

		UserCategoryManager ucMng = Factory.getUserCategoryManager(appName);
		BusinessDataManager businessDataManager = Factory
				.getBusinessDataManager(appName);

		for (Iterator iter = entitlements.iterator(); iter.hasNext();) {
			DecisionEntitlement entitle = (DecisionEntitlement) iter.next();
			UserCategory userCategory = ucMng.getUserCategory(entitle
					.getUserCategoryId());
			BusinessData businessData = businessDataManager
					.getBusinessData(entitle.getBusinessDataId());

			entitle.setUserCategory(userCategory);
			entitle.setBusinessData(businessData);
		}

		return entitlements;
	}

	public Collection getQueryEntitlements(int pvlgId) {
		QueryEntitlement hint = new QueryEntitlement();
		hint.setPrivilegeId(pvlgId);

		Collection entitlements = querySelector.select(queryPvlgIdCdtn, hint);

		UserCategoryManager ucMng = Factory.getUserCategoryManager(appName);
		QueryManager queryManager = Factory.getQueryManager(appName);

		for (Iterator iter = entitlements.iterator(); iter.hasNext();) {
			QueryEntitlement entitle = (QueryEntitlement) iter.next();
			UserCategory userCategory = ucMng.getUserCategory(entitle
					.getUserCategoryId());
			Query query = queryManager.getQuery(entitle.getQueryId());

			entitle.setUserCategory(userCategory);
			entitle.setQuery(query);
		}

		return entitlements;
	}

	public Decision permit(Locale locale, int privilegeId, User user,
			Map context) {
		Collection decisionEntitlements = getDecisionEntitlements(privilegeId);
		DecisionEntitlementTestResult result = testDecisionEntitlement(locale,
				privilegeId, new ArrayList(decisionEntitlements), user, context);
		Decision decision = null;
		if (result.isFailed()) {
			decision = new Decision();
			decision.setPermit(false);
			decision.setDenyReason(result.getErrorMessage());
		} else {
			decision = result.getDecision();
		}

		return decision;
	}

	/**
	 * Caculate rules by order. Return first match result. If no rule is matched, return deny.
	 * 
	 * @param decisionEntitlements
	 * @param user
	 * @param context
	 */
	private DecisionEntitlementTestResult decisionFirstApplicable(
			List decisionEntitlements, User user, Map context) {
		DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();

		Decision decision = new Decision();
		String denyReason = "";
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		BusinessDataManager businessDataManager = Factory
				.getBusinessDataManager(appName);

		for (Iterator iter = decisionEntitlements.iterator(); iter.hasNext();) {
			DecisionEntitlement entitlement = (DecisionEntitlement) iter.next();
			UserCategory userCategory = userCategoryManager
					.getUserCategory(entitlement.getUserCategoryId());
			// test userCategory
			UserCategoryTestResult userCategoryTestResult = userCategory.test(
					user, context, queryManager);
			result.getUserCategoryTestResults().add(userCategoryTestResult);
			
			if (userCategoryTestResult.isFailed()) {
				result.setFailed(true);
				result.setErrorMessage(userCategoryTestResult.getErrorMessage());
				result.getBusinessDataTestResults().add( new BusinessDataTestResult() );
				return result;
			}

//			if (entitlement.getEffect().equals(DecisionEntitlement.PERMIT)) {
//				if (!StringUtil.isEmpty(denyReason)) {
//					denyReason = denyReason + "\n";
//				}
//				denyReason = denyReason + entitlement.getDenyReason();
//			}

			if (userCategoryTestResult.isValid()) {
				BusinessData businessData = businessDataManager
						.getBusinessData(entitlement.getBusinessDataId());
				// test businessData
				BusinessDataTestResult businessDataTestResult = businessData
						.test(user, context, queryManager);
				result.getBusinessDataTestResults().add(businessDataTestResult);
				if (businessDataTestResult.isFailed()) {
					result.setFailed(true);
					result.setErrorMessage(businessDataTestResult.getErrorMessage());
					return result;
				}

				if (businessDataTestResult.isValid()) {
					if (entitlement.getEffect().equals(DecisionEntitlement.DENY)) {
						decision.setPermit(false);
						decision.setDenyReason(entitlement.getDenyReason());
					} else if (entitlement.getEffect().equals(DecisionEntitlement.PERMIT)) {
						decision.setPermit(true);
					}
					result.setFailed(false);
					result.setDecision(decision);
					return result;
				} else {
					if (entitlement.getEffect().equals(DecisionEntitlement.PERMIT)) {
						// append deny reason
						if (!StringUtil.isEmpty(denyReason)) {
							denyReason = denyReason + "\n";
						}
						denyReason = denyReason + entitlement.getDenyReason();
					}
				}
			} else {
				result.getBusinessDataTestResults().add( new BusinessDataTestResult() );
			}
		}

		// no rule is matched
		decision.setPermit(false);
		if (denyReason.equals("")) {
			decision.setDenyReason(Decision.DEFAULT_DENY_REASON);
		} else {
			decision.setDenyReason(denyReason);
		}
		result.setFailed(false);
		result.setDecision(decision);
		return result;
	}

	/**
	 * Deny override. Caculate all rules, return deny if any matched rule result is deny. 
	 * If all matched rules results are permit, then return permit.
	 * If no rule is matched, return deny.
	 * 
	 * 
	 * @param decisionEntitlements
	 * @param user
	 * @param context
	 */
	private DecisionEntitlementTestResult decisionOrderedDenyOverrides(
			List decisionEntitlements, User user, Map context) {
		DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();

		Decision decision = new Decision();
		String denyReason = "";
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		BusinessDataManager businessDataManager = Factory
				.getBusinessDataManager(appName);

		
		for (Iterator iter = decisionEntitlements.iterator(); iter.hasNext();) {
			DecisionEntitlement entitlement = (DecisionEntitlement) iter.next();
			UserCategory userCategory = userCategoryManager
					.getUserCategory(entitlement.getUserCategoryId());
			// test userCategory
			UserCategoryTestResult userCategoryTestResult = userCategory.test(
					user, context, queryManager);
			result.getUserCategoryTestResults().add(userCategoryTestResult);
			if (userCategoryTestResult.isFailed()) {
				result.setFailed(true);
				result
						.setErrorMessage(userCategoryTestResult
								.getErrorMessage());
				return result;
			}

			if (entitlement.getEffect().equals(DecisionEntitlement.PERMIT)) {
				if (!denyReason.equals("")) {
					denyReason = denyReason + "\n";
				}
				denyReason = denyReason + entitlement.getDenyReason();
			}

			if (userCategoryTestResult.isValid()) {
				// 
				BusinessData businessData = businessDataManager
						.getBusinessData(entitlement.getBusinessDataId());
				// test businessData
				BusinessDataTestResult businessDataTestResult = businessData
						.test(user, context, queryManager);
				result.getBusinessDataTestResults().add(businessDataTestResult);
				if (businessDataTestResult.isFailed()) {
					result.setFailed(true);
					result.setErrorMessage(businessDataTestResult
							.getErrorMessage());
					return result;
				}

				if (businessDataTestResult.isValid()) {
					if (entitlement.getEffect()
							.equals(DecisionEntitlement.DENY)) {
						decision.setPermit(false);
						decision.setDenyReason(entitlement.getDenyReason());
						result.setFailed(false);
						result.setDecision(decision);
						return result;
					} else if (entitlement.getEffect().equals(
							DecisionEntitlement.PERMIT)) {
						{
							decision.setPermit(true);
						}
					}
				}
			}
		}

		if (!decision.isPermit()) {
			if ("".equals(denyReason)) {
				decision.setDenyReason(Decision.DEFAULT_DENY_REASON);
			} else {
				decision.setDenyReason(denyReason);
			}
		}
		result.setFailed(false);
		result.setDecision(decision);
		return result;
	}

	/**
	 * Permit override. Caculate all rules, return permit if any matched rule result is permit. 
	 * If all matched rules results are deny, then return deny.
	 * If no rule is matched, return deny.
	 *  
	 * @param decisionEntitlements
	 * @param user
	 * @param context
	 */
	private DecisionEntitlementTestResult decisionOrderedPermitOverrides(
			List decisionEntitlements, User user, Map context) {
		DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();

		Decision decision = new Decision();
		String denyReason = "";
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		BusinessDataManager businessDataManager = Factory
				.getBusinessDataManager(appName);

		for (Iterator iter = decisionEntitlements.iterator(); iter.hasNext();) {
			DecisionEntitlement entitlement = (DecisionEntitlement) iter.next();
			UserCategory userCategory = userCategoryManager
					.getUserCategory(entitlement.getUserCategoryId());
			// test userCategory
			UserCategoryTestResult userCategoryTestResult = userCategory.test(
					user, context, queryManager);
			result.getUserCategoryTestResults().add(userCategoryTestResult);
			if (userCategoryTestResult.isFailed()) {
				result.setFailed(true);
				result
						.setErrorMessage(userCategoryTestResult
								.getErrorMessage());
				return result;
			}

			if (entitlement.getEffect().equals(DecisionEntitlement.PERMIT)) {
				if (!denyReason.equals("")) {
					denyReason = denyReason + "\n";
				}
				denyReason = denyReason + entitlement.getDenyReason();
			}

			if (userCategoryTestResult.isValid()) {
				// 
				BusinessData businessData = businessDataManager
						.getBusinessData(entitlement.getBusinessDataId());
				// test businessData
				BusinessDataTestResult businessDataTestResult = businessData
						.test(user, context, queryManager);
				result.getBusinessDataTestResults().add(businessDataTestResult);
				if (businessDataTestResult.isFailed()) {
					result.setFailed(true);
					result.setErrorMessage(businessDataTestResult
							.getErrorMessage());
					return result;
				}

				if (businessDataTestResult.isValid()) {
					if (entitlement.getEffect().equals(
							DecisionEntitlement.PERMIT)) {
						decision.setPermit(true);
						result.setFailed(false);
						result.setDecision(decision);
						return result;
					} else if (entitlement.getEffect().equals(
							DecisionEntitlement.DENY)
							&& decision.getDenyReason() == null) {
						{
							decision.setPermit(false);
							decision.setDenyReason(entitlement.getDenyReason());
						}
					}
				}
			}
		}

		decision.setPermit(false);
		if (decision.getDenyReason() == null) {
			if (denyReason.equals("")) {
				decision.setDenyReason(Decision.DEFAULT_DENY_REASON);
			} else {
				decision.setDenyReason(denyReason);
			}
		}
		result.setFailed(false);
		result.setDecision(decision);
		return result;
	}

	/**
	 * Execute Logic:
	 * <ol>
	 * 	<li>Which rule the user is first matched;</li>
	 *  <li>Execute the matched rule's query;</li>
	 *  <li>Return query result.</li>
	 * </ol>
	 */
	public QueryResult query(int pvlgId, User user, Map ctx) {
		if (!hasPrivilege(user, pvlgId)) {
			return new QueryResult();
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return new QueryResult();
		} else {
			return query.execute(user, ctx);
		}
	}

	public DecisionEntitlement addDecisionEntitlement(
			DecisionEntitlement decisionEntitlement)
			throws EntityExistException {
		decisionEntitlement.setId(newDecisionEntitlementId());
		decisionSaver.save(decisionEntitlement);

		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		BusinessDataManager businessDataManager = Factory
				.getBusinessDataManager(appName);
		UserCategory userCategory = userCategoryManager
				.getUserCategory(decisionEntitlement.getUserCategoryId());
		BusinessData businessData = businessDataManager
				.getBusinessData(decisionEntitlement.getBusinessDataId());

		decisionEntitlement.setBusinessData(businessData);
		decisionEntitlement.setUserCategory(userCategory);
		return decisionEntitlement;
	}

	private int newDecisionEntitlementId() {
		try {
			return DBUtil.getSequenceNextVal(decisionTable, "id");
		} catch (SQLException e) {
			throw new DBLevelException(e);
		}
	}

	private int newQueryEntitlementId() {
		try {
			return DBUtil.getSequenceNextVal(queryTable, "id");
		} catch (SQLException e) {
			throw new DBLevelException(e);
		}
	}

	public QueryEntitlement addQueryEntitlement(
			QueryEntitlement queryEntitlement) throws EntityExistException {
		queryEntitlement.setId(newQueryEntitlementId());
		querySaver.save(queryEntitlement);

		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategory userCategory = userCategoryManager
				.getUserCategory(queryEntitlement.getUserCategoryId());
		Query query = queryManager.getQuery(queryEntitlement.getQueryId());

		queryEntitlement.setUserCategory(userCategory);
		queryEntitlement.setQuery(query);

		return queryEntitlement;
	}

	public void deleteDecisionEntitlement(int decisionEntitlementId) {
		DecisionEntitlement hint = new DecisionEntitlement();
		hint.setId(decisionEntitlementId);

		decisionDeletor.deleteByIdColumns(hint);
	}

	public void deleteQueryEntitlement(int queryEntitlementId) {
		QueryEntitlement hint = new QueryEntitlement();
		hint.setId(queryEntitlementId);

		queryDeletor.deleteByIdColumns(hint);
	}

	public void updateDecisionEntitlement(
			DecisionEntitlement decisionEntitlement)
			throws EntityExistException {
		decisionUpdator.updateByIdColumns(decisionEntitlement);
	}

	public void updateQueryEntitlement(QueryEntitlement queryEntitlement)
			throws EntityExistException {
		queryUpdator.updateByIdColumns(queryEntitlement);
	}

	public void deleteCascadeEntitlementByUserCategory(int userCategoryId) {
		DecisionEntitlement decisionHint = new DecisionEntitlement();
		decisionHint.setUserCategoryId(userCategoryId);
		if (decisionUserCategoryIdWhereEmt == null) {
			newDecisionUserCategoryIdWhereEmt();
		}

		decisionDeletor.delete(decisionUserCategoryIdWhereEmt, decisionHint);

		QueryEntitlement queryHint = new QueryEntitlement();
		queryHint.setUserCategoryId(userCategoryId);
		if (queryUserCategoryIdWhereEmt == null) {
			newQueryUserCategoryIdWhereEmt();
		}

		queryDeletor.delete(queryUserCategoryIdWhereEmt, queryHint);
	}

	private void newDecisionUserCategoryIdWhereEmt() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(decisionTable.getColumns()[2]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);
		emt.setValue("userCategoryId");
		decisionUserCategoryIdWhereEmt = emt;
	}

	private void newDecisionBusinessDataIdWhereEmt() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(decisionTable.getColumns()[3]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);
		emt.setValue("businessDataId");
		decisionBusinessDataIdWhereEmt = emt;
	}

	private void newQueryUserCategoryIdWhereEmt() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(queryTable.getColumns()[2]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);
		emt.setValue("userCategoryId");
		queryUserCategoryIdWhereEmt = emt;
	}

	private void newQueryQueryIdWhereEmt() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(queryTable.getColumns()[3]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);
		emt.setValue("queryId");
		queryQueryIdWhereEmt = emt;
	}

	public void deleteCascadeEntitlementByBusinessData(int businessDataId) {
		DecisionEntitlement decisionHint = new DecisionEntitlement();
		decisionHint.setBusinessDataId(businessDataId);
		if (decisionBusinessDataIdWhereEmt == null) {
			newDecisionBusinessDataIdWhereEmt();
		}

		decisionDeletor.delete(decisionBusinessDataIdWhereEmt, decisionHint);
	}

	public void deleteCascadeEntitlementByQuery(int queryId) {
		QueryEntitlement queryHint = new QueryEntitlement();
		queryHint.setQueryId(queryId);
		if (queryQueryIdWhereEmt == null) {
			newQueryQueryIdWhereEmt();
		}

		queryDeletor.delete(queryQueryIdWhereEmt, queryHint);
	}

	private void deleteEntitlementsByPrivilege(int privilegeId) {
		// delete decisionEntitlements
		DecisionEntitlement decisionHint = new DecisionEntitlement();
		decisionHint.setPrivilegeId(privilegeId);
		decisionDeletor.delete(decisionPvlgIdWhereEmt, decisionHint);

		// delete queryEntitlements
		QueryEntitlement queryHint = new QueryEntitlement();
		queryHint.setPrivilegeId(privilegeId);
		queryDeletor.delete(queryPvlgIdWhereEmt, queryHint);
	}

	public QueryResult query(int pvlgId, User user, Map ctx,
			CustomizedWhere where) {
		if (!hasPrivilege(user, pvlgId)) {
			return new QueryResult();
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return new QueryResult();
		} else {
			return query.execute(user, ctx, where);
		}
	}

	private Query findQueryMatched(int pvlgId, User user, Map ctx) {
		Collection queryEntitlements = getQueryEntitlements(pvlgId);
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);

		// return the query of first matched rule
		for (Iterator iter = queryEntitlements.iterator(); iter.hasNext();) {
			QueryEntitlement entitlement = (QueryEntitlement) iter.next();
			UserCategory userCategory = userCategoryManager
					.getUserCategory(entitlement.getUserCategoryId());

			UserCategoryTestResult userCategoryTestResult = userCategory.test(
					user, ctx, queryManager);

			if (userCategoryTestResult.isFailed()) {
				String errorMessage=userCategoryTestResult
						.getErrorMessage();
				log.error( errorMessage );
				throw new RalasafeException(errorMessage);
			}

			if (userCategoryTestResult.isValid()) {
				return entitlement.getQuery();
			}
		}
		return null;
	}

	/**
	 * Find query of first matched rule. 
	 * 
	 * @return  In two suituation returns null. 1, Exception happens when test usercategory;
	 *          2, No rule is matched.
	 *          In these suituations, set result attributes: result.isFailed=true, errorMessage
	 */
	private Query findQueryMatched(Locale locale, Collection queryEntitlements,
			User user, Map ctx, QueryEntitlementTestResult result) {
		QueryManager queryManager = Factory.getQueryManager(appName);
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		
		for (Iterator iter = queryEntitlements.iterator(); iter.hasNext();) {
			QueryEntitlement entitlement = (QueryEntitlement) iter.next();
			UserCategory userCategory = userCategoryManager
					.getUserCategory(entitlement.getUserCategoryId());

			UserCategoryTestResult userCategoryTestResult = userCategory.test(
					user, ctx, queryManager);
			result.getUserCategoryTestResults().add(userCategoryTestResult);

			if (userCategoryTestResult.isFailed()) {
				result.setFailed(true);
				result
						.setErrorMessage(userCategoryTestResult
								.getErrorMessage());
				return null;
			}

			if (userCategoryTestResult.isValid()) {
				return queryManager.getQuery(entitlement.getQueryId());
			}
		}

		result.setFailed(true);
		result.setErrorMessage(Util.getMessage(locale,
				ResourceConstants.NO_QUERY_POLICY_MATCHED));
		return null;
	}

	public int queryCount(int pvlgId, User user, Map ctx) {
		if (!hasPrivilege(user, pvlgId)) {
			return 0;
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return 0;
		} else {
			return query.executeCount(user, ctx);
		}
	}

	public int queryCount(int pvlgId, User user, Map ctx, CustomizedWhere where) {
		if (!hasPrivilege(user, pvlgId)) {
			return 0;
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return 0;
		} else {
			return query.executeCount(user, ctx, where);
		}
	}

	public QueryResult query(int pvlgId, User user, Map ctx, int first, int max) {
		if (!hasPrivilege(user, pvlgId)) {
			return new QueryResult();
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return new QueryResult();
		} else {
			return query.execute(user, ctx, first, max);
		}
	}

	public QueryResult query(int pvlgId, User user, Map ctx,
			CustomizedWhere where, int first, int max) {
		if (!hasPrivilege(user, pvlgId)) {
			return new QueryResult();
		}

		Query query = findQueryMatched(pvlgId, user, ctx);
		if (query == null) {
			return new QueryResult();
		} else {
			return query.execute(user, ctx, where, first, max);
		}
	}

	public void addEntitlements(int privilegeId,
			Collection decisionEntitlements, Collection queryEntitlements) {
		// delete all existing entitlements
		deleteEntitlementsByPrivilege(privilegeId);

		// add new decisionEntitlements
		addDecisionEntitlements(privilegeId, decisionEntitlements);

		// add new queryEntitlements
		addQueryEntitlements(privilegeId, queryEntitlements);
	}

	private void addDecisionEntitlements(int privilegeId,
			Collection decisionEntitlements) {
		Iterator itr = decisionEntitlements.iterator();
		while (itr.hasNext()) {
			DecisionEntitlement item = (DecisionEntitlement) itr.next();
			item.setPrivilegeId(privilegeId);
			item.setId(newDecisionEntitlementId());
		}
		decisionSaver.batchSave(decisionEntitlements);
	}

	private void addQueryEntitlements(int privilegeId,
			Collection queryEntitlements) {
		Iterator itr = queryEntitlements.iterator();
		while (itr.hasNext()) {
			QueryEntitlement item = (QueryEntitlement) itr.next();
			item.setPrivilegeId(privilegeId);
			item.setId(newQueryEntitlementId());
		}
		querySaver.batchSave(queryEntitlements);
	}

	private boolean hasPrivilege(User user, int pvlgId) {
		PrivilegeManager pvlgMng = Factory.getPrivilegeManager(appName);
		Privilege privilege = pvlgMng.getPrivilege(pvlgId);
		if (privilege == null) {
			String msg="No privilege with id='" + pvlgId
					+ "' found.";
			log.error( msg );
			throw new RalasafeException(msg);
		}

		ApplicationManager applicationManager = Factory.getApplicationManager();
		// First UserType
		UserType userType = (UserType) applicationManager.getApplication(
				appName).getUserTypes().iterator().next();
		UserRoleManager userRoleManager = Factory.getUserRoleManager(appName,
				userType.getName());
		
		Object userId=null;
		if( user!=null ) {
			userId=user.get(User.idFieldName);
		}
		return userRoleManager.hasPrivilege(userId, pvlgId);
	}

	public DecisionEntitlementTestResult testDecisionEntitlement(Locale locale,
			int privilegeId, List decisionEntitlements, User user,
			Map context) {
		try {
			PrivilegeManager privilegeManager = Factory
					.getPrivilegeManager(appName);
			Privilege privilege = privilegeManager.getPrivilege(privilegeId);

			if (privilege == null) {
				DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();
				Decision decision = new Decision();
				decision.setPermit(false);
				decision.setDenyReason("No privilege with id='" + privilegeId
						+ "' found.");
				result.setDecision(decision);
				return result;
			}

			if (!hasPrivilege(user, privilegeId)) {
				DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();
				Decision decision = new Decision();
				decision.setPermit(false);
				decision.setDenyReason(Util.getMessage(locale,
						ResourceConstants.NO_PRIVILEGE, privilege.getName()));
				result.setDecision(decision);
				return result;
			}

			if (privilege.getDecisionPolicyCombAlg() == Privilege.DECISION_ORDERED_PERMIT_OVERRIDES) {
				return decisionOrderedPermitOverrides(decisionEntitlements,
						user, context);
			} else if (privilege.getDecisionPolicyCombAlg() == Privilege.DECISION_ORDERED_DENY_OVERRIDES) {
				return decisionOrderedDenyOverrides(decisionEntitlements, user,
						context);
			} else if (privilege.getDecisionPolicyCombAlg() == Privilege.DECISION_FIRST_APPLICABLE) {
				return decisionFirstApplicable(decisionEntitlements, user,
						context);
			} else {
				throw new RalasafeException(privilege
						.getDecisionPolicyCombAlg()
						+ " is an invalid DecisionPolicyCombiningAlgorithm");
			}
		} catch (Exception e) {
			DecisionEntitlementTestResult result = new DecisionEntitlementTestResult();
			result.setFailed(true);
			result.setErrorMessage(e.getMessage());
			return result;
		}
	}

	public QueryEntitlementTestResult testQueryEntitlement(Locale locale,
			int privilegeId, List queryEntitlements, User user,
			Map context, int first, int max) {
		QueryEntitlementTestResult result = new QueryEntitlementTestResult();
		try {
			PrivilegeManager privilegeManager = Factory
					.getPrivilegeManager(appName);
			Privilege privilege = privilegeManager.getPrivilege(privilegeId);

			if (privilege == null) {
				result.setFailed(true);
				result.setErrorMessage("No privilege with id='" + privilegeId
						+ "' found.");
				return result;
			}

			if (!hasPrivilege(user, privilegeId)) {
				result.setFailed(true);
				result.setErrorMessage(Util.getMessage(locale,
						ResourceConstants.NO_PRIVILEGE_FOR_QUERY_POLICY,
						privilege.getName()));
				return result;
			}

			Query query = findQueryMatched(locale, queryEntitlements, user,
					context, result);
			if (result.isFailed()) {
				return result;
			} else {
				result.setMatchedQuery(query);
				QueryTestResult queryTestResult = query.test(user, context,
						first, max);
				result.setQueryTestResults(queryTestResult);

				if (queryTestResult.isFailed()) {
					result.setFailed(true);
					result.setErrorMessage(queryTestResult.getErrorMessage());
					return result;
				} else {
					result.setFailed(false);
					return result;
				}
			}

		} catch (Exception e) {
			result.setFailed(true);
			result.setErrorMessage(e.getMessage());
			return result;
		}
	}

	public Query getQuery(int privilegeId, User user, Map context) {
		Query query = findQueryMatched(privilegeId, user, context);
		if (query != null) {
			QueryManager queryManager = Factory.getQueryManager(appName);
			query = queryManager.cloneQuery(query);
		}
		return query;
	}
}
