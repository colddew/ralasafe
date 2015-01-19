/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * WARN!!!!
 * We will deprecate it later.
 */
public class NoValueComparator extends AbstractComparator {
	private NoValueComparator( String type ) {
		setType( type );
	}
	
	public static final NoValueComparator IS_NULL =
		new NoValueComparator( "IS NULL" );
	public static final NoValueComparator NOT_NULL =
		new NoValueComparator( "IS NOT NULL" );
}
