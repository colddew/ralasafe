/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

public class InExpression implements Expression {
	private LeftOfIn left;
	private RightOfIn right;

	public String toSQL() {
		StringBuffer buf=new StringBuffer();
		buf.append( " " );
		if( left instanceof Query ) {
			buf.append( "(" ).append( left.toSQL() ).append( ")" );
		} else {
			buf.append( left.toSQL() );
		}
		buf.append( " IN " );
		if( right instanceof Query ) {
			buf.append( "(" ).append( right.toSQL() ).append( ")" );
		} else {
			buf.append( right.toSQL() );
		}
		buf.append( " " );
		return buf.toString();
	}

	public LeftOfIn getLeft() {
		return left;
	}

	public void setLeft( LeftOfIn left ) {
		this.left=left;
	}

	public RightOfIn getRight() {
		return right;
	}

	public void setRight( RightOfIn right ) {
		this.right=right;
	}
}
