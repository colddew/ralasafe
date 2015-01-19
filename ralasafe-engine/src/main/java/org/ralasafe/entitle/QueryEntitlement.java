/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;


/**
 * Query entitlement rule. Describes {@linkplain org.ralasafe.privilege.Privilege}/{@linkplain UserCategory}/{@linkplain Query} relationship.
 * Unique index( privilegeId, userCategoryId ).
 * For a certain privilage and a certain usercategory, must match only one query.
 */
public class QueryEntitlement {
	private int userCategoryId;
	private int queryId;
	/**
	 * Privilege Id, must be a leaf node.
	 */
	private int privilegeId;
	private int id;
	private String description;
	private UserCategory userCategory;
	private Query query;

	public int getUserCategoryId() {
		return userCategoryId;
	}

	public void setUserCategoryId(int userCategoryId) {
		this.userCategoryId = userCategoryId;
	}

	public int getQueryId() {
		return queryId;
	}

	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserCategory getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
