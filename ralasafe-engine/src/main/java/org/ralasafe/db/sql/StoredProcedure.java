/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db.sql;

import java.util.ArrayList;

public class StoredProcedure {
	private ArrayList parameters = new ArrayList();
	private String content = "";
	private Select select = new Select();

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public ArrayList getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList parameters) {
		this.parameters = parameters;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
