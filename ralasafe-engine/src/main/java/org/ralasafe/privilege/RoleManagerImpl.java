/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.ResourceConstants;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.SingleValueComparator;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.TableUpdator;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.userType.UserType;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.Util;

public class RoleManagerImpl implements RoleManager {
	private static final Log log=LogFactory.getLog( RoleManagerImpl.class );
	
	private static final int RALASAFE_ADMINISTRATOR_ID = -1;
	private String appName;
	private String tableName;

	private Table roleTable;
	private TableSelectorImpl roleSelector;
	private TableSaverImpl roleSaver;
	private TableUpdator roleUpdator;
	private TableDeletorImpl roleDeletor;

	private Table rolePrivilegeTable;
	private TableSelectorImpl rolePrivilegeSelector;
	private TableSaverImpl rolePrivilegeSaver;
	private TableUpdator rolePrivilegeUpdator;
	private TableDeletorImpl rolePrivilegeDeletor;

	public RoleManagerImpl(String appName) {
		this.appName = appName;
		tableName = appName + "_role";

		// role table
		TableNewer roleTableNewer = new TableNewer();
		roleTableNewer.setTableName(tableName);
		roleTableNewer.setColumnNames(new String[] { "id", "name",
				"description" });
		roleTableNewer.setIdColumnNames(new String[] { "id" });
		roleTableNewer.setUniqueColumnNames(new String[] { "name" });
		roleTableNewer.setMappingClass(Role.class.getName());
		roleTableNewer.put("id", new JavaBeanColumnAdapter("id", "int"));
		roleTableNewer.put("name", new JavaBeanColumnAdapter("name",
				"java.lang.String"));
		roleTableNewer.put("description", new JavaBeanColumnAdapter(
				"description", "java.lang.String"));
		roleTableNewer.setId(DBPower.getTableId(null, roleTableNewer
				.getTableName()));

		roleTable = roleTableNewer.getTable();
		roleSelector = new TableSelectorImpl();
		roleSelector.setObjectNewer(new JavaBeanObjectNewer(roleTableNewer
				.getMappingClass()));
		roleSaver = new TableSaverImpl();
		roleUpdator = new TableUpdatorImpl();
		roleDeletor = new TableDeletorImpl();

		roleSelector.setTable(roleTable);
		roleSaver.setTable(roleTable);
		roleUpdator.setTable(roleTable);
		roleDeletor.setTable(roleTable);

		// RolePrivilege table
		TableNewer rolePrivilegeTableNewer = new TableNewer();
		rolePrivilegeTableNewer.setTableName(appName + "_roleprivilege");
		rolePrivilegeTableNewer.setColumnNames(new String[] { "roleid",
				"privilegeid" });
		rolePrivilegeTableNewer.setIdColumnNames(new String[] { "roleid",
				"privilegeid" });
		rolePrivilegeTableNewer.setMappingClass(RolePrivilege.class.getName());
		rolePrivilegeTableNewer.put("roleid", new JavaBeanColumnAdapter(
				"roleId", "int"));
		rolePrivilegeTableNewer.put("privilegeid", new JavaBeanColumnAdapter(
				"privilegeId", "int"));
		rolePrivilegeTableNewer.setId(DBPower.getTableId(null,
				rolePrivilegeTableNewer.getTableName()));

		rolePrivilegeTable = rolePrivilegeTableNewer.getTable();
		rolePrivilegeSelector = new TableSelectorImpl();
		rolePrivilegeSelector.setObjectNewer(new JavaBeanObjectNewer(
				rolePrivilegeTableNewer.getMappingClass()));
		rolePrivilegeSaver = new TableSaverImpl();
		rolePrivilegeUpdator = new TableUpdatorImpl();
		rolePrivilegeDeletor = new TableDeletorImpl();

		rolePrivilegeSelector.setTable(rolePrivilegeTable);
		rolePrivilegeSaver.setTable(rolePrivilegeTable);
		rolePrivilegeUpdator.setTable(rolePrivilegeTable);
		rolePrivilegeDeletor.setTable(rolePrivilegeTable);
	}

	public void addReservedRole(Locale locale) {
		if( log.isInfoEnabled() ) {
			log.info( "Add reserved role(s)" );
		}
		addRalasafeAdministratorRole(locale);
		Collection privilegeIds = new ArrayList();
		privilegeIds.add(new Integer(Privilege.POLICY_ADMIN_ID));
		privilegeIds.add(new Integer(Privilege.ASSIGN_ROLE_TO_USER_ID));
		privilegeIds.add(new Integer(Privilege.ROLE_ADMIN_ID));
		
		if( log.isInfoEnabled() ) {
			log.info( "Assign privileges to reserved role(s)" );
		}
		assignPrivileges(RALASAFE_ADMINISTRATOR_ID, privilegeIds);
	}

	private void addRalasafeAdministratorRole(Locale locale) {
		try {
			Role role = new Role();
			role.setId(RALASAFE_ADMINISTRATOR_ID);
			role.setName(Util.getMessage(locale,
					ResourceConstants.RALASAFE_ADMINISTRATOR));
			roleSaver.save(role);

			// create it's related usercategory
			UserCategoryManager userCategoryManager = Factory
					.getUserCategoryManager(appName);
			userCategoryManager.addUserCategory(role);
		} catch (EntityExistException e) {
			throw new DBLevelException(e);
		}
	}

	public Role addRole(Role role) throws EntityExistException {
		role.setId(newRoleId());
		roleSaver.save(role);
		
		if( log.isInfoEnabled() ) {
			log.info( "Role created: " + role );
		}

		// create related usercategory
		UserCategoryManager userCategoryManager = Factory
				.getUserCategoryManager(appName);
		try {
			userCategoryManager.addUserCategory(role);
		} catch (EntityExistException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		}
		return role;
	}

	private int newRoleId() {
		try {
			return DBUtil.getSequenceNextVal(roleTable, "id");
		} catch (SQLException e) {
			throw new DBLevelException(e);
		}
	}

	public void assignPrivileges(int roleId, Collection pvlgIds) {
		// delete original role-privilege records
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(rolePrivilegeTable.getColumns()[0]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		RolePrivilege rolePrivilege = new RolePrivilege();
		rolePrivilege.setRoleId(roleId);
		rolePrivilegeDeletor.delete(emt, rolePrivilege);

		// new records
		ArrayList rolePrivileges = new ArrayList();
		Iterator itr = pvlgIds.iterator();
		while (itr.hasNext()) {
			int privilegeId = ((Integer) itr.next()).intValue();
			rolePrivileges.add(new RolePrivilege(roleId, privilegeId));
		}

		rolePrivilegeSaver.batchSave(rolePrivileges);
		
		if( log.isInfoEnabled() ) {
			log.info( "Privileges " + pvlgIds + " assigned to roleId=" + roleId );
		}
	}

	public void deleteRole(int id) {
		if( log.isDebugEnabled() ) {
			log.debug( "delete role-privilege relationships (by assigning none privilege to the role). RoleId=" + id );
		}
		// delete role-privilege relationship
		assignPrivileges(id, new ArrayList());

		if( log.isDebugEnabled() ) {
			log.debug( "delete user-role relationships. RoleId=" + id );
		}
		// delete user-role of current application usertype(s)
		Collection userTypes = Factory.getApplicationManager().getApplication(
				appName).getUserTypes();
		Iterator itr = userTypes.iterator();
		while (itr.hasNext()) {
			UserType userType = (UserType) itr.next();
			UserRoleManager userRoleManager = Factory.getUserRoleManager(
					appName, userType.getName());
			userRoleManager.deleteUserRoles(id);
		}

		// delete role
		Role role = new Role();
		role.setId(id);
		roleDeletor.deleteByIdColumns(role);
		
		if( log.isInfoEnabled() ) {
			log.info( "Role deleted: " + role );
		}
	}

	public void deleteRolePrivilegeByPrivilege(int privilegeId) {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(rolePrivilegeTable.getColumns()[1]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		RolePrivilege rolePrivilege = new RolePrivilege();
		rolePrivilege.setPrivilegeId(privilegeId);
		rolePrivilegeDeletor.delete(emt, rolePrivilege);
	}

	public Collection getAllRoles() {
		return roleSelector.select(new SelectCondition(), null);
	}

	public Collection getLikelyRoles(String name) {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(roleTable.getColumns()[1]);
		emt.setCompartor(SingleValueComparator.LIKE);
		emt.setContextValue(true);

		SelectCondition cdtn = new SelectCondition();
		cdtn.setWhereElement(emt);

		Role role = new Role();
		role.setName("%" + name + "%");

		return roleSelector.select(cdtn, role);
	}

	public Collection getPrivileges(int roleId) {
		// get RolePrivilege collection
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(rolePrivilegeTable.getColumns()[0]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		SelectCondition cdtn = new SelectCondition();
		cdtn.setWhereElement(emt);

		RolePrivilege rolePrivilege = new RolePrivilege();
		rolePrivilege.setRoleId(roleId);
		Collection rolePrivileges = rolePrivilegeSelector.select(cdtn,
				rolePrivilege);

		// get Privilege collections
		ArrayList privileges = new ArrayList();
		Iterator itr = rolePrivileges.iterator();
		while (itr.hasNext()) {
			int privilegeId = ((RolePrivilege) itr.next()).getPrivilegeId();
			privileges.add(Factory.getPrivilegeManager(appName).getPrivilege(
					privilegeId));
		}

		return privileges;
	}

	public Role getRole(int id) {
		Role role = new Role();
		role.setId(id);
		return (Role) roleSelector.selectByIdColumns(role);
	}

	public boolean hasPrivilege(int roleId, int pvlgId) {
		//PrivilegeManager privilegeManager = Factory.getPrivilegeManager(appName);
		boolean hasPrivilege = false;
		Iterator itr = getPrivileges(roleId).iterator();
		while (itr.hasNext()) {
			Privilege privilege = (Privilege) itr.next();
			if (privilege.getId() == pvlgId) {
				hasPrivilege = true;
				break;
			} /**else {
				if (privilegeManager.isCascadeChild(privilege.getId(), pvlgId)) {
					hasPrivilege = true;
					break;
				}
			}*/
		}

		return hasPrivilege;
	}

	public void updateRole(Role role) throws EntityExistException {
		roleUpdator.updateByIdColumns(role);
		
		if( log.isInfoEnabled() ) {
			log.info( "Role updated: " + role );
		}
	}
}
