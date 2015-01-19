/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.Factory;
import org.ralasafe.adapter.user.UserComplexTableAdapter;
import org.ralasafe.application.Application;
import org.ralasafe.db.Column;
import org.ralasafe.db.DBLevelException;
import org.ralasafe.db.MapStorageObjectNewer;
import org.ralasafe.db.MapStorgeColumnAdapter;
import org.ralasafe.db.MapStorgeObject;
import org.ralasafe.db.Table;
import org.ralasafe.db.impl.ComplexTableDeletorImpl;
import org.ralasafe.db.impl.ComplexTableSaverImpl;
import org.ralasafe.db.impl.ComplexTableSelectorImpl;
import org.ralasafe.db.impl.ComplexTableUpdatorImpl;
import org.ralasafe.db.sql.BinaryExpression;
import org.ralasafe.db.sql.ExpressionGroup;
import org.ralasafe.db.sql.Operator;
import org.ralasafe.db.sql.Query;
import org.ralasafe.db.sql.QueryFactory;
import org.ralasafe.db.sql.SimpleOperator;
import org.ralasafe.db.sql.SimpleValue;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;
import org.ralasafe.userType.UserType;
import org.ralasafe.util.StringUtil;

public class UserManager {
	private UserType userType;
	// private UserMetadata metadata;
	private UserComplexTableAdapter adapter;
	// private ComplexTable complexTable;
	private ComplexTableDeletorImpl deletor;
	private ComplexTableSaverImpl saver;
	private ComplexTableUpdatorImpl updator;
	private ComplexTableSelectorImpl selector;

	// The fields which would be shown on user panel
	private Collection shownFields = new ArrayList();

	public UserManager(UserType userType) {
		this.userType = userType;
		adapter = new UserComplexTableAdapter(userType.getUserMetadata());

		// complexTable=adapter.getComplexTable();

		deletor = adapter.getDeletor();
		saver = adapter.getSaver();
		updator = adapter.getUpdator();
		selector = adapter.getSelector();

		// shownFields
		String[] shownFieldNames = userType.getUserMetadata().getShowUserFields();
		FieldMetadata[] fields = userType.getUserMetadata()
				.getMainTableMetadata().getFields();
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < shownFieldNames.length; j++) {
				if (fields[i].getName().equals(shownFieldNames[j])) {
					shownFields.add(fields[i]);
				}
			}
		}
	}

	private Query adapterQuery(Table t) {
		StringBuffer b = new StringBuffer();

		b.append("<query name=\"innserQueryUser\" ds=\""
				+ t.getDatasourceName() + "\">");
		b.append("<select>");
		Column[] columns = t.getColumns();
		for (int i = 0; i < columns.length; i++) {
			Column column = columns[i];
			String javaType="java.lang.Object";
			MapStorgeColumnAdapter adapter=(MapStorgeColumnAdapter) column.getAdapter();
			javaType=adapter.getClassName();
			b.append("<column name=\"" + column.getName()
					+ "\" tableAlias=\"u\" javaType=\"" +
					javaType + "\"/>");
		}
		b.append("</select>");
		b.append("<from>");
		b.append("<table schema=\"\" name=\"" + t.getName()
				+ "\" alias=\"u\"/>");
		b.append("</from>");
		b.append("<rawSQL><content/></rawSQL>");
		b.append("</query>");

		org.ralasafe.db.sql.xml.QueryType xmlQuery;
		try {
			xmlQuery = org.ralasafe.db.sql.xml.Query.unmarshal(new StringReader(b
					.toString()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		org.ralasafe.db.sql.Query sqlQuery = QueryFactory.getQuery(xmlQuery);

		return sqlQuery;
	}

	private void enforce(Query query) {
		query.getSelect().setMappingClass(null);
		query.getSelect().setObjectNewer(new MapStorageObjectNewer());

		// set column adapter
		ArrayList columns = query.getSelect().getColumns();
		int count = columns.size();
		for (int i = 0; i < count; i++) {
			org.ralasafe.db.sql.Column column = (org.ralasafe.db.sql.Column) columns
					.get(i);
			column.setAdapter(new MapStorgeColumnAdapter(column.getProperty(), column.getJavaType()));
		}
	}

	/**
	 * Adapter sqlQuery to a new Query, add LIKE %key% to where condition
	 * 
	 * @param mainTable
	 * @param key                search value
	 * @param refQuery
	 * @return
	 */
	private Query adapterQuery(Table mainTable, String key, Query refQuery) {
		Query sqlQuery = null;
		if (refQuery != null) {
			sqlQuery = refQuery;
			enforce(sqlQuery);
		} else {
			sqlQuery = adapterQuery(mainTable);
		}

		ExpressionGroup expressionGroupForKey = getExpressionGroupForKey(key,
				sqlQuery);

		if (expressionGroupForKey != null && hasExpression(sqlQuery)) {
			//TODO change this impl later, put orginal express at first like expression group back, 
			ExpressionGroup expressionGroup = new ExpressionGroup();
			expressionGroup.setLinker(ExpressionGroup.AND);
			expressionGroup.getExpressions().add(expressionGroupForKey);
			expressionGroup.getExpressions().add(
					sqlQuery.getWhere().getExpressionGroup());
			sqlQuery.getWhere().setExpressionGroup(expressionGroup);
		} else if (expressionGroupForKey != null && !hasExpression(refQuery)) {
			sqlQuery.getWhere().setExpressionGroup(expressionGroupForKey);
		}

		sqlQuery.reloadSQL();
		return sqlQuery;
	}

	private boolean hasExpression(Query query) {
		if (query == null) {
			return false;
		}

		return query.getWhere().getExpressionGroup().getExpressions().size() > 0;
	}

	private ExpressionGroup getExpressionGroupForKey(String key, Query sqlQuery) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}

		ExpressionGroup expressionGroup = new ExpressionGroup();
		expressionGroup.setLinker(ExpressionGroup.OR);
		Iterator columnItr = sqlQuery.getSelect().getColumns().iterator();
		while (columnItr.hasNext()) {
			org.ralasafe.db.sql.Column column = (org.ralasafe.db.sql.Column) columnItr
					.next();
			if (!isColumnShown(column)) {
				continue;
			}

			BinaryExpression binaryExpression = new BinaryExpression();
			// operand1
			binaryExpression.setOperand1(column);
			// operand2
			SimpleValue operand2 = new SimpleValue();
			operand2.setType(SimpleValue.STRING);
			operand2.setValue(key);
			binaryExpression.setOperand2(operand2);
			// LIKE operator
			Operator operator = new SimpleOperator();
			operator.setValue(SimpleOperator.LIKE);
			binaryExpression.setOperator(operator);

			expressionGroup.getExpressions().add(binaryExpression);
			
			//TODO change this impl later, put orginal express at first like expression group back, 
			// add simple value to values
			sqlQuery.getValues().add(0, operand2);
		}

		return expressionGroup;
	}

	public int selectUserCounts(User user, Map context, String key,
			Query refQuery) {
		Query sqlQuery = adapterQuery(adapter.getComplexTable().getMainTable(),
				key, refQuery);
		return sqlQuery.executeCount(user, context);
	}

	public Collection selectUsers(User user, Map context, String key,
			Query refQuery, int start, int limit) {
		Query sqlQuery = adapterQuery(adapter.getComplexTable().getMainTable(),
				key, refQuery);

		Collection data = sqlQuery.execute(user, context, start, limit)
				.getData();
		List result = new ArrayList(data.size());
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			MapStorgeObject mso = (MapStorgeObject) iter.next();
			result.add(adapter.toUser(mso));
		}
		return result;
	}

	/**
	 * When ralasafe is turned secured (by web.xml-->RalasafeServlet-->secured=true),
	 * developer should customized query for select user, and return results in a customized javabean (not org.ralasafe.user.User).
	 *  
	 * @param user
	 * @param context
	 * @param key
	 * @param refQuery
	 * @param start
	 * @param limit
	 * @return
	 */
	public Collection selectUsersByCustomizedJavaBean(User user, Map context, String key,
			Query refQuery, int start, int limit) {
		Query sqlQuery = adapterQuery(adapter.getComplexTable().getMainTable(),
				key, refQuery);

		Collection data = sqlQuery.execute(user, context, start, limit)
				.getData();
		List result = new ArrayList(data.size());
		for (Iterator iter = data.iterator(); iter.hasNext();) {
			MapStorgeObject mso = (MapStorgeObject) iter.next();
			result.add(adapter.javabeanToUser(mso));
		}
		return result;
	}
	
	private boolean isColumnShown(org.ralasafe.db.sql.Column column) {
		Iterator itr = shownFields.iterator();
		while (itr.hasNext()) {
			FieldMetadata field = (FieldMetadata) itr.next();
			if (field.getColumnName().equalsIgnoreCase(column.getName())) {
				return true;
			}
		}
		return false;
	}

	public void save(User user) throws DBLevelException, EntityExistException {
		MapStorgeObject mso = adapter.toMSO(user);
		saver.save(mso);
	}

	public void update(User user) throws DBLevelException, EntityExistException {
		MapStorgeObject mso = adapter.toMSO(user);
		updator.update(mso);
	}

	public void delete(User user) {
		// delete this user's role-user relationship
		Collection applications = org.ralasafe.Factory.getUserTypeManager()
				.getApplications(userType.getName());
		Iterator itr = applications.iterator();
		while (itr.hasNext()) {
			Application application = (Application) itr.next();
			UserRoleManager userRoleManager = Factory.getUserRoleManager(
					application.getName(), userType.getName());
			userRoleManager.deleteUserRoles(user.get(User.idFieldName));
		}

		// delete user
		MapStorgeObject mso = adapter.toMSO(user);
		deletor.delete(mso);
	}

	public User selectById(User user) {
		MapStorgeObject mso = adapter.toMSO(user);

		MapStorgeObject selectValue = (MapStorgeObject) selector
				.selectByIdColumns(mso);
		return adapter.toUser(selectValue);
	}

	public User selectByUniqueFields(User user) {
		MapStorgeObject mso = adapter.toMSO(user);

		MapStorgeObject selectValue = (MapStorgeObject) selector
				.selectByUniqueColumns(mso);
		return adapter.toUser(selectValue);
	}

	public Collection selectAll() {
		Collection msos = selector.select(null, null);

		List users = new ArrayList(msos.size());
		for (Iterator iter = msos.iterator(); iter.hasNext();) {
			MapStorgeObject mso = (MapStorgeObject) iter.next();
			users.add(adapter.toUser(mso));
		}
		return users;
	}
}
