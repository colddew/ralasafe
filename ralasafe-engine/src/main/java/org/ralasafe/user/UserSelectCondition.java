/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;


/**
 *
 * @author back
 *
 */
public class UserSelectCondition {
	private WhereElement whereElement;
	private GroupPart groupPart;
	private OrderPart orderPart;
	
	public GroupPart getGroupPart() {
		return groupPart;
	}
	public OrderPart getOrderPart() {
		return orderPart;
	}
	public WhereElement getWhereElement() {
		return whereElement;
	}
	public void setGroupPart( GroupPart groupPart ) {
		this.groupPart = groupPart;
	}
	public void setOrderPart( OrderPart orderPart ) {
		this.orderPart = orderPart;
	}
	public void setWhereElement( WhereElement whereElement ) {
		this.whereElement = whereElement;
	}
}
