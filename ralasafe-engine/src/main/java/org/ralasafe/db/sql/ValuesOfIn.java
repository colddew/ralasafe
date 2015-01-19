/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Iterator;

public class ValuesOfIn implements RightOfIn {
	private ArrayList values=new ArrayList();

	public String toSQL() {
		StringBuffer buf=new StringBuffer();
		buf.append( " (" );
		if( values.size()>0 ) {
			Iterator itr=values.iterator();
			Value value=(Value) itr.next();
			buf.append( value.toSQL() );
			while( itr.hasNext() ) {
				value=(Value) itr.next();
				buf.append( "," ).append( value.toSQL() );
			}
		}
		buf.append( ") " );
		return buf.toString();
	}

	public ArrayList getValues() {
		return values;
	}

	public void setValues( ArrayList values ) {
		this.values=values;
	}
}
