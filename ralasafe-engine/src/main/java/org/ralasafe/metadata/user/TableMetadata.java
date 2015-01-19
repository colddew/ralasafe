/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.metadata.user;

public class TableMetadata {
	private String datasourceName;
	private String name;
	private String sqlTableName;
	private FieldMetadata[] uniqueFields;
	private FieldMetadata[] fields;
	private int minOcc=0;
	private int maxOcc=1;
	
	public String getDatasourceName() {
		return datasourceName;
	}
	public void setDatasourceName( String datasourceName ) {
		this.datasourceName=datasourceName;
	}
	public String getSqlTableName() {
		return sqlTableName;
	}
	public void setSqlTableName( String sqlTableName ) {
		this.sqlTableName=sqlTableName;
	}
	public int getMinOcc() {
		return minOcc;
	}
	public void setMinOcc( int minOcc ) {
		this.minOcc=minOcc;
	}
	public int getMaxOcc() {
		return maxOcc;
	}
	public void setMaxOcc( int maxOcc ) {
		this.maxOcc=maxOcc;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
	public FieldMetadata[] getUniqueFields() {
		return uniqueFields;
	}
	public void setUniqueFields( FieldMetadata[] uniqueFields ) {
		this.uniqueFields=uniqueFields;
	}
	public FieldMetadata[] getFields() {
		return fields;
	}
	public void setFields( FieldMetadata[] fields ) {
		this.fields=fields;
	}
}
