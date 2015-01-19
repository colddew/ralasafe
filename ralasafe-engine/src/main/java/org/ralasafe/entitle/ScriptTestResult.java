/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ScriptTestResult {
	private boolean failed;
	private String errorMessage;
	private String script;
	private boolean valid;
	private Map variableMap;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public Map getVariableMap() {
		return variableMap;
	}

	public void setVariableMap(Map variableMap) {
		this.variableMap = variableMap;
		// format date variable
		format(variableMap);
	}

	private void format(Map map) {
		Set entries = map.entrySet();
		Iterator itr = entries.iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			if (entry.getValue() instanceof Date) {
				Date date = (Date) entry.getValue();
				entry.setValue(format.format(date));
			}
		}
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
