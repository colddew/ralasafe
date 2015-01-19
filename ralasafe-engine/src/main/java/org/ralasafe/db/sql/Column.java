/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.util.StringUtil;

public class Column implements Operand {
	private String name;
	private String tableAlias;
	private String function;
	private String sqlType;
	private String javaType;
	private String property;
	private String order;
	private boolean readOnly;
	private ColumnAdapter adapter;

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String toSQL() {
		StringBuffer buf = new StringBuffer();
		if (function != null && !function.equals("")) {
			buf.append(" ").append(function).append("(").append(tableAlias)
					.append(".").append(name).append(") ");
		} else {
			if( StringUtil.isEmpty( tableAlias ) ) {
				buf.append(" ").append(name).append(" ");
			} else {				
				buf.append(" ").append(tableAlias)
					.append(".").append(name).append(" ");
			}
		}
		return buf.toString();
	}

	public String getAlias() {
		if (StringUtil.isEmpty(tableAlias)) {
			return name;
		} else {
			return tableAlias + "_" + name;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public ColumnAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ColumnAdapter adapter) {
		this.adapter = adapter;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
}
