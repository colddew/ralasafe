/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import java.util.ArrayList;
import java.util.Iterator;

public class ExprGroup implements Expr {
	private String linker = "AND";;
	private ArrayList exprs = new ArrayList();

	public String toScript() {
		Iterator itr = exprs.iterator();
		if (!itr.hasNext())
			return "";
		String linkerScript = null;
		if (linker.equalsIgnoreCase("AND"))
			linkerScript = "&&";
		else if (linker.equalsIgnoreCase("OR"))
			linkerScript = "||";
		StringBuffer buf = new StringBuffer();
		// the first Expr
		Expr expr = (Expr) itr.next();
		buf.append(" ( ");
		buf.append(expr.toScript());
		while (itr.hasNext()) {
			expr = (Expr) itr.next();
			if (expr instanceof ExprGroup
					&& ((ExprGroup) expr).getExprs().size() == 0) {
				continue;
			}
			// following Expr linked by linker
			buf.append(" ").append(linkerScript).append(" ").append(
					expr.toScript());
		}
		buf.append(" ) ");
		return buf.toString();
	}

	public ArrayList getExprs() {
		return exprs;
	}

	public void setExprs(ArrayList exprs) {
		this.exprs = exprs;
	}

	public String getLinker() {
		return linker;
	}

	public void setLinker(String linker) {
		this.linker = linker;
	}

	/**
	 * Does var named variableName is used id InExpr nor NotInExpr
	 */
	public boolean isUsedByInExprOrNotInExpr(String variableName) {
		boolean used = false;
		Iterator itr = this.exprs.iterator();
		while (itr.hasNext()) {
			Expr expr = (Expr) itr.next();
			if (expr instanceof InExpr) {
				used = ((InExpr) expr).isUsed(variableName);
			} else if (expr instanceof NotInExpr) {
				used = ((NotInExpr) expr).isUsed(variableName);
			} else if (expr instanceof ExprGroup) {
				used = ((ExprGroup) expr)
						.isUsedByInExprOrNotInExpr(variableName);
			}
			if (used) {
				return used;
			}
		}
		return used;
	}
}
