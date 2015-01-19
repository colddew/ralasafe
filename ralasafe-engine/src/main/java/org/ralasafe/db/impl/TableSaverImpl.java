/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.Column;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableSaver;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.DefaultPageBatch;
import org.ralasafe.util.Util;

public class TableSaverImpl implements TableSaver {
	private Table table;
	private String insertSql;
	private TableSelectorImpl selector;
	
	private static Log logger=LogFactory.getLog( TableSelectorImpl.class );
	
	public Table getTable() {
		return table;
	}
	public void setTable( Table table ) {
		this.table=table;
		insertSql=DBUtil.insertSql( table.getName(), table.getColumnNames() );
		
		selector=new TableSelectorImpl();
		selector.setTable( table );
	}
	
	public int[] batchSave( Object[] os )
			throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return batchSave( conn, os );
		} finally {
			DBUtil.close( conn );
		}
	}

	public Collection batchSave( Collection coll )
			throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return batchSave( conn, coll );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	
	public int[] batchSave( Connection conn, Object[] os )
			throws DBLevelException {
		if( os==null ) 
			return null;
		
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( insertSql );
			Column[] columns=table.getColumns();
			
			ArrayPageBatch apb=new ArrayPageBatch();
			apb.setPageSize( DBPower.getMaxBatchSize() );
			apb.setColumns( columns );
			apb.setPreparedStatement( pstmt );
			apb.doBatch( os );
			
			return apb.getResult();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} catch( Exception e ) {
			logger.error( "", e );
			throw new RalasafeException( e );
		} finally {
			DBUtil.close( pstmt );
		}
	}

	
	public Collection batchSave( Connection conn,
			Collection coll ) throws DBLevelException {
		if( coll==null ) 
			return null;
		
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( insertSql );
			Column[] columns=table.getColumns();
			
			CollectionPageBatch cpb=new CollectionPageBatch();
			cpb.setPageSize( DBPower.getMaxBatchSize() );
			cpb.setColumns( columns );
			cpb.setPreparedStatement( pstmt );
			cpb.doBatch( coll );
			
			return cpb.getResult();
		} catch( SQLException e ) {
			logger.error( "", e );			
			throw new DBLevelException( e );
		} catch( Exception e ) {
			logger.error( "", e );
			throw new RalasafeException( e );
		} finally {
			DBUtil.close( pstmt );
		}
	}

	
	public void save( Object o ) throws EntityExistException,
			DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			save( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}

	
	public void save( Connection conn, Object o )
			throws EntityExistException, DBLevelException {
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( insertSql );
			Column[] columns=table.getColumns();
			for( int i=0; i<columns.length; i++ ) {
				Column column=columns[i];
				ColumnAdapter columnAdapter=column.getAdapter();
				columnAdapter.setPreparedStatement( pstmt, i+1, o );
			}
			
			pstmt.executeUpdate();
		} catch( SQLException e ) {
			logger.error( "", e );
			
			if( conn!=null ) {
				// violate pk or unique constraints?
				if( !Util.isEmpty( table.getIdColumns() ) ) {
					boolean isExist=selector.isExistByIdColumns( conn, o );
					if( isExist ) {
						throw new EntityExistException( o );
					}
				}				
				
				if( !Util.isEmpty( table.getUniqueColumns() ) ) {
					boolean isExist=selector.isExistByUniqueColumns( conn, o );
					if( isExist ) {
						throw new EntityExistException( o );
					}
				}
				
				throw new DBLevelException( e );
			} else {
				throw new DBLevelException( e );
			}			
		} finally {
			DBUtil.close( pstmt );
		}
	}

	class ArrayPageBatch extends DefaultPageBatch {
		private Column[] columns;
		private PreparedStatement pstmt;
		private int[] result;
		private int index=0;
		
		public int[] getResult() {
			return result;
		}

		public void setColumns( Column[] columns ) {
			this.columns=columns;
		}

		public void setPreparedStatement( PreparedStatement pstmt ) {
			this.pstmt=pstmt;
		}

		
		public void doBatch( Object[] objs ) throws Exception {
			result=new int[ objs.length ];
			super.doBatch( objs );
		}

		
		public void doInPage( Object[] objs ) throws SQLException {
			for( int osIndex=0; osIndex<objs.length; osIndex++ ) {
				for( int colIndex=0; colIndex<columns.length; colIndex++ ) {
					Column column=columns[colIndex];
					ColumnAdapter columnAdapter=column.getAdapter();
					columnAdapter.setPreparedStatement( pstmt, colIndex+1, objs[osIndex] );
				}
				pstmt.addBatch();
			}
			
			int[] pageResult=pstmt.executeBatch();
			//COPY insert page result into batch result
			for( int i=0;i<pageResult.length; i++ ) {
				result[index]=pageResult[i];
				index++;
			}
		}
	}
	
	class CollectionPageBatch extends DefaultPageBatch {
		private Column[] columns;
		private PreparedStatement pstmt;
		private Collection result;
		
		public Collection getResult() {
			return result;
		}

		public void setColumns( Column[] columns ) {
			this.columns=columns;
		}

		public void setPreparedStatement( PreparedStatement pstmt ) {
			this.pstmt=pstmt;
		}

		
		public void doBatch( Collection coll ) throws Exception {
			result=new ArrayList( coll.size() );
			super.doBatch( coll );			
		}

		
		public void doInPage( Collection coll ) throws SQLException {
			for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
				Object o=iter.next();
				for( int colIndex=0; colIndex<columns.length; colIndex++ ) {
					Column column=columns[colIndex];
					ColumnAdapter columnAdapter=column.getAdapter();
					columnAdapter.setPreparedStatement( pstmt, colIndex+1, o );
				}
				pstmt.addBatch();
			}
			
			int[] pageResult=pstmt.executeBatch();
			//COPY insert page result into batch result
			for( int i=0;i<pageResult.length; i++ ) {
				result.add( new Integer( pageResult[i] ) );
			}
		}
	}
}