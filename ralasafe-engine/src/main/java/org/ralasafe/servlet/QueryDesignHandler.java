/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ralasafe.EntityExistException;
import org.ralasafe.db.ColumnView;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.DBView;
import org.ralasafe.db.TableView;
import org.ralasafe.db.sql.xml.BinaryExpression;
import org.ralasafe.db.sql.xml.Column;
import org.ralasafe.db.sql.xml.ContextValue;
import org.ralasafe.db.sql.xml.ExpressionGroup;
import org.ralasafe.db.sql.xml.ExpressionGroupTypeItem;
import org.ralasafe.db.sql.xml.GroupBy;
import org.ralasafe.db.sql.xml.InExpression;
import org.ralasafe.db.sql.xml.IsNotNullExpression;
import org.ralasafe.db.sql.xml.IsNullExpression;
import org.ralasafe.db.sql.xml.LeftOfIn;
import org.ralasafe.db.sql.xml.NotInExpression;
import org.ralasafe.db.sql.xml.Operand;
import org.ralasafe.db.sql.xml.OrderBy;
import org.ralasafe.db.sql.xml.Query;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.db.sql.xml.QueryTypeSequence;
import org.ralasafe.db.sql.xml.RightOfIn;
import org.ralasafe.db.sql.xml.Select;
import org.ralasafe.db.sql.xml.SimpleValue;
import org.ralasafe.db.sql.xml.Table;
import org.ralasafe.db.sql.xml.UserValue;
import org.ralasafe.db.sql.xml.Value;
import org.ralasafe.db.sql.xml.types.ColumnTypeOrderType;
import org.ralasafe.db.sql.xml.types.LinkerType;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class QueryDesignHandler {
	private QueryType query;
	//key/value=tableAlias/TableView
	private Map tableDefinitions;
	private Set undefinedTables;
	//key/value=tableAlias/undefinedColumns:Set<String>
	private Map undefinedTableColumns;
	private String[][] mappingClassPropertyAndTypes;
	private String defaultSchema;
	public QueryDesignHandler( QueryType query ) {
		this.query=query;
		
		// set default ds
		if( query.getDs()==null ) {
			String defaultAppDsName=DBPower.getDefaultAppDsName();
			query.setDs( defaultAppDsName );
		}
		
		defaultSchema=DBView.getDefaultSchema( query.getDs() );
		
		tableDefinitions=new HashMap();
		undefinedTables=new HashSet();
		undefinedTableColumns=new HashMap();
		
		getTableDefinition();
		
		getColumnDefinition();
		
		String mappingClass=query.getQueryTypeSequence().getSelect().getMappingClass();
		if( !StringUtil.isEmpty( mappingClass ) ) { 
			mappingClassPropertyAndTypes=Util.reflectJavaBean( mappingClass );
		}
	}

	public TableView getTableView( String tableAlias ) {
		return (TableView) tableDefinitions.get( tableAlias );
	}
	
	public List getCheckedColumns( String tableAlias ) {
		List colList=new LinkedList();
		
		Column[] columns=query.getQueryTypeSequence().getSelect().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];			
			String table=column.getTableAlias();
			
			if( table.equals( tableAlias ) ) {
				colList.add( column );
			}
		}
		return colList;
	}
	
	public Column getColumn( String tableAlias, String columnName ) {
		Column[] columns=query.getQueryTypeSequence().getSelect().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			String table=column.getTableAlias();
			String name=column.getName();
			
			if( table.equals( tableAlias ) && name.equals( columnName ) ) {
				return column;
			}
		}
		
		return null;
	}
	
	public Column getGroupColumn( String tableAlias, String columnName ) {
		Column[] columns=query.getQueryTypeSequence().getGroupBy().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			String table=column.getTableAlias();
			String name=column.getName();
			
			if( table.equals( tableAlias ) && name.equals( columnName ) ) {
				return column;
			}
		}
		
		return null;
	}
	
	public Column getOrderColumn( String tableAlias, String columnName ) {
		Column[] columns=query.getQueryTypeSequence().getOrderBy().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			String table=column.getTableAlias();
			String name=column.getName();
			
			if( table.equals( tableAlias ) && name.equals( columnName ) ) {
				return column;
			}
		}
		
		return null;
	}
	
	public QueryType getQuery() {
		return query;
	}

	private void getColumnDefinition() {
		Column[] columns=query.getQueryTypeSequence().getSelect().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			String tableAlias=column.getTableAlias();
			String name=column.getName();
			
			TableView tableView=(TableView) tableDefinitions.get( tableAlias );
			if( tableView!=null ) {
				Collection columnViews=tableView.getColumnViews();
				
				boolean undefined=true;
				for( Iterator iter=columnViews.iterator(); iter.hasNext(); ) {
					ColumnView colView=(ColumnView) iter.next();
					if( name.equals( colView.getName() ) ) {
						undefined=false;
						break;
					}
				}
				
				if( undefined ) {
					Set undefineColumns=(Set) undefinedTableColumns.get( tableAlias );
					if( undefineColumns==null ) {
						undefineColumns=new HashSet();
						undefinedTableColumns.put( tableAlias, undefineColumns );
					}
					
					undefineColumns.add( column );
				}
			}
		}
	}

	private void getTableDefinition() {
		String ds=query.getDs();
		
		Table[] tables=query.getQueryTypeSequence().getFrom().getTable();
		for( int i=0; i<tables.length; i++ ) {
			Table table=tables[i];
			String schema=table.getSchema();
			String tableName=table.getName();
			
			try {
				TableView tableView=DBView.getTable( ds, schema, tableName );
				
				tableDefinitions.put( table.getAlias(), tableView );
			} catch( DBLevelException e ) {
				// this table is undefined in database
				undefinedTables.add( table.getAlias() );
			}
			
			undefinedTableColumns.put( tableName, new HashSet() );
		}
	}

	public void changeDataSource( String name ) {
		query.setDs( name );
		defaultSchema=DBView.getDefaultSchema( name );
		
		// maybe throws NullPointException
		QueryTypeSequence queryTypeSequence=query.getQueryTypeSequence();
		try {
			queryTypeSequence.getFrom().removeAllTable();
		} catch( Exception e ) {}
		try {
			queryTypeSequence.getSelect().removeAllColumn();
		} catch( Exception e ) {}
		try {
			queryTypeSequence.getWhere().getExpressionGroup().removeAllExpressionGroupTypeItem();
		} catch( Exception e ) {}
		try {
			queryTypeSequence.getGroupBy().removeAllColumn();
		} catch( Exception e ) {}
		try {
			queryTypeSequence.getOrderBy().removeAllColumn();
		} catch( Exception e ) {}
	}
	
	public void setMappingClass( String mappingClass ) {
		// change javabean properties and types
		mappingClassPropertyAndTypes=Util.reflectJavaBean( mappingClass );
		
		// update query
		query.getQueryTypeSequence().getSelect().setMappingClass( mappingClass );
		
		// clear mapping
		Column[] columns=query.getQueryTypeSequence().getSelect().getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			column.setJavaType( null );
			column.setProperty( null );
		}
		
		// try auto column-javaProperty mapping
		autoORM();
	}
	
	private void autoORM() {
		String mappingClass=query.getQueryTypeSequence().getSelect().getMappingClass();
		
		if( !StringUtil.isEmpty( mappingClass ) ) {			
			// mapping
			Column[] columns=query.getQueryTypeSequence().getSelect().getColumn();
			for( int i=0; i<columns.length; i++ ) {
				Column column=columns[i];
				
				if( StringUtil.isEmpty( column.getProperty() ) ) {
					// this column not mapped yet
					String columnName=column.getName();
					columnName=columnName.replaceAll( "-", "" );
					columnName=columnName.replaceAll( "_", "" );
					
					for( int j=0; j<mappingClassPropertyAndTypes.length; j++ ) {
						String[] pAndT=mappingClassPropertyAndTypes[j];
						
						if( pAndT[0].equalsIgnoreCase( columnName ) ) {
							column.setJavaType( pAndT[1] );
							column.setProperty( pAndT[0] );
							
							//break loop
							j=mappingClassPropertyAndTypes.length; 
						}
					}					
				}
			}
		}
	}

	public String addTable( String schema, String name ) {
		TableView tableView=DBView.getTable( query.getDs(), schema, name );
		
		if( tableView==null ) {
			return "";
		}
		
		// give this table an alias
		String alias=getNewTableAlias();
		
		// cache definition for session
		tableDefinitions.put( alias, tableView );
		
		
		// update query-->from
		Table vTable=new Table();
		vTable.setAlias( alias );
		vTable.setName( name );
		vTable.setSchema( schema.equals(defaultSchema)?"":schema );
		query.getQueryTypeSequence().getFrom().addTable( vTable );
		
		return alias;
	}

	private String getNewTableAlias() {
		for( int i=0; ; i++ ) {
			String alias="t"+i;
			
			if( !tableDefinitions.containsKey( alias ) ) {
				return alias;
			} 
			// try again
		}
	}

	public void deleteTable( String alias ) {
		// remove definition from cache
		tableDefinitions.remove( alias );
		
		// delete table columns
		deleteTableColumns( alias );
		
		undefinedTables.remove( alias );
		undefinedTableColumns.remove( alias );
		
		// update query--->from
		Table[] tables=query.getQueryTypeSequence().getFrom().getTable();
		for( int i=0; i<tables.length; i++ ) {
			Table table=tables[i];
			
			if( table.getAlias().equals( alias ) ) {
				query.getQueryTypeSequence().getFrom().removeTableAt( i );
				
				return;
			}
		}
	}

	public void addTableColumns( String alias ) {
		Select select=query.getQueryTypeSequence().getSelect();
		Column[] columns=select.getColumn();
		Set columnNames=new HashSet();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			if( column.getTableAlias().equals( alias ) ) {
				columnNames.add( column.getName() );
			}			
		}
		
		// update query-->select
		TableView tableView=(TableView) tableDefinitions.get( alias );
		Collection columnViews=tableView.getColumnViews();
		for( Iterator iter=columnViews.iterator(); iter.hasNext(); ) {
			ColumnView colView=(ColumnView) iter.next();
			
			if( !columnNames.contains( colView.getName() ) ) {
				// column haven't been mapped, add it
				Column column=new Column();
				column.setName( colView.getName() );
				column.setSqlType( colView.getSqlType() );
				column.setTableAlias( alias );
				
				select.addColumn( column );
			}
		}
		
		// try auto column-javaProperty mapping
		autoORM();
	}

	public void deleteTableColumns( String alias ) {
		Select select=query.getQueryTypeSequence().getSelect();
		Column[] columns=select.getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			String tableAlias=column.getTableAlias();
			
			if( tableAlias.equals( alias ) ) {
				select.removeColumnAt( i );
				columns=select.getColumn();
				i--;
			}
		}
	}

	public void addColumn( String tableAlias, String columnName ) {
		Select select=query.getQueryTypeSequence().getSelect();
		TableView tableView=(TableView) tableDefinitions.get( tableAlias );
		
		Collection columnViews=tableView.getColumnViews();
		for( Iterator iter=columnViews.iterator(); iter.hasNext(); ) {
			ColumnView colView=(ColumnView) iter.next();
			
			if( columnName.equals( colView.getName() ) ) {
				Column column=new Column();
				column.setName( columnName );
				column.setSqlType( colView.getSqlType() );
				column.setTableAlias( tableAlias );
				
				select.addColumn( column );
			}
		}
		
		autoORM();
	}

	public void deleteColumn( String tableAlias, String columnName ) {
		Set set=(Set) undefinedTableColumns.get( tableAlias );
		if( set!=null ) {
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				Column column=(Column) iter.next();
				if( column.getName().equals( columnName ) ) {
					set.remove( column );
				}
			}
		}
		
		Select select=query.getQueryTypeSequence().getSelect();
		Column[] columns=select.getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( tableAlias.equals( column.getTableAlias() ) && 
					columnName.equals( column.getName() ) ) {
				select.removeColumnAt( i );
				
				return;
			}
		}		
	}

	public void changeColumnMapping( String tableAlias, String columnName,
			String function, 
			String javaProperty, String javaType,
			boolean readOnly ) {
		Select select=query.getQueryTypeSequence().getSelect();
		Column[] columns=select.getColumn();
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( tableAlias.equals( column.getTableAlias() ) && 
					columnName.equals( column.getName() ) ) {
				column.setFunction( function );
				column.setProperty( javaProperty );
				column.setJavaType( javaType );
				column.setReadOnly( readOnly );
				return;
			}
		}
	}
	
	public Set getUndefinedTables() {
		return undefinedTables;
	}
	
	public String[][] getMappingClassPropertyAndTypes() {
		return mappingClassPropertyAndTypes;
	}

	public Set getUndefinedColumns( String alias ) {
		return (Set) undefinedTableColumns.get( alias );
	}

	public void moveGroupColumn( String direct, String tableAlias,
			String columnName ) {
		GroupBy groupBy=query.getQueryTypeSequence().getGroupBy();
		Column[] columns=groupBy.getColumn();
		int moveIndex=0;
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( column.getTableAlias().equals( tableAlias ) && 
					column.getName().equals( columnName ) ) {
				moveIndex=i;
				i=columns.length;//break loop
			}
		}
		
		// change column with it
		int changeIndex=-1;
		if( "top".equals( direct ) ) {
			if( moveIndex!=0 ) {
				changeIndex=0;
			}
		} else if( "up".equals( direct ) ) {
			if( moveIndex!=0 ) { 
				changeIndex=moveIndex-1;
			}
		} else if( "down".equals( direct ) ) {
			if( moveIndex!=(columns.length-1) ) {
				changeIndex=moveIndex+1;
			}
		} else if( "bottom".equals( direct ) ) {
			if( moveIndex!=(columns.length-1) ) {
				changeIndex=columns.length-1;
			}
		}
		
		if( changeIndex!=-1 ) {
			Column moveCol=columns[moveIndex];
			
			if( "top".equals( direct ) ) {
				groupBy.removeColumnAt( moveIndex );
				groupBy.addColumn( 0, moveCol );
			} else if( "bottom".equals( direct ) ) {
				groupBy.removeColumnAt( moveIndex );
				groupBy.addColumn( groupBy.getColumnCount(), moveCol );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {	
				Column changeCol=columns[changeIndex];
				
				groupBy.setColumn( changeIndex, moveCol );
				groupBy.setColumn( moveIndex, changeCol );
			}
		}
	}
	
	public void editGroupColumn( int index, String tableAlias,
			String columnName ) {
		GroupBy groupBy=query.getQueryTypeSequence().getGroupBy();
		
		Column column=groupBy.getColumn( index );
		column.setTableAlias( tableAlias );
		column.setName( columnName );
	}
	
	public void addGroupColumn( String tableAlias,
			String columnName ) {
		GroupBy groupBy=query.getQueryTypeSequence().getGroupBy();
		
		Column column=new Column();
		column.setTableAlias( tableAlias );
		column.setName( columnName );
		groupBy.addColumn( column );
	}
	
	public void deleteGroupColumn( String tableAlias, String columnName ) {
		GroupBy groupBy=query.getQueryTypeSequence().getGroupBy();
		Column[] columns=groupBy.getColumn();
		
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( tableAlias.equals( column.getTableAlias() ) && 
					columnName.equals( column.getName() ) ) {
				groupBy.removeColumnAt( i );
				
				return;
			}
		}
	}
	
	public void moveOrderColumn( String direct, String tableAlias,
			String columnName ) {
		OrderBy orderBy=query.getQueryTypeSequence().getOrderBy();
		Column[] columns=orderBy.getColumn();
		int moveIndex=0;
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( column.getTableAlias().equals( tableAlias ) && 
					column.getName().equals( columnName ) ) {
				moveIndex=i;
				i=columns.length;//break loop
			}
		}
		
		// change column with it
		int changeIndex=-1;
		if( "top".equals( direct ) ) {
			if( moveIndex!=0 ) {
				changeIndex=0;
			}
		} else if( "up".equals( direct ) ) {
			if( moveIndex!=0 ) { 
				changeIndex=moveIndex-1;
			}
		} else if( "down".equals( direct ) ) {
			if( moveIndex!=(columns.length-1) ) {
				changeIndex=moveIndex+1;
			}
		} else if( "bottom".equals( direct ) ) {
			if( moveIndex!=(columns.length-1) ) {
				changeIndex=columns.length-1;
			}
		}
		
		if( changeIndex!=-1 ) {
			Column moveCol=columns[moveIndex];
			
			if( "top".equals( direct ) ) {
				orderBy.removeColumnAt( moveIndex );
				orderBy.addColumn( 0, moveCol );
			} else if( "bottom".equals( direct ) ) {
				orderBy.removeColumnAt( moveIndex );
				orderBy.addColumn( orderBy.getColumnCount(), moveCol );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {				
				Column changeCol=columns[changeIndex];
			
				orderBy.setColumn( changeIndex, moveCol );
				orderBy.setColumn( moveIndex, changeCol );
			}
		}
	}

	public void editOrderColumn( int index, String tableAlias,
			String columnName, String orderType ) {
		OrderBy orderBy=query.getQueryTypeSequence().getOrderBy();
		
		Column column=orderBy.getColumn( index );
		column.setTableAlias( tableAlias );
		column.setName( columnName );
		column.setOrder( ColumnTypeOrderType.valueOf( orderType ) );
	}
	
	public void addOrderColumn( String tableAlias,
			String columnName, String orderType ) {
		OrderBy orderBy=query.getQueryTypeSequence().getOrderBy();
		
		Column column=new Column();
		column.setTableAlias( tableAlias );
		column.setName( columnName );
		column.setOrder( ColumnTypeOrderType.valueOf( orderType ) );
		orderBy.addColumn( column );
	}
	
	public void deleteOrderColumn( String tableAlias, String columnName ) {
		OrderBy orderBy=query.getQueryTypeSequence().getOrderBy();
		Column[] columns=orderBy.getColumn();
		
		for( int i=0; i<columns.length; i++ ) {
			Column column=columns[i];
			
			if( tableAlias.equals( column.getTableAlias() ) && 
					columnName.equals( column.getName() ) ) {
				orderBy.removeColumnAt( i );
				
				return;
			}
		}
	}

	public String getWhere() {
		StringBuffer buff=new StringBuffer();
		ExpressionGroup exprGroup=query.getQueryTypeSequence().getWhere().getExpressionGroup();

//		return "[                                                     \r\n"
//		+" 	{ id:1, pId:0, name:\"Mobile\", open:true},        \r\n"
//		+" 	{ id:11, pId:1, name:\"Nokia\"},                   \r\n"
//		+" 	{ id:111, pId:11, name:\"C6(Music)\"},             \r\n"
//		+" 	{ id:112, pId:11, name:\"X6(GPS)\"},               \r\n"
//		+" 	{ id:113, pId:11, name:\"5230(SB)\"},              \r\n"
//		+" 	{ id:114, pId:11, name:\"N97mini\"},               \r\n"
//		+" 	{ id:12, pId:1, name:\"Samsung\"}                 \r\n"
//		+" ]"; 
		
		buff.append( "[" );
		
		buff.append( "{id:'0',pId:'-1', iconSkin: 'exprGroup', name:'Root expression group (" )
			.append( exprGroup.getLinker() )
			.append( ")', open:true}" );
		
		ExpressionGroupTypeItem[] items=exprGroup.getExpressionGroupTypeItem();
		if( items!=null ) {
			for( int i=0; i<items.length; i++ ) {
				ExpressionGroupTypeItem item=items[i];
				
				Object value=item.getChoiceValue();
				if( value instanceof BinaryExpression ) {
					print( (BinaryExpression) value, buff, "0", i );
				} else if( value instanceof ExpressionGroup ) {
					print( (ExpressionGroup) value, buff, "0", i );
				} else if( value instanceof InExpression ) {
					print( (InExpression) value, buff, "0", i );
				} else if( value instanceof IsNullExpression ) {
					print( (IsNullExpression) value, buff, "0", i );
				} else if( value instanceof IsNotNullExpression ) {
					print( (IsNotNullExpression) value, buff, "0", i );
				} else if( value instanceof NotInExpression ) {
					print( (NotInExpression) value, buff, "0", i );
				} 
			}
		}
		
		buff.append( "]" );
		return buff.toString();
	}

	private void print( NotInExpression value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getLeft().getLeftOfIn()) )
			.append( " NOT IN " )
			.append( format( value.getRight().getRightOfIn() ) )
			.append( "'}" );
	}

	private void print( IsNotNullExpression value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getColumn() ) )
			.append( " NOT NULL" )
			.append( "'}" );
	}

	private String format( Column column ) {
		return column.getTableAlias()+"."+column.getName();
	}

	private void print( IsNullExpression value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getColumn() ) )
			.append( " NULL" )
			.append( "'}" );
	}

	private void print( InExpression value, StringBuffer buff, 
			String pid,	int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getLeft().getLeftOfIn()) )
			.append( " IN " )
			.append( format( value.getRight().getRightOfIn() ) )
			.append( "'}" );
	}

	private Object format( RightOfIn r ) {
		Query query2=r.getQuery();
		Value[] values=r.getValue();
		
		if( query2!=null ) {
			return format( query2 );
		} else if( values!=null ) {
			return format( values );
		} else {
			return "Unsupport type";
		}
	}

	private String format( Value[] values ) {
		StringBuffer buff=new StringBuffer();
		for( int i=0; i<values.length; i++ ) {
			if( i!=0 ) {
				buff.append( "," );
			}
			
			Value value=values[i];
			buff.append( format( value ) );
		}
		
		return buff.toString();
	}

	private String format( LeftOfIn l ) {
		Column[] columns=l.getColumn();
		Query query2=l.getQuery();
		
		if( columns!=null ) {
			return format( columns );
		} else if( query2!=null ) {
			return format( query2 );
		} else {
			return "Unsupport type";
		}
	}

	private String format( Column[] columns ) {
		StringBuffer buff=new StringBuffer();
		for( int i=0; i<columns.length; i++ ) {
			if( i!=0 ) {
				buff.append( "," );
			}
			
			Column column=columns[i];
			buff.append( format( column ) );
		}
		
		return buff.toString();
	}

	private void print( BinaryExpression value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format(value.getOperand1().getOperand()) )
			.append( value.getOperator().getSimpleOperator() )
			.append( format(value.getOperand2().getOperand()) )
			.append( "'}" );
	}

	private Object format( Operand operand ) {
		Column column=operand.getColumn();
		Query query2=operand.getQuery();
		Value value=operand.getValue();
		
		if( column!=null ) {
			return format( column );
		} else if( query2!=null ) {
			return format( query2 );
		} else if( value!=null ) {
			return format( value );
		}
		return null;
	}

	private Object format( Value value ) {
		ContextValue contextValue=value.getContextValue();
		SimpleValue simpleValue=value.getSimpleValue();
		UserValue userValue=value.getUserValue();
		
		if( contextValue!=null ) {
			return "Context value: " + contextValue.getKey();
		} else if( simpleValue!=null ) {
			return "Simple value: "  + simpleValue.getContent();
		} else if( userValue!=null ) {
			return "User value: " + userValue.getKey();
		} else {
			return "Unsupport type";
		}
	}

	private String format( Query query2 ) {
		return "Query: " + query2.getName();
	}

	private void print( ExpressionGroup exprGroup, StringBuffer buff,
			String pid, int xPosition ) {
		String nodeId=pid+"-"+xPosition;
		// print this node
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'exprGroup',pId:'"+pid+"', name:'Expression group (" )
		.append( exprGroup.getLinker() )
		.append( ")', open:true}" );
		
		// print children items
		ExpressionGroupTypeItem[] items=exprGroup.getExpressionGroupTypeItem();
		if( items!=null ) {
			for( int i=0; i<items.length; i++ ) {
				ExpressionGroupTypeItem item=items[i];
				
				Object value=item.getChoiceValue();
				if( value instanceof BinaryExpression ) {
					print( (BinaryExpression) value, buff, nodeId, i );
				} else if( value instanceof ExpressionGroup ) {
					print( (ExpressionGroup) value, buff, nodeId, i );
				} else if( value instanceof InExpression ) {
					print( (InExpression) value, buff, nodeId, i );
				} else if( value instanceof IsNullExpression ) {
					print( (IsNullExpression) value, buff, nodeId, i );
				} else if( value instanceof IsNotNullExpression ) {
					print( (IsNotNullExpression) value, buff, nodeId, i );
				} else if( value instanceof NotInExpression ) {
					print( (NotInExpression) value, buff, nodeId, i );
				} 
			}
		}
	}

	public Object getWhereExprItem( String nodeId ) {
		ExpressionGroup group=query.getQueryTypeSequence().getWhere().getExpressionGroup();
		
		if( "0".equals( nodeId ) ) {
			return group;
		} else {
			return getExprItem( group, nodeId );
		}
	}

	private Object getExprItem( ExpressionGroup group,
			String nodeId ) {
		int indexOf=nodeId.indexOf( "-" );
		
		String childNodeId=nodeId.substring( indexOf+1 );
		indexOf=childNodeId.indexOf( "-" );
		
		if( indexOf>0 ) {
			int xPosition=Integer.parseInt( childNodeId.substring( 0, indexOf ) );
			
			ExpressionGroupTypeItem item=group.getExpressionGroupTypeItem( xPosition );
			
			return getExprItem( item.getExpressionGroup(), childNodeId );
		} else {
			int xPosition=Integer.parseInt( childNodeId );
			
			return group.getExpressionGroupTypeItem( xPosition );
		}
	}
	
	public void addWhereChildExprGroup( String nodeId, String type ) {
		Object obj=getWhereExprItem( nodeId );
		ExpressionGroup parent=toGroup( obj );
		ExpressionGroup newGrp=new ExpressionGroup();
		newGrp.setLinker( LinkerType.valueOf( type ) );
		ExpressionGroupTypeItem newItem=new ExpressionGroupTypeItem();
		newItem.setExpressionGroup( newGrp );
		
		parent.addExpressionGroupTypeItem( newItem );
	}

	public void editWhereExprGroup( String nodeId, String type ) {
		Object obj=getWhereExprItem( nodeId );
		ExpressionGroup group=toGroup( obj );
		group.setLinker( LinkerType.valueOf( type ) );
	}
	
	private ExpressionGroup toGroup( Object obj ) {
		if( obj instanceof ExpressionGroup ) {
			ExpressionGroup grp=(ExpressionGroup) obj;
			return grp;
		} else {
			ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) obj;
			return item.getExpressionGroup();
		}
	}
	
	public void deleteWhereExpr( String nodeId ) {
		int index=nodeId.lastIndexOf( "-" );
		String pId=nodeId.substring( 0,index );
		int xPosition=Integer.parseInt( nodeId.substring( index+1 ) );
		
		Object obj=getWhereExprItem( pId );
		ExpressionGroup parent=toGroup( obj );
		
		parent.removeExpressionGroupTypeItemAt( xPosition );
	}

	public void addBinaryExpression( BinaryExpression expr, String pId ) {
		Object item=getWhereExprItem( pId );
		ExpressionGroup group=toGroup( item );
		
		ExpressionGroupTypeItem newItem=new ExpressionGroupTypeItem();
		newItem.setBinaryExpression( expr );
		group.addExpressionGroupTypeItem( newItem );
	}

	public void editBinaryExpression( BinaryExpression expr, String nodeId ) {
		Object obj=getWhereExprItem( nodeId );
		ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) obj;
		
		item.setBinaryExpression( expr );
	}

	public void editNullExpression( Column column, String operator, String nodeId ) {
		Object obj=getWhereExprItem( nodeId );
		ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) obj;
		
		if( "NULL".equals( operator ) ) {
			IsNullExpression expr=new IsNullExpression();
			expr.setColumn( column );
			item.setIsNotNullExpression( null );
			item.setIsNullExpression( expr );
		} else {
			IsNotNullExpression expr=new IsNotNullExpression();
			expr.setColumn( column );
			item.setIsNullExpression( null );
			item.setIsNotNullExpression( expr );
		}
	}

	public void addNullExpression( Column column, String operator, String pId ) {
		Object item=getWhereExprItem( pId );
		ExpressionGroup group=toGroup( item );
		
		ExpressionGroupTypeItem newItem=new ExpressionGroupTypeItem();
		if( "NULL".equals( operator ) ) {
			IsNullExpression expr=new IsNullExpression();
			expr.setColumn( column );
			newItem.setIsNullExpression( expr );
		} else {
			IsNotNullExpression expr=new IsNotNullExpression();
			expr.setColumn( column );
			newItem.setIsNotNullExpression( expr );
		}
		group.addExpressionGroupTypeItem( newItem );
	}

	public void save( int id, QueryManager queryManager ) throws EntityExistException {
		queryManager.updateQuery( id, (Query) query );
	}

	public String getManagePage() {
		return "./queryMng.rls";
	}
}
