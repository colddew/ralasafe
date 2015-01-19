/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.encrypt;

import org.apache.commons.codec.binary.Base64;

/**
 * Need <code>org.apache.commons.codec.binary.Base64</code> as a libary.
 *
 */
public class Base64Encrypt implements Encrypt {
	public String encrypt( String rawTxt ) {
		if( rawTxt==null ) {
			return null;
		}
		
		return new String( Base64.encodeBase64( rawTxt.getBytes() ) );
	}
}
