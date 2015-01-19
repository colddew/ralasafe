/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ExpressionGroup implements Expression {
	public static final String AND = "AND";
	public static final String OR = "OR";
	private String linker = AND;
	private Collection expressions = new ArrayList();

	public String toSQL() {
		Iterator itr = expressions.iterator();
		if (!itr.hasNext())
			return "";
		StringBuffer buf = new StringBuffer();
		// first Expression
		Expression expression = (Expression) itr.next();
		buf.append(" (");
		buf.append(expression.toSQL());
		while (itr.hasNext()) {
			// link other Expressions with linker
			expression = (Expression) itr.next();
			buf.append(" ").append(linker).append(" ").append(
					expression.toSQL());
		}
		buf.append(") ");
		return buf.toString();
	}

	public Collection getExpressions() {
		return expressions;
	}

	public void setExpressions(Collection expressions) {
		this.expressions = expressions;
	}

	public String getLinker() {
		return linker;
	}

	public void setLinker(String linker) {
		this.linker = linker;
	}
}
