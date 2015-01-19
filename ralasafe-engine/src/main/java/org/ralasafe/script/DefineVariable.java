/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

public abstract class DefineVariable implements Script {
	private String variableName;

	public final String getVariableName() {
		return variableName;
	}
	public final void setVariableName( String variableName ) {
		this.variableName=variableName;
	}
}
