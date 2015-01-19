/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.db.ComplexTable;
import org.ralasafe.db.ComplexTableDBHelper;
import org.ralasafe.db.ComplexTableDeletor;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.Table;
import org.ralasafe.util.Util;

public class ComplexTableDeletorImpl implements ComplexTableDeletor {
	private ComplexTable complexTable;
	private TableDeletorImpl mainTableDeletor;
	private TableDeletorImpl[] singleValueTableDeletors;
	private TableDeletorImpl[] multiValueTableDeletors;
	
	private static Log logger=LogFactory.getLog( ComplexTableDeletorImpl.class );
	
	public ComplexTable getComplexTable() {
		return complexTable;
	}
	public void setComplexTable( ComplexTable complexTable ) {
		this.complexTable=complexTable;
		
		Table mainTable=complexTable.getMainTable();
		mainTableDeletor=new TableDeletorImpl();
		mainTableDeletor.setTable( mainTable );
		
		Table[] singleValueTables=complexTable.getSingleValueTables();
		singleValueTableDeletors=getSingleValueTableDeletors( singleValueTables );
		
		Table[] multiValueTables=complexTable.getMultiValueTables();
		multiValueTableDeletors=getMultiValueTableDeletors( multiValueTables, complexTable );
	}

	/**
	 * 
	 * @param idColumns 
	 * @param multiValueTables
	 * @return
	 */
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
	
	private TableDeletorImpl[] getSingleValueTableDeletors( Table[] tables ) {
		TableDeletorImpl[] impls=null;
		if( tables!=null ) {
			impls=new TableDeletorImpl[tables.length];
			for( int i=0; i<tables.length; i++ ) {
				TableDeletorImpl impl=new TableDeletorImpl();
				impl.setTable( tables[i] );
				impls[i]=impl;
			}
		}
		
		return impls;
	}
	
	public void delete( Object o ) throws DBLevelException {
		ComplexTableDBHelper helper=new ComplexTableDBHelper();
		helper.setComplexTable( complexTable );
		
		try {
			//get connections
			helper.getConnections();
			helper.beginTransaction();
			
			mainTableDeletor.deleteByIdColumns( helper.getMainTableConn(), o );
			deleteSingleValues( helper.getSingleValueTableConns(), singleValueTableDeletors, o );
			deleteMultiValues( helper.getMultiValueTableConns(), multiValueTableDeletors, o );
			
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
	
	private void deleteMultiValues( Connection[] conns,
			TableDeletorImpl[] deletors, Object o ) {
		if( Util.isEmpty( deletors ) ) {
			return;
		}
		
		for( int i=0; i<deletors.length; i++ ) {
			deletors[i].deleteByIdColumns( o );
		}
	}
	
	private void deleteSingleValues( Connection[] conns,
			TableDeletorImpl[] deletors, Object o ) {
		if( Util.isEmpty( deletors ) ) {
			return;
		}
		
		for( int i=0; i<deletors.length; i++ ) {
			deletors[i].deleteByIdColumns( o );
		}
	}	
}
