/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

/**
 * Operators: =, !=, <=, <, >=, >
 */
public class SimpleOperator extends Operator {
	public String toScript() {
		StringBuffer buff=new StringBuffer();
		String operator=getValue();
		if( getValue().equals( "=" ) ) {
			operator="==";
		}
		buff.append( " " ).append( operator ).append( " " );
		return buff.toString();
	}
}
