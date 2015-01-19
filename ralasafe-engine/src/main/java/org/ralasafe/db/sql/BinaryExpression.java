/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

public class BinaryExpression implements Expression {
	private Operand operand1;
	private Operand operand2;
	private Operator operator;

	public String toSQL() {
		StringBuffer buf = new StringBuffer();
		buf.append(" ");
		// operand1
		if (operand1 instanceof Query) {
			buf.append("(").append(operand1.toSQL()).append(")");
		} else {
			buf.append(operand1.toSQL());
		}

		// operator
		buf.append(operator.toSQL());

		// operand2
		if (operator.getValue().equals(SimpleOperator.LIKE)) {
			if(operand2 instanceof Value){
				((Value)operand2).setBehindLike(true);
			}
		}
		if (operand2 instanceof Query) {
			buf.append("(").append(operand2.toSQL()).append(")");
		} else {
			buf.append(operand2.toSQL());
		}

		buf.append(" ");
		return buf.toString();
	}

	public Operand getOperand1() {
		return operand1;
	}

	public void setOperand1(Operand operand1) {
		this.operand1 = operand1;
	}

	public Operand getOperand2() {
		return operand2;
	}

	public void setOperand2(Operand operand2) {
		this.operand2 = operand2;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
