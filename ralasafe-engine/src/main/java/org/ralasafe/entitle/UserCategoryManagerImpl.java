/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
import org.ralasafe.ResourceConstants;
import org.ralasafe.SystemConstant;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.impl.TableDeletorImpl;
import org.ralasafe.db.impl.TableSaverImpl;
import org.ralasafe.db.impl.TableSelectorImpl;
import org.ralasafe.db.impl.TableUpdatorImpl;
import org.ralasafe.db.sql.xml.UserCategories;
import org.ralasafe.db.sql.xml.types.LinkerType;
import org.ralasafe.db.sql.xml.types.SimpleValueTypeTypeType;
import org.ralasafe.group.Node;
import org.ralasafe.privilege.Role;
import org.ralasafe.script.ScriptFactory;
import org.ralasafe.user.User;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.IOUtil;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class UserCategoryManagerImpl implements UserCategoryManager {
	private static final int ROOT_ID = 0;
	private static String storeDir = SystemConstant.getUserCategoryStoreDir();
	/** key/value=id<Integer>/UserCategory<org.ralasafe.entitle.UserCategory> */
	private Map storeMap;
	private String appName;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdatorImpl updator;
	private TableDeletorImpl deleter;
	// private ScriptFactory translator;
	private Comparator comp;
	private String storeFilePostfix;

	private static Log log=LogFactory.getLog( UserCategoryManagerImpl.class );
	
	public UserCategoryManagerImpl(String appName) {
		this.appName = appName;
		storeFilePostfix = "_" + appName + ".xml";

		storeMap = new HashMap();
		// translator=new ScriptFactory();
		// table definition
		TableNewer tableNewer = new TableNewer();
		tableNewer.setTableName(appName + "_usercategory");
		tableNewer.setColumnNames(new String[] { "id", "name", "description",
				"installDate", "fileName", "pid", "isLeaf" });
		tableNewer.setIdColumnNames(new String[] { "id" });
		tableNewer.setUniqueColumnNames(new String[] { "name", "pid" });
		tableNewer.setMappingClass(UserCategory.class.getName());
		tableNewer.put("id", new JavaBeanColumnAdapter("id", "int"));
		tableNewer.put("name", new JavaBeanColumnAdapter("name",
				"java.lang.String"));
		tableNewer.put("description", new JavaBeanColumnAdapter("description",
				"java.lang.String"));
		tableNewer.put("installDate", new JavaBeanColumnAdapter("installDate",
				"java.util.Date"));
		tableNewer.put("fileName", new JavaBeanColumnAdapter("file",
				"java.lang.String"));
		tableNewer.put("pid", new JavaBeanColumnAdapter("pid", "int"));
		tableNewer
				.put("isLeaf", new JavaBeanColumnAdapter("isLeaf", "boolean"));
		tableNewer.setId(DBPower.getTableId(null, tableNewer.getTableName()));
		table = tableNewer.getTable();
		selector = new TableSelectorImpl();
		selector.setObjectNewer(new JavaBeanObjectNewer(tableNewer
				.getMappingClass()));
		saver = new TableSaverImpl();
		updator = new TableUpdatorImpl();
		deleter = new TableDeletorImpl();
		selector.setTable(table);
		saver.setTable(table);
		updator.setTable(table);
		deleter.setTable(table);
		comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				UserCategory q1 = (UserCategory) o1;
				UserCategory q2 = (UserCategory) o2;
				return q1.getName().compareTo(q2.getName());
			}
		};

		// load all user category into memory
		loadIntoMemory();
	}

	public Collection checkSameNameUserCategories(String fileUrl) {
		try {
			UserCategories root = UserCategories.unmarshal(new FileReader(
					fileUrl));
			org.ralasafe.db.sql.xml.UserCategory[] xmlUcs = root.getUserCategory();
			return checkSameNameUserCategories(xmlUcs);
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
	}

	private Collection checkSameNameUserCategories(
			org.ralasafe.db.sql.xml.UserCategory[] ucs) {
		// put all to check names into set
		Set toCheckNames = new HashSet();
		for (int i = 0; i < ucs.length; i++) {
			String name = ucs[i].getName();
			toCheckNames.add(name);
		}
		List sameNames = new ArrayList(ucs.length);
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			UserCategory uc = (UserCategory) iter.next();
			String name = uc.getName();
			if (uc.getIsLeaf() && toCheckNames.contains(name)) {
				// this name is depulicated
				sameNames.add(name);
			}
		}
		return sameNames;
	}

	public Collection getLikelyUserCategories(String name) {
		List result = new LinkedList();
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			UserCategory uc = (UserCategory) iter.next();
			String ucName = uc.getName();
			if (ucName.indexOf(name) != -1) {
				result.add(uc);
			}
		}
		return result;
	}

	public UserCategory getUserCategory(String name) {
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			UserCategory uc = (UserCategory) iter.next();
			String ucName = uc.getName();
			if (ucName.equals(name)) {
				return uc;
			}
		}
		return null;
	}

	public void installCategories(String fileUrl, boolean overwrite) {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(table.getId());
			UserCategories root = UserCategories.unmarshal(new FileReader(
					fileUrl));
			org.ralasafe.db.sql.xml.UserCategory[] xmlUcs = root.getUserCategory();
			List saveUcs = new ArrayList(xmlUcs.length);
			Collection sameNames = checkSameNameUserCategories(xmlUcs);
			Set sameNameSet = new HashSet();
			sameNameSet.addAll(sameNames);
			for (int i = 0; i < xmlUcs.length; i++) {
				org.ralasafe.db.sql.xml.UserCategory xmlUc = xmlUcs[i];
				String name = xmlUc.getName();
				java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
				xmlUc.marshal(xmlContentWriter);
				UserCategory uc = new UserCategory();
				uc.setName(name);
				uc.setXmlContent(xmlContentWriter.toString());
				uc.setInstallDate(new Date());
				uc.setPid(ROOT_ID);
				uc.setIsLeaf(true);

				if (sameNameSet.contains(name)) {
					if (overwrite) {
						// update this UserCategory
						UserCategory orginalUserCategory = getUserCategory(name);
						uc.setId(orginalUserCategory.getId());
						uc.setFile(uc.getId() + storeFilePostfix);
						uc.setDescription(orginalUserCategory.getDescription());
						uc.setIsLeaf(orginalUserCategory.getIsLeaf());
						uc.setPid(orginalUserCategory.getPid());
						updator.updateByIdColumns(uc);
					}
				} else {
					uc.setId(newUserCategoryId());
					uc.setFile(uc.getId() + storeFilePostfix);
					saveUcs.add(uc);
				}

				// write xml content into io system
				String storeFile = storeDir + uc.getFile();
				IOUtil.write(storeFile, uc.getXmlContent());
			}
			saver.batchSave(conn, saveUcs);
			loadIntoMemory();
		} catch (DBLevelException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RalasafeException(e1);
			}
			throw new RalasafeException(e);
		} catch (IOException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RalasafeException(e1);
			}
			throw new RalasafeException(e);
		} catch (Exception e) {
			throw new RalasafeException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

	private int newUserCategoryId() {
		try {
			int id = DBUtil.getSequenceNextVal(table, "id");

			// we need id>0
			while (id <= 0) {
				id = DBUtil.getSequenceNextVal(table, "id");
			}
			return id;
		} catch (SQLException e) {
			throw new DBLevelException(e);
		}
	}

	private synchronized void loadIntoMemory() {
		Collection ucs = selector.select(new SelectCondition(), null);
		storeMap.clear();
		for (Iterator iter = ucs.iterator(); iter.hasNext();) {
			UserCategory userCategory = (UserCategory) iter.next();

			if (userCategory.getIsLeaf()) {
				loadXmlContent(userCategory);
			}
			storeMap.put(new Integer(userCategory.getId()), userCategory);
		}

		// build tree
		// first, add a root node
		UserCategory root = new UserCategory();
		root.setId(ROOT_ID);
		root.setPid(-1);
		root.setName("root");
		root.setIsLeaf(false);
		storeMap.put(new Integer(ROOT_ID), root);

		for (Iterator iter = ucs.iterator(); iter.hasNext();) {
			UserCategory temp = (UserCategory) iter.next();
			// set parent
			int pid = temp.getPid();
			UserCategory pNode = (UserCategory) storeMap.get(new Integer(pid));
			temp.setParent(pNode);

			// set as a child
			if (pNode != null) {
				Collection children = pNode.getChildren();
				if (children == null) {
					children = new LinkedList();
					pNode.setChildren(children);
				}
				children.add(temp);
			}
		}
	}

	private void loadXmlContent(UserCategory userCategory) {
		// read xml content from io system
		String file = userCategory.getFile();
		if (!StringUtil.isEmpty(file)) {
			String storeFile = storeDir + file;

			try {
				String content = IOUtil.read(storeFile);
				userCategory.setXmlContent(content);
			} catch (IOException e) {
				throw new RalasafeException(e);
			}
		}

		// maybe there's no xml content for non-leaf nodes
		if (!StringUtil.isEmpty(userCategory.getXmlContent())) {
			org.ralasafe.db.sql.xml.UserCategoryType xmlUserCategory;
			try {
				xmlUserCategory = org.ralasafe.db.sql.xml.UserCategory
						.unmarshal(new StringReader(userCategory
								.getXmlContent()));
			} catch (Exception e) {
				throw new RalasafeException(e);
			}
			org.ralasafe.script.UserCategory scriptUserCategory = ScriptFactory
					.getUserCategory(xmlUserCategory, Factory
							.getQueryManager(appName));
			userCategory.setScriptUserCategory(scriptUserCategory);
		}
	}

	/**
	 * remove xml head likes <?xml version="1.0" encoding="UTF-8"?>
	 */
	private String removeXMLHead(String xml) {
		int index = xml.indexOf("?>");
		if (index > 0)
			return xml.substring(index + 2);
		else
			return xml;
	}

	public Collection getAllUserCategories() {
		Collection values = storeMap.values();
		List result = new ArrayList(values.size());
		result.addAll(values);
		Collections.sort(result, comp);
		return result;
	}

	public UserCategory addUserCategory(int pid, String name,
			String description, boolean isLeaf) throws EntityExistException {
		if (isLeaf) {
			return addLeafUserCategory(pid, name, description);
		} else {
			return addBranchUserCategory(pid, name, description);
		}
	}

	private UserCategory addLeafUserCategory(int pid, String name,
			String description) throws EntityExistException {
		UserCategory uc = new UserCategory();
		uc.setId(newUserCategoryId());
		uc.setName(name);
		uc.setDescription(description);
		uc.setInstallDate(new Date());
		uc.setXmlContent(initXmlContent(name, description));
		uc.setFile(uc.getId() + storeFilePostfix);
		uc.setPid(pid);
		uc.setIsLeaf(true);

		// write to io system
		String file = storeDir + uc.getFile();
		try {
			IOUtil.write(file, uc.getXmlContent());
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		saver.save(uc);

		// update cache
		loadXmlContent(uc);
		UserCategory parent = (UserCategory) storeMap.get(new Integer(uc
				.getPid()));
		uc.setParent(parent);
		parent.getChildren().add(uc);
		storeMap.put(new Integer(uc.getId()), uc);

		return uc;
	}

	private UserCategory addBranchUserCategory(int pid, String name,
			String description) throws DBLevelException, EntityExistException {
		UserCategory uc = new UserCategory();
		uc.setId(newUserCategoryId());
		uc.setName(name);
		uc.setDescription(description);
		uc.setInstallDate(new Date());
		uc.setPid(pid);
		uc.setIsLeaf(false);

		saver.save(uc);

		// update cache
		UserCategory parent = (UserCategory) storeMap.get(new Integer(uc
				.getPid()));
		uc.setParent(parent);
		parent.getChildren().add(uc);
		storeMap.put(new Integer(uc.getId()), uc);

		return uc;
	}

	private String initXmlContent(String name, String description) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>        \r\n"
		+ "<userCategory name=\"" + name +
				"\" isRawScript=\"false\">  \r\n"
		+ "    <exprGroup linker=\"AND\"/>                   \r\n"
		+ "    <rawScript>                                   \r\n"
		+ "        <content></content>                       \r\n"
		+ "    </rawScript>                                  \r\n"
		+ "</userCategory>";
	}

	public void deleteUserCategory(int id) {
		// check cache store
		UserCategory uc = (UserCategory) storeMap.get(new Integer(id));

		if (uc == null) {
			return;
		}

		if (uc.getIsLeaf()) {
			deleteSingleUserCategory(id);
		} else {
			Collection children = uc.getChildren();
			Iterator itr = children.iterator();
			int[] ids = new int[children.size()];
			for (int i = 0; i < ids.length; i++) {
				UserCategory child = (UserCategory) itr.next();
				ids[i] = child.getId();
			}
			for (int i = 0; i < ids.length; i++) {
				deleteUserCategory(ids[i]);
			}

			deleteSingleUserCategory(id);
		}
	}

	private void deleteSingleUserCategory(int id) {
		UserCategory hint = new UserCategory();
		hint.setId(id);
		deleter.deleteByIdColumns(hint);
		// delete cascade things
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		entitleManager.deleteCascadeEntitlementByUserCategory(id);

		// remove xml content from io system
		UserCategory uc = (UserCategory) storeMap.get(new Integer(id));

		if (uc != null && !StringUtil.isEmpty(uc.getFile())) {
			new File(storeDir + uc.getFile()).delete();
		}

		// update cache
		UserCategory parent = (UserCategory) storeMap.get(new Integer(uc
				.getPid()));
		uc.setParent(null);
		uc.setChildren(null);
		parent.getChildren().remove(uc);
		storeMap.remove(new Integer(uc.getId()));
	}

	public void updateUserCategory(int id, String name, String description)
			throws EntityExistException {
		UserCategory original = getUserCategory(id);
		if (original == null) {
			return;
		}
		original.setName(name);
		original.setDescription(description);

		updator.updateByIdColumns(original);
		
		String xmlContent=original.getXmlContent();
		if( StringUtil.isEmpty( xmlContent ) ) {
			// none xml content needs update
			return;
		}
		
		org.ralasafe.db.sql.xml.UserCategoryType xmlData;
		try {
			xmlData = org.ralasafe.db.sql.xml.UserCategory
					.unmarshal(new StringReader( xmlContent ));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		xmlData.setName( name );
		
		updateUserCategory( id, (org.ralasafe.db.sql.xml.UserCategory)xmlData );
	}

	public void updateUserCategory(int id,
			org.ralasafe.db.sql.xml.UserCategory content)
			throws EntityExistException {
		UserCategory original = getUserCategory(id);
		if (original == null) {
			return;
		}

		java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
		try {
			content.marshal(xmlContentWriter);
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		original.setXmlContent(xmlContentWriter.toString());

		// update xml content
		String file = storeDir + original.getFile();
		try {
			IOUtil.write(file, StringUtil.keepSpaceInContent(original
					.getXmlContent(), content.getRawScript().getContent()));
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		// update cache
		loadXmlContent(original);
	}

	public UserCategory getUserCategory(int id) {
		return (UserCategory) storeMap.get(new Integer(id));
	}

	public UserCategory getUserCategoryTree() {
		return getUserCategory(ROOT_ID);
	}

	public void moveUserCategory(int id, int newPid) {
		try {
			UserCategory userCategory = getUserCategory(id);
			UserCategory oldParent = getUserCategory(userCategory.getPid());
			UserCategory newParent = getUserCategory(newPid);

			if (newParent.getIsLeaf()) {
				throw new RalasafeException(
						"The target node must not be a leaf.");
			}
			if (userCategory == newParent
					|| isCascadeChild(userCategory.getId(), newParent.getId())) {
				throw new RalasafeException("This move will produce a cycel.");
			}
			// update this node
			userCategory.setPid(newParent.getId());
			updator.updateByIdColumns(userCategory);

			// update cache
			userCategory.setParent(newParent);
			oldParent.getChildren().remove(userCategory);
			newParent.getChildren().add(userCategory);
		} catch (EntityExistException e) {
			throw new RalasafeException(e);
		}
	}

	private boolean isCascadeChild(int pId, int id) {
		Node parent = getUserCategory(pId);
		Node child = getUserCategory(id);

		if (parent != null && child != null) {
			while (child.getParent() != null) {
				if (parent == child.getParent())
					return true;
				else
					child = child.getParent();
			}
		}
		return false;
	}

	public UserCategory copyUserCategory(int sourceId, String newName,
			String newDescription) throws EntityExistException {
		UserCategory original = getUserCategory(sourceId);
		if (original == null) {
			throw new RalasafeException(
					"The user category to copy doesn't exist.");
		}
		if (!original.getIsLeaf()) {
			throw new RalasafeException("Cannot copy user category group.");
		}
		UserCategory uc = new UserCategory();
		uc.setId(newUserCategoryId());
		uc.setName(newName);
		uc.setDescription(newDescription);
		uc.setInstallDate(new Date());
		uc.setFile(uc.getId() + storeFilePostfix);
		uc.setPid(original.getPid());
		uc.setIsLeaf(original.getIsLeaf());

		if (original.getIsLeaf()) {
			// get original node's xml content
			String file = original.getFile();
			if (!StringUtil.isEmpty(file)) {
				String storeFile = storeDir + file;

				try {
					String content = IOUtil.read(storeFile);
					uc.setXmlContent(content);
				} catch (IOException e) {
					throw new RalasafeException(e);
				}
			}

			// update name attribute and write to io system
			if (!StringUtil.isEmpty(uc.getXmlContent())) {
				org.ralasafe.db.sql.xml.UserCategoryType xmlUserCategory;
				try {
					xmlUserCategory = org.ralasafe.db.sql.xml.UserCategory
							.unmarshal(new StringReader(uc.getXmlContent()));
					xmlUserCategory.setName(uc.getName());
					java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
					try {
						xmlUserCategory.marshal(xmlContentWriter);
					} catch (Exception e) {
						throw new RalasafeException(e);
					}
					uc
							.setXmlContent(xmlContentWriter
									.toString());

					// update xml content
					file = storeDir + uc.getFile();
					IOUtil.write(file, StringUtil.keepSpaceInContent(uc
							.getXmlContent(), xmlUserCategory.getRawScript()
							.getContent()));

				} catch (Exception e) {
					throw new RalasafeException(e);
				}
			}
		}

		saver.save(uc);

		// update cache
		loadXmlContent(uc);
		UserCategory parent = (UserCategory) storeMap.get(new Integer(uc
				.getPid()));
		uc.setParent(parent);
		parent.getChildren().add(uc);
		storeMap.put(new Integer(uc.getId()), uc);

		return uc;
	}

	public void addReservedUserCategory(Locale locale) {
		UserCategory uc = new UserCategory();
		uc.setId(UserCategory.RESERVED_USER_CATEGORY_ID);
		uc.setName(Util.getMessage(locale,
				ResourceConstants.RESERVED_USER_CATEGORY_NAME));
		uc.setDescription("");
		uc.setInstallDate(new Date());
		uc.setPid(ROOT_ID);
		uc.setIsLeaf(false);
		try {
			saver.save(uc);
		} catch (EntityExistException e) {
			throw new RalasafeException(e);
		}

		// update cache
		UserCategory parent = (UserCategory) storeMap.get(new Integer(uc
				.getPid()));
		uc.setParent(parent);
		parent.getChildren().add(uc);
		storeMap.put(new Integer(uc.getId()), uc);
	}

	public void addUserCategory(Role role) throws EntityExistException {
		UserCategory userCategory = initUserCategory(role);

		// write xml content
		String file = storeDir + userCategory.getFile();
		try {
			IOUtil.write(file, userCategory.getXmlContent());
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		// deal with name conflict
		boolean nameConflict = true;
		while (nameConflict) {
			if (getUserCategory(userCategory.getName()) == null) {
				nameConflict = false;
			} else {
				// plus "_1"
				userCategory.setName(userCategory.getName() + "_1");
			}
		}
		saver.save(userCategory);

		// update cache
		loadXmlContent(userCategory);
		UserCategory parent = (UserCategory) storeMap.get(new Integer(
				userCategory.getPid()));
		userCategory.setParent(parent);
		parent.getChildren().add(userCategory);
		storeMap.put(new Integer(userCategory.getId()), userCategory);
	}

	private UserCategory initUserCategory(Role role) {
		UserCategory userCategory = new UserCategory();
		userCategory.setId(newUserCategoryId());
		userCategory.setName(role.getName());
		userCategory.setDescription(role.getDescription());
		userCategory.setInstallDate(new Date());
		userCategory.setFile(userCategory.getId() + storeFilePostfix);
		userCategory.setPid(UserCategory.RESERVED_USER_CATEGORY_ID);
		userCategory.setIsLeaf(true);
		// xml content
		org.ralasafe.db.sql.xml.UserCategoryType xmlUserCategory = initReservedXMLUserCategory(role);
		StringWriter sw = new StringWriter();
		try {
			xmlUserCategory.marshal(sw);
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		userCategory.setXmlContent(sw.toString());

		return userCategory;
	}

	private org.ralasafe.db.sql.xml.UserCategoryType initReservedXMLUserCategory(
			Role role) {
		final String ROLE = "role";
		final String MY_ROLES = "myRoles";
		String xmlContent=initXmlContent( MY_ROLES, "" );
		StringReader reader=new StringReader( xmlContent );
		org.ralasafe.db.sql.xml.UserCategoryType userCategory=null;
		try {
			userCategory=org.ralasafe.db.sql.xml.UserCategory.unmarshal( reader );
		} catch( MarshalException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		} catch( ValidationException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		}
		
		userCategory.setName(role.getName());

		// defineVariable1
		org.ralasafe.db.sql.xml.DefineVariable defineVariable1 = new org.ralasafe.db.sql.xml.DefineVariable();
		defineVariable1.setName(MY_ROLES);
		userCategory.addDefineVariable(defineVariable1);
		// defineVariable1-queryRef
		org.ralasafe.db.sql.xml.QueryRef queryRef = new org.ralasafe.db.sql.xml.QueryRef();
		queryRef.setId(Query.RESERVED_QUERY_ID);
		queryRef.setName(Query.RESERVED_QUERY_NAME);
		defineVariable1.setQueryRef(queryRef);

		// defineVariable2
		org.ralasafe.db.sql.xml.DefineVariable defineVariable2 = new org.ralasafe.db.sql.xml.DefineVariable();
		defineVariable2.setName(ROLE);
		userCategory.addDefineVariable(defineVariable2);
		// defineVariable2-simpleValue
		org.ralasafe.db.sql.xml.SimpleValue simpleValue = new org.ralasafe.db.sql.xml.SimpleValue();
		simpleValue.setType(SimpleValueTypeTypeType.INTEGER);
		simpleValue.setContent(role.getId() + "");
		defineVariable2.setSimpleValue(simpleValue);

		// exprGroup
		org.ralasafe.db.sql.xml.ExprGroup exprGroup = new org.ralasafe.db.sql.xml.ExprGroup();
		userCategory.setExprGroup(exprGroup);
		// exprGroup-linker
		exprGroup.setLinker(LinkerType.AND);
		// exprGroup-inExpr
		org.ralasafe.db.sql.xml.InExpr inExpr = new org.ralasafe.db.sql.xml.InExpr();
		org.ralasafe.db.sql.xml.ExprGroupTypeItem item = new org.ralasafe.db.sql.xml.ExprGroupTypeItem();
		item.setInExpr(inExpr);
		exprGroup.addExprGroupTypeItem(0, item);
		// inExpr-variable1
		org.ralasafe.db.sql.xml.Variable1 variable1 = new org.ralasafe.db.sql.xml.Variable1();
		variable1.setName(ROLE);
		inExpr.setVariable1(variable1);
		// inExpr-variable2
		org.ralasafe.db.sql.xml.Variable2 variable2 = new org.ralasafe.db.sql.xml.Variable2();
		variable2.setName(MY_ROLES);
		inExpr.setVariable2(variable2);

		return userCategory;
	}

	public UserCategoryTestResult testUserCategory(
			org.ralasafe.script.UserCategory scriptUserCategory, User user,
			Map context) {
		QueryManager queryManager = Factory.getQueryManager(appName);
		return scriptUserCategory.test(user, context, queryManager);
	}
}
