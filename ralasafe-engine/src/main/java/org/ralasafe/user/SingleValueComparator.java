/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;

public class SingleValueComparator extends AbstractComparator {
	private SingleValueComparator( String type ) {
		setType( type );
	}
		
	public static final SingleValueComparator EQUAL = 
		new SingleValueComparator( "=" );
	public static final SingleValueComparator GREATER = 
		new SingleValueComparator( ">" );
	public static final SingleValueComparator LESS = 
		new SingleValueComparator( "<" );
	public static final SingleValueComparator NOT_EQUAL = 
		new SingleValueComparator( "!=" );
	public static final SingleValueComparator NOT_GREATER = 
		new SingleValueComparator( "<=" );
	public static final SingleValueComparator NOT_LESS = 
		new SingleValueComparator( ">=" );
	public static final SingleValueComparator LIKE = 
		new SingleValueComparator( "LIKE" );
	public static final SingleValueComparator NOT_LIKE = 
		new SingleValueComparator( "NOT LIKE" );
	public static final SingleValueComparator LEFT_LIKE = 
		new SingleValueComparator( "LEFT LIKE" );
	//public static final SingleValueComparator NOT_LEFT_LIKE = 
	//	new SingleValueComparator( "NOT LEFT LIKE" );
	public static final SingleValueComparator RIGHT_LIKE = 
		new SingleValueComparator( "RIGHT LIKE" );
	//public static final SingleValueComparator NOT_RIGHT_LIKE = 
	//	new SingleValueComparator( "NOT RIGHT LIKE" );
}
