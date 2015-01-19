/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * Group part of a sql.
 * 
 * @author jbwang
 *
 */
public class GroupPart {
	private String[] columnNames;
	
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames( String[] columnNames ) {
		this.columnNames = columnNames;
	}
}
