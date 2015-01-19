/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;


public class FieldWhereElement implements WhereElement {
	private String name;
	private Comparator compartor;
	private Object value;
	
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
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
}
