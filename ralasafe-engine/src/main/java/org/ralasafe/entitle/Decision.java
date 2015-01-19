/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

/**
 * Result of decision policy.
 * If request is denied, <code>denyReason</code> should be given.
 * 
 */
public class Decision {
	protected static final String DEFAULT_DENY_REASON = "You are not granted to execute it";
	private boolean permit;
	private String denyReason;

	/**
	 * permit or deny
	 * 
	 * @return true--permit; false--deny
	 */
	public boolean isPermit() {
		return permit;
	}
	
	public void setPermit(boolean permit) {
		this.permit = permit;
	}

	/**
	 * deny reason
	 * 
	 * @return 
	 */
	public String getDenyReason() {
		return denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}
}
