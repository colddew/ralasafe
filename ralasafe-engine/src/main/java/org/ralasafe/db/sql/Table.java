/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import org.ralasafe.util.StringUtil;

public class Table implements SQLElement {
	private String schema;
	private String name;
	private String alias;

	public String toSQL() {
		StringBuffer buf = new StringBuffer();

		buf.append(" ");
		if (!StringUtil.isEmpty(schema)) {
			buf.append(schema).append(".");
		}
		buf.append(name).append(" ").append(alias).append(" ");

		return buf.toString();
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
