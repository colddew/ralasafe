/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.Connection;
import java.util.Collection;

import org.ralasafe.ObjectNewer;

/**
 * WARNING!!! We will change this API later, to use org.ralasafe.db.sql class instead.
 */
public interface TableSelector {
	
	public void setTable( Table mapping );

	/**
	 * When read resultset, we create a java object for every row.
	 *  
	 * @param objectNewer   this object is generated from this ObjectNewer
	 */
	public void setObjectNewer( ObjectNewer objectNewer );

	/**
	 * Query by condition.
	 * 
	 * @param cdtn            nullable
	 * @param o               nullable, when preparedstatement, 
	 *                        read context value from this object
	 * @return                Collection< mappingClass >, if no record, return empty collection not null
	 * @throws DBLevelException 
	 */
	public Collection select( SelectCondition cdtn, Object o ) throws DBLevelException;

	public Collection select( Connection conn, SelectCondition cdtn,
			Object o ) throws DBLevelException;

	/**
	 * Find entity by id. If not found, return null.
	 * 
	 * @param o           read id from this object, object type is mappingClass
	 * @return
	 * @throws DBLevelException
	 */
	public Object selectByIdColumns( Object o ) throws DBLevelException;

	public Object selectByIdColumns( Connection conn, Object o )
			throws DBLevelException;

	/**
	 * Find entity by unique fields. If not found, return null.
	 * 
	 * @param mapping
	 * @param uniqueColumnValues
	 * @return
	 * @throws DBLevelException
	 */
	public Object selectByUniqueColumns( Object o )
			throws DBLevelException;

	public Object selectByUniqueColumns( Connection conn,
			Object o ) throws DBLevelException;
	
	public Collection selectByPage( SelectCondition cdtn,
			Object o,
			int fromIndex, int pageSize ) throws DBLevelException;
	public Collection selectByPage( Connection conn, SelectCondition cdtn,
			Object o,
			int fromIndex, int pageSize ) throws DBLevelException;
	public int selectCount( SelectCondition cdtn, Object o );
	public int selectCount( Connection conn, SelectCondition cdtn, Object o );
	public boolean isExistByIdColumns( Object o );
	public boolean isExistByIdColumns( Connection conn, Object o );
	public boolean isExistByUniqueColumns( Object o );
	public boolean isExistByUniqueColumns( Connection conn, Object o );
}
