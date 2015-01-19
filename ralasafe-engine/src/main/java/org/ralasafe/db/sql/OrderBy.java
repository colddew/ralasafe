/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderBy implements SQLElement {
	private ArrayList columns=new ArrayList();

	public String toSQL() {
		if( columns.size()==0 ) {
			return "";
		}
		
		StringBuffer buf=new StringBuffer();
		buf.append( "\n" ).append( " ORDER BY " );
		Iterator itr=columns.iterator();
		Column column=(Column) itr.next();
		buf.append( column.toSQL() ).append( " " ).append( column.getOrder() )
				.append( " " );
		while( itr.hasNext() ) {
			column=(Column) itr.next();
			buf.append( "," ).append( column.toSQL() ).append( " " ).append(
					column.getOrder() ).append( " " );
		}
		buf.append( " " );
		return buf.toString();
	}

	public ArrayList getColumns() {
		return columns;
	}

	public void setColumns( ArrayList columns ) {
		this.columns=columns;
	}
}
