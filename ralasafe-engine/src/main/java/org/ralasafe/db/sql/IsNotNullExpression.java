/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

public class IsNotNullExpression implements Expression {
	private Column column;

	public String toSQL() {
		StringBuffer buf=new StringBuffer();
		buf.append( " " ).append( column.toSQL() ).append( " IS NOT NULL " );
		return buf.toString();
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
}
