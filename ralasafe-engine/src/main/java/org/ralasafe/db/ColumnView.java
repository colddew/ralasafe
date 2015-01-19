/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * Class for ui, display sql table column.
 */
public class ColumnView {
	private String name;
	private String sqlType;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name=name;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType( String sqlType ) {
		this.sqlType=sqlType;
	}
}
