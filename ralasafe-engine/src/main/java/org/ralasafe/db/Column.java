/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public class Column {
	private String name;
	private Type type;
	private ColumnAdapter adapter;
	private String function;
	
	public ColumnAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter( ColumnAdapter adapter ) {
		this.adapter = adapter;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType( Type type ) {
		this.type = type;
	}
	public void setFunction( String function ) {
		this.function = function;
		
	}
	public String getFunction() {
		return this.function;
		
	}
}
