/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.RalasafeException;

public class DBLevelException extends RalasafeException {
	public DBLevelException() {
		super();
	}

	public DBLevelException( String message, Throwable cause ) {
		super( message, cause );
	}

	public DBLevelException( String message ) {
		super( message );
	}

	public DBLevelException( Throwable cause ) {
		super( cause );
	}	
}
