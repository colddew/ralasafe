/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.Map;

import org.ralasafe.user.User;

public interface Value extends Operand, Parameter {
	/**
	 * Indicate this value is for a LIKE operator or not?
	 * @param behind         if true, for LIKE operator
	 */
	public void setBehindLike(boolean behind);

	/**
	 * Indicate this value is for a LIKE operator or not?
	 * @return   return true if it's for LIKE operator
	 */
	public boolean isBehindLike();

	public abstract Object getValue(User user, Map context);
}
