/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.Column;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.Table;

public class DBUtil {
	private static Log log=LogFactory.getLog( DBUtil.class );
			
	public static final String DB2 = "DB2";
	public static final String ORACLE = "ORACLE";
	public static final String MYSQL = "MYSQL";
	public static final String SQLSERVER = "SQL SERVER";
	public static final String OTHER_DATABASE = "OTHER";

	public static boolean supportsLimit(Connection conn) {
		String dataBase = getDatabaseProductName(conn);
		if (dataBase.equals(DB2) || dataBase.equals(ORACLE)
				|| dataBase.equals(MYSQL)) {
			return true;
		} else {
			return false;
		}
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				conn = null;
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				stmt = null;
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				rs = null;
			}
		}
	}

	public static void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}

	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		close(rs);
		close(stmt);
		close(conn);
	}

	public static String insertSql(String tableName, String[] columnNames) {
		StringBuffer buff = new StringBuffer();
		buff.append("INSERT INTO ");
		buff.append(tableName);
		buff.append(" (");
		StringUtil.append(buff, columnNames);
		buff.append(") VALUES(");
		StringUtil.append(buff, columnNames, false, "?", ",");
		buff.append(")");
		return buff.toString();
	}

	public static String selectSql(String tableName, String[] columnNames) {
		StringBuffer buff = new StringBuffer();
		buff.append("SELECT ");
		StringUtil.append(buff, columnNames);
		buff.append(" FROM ");
		buff.append(tableName);
		return buff.toString();
	}

	public static String selectSql(Table table) {
		String tableAlias = " t" + table.getId();
		StringBuffer buff = new StringBuffer();
		buff.append("SELECT ");
		buff.append(columnsString(table.getColumns(), tableAlias));
		buff.append(" FROM ");
		
		buff.append(table.getName() + tableAlias);
		return buff.toString();
	}

	private static StringBuffer columnsString(Column[] columns,
			String tableAlias) {
		StringBuffer buff = new StringBuffer();
		buff.append(columnString(columns[0], tableAlias));
		for (int i = 1; i < columns.length; i++) {
			buff.append(",").append(columnString(columns[i], tableAlias));
		}
		return buff;
	}

	private static String columnString(Column column, String tableAlias) {
		if (column.getFunction() == null || column.getFunction().equals(""))
			return tableAlias + "." + column.getName();
		else
			return column.getFunction() + "(" + tableAlias + "."
					+ column.getName() + ")";
	}

	public static String updateSql(String tableName, String[] idColumnNames,
			String[] exceptIdColumnNames) {
		StringBuffer buff = new StringBuffer();
		buff.append("UPDATE ");
		buff.append(tableName);
		buff.append(" SET ");
		StringUtil.append(buff, exceptIdColumnNames, true, "=?", ",");
		buff.append(" WHERE ");
		StringUtil.append(buff, idColumnNames, true, "=?", " AND ");
		return buff.toString();
	}

	public static String deleteSql(String name, String[] idColumnNames) {
		StringBuffer buff = new StringBuffer();
		buff.append("DELETE FROM ");
		buff.append(name);
		buff.append(" WHERE ");
		StringUtil.append(buff, idColumnNames, true, "=?", " AND ");
		return buff.toString();
	}

	public static String createTableSql(String name, String[] columnNames,
			String[] columnSqlTypes) {
		StringBuffer buff = new StringBuffer();
		buff.append("CREATE TABLE ");
		buff.append(name.toLowerCase());
		buff.append("(");
		for (int i = 0; i < columnNames.length; i++) {
			if (i > 0) {
				buff.append(",");
			}
			buff.append(columnNames[i]);
			buff.append(" ");
			buff.append(columnSqlTypes[i]);
		}
		buff.append(")");
		return buff.toString();
	}

	public static void exec(Connection conn, String sql) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.execute();
		} finally {
			close(pstmt);
		}
	}

	public static int getMax(Table table, String columnName)
			throws SQLException {
		Connection conn = DBPower.getConnection(table.getId());
		Statement stmt = null;
		ResultSet rs = null;
		try {
			int max = 0;
			String sql = "select MAX( " + columnName + " ) from "
					+ table.getName();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				max = rs.getInt(1);
			} else {
				max = 0;
			}
			return max;
		} finally {
			DBUtil.close(rs, stmt, conn);
		}
	}

	// Sequence table name(save sequence value in table)
	private static final String RALASAFE_SEQUNCE = "ralasafe_sequence";

	/**
	 * Get next value of certain column of table. For example: user table's id column.
	 * 
	 * @param table
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	public static int getSequenceNextVal(Table table, String columnName)
			throws SQLException {		
		Integer currentValue=readSequenceCurrentValFromDatabase(table, columnName);
		int nextValue=currentValue.intValue()+1;
		synchronizeIntoDatabase( table, columnName, nextValue );
		
		return nextValue;
	}

	private static void synchronizeIntoDatabase(Table table, String columnName,
			int synValue) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DBPower.getConnection(table.getId());

			pstmt = conn.prepareStatement("update " + RALASAFE_SEQUNCE
					+ " set currentValue=? where name=?");
			String name = table.getName() + "_" + columnName;
			pstmt.setInt(1, synValue);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
		} finally {
			DBUtil.close(pstmt, conn);
		}
	}

	private static Integer readSequenceCurrentValFromDatabase(Table table,
			String columnName) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBPower.getConnection(table.getId());
			Integer currentValue = null;
			pstmt = conn.prepareStatement("select currentValue from "
					+ RALASAFE_SEQUNCE + " where name=?");
			String name = table.getName() + "_" + columnName;
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				currentValue = new Integer(rs.getInt(1));
			}

			if (currentValue == null) {
				// there's no record in sequence table, then create a record
				int max = getMax(table, columnName);
				
				if( max<0 ) {
					max=0;
				}
				
				DBUtil.exec(conn, "insert into " + RALASAFE_SEQUNCE
						+ "(currentValue,name) values(" + max + ",'"
						+ name + "')");
				return new Integer(max);
			}

			return currentValue;
		} finally {
			DBUtil.close(rs, pstmt, conn);
		}
	}

	public static String roleTableCreateSql(String appName) {
		String sqlRole = " CREATE TABLE "
				+ appName.toLowerCase()
				+ "_role ( id integer NOT NULL,	name varchar(100) NOT NULL UNIQUE, "
				+ "description varchar(500), PRIMARY KEY(id) ) ";
		return sqlRole;
	}

	public static String rolePrivilegeTableCreateSql(String appName) {
		String sqlRolePrivilege = " CREATE TABLE "
				+ appName.toLowerCase()
				+ "_roleprivilege (	roleid integer NOT NULL,	privilegeid integer NOT NULL, "
				+ "PRIMARY KEY(roleid, privilegeid), FOREIGN KEY(roleid) REFERENCES "
				+ appName + "_role (id), FOREIGN KEY(privilegeid) REFERENCES "
				+ appName + "_privilege (id)) ";
		return sqlRolePrivilege;
	}

	public static String privilegeTableCreateSql(String appName) {
		String sqlPrivilege = " CREATE TABLE "
				+ appName.toLowerCase()
				+ "_privilege ( id integer NOT NULL, pid integer, "
				+ "description varchar(500), name varchar(100) NOT NULL UNIQUE, "
				+ "isLeaf integer, display integer, decisionPolicyCombAlg integer, "
				+ "queryPolicyCombAlg integer, type integer, constantName varchar(40), "
				+ "url varchar(100), target varchar(20), orderNum integer, PRIMARY KEY(id) ) ";
		return sqlPrivilege;
	}

	public static String userRoleTableCreateSql(String appName,
			String userTypeName, String idColumnName, String idColumnType) {
		String tableName = appName + "_" + userTypeName + "_userrole";
		String sqlUserRole = "CREATE TABLE "
				+ tableName.toLowerCase()
				+ " ( userid "
				+ idColumnType
				+ " NOT NULL, roleid integer NOT NULL, PRIMARY KEY(userid, roleid), "
				+ "FOREIGN KEY(roleid) REFERENCES " + appName + "_role (id)) ";
		return sqlUserRole;
	}

	public static String roleTableDropSql(String appName) {
		String sqlRole = " DROP TABLE " + appName + "_role";
		return sqlRole;
	}

	public static String rolePrivilegeTableDropSql(String appName) {
		String sqlRolePrivilege = " DROP TABLE " + appName + "_roleprivilege";
		return sqlRolePrivilege;
	}

	public static String privilegeTableDropSql(String appName) {
		String sqlPrivilege = " DROP TABLE " + appName + "_privilege";
		return sqlPrivilege;
	}

	public static String userRoleTableDropSql(String appName,
			String userTypeName) {
		String tableName = appName + "_" + userTypeName + "_userrole";
		String sqlUserRole = "DROP TABLE " + tableName;
		return sqlUserRole;
	}

	public static String tableQueryCreateSql(String appName) {
		return "CREATE TABLE " + appName.toLowerCase()
				+ "_query ( id int NOT NULL,name varchar(100) NOT NULL UNIQUE, "
				+ "description varchar(500)," + "installDate date, "
				+ "fileName varchar(40),pid int, isLeaf int,"
				+ "PRIMARY KEY(id) ) ";
	}

	public static String tableQueryDropSql(String appName) {
		return "DROP TABLE " + appName + "_query";
	}

	public static String tableUserCategoryCreateSql(String appName) {
		return "CREATE TABLE "
				+ appName.toLowerCase()
				+ "_usercategory ( id int NOT NULL, name varchar(100) NOT NULL UNIQUE, "
				+ "description varchar(500), " + "installDate date, "
				+ "fileName varchar(40),pid int, isLeaf int,"
				+ "PRIMARY KEY(id) ) ";
	}

	public static String tableUserCategoryDropSql(String appName) {
		return "DROP TABLE " + appName + "_usercategory";
	}

	public static String tableDecisionEntitlementCreateSql(String appName) {
		return "create table " + appName.toLowerCase() + "_decision_entitlement("
				+ "id int NOT NULL," + "privilegeId int,"
				+ "userCategoryId int," + "businessDataId int,"
				+ "effect varchar(100)," + "denyReason varchar(1000),"
				+ "PRIMARY KEY(id) )";
	}

	public static String tableDecisionEntitlementDropSql(String appName) {
		return "DROP TABLE " + appName + "_decision_entitlement";
	}

	public static String tableQueryEntitlementCreateSql(String appName) {
		return "create table " + appName.toLowerCase() + "_query_entitlement("
				+ "id int NOT NULL," + "privilegeId int,"
				+ "userCategoryId int," + "queryId int,"
				+ "description varchar(500)," + "PRIMARY KEY(id) )";
	}

	public static String tableQueryEntitlementDropSql(String appName) {
		return "DROP TABLE " + appName + "_query_entitlement";
	}

	public static String tableBusinessDataCreateSql(String appName) {
		return "CREATE TABLE "
				+ appName.toLowerCase()
				+ "_businessdata ( id int NOT NULL,name varchar(100) NOT NULL UNIQUE, "
				+ "description varchar(500)," + "installDate date, "
				+ "fileName varchar(40),pid int, isLeaf int,	"
				+ "PRIMARY KEY(id) ) ";
	}

	public static String tableBusinessDataDropSql(String appName) {
		return "DROP TABLE " + appName + "_businessdata";
	}
	
	public static String tableBackUpCreateSql(String appName) {
		return "CREATE TABLE "
				+ appName.toLowerCase()
				+ "_backup ( id int NOT NULL,createTime timestamp, "
				+ "description varchar(500),content blob,"
				+ "PRIMARY KEY(id) ) ";
	}

	public static String tableBackupDropSql(String appName) {
		return "DROP TABLE " + appName + "_backup";
	}
	
	public static String getLimitString(Connection conn, String sql, int first,
			int max) {
		String dataBase = getDatabaseProductName(conn);
		String result=sql;
		if (dataBase.equals(DB2)) {
			result=getDB2LimitString(sql, first, max);
		} else if (dataBase.equals(ORACLE)) {
			result=getOracleLimitString(sql, first, max);
		} else if (dataBase.equals(MYSQL)) {
			result=getMySQLLimitString(sql, first, max);
		}
		
		if( log.isDebugEnabled() ) {
			log.debug( "\n"+result );
		}
		
		return result;
	}

	private static String getMySQLLimitString(String sql, int first, int max) {
		return new StringBuffer(sql.length() + 20).append(sql).append(
				" limit " + first + ", " + max).toString();
	}

	private static String getOracleLimitString(String sql, int first, int max) {

		sql = sql.trim();
		boolean isForUpdate = false;
		if (sql.toLowerCase().endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

		pagingSelect
				.append("select * from ( select row_.*, rownum rownum_ from ( ");

		pagingSelect.append(sql);

		// hibernate style
		// pagingSelect.append(" ) row_ ) where rownum_ <= " + (first + max)
		// + " and rownum_ > " + first);

		// better style
		pagingSelect.append(" ) row_ where rownum <= " + (first + max)
				+ ") where " + " rownum_ >= " + (first + 1));

		if (isForUpdate) {
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}

	private static String getDB2LimitString(String sql, int first, int max) {

		int startOfSelect = sql.toLowerCase().indexOf("select");

		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100)
				.append(sql.substring(0, startOfSelect)) // add the comment
				.append("select * from ( select ") // nest the main query in an
				// outer select
				.append(getRowNumber(sql)); // add the rownnumber bit into the
		// outer query select list

		if (hasDistinct(sql)) {
			pagingSelect.append(" row_.* from ( ") // add another (inner) nested
					// select
					.append(sql.substring(startOfSelect)) // add the main query
					.append(" ) as row_"); // close off the inner nested select
		} else {
			pagingSelect.append(sql.substring(startOfSelect + 6)); // add the
			// main
			// query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		// add the restriction to the outer select
		pagingSelect.append("between " + (first + 1) + " and " + (first + max));

		return pagingSelect.toString();
	}

	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct") >= 0;
	}

	/**
	 * Render the <tt>rownumber() over ( .... ) as rownumber_,</tt> bit, that
	 * goes in the select list
	 */
	private static String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50)
				.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if (orderByIndex > 0 && !hasDistinct(sql)) {
			rownumber.append(sql.substring(orderByIndex));
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}

	public static String getDatabaseProductName(Connection conn) {
		String dataBase;
		try {
			dataBase = conn.getMetaData().getDatabaseProductName();
			dataBase = dataBase.toUpperCase();
		} catch (SQLException e) {
			throw new RalasafeException(e);
		}

		if (dataBase.indexOf(DB2) >= 0) {
			return DB2;
		} else if (dataBase.indexOf(ORACLE) >= 0) {
			return ORACLE;
		} else if (dataBase.indexOf(MYSQL) >= 0) {
			return MYSQL;
		} else if (dataBase.indexOf(SQLSERVER) >= 0) {
			return SQLSERVER;
		} else {
			return OTHER_DATABASE;
		}
	}

	public static String getDefaultSchema(Connection conn) throws SQLException {
		String productName = getDatabaseProductName(conn);
		if (productName.equals(MYSQL) || productName.equals(SQLSERVER)) {
			return conn.getCatalog();
		} else {
			DatabaseMetaData metaData = conn.getMetaData();
			return metaData.getUserName();
		}
	}

	public static void setCommitMode(Connection conn, boolean autoCommit) {
		if (conn != null) {
			try {
				conn.setAutoCommit(autoCommit);
			} catch (SQLException e) {
			}
		}
	}

	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
			}
		}
	}
	
}
