/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;

/**
 * Mapping a javabean to query result. This adapter adapters resultset to javabean.
 * A query may use where conditions, this adapter adapters javabean to preparedstatement.
 */
public class JavaBeanColumnAdapter implements ColumnAdapter {
	private static Log log=LogFactory.getLog( JavaBeanColumnAdapter.class );
			
	private String origField;
	private String field;
	private String getterStr;
	private String setterStr;
	private String className;
	
	private Method setter;
	private Method getter;
	
	private ResultSetReader rsReader;
	private PstmtSetter pstmtSetter;
	
	public JavaBeanColumnAdapter(String field, String className) {
		this.origField = field;
		this.field = field.substring(0, 1).toUpperCase() + field.substring(1);
		this.getterStr = "get" + this.field;
		this.setterStr = "set" + this.field;
		this.className = className;
		
		rsReader=ResultSetReaderUtil.getReader( className );
		pstmtSetter=PstmtSetterUtil.getSetter( className );
	}
	
	private void initSetterMethod(Object o) throws Exception {
		Class valueClass;
		Class clas = o.getClass();
		
		if (className.equals("int"))
			valueClass = Integer.TYPE;
		else if (className.equals("float"))
			valueClass = Float.TYPE;
		else if (className.equals("long"))
			valueClass = Long.TYPE;
		else if (className.equals("double"))
			valueClass = Double.TYPE;
		else if (className.equals("boolean"))
			valueClass = Boolean.TYPE;
		else
			valueClass = Class.forName(className);
		
		setter = clas.getMethod(setterStr, new Class[] { valueClass });
	}
	
	private void initGetterMethod(Object o) throws Exception {
		Class clas = o.getClass();
		
		// try getField method first
		try {
			getter = clas.getMethod(getterStr, new Class[] {});
		} catch( NoSuchMethodException e ) {
			if( "boolean".equals( className ) 
					|| "java.lang.Boolean".equals( className ) ) {
				// for boolean try, try isField method
				this.getterStr = "is" + this.field;
				getter = clas.getMethod(getterStr, new Class[] {});
			} else {
				throw e;
			}
		}
	}
	
	public void readResultSet(ResultSet rs, String columnName, Object o)
			throws SQLException {
		if( setter==null ) {
			try {
				initSetterMethod( o );
			} catch( Exception e ) {
				e.printStackTrace();
				throw new RalasafeException( e );
			}
		}
		
		try {
			Object readValue=rsReader.reader( rs, columnName );
			setter.invoke(o, new Object[] { readValue });
		} catch (Exception e) {
			e.printStackTrace();
			throw new RalasafeException(e);
		}
	}

	public void readResultSet(ResultSet rs, int columnIndex, Object o)
			throws SQLException {
		if( setter==null ) {
			try {
				initSetterMethod( o );
			} catch( Exception e ) {
				log.error( "", e );
				throw new RalasafeException( e );
			}
		}
		
		try {
			Object readValue=rsReader.reader( rs, columnIndex );
			setter.invoke(o, new Object[] { readValue });
		} catch (Exception e) {
			log.error( "", e );
			throw new RalasafeException(e);
		}
	}

	public void setPreparedStatement(PreparedStatement pstmt, int paramIndex,
			Object o) throws SQLException {
		try {
			Object setValue=null;
			if (o instanceof MapStorgeObject) {
				MapStorgeObject mso = (MapStorgeObject) o;
				setValue = mso.get(this.origField);
			} else {
				if( getter==null ) {
					initGetterMethod(o);
				}
				
				setValue=getter.invoke(o, new Object[] {});
			}
			
			pstmtSetter.set( pstmt, paramIndex, setValue );
		} catch( Exception e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		}
	}

	public Object extractFieldValue(Object o) {
		try {
			if( getter==null ) {
				initGetterMethod(o);
			}
			
			return getter.invoke(o, new Object[] {});
		} catch (Exception e) {
			log.error( "", e );
			return null;
		}
	}
}
