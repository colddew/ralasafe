/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.ralasafe.SystemConstant;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.UserCategoryTestResult;
import org.ralasafe.user.User;
import org.ralasafe.util.StringUtil;

import bsh.EvalError;
import bsh.Interpreter;

public class UserCategory extends AbstractPolicy implements Script {
	
	public String getRule() {
		StringBuffer buff = new StringBuffer();
		if (isRawScript()) {
			buff.append(getRawScript().toScript()).append("\n");
		} else {
			buff.append("return ").append(getExprGroup().toScript()).append(";\n");
		}
		return buff.toString();
	}

	public String toScript() {
		if (getScript() == null) {

			StringBuffer buff = new StringBuffer();
			buff.append("java.util.Map " + SystemConstant.VARIABLE_MAP
					+ " = new java.util.HashMap();\n");
			int count = getDefineVaribles().size();
			for (int i = 0; i < count; i++) {
				DefineVariable defineVariable = (DefineVariable) getDefineVaribles()
						.get(i);
				if (defineVariable instanceof QueryRef) {
					QueryRef queryRef = (QueryRef) defineVariable;
					if (getExprGroup().isUsedByInExprOrNotInExpr(queryRef
							.getVariableName())) {
						buff.append(queryRef.toScript(true));
					} else {
						buff.append(queryRef.toScript(false));
					}
				} else {
					buff.append(defineVariable.toScript());
				}
				buff.append(SystemConstant.VARIABLE_MAP + ".put( \""
						+ defineVariable.getVariableName() + "\", "
						+ defineVariable.getVariableName() + ");\n");
			}
			buff.append("\n");
			// return values is "doesUserCategoryContain"
			String doesUserCategoryContain = SystemConstant.DOES_USER_CATEGORY_CONTAIN;
			if (isRawScript()) {
				buff.append(getRawScript().toScript() + ";\n");
				int index = buff.indexOf("return");
				if (index >= 0) {
					buff.replace(index, index + 6, " boolean "
							+ doesUserCategoryContain + " = ");
					buff.append(";\n");
				} /*else {
					buff.append(" boolean " + doesUserCategoryContain
											+ " = false;");
				}*/
			} else {
				buff.append(" boolean ").append(doesUserCategoryContain)
						.append(" = ( ").append(getExprGroup().toScript()).append(
								" ); \n");
			}

			setScript( buff.toString() );
		}

		return getScript();
	}

	public UserCategoryTestResult test(User user, Map context,
			QueryManager queryManager) {
		UserCategoryTestResult result = new UserCategoryTestResult();
		Interpreter interpreter = new Interpreter();
		String script = toScript();
		try {
			eval(interpreter, user, context, queryManager);
			Boolean contain = (Boolean) interpreter
					.get(SystemConstant.DOES_USER_CATEGORY_CONTAIN);
			Map variableMap = (Map) interpreter
					.get(SystemConstant.VARIABLE_MAP);
			result.setValid(contain.booleanValue());
			result.setFailed(false);
			result.setScript(script);
			result.setVariableMap(variableMap);
		} catch (EvalError e) {
			result.setFailed(true);
			StringWriter sw=new StringWriter();
			PrintWriter pw=new PrintWriter( sw );
			e.printStackTrace( pw );
			result.setErrorMessage( sw.toString() );
			//result.setErrorMessage(StringUtil.getEvalError(e.getMessage()));
			result.setScript(script);
		}
		return result;
	}

	private void eval(Interpreter interpreter, User user, Map context,
			QueryManager queryManager) throws EvalError {
		String script = toScript();
		// Set variables
		interpreter.set(SystemConstant.USER_KEY, user);
		interpreter.set(SystemConstant.CONTEXT, context);
		interpreter.set(SystemConstant.QUERY_MANAGER, queryManager);

		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		interpreter.set(SystemConstant.SIMPLE_DATE_FORMAT, format);
		// eval the rule
		interpreter.eval(script);
	}
}