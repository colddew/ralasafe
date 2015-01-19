/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import org.ralasafe.SystemConstant;

public class UserValue extends DefineVariable {
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key=key;
	}

	/**
	 * Script likes: Object v = user.get("key");
	 */
	public String toScript() {
		String v=getVariableName();
		String user=SystemConstant.USER_KEY;
		StringBuffer buff=new StringBuffer();
		buff.append( " Object " ).append( v ).append( " = " ).append( user )
				.append( ".get(\"" ).append( key ).append( "\"); " ).append(
						"\n" );
		return buff.toString();
	}
}
