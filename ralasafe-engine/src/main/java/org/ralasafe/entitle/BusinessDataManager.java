/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Collection;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.user.User;

public interface BusinessDataManager {

	/**
	 * Install business data into system, the position is direct child node of root.
	 * 
	 * @param fileUrl        Business data definition file, could contail more than one definition.
	 * @param overwrite
	 */
	public abstract void installBusinessData(String fileUrl, boolean overwrite);

	/**
	 * Compare definition xml file with exist names in system, find all same names(exclude non-leaf node).
	 * 
	 * @param
	 * @return   Collection<String> of node name
	 */
	public abstract Collection checkSameNameBusinessData(String fileUrl);

	public abstract BusinessData getBusinessData(int id);

	/**
	 * Search nodes which has name like given name, include leaf and non-leaf nodes.
	 * 
	 * @param name
	 * @return Collection<BusinessData>
	 */
	public abstract Collection getLikelyBusinessData(String name);

	public abstract BusinessData addBusinessData(int pid, String name,
			String description, boolean isLeaf) throws EntityExistException;

	public abstract void updateBusinessData(int id, String name,
			String description) throws EntityExistException;

	public abstract void updateBusinessData(int id,
			org.ralasafe.db.sql.xml.BusinessData content)
			throws EntityExistException;

	/**
	 * Delete this node, if it's a non-leaf node, delete it's children cascade.
	 * 
	 * @param id
	 */
	public abstract void deleteBusinessData(int id);

	/**
	 * Get all business data, inclide ROOT node. (ROOT node id=0, pid=-1)
	 * 
	 * @return
	 */
	public abstract Collection getAllBusinessData();

	/**
	 * Get ROOT node with tree struction
	 * 
	 * @return
	 */
	public abstract org.ralasafe.entitle.BusinessData getBusinessDataTree();

	public void moveBusinessData(int id, int newPid);

	public BusinessData copyBusinessData(int sourceId, String newName,
			String newDescription) throws EntityExistException;

	public BusinessDataTestResult testBusinessData(
			org.ralasafe.script.BusinessData scriptBusinessData, User user,
			Map context);
}
