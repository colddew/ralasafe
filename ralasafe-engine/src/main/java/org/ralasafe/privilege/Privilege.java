/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.privilege;

import org.ralasafe.group.Node;


/**
 * Basic thing of ralasafe. A privilege means an action, like update bill, query bills, delete employee,
 * exec a job, send email, etc.
 * 
 * <p>
 * Privilege be divided into two categories: BUSINESS_PRIVILEGE and NON_ROLE_PRIVILEGE.
 * BUSINESS_PRIVILEGE is a normal privilege as everyone knows, like update bill.
 * NON_ROLE_PRIVILEGE is privilege for some little things, like a combo box of web page.
 * Unlike BUSINESS_PRIVILEGE, it needn't check function-level privilege of the combo box.
 * </p>
 */
public class Privilege extends Node {
	public static final int BUSINESS_PRIVILEGE_TREE_ROOT_ID = 0;
	public static final int NON_ROLE_PRIVILEGE_TREE_ROOT_ID = -1;
	public static final int NULL_ROOT_ID = -2;
	public static final int RALASAFE_ADMIN_ID = -3;
	public static final int POLICY_ADMIN_ID = -4;
	public static final int ASSIGN_ROLE_TO_USER_ID = -5;
	public static final int ROLE_ADMIN_ID = -6;
	public static final int BUSINESS_PRIVILEGE = 0;
	public static final int NON_ROLE_PRIVILEGE = 1;
	public static final int DECISION_FIRST_APPLICABLE = 0;
	public static final int DECISION_ORDERED_PERMIT_OVERRIDES = 1;
	public static final int DECISION_ORDERED_DENY_OVERRIDES = 2;
	public static final int QUERY_FIRST_APPLICABLE = 0;
	private String name = "";
	private String description = "";
	private int decisionPolicyCombAlg;
	private int queryPolicyCombAlg;
	private int type;
	private String constantName = "";
	private String url = "";
	private String target = "_self";
	private int orderNum;
	private boolean display=true;
	
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum( int orderNum ) {
		this.orderNum=orderNum;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getConstantName() {
		return constantName;
	}

	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDecisionPolicyCombAlg() {
		return decisionPolicyCombAlg;
	}

	public void setDecisionPolicyCombAlg(
			int decisionPolicyCombiningAlgorithm) {
		this.decisionPolicyCombAlg = decisionPolicyCombiningAlgorithm;
	}

	public int getQueryPolicyCombAlg() {
		return queryPolicyCombAlg;
	}

	public void setQueryPolicyCombAlg(
			int queryPolicyCombiningAlgorithm) {
		this.queryPolicyCombAlg = queryPolicyCombiningAlgorithm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean equals(Object o) {
		if (o instanceof Privilege) {
			Privilege privilege = (Privilege) o;
			return (privilege.getId() == this.getId()
					&& privilege.name.equals(this.name)
					&& privilege.type == this.type);
		}

		return false;
	}
	
	public Object clone() {
		Privilege newOne=new Privilege();
		newOne.setConstantName( constantName );
		newOne.setDecisionPolicyCombAlg( decisionPolicyCombAlg );
		newOne.setDescription( description );
		newOne.setId( getId() );
		newOne.setIsLeaf( getIsLeaf() );
		newOne.setName( name );
		newOne.setPid( getPid() );
		newOne.setQueryPolicyCombAlg( queryPolicyCombAlg );
		newOne.setTarget( target );
		newOne.setType( type );
		newOne.setUrl( url );
		newOne.setOrderNum( orderNum );
		
		return newOne;
	}
	
	public String toString() {
		return "Privilege(id=" + this.getId() 
		+ ",name=" + name
		+ ",type=" + type + ",pid=" + this.getPid() + ",isLeaf=\"" + this.getIsLeaf()
		+ ",orderNum=" + orderNum
		+ ",description="+description + ")";
	}	
}
