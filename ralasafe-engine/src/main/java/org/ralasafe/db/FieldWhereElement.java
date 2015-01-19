/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * Field where condition.
 *  
 * @author jbwang
 *
 */
public class FieldWhereElement implements WhereElement {
	private Column column;
	private Comparator compartor;
	/** If SingleValueComparator, the value is a string;
	 * If it's a MultiValueComparator, the value is a collection of string */
	private Object value;
	
	private boolean isContextValue;
	
	public Comparator getCompartor() {
		return compartor;
	}
	public void setCompartor( Comparator compartor ) {
		this.compartor = compartor;
	}
	public Object getValue() {
		return value;
	}
	public void setValue( Object value ) {
		this.value = value;
	}
	public Column getColumn() {
		return column;
	}
	public void setColumn( Column column ) {
		this.column = column;
	}
	public boolean isContextValue() {
		return isContextValue;
	}
	public void setContextValue( boolean isContextValue ) {
		this.isContextValue = isContextValue;
	}
}
