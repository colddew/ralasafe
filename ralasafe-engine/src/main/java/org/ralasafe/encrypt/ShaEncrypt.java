/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.encrypt;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Use SHA encrypt, then hex to string.
 * Need <code>org.apache.commons.codec.digest.DigestUtils</code> as libary.
 *
 */
public class ShaEncrypt implements Encrypt {
	public String encrypt( String rawTxt ) {
		if( rawTxt==null ) {
			return null;
		}
		return DigestUtils.shaHex( rawTxt );
	}
}
