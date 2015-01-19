/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import org.ralasafe.SystemConstant;
import org.ralasafe.entitle.Query;

public class QueryRef extends DefineVariable {

	private int id;
	private Query query;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toScript() {
		return toScript(false);
	}

	/**
	 * Script likes: 
	 * Object v = queryManager.getQuery( queryId ).executeQueryRef(user,
	 * context, returnCollection );
	 */
	public String toScript(boolean returnCollection) {
		String v = getVariableName();
		String user = SystemConstant.USER_KEY;
		String context = SystemConstant.CONTEXT;
		String queryManager = SystemConstant.QUERY_MANAGER;
		StringBuffer buff = new StringBuffer();
		buff.append(" Object ").append(v).append(" = ").append(queryManager)
				.append(".getQuery(").append(id).append(").executeQueryRef(")
				.append(user).append(", ").append(context).append(", ").append(
						returnCollection).append(" ); ").append("\n");
		return buff.toString();
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
}
