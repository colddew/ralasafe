/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * WARN!!!!
 * We will deprecate it later.
 * 
 * <p>
 * Where condition likes: where areaId in( select areaid from area where areaid=? or areasuperid=? )
 * </p>
 * @author back
 *
 */
public class PstmtInWhereElement implements WhereElement {
	private String name;
	private Object hint;
	private String[] columnNames;
	private Column[] pstmtColumns;
	private String inSql;
	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
	public Object getHint() {
		return hint;
	}
	public void setHint( Object hint ) {
		this.hint=hint;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames( String[] columnNames ) {
		this.columnNames=columnNames;
	}
	public Column[] getPstmtColumns() {
		return pstmtColumns;
	}
	public void setPstmtColumns( Column[] pstmtColumns ) {
		this.pstmtColumns=pstmtColumns;
	}
	public String getInSql() {
		return inSql;
	}
	public void setInSql( String inSql ) {
		this.inSql=inSql;
	}
}
