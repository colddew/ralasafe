/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Collection;

/**
 * WARNING!!!!
 * We will deprecate it later.
 */
public class InnerWhereElement implements WhereElement {
	private Column[] columns;
	private String[] innerTableColumnNames;
	//private Comparator comparator;
	private String tableName;
	private Collection values;
	
	public Column[] getColumns() {
		return columns;
	}
	public void setColumns( Column[] columns ) {
		this.columns=columns;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName( String tableName ) {
		this.tableName = tableName;
	}
	public Collection getValues() {
		return values;
	}
	public void setValues( Collection values ) {
		this.values = values;
	}
	public String[] getInnerTableColumnNames() {
		if( innerTableColumnNames == null ) {
			innerTableColumnNames=new String[columns.length];
			for( int i=0; i<columns.length; i++ ) {
				Column column=columns[i];
				innerTableColumnNames[i]=column.getName();
			}
		}
		return innerTableColumnNames;
	}
	public void setInnerTableColumnNames( String[] innerTableColumnNames ) {
		this.innerTableColumnNames = innerTableColumnNames;
	}
}
