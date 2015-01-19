/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.user.User;

public class ContextValue implements Value {
	private static Log log=LogFactory.getLog( ContextValue.class );
	private String key;
	private boolean behindLike;

	public boolean isBehindLike() {
		return behindLike;
	}

	public void setBehindLike(boolean behindLike) {
		this.behindLike = behindLike;
	}

	public String toSQL() {
		return " ? ";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue(User user, Map context) {
		if (!context.containsKey(key)) {
			String msg="No '" + key + "' found in context.";
			log.error( msg );
			throw new RalasafeException("No '" + key + "' found in context.");
		}
		return context.get(key);
	}
}
