/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.metadata.user;

import org.ralasafe.Checker;

public class FieldMetadata {
	/**
	 * user field name
	 */
	private String name;
	private String columnName;
	private String sqlType;
	private String javaType;
	private String displayName;
	/**
	 * Does show in ralsafe designer's user panel?
	 */
	private boolean show;
	private Checker checkers[];

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public Checker[] getCheckers() {
		return checkers;
	}

	public void setCheckers(Checker[] checkers) {
		this.checkers = checkers;
	}
}
