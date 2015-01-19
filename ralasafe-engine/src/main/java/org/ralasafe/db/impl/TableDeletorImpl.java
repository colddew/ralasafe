/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.db.Column;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableDeletor;
import org.ralasafe.db.WhereElement;
import org.ralasafe.db.WhereElementUtil;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.Util;

public class TableDeletorImpl implements TableDeletor {
	private Table table;
	private String deleteSql;
	
	private static Log logger=LogFactory.getLog( TableUpdatorImpl.class );
	
	public Table getTable() {
		return table;
	}

	public void setTable( Table table ) {
		this.table=table;
		
		if( !Util.isEmpty( table.getIdColumns() ) ) {
			deleteSql=DBUtil.deleteSql( table.getName(), table.getIdColumnNames() );
		}
	}

	public void deleteByIdColumns( Object o ) {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			deleteByIdColumns( conn, o );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	public void deleteByIdColumns( Connection conn, Object o ) {
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( deleteSql );
			// set DELETE WHERE PART VALUES
			Column[] idColumns=table.getIdColumns();			
			for( int i=0; i<idColumns.length; i++ ) {
				Column column=idColumns[i];
				ColumnAdapter columnAdapter=column.getAdapter();
				columnAdapter.setPreparedStatement( pstmt, i+1, o );
			}
			
			pstmt.executeUpdate();
		} catch( SQLException e ) {
			logger.error( "", e );
			
			throw new DBLevelException( e );
		} finally {
			DBUtil.close( pstmt );
		}
	}

	public void delete( WhereElement emt, Object o ) {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			delete( conn, emt, o );
		} finally {
			DBUtil.close( conn );
		}
	}
	
	public void delete( Connection conn, WhereElement emt, Object o ) {
		String sql=WhereElementUtil.toSql( emt );
		List byColumns=new LinkedList();
		List hints=new LinkedList();
		WhereElementUtil.extractColumns( emt, byColumns, o, hints );
		
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( "DELETE FROM " + table.getName() + " " + sql );
			
			if( byColumns.size()>0 ) {
				int i=1;
				Iterator hintIter=hints.iterator();
				for( Iterator columnIter=byColumns.iterator(); columnIter.hasNext(); ) {
					Column column=(Column) columnIter.next();
					ColumnAdapter adapter=column.getAdapter();
					adapter.setPreparedStatement( pstmt, i, hintIter.next() );
					i++;
				}
			}
			
			pstmt.executeUpdate();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close( pstmt );
		}
	}
}
