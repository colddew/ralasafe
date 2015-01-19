/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.encrypt;


/**
 * Don't encrypt. Return rawtxt.
 *
 */
public class PlainEncrypt implements Encrypt {
	public String encrypt( String rawTxt ) {
		return rawTxt;
	}
}
