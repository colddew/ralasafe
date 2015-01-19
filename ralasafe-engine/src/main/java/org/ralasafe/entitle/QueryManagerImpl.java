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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
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
import org.ralasafe.db.sql.QueryFactory;
import org.ralasafe.db.sql.xml.Sql;
import org.ralasafe.db.sql.xml.types.LinkerType;
import org.ralasafe.db.sql.xml.types.SimpleOperatorType;
import org.ralasafe.group.Node;
import org.ralasafe.privilege.UserRole;
import org.ralasafe.user.User;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.IOUtil;
import org.ralasafe.util.StringUtil;

/**
 * Query Manager. Every Application owns query manager and a sql table by itself.
 * If the sql table is not created, then  table will be created by this manager. 
 * 
 * 
 * @author back
 * 
 */
public class QueryManagerImpl implements QueryManager {
	private static final int ROOT_ID = 0;
	private static String storeDir = SystemConstant.getQueryStoreDir();
	/** key/value=id<Integer>/query<org.ralasafe.entitle.Query> */
	private Map storeMap;
	private String appName;
	private Table table;
	private TableSelectorImpl selector;
	private TableSaverImpl saver;
	private TableUpdatorImpl updator;
	private TableDeletorImpl deleter;
	// private QueryFactory translator;
	private Comparator comp;
	private static Log log = LogFactory.getLog(QueryManagerImpl.class);
	private String storeFilePostfix;
	
	public QueryManagerImpl(String appName) {
		this.appName = appName;
		storeFilePostfix = "_" + appName + ".xml";
		storeMap = new HashMap();
		// translator=new QueryFactory();
		// table definition
		TableNewer tableNewer = new TableNewer();
		tableNewer.setTableName(appName + "_query");
		tableNewer.setColumnNames(new String[] { "id", "name", "description",
				"installDate", "fileName", "pid", "isLeaf" });
		tableNewer.setIdColumnNames(new String[] { "id" });
		tableNewer.setUniqueColumnNames(new String[] { "name", "isLeaf" });
		tableNewer.setMappingClass(Query.class.getName());
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
				Query q1 = (Query) o1;
				Query q2 = (Query) o2;
				return q1.getName().compareTo(q2.getName());
			}
		};

		// load all definitions
		loadIntoMemory();
	}

	public Collection getLikelyQueries(String name) {
		List result = new LinkedList();
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();
			String queryName = query.getName();
			if (queryName.indexOf(name) > -1) {
				result.add(query);
			}
		}
		return result;
	}

	public Query getQuery(String name, boolean isLeaf) {
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();
			String queryName = query.getName();
			if (queryName.equals(name) && isLeaf==query.getIsLeaf()) {
				return query;
			}
		}
		return null;
	}

	public Collection checkSameNameQueries(String srcFile) {
		try {
			Sql sql = Sql.unmarshal(new FileReader(srcFile));
			return checkSameNameQueries(sql);
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
	}

	private Collection checkSameNameQueries(Sql sql) {
		org.ralasafe.db.sql.xml.Query[] queries = sql.getQuery();
		Set toCheckNameSet = new HashSet();
		for (int i = 0; i < queries.length; i++) {
			String name = queries[i].getName();
			toCheckNameSet.add(name);
		}
		List sameNames = new LinkedList();
		for (Iterator iter = storeMap.values().iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();
			String queryName = query.getName();
			if (query.getIsLeaf() && toCheckNameSet.contains(queryName)) {
				sameNames.add(queryName);
			}
		}
		return sameNames;
	}

	public void installQueries(String fileUrl, boolean overwrite) {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(table.getId());
			Sql sql = Sql.unmarshal(new FileReader(fileUrl));
			org.ralasafe.db.sql.xml.Query[] xmlQueries = sql.getQuery();
			List saveQueries = new ArrayList(xmlQueries.length);
			Collection sameNames = checkSameNameQueries(sql);
			Set sameNameSet = new HashSet();
			sameNameSet.addAll(sameNames);
			for (int i = 0; i < xmlQueries.length; i++) {
				org.ralasafe.db.sql.xml.Query xmlQuery = xmlQueries[i];
				String name = xmlQuery.getName();
				java.io.StringWriter sw = new java.io.StringWriter();
				xmlQuery.marshal(sw);
				org.ralasafe.db.sql.Query sqlQuery = QueryFactory
						.getQuery(xmlQuery);
				Query query = new Query();

				query.setName(sqlQuery.getName());
				query.setXmlContent(sw.toString());
				query.setInstallDate(new Date());
				query.setPid(ROOT_ID);// as a child of root node
				query.setIsLeaf(true);

				if (sameNameSet.contains(name)) {
					if (overwrite) {
						// if overwrite, update this query
						Query orginal = getQuery(name,true);
						query.setId(orginal.getId());
						query.setFile(query.getId() + storeFilePostfix);
						query.setIsLeaf(orginal.getIsLeaf());
						query.setPid(orginal.getPid());
						updator.updateByIdColumns(query);
					}
				} else {
					query.setId(newQueryId());
					query.setFile(query.getId() + storeFilePostfix);
					saveQueries.add(query);
				}

				// write xml content into io system
				String storeFile = storeDir + query.getFile();
				IOUtil.write(storeFile, query.getXmlContent());
			}
			saver.batchSave(conn, saveQueries);
			loadIntoMemory();
		} catch (DBLevelException e) {
			log.error("", e);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new RalasafeException(e1);
			}
			throw new RalasafeException(e);
		} catch (IOException e) {
			// io exception, roll back
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

	private int newQueryId() {
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
		Collection queries = selector.select(new SelectCondition(), null);
		storeMap.clear();

		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();

			if (query.getIsLeaf()) {
				loadXmlContent(query);
			}
			storeMap.put(new Integer(query.getId()), query);
		}

		// build tree
		// first, add a root node
		Query rootQuery = new Query();
		rootQuery.setId(ROOT_ID);
		rootQuery.setPid(-1);
		rootQuery.setName("root");
		rootQuery.setIsLeaf(false);
		storeMap.put(new Integer(ROOT_ID), rootQuery);

		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			Query query = (Query) iter.next();
			// set parent
			int pid = query.getPid();
			Query pNode = (Query) storeMap.get(new Integer(pid));
			query.setParent(pNode);

			// set as a child
			if (pNode != null) {
				Collection children = pNode.getChildren();
				if (children == null) {
					children = new LinkedList();
					pNode.setChildren(children);
				}
				children.add(query);
			}
		}
	}

	private void loadXmlContent(Query query) {
		// read xml content from io system
		String file = query.getFile();
		if (!StringUtil.isEmpty(file)) {
			String storeFile = storeDir + file;

			try {
				String content = IOUtil.read(storeFile);
				query.setXmlContent(content);
			} catch (IOException e) {
				throw new RalasafeException(e);
			}
		}

		if (!StringUtil.isEmpty(query.getXmlContent())) {
			org.ralasafe.db.sql.xml.QueryType xmlQuery;
			try {
				xmlQuery = org.ralasafe.db.sql.xml.Query
						.unmarshal(new StringReader(query.getXmlContent()));
			} catch (Exception e) {
				throw new RalasafeException(e);
			}
			org.ralasafe.db.sql.Query sqlQuery = QueryFactory.getQuery(xmlQuery);
			query.setSqlQuery(sqlQuery);
		}
	}

	/**
	 * remove xml head likes "<?xml version="1.0" encoding="UTF-8"?>"
	 */
	private String removeXMLHead(String xml) {
		int index = xml.indexOf("?>");
		if (index > 0)
			return xml.substring(index + 2);
		else
			return xml;
	}

	public Collection getAllQueries() {
		Collection values = storeMap.values();
		List result = new ArrayList(values.size());
		result.addAll(values);
		Collections.sort(result, comp);
		return result;
	}

	public Query addQuery(int pid, String name, String description,
			boolean isLeaf) throws EntityExistException {
		if (isLeaf) {
			return addLeafQuery(pid, name, description);
		} else {
			return addBranchQuery(pid, name, description);
		}
	}

	private Query addLeafQuery(int pid, String name, String description)
			throws EntityExistException {
		Query query = new Query();
		query.setId(newQueryId());
		query.setName(name);
		query.setDescription(description);
		query.setInstallDate(new Date());
		query.setXmlContent(initXmlContent(name, description));
		query.setFile(query.getId() + storeFilePostfix);
		query.setPid(pid);
		query.setIsLeaf(true);

		// write xml content to io system
		String file = storeDir + query.getFile();
		try {
			IOUtil.write(file, query.getXmlContent());
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		saver.save(query);

		// update cache
		loadXmlContent(query);
		Query parent = (Query) storeMap.get(new Integer(query.getPid()));
		query.setParent(parent);
		parent.getChildren().add(query);
		storeMap.put(new Integer(query.getId()), query);

		return query;
	}

	private String initXmlContent(String name, String description) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>          \r\n"
		+"<query name=\"" + name +
				"\" isRawSQL=\"false\" type=\"sql\">\r\n"
		+"    <select isDistinct=\"false\"/>                 \r\n"
		+"    <from/>                                        \r\n"
		+"    <where>                                        \r\n"
		+"        <expressionGroup linker=\"AND\"/>          \r\n"
		+"    </where>                                       \r\n"
		+"    <groupBy/>                                     \r\n"
		+"    <orderBy/>                                     \r\n"
		+"    <rawSQL>                                       \r\n"
		+"        <content></content>                        \r\n"
		+"        <select isDistinct=\"false\"/>             \r\n"
		+"    </rawSQL>                                      \r\n"
		+"    <storedProcedure>                              \r\n"
		+"        <content></content>                        \r\n"
		+"        <select isDistinct=\"false\"/>             \r\n"
		+"    </storedProcedure>                             \r\n"
		+"</query>";
	}

	public void deleteQuery(int id) {
		// check cache store
		Query query = (Query) storeMap.get(new Integer(id));

		if (query == null) {
			return;
		}

		if (query.getIsLeaf()) {
			deleteSingleQuery(id);
		} else {
			Collection children = query.getChildren();
			Iterator itr = children.iterator();
			int[] ids = new int[children.size()];
			for (int i = 0; i < ids.length; i++) {
				Query child = (Query) itr.next();
				ids[i] = child.getId();
			}
			for (int i = 0; i < ids.length; i++) {
				deleteQuery(ids[i]);
			}

			deleteSingleQuery(id);
		}
	}

	private void deleteSingleQuery(int id) {
		Query hint = new Query();
		hint.setId(id);
		deleter.deleteByIdColumns(hint);
		// delete cascade things
		EntitleManager entitleManager = Factory.getEntitleManager(appName);
		entitleManager.deleteCascadeEntitlementByQuery(id);

		// delete xml file
		Query query = (Query) storeMap.get(new Integer(id));

		if (query != null && !StringUtil.isEmpty(query.getFile())) {
			new File(storeDir + query.getFile()).delete();
		}

		// update cache
		Query parent = (Query) storeMap.get(new Integer(query.getPid()));
		query.setParent(null);
		query.setChildren(null);
		parent.getChildren().remove(query);
		storeMap.remove(new Integer(query.getId()));
	}

	public Query getQuery(int id) {
		return (Query) storeMap.get(new Integer(id));
	}

	public void updateQuery(int id, String name, String description)
			throws EntityExistException {
		Query original = getQuery(id);
		if (original == null) {
			return;
		}
		original.setName(name);
		original.setDescription(description);

		updator.updateByIdColumns(original);
		
		String xmlContent=original.getXmlContent();
		if( StringUtil.isEmpty( xmlContent ) ) {
			// no xml file needs be updated
			return;
		}
		
		org.ralasafe.db.sql.xml.QueryType xmlQuery;
		try {
			xmlQuery = org.ralasafe.db.sql.xml.Query
					.unmarshal(new StringReader( xmlContent ));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		xmlQuery.setName( name );
		
		updateQuery( id, (org.ralasafe.db.sql.xml.Query)xmlQuery );
	}

	public void updateQuery(int id, org.ralasafe.db.sql.xml.Query content)
			throws EntityExistException {
		Query original = getQuery(id);
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
					.getXmlContent(), content.getRawSQL().getContent()));
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		// update cache
		loadXmlContent(original);
	}

	private Query addBranchQuery(int pid, String name, String description)
			throws DBLevelException, EntityExistException {
		Query query = new Query();
		query.setId(newQueryId());
		query.setName(name);
		query.setDescription(description);
		query.setInstallDate(new Date());
		query.setPid(pid);
		query.setIsLeaf(false);

		saver.save(query);

		// update cache
		Query parent = (Query) storeMap.get(new Integer(query.getPid()));
		query.setParent(parent);
		parent.getChildren().add(query);
		storeMap.put(new Integer(query.getId()), query);

		return query;
	}

	public Query getQueryTree() {
		return getQuery(ROOT_ID);
	}

	public void moveQuery(int id, int newPid) {
		try {
			Query query = getQuery(id);
			Query oldParent = getQuery(query.getPid());
			Query newParent = getQuery(newPid);

			if (newParent.getIsLeaf()) {
				throw new RalasafeException("The target node may not be a leaf");
			}
			if (query == newParent
					|| isCascadeChild(query.getId(), newParent.getId())) {
				throw new RalasafeException("This move will prodce a cycle.");
			}
			// update node
			query.setPid(newParent.getId());
			updator.updateByIdColumns(query);

			// update cache
			query.setParent(newParent);
			oldParent.getChildren().remove(query);
			newParent.getChildren().add(query);
		} catch (EntityExistException e) {
			throw new RalasafeException(e);
		}
	}

	private boolean isCascadeChild(int pId, int id) {
		Node parent = getQuery(pId);
		Node child = getQuery(id);

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

	public Query copyQuery(int sourceId, String newName, String newDescription)
			throws EntityExistException {
		Query original = getQuery(sourceId);
		if (original == null) {
			throw new RalasafeException("The query to copy doesn't exist.");
		}
		if (!original.getIsLeaf()) {
			throw new RalasafeException("Cannot copy query group.");
		}
		Query query = new Query();
		query.setId(newQueryId());
		query.setName(newName);
		query.setDescription(newDescription);
		query.setInstallDate(new Date());
		query.setFile(query.getId() + storeFilePostfix);
		query.setPid(original.getPid());
		query.setIsLeaf(original.getIsLeaf());

		if (query.getIsLeaf()) {
			// read original node's xml file content
			String file = original.getFile();
			if (!StringUtil.isEmpty(file)) {
				String storeFile = storeDir + file;

				try {
					String content = IOUtil.read(storeFile);
					query.setXmlContent(content);
				} catch (IOException e) {
					throw new RalasafeException(e);
				}
			}

			// update name attribute, then write to io system
			if (!StringUtil.isEmpty(query.getXmlContent())) {
				org.ralasafe.db.sql.xml.QueryType xmlQuery;
				try {
					xmlQuery = org.ralasafe.db.sql.xml.Query
							.unmarshal(new StringReader(query.getXmlContent()));
					xmlQuery.setName(query.getName());
					java.io.StringWriter xmlContentWriter = new java.io.StringWriter();
					try {
						xmlQuery.marshal(xmlContentWriter);
					} catch (Exception e) {
						throw new RalasafeException(e);
					}
					query.setXmlContent(xmlContentWriter.toString());

					// update xml content
					file = storeDir + query.getFile();
					IOUtil.write(file, StringUtil
							.keepSpaceInContent(query.getXmlContent(), xmlQuery
									.getRawSQL().getContent()));
				} catch (Exception e) {
					throw new RalasafeException(e);
				}
				org.ralasafe.db.sql.Query sqlQuery = QueryFactory
						.getQuery(xmlQuery);
				query.setSqlQuery(sqlQuery);
			}
		}

		saver.save(query);

		// update cache
		loadXmlContent(query);
		Query parent = (Query) storeMap.get(new Integer(query.getPid()));
		query.setParent(parent);
		parent.getChildren().add(query);
		storeMap.put(new Integer(query.getId()), query);

		return query;
	}

	public void addReservedQuery(String userTypeName) {
		Query query = initReservedQuery(userTypeName);

		// write xml content
		String file = storeDir + query.getFile();
		try {
			IOUtil.write(file, query.getXmlContent());
		} catch (IOException e) {
			throw new RalasafeException(e);
		}

		try {
			saver.save(query);
		} catch (EntityExistException e) {
			throw new RalasafeException(e);
		}

		// update cache
		loadXmlContent(query);
		Query parent = (Query) storeMap.get(new Integer(query.getPid()));
		query.setParent(parent);
		parent.getChildren().add(query);
		storeMap.put(new Integer(query.getId()), query);
	}

	private Query initReservedQuery(String userTypeName) {
		Query query = new Query();
		query.setId(Query.RESERVED_QUERY_ID);
		query.setName(Query.RESERVED_QUERY_NAME);
		query.setDescription(Query.RESERVED_QUERY_DESCRIPTION);
		query.setInstallDate(new Date());
		query.setFile(query.getId() + storeFilePostfix);
		query.setPid(0);
		query.setIsLeaf(true);
		// xml content
		org.ralasafe.db.sql.xml.QueryType xmlQuery = initReservedXMLQuery(userTypeName);
		StringWriter sw = new StringWriter();
		try {
			xmlQuery.marshal(sw);
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		query.setXmlContent(sw.toString());

		return query;
	}

	private org.ralasafe.db.sql.xml.QueryType initReservedXMLQuery(String userTypeName) {
		String tableName=appName + "_" + userTypeName + "_userrole";
		String alias="t1";
		StringReader reader=new StringReader(initXmlContent( "", "" ));
		org.ralasafe.db.sql.xml.QueryType query;
		try {
			query=org.ralasafe.db.sql.xml.Query.unmarshal( reader );
		} catch( MarshalException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		} catch( ValidationException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		}
		query.setName(Query.RESERVED_QUERY_NAME);
		query.setDs(DBPower.getDefaultDsName());
		query.setIsRawSQL(false);
		// rawSQL
		org.ralasafe.db.sql.xml.RawSQL rawSQL = new org.ralasafe.db.sql.xml.RawSQL();
		rawSQL.setContent("");
		query.setRawSQL(rawSQL);

		// queryTypeSequence
		org.ralasafe.db.sql.xml.QueryTypeSequence queryTypeSequence = new org.ralasafe.db.sql.xml.QueryTypeSequence();
		query.setQueryTypeSequence(queryTypeSequence);

		// select
		org.ralasafe.db.sql.xml.Select select = new org.ralasafe.db.sql.xml.Select();
		query.getQueryTypeSequence().setSelect(select);
		select.setIsDistinct(false);
		select.setMappingClass( UserRole.class.getName() );
		// select-column
		org.ralasafe.db.sql.xml.Column roleIdColumn = new org.ralasafe.db.sql.xml.Column();
		roleIdColumn.setName("roleid");
		roleIdColumn.setTableAlias(alias);
		roleIdColumn.setJavaType( "int" );
		roleIdColumn.setProperty( "roleId" );
		select.addColumn(0, roleIdColumn);

		// from
		org.ralasafe.db.sql.xml.From from = new org.ralasafe.db.sql.xml.From();
		query.getQueryTypeSequence().setFrom(from);
		// from-table
		org.ralasafe.db.sql.xml.Table table = new org.ralasafe.db.sql.xml.Table();
		//table.setSchema("ralasafe");
		// special operate for SQL Server
		if (isSQLServer()) {
			table.setName("dbo." + tableName);
		} else {
			table.setName(tableName);
		}
		table.setAlias(alias);
		from.addTable(0, table);

		// where
		org.ralasafe.db.sql.xml.Where where = new org.ralasafe.db.sql.xml.Where();
		query.getQueryTypeSequence().setWhere(where);
		// where-expressionGroup
		org.ralasafe.db.sql.xml.ExpressionGroup expressionGroup = new org.ralasafe.db.sql.xml.ExpressionGroup();
		where.setExpressionGroup(expressionGroup);
		// expressionGroup-linker
		expressionGroup.setLinker(LinkerType.AND);
		// expressionGroup-binaryExpression
		org.ralasafe.db.sql.xml.BinaryExpression binaryExpression = new org.ralasafe.db.sql.xml.BinaryExpression();
		org.ralasafe.db.sql.xml.ExpressionGroupTypeItem item = new org.ralasafe.db.sql.xml.ExpressionGroupTypeItem();
		item.setBinaryExpression(binaryExpression);
		expressionGroup.addExpressionGroupTypeItem(0, item);
		// binaryExpression-operand1
		org.ralasafe.db.sql.xml.Operand1 operand1 = new org.ralasafe.db.sql.xml.Operand1();
		binaryExpression.setOperand1(operand1);
		// operand1-column
		org.ralasafe.db.sql.xml.Column userIdCcolumn = new org.ralasafe.db.sql.xml.Column();
		userIdCcolumn.setName("userid");
		userIdCcolumn.setTableAlias(alias);
		org.ralasafe.db.sql.xml.Operand operand = new org.ralasafe.db.sql.xml.Operand();
		operand.setColumn(userIdCcolumn);
		operand1.setOperand(operand);
		// binaryExpression-simpleOperator
		org.ralasafe.db.sql.xml.Operator simpleOperator = new org.ralasafe.db.sql.xml.Operator();
		simpleOperator.setSimpleOperator(SimpleOperatorType.valueOf("="));
		binaryExpression.setOperator(simpleOperator);
		// binaryExpression-operand2
		org.ralasafe.db.sql.xml.Operand2 operand2 = new org.ralasafe.db.sql.xml.Operand2();
		binaryExpression.setOperand2(operand2);
		// operand2-userValue
		org.ralasafe.db.sql.xml.UserValue userValue = new org.ralasafe.db.sql.xml.UserValue();
		userValue.setKey(User.idFieldName);
		operand = new org.ralasafe.db.sql.xml.Operand();
		org.ralasafe.db.sql.xml.Value value = new org.ralasafe.db.sql.xml.Value();
		value.setUserValue(userValue);
		operand.setValue(value);
		operand2.setOperand(operand);

		return query;
	}

	private boolean isSQLServer() {
		return DBUtil.getDatabaseProductName(DBPower.getConnection(null))
				.equals(DBUtil.SQLSERVER);
	}

	public QueryTestResult testQuery(org.ralasafe.db.sql.Query query, User user,
			Map context, int start, int limit) {
		return query.test(user, context, start, limit);
	}

	public Query cloneQuery(Query query) {
		Query newQuery = new Query();
		newQuery.setDescription(query.getDescription());
		newQuery.setFile(query.getFile());
		newQuery.setId(query.getId());
		newQuery.setInstallDate(query.getInstallDate());
		newQuery.setIsLeaf(query.getIsLeaf());
		newQuery.setName(query.getName());
		newQuery.setPid(query.getPid());
		loadXmlContent(newQuery);

		return newQuery;
	}
}
