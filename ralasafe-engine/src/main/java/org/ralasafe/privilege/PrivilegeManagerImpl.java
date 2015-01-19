/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
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
import org.ralasafe.group.Node;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.Util;

public class PrivilegeManagerImpl implements PrivilegeManager {
	private Privilege businessPrivilegeTree;
	private Privilege nonRolePrivilegeTree;
	private Map allBusinessPrivilegesMap;
	private Map allNonRolePrivilegesMap;
	private boolean businessPrivilegeTreeChanged = true;
	private boolean nonRolePrivilegeTreeChanged = true;
	private String appName;
	private String tableName;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdator updator;
	private TableDeletorImpl deletor;

	public PrivilegeManagerImpl(String appName) {
		this.appName = appName;
		this.tableName = appName + "_privilege";
		TableNewer newer = new TableNewer();
		newer.setTableName(tableName);
		newer.setColumnNames(new String[] { "id", "pid", "name", "description",
				"isleaf", "display", "decisionPolicyCombAlg", "queryPolicyCombAlg",
				"type", "constantName", "url", "target", "orderNum" });
		newer.setIdColumnNames(new String[] { "id" });
		newer.setUniqueColumnNames(new String[] { "name" });
		newer.setMappingClass(Privilege.class.getName());
		newer.put("id", new JavaBeanColumnAdapter("id", "int"));
		newer.put("pid", new JavaBeanColumnAdapter("pid", "int"));
		newer.put("name", new JavaBeanColumnAdapter(
				"name", "java.lang.String"));
		newer.put("description", new JavaBeanColumnAdapter(
				"description", "java.lang.String"));
		newer.put("isleaf", new JavaBeanColumnAdapter("isLeaf", "boolean"));
		newer.put("display", new JavaBeanColumnAdapter("display", "boolean"));
		newer.put("decisionPolicyCombAlg", new JavaBeanColumnAdapter(
				"decisionPolicyCombAlg", "int"));
		newer.put("queryPolicyCombAlg", new JavaBeanColumnAdapter(
				"queryPolicyCombAlg", "int"));
		newer.put("type", new JavaBeanColumnAdapter("type", "int"));
		newer.put("constantName", new JavaBeanColumnAdapter("constantName",
				"java.lang.String"));
		newer.put("url", new JavaBeanColumnAdapter("url", "java.lang.String"));
		newer.put("target", new JavaBeanColumnAdapter("target",
				"java.lang.String"));
		newer.put("orderNum", new JavaBeanColumnAdapter("orderNum", "int"));

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

	public void addReservedPrivilege(Locale locale) {
		try {
			addRalasafeAdminPrivilege(locale);
			addPolicyAdminPrivilege(locale);
			addAssignRoleToUserPrivilege(locale);
			addRoleAdminPrivilege(locale);
			businessPrivilegeTreeChanged = true;
		} catch (EntityExistException e) {
			throw new RalasafeException(e);
		}
	}

	private void addRalasafeAdminPrivilege(Locale locale)
			throws EntityExistException {
		Privilege privilege = new Privilege();
		privilege.setType(Privilege.BUSINESS_PRIVILEGE);
		privilege.setIsLeaf(false);
		privilege.setId(Privilege.RALASAFE_ADMIN_ID);
		privilege.setPid(Privilege.BUSINESS_PRIVILEGE_TREE_ROOT_ID);
		privilege.setName(Util.getMessage(locale,
				ResourceConstants.RALASAFE_ADMIN));
		privilege.setOrderNum(0);
		saver.save(privilege);
	}

	private void addPolicyAdminPrivilege(Locale locale)
			throws EntityExistException {
		Privilege privilege = new Privilege();
		privilege.setType(Privilege.BUSINESS_PRIVILEGE);
		privilege.setIsLeaf(true);
		privilege.setId(Privilege.POLICY_ADMIN_ID);
		privilege.setPid(Privilege.RALASAFE_ADMIN_ID);
		privilege.setDescription(Util.getMessage(locale,
				ResourceConstants.POLICY_ADMIN_DESCRIPTION));
		privilege.setName(Util.getMessage(locale,
				ResourceConstants.POLICY_ADMIN));
		privilege.setConstantName("POLICY_ADMIN");
		privilege.setOrderNum(1);
		saver.save(privilege);
	}

	private void addAssignRoleToUserPrivilege(Locale locale)
			throws EntityExistException {
		Privilege privilege = new Privilege();
		privilege.setType(Privilege.BUSINESS_PRIVILEGE);
		privilege.setIsLeaf(true);
		privilege.setId(Privilege.ASSIGN_ROLE_TO_USER_ID);
		privilege.setPid(Privilege.RALASAFE_ADMIN_ID);
		privilege.setName(Util.getMessage(locale,
				ResourceConstants.ASSIGN_ROLE_TO_USER));
		privilege.setConstantName("ASSIGN_ROLE_TO_USER");
		privilege.setOrderNum(2);
		saver.save(privilege);
	}

	private void addRoleAdminPrivilege(Locale locale)
			throws EntityExistException {
		Privilege privilege = new Privilege();
		privilege.setType(Privilege.BUSINESS_PRIVILEGE);
		privilege.setIsLeaf(true);
		privilege.setId(Privilege.ROLE_ADMIN_ID);
		privilege.setPid(Privilege.RALASAFE_ADMIN_ID);
		privilege
				.setName(Util.getMessage(locale, ResourceConstants.ROLE_ADMIN));
		privilege.setConstantName("ROLE_ADMIN");
		privilege.setOrderNum(3);
		saver.save(privilege);
	}

	public Privilege addPrivilege(Privilege pvlg) throws EntityExistException {
		int pid = pvlg.getPid();
		Privilege parentPvlg = null;
		if (pvlg.getType() == Privilege.BUSINESS_PRIVILEGE) {
			buildBusinessPrivilegeTree();
			parentPvlg = (Privilege) allBusinessPrivilegesMap.get(new Integer(
					pid));
		} else if (pvlg.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			buildNonRolePrivilegeTree();
			parentPvlg = (Privilege) allNonRolePrivilegesMap.get(new Integer(
					pid));
		}

		int orderNum = -1;
		// get the greatest orderNum
		if (parentPvlg != null && parentPvlg.getChildren() != null
				&& parentPvlg.getChildren().size() > 0) {
			for (Iterator iter = parentPvlg.getChildren().iterator(); iter
					.hasNext();) {
				int temp = ((Privilege) iter.next()).getOrderNum();
				orderNum = orderNum >= temp ? orderNum : temp;
			}
		}

		pvlg.setId(newPrivilegeId());
		pvlg.setOrderNum(orderNum + 1);

		saver.save(pvlg);
		if (pvlg.getType() == Privilege.BUSINESS_PRIVILEGE) {
			businessPrivilegeTreeChanged = true;
		} else if (pvlg.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			nonRolePrivilegeTreeChanged = true;
		}
		return pvlg;
	}

	private int newPrivilegeId() {
		try {
			return DBUtil.getSequenceNextVal(table, "id");
		} catch (SQLException e) {
			throw new DBLevelException(e);
		}
	}

	public void deletePrivilege(int id) {
		if (id <= 0) {
			throw new RalasafeException("Cannot delete reserved privilege.");
		}
		// delete role-privilege relation infos
		RoleManager roleManager = Factory.getRoleManager(appName);
		roleManager.deleteRolePrivilegeByPrivilege(id);
		// delete privilege
		Privilege pvlg = getPrivilege(id);
		if (pvlg == null) {
			return;
		}

		deletor.deleteByIdColumns(pvlg);
		if (pvlg.getType() == Privilege.BUSINESS_PRIVILEGE) {
			businessPrivilegeTreeChanged = true;
		} else if (pvlg.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			nonRolePrivilegeTreeChanged = true;
		}
	}

	public Collection getAllBusinessPrivileges() {
		buildBusinessPrivilegeTree();
		return allBusinessPrivilegesMap.values();
	}

	public Collection getAllBusinessPrivilegesFromDb() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[8]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setValue(new Integer(Privilege.BUSINESS_PRIVILEGE));
		SelectCondition cdtn = new SelectCondition();
		cdtn.setWhereElement(emt);
		return selector.select(cdtn, null);
	}
	
	public Collection getAllNonRolePrivileges() {
		buildNonRolePrivilegeTree();
		return allNonRolePrivilegesMap.values();
	}
	
	public Collection getAllNonRolePrivilegesFromDb() {
		FieldWhereElement emt = new FieldWhereElement();
		emt.setColumn(table.getColumns()[8]);
		emt.setCompartor(SingleValueComparator.EQUAL);
		emt.setValue(new Integer(Privilege.NON_ROLE_PRIVILEGE));
		SelectCondition cdtn = new SelectCondition();
		cdtn.setWhereElement(emt);
		return selector.select(cdtn, null);
	}
	
	public Collection getLikelyPrivileges(String name) {
		buildBusinessPrivilegeTree();
		buildNonRolePrivilegeTree();
		
		Collection result=new LinkedList();
		for( Iterator iter=allBusinessPrivilegesMap.values().iterator(); iter.hasNext(); ) {
			Privilege pvlg=(Privilege) iter.next();
			if( pvlg.getName().contains( name ) ) {
				result.add( pvlg );
			}
		}
		
		for( Iterator iter=allNonRolePrivilegesMap.values().iterator(); iter.hasNext(); ) {
			Privilege pvlg=(Privilege) iter.next();
			if( pvlg.getName().contains( name ) ) {
				result.add( pvlg );
			}
		}
		
		return result;
//		FieldWhereElement emt = new FieldWhereElement();
//		emt.setColumn(table.getColumns()[2]);
//		emt.setCompartor(SingleValueComparator.LIKE);
//		emt.setContextValue(true);
//		SelectCondition cdtn = new SelectCondition();
//		cdtn.setWhereElement(emt);
//		Privilege pvlg = new Privilege();
//		pvlg.setName("%" + name + "%");
//		return selector.select(cdtn, pvlg);
	}

	public Collection getLikelyPrivilegesByUrl(String url) {
		buildBusinessPrivilegeTree();
		buildNonRolePrivilegeTree();
		
		List result=new LinkedList();
		for( Iterator iter=allBusinessPrivilegesMap.values().iterator(); iter.hasNext(); ) {
			Privilege pvlg=(Privilege) iter.next();
			if( pvlg.getUrl()!=null && pvlg.getUrl().contains( url ) ) {
				result.add( pvlg );
			}
		}
		
		for( Iterator iter=allNonRolePrivilegesMap.values().iterator(); iter.hasNext(); ) {
			Privilege pvlg=(Privilege) iter.next();
			if( pvlg.getUrl()!=null && pvlg.getUrl().contains( url ) ) {
				result.add( pvlg );
			}
		}
		
		Comparator comp = new Comparator() {
			public int compare(Object arg0, Object arg1) {
				Privilege p0 = (Privilege) arg0;
				Privilege p1 = (Privilege) arg1;
				String url0=p0.getUrl();
				String url1=p1.getUrl();
				
				if( url0==null ) {
					url0="";
				}
				if( url1==null ) {
					url1="";
				}
				
				return url0.compareTo( url1 );
			}
		};
		
		Collections.sort( result, comp );
		return result;
//		FieldWhereElement emt = new FieldWhereElement();
//		emt.setColumn(table.getColumns()[9]);
//		emt.setCompartor(SingleValueComparator.LIKE);
//		emt.setContextValue(true);
//		SelectCondition cdtn = new SelectCondition();
//		cdtn.setWhereElement(emt);
//		OrderPart orderPart=new OrderPart();
//		orderPart.setColumnNames( new String[]{"url"} );
//		orderPart.setOrderTypes( new String[]{"desc"} );
//		cdtn.setOrderPart( orderPart );
//		Privilege pvlg = new Privilege();
//		pvlg.setUrl("%" + url + "%");
//		return selector.select(cdtn, pvlg);
	}
	
	public Privilege getPrivilege(int id) {
		buildBusinessPrivilegeTree();
		buildNonRolePrivilegeTree();
		Privilege nodeFromTree0 = (Privilege) allBusinessPrivilegesMap
				.get(new Integer(id));
		Privilege nodeFromTree1 = (Privilege) allNonRolePrivilegesMap
				.get(new Integer(id));
		return (nodeFromTree0 != null ? nodeFromTree0 : nodeFromTree1);
	}

	public void updatePrivilege(Privilege pvlg) throws EntityExistException {
		updator.updateByIdColumns(pvlg);
		if (pvlg.getType() == Privilege.BUSINESS_PRIVILEGE) {
			businessPrivilegeTreeChanged = true;
		} else if (pvlg.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			nonRolePrivilegeTreeChanged = true;
		}
	}

	public void movePrivilege(Privilege privilege, Privilege target,
			int newOrderNum) {
		try {
			if (privilege.getId() <= 0) {
				throw new RalasafeException("Cannot move reserved privilege.");
			}

			privilege = getPrivilege(privilege.getId());
			target = getPrivilege(target.getId());
			if (privilege.getType() != target.getType()) {
				throw new RalasafeException(
						"Cannot move privilege out of the tree.");
			}
			if (target.getIsLeaf()) {
				throw new RalasafeException(
						"Canot move privilege to a leaf node.");
			}
			if (privilege == target
					|| isCascadeChild(privilege.getId(), target.getId())) {
				throw new RalasafeException("This move will produce a cycle.");
			}

			Collection children = getChildren(target);
			ArrayList list = new ArrayList();
			if (children != null) {
				list.addAll(children);
			}

			// If it still be a child of the original parent node
			if (privilege.getPid() == target.getId()) {
				list.remove(privilege);
			}

			privilege.setPid(target.getId());

			// reset orderNum(s), and update to db
			list.add(newOrderNum, privilege);
			for (int i = 0, size = list.size(); i < size; i++) {
				Privilege p = (Privilege) list.get(i);
				p.setOrderNum(i);

				updator.updateByIdColumns(p);
			}

			if (target.getType() == Privilege.BUSINESS_PRIVILEGE) {
				businessPrivilegeTreeChanged = true;
			} else if (target.getType() == Privilege.NON_ROLE_PRIVILEGE) {
				nonRolePrivilegeTreeChanged = true;
			}
		} catch (EntityExistException e) {
			throw new DBLevelException(e);
		}
	}

	public void deletePrivilegeCascade(int nodeId) {
		Privilege current = getPrivilege(nodeId);
		Collection children = getAllChildren(current);
		// delete current node
		deletePrivilege(current.getId());
		// delete cascade children nodes
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Node child = (Node) itr.next();
			deletePrivilege(child.getId());
		}
		
		if (current.getType() == Privilege.BUSINESS_PRIVILEGE) {
			businessPrivilegeTreeChanged = true;
		} else if (current.getType() == Privilege.NON_ROLE_PRIVILEGE) {
			nonRolePrivilegeTreeChanged = true;
		}
	}

	private Collection getAllChildren(Node current) {
		Set allChildren = new HashSet();
		// direct children
		Collection children = current.getChildren();
		allChildren.addAll(children);
		// find children of children
		Iterator itr = children.iterator();
		while (itr.hasNext()) {
			Node child = (Node) itr.next();
			// cascade
			allChildren.addAll(getAllChildren(child));
		}
		return allChildren;
	}

	public Collection getChildren(Privilege privilege) {
		Privilege parent = getPrivilege(privilege.getId());
		return parent.getChildren();
	}

	public Privilege getParent(Privilege privilege) {
		Privilege child = getPrivilege(privilege.getId());
		return (Privilege) child.getParent();
	}

	public Privilege getBusinessPrivilegeTree() {
		buildBusinessPrivilegeTree();
		return businessPrivilegeTree;
	}

	public Privilege getNonRolePrivilegeTree() {
		buildNonRolePrivilegeTree();
		return nonRolePrivilegeTree;
	}

	public Privilege getTree(int nodeId) {
		return getPrivilege(nodeId);
	}

	public boolean isCascadeChild(int pId, int id) {
		Node parent = getPrivilege(pId);
		Node child = getPrivilege(id);

		if (parent != null && child != null) {
			while (child.getParent() != null) {
				if (parent.getId() == child.getPid())
					return true;
				else
					child = child.getParent();
			}
		}
		return false;
	}

	public boolean isChild(int pId, int id) {
		Node parent = getPrivilege(pId);
		Node child = getPrivilege(id);

		if (parent != null && child != null ) {
			if (parent.getId() == child.getPid())
				return true;
		}
		return false;
	}

	private void buildTree(Collection privileges, Map allNodesMap) {
		// 1, Put all privilege into map
		Iterator itr = privileges.iterator();
		while (itr.hasNext()) {
			Privilege privilege = (Privilege) itr.next();
			allNodesMap.put(new Integer(privilege.getId()), privilege);
		}

		// 2, arrange child-parent relationship
		Privilege child;
		Privilege parent;
		itr = privileges.iterator();
		while (itr.hasNext()) {
			child = (Privilege) itr.next();
			parent = (Privilege) allNodesMap.get(new Integer(child.getPid()));
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

		// 3, sort by orderNum
		for (Iterator iter = allNodesMap.values().iterator(); iter.hasNext();) {
			Privilege p = (Privilege) iter.next();
			Collection children = p.getChildren();

			if (children != null) {
				Collections.sort((List) children, comp);
			}
		}
	}

	private synchronized void buildBusinessPrivilegeTree() {
		if (!businessPrivilegeTreeChanged)
			return;
		businessPrivilegeTree = new Privilege();
		businessPrivilegeTree.setId(Privilege.BUSINESS_PRIVILEGE_TREE_ROOT_ID);
		businessPrivilegeTree.setType(Privilege.BUSINESS_PRIVILEGE);
		businessPrivilegeTree.setIsLeaf(false);
		businessPrivilegeTree.setPid(Privilege.NULL_ROOT_ID);
		businessPrivilegeTree.setParent(null);
		allBusinessPrivilegesMap = new HashMap();
		Collection privileges = getAllBusinessPrivilegesFromDb();
		privileges.add(businessPrivilegeTree);
		buildTree(privileges, allBusinessPrivilegesMap);
		businessPrivilegeTreeChanged = false;
	}

	private synchronized void buildNonRolePrivilegeTree() {
		if (!nonRolePrivilegeTreeChanged)
			return;
		nonRolePrivilegeTree = new Privilege();
		nonRolePrivilegeTree.setId(Privilege.NON_ROLE_PRIVILEGE_TREE_ROOT_ID);
		nonRolePrivilegeTree.setType(Privilege.NON_ROLE_PRIVILEGE);
		nonRolePrivilegeTree.setIsLeaf(false);
		nonRolePrivilegeTree.setPid(Privilege.NULL_ROOT_ID);
		nonRolePrivilegeTree.setParent(null);
		allNonRolePrivilegesMap = new HashMap();
		Collection privileges = getAllNonRolePrivilegesFromDb();
		privileges.add(nonRolePrivilegeTree);
		buildTree(privileges, allNonRolePrivilegesMap);
		nonRolePrivilegeTreeChanged = false;
	}
}
