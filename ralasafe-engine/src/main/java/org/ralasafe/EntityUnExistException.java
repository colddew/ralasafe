/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;


/**
 * Related to <code>EntityExistException</code>, the entity is not exist.
 * @author back
 *
 */
public class EntityUnExistException extends Exception {
	private Object entity;
	
	public EntityUnExistException( Object entity ) {
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}