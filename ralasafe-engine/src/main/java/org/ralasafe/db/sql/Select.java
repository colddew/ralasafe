/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.ralasafe.ObjectNewer;
import org.ralasafe.util.StringUtil;

public class Select implements SQLElement {
	private ArrayList columns = new ArrayList();
	private boolean isDistinct;
	private String mappingClass;
	private ObjectNewer objectNewer;

	public String toSQL() {
		StringBuffer buf = new StringBuffer();
		buf.append(" SELECT ");
		if (isDistinct) {
			buf.append(" DISTINCT ");
		}
		if (columns.size() > 0) {
			Iterator itr = columns.iterator();
			Column column = (Column) itr.next();
			buf.append(column.toSQL()).append(" AS " + column.getAlias() + " ");
			while (itr.hasNext()) {
				column = (Column) itr.next();
				buf.append(",").append(column.toSQL()).append(
						" AS " + column.getAlias() + " ");
			}
		}
		buf.append(" ");
		return buf.toString();
	}

	public ArrayList getColumns() {
		return columns;
	}

	public void setColumns(ArrayList columns) {
		this.columns = columns;
	}

	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct(boolean isDistinct) {
		this.isDistinct = isDistinct;
	}

	public String getMappingClass() {
		return mappingClass;
	}

	public void setMappingClass(String mappingClass) {
		this.mappingClass = mappingClass;
	}

	public ObjectNewer getObjectNewer() {
		return objectNewer;
	}

	public void setObjectNewer(ObjectNewer objectNewer) {
		this.objectNewer = objectNewer;
	}

	public Column findColumnByProperty(String property) {
		int size = columns.size();
		for (int i = 0; i < size; i++) {
			Column column = (Column) columns.get(i);
			if (property.equals(column.getProperty())) {
				return column;
			}
		}
		return null;
	}

	public Collection getFields() {
		int size = columns.size();
		ArrayList fields = new ArrayList(size);
		for (int i = 0; i < size; i++) {
			Column column = (Column) columns.get(i);
			if (StringUtil.isEmpty(column.getProperty())) {
				fields.add(column.getName());
			} else {
				fields.add(column.getProperty());
			}
		}
		return fields;
	}

	public Collection getReadOnlyFields() {
		int size = columns.size();
		ArrayList readOnlyFields = new ArrayList(size);
		for (int i = 0; i < size; i++) {
			Column column = (Column) columns.get(i);
			if (column.isReadOnly()) {
				if (StringUtil.isEmpty(column.getProperty())) {
					readOnlyFields.add(column.getName());
				} else {
					readOnlyFields.add(column.getProperty());
				}
			}
		}
		return readOnlyFields;
	}
}
