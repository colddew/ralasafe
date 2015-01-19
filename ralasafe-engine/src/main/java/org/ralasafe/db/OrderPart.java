/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * WARN!!!!
 * We will deprecate it later.
 * 
 */
public class OrderPart {
	private String[] columnNames;
	private String[] orderTypes;
	
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames( String[] columnNames ) {
		this.columnNames = columnNames;
	}
	public String[] getOrderTypes() {
		return orderTypes;
	}
	public void setOrderTypes( String[] orderTypes ) {
		this.orderTypes = orderTypes;
	}
}
