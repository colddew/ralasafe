/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.ralasafe.util.DBUtil;
import org.ralasafe.util.StringUtil;

/**
 * WARNING!!!!
 * We will deprecate it later.
 */
public class WhereElementUtil {
	public static String toSql( SelectCondition cdtn ) {
		if( cdtn==null )
			return "";
		
		StringBuffer buff=new StringBuffer();
		
		// translate where part		
		WhereElement whereElement=cdtn.getWhereElement();
		if( whereElement!=null ) 
			buff.append( " WHERE " );
		append( buff, whereElement );
		
		// translate order part
		append( buff, cdtn.getOrderPart() );
		
		// translate group part
		append( buff, cdtn.getGroupPart() );
		
		return buff.toString();
	}

	public static String toSql( WhereElement emt ) {
		if( emt==null )
			return "";
		
		StringBuffer buff=new StringBuffer();
		buff.append( " WHERE " );
		append( buff, emt );
		
		return buff.toString();
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

	private static void append( StringBuffer buff, WhereElement whereEmt ) {
		if( whereEmt==null )
			return;
		
		if( whereEmt instanceof FieldWhereElement ) {
			FieldWhereElement fEmt=(FieldWhereElement) whereEmt;
			buff.append( fEmt.getColumn().getName() );
			
			Comparator cptr=fEmt.getCompartor();
			if( cptr.equals( SingleValueComparator.EQUAL ) ) {
				buff.append( "=" );
			} else if( cptr.equals( SingleValueComparator.GREATER ) ) {
				buff.append( ">" );
			} else if( cptr.equals( SingleValueComparator.LESS ) ) {
				buff.append( "<" );
			} else if( cptr.equals( SingleValueComparator.NOT_EQUAL ) ) {
				buff.append( "<>" );
			} else if( cptr.equals( SingleValueComparator.NOT_GREATER ) ) {
				buff.append( "<=" );
			} else if( cptr.equals( SingleValueComparator.NOT_LESS ) ) {
				buff.append( ">=" );
			} else if( cptr.equals( SingleValueComparator.LIKE ) ) {
				if( fEmt.isContextValue() ) {
					buff.append( " like " );
				} else {
					buff.append( " like " );
				}
			} else if( cptr.equals( SingleValueComparator.RIGHT_LIKE ) ) {
				if( fEmt.isContextValue() ) {
					buff.append( " like " );
				} else {
					buff.append( " like " );
				}
			} else if( cptr.equals( SingleValueComparator.LEFT_LIKE ) ) {
				if( fEmt.isContextValue() ) {
					buff.append( " like " );
				} else {
					buff.append( " like " );
				}
			}
			
			if( fEmt.isContextValue() ) {
				buff.append( "?" );
			} else {
				// formated value
				//buff.append( "'" );
				buff.append( fEmt.getValue() );
				//buff.append( "'" );
			}
			
//			if( cptr.equals( SingleValueComparator.LEFT_LIKE ) ) {
//				if( fEmt.isContextValue() ) {
//					buff.append( "%'" );
//				}
//			} else if( cptr.equals( SingleValueComparator.LIKE ) ) {
//				if( fEmt.isContextValue() ) {
//					buff.append( "%'" );
//				}
//			} else if( cptr.equals( SingleValueComparator.RIGHT_LIKE ) ) {
//				if( fEmt.isContextValue() ) {
//					buff.append( "'" );
//				}
//			}
		} else if( whereEmt instanceof ComplexWhereElement ) {
			ComplexWhereElement cEmt=(ComplexWhereElement) whereEmt;
			append( buff, cEmt.getFirstPart() );
			LWhereElement[] lEmts=cEmt.getLinkedParts();
			if( lEmts!=null ) {
				for( int i=0; i<lEmts.length; i++ ) {
					LWhereElement lEmt=lEmts[i];
					String linkerType=lEmt.getLinkType();
					if( LWhereElement.OR_LINK_TYPE
							.equalsIgnoreCase( linkerType ) ) {
						buff.append( " OR (" );
						append( buff, lEmt.getWhereElement() );
						buff.append( ") " );
					} else {
						buff.append( " AND (" );
						append( buff, lEmt.getWhereElement() );
						buff.append( ") " );
					}
				}
			}
		} else if( whereEmt instanceof InnerWhereElement ) {
			InnerWhereElement emt=(InnerWhereElement) whereEmt;
			String tableName=emt.getTableName();
			Column[] columns=emt.getColumns();
			String[] innerTableColumnNames=emt.getInnerTableColumnNames();
			Collection values=emt.getValues();
			
			buff.append( " (" );
			for( int i=0; i<columns.length; i++ ) {
				if( i>0 ) {
					buff.append( "," );
				}
				buff.append( columns[i].getName() );
			}
			buff.append( ") in (" );
			
			if( tableName==null ) {
				if( columns.length>1 ) {
					boolean notFirstOne=false;
					for(Iterator iter=values.iterator(); iter.hasNext(); ) {
						if( notFirstOne ) {
							buff.append( "," );
						} else {
							notFirstOne=true;
						}
						buff.append( "(" );
						Object[] rowValues=(Object[]) iter.next();
						for( int i=0; i<rowValues.length; i++ ) {
							if( i>0 ) {
								buff.append( "," );
							}
							buff.append( rowValues[i] );
						}
						buff.append( ")" );
					}
				} else {
					boolean notFirstOne=false;
					for(Iterator iter=values.iterator(); iter.hasNext(); ) {
						if( notFirstOne ) {
							buff.append( "," );
						} else {
							notFirstOne=true;
						}
						
						buff.append( iter.next() );
					}
				}
			} else {
				buff.append( DBUtil.selectSql( tableName, innerTableColumnNames ) );
			}
			
			buff.append( ")" );
		} else if( whereEmt instanceof PstmtInWhereElement ) {
			PstmtInWhereElement pstmtIn=(PstmtInWhereElement) whereEmt;
			String[] columnNames=pstmtIn.getColumnNames();
			StringUtil.append( buff, columnNames );
			
			buff.append( " in (");
			String inSql=pstmtIn.getInSql();
			buff.append( inSql );
			buff.append( ") ");	
		}
	}
	
	public static void extractColumns( WhereElement emt, List selectByColumns,
			Object mainHint, List hints ) {
		if( emt==null )
			return;
		
		if( emt instanceof FieldWhereElement ) {
			FieldWhereElement fEmt=(FieldWhereElement) emt;
			
			if( fEmt.isContextValue() ) {
				selectByColumns.add( fEmt.getColumn() );
				hints.add( mainHint );
			}
		} else if( emt instanceof ComplexWhereElement ) {
			ComplexWhereElement cEmt=(ComplexWhereElement) emt;
			extractColumns( cEmt.getFirstPart(), selectByColumns, mainHint, hints );
			
			
			LWhereElement[] linkedParts=cEmt.getLinkedParts();
			if( linkedParts!=null ) {
				for( int i=0; i<linkedParts.length; i++ ) {
					extractColumns( linkedParts[i].getWhereElement(), selectByColumns, mainHint, hints );
				}
			}
		} else if( emt instanceof InnerWhereElement ) {
			
		} else if( emt instanceof PstmtInWhereElement ) {
			PstmtInWhereElement pstmtIn=(PstmtInWhereElement) emt;
			Column[] pstmtColumns=pstmtIn.getPstmtColumns();
			
			for( int i=0; i<pstmtColumns.length; i++ ) {
				Column column=pstmtColumns[i];
				selectByColumns.add( column );
				hints.add( pstmtIn.getHint() );
			}
		}
	}
}
