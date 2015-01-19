/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.ObjectNewer;
import org.ralasafe.db.MapStorageObjectNewer;
import org.ralasafe.db.MapStorgeObject;
import org.ralasafe.db.SingleValueTableAdapter;

public class DefaultSingleValueTableAdapter implements SingleValueTableAdapter {
	private String mapKey;
	//private String idColumnName;
	
	public DefaultSingleValueTableAdapter( String mapKey/*, String idColumnName*/ ) {
		super();
		this.mapKey=mapKey;
		//this.idColumnName=idColumnName;
	}

	public void combine( Object o, Object extractValue ) {
		MapStorgeObject mso=(MapStorgeObject) o;
		mso.put( mapKey, extractValue );
	}

	public Object extract( Object o ) {
		MapStorgeObject mso=(MapStorgeObject) o;
		return mso.get( mapKey );
	}

	public Object extractEvenNoValueExist( Object o ) {
		Object extract=extract( o );
		if( extract==null ) {
//			MapStorgeObject another=new MapStorgeObject();
//			Object idValue=mso.get( mainTableIdColumnName );
//			another.put( mainTableIdColumnName, idValue );
//			return another;
			return o;
		} else { 
			return extract;
		}
	}

	public ObjectNewer getObjectNewer() {
		return new MapStorageObjectNewer();
	}
}
