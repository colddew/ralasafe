/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public class Type {
	private String javaType;
	private String maskSqlType;
	private String sqlType;
	
	public Type( String javaType, String sqlType ) {
		this.javaType = javaType;
		this.sqlType = sqlType;
		this.maskSqlType = maskSqlType( sqlType );
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
	
	public String toString() {
		return javaType + "-" + maskSqlType;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType( String javaType ) {
		this.javaType = javaType;
	}

	public String getMaskSqlType() {
		return maskSqlType;
	}

	/**public void setMaskSqlType( String sqlType ) {
		this.maskSqlType = sqlType;
	}*/
	
	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType( String sqlType ) {
		this.sqlType = sqlType;
	}
	
	public boolean equals( Object obj ) {
		if( obj instanceof Type ) {
			Type another = (Type) obj;
			
			return this.javaType.equals( another.javaType ) && this.maskSqlType.equals( another.maskSqlType );
		} else {
			return false;
		}
	}
	
	public String maskSqlType( String type ) {
		int leftParenthesisIndex = type.indexOf( '(' );
		if(  leftParenthesisIndex > 0 ) {
			return type.substring( 0, leftParenthesisIndex );
		} else {
			return type;
		}
	}
}
