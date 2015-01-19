/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class PageBatch {
	private int pageSize;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize( int pageSize ) {
		this.pageSize=pageSize;
	}

	public void doBatch( Object[] objs ) throws Exception {
		if( objs==null )
			return;
		
		for( int i=0; i<objs.length; ) {
			int fromIndex=i;
			i+=pageSize;
			
			int toIndex=i;
			if( toIndex>objs.length ) {
				toIndex=objs.length;
			}
			
			Object[] pageObjs=new Object[toIndex-fromIndex];
			for( int j=0; j<pageObjs.length; j++ ) {
				pageObjs[j]=objs[fromIndex+j];
			}
			
			doInPage( pageObjs );
		}
	}
	
	public void doBatch( Collection coll ) throws Exception {
		if( coll==null ) 
			return;
		
		Collection pageColl=new ArrayList( pageSize );
		Iterator iter=coll.iterator();
		while( iter.hasNext() ) {
			for( int i=0; i<pageSize&&iter.hasNext(); i++ ) {
				pageColl.add( iter.next() );
			}
			
			doInPage( pageColl );
			pageColl.clear();
		}
	}
	
	public abstract void doInPage( Object[] obj ) throws Exception;
	public abstract void doInPage( Collection coll ) throws Exception;
}
