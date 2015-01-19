/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

/**
 * Operation: =, !=, <=, <, >=, >, LIKE
 */
public class SimpleOperator extends Operator {
	public static final String EQUAL="=";
	public static final String NOT_EQUAL="!=";
	public static final String LESS_EQUAL="<=";
	public static final String LESS_THAN="<";
	public static final String GREATER_EQUAL=">=";
	public static final String GREATER_THAN=">";
	public static final String LIKE="LIKE";
	
	public String toSQL() {
		StringBuffer buf=new StringBuffer();
		buf.append( " " ).append( this.getValue() ).append( " " );
		return buf.toString();
	}
}
