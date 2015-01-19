/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import org.ralasafe.EntityExistException;
import org.ralasafe.db.sql.xml.Column;
import org.ralasafe.db.sql.xml.ContextValue;
import org.ralasafe.db.sql.xml.Parameter;
import org.ralasafe.db.sql.xml.Query;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.db.sql.xml.RawSQL;
import org.ralasafe.db.sql.xml.Select;
import org.ralasafe.db.sql.xml.UserValue;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public class QueryRawHandler {

	private final QueryType query;
	private String[][] mappingClassPropertyAndTypes;
	
	public QueryRawHandler( QueryType xmlQuery ) {
		this.query=xmlQuery;
		
		String mappingClass=query.getRawSQL().getSelect().getMappingClass();
		if( !StringUtil.isEmpty( mappingClass ) ) { 
			mappingClassPropertyAndTypes=Util.reflectJavaBean( mappingClass );
		}
	}

	public QueryType getQuery() {
		return query;
	}
	
	public String[] format( Parameter param ) {
		Object value=param.getChoiceValue();
		if( value instanceof ContextValue ) {
			return new String[]{"Context value", param.getContextValue().getKey()};
		} /*else if( param.getSimpleValue()!=null ) {
			SimpleValue value=param.getSimpleValue();
			return new String[]{"simpleValue", param.getSimpleValue().getContent()};
		} */else if( value instanceof UserValue ) {
			return new String[]{"User value", param.getUserValue().getKey()};
		}
		
		return null;
	}

	public Parameter getParameter( int index ) {
		return query.getRawSQL().getParameter( index );
	}

	public void addParameter( String type, String key ) {
		Parameter param=new Parameter();
		
		if( "userValue".equals( type ) ) {
			UserValue value=new UserValue();
			value.setKey( key );
			
			param.setContextValue( null );
			param.setUserValue( value );
		} else if( "contextValue".equals( type ) ) {
			ContextValue value=new ContextValue();
			value.setKey( key );
			
			param.setUserValue( null );
			param.setContextValue( value );
		}
		
		query.getRawSQL().addParameter( param );
	}

	public void updateParameter( int index, String type, String key ) {
		Parameter param=query.getRawSQL().getParameter( index );
		
		if( "userValue".equals( type ) ) {
			UserValue value=new UserValue();
			value.setKey( key );
			
			param.setContextValue( null );
			param.setUserValue( value );			
		} else if( "contextValue".equals( type ) ) {
			ContextValue value=new ContextValue();
			value.setKey( key );
			
			param.setUserValue( null );
			param.setContextValue( value );
		}
	}

	public void deleteParameter( int index ) {
		query.getRawSQL().removeParameterAt( index );
	}
	
	public void moveParameter( String direct, int index ) {
		RawSQL rawSql=query.getRawSQL();
		Parameter[] parameters=rawSql.getParameter();
		int moveIndex=index;
		
		// change parameter with it
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
			if( moveIndex!=(parameters.length-1) ) {
				changeIndex=moveIndex+1;
			}
		} else if( "bottom".equals( direct ) ) {
			if( moveIndex!=(parameters.length-1) ) {
				changeIndex=parameters.length-1;
			}
		}
		
		if( changeIndex!=-1 ) {
			Parameter moveCol=parameters[moveIndex];
			
			if( "top".equals( direct ) ) {
				rawSql.removeParameterAt( moveIndex );
				rawSql.addParameter( 0, moveCol );
			} else if( "bottom".equals( direct ) ) {
				rawSql.removeParameterAt( moveIndex );
				rawSql.addParameter( rawSql.getParameterCount(), moveCol );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {	
				Parameter changeCol=parameters[changeIndex];
				
				rawSql.setParameter( changeIndex, moveCol );
				rawSql.setParameter( moveIndex, changeCol );
			}
		}
	}

	public Column getColumn( int index ) {
		return query.getRawSQL().getSelect().getColumn( index );
	}

	public void addProperty( String columnName, String javaProp, String javaType, boolean readOnly ) {
		Column column=new Column();
		column.setName( columnName );
		column.setTableAlias( "" );
		column.setProperty( javaProp );
		column.setJavaType( javaType );
		column.setReadOnly( readOnly );
		
		query.getRawSQL().getSelect().addColumn( column );
	}

	public void updateProperty( int index, String columnName, String javaProp, String javaType,
			boolean readOnly ) {
		Column column=query.getRawSQL().getSelect().getColumn( index );
		column.setName( columnName );
		column.setTableAlias( "" );
		column.setProperty( javaProp );
		column.setJavaType( javaType );
		column.setReadOnly( readOnly );
	}

	public void deleteProperty( int index ) {
		query.getRawSQL().getSelect().removeColumnAt( index );
	}
	
	public void moveProperty( String direct, int index ) {
		Select select=query.getRawSQL().getSelect();
		Column[] columns=select.getColumn();
		int moveIndex=index;
		
		// change parameter with it
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
				select.removeColumnAt( moveIndex );
				select.addColumn( 0, moveCol );
			} else if( "bottom".equals( direct ) ) {
				select.removeColumnAt( moveIndex );
				select.addColumn( select.getColumnCount(), moveCol );
			} else if( "up".equals( direct ) || "down".equals( direct ) ) {	
				Column changeCol=columns[changeIndex];
				
				select.setColumn( changeIndex, moveCol );
				select.setColumn( moveIndex, changeCol );
			}
		}
	}
	
	public void setMappingClass( String mappingClass ) {
		// update query
		query.getRawSQL().getSelect().setMappingClass( mappingClass );
		mappingClassPropertyAndTypes=Util.reflectJavaBean( mappingClass );
		
		// clear mapping
//		Column[] columns=query.getRawSQL().getSelect().getColumn();
//		for( int i=0; i<columns.length; i++ ) {
//			Column column=columns[i];
//			column.setJavaType( null );
//			column.setProperty( null );
//		}
	}

	public String[][] getMappingClassPropertyAndTypes() {
		return mappingClassPropertyAndTypes;
	}

	public void setRawSql( String rawSql ) {
		query.getRawSQL().setContent( rawSql );
	}
	
	public void save( int id, QueryManager queryManager ) throws EntityExistException {
		queryManager.updateQuery( id, (Query) query );
	}

	public String getManagePage() {
		return "./queryMng.rls";
	}
}
