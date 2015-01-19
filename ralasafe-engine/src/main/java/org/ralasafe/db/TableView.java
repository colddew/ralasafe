/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.ArrayList;
import java.util.Collection;

public class TableView {
	private String schema;
	private String name;
	private Collection columnViews = new ArrayList();
	private Collection pkColumnViews = new ArrayList();

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

	public Collection getColumnViews() {
		return columnViews;
	}

	public void setColumnViews(Collection columnViews) {
		this.columnViews = columnViews;
	}

	public Collection getPkColumnViews() {
		return pkColumnViews;
	}

	public void setPkColumnViews(Collection pkColumnViews) {
		this.pkColumnViews = pkColumnViews;
	}
}
