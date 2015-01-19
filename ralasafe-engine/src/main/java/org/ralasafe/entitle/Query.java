/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.BinaryExpression;
import org.ralasafe.db.sql.Column;
import org.ralasafe.db.sql.ExpressionGroup;
import org.ralasafe.db.sql.Operand;
import org.ralasafe.db.sql.Operator;
import org.ralasafe.db.sql.OrderBy;
import org.ralasafe.db.sql.SimpleOperator;
import org.ralasafe.db.sql.SimpleValue;
import org.ralasafe.entitle.CustomizedWhere.EqualCondition;
import org.ralasafe.entitle.CustomizedWhere.GreaterEqualCondition;
import org.ralasafe.entitle.CustomizedWhere.GreaterThanCondition;
import org.ralasafe.entitle.CustomizedWhere.LessEqualCondition;
import org.ralasafe.entitle.CustomizedWhere.LessThanCondition;
import org.ralasafe.entitle.CustomizedWhere.LikeCondition;
import org.ralasafe.entitle.CustomizedWhere.NotEqualCondition;
import org.ralasafe.entitle.CustomizedWhere.OrderByCondition;
import org.ralasafe.group.Node;
import org.ralasafe.user.User;

public class Query extends Node {
	public final static int RESERVED_QUERY_ID = -10;
	public final static String RESERVED_QUERY_NAME = "current user's roles";
	public final static String RESERVED_QUERY_DESCRIPTION = "Get current user's roles";
	private int id;
	private String name;
	private String description;
	private Date installDate;
	private String xmlContent;
	private org.ralasafe.db.sql.Query sqlQuery;
	private String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getInstallDate() {
		return installDate;
	}

	public void setInstallDate(Date installDate) {
		this.installDate = installDate;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public org.ralasafe.db.sql.Query getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(org.ralasafe.db.sql.Query sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public QueryResult execute(User user, Map context) {
		return sqlQuery.execute(user, context);
	}

	public QueryResult execute(User user, Map context, CustomizedWhere where) {
		org.ralasafe.db.sql.Query newQuery=addCustomizedWhere(where);
		return newQuery.execute(user, context);
	}

	private ExpressionGroup mergeExpressionGroup(ExpressionGroup group1,
			ExpressionGroup group2) {
		ExpressionGroup newGroup = new ExpressionGroup();
		newGroup.setLinker(ExpressionGroup.AND);
		
		if( group1.getExpressions().size()>0 ) {
			newGroup.getExpressions().add(group1);
		}
		if( group2.getExpressions().size()>0 ) {
			newGroup.getExpressions().add(group2);
		}
		
		return newGroup;
	}

	private ExpressionGroup getExpressionGroup(CustomizedWhere where) {
		ExpressionGroup group = new ExpressionGroup();
		group.setLinker(where.getLinker());

		// EqualCondition
		Iterator itr = where.getEqualConditions().iterator();
		while (itr.hasNext()) {
			EqualCondition equal = (EqualCondition) itr.next();
			BinaryExpression binaryExpression = getBinaryExpression(equal);
			group.getExpressions().add(binaryExpression);
		}

		// NotEqualCondition
		itr = where.getNotEqualConditions().iterator();
		while (itr.hasNext()) {
			NotEqualCondition notEqual = (NotEqualCondition) itr.next();
			BinaryExpression binaryExpression = getBinaryExpression(notEqual);
			group.getExpressions().add(binaryExpression);
		}

		// LessThanCondition
		itr = where.getLessThanConditions().iterator();
		while (itr.hasNext()) {
			LessThanCondition lessThan = (LessThanCondition) itr.next();
			BinaryExpression binaryExpression = getBinaryExpression(lessThan);
			group.getExpressions().add(binaryExpression);
		}

		// LessEqualCondition
		itr = where.getLessEqualConditions().iterator();
		while (itr.hasNext()) {
			LessEqualCondition lessEqual = (LessEqualCondition) itr.next();
			BinaryExpression binaryExpression = getBinaryExpression(lessEqual);
			group.getExpressions().add(binaryExpression);
		}

		// GreaterThanCondition
		itr = where.getGreaterThanConditions().iterator();
		while (itr.hasNext()) {
			GreaterThanCondition greaterThan = (GreaterThanCondition) itr
					.next();
			BinaryExpression binaryExpression = getBinaryExpression(greaterThan);
			group.getExpressions().add(binaryExpression);
		}

		// GreaterEqualCondition
		itr = where.getGreaterEqualConditions().iterator();
		while (itr.hasNext()) {
			GreaterEqualCondition greaterEqual = (GreaterEqualCondition) itr
					.next();
			BinaryExpression binaryExpression = getBinaryExpression(greaterEqual);
			group.getExpressions().add(binaryExpression);
		}

		// LikeCondition
		itr = where.getLikeConditions().iterator();
		while (itr.hasNext()) {
			LikeCondition like = (LikeCondition) itr.next();
			BinaryExpression binaryExpression = getBinaryExpression(like);
			group.getExpressions().add(binaryExpression);
		}

		return group;
	}

	private BinaryExpression getBinaryExpression(String property, Object value,
			Operator operator) {
		BinaryExpression binaryExpression = new BinaryExpression();

		// operand1
		Column operand1=getColumn( property );

		// operand2
		SimpleValue operand2 = new SimpleValue();
		if (operand1.getJavaType().indexOf("Float") != -1
				|| operand1.getJavaType().indexOf("float") != -1
				|| operand1.getJavaType().indexOf("Double") != -1
				|| operand1.getJavaType().indexOf("double") != -1) {
			operand2.setType(SimpleValue.FLOAT);
		} else if (operand1.getJavaType().indexOf("Integer") != -1
				|| operand1.getJavaType().indexOf("int") != -1) {
			operand2.setType(SimpleValue.INTEGER);
		} else if (operand1.getJavaType().indexOf("boolean") != -1
				|| operand1.getJavaType().indexOf("Boolean") != -1) {
			operand2.setType(SimpleValue.BOOLEAN);
		} else if (operand1.getJavaType().indexOf("Date") != -1) {
			operand2.setType(SimpleValue.DATETIME);
		} else {
			operand2.setType(SimpleValue.STRING);
		}

		if (value instanceof Date) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			operand2.setValue(format.format((Date) value));
		} else {
			operand2.setValue(value.toString());
		}

		// binaryExpression
		binaryExpression.setOperand1(operand1);
		binaryExpression.setOperand2(operand2);
		binaryExpression.setOperator(operator);
		return binaryExpression;
	}

	private Column getColumn( String property ) {
		Column operand1 = null;
		if( sqlQuery.isRawSQL() ) {
			operand1=sqlQuery.getRawSQL().getSelect().findColumnByProperty( property );
		} else {
			operand1=sqlQuery.getSelect().findColumnByProperty(property);
		}
		
		if (operand1 == null) {
			// column not matched
			throw new RalasafeException(
					"The Query object has no column mapping to property '"
							+ property + "'.");
		}
		
		Column newColumn=new Column();
		newColumn.setAdapter( operand1.getAdapter() );
		newColumn.setFunction( operand1.getFunction() );
		newColumn.setTableAlias( operand1.getTableAlias() );
		newColumn.setJavaType( operand1.getJavaType() );
		newColumn.setName( operand1.getName() );
		newColumn.setOrder( operand1.getOrder() );
		newColumn.setProperty( operand1.getProperty() );
		newColumn.setSqlType( operand1.getSqlType() );
		
		return newColumn;
	}

	private BinaryExpression getBinaryExpression(EqualCondition equal) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.EQUAL);

		return getBinaryExpression(equal.getProperty(), equal.getValue(),
				operator);
	}

	private BinaryExpression getBinaryExpression(NotEqualCondition notEqual) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.NOT_EQUAL);

		return getBinaryExpression(notEqual.getProperty(), notEqual.getValue(),
				operator);
	}

	private BinaryExpression getBinaryExpression(LessThanCondition lessThan) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.LESS_THAN);

		return getBinaryExpression(lessThan.getProperty(), lessThan.getValue(),
				operator);
	}

	private BinaryExpression getBinaryExpression(LessEqualCondition lessEqual) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.LESS_EQUAL);

		return getBinaryExpression(lessEqual.getProperty(), lessEqual
				.getValue(), operator);
	}

	private BinaryExpression getBinaryExpression(
			GreaterThanCondition greaterThan) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.GREATER_THAN);

		return getBinaryExpression(greaterThan.getProperty(), greaterThan
				.getValue(), operator);
	}

	private BinaryExpression getBinaryExpression(
			GreaterEqualCondition greaterEqual) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.GREATER_EQUAL);

		return getBinaryExpression(greaterEqual.getProperty(), greaterEqual
				.getValue(), operator);
	}

	private BinaryExpression getBinaryExpression(LikeCondition like) {
		// operator
		Operator operator = new SimpleOperator();
		operator.setValue(SimpleOperator.LIKE);

		return getBinaryExpression(like.getProperty(), like.getValue(),
				operator);
	}

	public Object executeQueryRef(User user, Map context,
			boolean returnCollection) {
		return sqlQuery.executeQueryRef(user, context, returnCollection);
	}

	public int executeCount(User user, Map ctx) {
		return sqlQuery.executeCount(user, ctx);
	}

	public int executeCount(User user, Map ctx, CustomizedWhere where) {
		org.ralasafe.db.sql.Query newQuery=addCustomizedWhere(where);
		return newQuery.executeCount(user, ctx);
	}

	/**
	 * Append where into this sqlQuery's where condition
	 */
	private org.ralasafe.db.sql.Query addCustomizedWhere(CustomizedWhere where) {
		if (where == null || !where.hasCondition()) {
			return sqlQuery;
		}

		ExpressionGroup customizedGroup = getExpressionGroup(where);
		ExpressionGroup newGroup = mergeExpressionGroup(sqlQuery.getWhere().getExpressionGroup(),customizedGroup);
		
		org.ralasafe.db.sql.Query newQuery=sqlQuery.lightCopy();
		newQuery.getWhere().setExpressionGroup(newGroup);
		
		// add customized where values
		Collection expressions=customizedGroup.getExpressions();
		ArrayList customizedValues=new ArrayList(expressions.size());
		for( Iterator iter=expressions.iterator(); iter.hasNext(); ) {
			BinaryExpression be=(BinaryExpression)iter.next();
			Operand operand2=be.getOperand2();
			if( operand2 instanceof org.ralasafe.db.sql.Value ) {
				customizedValues.add(operand2);
			}
		}
		
		newQuery.getValues().addAll( customizedValues );
		
		// order by
		if( where.getOrderByConditions().size()>0 ) {
			OrderBy newOrderBy=new OrderBy();
			for( Iterator iter=where.getOrderByConditions().iterator(); iter.hasNext(); ) {
				OrderByCondition obc=(OrderByCondition) iter.next();
				Column column=getColumn( obc.getProperty() );
				column.setOrder( obc.getType() );
				
				newOrderBy.getColumns().add( column );
			}
			
			newQuery.setOrderBy( newOrderBy );
		}
		
		return newQuery;
	}

	public QueryResult execute(User user, Map context, int first, int max) {
		return sqlQuery.execute(user, context, first, max);
	}

	public QueryResult execute(User user, Map context, CustomizedWhere where,
			int first, int max) {
		org.ralasafe.db.sql.Query newQuery=addCustomizedWhere(where);
		return newQuery.execute(user, context, first, max);
	}

	public QueryTestResult test(User user, Map context, int first, int max) {
		return sqlQuery.test(user, context, first, max);
	}
}
