/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public interface TableCreator {
	/**
	 * Does table exist in a datasource?
	 * @see {@link DBPower#getConnection(long)}
	 * @param table
	 * @return
	 */
	public boolean exist( Table table ) throws DBLevelException;
	
	/**
	 * Create it.
	 * @param table
	 */
	public void create( Table table ) throws DBLevelException;
}
