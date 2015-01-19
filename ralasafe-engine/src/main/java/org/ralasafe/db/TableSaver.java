/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.Connection;
import java.util.Collection;

import org.ralasafe.EntityExistException;

/**
 * Save entity operation to database.
 */
public interface TableSaver {
	/**
	 * Table definition.
	 * @param mapping
	 */
	public void setTable( Table mapping );
	public void save( Object o ) throws EntityExistException, DBLevelException;
	public int[] batchSave( Object[] os ) throws DBLevelException;
	public Collection batchSave( Collection coll )	throws DBLevelException;
	
	public void save( Connection conn, Object o ) throws EntityExistException, DBLevelException;
	public int[] batchSave( Connection conn, Object[] os )	throws DBLevelException;
	public Collection batchSave( Connection conn, Collection coll )	throws DBLevelException;
}
