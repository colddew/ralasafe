/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

public interface SQLElement {
 
	/**
	 * Generate SQL.
	 * @return
	 */
	public abstract String toSQL();
}
 
