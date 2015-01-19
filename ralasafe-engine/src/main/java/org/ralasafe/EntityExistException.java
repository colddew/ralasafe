/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;


/**
 * The entity requested is already exist.
 * 
 * <p>Just like when you add a user(already exist user) to db, Then raise <code>EntityExistException</code>.
 * @author back
 *
 */
public class EntityExistException extends Exception {

	private Object entity;
	
	public EntityExistException( Object entity ) {
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}
