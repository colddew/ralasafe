/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

public class IsNullExpr implements Expr {
	private Variable variable;

	/**
	 * Script likes: v == null
	 */
	public String toScript() {
		StringBuffer buff=new StringBuffer();
		String v=variable.toScript();
		buff.append( v ).append( " == null" );
		return buff.toString();
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable( Variable variable ) {
		this.variable=variable;
	}
}
