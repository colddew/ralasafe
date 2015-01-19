/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.privilege.Role;
import org.ralasafe.user.User;

public interface UserCategoryManager {

	public abstract void installCategories(String fileUrl, boolean overwrite);

	/**
	 * Search leaf nodes which has the names in definition file. 
	 * Generally, before install definition xml file, find out same name queries, and ask operator overwrite or not.
	 * 
	 * @param fileUrl
	 * @return Collection< String >
	 */
	public abstract Collection checkSameNameUserCategories(String fileUrl);

	public abstract UserCategory getUserCategory(int id);

	/**
	 * Have likely name nodes, include leaf and non-leaf nodes.
	 * 
	 * @param name
	 * @return Collection< {@linkplain UserCategory} >
	 */
	public abstract Collection getLikelyUserCategories(String name);

	/**
	 * Get all nodes, include ROOT node(id=0, pid=-1)
	 * 
	 * @return
	 */
	public abstract Collection getAllUserCategories();

	/**
	 * If the user category is a leaf node, delete it; else delete it cascade.
	 * 
	 * @param id
	 */
	public abstract void deleteUserCategory(int id);

	public abstract UserCategory addUserCategory(int pid, String name,
			String description, boolean isLeaf) throws EntityExistException;

	public abstract void updateUserCategory(int id, String name,
			String description) throws EntityExistException;

	public abstract void updateUserCategory(int id,
			org.ralasafe.db.sql.xml.UserCategory content)
			throws EntityExistException;

	/**
	 * Get user category tree, return ROOT node.
	 * 
	 * @return
	 */
	public abstract UserCategory getUserCategoryTree();

	public void moveUserCategory(int id, int newPid);

	public UserCategory copyUserCategory(int sourceId, String newName,
			String newDescription) throws EntityExistException;

	/**
	 * Create Reserved user category: role.
	 * 
	 * @throws EntityExistException
	 */
	public abstract void addReservedUserCategory(Locale locale);

	/**
	 * Create a usercatory means users who own this role.
	 * 
	 * @param role
	 * @throws EntityExistException
	 */
	public abstract void addUserCategory(Role role) throws EntityExistException;

	public abstract UserCategoryTestResult testUserCategory(
			org.ralasafe.script.UserCategory scriptUserCategory, User user,
			Map context);
}
