/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;

public class OrderPart {
	private String[] names;
	private String[] orderTypes;
	
	public String[] getNames() {
		return names;
	}
	public void setNames( String[] names ) {
		this.names = names;
	}
	public String[] getOrderTypes() {
		return orderTypes;
	}
	public void setOrderTypes( String[] orderTypes ) {
		this.orderTypes = orderTypes;
	}
}
