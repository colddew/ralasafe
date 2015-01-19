/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.db.ComplexTable;
import org.ralasafe.db.ComplexTableDBHelper;
import org.ralasafe.db.ComplexTableUpdator;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.MultiValueTableAdapter;
import org.ralasafe.db.SingleValueTableAdapter;
import org.ralasafe.db.Table;
import org.ralasafe.util.Util;

public class ComplexTableUpdatorImpl implements ComplexTableUpdator {
	private ComplexTable complexTable;

	private SingleValueTableAdapter[] singleValueAdapters;
	private MultiValueTableAdapter[] multiValueAdapters;
	
	private TableSelectorImpl mainTableSelector;
	private TableUpdatorImpl mainTableUpdator;
	private TableDeletorImpl[] singleValueTableDeletors;
	private TableUpdatorImpl[] singleValueTableUpdators;
	private TableDeletorImpl[] multiValueTableDeletors;
	private TableSaverImpl[] multiValueTableSavers;
	
	private static Log logger=LogFactory.getLog( ComplexTableUpdatorImpl.class );
	
	public ComplexTable getComplexTable() {
		return complexTable;
	}
	public void setComplexTable( ComplexTable complexTable ) {
		this.complexTable=complexTable;
		
		Table mainTable=complexTable.getMainTable();
		mainTableSelector=new TableSelectorImpl();
		mainTableSelector.setTable( mainTable );
		mainTableUpdator=new TableUpdatorImpl();
		mainTableUpdator.setTable( mainTable );
		
		Table[] singleValueTables=complexTable.getSingleValueTables();
		singleValueTableUpdators=getSingleValueTableUpdators( singleValueTables );
		singleValueTableDeletors=getSingleValueTableDeletors( singleValueTables );
		
		Table[] multiValueTables=complexTable.getMultiValueTables();
		multiValueTableDeletors=getMultiValueTableDeletors( multiValueTables, complexTable );
		multiValueTableSavers=getTableSavers( multiValueTables );
	}
	
	public SingleValueTableAdapter[] getSingleValueAdapters() {
		return singleValueAdapters;
	}
	public void setSingleValueAdapters( SingleValueTableAdapter[] singleValueAdapters ) {
		this.singleValueAdapters=singleValueAdapters;
	}
	public MultiValueTableAdapter[] getMultiValueAdapters() {
		return multiValueAdapters;
	}

	public void setMultiValueAdapters( MultiValueTableAdapter[] multiValueAdapters ) {
		this.multiValueAdapters=multiValueAdapters;
	}
	
	public void update( Object o ) throws EntityExistException,
			DBLevelException {
		if( !mainTableSelector.isExistByIdColumns( o ) ) {
			return; 
		}
		
		ComplexTableDBHelper helper=new ComplexTableDBHelper();
		helper.setComplexTable( complexTable );
		
		try {
			//get connections
			helper.getConnections();
			helper.beginTransaction();
			
			mainTableUpdator.updateByIdColumns( helper.getMainTableConn(), o );
			updateSingleValues( helper.getSingleValueTableConns(), singleValueTableUpdators, o );
			deleteMultiValues( helper.getMultiValueTableConns(), multiValueTableDeletors, o );
			saveMultiValues( helper.getMultiValueTableConns(), multiValueTableSavers, o );
			
			helper.commit();
		} catch( DBLevelException e ) {
			try {
				helper.rollback();
			} catch( SQLException sqle ) {
				logger.error( "", sqle );
				throw new DBLevelException( sqle );
			}
			throw e;
		} catch( SQLException e ) {
			logger.error( "", e );
			try {
				helper.rollback();
			} catch( SQLException sqle ) {
				logger.error( "", sqle );
				throw new DBLevelException( sqle );
			}
			throw new DBLevelException( e );
		} finally {
			helper.closeConnections();
		}
	}
	
	private void saveMultiValues( Connection[] conns,
			TableSaverImpl[] savers, Object o ) {
		if( Util.isEmpty( savers ) )
			return;
		
		for( int i=0; i<savers.length; i++ ) {
			MultiValueTableAdapter adapter=multiValueAdapters[i];
			Object[] extractValues=adapter.extract( o );
			
			savers[i].batchSave( conns[i], extractValues );
		}
	}
	
	private void deleteMultiValues( Connection[] conns,
			TableDeletorImpl[] deletors, Object o ) {
		if( Util.isEmpty( deletors ) ) {
			return;
		}
		
		for( int i=0; i<deletors.length; i++ ) {
			deletors[i].deleteByIdColumns( o );
		}
	}
	
	private void updateSingleValues( Connection[] conns,
			TableUpdatorImpl[] updators, Object o ) throws EntityExistException {
		if( Util.isEmpty( updators ) )
			return;
		
		for( int i=0; i<updators.length; i++ ) {
			Object extractValue=singleValueAdapters[i].extract( o );
			if( extractValue==null ) {
				//delete original values
				singleValueTableDeletors[i].deleteByIdColumns( o );
			} else {
				updators[i].updateByIdColumns( conns[i], extractValue );
			}
		}
	}
	
	private TableSaverImpl[] getTableSavers( Table[] tables ) {
		TableSaverImpl[] savers=null;
		if( tables!=null ) {
			savers=new TableSaverImpl[tables.length];
			for( int i=0; i<tables.length; i++ ) {
				TableSaverImpl impl=new TableSaverImpl();
				impl.setTable( tables[i] );
				savers[i]=impl;
			}
		}
		
		return savers;
	}
	
	private TableUpdatorImpl[] getSingleValueTableUpdators(
			Table[] tables ) {
		if( Util.isEmpty( tables ) )
			return null;
		
		TableUpdatorImpl[] impls=new TableUpdatorImpl[ tables.length ];
		for( int i=0; i<tables.length; i++ ) {
			TableUpdatorImpl impl=new TableUpdatorImpl();
			impl.setTable( tables[i] );
			impls[i]=impl;
		}
		
		return impls;
	}
	
	private TableDeletorImpl[] getSingleValueTableDeletors(
			Table[] tables ) {
		if( Util.isEmpty( tables ) )
			return null;
		
		TableDeletorImpl[] impls=new TableDeletorImpl[ tables.length ];
		for( int i=0; i<tables.length; i++ ) {
			TableDeletorImpl impl=new TableDeletorImpl();
			impl.setTable( tables[i] );
			impls[i]=impl;
		}
		
		return impls;
	}
	
	private TableDeletorImpl[] getMultiValueTableDeletors(
			Table[] tables, ComplexTable complexTable ) {
		TableDeletorImpl[] impls=null;
		if( tables!=null ) {
			impls=new TableDeletorImpl[tables.length];
			for( int i=0; i<tables.length; i++ ) {
				TableDeletorImpl impl=new TableDeletorImpl();
				Table multiValueTable=tables[i];
				Table newTable=new Table();
				newTable.setId( multiValueTable.getId() );
				newTable.setName( multiValueTable.getName() );
				newTable.setIdColumnNames( complexTable.getMainTable().getIdColumnNames() );
				newTable.setIdColumns( complexTable.getMainTable().getIdColumns() );
				
				impl.setTable( newTable );
				impls[i]=impl;
			}
		}
		
		return impls;
	}
}
