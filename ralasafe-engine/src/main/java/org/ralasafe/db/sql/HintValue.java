/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.MapStorgeObject;
import org.ralasafe.user.User;

public class HintValue implements Value {
	private static Log log=LogFactory.getLog( HintValue.class );
	private String hint;
	private String key;
	private boolean behindLike;
	private Method getter;
	
	public boolean isBehindLike() {
		return behindLike;
	}

	public void setBehindLike(boolean behindLike) {
		this.behindLike = behindLike;
	}

	public String toSQL() {
		return " ? ";
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue(User user, Map context) {
		if (!context.containsKey(hint)) {
			String msg="No business data object '" + hint
					+ "' found.";
			log.error( msg );
			throw new RalasafeException(msg);
		}
		Object hintObj = context.get(hint);
		if (hintObj instanceof MapStorgeObject) {
			if (!((MapStorgeObject) hintObj).containsKey(key)) {
				String msg="No '" + key
						+ "' found in business data object '" + hint + "'.";
				log.error( msg );
				throw new RalasafeException(msg);
			}
			return ((MapStorgeObject) hintObj).get(key);
		} else {
			// java bean
			try {				
				if( getter==null ) {
					initGetterMethod( hintObj );
				}
				return getter.invoke(hintObj, new Object[] {});
			} catch (Exception e) {
				log.error( "", e );
				throw new RalasafeException(e);
			}
		}
	}
	
	private void initGetterMethod(Object o) throws Exception {
		Class clas = o.getClass();
		
		// try getField method first
		String getterStr="get" + key.substring(0, 1).toUpperCase() + key.substring(1);
		try {
			getter = clas.getMethod(getterStr, new Class[] {});
		} catch( NoSuchMethodException e ) {
			String isStr="is" + key.substring(0, 1).toUpperCase()
			+ key.substring(1);
			try {
				// try isField method
				getter = clas.getMethod(isStr, new Class[] {});
			} catch( NoSuchMethodException e1 ) {
				String msg=getterStr+" or " + isStr + " method not found in class:" + clas;
				log.error( msg );
				throw new RalasafeException( msg );
			}
		}
	}
}
