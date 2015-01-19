/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import java.util.ArrayList;


public abstract class AbstractPolicy {
	private String name;
	private boolean isRawScript;
	private ArrayList defineVaribles = new ArrayList();
	private ExprGroup exprGroup = new ExprGroup();
	private RawScript rawScript = new RawScript();
	private String script;
	
	public String getScript() {
		return script;
	}

	public void setScript( String script ) {
		this.script=script;
	}

	public boolean isRawScript() {
		return isRawScript;
	}

	public void setIsRawScript(boolean isRawScript) {
		this.isRawScript = isRawScript;
	}

	public RawScript getRawScript() {
		return rawScript;
	}

	public void setRawScript(RawScript rawScript) {
		this.rawScript = rawScript;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList getDefineVaribles() {
		return defineVaribles;
	}

	public void setDefineVaribles(ArrayList defineVaribles) {
		this.defineVaribles = defineVaribles;
	}

	public ExprGroup getExprGroup() {
		return exprGroup;
	}

	public void setExprGroup(ExprGroup exprGroup) {
		this.exprGroup = exprGroup;
	}
	
	public abstract String toScript();
}
