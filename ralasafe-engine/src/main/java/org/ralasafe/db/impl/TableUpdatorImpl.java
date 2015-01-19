/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.db.Column;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableUpdator;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.Util;

public class TableUpdatorImpl implements TableUpdator {

	private Table table;
	private String updateSql;
	private TableSelectorImpl selector;
	
	private static Log logger=LogFactory.getLog( TableUpdatorImpl.class );
	
	public Table getTable() {
		return table;
	}

	public void setTable( Table table ) {
		this.table=table;
		
		if( !Util.isEmpty( table.getIdColumns() ) ) {
			updateSql=DBUtil.updateSql( table.getName(), table.getIdColumnNames(), table.getExceptIdColumnNames() );
		}
		
		selector=new TableSelectorImpl();
		selector.setTable( table );
	}


	public void updateByIdColumns( Object newValue ) throws EntityExistException {
		Connection conn=null;
		try {
			conn=DBPower.getConnection( table.getId() );
			updateByIdColumns( conn, newValue );
		} finally {
			DBUtil.close( conn );
		}
	}

	public void updateByIdColumns( Connection conn, Object newValue )
			throws EntityExistException {
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( updateSql );
			//set UPDATE SET PART VALUES
			Column[] exceptIdColumns=table.getExceptIdColumns();
			for( int i=0; i<exceptIdColumns.length; i++ ) {
				Column column=exceptIdColumns[i];
				ColumnAdapter columnAdapter=column.getAdapter();
				columnAdapter.setPreparedStatement( pstmt, i+1, newValue );
			}
			
			// set UPDATE WHERE PART VALUES
			int offset=exceptIdColumns.length+1;
			Column[] idColumns=table.getIdColumns();			
			for( int i=0; i<idColumns.length; i++ ) {
				Column column=idColumns[i];
				ColumnAdapter columnAdapter=column.getAdapter();
				columnAdapter.setPreparedStatement( pstmt, i+offset, newValue );
			}
			
			pstmt.executeUpdate();
		} catch( SQLException e ) {
			logger.error( "", e );
			
			if( conn!=null ) {
				//violate pk or unique constraints?		
				if( !Util.isEmpty( table.getUniqueColumns() ) ) {
					boolean isExist=selector.isExistByUniqueColumns( conn, newValue );
					if( isExist ) {
						throw new EntityExistException( newValue );
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
}
