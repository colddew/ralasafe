/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

import org.ralasafe.util.Util;

public class GeneralPK {
	private Object[] pkFields;

	public GeneralPK( Object[] pkFields ) {
		this.pkFields=pkFields;
	}

	public boolean equals( Object obj ) {
		if( obj instanceof GeneralPK ) {
			GeneralPK that=(GeneralPK) obj;
			Object[] thatPKFields=that.pkFields;
			for( int i=0; i<pkFields.length; i++ ) {
				boolean result=Util.equals( pkFields[i], thatPKFields[i] );
				if( !result ) {
					return result;
				}
			}
			
			return true;
		} else {
			return false;
		}		
	}

	public int hashCode() {
		int result=0;
		for( int i=0; i<pkFields.length; i++ ) {
			if( pkFields[i]!=null ) {
				int temp=pkFields[i].hashCode();
				int moveBit=5*i;
				temp=temp<<moveBit;
				result+=temp;
			}
		}

		return result;
	}

	public String toString() {
		StringBuffer buff=new StringBuffer();
		buff.append( this );
		for( int i=0; i<pkFields.length; i++ ) {
			buff.append( "\r\n\t" );
			buff.append( pkFields[i] );
		}
		return buff.toString();
	}
}
