/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ralasafe.GeneralPK;
import org.ralasafe.util.Util;

/**
 * Given a conditon, we query MainTable, SingleValueTables and MultiValueTables.
 * Many values are returned. The combiner combine them into entities and don't disorder the entities.
 * 
 */
public class ComplexTableValueCombiner {
	private ComplexTable complexTable;
	private SingleValueTableAdapter[] singleValueTableAdapters;
	private MultiValueTableAdapter[] multiValueTableAdapters;
	private Collection mainTableValues;
	private Collection[] singleValueTableValues;
	private Collection[] multiValueTableValues;
	// Store ComplexTable's attribute/attribute's value by order,key/value=pk< GeneralPK >/entity< Object >
	private LinkedHashMap map=new LinkedHashMap();

	public ComplexTableValueCombiner( ComplexTable complexTable ) {
		this.complexTable=complexTable;
	}
	
	public SingleValueTableAdapter[] getSingleValueTableAdapters() {
		return singleValueTableAdapters;
	}

	public void setSingleValueTableAdapters(
			SingleValueTableAdapter[] singleValueTableAdapters ) {
		this.singleValueTableAdapters=singleValueTableAdapters;
	}

	public MultiValueTableAdapter[] getMultiValueTableAdapters() {
		return multiValueTableAdapters;
	}

	public void setMultiValueTableAdapters(
			MultiValueTableAdapter[] multiValueTableAdapters ) {
		this.multiValueTableAdapters=multiValueTableAdapters;
	}

	public Collection getMainTableValues() {
		return mainTableValues;
	}

	public void setMainTableValues( Collection coll ) {
		this.mainTableValues=coll;
		Table mainTable=complexTable.getMainTable();
		Column[] idColumns=mainTable.getIdColumns();
		for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
			Object temp=(Object) iter.next();
			Object[] keyFields=new Object[idColumns.length];
			for( int j=0; j<idColumns.length; j++ ) {
				keyFields[j]=idColumns[j].getAdapter().extractFieldValue( temp );
			}
			GeneralPK pk=new GeneralPK( keyFields );
			map.put( pk, temp );
		}
	}

	public Collection[] getSingleValueTableValues() {
		return singleValueTableValues;
	}

	public void setSingleValueTableValues( Collection[] singleValueTableValues ) {
		this.singleValueTableValues=singleValueTableValues;
	}

	public Collection[] getMultiValueTableValues() {
		return multiValueTableValues;
	}

	public void setMultiValueTableValues( Collection[] multiValueTableValues ) {
		this.multiValueTableValues=multiValueTableValues;
	}

	public Collection getGeneralPks() {
		return map.keySet();
	}

	public Collection combine() {
		combineSingleValueTables();
		combineMultiValueTables();
		
		return map.values();
	}

	private void combineSingleValueTables() {
		if( Util.isEmpty( singleValueTableValues ) )
			return;
		
		for( int i=0; i<singleValueTableValues.length; i++ ) {
			Collection coll=singleValueTableValues[i];
			Table table=complexTable.getSingleValueTables()[i];
			SingleValueTableAdapter adapter=singleValueTableAdapters[i];
			
			for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
				Object obj=iter.next();
				// extract pkFields to get pk, then read cached entity from map,
				// combine obj into cached value.
				Column[] idColumns2=table.getIdColumns();
				Object[] pkFields=new Object[idColumns2.length];
				for( int j=0; j<idColumns2.length; j++ ) {
					Column column=idColumns2[j];
					pkFields[j]=column.getAdapter().extractFieldValue( obj );
				}
				GeneralPK pk=new GeneralPK( pkFields );
				Object mapValue=map.get( pk );
				
				adapter.combine( mapValue, obj );
			}
		}
	}

	private void combineMultiValueTables() {
		if( Util.isEmpty( multiValueTableValues ) ) {
			return;
		}
		
		for( int i=0; i<multiValueTableValues.length; i++ ) {
			// key/value=pk<GeneralPK>/multiValues<List>
			Map pkCollectionMap=new HashMap();
			
			Collection coll=multiValueTableValues[i];
			Table table=complexTable.getMultiValueTables()[i];
			MultiValueTableAdapter adapter=multiValueTableAdapters[i];
			
			for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
				Object obj=iter.next();
				// extract pkFields to get pk, then read cached entity from map,
				// combine obj into cached value.
				Column[] idColumns2=table.getIdColumns();
				Object[] pkFields=new Object[idColumns2.length];
				for( int j=0; j<idColumns2.length; j++ ) {
					Column column=idColumns2[j];
					pkFields[j]=column.getAdapter().extractFieldValue( obj );
				}
				GeneralPK pk=new GeneralPK( pkFields );
				
				List values=(List) pkCollectionMap.get( pk );
				if( values==null ) {
					values=new LinkedList();
					pkCollectionMap.put( pk, values );
				}
				
				values.add( obj );
			}
			
			for( Iterator iter=pkCollectionMap.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry entry=(Map.Entry) iter.next();
				GeneralPK pk=(GeneralPK) entry.getKey();
				List values= (List) entry.getValue();
				
				Object mapValue=map.get( pk );
				adapter.combine( mapValue, values.toArray() );
			}
		}
	}
}
