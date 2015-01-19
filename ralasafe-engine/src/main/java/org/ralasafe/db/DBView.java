/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.StringUtil;

/**
 * Reflect schema's table and view infos.
 * The table or view is omited which name starts with '$'. 
 * We think it's a system table. 
 * 
 * @author back
 * 
 */
public class DBView {
	private static final String[] tableType = new String[] { "TABLE" };
	private static final String[] viewType = new String[] { "VIEW" };
	private static final Log log=LogFactory.getLog( DBView.class );
	
	public static String getDefaultSchema(String dsName) {
		Connection conn = DBPower.getConnection(dsName);
		try {
			return DBUtil.getDefaultSchema(conn);
		} catch (SQLException e) {
			log.error( "", e );
			return "";
		} finally {
			DBUtil.close(conn);
		}
	}

	public static TableView getTable(String dsName, String tableName) {
		Connection conn = null;

		try {
			conn = DBPower.getConnection(dsName);

			return getTable(conn, dsName, null, tableName);
		} catch (SQLException e) {
			log.error( "Failed to get definition of " + "table/view '"
					+ tableName, e );
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(conn);
		}
	}
	
	public static TableView getTable(String dsName, String schema, String tableName) {
		Connection conn = null;

		try {
			conn = DBPower.getConnection(dsName);

			return getTable(conn, dsName, schema, tableName);
		} catch (SQLException e) {
			log.error( "Failed to get definition of " + "table/view '"
					+ schema + "." + tableName, e );
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

	private static TableView getTable(Connection conn, String dsName,
			String schema, String tableName) throws SQLException {
		String mySchema = "";
		if (!StringUtil.isEmpty(schema)) {
			mySchema = schema + ".";
		}

		if( log.isDebugEnabled() ) {
			log.debug( "Get table/view definition: dsName=" + dsName 
					+ ", table/view Name=" + mySchema+tableName );
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet primaryKeys = null;

		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery("select * from " + mySchema + tableName
					+ " where 1=2");
			ResultSetMetaData metaData = rs.getMetaData();

			TableView table = new TableView();
			table.setSchema(schema);
			table.setName(tableName);

			DatabaseMetaData metaData2 = conn.getMetaData();
			String databaseProductName = DBUtil.getDatabaseProductName(conn);

			if (databaseProductName == DBUtil.MYSQL) {
				primaryKeys = metaData2.getPrimaryKeys(schema, null, tableName);
			} else {
				primaryKeys = metaData2.getPrimaryKeys(null, null, tableName);
			}

			Map pkColumnViewMap = new HashMap();
			while (primaryKeys.next()) {
				pkColumnViewMap.put(primaryKeys.getString("COLUMN_NAME"), null);
			}

			List columnList = new ArrayList(metaData.getColumnCount());
			for (int i = 1, columnCount = metaData.getColumnCount(); i <= columnCount; i++) {
				ColumnView column = new ColumnView();
				String columnName = metaData.getColumnName(i);
				column.setName(columnName);
				String sqlType = metaData.getColumnTypeName(i);

				if (sqlType.equalsIgnoreCase("blob")
						|| sqlType.equalsIgnoreCase("clob")
						|| sqlType.equalsIgnoreCase("text")) {
					// DO NOTHING
				} else {
					int precision = metaData.getPrecision(i);
					int scale = metaData.getScale(i);

					if (precision != 0) {
						if (scale == 0) {
							sqlType = sqlType + "(" + precision + ")";
						} else {
							sqlType = sqlType + "(" + precision + "," + scale
									+ ")";
						}
					}
				}
				column.setSqlType(sqlType);
				columnList.add(column);

				// it's a primary key?
				if (pkColumnViewMap.containsKey(columnName)) {
					pkColumnViewMap.put(columnName, column);
				}
			}

			table.setColumnViews(columnList);

			// sometimes, oracle jdbc driver returns pk info is redundance, 
			// actually the column does exist at all.  Clear them.
			clearInvalidPK(pkColumnViewMap);
			
			if (pkColumnViewMap.size() > 0) {
				table.setPkColumnViews(pkColumnViewMap.values());
			}
			return table;
		} finally {
			DBUtil.close(primaryKeys);
			DBUtil.close(rs);
			DBUtil.close(stmt);
		}
	}

	private static void clearInvalidPK(Map pkColumnViewMap) {
		Iterator itr = pkColumnViewMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			if (entry.getValue() == null) {
				itr.remove();
			}
		}
	}

	public static String[] getSchemas(String dsName) {
		DataSource ds = DBPower.getDataSource(dsName);
		if (ds.isShowAllSchemas()) {
			return getAllSchemasFromDB(dsName);
		} else {
			return ds.getSchemas();
		}
	}

	public static String[] getAllSchemasFromDB(String dsName) {
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DBPower.getConnection(dsName);

			DatabaseMetaData metaData = conn.getMetaData();

			String databaseProductName = DBUtil.getDatabaseProductName(conn);
			if (databaseProductName == DBUtil.MYSQL
					|| databaseProductName == DBUtil.SQLSERVER) {
				rs = metaData.getCatalogs();
			} else {
				rs = metaData.getSchemas();
			}

			List result = new LinkedList();
			while (rs.next()) {
				String name = rs.getString(1);
				result.add(name);
			}

			String[] names = new String[result.size()];
			Iterator itr = result.iterator();
			for (int i = 0; i < names.length; i++) {
				names[i] = (String) itr.next();
			}
			return names;
		} catch (SQLException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(conn);
		}
	}

	public static String[] getTableNames(String dsName, String schema) {
		return getObjectNames(dsName, schema, tableType);
	}

	private static String[] getObjectNames(String dsName, String schema,
			String[] objectTypes) {
		Connection conn = null;
		ResultSet tables = null;

		try {
			conn = DBPower.getConnection(dsName);

			DatabaseMetaData metaData = conn.getMetaData();

			String databaseProductName = DBUtil.getDatabaseProductName(conn);
			if (databaseProductName == DBUtil.MYSQL
					|| databaseProductName == DBUtil.SQLSERVER) {
				tables = metaData.getTables(schema, null, null, objectTypes);
			} else if (databaseProductName == DBUtil.ORACLE) {
				tables = metaData.getTables(null, null, null, objectTypes);
			} else {
				tables = metaData.getTables(null, schema, null, objectTypes);
			}

			List result = new LinkedList();
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");

				if (databaseProductName == DBUtil.ORACLE) {
					String s = tables.getString("TABLE_SCHEM");
					if (s.equalsIgnoreCase(schema)) {
						result.add(tableName);
					}
				} else if (databaseProductName == DBUtil.SQLSERVER) {
					String s = tables.getString("TABLE_SCHEM");
					tableName = s + "." + tableName;
					result.add(tableName);
				} else {
					result.add(tableName);
				}
			}

			String[] names = new String[result.size()];
			Iterator itr = result.iterator();
			for (int i = 0; i < names.length; i++) {
				names[i] = (String) itr.next();
			}
			return names;
		} catch (SQLException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(tables);
			DBUtil.close(conn);
		}
	}

	public static Collection getTables(String dsName, String schema) {
		String[] tableNames = getTableNames(dsName, schema);
		return getTables(dsName, schema, tableNames);
	}

	public static Collection getTables(String dsName, String schema,
			String[] tableNames) {
		Connection conn = DBPower.getConnection(dsName);
		try {
			List tables = new ArrayList();
			for (int i = 0; i < tableNames.length; i++) {
				// omit system tables like $sys_
				if (tableNames[i].indexOf("$") >= 0) {
					continue;
				}

				try {
					TableView table = getTable(conn, dsName, schema,
							tableNames[i]);
					tables.add(table);
				} catch (SQLException e) {
					log.error( "Failed to get definition in schema '"
							+ schema + "'." + "table/view:" + tableNames[i], e );
				}
			}
			return tables;
		} finally {
			DBUtil.close(conn);
		}
	}

	public static String[] getViewNames(String dsName, String schema) {
		return getObjectNames(dsName, schema, viewType);
	}

	public static Collection getViewTables(String dsName, String schema,
			String[] viewNames) {
		return getTables(dsName, schema, viewNames);
	}

	public static Collection getViewTables(String dsName, String schema) {
		String[] viewNames = getViewNames(dsName, schema);
		return getTables(dsName, schema, viewNames);
	}
}
