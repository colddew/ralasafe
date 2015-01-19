/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.user.User;

public class SimpleValue implements Value {
	private static Log log=LogFactory.getLog( SimpleValue.class );
	public static final String STRING = "string";
	public static final String INTEGER = "integer";
	public static final String FLOAT = "float";
	public static final String BOOLEAN = "boolean";
	public static final String DATETIME = "datetime";
	private String type;
	private String value;
	private boolean behindLike;

	public boolean isBehindLike() {
		return behindLike;
	}

	public void setBehindLike(boolean behindLike) {
		this.behindLike = behindLike;
	}

	public String toSQL() {
		return " ? ";
		// StringBuffer buf = new StringBuffer();
		// buf.append(" ");
		// if (behindLike) {
		// buf.append("'%").append(value).append("%'");
		// } else {
		// if (type.equals(STRING) || type.equals(DATETIME)
		// || type.equals(BOOLEAN)) {
		// buf.append("'").append(value).append("'");
		// } else {
		// buf.append(value);
		// }
		// }
		// buf.append(" ");
		// return buf.toString();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Object getValue(User user, Map context) {
		if (type.equals(STRING)) {
			return value;
		} else if (type.equals(INTEGER)) {
			return new Integer(value);
		} else if (type.equals(FLOAT)) {
			return new Float(value);
		} else if (type.equals(BOOLEAN)) {
			return new Boolean(value);
		} else if (type.equals(DATETIME)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				return format.parse(value);
			} catch (ParseException e) {
				log.error( "", e );
				throw new RalasafeException(e);
			}
		} else {
			String msg="Not supported value type '" + type + "'.";
			log.error( msg );
			throw new RalasafeException(msg);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
