/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Quyer policy test result.
 * One query rule or a group rules be assigned to a privilege. We call this one or this group rules, policy.
 * 
 */
public class QueryResult {
	private Collection data = new ArrayList();
	private Collection fields = new ArrayList();
	private Collection readOnlyFields = new ArrayList();
	private int totalCount;
	private boolean reachQueryLimit;

	/**
	 * The total count of records
	 * 
	 * @return if it's pagination query, return all records count, not this page's records count
	 */
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Query records
	 * 
	 * @return Collection< YourBean >
	 */
	public Collection getData() {
		return data;
	}

	public void setData(Collection data) {
		this.data = data;
	}

	/**
	 * Which fields of Your bean's class been fetched
	 * 
	 * @return Collection< String >, field name of your bean class
	 */
	public Collection getFields() {
		return fields;
	}

	public void setFields(Collection fields) {
		this.fields = fields;
	}

	/**
	 * According to policy, which fields is readonly
	 * 
	 * @return Collection< String >
	 */
	public Collection getReadOnlyFields() {
		return readOnlyFields;
	}

	public void setReadOnlyFields(Collection readOnlyFields) {
		this.readOnlyFields = readOnlyFields;
	}

	/**
	 * If you want limit query count, like not more than 1000 once.
	 * Then you can set queryLimit in web.xml->StartupServlet->queryLimit.
	 * 
	 * @return ture - queryLimit is reached
	 */
	public boolean isReachQueryLimit() {
		return reachQueryLimit;
	}

	public void setReachQueryLimit(boolean reachQueryLimit) {
		this.reachQueryLimit = reachQueryLimit;
	}
}
