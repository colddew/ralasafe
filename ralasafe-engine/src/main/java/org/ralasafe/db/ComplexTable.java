/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.util.Util;

/**
 * Unlike {@link Table}, <code>ComplexTable</code> means collection of many tables.
 * It include a MainTable(required), SingleValueTable(optional,zero,one or many),
 * MultiValueTable(optional,zero,one or many).
 * 
 * <p>
 * An entity contains many attributes. Some attributes are single normal values, 
 * like attr1=1, attr2="name". 
 * While others are collection values, like attr3=[1,2,3], attr4=["name1","name2","name3"].
 * 
 * Single value attributes store in MainTable and SingleValueTable, while collection
 * value attributes store in MultiValueTable.
 * </p>
 * 
 * <p>
 * For db structure, SingleValueTable and MultiValueTable, they also
 * contain idColumns of MainTable. And for SingleValueTable(s), idColumns
 * must be SingleValueTable's idColumns too.
 * </p>
 * 
 * <p>
 * There's no difference between MainTable and SingleValueTable.
 * It's suggested that attributes which should be loaded immediately (not lazily) stored in MainTable.
 * </p>
 *  
 * @author back
 *
 */
public class ComplexTable {
	private Table mainTable;
	/** SingleValueTables, maybe null */
	private Table[] singleValueTables;
	/** MutliValueTables, maybe null */
	private Table[] multiValueTables;
	
	public Table getMainTable() {
		return mainTable;
	}
	public void setMainTable( Table mainTable ) {
		this.mainTable=mainTable;
	}
	public Table[] getSingleValueTables() {
		return singleValueTables;
	}
	public void setSingleValueTables( Table[] singleValueTables ) {
		this.singleValueTables=singleValueTables;
	}
	public Table[] getMultiValueTables() {
		return multiValueTables;
	}
	public void setMultiValueTables( Table[] multiValueTables ) {
		this.multiValueTables=multiValueTables;
	}
	
	public SingleValueTableAdapter[] getDefaultSingleValueTableAdapters() {
		if( !Util.isEmpty( singleValueTables ) ) {
			SingleValueTableAdapter[] singleValueTableAdapters=
				new DefaultSingleValueTableAdapter[singleValueTables.length];
			for( int i=0; i<singleValueTables.length; i++ ) {
				Table table=singleValueTables[i];
				String mapKey=table.getId()+"";
				//String[] idColumnName=mainTable.getIdColumnNames();
				
				DefaultSingleValueTableAdapter adapter=new DefaultSingleValueTableAdapter( mapKey );
				singleValueTableAdapters[i]=adapter;
			}
			
			return singleValueTableAdapters;
		} else {
			return null;
		}
	}
	
	public MultiValueTableAdapter[] getDefaultMultiValueTableAdapters() {
		if( !Util.isEmpty( multiValueTables ) ) {
			DefaultMultiValueTableAdapter[] multiValueTableAdapters=
				new DefaultMultiValueTableAdapter[multiValueTables.length];
			for( int i=0; i<multiValueTables.length; i++ ) {
				Table table=multiValueTables[i];
				String mapKey=table.getId()+"";
				
				DefaultMultiValueTableAdapter adapter=new DefaultMultiValueTableAdapter( mapKey );
				multiValueTableAdapters[i]=adapter;
			}	
			
			return multiValueTableAdapters;
		} else {
			return null;
		}
	}
}
