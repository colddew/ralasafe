/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.Connection;

import org.ralasafe.EntityExistException;

/**
 * Update entity operations to database.
 * 
 * @author back
 *
 */
public interface TableUpdator {
	/**
	 * Table definition. 
	 * 
	 * @param mapping
	 */
	public void setTable( Table mapping );
	
	/**
	 * Update an entity.
	 * If it doesn't exist, throw no exception;
	 * If update operation would violate some constraints, throw EntityExistException
	 * 
	 * @param newValue
	 * @throws EntityExistException
	 */
	public void updateByIdColumns( Object newValue ) throws EntityExistException;
	
	public void updateByIdColumns( Connection conn, Object newValue ) throws EntityExistException;
}
