/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.db.ComplexWhereElement;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.LWhereElement;
import org.ralasafe.db.SingleValueComparator;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.TableUpdator;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.metadata.user.TableMetadata;
import org.ralasafe.metadata.user.UserMetadata;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.user.User;
import org.ralasafe.userType.UserType;
import org.ralasafe.userType.UserTypeManager;
import org.ralasafe.util.DBUtil;

public class ApplicationManagerImpl implements ApplicationManager {
	private static Log logger = LogFactory.getLog(ApplicationManagerImpl.class);

	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdator updator;
	private TableDeletorImpl deletor;
	private FieldWhereElement appNameWhereEmt;

	private TableSaverImpl applicationUserTypeSaver;
	private TableSelectorImpl applicationUserTypeSelector;
	private TableDeletorImpl applicationUserTypeDeletor;
	private FieldWhereElement appNameUserTypeTableWhereEmt;
	private ComplexWhereElement appNameUserTypeNameUserTypeTableWhereEmt;

	private Map store = new HashMap();
	private boolean changed = true;

	public ApplicationManagerImpl() {
		initApplicationTableInfo();
		initApplicationUserTypeTableInfo();
	}

	private void initApplicationTableInfo() {
		TableNewer newer = new TableNewer();
		newer.setTableName("application");
		newer.setColumnNames(new String[] { "name", "description" });
		newer.setIdColumnNames(new String[] { "name" });
		// newer.setUniqueColumnNames(new String[] { "name" });
		newer.setMappingClass(Application.class.getName());
		newer
				.put("name", new JavaBeanColumnAdapter("name",
						"java.lang.String"));
		newer.put("description", new JavaBeanColumnAdapter("description",
				"java.lang.String"));

		newer.setId(DBPower.getTableId(null, newer.getTableName()));
		table = newer.getTable();
		selector = new TableSelectorImpl();
		selector
				.setObjectNewer(new JavaBeanObjectNewer(newer.getMappingClass()));
		saver = new TableSaverImpl();
		updator = new TableUpdatorImpl();
		deletor = new TableDeletorImpl();
		selector.setTable(table);
		saver.setTable(table);
		updator.setTable(table);
		deletor.setTable(table);

		appNameWhereEmt = new FieldWhereElement();
		appNameWhereEmt.setColumn(table.getColumns()[0]);
		appNameWhereEmt.setCompartor(SingleValueComparator.EQUAL);
		appNameWhereEmt.setContextValue(true);
	}

	private void initApplicationUserTypeTableInfo() {
		TableNewer newer = new TableNewer();
		newer.setTableName("applicationusertype");
		newer.setColumnNames(new String[] { "appName", "userTypeName",
				"userMetadataStr" });
		newer.setIdColumnNames(new String[] { "appName", "userTypeName" });
		// newer.setUniqueColumnNames(new String[] { "name" });
		newer.setMappingClass(ApplicationUserType.class.getName());
		newer.put("appName", new JavaBeanColumnAdapter("appName",
				"java.lang.String"));
		newer.put("userTypeName", new JavaBeanColumnAdapter("userTypeName",
				"java.lang.String"));
		newer.put("userMetadataStr", new JavaBeanColumnAdapter(
				"userMetadataStr", "java.lang.String"));

		newer.setId(DBPower.getTableId(null, newer.getTableName()));
		Table appUserTypeTable = newer.getTable();
		applicationUserTypeSelector = new TableSelectorImpl();
		applicationUserTypeSelector.setObjectNewer(new JavaBeanObjectNewer(
				newer.getMappingClass()));
		applicationUserTypeSaver = new TableSaverImpl();
		// updator = new TableUpdatorImpl();
		applicationUserTypeDeletor = new TableDeletorImpl();
		applicationUserTypeSelector.setTable(appUserTypeTable);
		applicationUserTypeSaver.setTable(appUserTypeTable);
		// updator.setTable(table);
		applicationUserTypeDeletor.setTable(appUserTypeTable);

		appNameUserTypeTableWhereEmt = new FieldWhereElement();
		appNameUserTypeTableWhereEmt
				.setColumn(appUserTypeTable.getColumns()[0]);
		appNameUserTypeTableWhereEmt.setCompartor(SingleValueComparator.EQUAL);
		appNameUserTypeTableWhereEmt.setContextValue(true);

		appNameUserTypeNameUserTypeTableWhereEmt = new ComplexWhereElement();
		FieldWhereElement emt1 = new FieldWhereElement();
		emt1.setColumn(appUserTypeTable.getColumns()[0]);
		emt1.setCompartor(SingleValueComparator.EQUAL);
		emt1.setContextValue(true);
		FieldWhereElement emt2 = new FieldWhereElement();
		emt2.setColumn(appUserTypeTable.getColumns()[1]);
		emt2.setCompartor(SingleValueComparator.EQUAL);
		emt2.setContextValue(true);

		appNameUserTypeNameUserTypeTableWhereEmt.setFirstPart(emt1);
		LWhereElement lemt = new LWhereElement();
		lemt.setLinkType(LWhereElement.AND_LINK_TYPE);
		lemt.setWhereElement(emt2);
		LWhereElement[] linkedParts = new LWhereElement[] { lemt };
		appNameUserTypeNameUserTypeTableWhereEmt.setLinkedParts(linkedParts);
	}

	private synchronized void loadIntoMemory() {
		if (!changed) {
			return;
		}

		Collection allApps = selector.select(null, null);
		Collection allAppUserTypes = applicationUserTypeSelector.select(null,
				null);

		store.clear();

		for (Iterator iter = allApps.iterator(); iter.hasNext();) {
			Application app = (Application) iter.next();
			store.put(app.getName(), app);
		}

		for (Iterator iter = allAppUserTypes.iterator(); iter.hasNext();) {
			ApplicationUserType entry = (ApplicationUserType) iter.next();
			String appName = entry.getAppName();

			UserType userType = extractUserType(entry);

			Application app = (Application) store.get(appName);
			app.getUserTypes().add(userType);
		}
		changed = false;
	}

	public void addApplication(Locale locale, Application app)
			throws EntityExistException {
		loadIntoMemory();

		// Already exist?
		if (store.keySet().contains(app.getName())) {
			throw new EntityExistException("The name '" + app.getName()
					+ "' already exists.");
		}

		Connection conn = null;
		boolean commitMode = true;
		try {
			conn = DBPower.getConnection(table.getId());
			commitMode = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// save application info
			saver.save(conn, app);

			// save usertypes info
			Collection userTypes = app.getUserTypes();
			if (userTypes != null && userTypes.size() > 0) {
				List appUserTypes = new ArrayList(userTypes.size());
				for (Iterator iter = userTypes.iterator(); iter.hasNext();) {
					UserType userType = (UserType) iter.next();

					ApplicationUserType appUserType = new ApplicationUserType();
					appUserType.setAppName(app.getName());
					appUserType.setUserTypeName(userType.getName());
					String produceUserMetadataStr = produceUserMetadataStr(userType
							.getUserMetadata());
					appUserType.setUserMetadataStr(produceUserMetadataStr);

					appUserTypes.add(appUserType);
				}

				applicationUserTypeSaver.batchSave(conn, appUserTypes);
			}

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw new DBLevelException(e);
		} finally {
			DBUtil.setCommitMode(conn, commitMode);
			DBUtil.close(conn);
		}

		changed = true;

		// create tables
		createTablesForApp(app);

		Iterator itr = app.getUserTypes().iterator();
		while (itr.hasNext()) {
			UserType userType = (UserType) itr.next();
			// create reserved query
			QueryManager queryManager = Factory.getQueryManager(app.getName());
			queryManager.addReservedQuery(userType.getName());

			// create reserved usercategory
			UserCategoryManager userCategoryManager = Factory
					.getUserCategoryManager(app.getName());
			userCategoryManager.addReservedUserCategory(locale);

			// create reserved privilege
			PrivilegeManager privilegeManager = Factory.getPrivilegeManager(app
					.getName());
			privilegeManager.addReservedPrivilege(locale);

			// create reserved roles
			RoleManager roleManager = Factory.getRoleManager(app.getName());
			roleManager.addReservedRole(locale);
		}
	}

	private void createTablesForApp(Application app) {

		// Name rule for role, privilege and roleprivilege is:
		//   <appName>_role, <appName>_privilege and <appName>_roleprivilege
		String appName = app.getName();
		String sqlRole = DBUtil.roleTableCreateSql(appName);
		String sqlPrivilege = DBUtil.privilegeTableCreateSql(appName);
		String sqlRolePrivilege = DBUtil.rolePrivilegeTableCreateSql(appName);
		
		// Each usertype has it's own userrole table. Name rule is:
		// <appName>_<userTypeName>_userrole
		ArrayList sqlUserRoles = new ArrayList();
		Iterator itr = app.getUserTypes().iterator();
		while (itr.hasNext()) {
			UserType userType = (UserType) itr.next();
			userType = Factory.getUserTypeManager().getUserType(
					userType.getName());
			TableMetadata userTable = userType.getUserMetadata()
					.getMainTableMetadata();
			String sqlUserRole = DBUtil.userRoleTableCreateSql(appName,
					userType.getName(), User.idFieldName,
					getIdColumnType(userTable));
			sqlUserRoles.add(sqlUserRole);
		}

		String tableQueryCreateSql = DBUtil.tableQueryCreateSql(appName);
		String tableUserCategoryCreateSql = DBUtil
				.tableUserCategoryCreateSql(appName);
		String tableDicisionEntitlementCreateSql = DBUtil
				.tableDecisionEntitlementCreateSql(appName);
		String tableQueryEntitlementCreateSql = DBUtil
				.tableQueryEntitlementCreateSql(appName);
		String tableBusinessDataCreateSql = DBUtil
				.tableBusinessDataCreateSql(appName);
	
		Connection conn = DBPower.getConnection(DBPower.getDefaultDsName());
		try {
			// conn.setAutoCommit(false);
			// create tables
			DBUtil.exec(conn, sqlRole);
			DBUtil.exec(conn, sqlPrivilege);
			DBUtil.exec(conn, sqlRolePrivilege);
			itr = sqlUserRoles.iterator();
			while (itr.hasNext()) {
				DBUtil.exec(conn, (String) itr.next());
			}
			DBUtil.exec(conn, tableQueryCreateSql);
			DBUtil.exec(conn, tableUserCategoryCreateSql);
			DBUtil.exec(conn, tableDicisionEntitlementCreateSql);
			DBUtil.exec(conn, tableQueryEntitlementCreateSql);
			DBUtil.exec(conn, tableBusinessDataCreateSql);
			// conn.commit();
		} catch (SQLException e) {
			// try {
			// conn.rollback();
			// } catch (SQLException e1) {
			// throw new DBLevelException(e1);
			// }
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public void addApplicationUserType(Locale locale, String appName,
			UserType userType) {
		loadIntoMemory();

		// 1. save ApplicationUserType info
		ApplicationUserType appUserType = new ApplicationUserType();
		appUserType.setAppName(appName);
		appUserType.setUserTypeName(userType.getName());
		String produceUserMetadataStr = produceUserMetadataStr(userType
				.getUserMetadata());
		appUserType.setUserMetadataStr(produceUserMetadataStr);

		try {
			applicationUserTypeSaver.save(appUserType);
		} catch (EntityExistException e2) {
			throw new DBLevelException(e2);
		}

		changed = true;

		// 2. create related userrole tables of application's userType
		Connection conn = DBPower.getConnection(DBPower.getDefaultDsName());
		try {
			conn.setAutoCommit(false);
			userType = Factory.getUserTypeManager().getUserType(
					userType.getName());
			TableMetadata userTable = userType.getUserMetadata()
					.getMainTableMetadata();
			DBUtil.exec(conn, DBUtil.userRoleTableCreateSql(appName, userType
					.getName(), User.idFieldName, getIdColumnType(userTable)));
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new DBLevelException(e1);
			}
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(conn);
		}

		// 3. create reserved query
		QueryManager queryManager = Factory.getQueryManager(appName);
		queryManager.addReservedQuery(userType.getName());

		// 4. create reserved usercategory
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		userCategoryManager.addReservedUserCategory(locale);

		// 5. create reserved privilege
		PrivilegeManager privilegeManager = Factory
				.getPrivilegeManager(appName);
		privilegeManager.addReservedPrivilege(locale);

		// 6. create reserved roles
		RoleManager roleManager = Factory.getRoleManager(appName);
		roleManager.addReservedRole(locale);
	}

	private String getIdColumnType(TableMetadata userTable) {
		String idColumnType = null;
		FieldMetadata[] fields = userTable.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(User.idFieldName)) {
				idColumnType = fields[i].getSqlType();
				break;
			}
		}
		return idColumnType;
	}

	// private void addApplicationUserType(String appName, UserType userType,
	// Session session) {
	// ApplicationUserType appUserType = new ApplicationUserType();
	// ApplicationUserTypeId id = new ApplicationUserTypeId(appName, userType
	// .getName());
	// appUserType.setId(id);
	// appUserType.setUserMetadataStr(produceUserMetadataStr(userType
	// .getUserMetadata()));
	// session.save(appUserType);
	// }

	private String produceUserMetadataStr(UserMetadata userMetadata) {
		StringBuffer buf = new StringBuffer();
		FieldMetadata[] fields = userMetadata.getMainTableMetadata()
				.getFields();
		for (int i = 0; i < fields.length; i++) {
			buf.append(fields[i].getColumnName() + " ");
		}
		return buf.toString();
	}

	private UserType extractUserType(ApplicationUserType appUserType) {
		String[] columNames = appUserType.getUserMetadataStr().split(" ");
		UserTypeManager userTypeManager = Factory.getUserTypeManager();
		UserType userType = userTypeManager.getUserTypeCopy(appUserType
				.getUserTypeName());
		FieldMetadata[] fields = userType.getUserMetadata()
				.getMainTableMetadata().getFields();
		// find usertype infos which defined in usertype definition
		ArrayList newFields = new ArrayList();
		for (int i = 0; i < columNames.length; i++) {
			for (int j = 0; j < fields.length; j++) {
				if (columNames[i].equals(fields[j].getColumnName())) {
					newFields.add(fields[j]);
				}
			}
		}
		userType.getUserMetadata().getMainTableMetadata().setFields(
				(FieldMetadata[]) newFields.toArray(new FieldMetadata[newFields
						.size()]));
		return userType;
	}

	public void deleteApplication(String name) {
		Application app = getApplication(name);

		// delete infos
		Connection conn = null;
		boolean autoCommit = true;
		try {
			conn = DBPower.getConnection(table.getId());
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			ApplicationUserType appUserType = new ApplicationUserType();
			appUserType.setAppName(name);
			applicationUserTypeDeletor.delete(conn,
					appNameUserTypeTableWhereEmt, appUserType);

			Application hint = new Application();
			hint.setName(name);
			deletor.delete(conn, appNameWhereEmt, hint);

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw new DBLevelException(e);
		} finally {
			DBUtil.setCommitMode(conn, autoCommit);
			DBUtil.close(conn);
		}

		changed = true;

		// delete tables
		deleteTablesForApp(app);

		// notify Factory
		org.ralasafe.Factory.applicationChanged(app.getName());
	}

	private void deleteTablesForApp(Application app) {
		// Name rule for role, privilegeand roleprivilege table is:
		// <appName>_role,<appName>_privilege and<appName>_roleprivilege
		String appName = app.getName();
		String sqlRole = DBUtil.roleTableDropSql(appName);
		String sqlPrivilege = DBUtil.privilegeTableDropSql(appName);
		String sqlRolePrivilege = DBUtil.rolePrivilegeTableDropSql(appName);
		
		// userrole tables
		ArrayList sqlUserRoles = new ArrayList();
		Iterator itr = app.getUserTypes().iterator();
		while (itr.hasNext()) {
			UserType userType = (UserType) itr.next();
			String sqlUserRole = DBUtil.userRoleTableDropSql(appName, userType
					.getName());
			sqlUserRoles.add(sqlUserRole);
		}

		String tableQueryDropSql = DBUtil.tableQueryDropSql(appName);
		String tableUserCategoryDropSql = DBUtil
				.tableUserCategoryDropSql(appName);
		String tableDicisionEntitlementDropSql = DBUtil
				.tableDecisionEntitlementDropSql(appName);
		String tableQueryEntitlementDropSql = DBUtil
				.tableQueryEntitlementDropSql(appName);
		String tableBusinessDataDropSql = DBUtil
				.tableBusinessDataDropSql(appName);
		
		Connection conn = DBPower.getConnection(DBPower.getDefaultDsName());
		try {
			conn.setAutoCommit(false);
			DBUtil.exec(conn, tableQueryDropSql);
			DBUtil.exec(conn, tableUserCategoryDropSql);
			DBUtil.exec(conn, tableDicisionEntitlementDropSql);
			DBUtil.exec(conn, tableQueryEntitlementDropSql);
			DBUtil.exec(conn, tableBusinessDataDropSql);
			
			itr = sqlUserRoles.iterator();
			while (itr.hasNext()) {
				String sql = (String) itr.next();
				try {
					DBUtil.exec(conn, sql);
				} catch (SQLException e) {
					logger.error("Error when delete table: " + sql);
				}
			}
			DBUtil.exec(conn, sqlRolePrivilege);
			DBUtil.exec(conn, sqlRole);
			DBUtil.exec(conn, sqlPrivilege);
			
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new DBLevelException(e1);
			}
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public void deleteApplicationUserType(String appName, String userTypeName) {
		Connection conn = null;
		boolean autoCommit = true;
		try {
			conn = DBPower.getConnection(table.getId());
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// delete ApplicationUserType infos
			ApplicationUserType hint = new ApplicationUserType();
			hint.setAppName(appName);
			hint.setUserTypeName(userTypeName);

			applicationUserTypeDeletor.delete(conn,
					appNameUserTypeNameUserTypeTableWhereEmt, hint);

			// drop userrole tables
			DBUtil.exec(conn, DBUtil
					.userRoleTableDropSql(appName, userTypeName));

			conn.commit();
		} catch (SQLException e) {
			throw new DBLevelException(e);
		} finally {
			DBUtil.setCommitMode(conn, autoCommit);
			DBUtil.close(conn);
		}

		changed = true;
	}

	// private void deleteApplicationUserType(String appName, String
	// userTypeName,
	// Session session) {
	// session
	// .createQuery(
	// "delete ApplicationUserType where appName = :appName and userTypeName = :userTypeName")
	// .setString("appName", appName).setString("userTypeName",
	// userTypeName).executeUpdate();
	// }

	public Collection getAllApplications() {
		loadIntoMemory();
		return store.values();
	}

	public Application getApplication(String name) {
		loadIntoMemory();
		return (Application) store.get(name);
	}

	// private Application getApplication(String name, Session session) {
	// Application app = (Application) session.load(Application.class, name);
	// 
	// extractUserTypes(app, session);
	// return app;
	// }

	// private void extractUserTypes(Application app, Session session) {
	// Iterator itr = app.getApplicationUserTypes().iterator();
	// while (itr.hasNext()) {
	// ApplicationUserType appUserType = (ApplicationUserType) itr.next();
	// UserType userType = extractUserType(appUserType, session);
	// app.getUserTypes().add(userType);
	// }
	// }

	public void updateApplication(Application app) {
		loadIntoMemory();

		Application originalApplication = getApplication(app.getName());
		if (originalApplication == null) {
			return;
		}

		// get a copy of original application
		Collection origUserTypes = originalApplication.getUserTypes();
		Collection newUserTypes = app.getUserTypes();

		Connection conn = null;
		boolean autoCommit = true;
		try {
			conn = DBPower.getConnection(table.getId());
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			// delete original app-usertype
			ApplicationUserType hintUserType = new ApplicationUserType();
			hintUserType.setAppName(app.getName());
			applicationUserTypeDeletor.delete(conn,
					appNameUserTypeTableWhereEmt, hintUserType);

			// save updated app-usertype relationship
			Collection userTypes = app.getUserTypes();
			if (userTypes != null && userTypes.size() > 0) {
				List appUserTypes = new ArrayList(userTypes.size());
				for (Iterator iter = userTypes.iterator(); iter.hasNext();) {
					UserType userType = (UserType) iter.next();

					ApplicationUserType appUserType = new ApplicationUserType();
					appUserType.setAppName(app.getName());
					appUserType.setUserTypeName(userType.getName());
					String produceUserMetadataStr = produceUserMetadataStr(userType
							.getUserMetadata());
					appUserType.setUserMetadataStr(produceUserMetadataStr);

					appUserTypes.add(appUserType);
				}

				applicationUserTypeSaver.batchSave(conn, appUserTypes);
			}

			// update app info
			try {
				updator.updateByIdColumns(conn, app);
			} catch (EntityExistException e) {
				e.printStackTrace();
				throw new DBLevelException(e);
			}

			// update userrole tables
			// 1, find out which usertype(s) to be newly added and which to be deleted
			Iterator origItr = origUserTypes.iterator();
			while (origItr.hasNext()) {
				UserType origUserType = (UserType) origItr.next();
				Iterator newItr = newUserTypes.iterator();
				while (newItr.hasNext()) {
					UserType newUserType = (UserType) newItr.next();
					if (origUserType.getName().equals(newUserType.getName())) {
						// this usertype is updated, move out
						origItr.remove();
						newItr.remove();
					}
				}
			}
			
			Collection userTypesToBeDeleted = origUserTypes;
			Collection userTypesToBeAdded = newUserTypes;

			// 2, delete related userrole tables

			Iterator delItr = userTypesToBeDeleted.iterator();
			while (delItr.hasNext()) {
				UserType delUserType = (UserType) delItr.next();
				DBUtil.exec(conn, DBUtil.userRoleTableDropSql(app.getName(),
						delUserType.getName()));
			}

			// 3, create related userrole tables

			Iterator addItr = userTypesToBeAdded.iterator();
			while (addItr.hasNext()) {
				UserType delUserType = (UserType) addItr.next();
				delUserType = Factory.getUserTypeManager().getUserType(
						delUserType.getName());
				TableMetadata userTable = delUserType.getUserMetadata()
						.getMainTableMetadata();
				DBUtil.exec(conn, DBUtil.userRoleTableCreateSql(app.getName(),
						delUserType.getName(), User.idFieldName,
						getIdColumnType(userTable)));
			}

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw new DBLevelException(e);
		} finally {
			DBUtil.setCommitMode(conn, autoCommit);
			DBUtil.close(conn);
		}

		changed = true;

		// notify Factory
		Factory.applicationChanged(app.getName());
	}

	public void updateApplicatonUserType(String appName, UserType userType) {
		Connection conn = null;
		boolean autoCommit = true;
		try {
			conn = DBPower.getConnection(table.getId());
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			ApplicationUserType appUserType = new ApplicationUserType();
			appUserType.setAppName(appName);
			appUserType.setUserTypeName(userType.getName());
			String userMetadataStr = produceUserMetadataStr(userType
					.getUserMetadata());
			appUserType.setUserMetadataStr(userMetadataStr);

			// delete original app-usertype
			applicationUserTypeDeletor.delete(conn,
					appNameUserTypeNameUserTypeTableWhereEmt, appUserType);

			// save newly app-usertype
			try {
				applicationUserTypeSaver.save(conn, appUserType);
			} catch (EntityExistException e) {
				// should not happen
				e.printStackTrace();
				throw new DBLevelException(e);
			}

			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			throw new DBLevelException(e);
		} finally {
			DBUtil.setCommitMode(conn, autoCommit);
			DBUtil.close(conn);
		}

		changed = true;

		// notify Factory
		Factory.applicationUserTypeChanged(appName, userType.getName());
	}
}
