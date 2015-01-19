/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.ArrayList;
import java.util.List;

import org.ralasafe.ObjectNewer;
import org.ralasafe.db.MapStorageObjectNewer;
import org.ralasafe.db.MapStorgeObject;
import org.ralasafe.db.MultiValueTableAdapter;
import org.ralasafe.util.Util;

public class DefaultMultiValueTableAdapter implements MultiValueTableAdapter {
	private String mapKey;
	//private String idColumnName;
	
	public DefaultMultiValueTableAdapter( String mapKey/*, String idColumnName*/ ) {
		super();
		this.mapKey=mapKey;
		//this.idColumnName=idColumnName;
	}

	public void combine( Object o, Object[] extractValues ) {
		if( extractValues==null )
			return;
		
		MapStorgeObject mso=(MapStorgeObject) o;
		List tableMsos=new ArrayList( extractValues.length );
		for( int i=0; i<extractValues.length; i++ ) {
			Object value=extractValues[i];
			tableMsos.add( value );
		}
		
		mso.put( mapKey, tableMsos );
	}

	public Object[] extract( Object o ) {
		MapStorgeObject mso=(MapStorgeObject) o;
		Object[] values=(Object[]) mso.get( mapKey );
		return values;
	}

	public Object extractEvenNoValueExist( Object o ) {
		MapStorgeObject mso=(MapStorgeObject) o;
		Object[] values=(Object[]) mso.get( mapKey );
		
		if( Util.isEmpty( values ) ) {
			//MapStorgeObject another=new MapStorgeObject();
			//another.put( idColumnName, mso.get(  ) )
			return mso;
		} else {
			return values[0];
		}
	}

	public ObjectNewer getObjectNewer() {
		return new MapStorageObjectNewer();
	}
}
