/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * WARN!!!!
 * We will deprecate it later.
 */
public class SelectCondition {
	private WhereElement whereElement;
	private GroupPart groupPart;
	private OrderPart orderPart;
	private Map pstmtInObjectMap;
	
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
	public void registPstmtInWhereElementHintObject( String name, Object hint ) {
		PstmtInWhereElement inEmt=(PstmtInWhereElement) pstmtInObjectMap.get( name );
		inEmt.setHint( hint );
	}
	public void registPstmtInWhereElement( String name, PstmtInWhereElement inEmt ) {
		if( pstmtInObjectMap==null ) {
			pstmtInObjectMap=new HashMap();
		}
		pstmtInObjectMap.put( name, inEmt );
	}
	public Set getRegistedPstmtInNames() {
		if( pstmtInObjectMap==null ) {
			return null;
		} 
		return pstmtInObjectMap.keySet();
	}
}
