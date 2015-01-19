/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.util.DBUtil;

public class ComplexTableDBHelper {
	private ComplexTable complexTable;
	private Connection mainTableConn=null;
	private Connection[] singleValueTableConns=null;
	private Connection[] multiValueTableConns=null;
	
	private boolean mainTableOrignalCommitMode=true;
	private boolean[] singleValueTableOrignalCommitModes=null;
	private boolean[] multiValueTableOrignalCommitModes=null;
	
	private static Log logger=LogFactory.getLog( ComplexTableDBHelper.class );
	
	public ComplexTable getComplexTable() {
		return complexTable;
	}
	public void setComplexTable( ComplexTable complexTable ) {
		this.complexTable=complexTable;
	}
	public Connection getMainTableConn() {
		return mainTableConn;
	}
	public void setMainTableConn( Connection mainTableConn ) {
		this.mainTableConn=mainTableConn;
	}
	public Connection[] getSingleValueTableConns() {
		return singleValueTableConns;
	}
	public void setSingleValueTableConns( Connection[] singleValueTableConns ) {
		this.singleValueTableConns=singleValueTableConns;
	}
	public Connection[] getMultiValueTableConns() {
		return multiValueTableConns;
	}
	public void setMultiValueTableConns( Connection[] multiValueTableConns ) {
		this.multiValueTableConns=multiValueTableConns;
	}
	
	public void getConnections() {
		int singleValueTableSize=0;
		int multiValueTableSize=0;
		Table[] singleValueTables=complexTable.getSingleValueTables();
		Table[] multiValueTables=complexTable.getMultiValueTables();
		if( singleValueTables!=null ) {
			singleValueTableSize=singleValueTables.length;
		}		
		if( multiValueTables!=null ) {
			multiValueTableSize=multiValueTables.length;
		}
		
		int[] tableIds=new int[1+singleValueTableSize+multiValueTableSize];
		tableIds[0]=complexTable.getMainTable().getId();
		for( int i=0; i<singleValueTableSize; i++ ) {
			tableIds[i+1]=singleValueTables[i].getId();
		}
		for( int i=0; i<multiValueTableSize; i++ ) {
			tableIds[i+1+singleValueTableSize]=multiValueTables[i].getId();
		}
		
		Connection[] conns=DBPower.getConnections( tableIds );
		mainTableConn=conns[0];
		if( singleValueTableSize>0 ) {
			singleValueTableConns=new Connection[singleValueTableSize];
			for( int i=0; i<singleValueTableSize; i++ ) {
				singleValueTableConns[i]=conns[i+1];
			}
		}
		if( multiValueTableSize>0 ) {
			multiValueTableConns=new Connection[multiValueTableSize];
			for( int i=0; i<multiValueTableSize; i++ ) {
				multiValueTableConns[i]=conns[i+1+singleValueTableSize];
			}
		}
	}
	
	public void beginTransaction() throws SQLException {
		turnAutoCommitOff();
	}
	
	public void commit() throws SQLException {
		mainTableConn.commit();
		commit( singleValueTableConns );
		commit( multiValueTableConns );
	}
	
	public void rollback() throws SQLException {
		if( mainTableConn!=null ) {
			mainTableConn.rollback();
		}
		
		rollback( singleValueTableConns );
		rollback( multiValueTableConns );
	}
	
	public void closeConnections() {
		resetAutoCommitModes();
		
		DBUtil.close( mainTableConn );
		close( singleValueTableConns );
		close( multiValueTableConns );
	}
	
	private void close( Connection[] conns ) {
		if( conns!=null ) {
			for( int i=0; i<conns.length; i++ ) {
				DBUtil.close( conns[i] );
			}
		}
	}
	
	private void rollback( Connection[] conns ) throws SQLException {
		if( conns!=null ) {
			for( int i=0; i<conns.length; i++ ) {
				if( conns[i]!=null ) {
					conns[i].rollback();
				}
			}
		}
	}
	
	private void commit( Connection[] conns ) throws SQLException {
		if( conns!=null ) {
			for( int i=0; i<conns.length; i++ ) {
				if( conns[i]!=null ) {
					conns[i].commit();
				}
			}
		}
	}
	
	private void turnAutoCommitOff() throws SQLException {
		recordAutoCmmitModes();
		
		if( mainTableOrignalCommitMode ) {
			mainTableConn.setAutoCommit( false );
		}
		turnAutoCommitOff( singleValueTableConns, singleValueTableOrignalCommitModes );
		turnAutoCommitOff( multiValueTableConns, multiValueTableOrignalCommitModes );
	}
	
	private void turnAutoCommitOff( Connection[] conns,
			boolean[] modes ) throws SQLException {
		if( conns!=null ) {
			for( int i=0; i<conns.length; i++ ) {
				if( modes[i] ) {
					conns[i].setAutoCommit( false );
				}
			}
		}
	}
	
	/**
	 * Record connection's mode of MainTable's, SingleValueTables' and MultiValueTables'.
	 * 
	 * @throws SQLException 
	 */
	private void recordAutoCmmitModes() throws SQLException {
		mainTableOrignalCommitMode=mainTableConn.getAutoCommit();
		singleValueTableOrignalCommitModes=getOrignalCommitModes( singleValueTableConns );
		multiValueTableOrignalCommitModes=getOrignalCommitModes( multiValueTableConns );
	}
	
	private boolean[] getOrignalCommitModes( Connection[] conns ) throws SQLException {
		if( conns==null )
			return null;
		
		boolean[] modes=new boolean[conns.length];
		for( int i=0; i<conns.length; i++ ) {
			modes[i]=conns[i].getAutoCommit();
		}
		
		return modes;
	}

	private void resetAutoCommitModes() {
		if( mainTableConn!=null&&mainTableOrignalCommitMode ) {
			try {
				mainTableConn.setAutoCommit( mainTableOrignalCommitMode );
			} catch( SQLException e ) {
				logger.error( "", e );
			}
		}
		
		resetCommitModes( singleValueTableConns, singleValueTableOrignalCommitModes );
		resetCommitModes( multiValueTableConns, multiValueTableOrignalCommitModes );		
	}
	
	private void resetCommitModes( Connection[] conns,
			boolean[] modes ) {
		if( conns!=null ) {
			for( int i=0; i<conns.length; i++ ) {
				if( modes[i] ) {
					try {
						conns[i].setAutoCommit( modes[i] );
					} catch( SQLException e ) {
						logger.error( "", e );
					}
				}
			}
		}
	}
}
