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
import org.ralasafe.db.ComplexTableSaver;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.MultiValueTableAdapter;
import org.ralasafe.db.SingleValueTableAdapter;
import org.ralasafe.db.Table;
import org.ralasafe.util.Util;

public class ComplexTableSaverImpl implements ComplexTableSaver {
	private ComplexTable complexTable;
	
	private SingleValueTableAdapter[] singleValueAdapters;
	private MultiValueTableAdapter[] multiValueAdapters;
	
	private TableSaverImpl mainTableSaver;
	private TableSaverImpl[] singleValueTableSavers;
	private TableSaverImpl[] multiValueTableSavers;
	
	private static Log logger=LogFactory.getLog( ComplexTableSaverImpl.class );
	
	public ComplexTable getComplexTable() {
		return complexTable;
	}
	
	public void setComplexTable( ComplexTable complexTable ) {
		this.complexTable=complexTable;
		
		Table mainTable=complexTable.getMainTable();
		mainTableSaver=new TableSaverImpl();
		mainTableSaver.setTable( mainTable );
		
		Table[] singleValueTables=complexTable.getSingleValueTables();
		singleValueTableSavers=getTableSavers( singleValueTables );
		
		Table[] multiValueTables=complexTable.getMultiValueTables();
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
	
	public void save( Object o ) throws DBLevelException, EntityExistException {
		ComplexTableDBHelper helper=new ComplexTableDBHelper();
		helper.setComplexTable( complexTable );
		
		try {
			//get connections
			helper.getConnections();
			helper.beginTransaction();
			
			mainTableSaver.save( helper.getMainTableConn(), o );
			saveSingleValues( helper.getSingleValueTableConns(), singleValueTableSavers, o );
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
		} catch( EntityExistException e ) {
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

	private void saveSingleValues( Connection[] conns,
			TableSaverImpl[] savers, Object o ) throws DBLevelException, EntityExistException {
		if( Util.isEmpty( savers ) )
			return;
		
		for( int i=0; i<savers.length; i++ ) {
			Object extractValue=singleValueAdapters[i].extract( o );
			savers[i].save( conns[i], extractValue );
		}
	}	
}
