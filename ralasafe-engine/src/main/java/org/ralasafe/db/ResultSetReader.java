/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetReader {
	public Object reader(ResultSet rs, String columnName) throws SQLException;

	public Object reader(ResultSet rs, int columnIndex) throws SQLException;

	class FloatReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Float(rs.getFloat(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Float(rs.getFloat(columnName));
		}
	};

	class DoubleReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Double(rs.getDouble(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Double(rs.getDouble(columnName));
		}
	};

	class IntegerReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Integer(rs.getInt(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Integer(rs.getInt(columnName));
		}
	};

	class LongReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Long(rs.getLong(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Long(rs.getLong(columnName));
		}
	};

	class BooleanReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Boolean(rs.getBoolean(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Boolean(rs.getBoolean(columnName));
		}
	};

	class ShortReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return new Short(rs.getShort(columnIndex));
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return new Short(rs.getShort(columnName));
		}
	};
	
	class JavaUtilDateReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return rs.getTimestamp(columnIndex);
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return rs.getTimestamp(columnName);
		}
	};

	class ObjectReader implements ResultSetReader {

		public Object reader(ResultSet rs, int columnIndex) throws SQLException {
			return rs.getObject(columnIndex);
		}

		public Object reader(ResultSet rs, String columnName)
				throws SQLException {
			return rs.getObject(columnName);
		}
	};
}
