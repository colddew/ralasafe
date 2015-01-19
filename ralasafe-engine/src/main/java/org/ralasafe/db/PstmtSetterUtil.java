/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;


public class PstmtSetterUtil {
	public static PstmtSetter getSetter( String fieldClassName ) {
		if( "java.util.Date".equalsIgnoreCase( fieldClassName ) ) {
			return new PstmtSetter.JavaUtilDatePstmtSetter();
		} else {
			return new PstmtSetter.ObjectPstmtSetter();
		}
	}
}
