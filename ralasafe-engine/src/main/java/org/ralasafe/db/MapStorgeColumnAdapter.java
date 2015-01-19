/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapStorgeColumnAdapter implements ColumnAdapter {
	private String key;
	private String className;

	private ResultSetReader rsReader;
//	private PstmtSetter pstmtSetter;
	
	public MapStorgeColumnAdapter(String mapKey) {
		this.key = mapKey;
		rsReader=new ResultSetReader.ObjectReader();
//		pstmtSetter=new PstmtSetter.ObjectPstmtSetter();
	}
	
	public MapStorgeColumnAdapter(String mapKey, String className) {
		this.key = mapKey;
		this.className=className;
		
		rsReader=ResultSetReaderUtil.getReader( className );
//		pstmtSetter=PstmtSetterUtil.getSetter( className );O
	}

	public void readResultSet(ResultSet rs, String columnName, Object o)
			throws SQLException {
		Object readValue = rsReader.reader( rs, columnName );
		
//		if (StringUtil.isEmpty(className)) {
//			readValue = rs.getObject(columnName);
//		else if (className.equals("float")
//				|| className.equals("java.lang.Float"))
//			readValue = new Float(rs.getFloat(columnName));
//		else if (className.equals("double")
//				|| className.equals("java.lang.Double"))
//			readValue = new Double(rs.getDouble(columnName));
//		else if (className.equals("int")
//				|| className.equals("java.lang.Integer"))
//			readValue = new Integer(rs.getInt(columnName));
//		else if (className.equals("long")
//				|| className.equals("java.lang.Long"))
//			readValue = new Long(rs.getLong(columnName));
//		else if (className.equals("boolean")
//				||className.equals( "java.lang.Boolean" ))
//			readValue = new Boolean(rs.getBoolean(columnName));
//		else if (className.equals("java.util.Date"))
//			readValue = rs.getDate(columnName);
//		else
//			readValue = rs.getObject(columnName);
//		
		MapStorgeObject mso = (MapStorgeObject) o;
		mso.put(key, readValue);

	}

	public void readResultSet(ResultSet rs, int columnIndex, Object o)
			throws SQLException {
		Object readValue = rsReader.reader( rs, columnIndex );
//		
//		if (StringUtil.isEmpty(className))
//			readValue = rs.getObject(columnIndex);
//		else if (className.equals("float")
//				|| className.equals("java.lang.Float"))
//			readValue = new Float(rs.getFloat(columnIndex));
//		else if (className.equals("double")
//				|| className.equals("java.lang.Double"))
//			readValue = new Double(rs.getDouble(columnIndex));
//		else if (className.equals("int")
//				|| className.equals("java.lang.Integer"))
//			readValue = new Integer(rs.getInt(columnIndex));
//		else if (className.equals("long")
//				|| className.equals("java.lang.Long"))
//			readValue = new Long(rs.getLong(columnIndex));
//		else if (className.equals("boolean")
//				||className.equals( "java.lang.Boolean" ))
//			readValue = new Boolean(rs.getBoolean(columnIndex));
//		else if (className.equals("java.util.Date"))
//			readValue = rs.getDate(columnIndex);
//		else
//			readValue = rs.getObject(columnIndex);
		
		MapStorgeObject mso = (MapStorgeObject) o;
		mso.put(key, readValue);
	}

	public void setPreparedStatement(PreparedStatement pstmt, int paramIndex,
			Object o) throws SQLException {
		MapStorgeObject mso = (MapStorgeObject) o;
		Object setValue = mso.get(key);
		
		//pstmtSetter.set( pstmt, paramIndex, setValue );
		if(setValue instanceof java.util.Date){
			java.util.Date date=(java.util.Date)setValue;
			setValue= new java.sql.Date(date.getTime());
		}
		pstmt.setObject(paramIndex, setValue);
	}

	public Object extractFieldValue(Object o) {
		MapStorgeObject mso = (MapStorgeObject) o;
		return mso.get(key);
	}

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key=key;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName( String className ) {
		this.className=className;
	}
}
