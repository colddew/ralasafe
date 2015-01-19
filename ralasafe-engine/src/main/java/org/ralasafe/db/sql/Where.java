/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

public class Where implements SQLElement {
	private ExpressionGroup expressionGroup=new ExpressionGroup();

	public String toSQL() {
		if( expressionGroup.getExpressions().size()==0 ) {
			return "";
		}
		StringBuffer buf=new StringBuffer();
		buf.append( "\n" ).append( " WHERE " ).append( expressionGroup.toSQL() ).append( " " );
		return buf.toString();
	}

	public ExpressionGroup getExpressionGroup() {
		return expressionGroup;
	}

	public void setExpressionGroup( ExpressionGroup expressionGroup ) {
		this.expressionGroup=expressionGroup;
	}

	public Where lightCopy() {
		return new Where();
	}
}
