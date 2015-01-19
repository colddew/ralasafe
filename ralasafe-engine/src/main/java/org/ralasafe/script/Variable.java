/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

public class Variable implements Script {
	private String name;
	public String toScript() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
}
