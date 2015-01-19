/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class From implements SQLElement {
	private Collection tables=new ArrayList();

	public String toSQL() {
		StringBuffer buf=new StringBuffer();
		buf.append( "\n" ).append( " FROM " );
		if( tables.size()>0 ) {
			Iterator itr=tables.iterator();
			Table table=(Table) itr.next();
			buf.append( table.toSQL() );
			while( itr.hasNext() ) {
				table=(Table) itr.next();
				buf.append( "," ).append( table.toSQL() );
			}
		}
		return buf.toString();
	}

	public Collection getTables() {
		return tables;
	}

	public void setTables( Collection tables ) {
		this.tables=tables;
	}
}
