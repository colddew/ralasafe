/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;
import java.util.Collection;

import org.ralasafe.RalasafeException;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.JavaBeanColumnAdapter;
import org.ralasafe.db.JavaBeanObjectNewer;
import org.ralasafe.db.MapStorageObjectNewer;
import org.ralasafe.db.MapStorgeColumnAdapter;
import org.ralasafe.db.sql.xml.Operand1;
import org.ralasafe.db.sql.xml.Operand2;
import org.ralasafe.db.sql.xml.types.SimpleOperatorType;
import org.ralasafe.util.StringUtil;

public class QueryFactory {
	public static Query getQuery(org.ralasafe.db.sql.xml.QueryType xmlQuery) {
		ArrayList values = new ArrayList();
		Query query = getQuery(xmlQuery, values);
		query.setValues(values);
		return query;
	}

	private static Query getQuery(org.ralasafe.db.sql.xml.QueryType xmlQuery,
			ArrayList values) {
		String type = xmlQuery.getType().toString();
		Query query = new Query();
		query.setName(xmlQuery.getName());
		String ds = xmlQuery.getDs();
		if (ds == null || ds.equals("") || DBPower.getDataSource(ds) == null) {
			ds = DBPower.getDefaultAppDsName();
		}
		query.setDs(ds);
		query.setType(type);
		query.setIsRawSQL(xmlQuery.isIsRawSQL());

		if (xmlQuery.getRawSQL() != null) {
			query.setRawSQL(getRawSQL(xmlQuery.getRawSQL(), values, type
					.equals(Query.SQL_TYPE)
					&& xmlQuery.isIsRawSQL()));
		} else {
			query.setRawSQL(new RawSQL());
		}

		if (xmlQuery.getStoredProcedure() != null) {
			query.setStoredProcedure(getStoredProcedure(xmlQuery
					.getStoredProcedure(), values, type
					.equals(Query.STORED_PROCEDURE_TYPE)));
		} else {
			query.setStoredProcedure(new StoredProcedure());
		}

		query.setSelect(getSelect(xmlQuery.getQueryTypeSequence().getSelect()));
		query.setFrom(getFrom(xmlQuery.getQueryTypeSequence().getFrom()));

		if (xmlQuery.getQueryTypeSequence().getWhere() != null
				&&!xmlQuery.isIsRawSQL()
				&&!xmlQuery.isIsStoredProcedure()) {
			query.setWhere(getWhere(xmlQuery.getQueryTypeSequence().getWhere(),
					values, type.equals(Query.SQL_TYPE)
							&& !xmlQuery.isIsRawSQL()));
		}

		if (xmlQuery.getQueryTypeSequence().getGroupBy() != null) {
			query.setGroupBy(getGroupBy(xmlQuery.getQueryTypeSequence()
					.getGroupBy()));
		}

		if (xmlQuery.getQueryTypeSequence().getOrderBy() != null) {
			query.setOrderBy(getOrderBy(xmlQuery.getQueryTypeSequence()
					.getOrderBy()));
		}

		return query;
	}

	private static RawSQL getRawSQL(org.ralasafe.db.sql.xml.RawSQL xmlRawSQL,
			ArrayList values, boolean addParam2Values) {
		RawSQL rawSQL = new RawSQL();
		rawSQL.setContent(xmlRawSQL.getContent());
		rawSQL.setParameters(getParameters(xmlRawSQL.getParameter(), values,
				addParam2Values));
		if (xmlRawSQL.getSelect() != null) {
			rawSQL.setSelect(getSelect(xmlRawSQL.getSelect()));
		}
		return rawSQL;
	}

	private static StoredProcedure getStoredProcedure(
			org.ralasafe.db.sql.xml.StoredProcedure xmlStoredProcedure,
			ArrayList values, boolean addParam2Values) {
		StoredProcedure storedProcedure = new StoredProcedure();
		storedProcedure.setContent(xmlStoredProcedure.getContent());
		storedProcedure.setParameters(getParameters(xmlStoredProcedure
				.getParameter(), values, addParam2Values));
		if (xmlStoredProcedure.getSelect() != null) {
			storedProcedure
					.setSelect(getSelect(xmlStoredProcedure.getSelect()));
		}
		return storedProcedure;
	}

	private static ArrayList getParameters(
			org.ralasafe.db.sql.xml.Parameter[] xmlParameters, ArrayList values,
			boolean addParam2Values) {
		ArrayList parameters = new ArrayList(xmlParameters.length);
		for (int i = 0; i < xmlParameters.length; i++) {
			parameters.add(getParameter(xmlParameters[i], values,
					addParam2Values));
		}
		return parameters;
	}

	private static Parameter getParameter(
			org.ralasafe.db.sql.xml.Parameter xmlParameter, ArrayList values,
			boolean addParam2Values) {
		if (xmlParameter.getContextValue() != null) {
			Value value = getContextValue(xmlParameter.getContextValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}
		if (xmlParameter.getHintValue() != null) {
			Value value = getHintValue(xmlParameter.getHintValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}
		if (xmlParameter.getSimpleValue() != null) {
			Value value = getSimpleValue(xmlParameter.getSimpleValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}
		if (xmlParameter.getUserValue() != null) {
			Value value = getUserValue(xmlParameter.getUserValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}
		throw new RalasafeException("No Parameter found.");
	}

	private static Where getWhere(org.ralasafe.db.sql.xml.Where xmlWhere,
			ArrayList values, boolean addParam2Values) {
		Where where = new Where();
		where.setExpressionGroup(getExpressionGroup(xmlWhere
				.getExpressionGroup(), values, addParam2Values));
		return where;
	}

	private static ExpressionGroup getExpressionGroup(
			org.ralasafe.db.sql.xml.ExpressionGroup xmlExpressionGroup,
			ArrayList values, boolean addParam2Values) {
		ExpressionGroup expressionGroup = new ExpressionGroup();
		expressionGroup.setExpressions(getExpressions(xmlExpressionGroup
				.getExpressionGroupTypeItem(), values, addParam2Values));
		expressionGroup.setLinker(xmlExpressionGroup.getLinker().toString());
		return expressionGroup;
	}

	private static Expression getExpression(
			org.ralasafe.db.sql.xml.ExpressionGroupTypeItem xmlExpression,
			ArrayList values, boolean addParam2Values) {
		if (xmlExpression.getBinaryExpression() != null)
			return getBinaryExpression(xmlExpression.getBinaryExpression(),
					values, addParam2Values);
		if (xmlExpression.getInExpression() != null)
			return getInExpression(xmlExpression.getInExpression(), values,
					addParam2Values);
		if (xmlExpression.getNotInExpression() != null)
			return getNotInExpression(xmlExpression.getNotInExpression(),
					values, addParam2Values);
		if (xmlExpression.getIsNullExpression() != null)
			return getIsNullExpression(xmlExpression.getIsNullExpression());
		if (xmlExpression.getIsNotNullExpression() != null)
			return getIsNotNullExpression(xmlExpression
					.getIsNotNullExpression());
		if (xmlExpression.getExpressionGroup() != null)
			return getExpressionGroup(xmlExpression.getExpressionGroup(),
					values, addParam2Values);
		throw new RalasafeException("No Expression found");
	}

	private static Expression getIsNullExpression(
			org.ralasafe.db.sql.xml.IsNullExpression xmlIsNullExpression) {
		IsNullExpression isNullExpression = new IsNullExpression();
		isNullExpression.setColumn(getColumn(xmlIsNullExpression.getColumn()));
		return isNullExpression;
	}

	private static Expression getIsNotNullExpression(
			org.ralasafe.db.sql.xml.IsNotNullExpression xmlIsNotNullExpression) {
		IsNotNullExpression isNotNullExpression = new IsNotNullExpression();
		isNotNullExpression.setColumn(getColumn(xmlIsNotNullExpression
				.getColumn()));
		return isNotNullExpression;
	}

	private static Collection getExpressions(
			org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[] xmlExpressions,
			ArrayList values, boolean addParam2Values) {
		Collection expressions = new ArrayList();
		int count = xmlExpressions.length;
		for (int i = 0; i < count; i++) {
			expressions.add(getExpression(xmlExpressions[i], values,
					addParam2Values));
		}
		return expressions;
	}

	private static NotInExpression getNotInExpression(
			org.ralasafe.db.sql.xml.NotInExpression xmlNotInExpression,
			ArrayList values, boolean addParam2Values) {
		NotInExpression notInExpression = new NotInExpression();
		notInExpression.setLeft(getLeft(xmlNotInExpression.getLeft()
				.getLeftOfIn(), values, addParam2Values));
		notInExpression.setRight(getRight(xmlNotInExpression.getRight()
				.getRightOfIn(), values, addParam2Values));
		return notInExpression;
	}

	private static InExpression getInExpression(
			org.ralasafe.db.sql.xml.InExpression xmlInExpression, ArrayList values,
			boolean addParam2Values) {
		InExpression inExpression = new InExpression();
		inExpression.setLeft(getLeft(xmlInExpression.getLeft().getLeftOfIn(),
				values, addParam2Values));
		inExpression.setRight(getRight(xmlInExpression.getRight()
				.getRightOfIn(), values, addParam2Values));
		return inExpression;
	}

	private static LeftOfIn getLeft(org.ralasafe.db.sql.xml.LeftOfIn xmlLeft,
			ArrayList values, boolean addParam2Values) {
		if (xmlLeft.getQuery() != null)
			return getQuery(xmlLeft.getQuery(), values);
		if (xmlLeft.getColumn() != null) {
			ColumnsOfIn columnsOfIn = new ColumnsOfIn();
			columnsOfIn.setColumns(getColumns(xmlLeft.getColumn()));
			return columnsOfIn;
		}
		throw new RalasafeException("No LeftOfIn found.");
	}

	private static RightOfIn getRight(org.ralasafe.db.sql.xml.RightOfIn xmlRight,
			ArrayList values, boolean addParam2Values) {
		if (xmlRight.getQuery() != null)
			return getQuery(xmlRight.getQuery(), values);
		if (xmlRight.getValue() != null) {
			ValuesOfIn valuesOfIn = new ValuesOfIn();
			valuesOfIn.setValues(getValues(xmlRight.getValue(), values,
					addParam2Values));
			return valuesOfIn;
		}
		throw new RalasafeException("No RightOfIn found.");
	}

	private static ArrayList getValues(org.ralasafe.db.sql.xml.Value[] xmlValues,
			ArrayList values, boolean addParam2Values) {
		ArrayList list = new ArrayList();
		int count = xmlValues.length;
		for (int i = 0; i < count; i++) {
			list.add(getValue(xmlValues[i], values, addParam2Values));
		}
		return list;
	}

	private static BinaryExpression getBinaryExpression(
			org.ralasafe.db.sql.xml.BinaryExpression xmlBinaryExpression,
			ArrayList values, boolean addParam2Values) {
		BinaryExpression binaryExpression = new BinaryExpression();
		binaryExpression.setOperand1(getOperand(xmlBinaryExpression
				.getOperand1(), values, addParam2Values));
		binaryExpression.setOperand2(getOperand(xmlBinaryExpression
				.getOperand2(), values, addParam2Values));
		binaryExpression.setOperator(getOperator(xmlBinaryExpression
				.getOperator()));
		return binaryExpression;
	}

	private static Operand getOperand(Operand1 operand1, ArrayList values,
			boolean addParam2Values) {
		return getOperand(operand1.getOperand(), values, addParam2Values);
	}

	private static Operand getOperand(Operand2 operand2, ArrayList values,
			boolean addParam2Values) {
		return getOperand(operand2.getOperand(), values, addParam2Values);
	}

	private static Operand getOperand(org.ralasafe.db.sql.xml.Operand xmlOperand,
			ArrayList values, boolean addParam2Values) {
		if (xmlOperand.getColumn() != null)
			return getColumn(xmlOperand.getColumn());
		if (xmlOperand.getValue() != null)
			return getValue(xmlOperand.getValue(), values, addParam2Values);
		if (xmlOperand.getQuery() != null)
			return getQuery(xmlOperand.getQuery(), values);
		throw new RalasafeException("No Operand found.");
	}

	private static Value getValue(org.ralasafe.db.sql.xml.Value xmlValue,
			ArrayList values, boolean addParam2Values) {
		if (xmlValue.getSimpleValue() != null) {
			Value value = getSimpleValue(xmlValue.getSimpleValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}

		if (xmlValue.getContextValue() != null) {
			Value value = getContextValue(xmlValue.getContextValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}

		if (xmlValue.getHintValue() != null) {
			Value value = getHintValue(xmlValue.getHintValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}

		if (xmlValue.getUserValue() != null) {
			Value value = getUserValue(xmlValue.getUserValue());
			if (addParam2Values) {
				values.add(value);
			}
			return value;
		}
		throw new RalasafeException("No Value found.");
	}

	private static UserValue getUserValue(
			org.ralasafe.db.sql.xml.UserValue xmlUserValue) {
		UserValue userValue = new UserValue();
		userValue.setKey(xmlUserValue.getKey());
		return userValue;
	}

	private static HintValue getHintValue(
			org.ralasafe.db.sql.xml.HintValue xmlHintValue) {
		HintValue hintValue = new HintValue();
		hintValue.setHint(xmlHintValue.getHint());
		hintValue.setKey(xmlHintValue.getKey());
		return hintValue;
	}

	private static ContextValue getContextValue(
			org.ralasafe.db.sql.xml.ContextValue xmlContextValue) {
		ContextValue contextValue = new ContextValue();
		contextValue.setKey(xmlContextValue.getKey());
		return contextValue;
	}

	private static SimpleValue getSimpleValue(
			org.ralasafe.db.sql.xml.SimpleValue xmlSimpleValue) {
		SimpleValue simpleValue = new SimpleValue();
		simpleValue.setType(xmlSimpleValue.getType().toString());
		simpleValue.setValue(xmlSimpleValue.getContent());
		return simpleValue;
	}

	private static Operator getOperator(org.ralasafe.db.sql.xml.Operator xmlOperator) {
		if (xmlOperator.getSimpleOperator() != null)
			return getSimpleOperator(xmlOperator.getSimpleOperator());
		throw new RalasafeException("No SimpleOperator found.");
	}

	private static SimpleOperator getSimpleOperator(
			SimpleOperatorType xmlSimpleOperator) {
		SimpleOperator simpleOperator = new SimpleOperator();
		simpleOperator.setValue(xmlSimpleOperator.toString());
		return simpleOperator;
	}

	private static OrderBy getOrderBy(org.ralasafe.db.sql.xml.OrderBy xmlOrderBy) {
		OrderBy orderBy = new OrderBy();
		orderBy.setColumns(getColumns(xmlOrderBy.getColumn()));
		return orderBy;
	}

	private static GroupBy getGroupBy(org.ralasafe.db.sql.xml.GroupBy xmlGroupBy) {
		GroupBy groupBy = new GroupBy();
		groupBy.setColumns(getColumns(xmlGroupBy.getColumn()));
		return groupBy;
	}

	private static From getFrom(org.ralasafe.db.sql.xml.From xmlFrom) {
		From from = new From();
		from.setTables(getTables(xmlFrom.getTable()));
		return from;
	}

	private static Collection getTables(org.ralasafe.db.sql.xml.Table[] xmlTables) {
		Collection tables = new ArrayList();
		int count = xmlTables.length;
		for (int i = 0; i < count; i++) {
			tables.add(getTable(xmlTables[i]));
		}
		return tables;
	}

	private static Table getTable(org.ralasafe.db.sql.xml.Table xmlTable) {
		Table table = new Table();
		table.setSchema(xmlTable.getSchema());
		table.setName(xmlTable.getName());
		table.setAlias(xmlTable.getAlias());
		return table;
	}

	private static Select getSelect(org.ralasafe.db.sql.xml.Select xmlSelect) {
		Select select = new Select();
		select.setMappingClass(xmlSelect.getMappingClass());
		select.setDistinct(xmlSelect.getIsDistinct());
		select.setColumns(getColumns(xmlSelect.getColumn()));
		
		// set mapping class, also known as ObjectNewer
		if (select.getMappingClass() == null
				|| select.getMappingClass().equals(""))
			select.setObjectNewer(new MapStorageObjectNewer());
		else
			select.setObjectNewer(new JavaBeanObjectNewer(select
					.getMappingClass()));
		
		// set ColumnAdapter
		ArrayList columns = select.getColumns();
		int count = columns.size();
		for (int i = 0; i < count; i++) {
			Column column = (Column) columns.get(i);
			// if no property value, then use column name instead
			String property = column.getProperty();
			if (StringUtil.isEmpty(property)) {
				property = column.getName();
			}
			String javaType = column.getJavaType();
			if (StringUtil.isEmpty(javaType)) {
				javaType = "java.lang.Object";
			}
			if (select.getMappingClass() == null
					|| select.getMappingClass().equals(""))
				column.setAdapter(new MapStorgeColumnAdapter(property,javaType));
			else
				column
						.setAdapter(new JavaBeanColumnAdapter(property,
								javaType));
		}
		return select;
	}

	private static ArrayList getColumns(org.ralasafe.db.sql.xml.Column[] xmlColumns) {
		ArrayList columns = new ArrayList();
		int count = xmlColumns.length;
		for (int i = 0; i < count; i++) {
			columns.add(getColumn(xmlColumns[i]));
		}
		return columns;
	}

	private static Column getColumn(org.ralasafe.db.sql.xml.Column xmlColumn) {
		Column column = new Column();
		column.setName(xmlColumn.getName());
		column.setTableAlias(xmlColumn.getTableAlias());
		column.setSqlType(xmlColumn.getSqlType());
		column.setJavaType(xmlColumn.getJavaType());
		column.setProperty(xmlColumn.getProperty());
		column.setOrder(xmlColumn.getOrder().toString());
		column.setReadOnly(xmlColumn.isReadOnly());
		if (xmlColumn.getFunction() != null)
			column.setFunction(xmlColumn.getFunction().toString());
		return column;
	}
}
