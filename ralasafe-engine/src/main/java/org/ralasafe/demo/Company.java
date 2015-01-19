/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

public class Company {
	private int id;
	private String name;
	private int parentId;
	private int level;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId( int parentId ) {
		this.parentId=parentId;
	}
	public int getId() {
		return id;
	}
	public void setId( int id ) {
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
}
