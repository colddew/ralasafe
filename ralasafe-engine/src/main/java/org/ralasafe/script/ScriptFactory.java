/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import java.util.ArrayList;

import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.xml.types.SimpleOperatorType;
import org.ralasafe.entitle.QueryManager;

public class ScriptFactory {
	public static UserCategory getUserCategory(
			org.ralasafe.db.sql.xml.UserCategoryType xmlUserCategory,
			QueryManager queryManager) {
		UserCategory userCategory = new UserCategory();
		userCategory.setName(xmlUserCategory.getName());
		userCategory.setIsRawScript(xmlUserCategory.isIsRawScript());
		userCategory.setExprGroup(getExprGroup(xmlUserCategory.getExprGroup()));
		userCategory.setDefineVaribles(getDefineVariables(xmlUserCategory
				.getDefineVariable(), queryManager));
		userCategory.setRawScript(getRawScript(xmlUserCategory.getRawScript()));
		return userCategory;
	}

	private static RawScript getRawScript(
			org.ralasafe.db.sql.xml.RawScript xmlRawScript) {
		RawScript rawScript = new RawScript();
		if (xmlRawScript != null) {
			rawScript.setContent(xmlRawScript.getContent());
		}
		return rawScript;
	}

	public static BusinessData getBusinessData(
			org.ralasafe.db.sql.xml.BusinessDataType xmlBusinessData,
			QueryManager queryManager) {
		BusinessData businessData = new BusinessData();
		businessData.setName(xmlBusinessData.getName());
		businessData.setIsRawScript(xmlBusinessData.getIsRawScript());
		businessData.setExprGroup(getExprGroup(xmlBusinessData.getExprGroup()));
		businessData.setDefineVaribles(getDefineVariables(xmlBusinessData
				.getDefineVariable(), queryManager));
		businessData.setRawScript(getRawScript(xmlBusinessData.getRawScript()));
		return businessData;
	}

	private static ArrayList getDefineVariables(
			org.ralasafe.db.sql.xml.DefineVariable[] xmlDefineVariable,
			QueryManager queryManager) {
		int count = xmlDefineVariable.length;
		ArrayList defineVariables = new ArrayList(count);
		for (int i = 0; i < count; i++) {
			defineVariables.add(getDefineVariable(xmlDefineVariable[i],
					queryManager));
		}
		return defineVariables;
	}

	private static DefineVariable getDefineVariable(
			org.ralasafe.db.sql.xml.DefineVariable xmlDefineVariable,
			QueryManager queryManager) {
		DefineVariable defineVariable = null;
		if (xmlDefineVariable.getContextValue() != null) {
			defineVariable = getContextValue(xmlDefineVariable
					.getContextValue());
		} else if (xmlDefineVariable.getHintValue() != null) {
			defineVariable = getHintValue(xmlDefineVariable.getHintValue());
		} else if (xmlDefineVariable.getUserValue() != null) {
			defineVariable = getUserValue(xmlDefineVariable.getUserValue());
		} else if (xmlDefineVariable.getSimpleValue() != null) {
			defineVariable = getSimpleValue(xmlDefineVariable.getSimpleValue());
		} else if (xmlDefineVariable.getQueryRef() != null) {
			defineVariable = getQueryRef(xmlDefineVariable.getQueryRef(),
					queryManager);
		} else if (xmlDefineVariable.getFormula() != null) {
			defineVariable = getFormula(xmlDefineVariable.getFormula());
		} else {
			throw new RalasafeException("No DefineVariable found.");
		}
		defineVariable.setVariableName(xmlDefineVariable.getName());
		return defineVariable;
	}

	private static Formula getFormula(org.ralasafe.db.sql.xml.Formula xmlFormula) {
		Formula formula = new Formula();
		formula.setOperator(xmlFormula.getOperator().toString());
		formula.setType(xmlFormula.getType().toString());
		formula.setVariables(getVariables(xmlFormula.getVariable()));
		return formula;
	}

	private static ArrayList getVariables(
			org.ralasafe.db.sql.xml.Variable[] xmlVariables) {
		int count = xmlVariables.length;
		ArrayList variables = new ArrayList(count);
		for (int i = 0; i < count; i++) {
			variables.add(getVariable(xmlVariables[i]));
		}
		return variables;
	}

	private static QueryRef getQueryRef(
			org.ralasafe.db.sql.xml.QueryRef xmlQueryRef, QueryManager queryManager) {
		QueryRef queryRef = new QueryRef();
		queryRef.setId(xmlQueryRef.getId());
		queryRef.setName(xmlQueryRef.getName());
		if (queryManager != null) {
			// RPCDataUtil.getScriptUserCategory & getScriptBusinessData method only need script,
			// so there's no need to pass queryManager
			queryRef.setQuery(queryManager.getQuery(queryRef.getId()));
		}
		return queryRef;
	}

	private static UserValue getUserValue(
			org.ralasafe.db.sql.xml.UserValue xmlUserValue) {
		UserValue userValue = new UserValue();
		userValue.setKey(xmlUserValue.getKey());
		return userValue;
	}

	private static ExprGroup getExprGroup(
			org.ralasafe.db.sql.xml.ExprGroup xmlExprGroup) {
		ExprGroup exprGroup = new ExprGroup();
		exprGroup.setExprs(getExprs(xmlExprGroup.getExprGroupTypeItem()));
		exprGroup.setLinker(xmlExprGroup.getLinker().toString());
		return exprGroup;
	}

	private static ArrayList getExprs(
			org.ralasafe.db.sql.xml.ExprGroupTypeItem[] xmlExprs) {
		int count = xmlExprs.length;
		ArrayList exprs = new ArrayList(count);
		for (int i = 0; i < count; i++) {
			exprs.add(getExpr(xmlExprs[i]));
		}
		return exprs;
	}

	private static Expr getExpr(org.ralasafe.db.sql.xml.ExprGroupTypeItem xmlExpr) {
		if (xmlExpr.getBinaryExpr() != null)
			return getBinaryExpr(xmlExpr.getBinaryExpr());
		if (xmlExpr.getInExpr() != null)
			return getInExpr(xmlExpr.getInExpr());
		if (xmlExpr.getNotInExpr() != null)
			return getNotInExpr(xmlExpr.getNotInExpr());
		if (xmlExpr.getIsNullExpr() != null)
			return getIsNullExpr(xmlExpr.getIsNullExpr());
		if (xmlExpr.getIsNotNullExpr() != null)
			return getIsNotNullExpr(xmlExpr.getIsNotNullExpr());
		if (xmlExpr.getExprGroup() != null)
			return getExprGroup(xmlExpr.getExprGroup());
		throw new RalasafeException("No Expr found.");
	}

	private static Expr getIsNullExpr(
			org.ralasafe.db.sql.xml.IsNullExpr xmlIsNullExpr) {
		IsNullExpr isNullExpr = new IsNullExpr();
		isNullExpr.setVariable(getVariable(xmlIsNullExpr.getVariable()));
		return isNullExpr;
	}

	private static Expr getIsNotNullExpr(
			org.ralasafe.db.sql.xml.IsNotNullExpr xmlIsNotNullExpr) {
		IsNotNullExpr isNotNullExpr = new IsNotNullExpr();
		isNotNullExpr.setVariable(getVariable(xmlIsNotNullExpr.getVariable()));
		return isNotNullExpr;
	}

	private static NotInExpr getNotInExpr(
			org.ralasafe.db.sql.xml.NotInExpr xmlNotInExpr) {
		NotInExpr notInExpr = new NotInExpr();
		notInExpr.setVariable1(getVariable(xmlNotInExpr.getVariable1()));
		notInExpr.setVariable2(getVariable(xmlNotInExpr.getVariable2()));
		return notInExpr;
	}

	private static InExpr getInExpr(org.ralasafe.db.sql.xml.InExpr xmlInExpr) {
		InExpr inExpr = new InExpr();
		inExpr.setVariable1(getVariable(xmlInExpr.getVariable1()));
		inExpr.setVariable2(getVariable(xmlInExpr.getVariable2()));
		return inExpr;
	}

	private static Variable getVariable(org.ralasafe.db.sql.xml.Variable xmlVariable) {
		Variable variable = new Variable();
		variable.setName(xmlVariable.getName());
		return variable;
	}

	private static Variable getVariable(
			org.ralasafe.db.sql.xml.Variable1 xmlVariable) {
		Variable variable = new Variable();
		variable.setName(xmlVariable.getName());
		return variable;
	}

	private static Variable getVariable(
			org.ralasafe.db.sql.xml.Variable2 xmlVariable) {
		Variable variable = new Variable();
		variable.setName(xmlVariable.getName());
		return variable;
	}

	private static BinaryExpr getBinaryExpr(
			org.ralasafe.db.sql.xml.BinaryExpr xmlBinaryExpr) {
		BinaryExpr binaryExpr = new BinaryExpr();
		binaryExpr.setVariable1(getVariable(xmlBinaryExpr.getVariable1()));
		binaryExpr.setVariable2(getVariable(xmlBinaryExpr.getVariable2()));
		binaryExpr.setOperator(getOperator(xmlBinaryExpr.getOperator()));
		return binaryExpr;
	}

	private static Operator getOperator(org.ralasafe.db.sql.xml.Operator xmlOperator) {
		if (xmlOperator.getSimpleOperator() != null)
			return getSimpleOperator(xmlOperator.getSimpleOperator());
		throw new RalasafeException("No SimpleOperator found.");
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

	private static SimpleOperator getSimpleOperator(
			SimpleOperatorType xmlSimpleOperator) {
		SimpleOperator simpleOperator = new SimpleOperator();
		simpleOperator.setValue(xmlSimpleOperator.toString());
		return simpleOperator;
	}
}
