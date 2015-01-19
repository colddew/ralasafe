/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public abstract class AbstractComparator implements Comparator {
	private String type;

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public boolean equals( Object obj ) {
		if( obj != null && obj instanceof AbstractComparator ) {
			return type.equals( ( ( AbstractComparator ) obj ).getType() ); 
		} else {
			return false;
		}
	}
	
}
