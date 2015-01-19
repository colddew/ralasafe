/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public interface PstmtSetter {
	public void set(PreparedStatement pstmt, int pstmtIndex, Object o)
			throws SQLException;

	class JavaUtilDatePstmtSetter implements PstmtSetter {

		public void set(PreparedStatement pstmt, int pstmtIndex, Object o)
				throws SQLException {
			if (o == null) {
				pstmt.setNull(pstmtIndex, java.sql.Types.DATE);
			} else {
				Date d = (Date) o;
				pstmt.setTimestamp(pstmtIndex, new java.sql.Timestamp(d
						.getTime()));
			}
		}
	};

	class ObjectPstmtSetter implements PstmtSetter {

		public void set(PreparedStatement pstmt, int pstmtIndex, Object o)
				throws SQLException {
			pstmt.setObject(pstmtIndex, o);
		}
	};
}
