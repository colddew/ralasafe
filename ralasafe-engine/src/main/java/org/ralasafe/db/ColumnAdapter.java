/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Adapter JavaBean and ResultSet/PreparedStatement
 * 
 * @author back
 * 
 */
public interface ColumnAdapter {
	public void setPreparedStatement(PreparedStatement pstmt, int paramIndex,
			Object o) throws SQLException;

	public void readResultSet(ResultSet rs, int columnIndex, Object o)
			throws SQLException;

	public void readResultSet(ResultSet rs, String columnName, Object o)
			throws SQLException;

	public Object extractFieldValue(Object o);
}
