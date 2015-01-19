/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Collection;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.user.User;

/**
 * Query manager. Id is query's pk, name and leaf are unique index.
 * 
 * @author back
 * 
 */
public interface QueryManager {
	/**
	 * Have likely name nodes, include leaf and non-leaf nodes.
	 * 
	 * @param name
	 * @return Collection< {@linkplain Query} >
	 */
	public abstract Collection getLikelyQueries(String name);

	/**
	 * Search leaf nodes which has the names in definition file. 
	 * Generally, before install definition xml file, find out same name queries, and ask operator overwrite or not.
	 */
	public abstract Collection checkSameNameQueries(String fileUrl);

	public abstract void installQueries(String fileUrl, boolean overwrite);

	/**
	 * Get all nodes, include ROOT node(id=0, pid=-1)
	 * 
	 * @return
	 */
	public abstract Collection getAllQueries();

	public abstract Query getQuery(int id);

	/**
	 * Get a query.
	 * 
	 * @param name
	 * @param isLeaf
	 * @return
	 */
	public abstract Query getQuery(String name, boolean isLeaf);

	/**
	 * Add a query.
	 * 
	 * @throws EntityExistException
	 */
	public abstract Query addQuery(int pid, String name, String description,
			boolean isLeaf) throws EntityExistException;

	public abstract void updateQuery(int id, String name, String description)
			throws EntityExistException;

	public abstract void updateQuery(int id, org.ralasafe.db.sql.xml.Query content)
			throws EntityExistException;

	/**
	 * If the query is a leaf node, delete it; else delete it cascade.
	 * 
	 * @param id
	 */
	public abstract void deleteQuery(int id);

	/**
	 * Get query tree, return ROOT node.
	 * 
	 * @return
	 */
	public abstract Query getQueryTree();

	public void moveQuery(int id, int newPid);

	public Query copyQuery(int sourceId, String newName, String newDescription)
			throws EntityExistException;

	/**
	 * Create Reserved query: get current user's role.
	 * 
	 * @throws EntityExistException
	 */
	public abstract void addReservedQuery(String userTypeName);

	public abstract QueryTestResult testQuery(org.ralasafe.db.sql.Query query,
			User user, Map context, int start, int limit);

	public abstract Query cloneQuery(Query query);
}
