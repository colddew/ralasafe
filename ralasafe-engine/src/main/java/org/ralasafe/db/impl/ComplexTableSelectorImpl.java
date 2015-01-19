/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.ObjectNewer;
import org.ralasafe.db.Column;
import org.ralasafe.db.ComplexTable;
import org.ralasafe.db.ComplexTableDBHelper;
import org.ralasafe.db.ComplexTableSelector;
import org.ralasafe.db.ComplexTableValueCombiner;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.InnerWhereElement;
import org.ralasafe.db.MultiValueTableAdapter;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.SelectConditionUtil;
import org.ralasafe.db.SingleValueTableAdapter;
import org.ralasafe.db.Table;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.Util;

public class ComplexTableSelectorImpl implements ComplexTableSelector {
	private ComplexTable complexTable;
	
	private ObjectNewer mainTableObjectNewer;
	private SingleValueTableAdapter[] singleValueAdapters;
	private MultiValueTableAdapter[] multiValueAdapters;
	
	private TableSelectorImpl mainTableSelector;
	private TableSelectorImpl[] singleValueTableSelectors;
	private TableSelectorImpl[] multiValueTableSelectors;
	
	private SelectCondition byIdColumnsSelectCdtn;
	
	private static Log logger=LogFactory.getLog( ComplexTableSelectorImpl.class );
	
	public ComplexTable getComplexTable() {
		return complexTable;
	}

	public void setComplexTable( ComplexTable complexTable ) {
		this.complexTable=complexTable;
		
		byIdColumnsSelectCdtn=SelectConditionUtil.simplyConnectColumns( complexTable.getMainTable().getIdColumns() );
		
		Table mainTable=complexTable.getMainTable();
		mainTableSelector=new TableSelectorImpl();
		mainTableSelector.setTable( mainTable );
		innerSetMainTableObjectNewer();
		
		Table[] singleValueTables=complexTable.getSingleValueTables();
		singleValueTableSelectors=getTableSelectors( singleValueTables );
		innerSetSingleValueTableObjectNewer();
		
		Table[] multiValueTables=complexTable.getMultiValueTables();
		multiValueTableSelectors=getTableSelectors( multiValueTables );
		innerSetMultiValueTableObjectNewer();
	}

	public void setMainTableObjectNewer( ObjectNewer mainTableObjectNewer ) {
		this.mainTableObjectNewer=mainTableObjectNewer;
		
		innerSetMainTableObjectNewer();
	}

	private void innerSetMainTableObjectNewer() {
		if( mainTableSelector!=null ) {
			mainTableSelector.setObjectNewer( mainTableObjectNewer );
		}
	}

	private void innerSetSingleValueTableObjectNewer() {
		if( Util.isEmpty( singleValueAdapters ) || Util.isEmpty( singleValueTableSelectors ) )
			return;
		
		for( int i=0; i<singleValueAdapters.length; i++ ) {
			singleValueTableSelectors[i].setObjectNewer( singleValueAdapters[i].getObjectNewer() );
		}
	}
	
	private void innerSetMultiValueTableObjectNewer() {
		if( Util.isEmpty( multiValueAdapters ) || Util.isEmpty( multiValueTableSelectors ) )
			return;
		
		for( int i=0; i<multiValueAdapters.length; i++ ) {
			multiValueTableSelectors[i].setObjectNewer( multiValueAdapters[i].getObjectNewer() );
		}
	}

	private TableSelectorImpl[] getTableSelectors( Table[] tables ) {
		if( Util.isEmpty( tables ) )
			return null;
		
		TableSelectorImpl[] impls=new TableSelectorImpl[ tables.length ];
		for( int i=0; i<tables.length; i++ ) {
			TableSelectorImpl impl=new TableSelectorImpl();
			impl.setTable( tables[i] );
			
			impls[i]=impl;
		}
		
		return impls;
	}

	public SingleValueTableAdapter[] getSingleValueAdapters() {
		return singleValueAdapters;
	}

	public void setSingleValueAdapters( SingleValueTableAdapter[] singleValueAdapters ) {
		this.singleValueAdapters=singleValueAdapters;
		
		innerSetSingleValueTableObjectNewer();
	}

	public MultiValueTableAdapter[] getMultiValueAdapters() {
		return multiValueAdapters;
	}

	public void setMultiValueAdapters( MultiValueTableAdapter[] adapters ) {
		this.multiValueAdapters=adapters;
		
		innerSetMultiValueTableObjectNewer();
	}

	public Object selectByIdColumns( Object o ) throws DBLevelException {
		// load main table values
		Object selectValue=mainTableSelector.selectByIdColumns( o );
		if( selectValue==null )
			return null;
		
		// load single value table values
		if( !Util.isEmpty( singleValueTableSelectors ) ) {
			for( int i=0; i<singleValueTableSelectors.length; i++ ) {
				Object temp=singleValueAdapters[i].extractEvenNoValueExist( o );
				Object tableSelectValue=singleValueTableSelectors[i].selectByIdColumns( temp );
				if( tableSelectValue!=null ) {
					singleValueAdapters[i].combine( selectValue, tableSelectValue );
				}
			}
		}
		
		// load multi value table values
		if( !Util.isEmpty( multiValueTableSelectors ) ) {
			for( int i=0; i<multiValueTableSelectors.length; i++ ) {
				Object temp=multiValueAdapters[i].extractEvenNoValueExist( o );
				Collection tableSelectValue=multiValueTableSelectors[i].select( byIdColumnsSelectCdtn, temp );
				if( tableSelectValue.size()>0 ) {
					multiValueAdapters[i].combine( selectValue, tableSelectValue.toArray() );
				}
			}
		}
		
		return selectValue;
	}	
	
	public Object selectByUniqueColumns( Object o ) throws DBLevelException {
		// load main table values
		Object selectValue=mainTableSelector.selectByUniqueColumns( o );
		if( selectValue==null )
			return null;
		
		// load single value table values
		if( !Util.isEmpty( singleValueTableSelectors ) ) {
			for( int i=0; i<singleValueTableSelectors.length; i++ ) {
				Object temp=singleValueAdapters[i].extractEvenNoValueExist( o );
				Object tableSelectValue=singleValueTableSelectors[i].selectByIdColumns( temp );
				if( tableSelectValue!=null ) {
					singleValueAdapters[i].combine( selectValue, tableSelectValue );
				}
			}
		}
		
		// load multi value table values
		if( !Util.isEmpty( multiValueTableSelectors ) ) {
			for( int i=0; i<multiValueTableSelectors.length; i++ ) {
				Object temp=multiValueAdapters[i].extractEvenNoValueExist( o );
				Collection tableSelectValue=multiValueTableSelectors[i].select( byIdColumnsSelectCdtn, temp );
				if( tableSelectValue.size()>0 ) {
					multiValueAdapters[i].combine( selectValue, tableSelectValue.toArray() );
				}
			}
		}
		
		return selectValue;
	}	
	
	public Collection select( SelectCondition cdtn, Object hint ) throws DBLevelException {
		Map tempTableNameConnMap=null;
		ComplexTableDBHelper helper=new ComplexTableDBHelper();
		helper.setComplexTable( complexTable );
		try {
			helper.getConnections();
			helper.beginTransaction();
			
			// select main table
			Collection coll=mainTableSelector.select( helper.getMainTableConn(), cdtn, hint );
			
			if( coll.isEmpty() ) {
				return coll;
			}
			
			ComplexTableValueCombiner combiner=new ComplexTableValueCombiner( complexTable );
			combiner.setMainTableValues( coll );
			combiner.setSingleValueTableAdapters( singleValueAdapters );
			combiner.setMultiValueTableAdapters( multiValueAdapters );
			
			// save idColumns as a temp table
			Column[] idColumns=complexTable.getMainTable().getIdColumns();
			//Collection pks=combiner.getGeneralPks();
			tempTableNameConnMap=new HashMap();
			createTempTables( helper.getSingleValueTableConns(), idColumns, coll,
					tempTableNameConnMap );
			createTempTables( helper.getMultiValueTableConns(), idColumns, coll, 
					tempTableNameConnMap );
			
			// according to temp table, query singlevaluetables and multivaluetables
			Collection[] readSingleValueTables=readSingleValueTables( helper.getSingleValueTableConns(), tempTableNameConnMap );
			Collection[] readMultiValueTables=readMultiValueTables( helper.getMultiValueTableConns(), tempTableNameConnMap );
			
			combiner.setSingleValueTableValues( readSingleValueTables );
			combiner.setMultiValueTableValues( readMultiValueTables );
			
			return combiner.combine();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			// drop temp tables
			if( tempTableNameConnMap!=null ) {
				for( Iterator iter=tempTableNameConnMap.entrySet().iterator(); iter.hasNext(); ) {
					Entry entry=(Entry) iter.next();
					Connection conn=(Connection) entry.getKey();
					String tempTableName=(String) entry.getValue();
					
					try {
						DBUtil.exec( conn, "DROP TABLE " + tempTableName );
					} catch( SQLException e ) {
						logger.error( "", e );
					}
				}
			}
			
			helper.closeConnections();
		}
	}

	public Collection selectByPage( SelectCondition cdtn, 
			Object hint,
			int startIndex, int pageSize ) throws DBLevelException {
		Map tempTableNameConnMap=null;
		ComplexTableDBHelper helper=new ComplexTableDBHelper();
		helper.setComplexTable( complexTable );
		try {
			helper.getConnections();
			helper.beginTransaction();
			
			// query main table
			Collection coll=mainTableSelector.selectByPage( helper.getMainTableConn(), 
					cdtn, hint,
					startIndex, pageSize );
			
			if( coll.isEmpty() ) {
				return coll;
			}
			
			ComplexTableValueCombiner combiner=new ComplexTableValueCombiner( complexTable );
			combiner.setMainTableValues( coll );
			combiner.setSingleValueTableAdapters( singleValueAdapters );
			combiner.setMultiValueTableAdapters( multiValueAdapters );
			
			// save idColumns as a temp table
			Column[] idColumns=complexTable.getMainTable().getIdColumns();
			//Collection pks=combiner.getGeneralPks();
			tempTableNameConnMap=new HashMap();
			createTempTables( helper.getSingleValueTableConns(), idColumns, coll,
					tempTableNameConnMap );
			createTempTables( helper.getMultiValueTableConns(), idColumns, coll, 
					tempTableNameConnMap );
			
			// according temp table, query singlevalue and multivalue tables
			Collection[] readSingleValueTables=readSingleValueTables( helper.getSingleValueTableConns(), tempTableNameConnMap );
			Collection[] readMultiValueTables=readMultiValueTables( helper.getMultiValueTableConns(), tempTableNameConnMap );
			
			combiner.setSingleValueTableValues( readSingleValueTables );
			combiner.setMultiValueTableValues( readMultiValueTables );
			
			return combiner.combine();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			// drop temp tables
			if( tempTableNameConnMap!=null ) {
				for( Iterator iter=tempTableNameConnMap.entrySet().iterator(); iter.hasNext(); ) {
					Entry entry=(Entry) iter.next();
					Connection conn=(Connection) entry.getKey();
					String tempTableName=(String) entry.getValue();
					
					try {
						DBUtil.exec( conn, "DROP TABLE " + tempTableName );
					} catch( SQLException e ) {
						logger.error( "", e );
					}
				}
			}
			
			helper.closeConnections();
		}
	}
	
	private Collection[] readSingleValueTables( Connection[] conns,
			Map tempTableNameConnMap ) {
		Collection[] readResult=null;
		
		Table[] tables=complexTable.getSingleValueTables();
		TableSelectorImpl[] selectors=singleValueTableSelectors;
		
		if( !Util.isEmpty( tables ) ) {
			readResult=new Collection[tables.length];
			for( int i=0; i<tables.length; i++ ) {
				Connection conn=conns[i];
				String tempTableName=(String) tempTableNameConnMap.get( conn );
				
				InnerWhereElement whereEmt=new InnerWhereElement();
				whereEmt.setColumns( complexTable.getMainTable().getIdColumns() );
				whereEmt.setInnerTableColumnNames( complexTable.getMainTable().getIdColumnNames() );
				whereEmt.setTableName( tempTableName );
				
				SelectCondition cdtn=new SelectCondition();
				cdtn.setWhereElement( whereEmt );
				Collection coll=selectors[i].select( conn, cdtn, null );
				readResult[i]=coll;
			}
		}
		
		return readResult;
	}
	
	private Collection[] readMultiValueTables( Connection[] conns,
			Map tempTableNameConnMap ) {
		Collection[] readResult=null;
		Table[] tables=complexTable.getMultiValueTables();
		TableSelectorImpl[] selectors=multiValueTableSelectors;
		
		if( !Util.isEmpty( tables ) ) {
			readResult=new Collection[tables.length];
			for( int i=0; i<tables.length; i++ ) {
				Connection conn=conns[i];
				String tempTableName=(String) tempTableNameConnMap.get( conn );
				
				InnerWhereElement whereEmt=new InnerWhereElement();
				whereEmt.setColumns( complexTable.getMainTable().getIdColumns() );
				whereEmt.setInnerTableColumnNames( complexTable.getMainTable().getIdColumnNames() );
				whereEmt.setTableName( tempTableName );
				
				SelectCondition cdtn=new SelectCondition();
				cdtn.setWhereElement( whereEmt );
				Collection coll=selectors[i].select( conn, cdtn, null );
				readResult[i]=coll;
			}
		}
		
		return readResult;
	}
	
	private void createTempTables( Connection[] conns,
			Column[] idColumns, Collection tableValues, Map tempTableNameConnMap ) {
		if( !Util.isEmpty( conns ) ) {
			for( int i=0; i<conns.length; i++ ) {
				Connection conn=conns[i];
				if( tempTableNameConnMap.containsKey( conn ) ) {
					// already done
				} else {
					// create and insert value
					String table=createTempTable( conn, idColumns, tableValues );
					
					tempTableNameConnMap.put( conn, table );
				}
			}
		}
	}

	private String createTempTable( Connection conn, Column[] insertColumns,
			Collection tableValues ) {
		String tempTableName="Tmp_"+System.currentTimeMillis()+((int)(Math.random()*100));
		
		String[] columnNames=new String[insertColumns.length];
		String[] columnSqlTypes=new String[insertColumns.length];
		for( int i=0; i<insertColumns.length; i++ ) {
			Column column=insertColumns[i];
			columnNames[i]=column.getName();
			columnSqlTypes[i]=column.getType().getSqlType();
		}
		String createSql=DBUtil.createTableSql( tempTableName, columnNames, columnSqlTypes );
		
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement( createSql );
			pstmt.execute();
		} catch( SQLException e ) {
			logger.error( "", e );
			throw new DBLevelException( e );
		} finally {
			DBUtil.close( pstmt );
		}
		
		Table table=new Table();
		table.setColumnNames( columnNames );
		table.setColumns( insertColumns );
		table.setExceptIdColumnNames( columnNames );
		table.setExceptIdColumns( insertColumns );
		table.setName( tempTableName );
		
		TableSaverImpl saver=new TableSaverImpl();
		saver.setTable( table );
		saver.batchSave( conn, tableValues );
		
		return tempTableName;
	}

	public int selectCount() throws DBLevelException {
		return mainTableSelector.selectCount( null, null );
	}

	public int selectCount( SelectCondition cdtn, Object hint ) throws DBLevelException {
		return mainTableSelector.selectCount( cdtn, hint );
	}
}
