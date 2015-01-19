/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.SystemConstant;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.DBPower;
import org.ralasafe.entitle.QueryResult;
import org.ralasafe.entitle.QueryTestResult;
import org.ralasafe.user.User;
import org.ralasafe.util.DBUtil;

public class Query implements Operand, LeftOfIn, RightOfIn, SQLElement {
	private static Log log=LogFactory.getLog( Query.class );
	
	public static final String SQL_TYPE = "sql";
	public static final String STORED_PROCEDURE_TYPE = "storedProcedure";
	private String name;
	/**
	 * datasource name
	 */
	private String ds;
	private boolean isRawSQL;
	private String type = SQL_TYPE;
	private Select select = new Select();
	private From from = new From();
	private Where where = new Where();
	private GroupBy groupBy = new GroupBy();
	private OrderBy orderBy = new OrderBy();
	private RawSQL rawSQL = new RawSQL();
	private StoredProcedure storedProcedure = new StoredProcedure();
	private String SQL;

	/**
	 * The values would be set to PreparedStatement
	 */
	private ArrayList values = new ArrayList();

	public boolean isRawSQL() {
		return isRawSQL;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StoredProcedure getStoredProcedure() {
		return storedProcedure;
	}

	public void setStoredProcedure(StoredProcedure storedProcedure) {
		this.storedProcedure = storedProcedure;
	}

	public void setIsRawSQL(boolean isRawSQL) {
		this.isRawSQL = isRawSQL;
	}

	public RawSQL getRawSQL() {
		return rawSQL;
	}

	public void setRawSQL(RawSQL rawSQL) {
		this.rawSQL = rawSQL;
	}

	public String reloadSQL() {
		this.SQL = null;
		return toSQL();
	}

	public String toSQL() {
		if (this.SQL == null) {
			StringBuffer buf = new StringBuffer();
			if (isRawSQL) {
				buf.append(rawSQL.getContent());
				buf.append( where.toSQL() );
			} else {
				buf.append(select.toSQL()).append(from.toSQL());
				buf.append(where.toSQL());
				buf.append(groupBy.toSQL());
				buf.append(orderBy.toSQL());
			}
			this.SQL = format(buf.toString());
		}
		
		if( log.isDebugEnabled() ) {
			log.debug( "\n"+this.SQL );
		}
		return this.SQL;
	}

	/**
	 * Add line breaker at propriety positions, to format for web view
	 * @param script
	 * @return
	 */
	private String format(String script) {
		int maxInLine = 80;
		int lengthOfLine = 0;
		int indexOfTheLatestSpaceOrComma = 0;
		char[] dst = new char[script.length()];
		script.getChars(0, script.length(), dst, 0);
		StringBuffer buf = new StringBuffer();
		char replacedChar = ' ';

		for (int i = 0; i < dst.length; i++, lengthOfLine++) {
			char curChar = dst[i];
			buf.append(curChar);

			if (curChar == '\n') {
				// reset counter
				lengthOfLine = 0;
				continue;
			}

			if (curChar == ' ' || curChar == '\t' || curChar == ',') {
				replacedChar = curChar;
				indexOfTheLatestSpaceOrComma = buf.length() - 1;
			}

			if (lengthOfLine == maxInLine) {
				// break line
				if (replacedChar == ',') {
					buf.replace(indexOfTheLatestSpaceOrComma,
							indexOfTheLatestSpaceOrComma + 1, ",\n ");
				} else {
					buf.replace(indexOfTheLatestSpaceOrComma,
							indexOfTheLatestSpaceOrComma + 1, "\n ");
				}
				lengthOfLine = (buf.length() - indexOfTheLatestSpaceOrComma + 1);
			}
		}
		return buf.toString();
	}

	public void setValues(ArrayList values) {
		this.values = values;
	}

	public ArrayList getValues() {
		return values;
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public Where getWhere() {
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public GroupBy getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * Execute this query. It' often be called by UserCategory and BusinessDate script.
	 * 
	 * @param returnCollection     return a collection?
	 * @return   if returnCollection, return the collection< first column's value of query records>
	 *           else, return the first record's first column value
	 */
	public Object executeQueryRef(User user, Map context,
			boolean returnCollection) throws DBLevelException {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(ds);
			return executeQueryRef(conn, user, context, returnCollection);
		} finally {
			DBUtil.close(conn);
		}
	}

	private Object executeQueryRef(Connection conn, User user, Map context,
			boolean returnCollection) {
		QueryResult queryResult=execute( conn, user, context );
		Collection dataColl=queryResult.getData();
		int size=dataColl.size();
		
		if( returnCollection ) {			
			ArrayList list=new ArrayList( size );
			
			for( Iterator iter=dataColl.iterator(); iter.hasNext(); ) {
				Object record=iter.next();
				
				list.add( extractValue( record ) );
			}
			
			return list;
		} else {
			Object record=null;
			
			// return default value if no record be queried
			if( size==0 ) {
				record=this.select.getObjectNewer().newObject();
			} else {
				record=dataColl.iterator().next();
			}
			
			return extractValue( record );
		}
	}

	private Object extractValue( Object record ) {
		ArrayList columns=this.select.getColumns();
		Column column=(Column) columns.get( 0 );
		ColumnAdapter adapter=column.getAdapter();
		return adapter.extractFieldValue( record );
	}

//	private Object getValue( ResultSet rs ) throws SQLException {
//		ArrayList columns=this.select.getColumns();
//		if( columns.size()>0 ) {
//			Column column=(Column) columns.get( 0 );
//			column.getAdapter().readResultSet( rs, 1, o )
//		}
//		return rs.getObject(1);
//	}

	public QueryResult execute(User user, Map context) throws DBLevelException {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(ds);
			QueryResult result = null;
			int queryLimit = SystemConstant.getQueryLimit();
			if (queryLimit > 0) {
				result = execute(conn, user, context, 0, queryLimit);
				if (result.getData().size() >= queryLimit) {
					result.setReachQueryLimit(true);
				}
			} else {
				result = execute(conn, user, context);
			}
			result.setTotalCount(executeCount(conn, user, context));
			return result;
		} finally {
			DBUtil.close(conn);
		}
	}

	/**
	 * Return query result pagination.
	 * 
	 * @param first         first index, count from 0,1,2,...
	 * @param max           max records
	 */
	public QueryResult execute(User user, Map context, int first, int max) {
		first = (first < 0 ? 0 : first);
		max = (max < 0 ? 0 : max);
		Connection conn = null;
		try {
			conn = DBPower.getConnection(ds);
			int queryLimit = SystemConstant.getQueryLimit();
			if (queryLimit > 0 && queryLimit < max) {
				max = queryLimit;
			}
			QueryResult result = execute(conn, user, context, first, max);
			if (result.getData().size() >= queryLimit) {
				result.setReachQueryLimit(true);
			}
			result.setTotalCount(executeCount(conn, user, context));
			return result;
		} finally {
			DBUtil.close(conn);
		}
	}

	private QueryResult execute(Connection conn, User user, Map context) {
		if (context == null) {
			context = new HashMap();
		}

		List data = new LinkedList();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (type.equals(SQL_TYPE)) {
				pstmt = conn.prepareStatement(toSQL());
			} else if (type.equals(STORED_PROCEDURE_TYPE)) {
				pstmt = conn.prepareCall(storedProcedure.getContent());
			}
			rs = getResultSet(user, context, pstmt);

			// mapping
			Select selectObj = null;
			if (type.equals(SQL_TYPE)) {
				if (isRawSQL) {
					selectObj = rawSQL.getSelect();
				} else {
					selectObj = select;
				}
			} else if (type.equals(STORED_PROCEDURE_TYPE)) {
				selectObj = storedProcedure.getSelect();
			}

			while (rs.next()) {
				Object obj = selectObj.getObjectNewer().newObject();
				ArrayList columns = selectObj.getColumns();
				int columnCount = columns.size();
				for (int i = 0; i < columnCount; i++) {
					Column column = (Column) columns.get(i);
					column.getAdapter().readResultSet(rs, column.getAlias(),
							obj);
				}
				data.add(obj);
			}
		} catch (SQLException e) {
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return getQueryResult(data);
	}

	private QueryResult getQueryResult(Collection data) {
		QueryResult result = new QueryResult();
		result.setData(data);

		if (type.equals(SQL_TYPE)) {

			if (isRawSQL) {
				result.setFields(rawSQL.getSelect().getFields());
				result
						.setReadOnlyFields(rawSQL.getSelect()
								.getReadOnlyFields());
			} else {
				result.setFields(select.getFields());
				result.setReadOnlyFields(select.getReadOnlyFields());
			}

		} else if (type.equals(STORED_PROCEDURE_TYPE)) {
			result.setFields(storedProcedure.getSelect().getFields());
			result.setReadOnlyFields(storedProcedure.getSelect()
					.getReadOnlyFields());
		}

		return result;
	}

	private QueryResult execute(Connection conn, User user, Map context,
			int first, int max) {
		if (context == null) {
			context = new HashMap();
		}

		List data = new LinkedList();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			boolean supportsLimit = false;
			if (type.equals(SQL_TYPE)) {
				supportsLimit = DBUtil.supportsLimit(conn);
				String sql = toSQL();

				if (supportsLimit) {
					sql = DBUtil.getLimitString(conn, sql, first, max);
				}

				pstmt = conn.prepareStatement(sql);
			} else if (type.equals(STORED_PROCEDURE_TYPE)) {
				supportsLimit = false;
				pstmt = conn.prepareCall(storedProcedure.getContent());
			}

			rs = getResultSet(user, context, pstmt);

			// mapping
			Select selectObj = null;
			if (type.equals(SQL_TYPE)) {
				if (isRawSQL) {
					selectObj = rawSQL.getSelect();
				} else {
					selectObj = select;
				}
			} else if (type.equals(STORED_PROCEDURE_TYPE)) {
				selectObj = storedProcedure.getSelect();
			}

			if (supportsLimit) {
				while (rs.next()) {
					Object obj = selectObj.getObjectNewer().newObject();
					ArrayList columns = selectObj.getColumns();
					int columnCount = columns.size();
					for (int i = 0; i < columnCount; i++) {
						Column column = (Column) columns.get(i);
						column.getAdapter().readResultSet(rs,
								column.getAlias(), obj);
					}
					data.add(obj);
				}
			} else {
				// skip to index(first)
				for (int n = 0; n < first && rs.next(); n++)
					;

				// read max records
				for (int n = 0; n < max && rs.next(); n++) {
					Object obj = selectObj.getObjectNewer().newObject();
					ArrayList columns = selectObj.getColumns();
					int columnCount = columns.size();
					for (int i = 0; i < columnCount; i++) {
						Column column = (Column) columns.get(i);
						column.getAdapter().readResultSet(rs,
								column.getAlias(), obj);
					}
					data.add(obj);
				}
			}
		} catch (SQLException e) {
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return getQueryResult(data);
	}

	public String getDs() {
		return ds;
	}

	public void setDs(String ds) {
		this.ds = ds;
	}

	public int executeCount(User user, Map context) {
		Connection conn = null;
		try {
			conn = DBPower.getConnection(ds);
			return executeCount(conn, user, context);
		} finally {
			DBUtil.close(conn);
		}
	}

	private int executeCount(Connection conn, User user, Map context) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			if (type.equals(SQL_TYPE)) {
				pstmt = conn.prepareStatement(toCountSQL());
				rs = getResultSet(user, context, pstmt);
				rs.next();
				count = rs.getInt(1);
			} else if (type.equals(STORED_PROCEDURE_TYPE)) {
				pstmt = conn.prepareCall(storedProcedure.getContent());
				rs = getResultSet(user, context, pstmt);
				while (rs.next()) {
					count++;
				}
			}
			return count;
		} catch (SQLException e) {
			throw new DBLevelException(e);
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}

	/**
	 * SQL: SELECT COUNT(*)...
	 * @return
	 */
	private String toCountSQL() {
		StringBuffer buf = new StringBuffer();
		if (isRawSQL) {
			buf.append( "SELECT COUNT(*) FROM ( ");
			buf.append( rawSQL.getContent() );
			buf.append( where.toSQL() );
			buf.append( ") ralasafe_");
		} else {
			buf.append("SELECT COUNT(*) FROM ( ");
			buf.append(select.toSQL()).append(from.toSQL());
			buf.append(where.toSQL());
			buf.append(groupBy.toSQL());
			buf.append( ") ralasafe_" );
			//buf.append(orderBy.toSQL());

		}
		
		String countSql=buf.toString();
		if( log.isDebugEnabled() ) {
			log.debug( "\n"+countSql );
		}
		
		return countSql;
	}

	private ResultSet getResultSet(User user, Map context,
			PreparedStatement pstmt) throws SQLException {
		int valueCount = values.size();
		for (int i = 0; i < valueCount; i++) {
			Value value = (Value) values.get(i);
			Object setValue = value.getValue(user, context);
			if (value.isBehindLike()) {
				if (setValue instanceof java.util.Date) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					setValue = format.format((java.util.Date) setValue);
				}
				pstmt.setObject(i + 1, "%" + setValue + "%");
			} else {
				if (setValue instanceof java.util.Date) {
					java.util.Date utilDate = (java.util.Date) setValue;
					java.sql.Date sqlDate = new java.sql.Date(utilDate
							.getTime());
					pstmt.setDate(i + 1, sqlDate);
				} else {
					pstmt.setObject(i + 1, setValue);
				}
			}
		}
		// long start=System.currentTimeMillis();
		ResultSet executeQuery = pstmt.executeQuery();
		// long end=System.currentTimeMillis();
		// System.out.println( "Oracle Query Cost Time(ms):" + (end-start) );

		return executeQuery;
	}

	public QueryTestResult test(User user, Map context, int first, int max) {
		QueryTestResult result = new QueryTestResult();
		try {
			QueryResult queryResult = execute(user, context, first, max);
			List data = new ArrayList(queryResult.getData());
			int count = queryResult.getTotalCount();
			result.setSQL(toSQL());
			result.setProperties(getProperties());
			result.setData(getData(data));
			result.setTotalRecords(count);
		} catch (Exception e) {
			result.setFailed(true);
			result.setErrorMessage(e.getMessage());
			result.setSQL(toSQL());
		}

		return result;
	}

	private String[][] getData(List result) {
		ArrayList columns = null;
		if (type.equals(SQL_TYPE)) {
			if (isRawSQL) {
				columns = rawSQL.getSelect().getColumns();
			} else {
				columns = select.getColumns();
			}
		} else if (type.equals(STORED_PROCEDURE_TYPE)) {
			columns = storedProcedure.getSelect().getColumns();
		}

		String[][] data = new String[result.size()][columns.size()];
		for (int i = 0; i < data.length; i++) {
			Object mappingObj = result.get(i);
			for (int j = 0; j < data[i].length; j++) {
				Column column = (Column) columns.get(j);
				Object extractFieldValue=column.getAdapter().extractFieldValue(mappingObj);
				if( extractFieldValue==null ) {
					data[i][j]=null;
				} else {
					data[i][j] = extractFieldValue.toString();
				}
			}
		}
		return data;
	}

	private String[] getProperties() {
		ArrayList columns = null;
		if (type.equals(SQL_TYPE)) {
			if (isRawSQL) {
				columns = rawSQL.getSelect().getColumns();
			} else {
				columns = select.getColumns();
			}
		} else if (type.equals(STORED_PROCEDURE_TYPE)) {
			columns = storedProcedure.getSelect().getColumns();
		}

		String[] properties = new String[columns.size()];
		for (int i = 0; i < properties.length; i++) {
			Column column = (Column) columns.get(i);
			properties[i] = column.getProperty();
		}
		return properties;
	}

	public Query lightCopy() {
		Query q=new Query();
		q.setDs( ds );
		q.setFrom( from );
		q.setGroupBy( groupBy );
		q.setIsRawSQL( isRawSQL );
		q.setName( name );
		q.setOrderBy( orderBy );
		q.setRawSQL( rawSQL );
		q.setSelect( select );
		q.setStoredProcedure( storedProcedure );
		q.setType( type );
		
		ArrayList newValues=new ArrayList( values.size() );
		newValues.addAll( values );
		q.setValues( newValues );
		
		Where w=where.lightCopy();
		q.setWhere( w );
		
		return q;
	}
}
