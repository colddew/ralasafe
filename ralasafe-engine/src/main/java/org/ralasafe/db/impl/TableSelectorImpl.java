/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.ObjectNewer;
import org.ralasafe.db.Column;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.GroupPart;
import org.ralasafe.db.OrderPart;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.SelectConditionUtil;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableSelector;
import org.ralasafe.db.WhereElement;
import org.ralasafe.db.WhereElementUtil;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.StringUtil;

/**
 * WARNING!!!!
 * We will deprecate it later.
 */
public class TableSelectorImpl implements TableSelector {
	private ObjectNewer objectNewer;
	private String selectByIdSql;
	private String selectByUniqueSql;
	private String selectSqlWithoutWherePart;
	private String selectCountByIdSql;
	private String selectCountByUniqueSql;
	
	private Table table;

	private static Log logger = LogFactory.getLog( TableSelectorImpl.class );
	
	public Table getTable() {
		return table;
	}

	public void setTable( Table table ) {
		this.table=table;
		
		SelectCondition byIdCdtn=SelectConditionUtil.simplyConnectColumns( table.getIdColumns() );
		SelectCondition byUniqueCdtn=SelectConditionUtil.simplyConnectColumns( table.getUniqueColumns() );
		
		selectSqlWithoutWherePart=DBUtil.selectSql( table.getName(), table.getColumnNames() );
		
		if( byIdCdtn!=null ) {
			String sql=WhereElementUtil.toSql( byIdCdtn );
			selectByIdSql=selectSqlWithoutWherePart + sql;
			selectCountByIdSql="select count(1) from " + table.getName() + sql;
		}
		
		if( byUniqueCdtn!=null ) {
			String sql=WhereElementUtil.toSql( byUniqueCdtn );
			selectByUniqueSql=selectSqlWithoutWherePart + sql;
			selectCountByUniqueSql="select count(1) from " + table.getName() + sql;
		}
	}

	public TableSelectorImpl() {
	}

	public void setObjectNewer( ObjectNewer objectNewer ) {
		this.objectNewer=objectNewer;
	}

	
	public Collection select( SelectCondition cdtn, Object o )
			throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return select( conn, cdtn, o );
		} finally {
			DBUtil.close( conn );
		}
	}

	
	public Object selectByIdColumns( Object o ) throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return selectByIdColumns( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}

	
	public Object selectByUniqueColumns( Object o )
			throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return selectByUniqueColumns( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}

	
	public Collection select( Connection conn, SelectCondition cdtn,
			Object o ) throws DBLevelException {
		String selectSql=selectSqlWithoutWherePart + WhereElementUtil.toSql( cdtn );
		return select( conn, selectSql, cdtn, o );
	}
	
	public Collection selectByPage( SelectCondition cdtn,
			Object o,
			int fromIndex, int pageSize ) throws DBLevelException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return selectByPage( conn, cdtn, o, fromIndex, pageSize );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	public Collection selectByPage( Connection conn, 
			SelectCondition cdtn,
			Object o,
			int fromIndex, int pageSize ) throws DBLevelException {
		String selectSql="";
		String dbName;
		try {
			dbName=conn.getMetaData().getDatabaseProductName();
			dbName=dbName.toUpperCase();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		}
		
		if( dbName.indexOf( "MYSQL" )!=-1 ) {
			//SQL:select .. from .. order by .. limit ?,?
			selectSql=selectSqlWithoutWherePart 
			+ WhereElementUtil.toSql( cdtn )
			+ " limit " + (fromIndex-1)
			+ "," + pageSize;
		} else if( dbName.indexOf( "ORACLE" )!=-1 ) {
			StringBuffer buff=new StringBuffer();
			buff.append( "SELECT " );
			StringUtil.append( buff, table.getColumnNames() );
			buff.append( " FROM (" );
			buff.append( "select *, rownum as __rn from (" )
				.append( selectSqlWithoutWherePart ) 
				.append( WhereElementUtil.toSql( cdtn ) ) 
				.append( ") where __rn<=" +(fromIndex+pageSize-1) )
				.append( ") where __rn>=" + fromIndex );
			selectSql=buff.toString();
		} else if( dbName.indexOf( "DB2" )!=-1 ) {
			//SQL LOOKS LIKE
			//select * from ( 
			//SELECT rownumber() 
			//over(ORDER BY SCORE DESC) 
			//as row_, * FROM STUDENT ORDER BY SCORE DESC ) 
			//as temp_ where row_ between 11 and 20
			
			StringBuffer buff=new StringBuffer();
			buff.append( "SELECT " );
			StringUtil.append( buff, table.getColumnNames() );
			buff.append( " FROM (" );
			buff.append( "SELECT " );
			StringUtil.append( buff, table.getColumnNames() );
			buff.append( ",rownumber() over(" );
			append( buff, cdtn.getOrderPart() );
			buff.append( ") as __rn FROM " )
				.append( table.getName() );
			append( buff, cdtn.getOrderPart() );
			append( buff, cdtn.getGroupPart() );
			buff.append( ") as temp_ where __rn between " )
				.append( fromIndex )
				.append( " and " )
				.append( fromIndex+pageSize-1 );			
			
			selectSql=buff.toString();
		}
		
		if( logger.isDebugEnabled() ) {
			logger.debug( selectSql );
		}
		
		if( dbName.indexOf( "MYSQL" )!=-1
				|| dbName.indexOf( "ORACLE" )!=-1
				|| dbName.indexOf( "DB2" )!=-1 ) {
			return select( conn, selectSql, cdtn, o );
		} else {
			return select( conn, selectSql, cdtn, o, fromIndex, pageSize );
		}
		
	}
	
	private static void append( StringBuffer buff, GroupPart groupPart ) {
		if( groupPart==null||groupPart.getColumnNames().length==0 )
			return;
		
		String[] columnNames=groupPart.getColumnNames();
		
		buff.append( " group by " );
		for( int i = 0; i < columnNames.length; i++ ) {
			if( i!=0 ) {
				buff.append( "," );
			}
			buff.append( columnNames[i] );
		}
	}
	
	private static void append( StringBuffer buff, OrderPart orderPart ) {
		if( orderPart==null||orderPart.getColumnNames().length==0 )
			return;
		
		String[] columnNames=orderPart.getColumnNames();
		String[] orderTypes=orderPart.getOrderTypes();
		
		buff.append( " ORDER BY " );
		for( int i=0; i<columnNames.length; i++ ) {
			if( i!=0 ) {
				buff.append( "," );
			}
			buff.append( columnNames[i] );
			if( orderTypes!=null && "DESC".equalsIgnoreCase( orderTypes[i] ) ) {
				buff.append( " DESC" );
			}
		}
	}
	
	public Object selectByIdColumns( Connection conn, Object o )
			throws DBLevelException {
		Collection collection=select( conn, selectByIdSql, table.getIdColumns(), o );
		if( collection.size()>0 ) {
			return collection.iterator().next();
		} else {
			return null;
		}
	}

	
	public Object selectByUniqueColumns( Connection conn,
			Object o ) throws DBLevelException {
		Collection collection=select( conn, selectByUniqueSql, table.getUniqueColumns(), o );
		if( collection.size()>0 ) {
			return collection.iterator().next();
		} else {
			return null;
		}
	}

	private Collection select( Connection conn, String pstmtSql, SelectCondition cdtn, Object o )
			throws DBLevelException {
		List selectByColumns=new LinkedList();
		List hints=new LinkedList();
		WhereElement whereElement=null;
		if( cdtn!=null ) {
			whereElement=cdtn.getWhereElement();
		}
		WhereElementUtil.extractColumns( whereElement, selectByColumns, o, hints );
		
		return select( conn, pstmtSql, selectByColumns, hints );
	}
	
	private Collection select( Connection conn, String pstmtSql,
			SelectCondition cdtn, Object o,
			int startIndex, int pageSize ) throws DBLevelException {
		List selectByColumns=new LinkedList();
		List hints=new LinkedList();
		WhereElementUtil.extractColumns( cdtn==null?null:cdtn.getWhereElement(),
				selectByColumns, o, hints );
		return select( conn, pstmtSql, selectByColumns, hints, startIndex, pageSize );
	}
	
	private Collection select( Connection conn, String pstmtSql,
			List selectByColumns, List hints ) throws DBLevelException {
		List result=new LinkedList();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement( pstmtSql );
			if( selectByColumns!=null ) {
				Iterator columnIter=selectByColumns.iterator();
				Iterator hintsIter=hints.iterator();
				int i=1;
				while( columnIter.hasNext() ) {
					Column column=(Column) columnIter.next();
					ColumnAdapter adapter=column.getAdapter();
					adapter.setPreparedStatement( pstmt, i, hintsIter.next() );
					i++;
				}
			}
			
			rs=pstmt.executeQuery();
			while( rs.next() ) {
				Object obj=objectNewer.newObject();
				Column[] columns=table.getColumns();
				for( int i=0; i<columns.length; i++ ) {
					Column column=columns[i];
					column.getAdapter().readResultSet( rs, i+1, obj );
				}
				result.add( obj );
			}
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt );
		}
		return result;
	}

	private Collection select( Connection conn, String pstmtSql,
			List selectByColumns, List hints,
			int startIndex, int pageSize ) throws DBLevelException {
		List result=new LinkedList();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement( pstmtSql );
			if( selectByColumns!=null ) {
				Iterator columnIter=selectByColumns.iterator();
				Iterator hintsIter=hints.iterator();
				int i=1;
				while( columnIter.hasNext() ) {
					Column column=(Column) columnIter.next();
					ColumnAdapter adapter=column.getAdapter();
					adapter.setPreparedStatement( pstmt, i, hintsIter.next() );
					i++;
				}
			}
			
			rs=pstmt.executeQuery();
			
			int index=1;
			while( index<startIndex&&rs.next() ) {
				index++;
			}
			
			int readSize=0;
			while( rs.next() && readSize<pageSize ) {
				Object obj=objectNewer.newObject();
				Column[] columns=table.getColumns();
				for( int i=0; i<columns.length; i++ ) {
					Column column=columns[i];
					column.getAdapter().readResultSet( rs, i+1, obj );
				}
				result.add( obj );
				readSize++;
			}
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt );
		}
		return result;
	}
	
	private Collection select( Connection conn, String pstmtSql, Column[] selectByColumns, Object o )
			throws DBLevelException {
		List result=new LinkedList();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement( pstmtSql );
			
			if( selectByColumns!=null ) {
				for( int i=0; i<selectByColumns.length; i++ ) {
					ColumnAdapter adapter=selectByColumns[i].getAdapter();
					adapter.setPreparedStatement( pstmt, i+1, o );
				}
			}
			
			rs=pstmt.executeQuery();
			
			while( rs.next() ) {
				Object obj=objectNewer.newObject();
				Column[] columns=table.getColumns();
				for( int i=0; i<columns.length; i++ ) {
					Column column=columns[i];
					column.getAdapter().readResultSet( rs, i+1, obj );
				}
				result.add( obj );
			}
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt );
		}

		return result;
	}
	
	
	public int selectCount( SelectCondition cdtn, Object o ) {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return selectCount( conn, cdtn, o );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	
	public int selectCount( Connection conn, SelectCondition cdtn, Object o ) {
		String sql="select count(1) from " + table.getName() + WhereElementUtil.toSql( cdtn ); 
		return selectCount( conn, sql, cdtn, o );
	}
	
	
	public boolean isExistByIdColumns( Object o ) {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return isExistByIdColumns( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	
	public boolean isExistByUniqueColumns( Object o ) {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			return isExistByUniqueColumns( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	
	public boolean isExistByUniqueColumns( Connection conn, Object o ) {
		int selectCount=selectCount( conn, selectCountByUniqueSql, table.getUniqueColumns(), o );
		return selectCount>0 ? true: false;
	}

	
	public boolean isExistByIdColumns( Connection conn, Object o ) {
		int selectCount=selectCount( conn, selectCountByIdSql, table.getIdColumns(), o );
		return selectCount>0 ? true: false;
	}

	private int selectCount( Connection conn, String sql, SelectCondition cdtn, Object o ) 
	throws DBLevelException {
		List selectByColumns=new LinkedList();
		List hints=new LinkedList();
		WhereElementUtil.extractColumns( cdtn==null?null:cdtn.getWhereElement(), selectByColumns, o, hints );
		
		return selectCount( conn, sql, selectByColumns, hints );
	}
	
	private int selectCount( Connection conn, String sql, List selectByColumns, List hints ) 
	throws DBLevelException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement( sql );
			
			if( selectByColumns!=null ) {
				Iterator columnIter=selectByColumns.iterator();
				Iterator hintsIter=hints.iterator();
				int i=1;
				while( columnIter.hasNext() ) {
					Column column=(Column) columnIter.next();
					ColumnAdapter adapter=column.getAdapter();
					adapter.setPreparedStatement( pstmt, i, hintsIter.next() );
					i++;
				}
			}
			
			rs=pstmt.executeQuery();
			
			if( rs.next() ) {
				return rs.getInt( 1 );
			} else {
				return 0;
			}
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt );
		}
	}
	
	private int selectCount( Connection conn, String sql, Column[] selectByColumns, Object o ) 
	throws DBLevelException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement( sql );
			
			if( selectByColumns!=null ) {
				for( int i=0; i<selectByColumns.length; i++ ) {
					ColumnAdapter adapter=selectByColumns[i].getAdapter();
					adapter.setPreparedStatement( pstmt, i+1, o );
				}
			}
			
			rs=pstmt.executeQuery();
			
			if( rs.next() ) {
				return rs.getInt( 1 );
			} else {
				return 0;
			}
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt );
		}
	}
}
