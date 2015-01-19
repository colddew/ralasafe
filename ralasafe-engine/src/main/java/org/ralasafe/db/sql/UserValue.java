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

public class UserValue implements Value {
	private static Log log=LogFactory.getLog( UserValue.class );
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
		if (!user.containsField(key)) {
			String msg="No '" + key + "' found in user.";
			log.error( msg );
			throw new RalasafeException(msg);
		}
		return user.get(key);
	}
}
