/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
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
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.user.User;
import org.ralasafe.userType.UserType;

public class UserRoleManagerImpl implements UserRoleManager {

	private String appName;
	private String userTypeName;
	private FieldMetadata idField;

	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdator updator;
	private TableDeletorImpl deletor;

	public UserRoleManagerImpl(String appName, String userTypeName) {
		this.appName = appName;
		this.userTypeName = userTypeName;

		// get userId definition
		Collection userTypes = Factory.getApplicationManager().getApplication(
				appName).getUserTypes();
		Iterator itr = userTypes.iterator();
		while (itr.hasNext()) {
			UserType userType = (UserType) itr.next();
			if (userType.getName().equals(userTypeName)) {
				FieldMetadata[] fields = userType.getUserMetadata()
						.getMainTableMetadata().getFields();
				for (int i = 0; i < fields.length; i++) {
					FieldMetadata field = fields[i];
					if (field.getName().equals(User.idFieldName)) {
						idField = field;
						break;
					}
				}
			}
		}

		TableNewer newer = new TableNewer();
		newer.setTableName(appName + "_" + userTypeName + "_userrole");
		newer.setColumnNames(new String[] { "userid", "roleid" });
		newer.setIdColumnNames(new String[] { "userid", "roleid" });
		newer.setMappingClass(UserRole.class.getName());
		newer.put("userid", new JavaBeanColumnAdapter("userId",
				"java.lang.Object"));
		newer.put("roleid", new JavaBeanColumnAdapter("roleId", "int"));
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
	}

	public void assignRoles(Object userId, Collection roleIds) {
		// delete original infos
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[0]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		deletor.delete(emt, userRole);

		// create new records
		ArrayList userRoles = new ArrayList();
		Iterator itr = roleIds.iterator();
		while (itr.hasNext()) {
			int roleId = ((Integer) itr.next()).intValue();
			userRoles.add(new UserRole(userId, roleId));
		}

		saver.batchSave(userRoles);
	}

	public void assignRoles(Collection userIds, Collection roleIds) {
		Iterator itr = userIds.iterator();
		while (itr.hasNext()) {
			Object userId = itr.next();
			assignRoles(userId, roleIds);
		}

	}

	public void deleteUserRoles(Object userId) {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[0]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		deletor.delete(emt, userRole);
	}

	public void deleteUserRoles(int roleId) {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[1]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		UserRole userRole = new UserRole();
		userRole.setRoleId(roleId);
		deletor.delete(emt, userRole);
	}

	public Collection getRoles(Object userId) {
		// get UserRole collection
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[0]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setContextValue(true);

		SelectCondition cdtn = new SelectCondition();
		cdtn.setWhereElement(emt);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		Collection userRoles = selector.select(cdtn, userRole);

		// ge Privilege collection
		ArrayList roles = new ArrayList();
		Iterator itr = userRoles.iterator();
		while (itr.hasNext()) {
			int roleId = ((UserRole) itr.next()).getRoleId();
			roles.add(Factory.getRoleManager(appName).getRole(roleId));
		}

		return roles;
	}

	public Privilege getBusinessPrivilegeTree(Object userId) {
		PrivilegeManager pvlgMng = Factory.getPrivilegeManager(appName);
		RoleManager roleMng = Factory.getRoleManager(appName);

		// which privilege(s) this user has been assigned? 
		Collection assignedPrivilegeIds = new HashSet();
		Collection roles = getRoles(userId);
		for (Iterator roleIter = roles.iterator(); roleIter.hasNext();) {
			Role role = (Role) roleIter.next();
			Collection privileges = roleMng.getPrivileges(role.getId());

			for (Iterator pvlgIter = privileges.iterator(); pvlgIter.hasNext();) {
				Privilege pvlg = (Privilege) pvlgIter.next();
				assignedPrivilegeIds.add(new Integer(pvlg.getId()));
			}
		}

		Collection allBussinessPrivileges = pvlgMng.getAllBusinessPrivileges();

		// get privilege copies (copy from allBusinessPrivilege)
		Collection assignedPrivileges = new HashSet();
		for (Iterator iter = allBussinessPrivileges.iterator(); iter.hasNext();) {
			Privilege pvlg = (Privilege) iter.next();
			
			if (!pvlg.getIsLeaf()) {
				// parent nodes, does't evel be granted now. We will delte "lonely" nodes later
				boolean rootNode=pvlg.getId()==Privilege.BUSINESS_PRIVILEGE_TREE_ROOT_ID;
				if( !rootNode ) {
					assignedPrivileges.add((Privilege) (pvlg.clone()));
				}
			} else {
				// only add granted privilege & displayable privilege here
				if (pvlg.getDisplay() &&
						assignedPrivilegeIds.contains(new Integer(pvlg.getId()))) {
					assignedPrivileges.add((Privilege) (pvlg.clone()));
				}
			}
		}

		Privilege root = new Privilege();
		root.setId(Privilege.BUSINESS_PRIVILEGE_TREE_ROOT_ID);
		root.setType(Privilege.BUSINESS_PRIVILEGE);
		root.setIsLeaf(false);
		root.setPid(Privilege.NULL_ROOT_ID);
		root.setParent(null);

		// add root node
		assignedPrivileges.add(root);

		// buildTree. A litter difference from PrivilegeManagerImpl#buildTree()
		buildTree(assignedPrivileges);

		// delete lonely nodes (they are parent nodes :) )
		reduceTree(assignedPrivileges);
		return root;
	}

	private void reduceTree(Collection assignedPrivileges) {
		for (Iterator iter = assignedPrivileges.iterator(); iter.hasNext();) {
			Privilege pvlg = (Privilege) iter.next();
			checkNode(pvlg);
		}
	}

	private void checkNode(Privilege pvlg) {
		// leaf and root node needn't to be checked
		if (pvlg.getIsLeaf()
				|| pvlg.getId() == Privilege.BUSINESS_PRIVILEGE_TREE_ROOT_ID) {
			return;
		}

		// if a parent has no child nodes, we call it "lonely node". Delete it
		Collection children = pvlg.getChildren();
		if (children == null || children.size() == 0) {
			Privilege parent = (Privilege) pvlg.getParent();
			parent.getChildren().remove(pvlg);

			// When deleted, the node's parent comes to be a "lonely node", so check parent again
			checkNode(parent);
		}
	}

	private void buildTree(Collection assignedPrivileges) {
		Map map = new HashMap();

		for (Iterator iter = assignedPrivileges.iterator(); iter.hasNext();) {
			Privilege child = (Privilege) iter.next();
			map.put(new Integer(child.getId()), child);
		}

		for (Iterator iter = assignedPrivileges.iterator(); iter.hasNext();) {
			Privilege child = (Privilege) iter.next();
			Privilege parent = (Privilege) map.get(new Integer(child.getPid()));
			if (parent != null) {
				parent.getChildren().add(child);
				child.setParent(parent);
			}
		}

		Comparator comp = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Privilege p0 = (Privilege) arg0;
				Privilege p1 = (Privilege) arg1;
				return p0.getOrderNum() - p1.getOrderNum();
			}
		};

		// sort by orderNum
		for (Iterator iter = assignedPrivileges.iterator(); iter.hasNext();) {
			Privilege p = (Privilege) iter.next();
			Collection children = p.getChildren();

			if (children != null) {
				Collections.sort((List) children, comp);
			}
		}
	}

	public boolean hasPrivilege(Object userId, int pvlgId) {

		PrivilegeManager pvlgMng = Factory.getPrivilegeManager(appName);
		Privilege privilege = pvlgMng.getPrivilege(pvlgId);
		if (privilege == null) {
			throw new RalasafeException("No privilege with id='" + pvlgId
					+ "' found.");
		}

		if (privilege.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			// NON_ROLE_PRIVILEGE don't need to be check
			return true;
		}

		boolean hasPrivilege = false;
		Iterator itr = getRoles(userId).iterator();
		while (itr.hasNext()) {
			Role role = (Role) itr.next();
			hasPrivilege = Factory.getRoleManager(appName).hasPrivilege(
					role.getId(), pvlgId);
			if (hasPrivilege)
				break;
		}
		return hasPrivilege;
	}

}
