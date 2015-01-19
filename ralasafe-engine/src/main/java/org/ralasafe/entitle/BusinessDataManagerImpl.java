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
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
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
import org.ralasafe.db.sql.xml.BusinessDataList;
import org.ralasafe.group.Node;
import org.ralasafe.script.ScriptFactory;
import org.ralasafe.user.User;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.IOUtil;
import org.ralasafe.util.StringUtil;

public class BusinessDataManagerImpl implements BusinessDataManager {
	private static final int ROOT_ID = 0;
	private static String storeDir = SystemConstant.getBusinessDataStoreDir();
	/** key/value=id<Integer>/businessData<org.ralasafe.entitle.BusinessData> */
	private Map storeMap;
	private String appName;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdatorImpl updator;
	private TableDeletorImpl deleter;
	private Comparator comp;
	private String storeFilePostfix;

	private static Log log=LogFactory.getLog( BusinessDataManagerImpl.class );
	
	public BusinessDataManagerImpl(String appName) {
		this.appName = appName;
		storeFilePostfix = "_" + appName + ".xml";

		storeMap = new HashMap();
		// 
		TableNewer tableNewer = new TableNewer();
		tableNewer.setTableName(appName + "_businessdata");
		tableNewer.setColumnNames(new String[] { "id", "name", "description",
				"installDate", "fileName", "pid", "isLeaf" });
		tableNewer.setIdColumnNames(new String[] { "id" });
		tableNewer.setUniqueColumnNames(new String[] { "name" });
		tableNewer.setMappingClass(BusinessData.class.getName());
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
				BusinessData q1 = (BusinessData) o1;
				BusinessData q2 = (BusinessData) o2;
				return q1.getName().compareTo(q2.getName());
			}
		};

		// load all definitions
		loadIntoMemory();
	}

	public Collection checkSameNameBusinessData(String fileUrl) {
		try {
			BusinessDataList root = BusinessDataList.unmarshal(new FileReader(
					fileUrl));
			org.ralasafe.db.sql.xml.BusinessData[] xmlDataList = root
					.getBusinessData();
			return checkSameNameBusinessData(xmlDataList);
		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
	}

	private Collection checkSameNameBusinessData(
			org.ralasafe.db.sql.xml.BusinessData[] dataList) {
		// put to be checked name into set
		Set toCheckNames = new HashSet();
		for (int i = 0; i < dataList.length; i++) {
			String name = dataList[i].getName();
			toCheckNames.add(name);
		}
		
		// compare every node name in store with toCheckNames  
		List sameNames = new ArrayList(dataList.length);
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			BusinessData data = (BusinessData) iter.next();
			String name = data.getName();
			if (data.getIsLeaf() && toCheckNames.contains(name)) {
				sameNames.add(name);
			}
		}
		
		return sameNames;
	}

	public void installBusinessData(String fileUrl, boolean overwrite) {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(table.getId());
			BusinessDataList root = BusinessDataList.unmarshal(new FileReader(
					fileUrl));
			org.ralasafe.db.sql.xml.BusinessData[] xmlDataList = root
					.getBusinessData();
			List saveUcs = new ArrayList(xmlDataList.length);
			Collection sameNames = checkSameNameBusinessData(xmlDataList);
			Set sameNameSet = new HashSet();
			sameNameSet.addAll(sameNames);
			for (int i = 0; i < xmlDataList.length; i++) {
				org.ralasafe.db.sql.xml.BusinessData xmlData = xmlDataList[i];
				String name = xmlData.getName();
				java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
				xmlData.marshal(xmlContentWriter);
				BusinessData data = new BusinessData();

				data.setName(name);
				data.setXmlContent(xmlContentWriter.toString());
				data.setInstallDate(new Date());
				data.setPid(ROOT_ID);// AS a child of ROOT node
				data.setIsLeaf(true);

				if (sameNameSet.contains(name)) {
					if (overwrite) {
						// update this BusinessData
						BusinessData orginal = getBusinessData(name);
						data.setId(orginal.getId());
						data.setFile(data.getId() + storeFilePostfix);
						data.setIsLeaf(orginal.getIsLeaf());
						data.setPid(orginal.getPid());
						updator.updateByIdColumns(data);
					}
				} else {
					data.setId(newBusinessDataId());
					data.setFile(data.getId() + storeFilePostfix);
					saveUcs.add(data);
				}

				// write xml content into file system
				String storeFile = storeDir + data.getFile();
				IOUtil.write(storeFile, data.getXmlContent());
			}
			saver.batchSave(conn, saveUcs);
			
			// definition changed, reload
			loadIntoMemory();
		} catch (DBLevelException e) {
			log.error( "", e );
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RalasafeException(e1);
			}
			throw new RalasafeException(e);
		} catch (IOException e) {
			log.error( "", e );
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RalasafeException(e1);
			}
			throw new RalasafeException(e);
		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

	public Collection getLikelyBusinessData(String name) {
		List result = new LinkedList();
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			BusinessData businessData = (BusinessData) iter.next();
			String businessDataName = businessData.getName();
			if (businessDataName.indexOf(name) > -1) {
				result.add(businessData);
			}
		}
		return result;
	}

	public BusinessData getBusinessData(String name) {
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			BusinessData businessData = (BusinessData) iter.next();
			String businessDataName = businessData.getName();
			if (businessDataName.equals(name)) {
				return businessData;
			}
		}
		return null;
	}

	private int newBusinessDataId() {
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

	/**
	 * Load BusinessData definitions from database into memory.
	 */
	private synchronized void loadIntoMemory() {
		Collection businessDataList = selector.select(new SelectCondition(),
				null);
		storeMap.clear();
		for (Iterator iter = businessDataList.iterator(); iter.hasNext();) {
			BusinessData businessData = (BusinessData) iter.next();

			if (businessData.getIsLeaf()) {
				loadXmlContent(businessData);
			}
			storeMap.put(new Integer(businessData.getId()), businessData);
		}

		// build tree
		// first, add a root node
		BusinessData root = new BusinessData();
		root.setId(ROOT_ID);
		root.setPid(-1);
		root.setName("root");
		root.setIsLeaf(false);
		storeMap.put(new Integer(ROOT_ID), root);

		for (Iterator iter = businessDataList.iterator(); iter.hasNext();) {
			BusinessData current = (BusinessData) iter.next();
			// set parent
			int pid = current.getPid();
			BusinessData pNode = (BusinessData) storeMap.get(new Integer(pid));
			current.setParent(pNode);

			// set as a child
			if (pNode != null) {
				Collection children = pNode.getChildren();
				if (children == null) {
					children = new LinkedList();
					pNode.setChildren(children);
				}
				children.add(current);
			}
		}
	}

	private void loadXmlContent(BusinessData businessData) {
		// Read file system to receive xml content
		String file = businessData.getFile();
		if (!StringUtil.isEmpty(file)) {
			String storeFile = storeDir + file;

			try {
				String content = IOUtil.read(storeFile);
				businessData.setXmlContent(content);
			} catch (IOException e) {
				log.error( "", e );
				throw new RalasafeException(e);
			}
		}

		String xmlContent = businessData.getXmlContent();
		if (!StringUtil.isEmpty(xmlContent)) {
			org.ralasafe.db.sql.xml.BusinessDataType xmlBusinessData;
			try {
				xmlBusinessData = org.ralasafe.db.sql.xml.BusinessData
						.unmarshal(new StringReader(xmlContent));
			} catch (Exception e) {
				log.error( "", e );
				throw new RalasafeException(e);
			}
			org.ralasafe.script.BusinessData scriptBusinessData = ScriptFactory
					.getBusinessData(xmlBusinessData, Factory
							.getQueryManager(appName));
			businessData.setScriptBusinessData(scriptBusinessData);
		}
	}

	/**
	 * Remove xml head like <?xml version="1.0" encoding="UTF-8"?>
	 */
	private String removeXMLHead(String xml) {
		int index = xml.indexOf("?>");
		if (index > 0)
			return xml.substring(index + 2);
		else
			return xml;
	}

	public Collection getAllBusinessData() {
		Collection values = storeMap.values();
		List result = new ArrayList(values.size());
		result.addAll(values);
		Collections.sort(result, comp);
		return result;
	}

	public BusinessData addBusinessData(int pid, String name,
			String description, boolean isLeaf) throws EntityExistException {
		if (isLeaf) {
			return addLeafBusinessData(pid, name, description);
		} else {
			return addBranchBusinessData(pid, name, description);
		}
	}

	private BusinessData addLeafBusinessData(int pId, String name,
			String description) throws EntityExistException {
		BusinessData businessData = new BusinessData();
		businessData.setId(newBusinessDataId());
		businessData.setName(name);
		businessData.setDescription(description);
		businessData.setInstallDate(new Date());
		businessData.setPid(pId);
		businessData.setIsLeaf(true);
		businessData.setXmlContent(initXmlContent(name, description));
		businessData.setFile(businessData.getId() + storeFilePostfix);

		// Write to file system
		String file = storeDir + businessData.getFile();
		try {
			IOUtil.write(file, businessData.getXmlContent());
		} catch (IOException e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
		saver.save(businessData);

		// update cache
		loadXmlContent(businessData);
		BusinessData parent = (BusinessData) storeMap.get(new Integer(
				businessData.getPid()));
		businessData.setParent(parent);
		parent.getChildren().add(businessData);
		storeMap.put(new Integer(businessData.getId()), businessData);

		return businessData;
	}

	private BusinessData addBranchBusinessData(int pid, String name,
			String description) throws DBLevelException, EntityExistException {
		BusinessData businessData = new BusinessData();
		businessData.setId(newBusinessDataId());
		businessData.setName(name);
		businessData.setDescription(description);
		businessData.setInstallDate(new Date());
		businessData.setPid(pid);
		businessData.setIsLeaf(false);

		saver.save(businessData);

		// update cache
		BusinessData parent = (BusinessData) storeMap.get(new Integer(
				businessData.getPid()));
		businessData.setParent(parent);
		parent.getChildren().add(businessData);
		storeMap.put(new Integer(businessData.getId()), businessData);

		return businessData;
	}

	private String initXmlContent(String name, String description) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>      \r\n"
		+"<businessData name=\"" + name +
				"\" isRawScript=\"false\">\r\n"
		+"    <exprGroup linker=\"AND\"/>                 \r\n"
		+"    <rawScript>                                 \r\n"
		+"        <content></content>                     \r\n"
		+"    </rawScript>                                \r\n"
		+"</businessData>";
	}

	public void deleteBusinessData(int id) {
		// Check cache store
		BusinessData businessData = (BusinessData) storeMap
				.get(new Integer(id));
		if (businessData == null) {
			return;
		}

		if (businessData.getIsLeaf()) {
			deleteSingleBusinessData(id);
		} else {
			Collection children = businessData.getChildren();
			Iterator itr = children.iterator();
			int[] ids = new int[children.size()];
			for (int i = 0; i < ids.length; i++) {
				BusinessData child = (BusinessData) itr.next();
				ids[i] = child.getId();
			}
			for (int i = 0; i < ids.length; i++) {
				deleteBusinessData(ids[i]);
			}

			deleteSingleBusinessData(id);
		}
	}

	private void deleteSingleBusinessData(int id) {
		BusinessData hint = new BusinessData();
		hint.setId(id);
		deleter.deleteByIdColumns(hint);
		// delete cascade things
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		entitleManager.deleteCascadeEntitlementByBusinessData(id);

		// remove xml file
		BusinessData businessData = (BusinessData) storeMap
				.get(new Integer(id));

		if (businessData != null && !StringUtil.isEmpty(businessData.getFile())) {
			new File(storeDir + businessData.getFile()).delete();
		}

		// update cache
		BusinessData parent = (BusinessData) storeMap.get(new Integer(
				businessData.getPid()));
		businessData.setParent(null);
		businessData.setChildren(null);
		parent.getChildren().remove(businessData);
		storeMap.remove(new Integer(businessData.getId()));
	}

	public BusinessData getBusinessData(int id) {
		return (BusinessData) storeMap.get(new Integer(id));
	}

	public void updateBusinessData(int id, String name, String description)
			throws EntityExistException {
		BusinessData original = getBusinessData(id);
		if (original == null) {
			return;
		}
		original.setName(name);
		original.setDescription(description);

		updator.updateByIdColumns(original);
		
		String xmlContent=original.getXmlContent();
		if( StringUtil.isEmpty( xmlContent ) ) {
			// no xml content to be updated
			return;
		}
		
		org.ralasafe.db.sql.xml.BusinessDataType xmlData;
		try {
			xmlData = org.ralasafe.db.sql.xml.BusinessData
					.unmarshal(new StringReader( xmlContent ));
		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
		xmlData.setName( name );
		
		updateBusinessData( id, (org.ralasafe.db.sql.xml.BusinessData)xmlData );
	}

	public void updateBusinessData(int id,
			org.ralasafe.db.sql.xml.BusinessData content)
			throws EntityExistException {
		BusinessData original = getBusinessData(id);
		if (original == null) {
			return;
		}

		java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
		try {
			content.marshal(xmlContentWriter);
		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
		original.setXmlContent(xmlContentWriter.toString());

		// Update xml file
		String file = storeDir + original.getFile();
		try {
			IOUtil.write(file, StringUtil.keepSpaceInContent(original
					.getXmlContent(), content.getRawScript().getContent()));
		} catch (IOException e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}

		// update cache
		loadXmlContent(original);
	}

	public BusinessData copyBusinessData(int sourceId, String newName,
			String newDescription) throws EntityExistException {
		BusinessData original = getBusinessData(sourceId);
		if (original == null) {
			String msg="No business data found to copy.";
			log.error( msg );
			throw new RalasafeException(msg);
		}
		if (!original.getIsLeaf()) {
			throw new RalasafeException("Can not copy business data group.");
		}
		BusinessData businessData = new BusinessData();
		businessData.setId(newBusinessDataId());
		businessData.setName(newName);
		businessData.setDescription(newDescription);
		businessData.setInstallDate(new Date());
		businessData.setFile(businessData.getId() + storeFilePostfix);
		businessData.setPid(original.getPid());
		businessData.setIsLeaf(original.getIsLeaf());

		if (businessData.getIsLeaf()) {
			// update xml content (only name attribute been changed)
			String file = original.getFile();
			if (!StringUtil.isEmpty(file)) {
				String storeFile = storeDir + file;

				try {
					String content = IOUtil.read(storeFile);
					businessData.setXmlContent(content);
				} catch (IOException e) {
					log.error( "", e );
					throw new RalasafeException(e);
				}
			}

			String xmlContent = businessData.getXmlContent();
			if (!StringUtil.isEmpty(xmlContent)) {
				org.ralasafe.db.sql.xml.BusinessDataType xmlBusinessData;
				try {
					xmlBusinessData = org.ralasafe.db.sql.xml.BusinessData
							.unmarshal(new StringReader(xmlContent));
					xmlBusinessData.setName(businessData.getName());

					java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
					try {
						xmlBusinessData.marshal(xmlContentWriter);
					} catch (Exception e) {
						log.error( "", e );
						throw new RalasafeException(e);
					}
					businessData.setXmlContent(xmlContentWriter
							.toString());

					// update to file system
					file = storeDir + businessData.getFile();
					IOUtil.write(file, StringUtil.keepSpaceInContent(
							businessData.getXmlContent(), xmlBusinessData
									.getRawScript().getContent()));
				} catch (Exception e) {
					log.error( "", e );
					throw new RalasafeException(e);
				}
			}
		}

		saver.save(businessData);

		// update cache
		loadXmlContent(businessData);
		BusinessData parent = (BusinessData) storeMap.get(new Integer(
				businessData.getPid()));
		businessData.setParent(parent);
		parent.getChildren().add(businessData);
		storeMap.put(new Integer(businessData.getId()), businessData);

		return businessData;
	}

	public BusinessData getBusinessDataTree() {
		return getBusinessData(ROOT_ID);
	}

	public void moveBusinessData(int id, int newPid) {
		try {
			BusinessData businessData = getBusinessData(id);
			BusinessData oldParent = getBusinessData(businessData.getPid());
			BusinessData newParent = getBusinessData(newPid);

			if (newParent.getIsLeaf()) {
				throw new RalasafeException("The target node may not be a leaf.");
			}
			if (businessData == newParent
					|| isCascadeChild(businessData.getId(), newParent.getId())) {
				throw new RalasafeException("This node noving will produce a cycle.");
			}
			// update this node
			businessData.setPid(newParent.getId());
			updator.updateByIdColumns(businessData);

			// update cache
			businessData.setParent(newParent);
			oldParent.getChildren().remove(businessData);
			newParent.getChildren().add(businessData);
		} catch (EntityExistException e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
	}

	private boolean isCascadeChild(int pId, int id) {
		Node parent = getBusinessData(pId);
		Node child = getBusinessData(id);

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

	public BusinessDataTestResult testBusinessData(
			org.ralasafe.script.BusinessData scriptBusinessData, User user,
			Map context) {
		QueryManager queryManager = Factory.getQueryManager(appName);
		return scriptBusinessData.test(user, context, queryManager);
	}

}
