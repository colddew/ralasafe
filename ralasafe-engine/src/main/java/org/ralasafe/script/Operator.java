/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

public abstract class Operator implements Script {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value=value;
	}
}
