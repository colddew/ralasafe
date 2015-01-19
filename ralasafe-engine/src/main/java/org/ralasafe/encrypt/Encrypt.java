/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.encrypt;

public interface Encrypt {
	/**
	 * Encrypt some text
	 * @param rawTxt 
	 * @return 
	 */
	public String encrypt( String rawTxt );
}
