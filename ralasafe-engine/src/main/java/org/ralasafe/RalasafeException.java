/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

public class RalasafeException extends RuntimeException {
	public RalasafeException() {
		super();
	}

	public RalasafeException( String message, Throwable cause ) {
		super( message, cause );
	}

	public RalasafeException( String message ) {
		super( message );
	}

	public RalasafeException( Throwable cause ) {
		super( cause );
	}	
}
