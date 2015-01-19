/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public class TwoValueComparator extends AbstractComparator {
	private TwoValueComparator( String type ) {
		setType( type );
	}
	
	public static final TwoValueComparator BETWEEN =
		new TwoValueComparator( "BETWEEN" );
	public static final TwoValueComparator NOT_BETWEEN =
		new TwoValueComparator( "NOT BETWEEN" );
}
