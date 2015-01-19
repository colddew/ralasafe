/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.HashMap;
import java.util.Map;

/**
 * A sql table definition.
 * 
 * @author back
 *
 */
public class Table {
	/** internal id, used only for ralasafe */
	private int id;
	private String name;
	private Column[] idColumns;
	private Column[] uniqueColumns;
	private Column[] columns;
	private Column[] exceptIdColumns;
	private String[] columnNames;
	private String[] idColumnNames;
	private String[] uniqueColumnNames;
	private String[] exceptIdColumnNames;
	private String datasourceName;
	private String mappingClass;
	
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName( String dataSourceName ) {
		this.datasourceName=dataSourceName;
	}
	public Column[] getExceptIdColumns() {
		return exceptIdColumns;
	}
	public void setExceptIdColumns( Column[] exceptIdColumns ) {
		this.exceptIdColumns=exceptIdColumns;
	}
	public String[] getIdColumnNames() {
		return idColumnNames;
	}
	public void setIdColumnNames( String[] idColumnNames ) {
		this.idColumnNames=idColumnNames;
	}
	public String getMappingClass() {
		return this.mappingClass;
	}
	public void setMappingClass( String mappingClass ) {
		this.mappingClass = mappingClass;
	}
	public String[] getUniqueColumnNames() {
		return uniqueColumnNames;
	}
	public void setUniqueColumnNames( String[] uniqueColumnNames ) {
		this.uniqueColumnNames=uniqueColumnNames;
	}
	public String[] getExceptIdColumnNames() {
		return exceptIdColumnNames;
	}
	public void setExceptIdColumnNames( String[] exceptIdColumnNames ) {
		this.exceptIdColumnNames=exceptIdColumnNames;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames( String[] columnNames ) {
		this.columnNames=columnNames;
	}
	public int getId() {
		return id;
	}
	public void setId( int id ) {
		this.id=id;
	}
	public Column[] getColumns() {
		return columns;
	}
	public void setColumns( Column[] columns ) {
		this.columns = columns;
	}
	public Column[] getIdColumns() {
		return idColumns;
	}
	public void setIdColumns( Column[] idColumns ) {
		this.idColumns = idColumns;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	public Column[] getUniqueColumns() {
		return uniqueColumns;
	}
	public void setUniqueColumns( Column[] uniqueColumns ) {
		this.uniqueColumns = uniqueColumns;
	}
	/**
	 * Extract a sub table definition.
	 * 
	 * @param subColumnNames
	 * @return
	 */
	public Table extractSubTable( String[] subColumnNames ) {
		Table subTable=new Table();
		subTable.setId( id );
		subTable.setName( name );
		subTable.setColumnNames( subColumnNames );
		
		Map columnMap=new HashMap();
		for( int i=0; i<columns.length; i++ ) {
			columnMap.put( columns[i].getName(), columns[i] );
		}
		Column[] subColumns=new Column[subColumnNames.length];
		for( int i=0; i<subColumnNames.length; i++ ) {
			subColumns[i]=(Column) columnMap.get( subColumnNames[i] );
		}
		subTable.setColumns( subColumns );
		
		return subTable;
	}
}
