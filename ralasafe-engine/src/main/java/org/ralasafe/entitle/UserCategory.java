/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Date;
import java.util.Map;

import org.ralasafe.group.Node;
import org.ralasafe.user.User;

/**
 * UserCategory. Id is pk, name and leaf fields are unqiue fields.
 * 
 * @author back
 * 
 */
public class UserCategory extends Node {
	public final static int RESERVED_USER_CATEGORY_ID = -10;
	private int id;
	private String name;
	private String description;
	private Date installDate;
	private String xmlContent;
	private String file;
	private org.ralasafe.script.UserCategory scriptUserCategory;

	public org.ralasafe.script.UserCategory getScriptUserCategory() {
		return scriptUserCategory;
	}

	public void setScriptUserCategory(
			org.ralasafe.script.UserCategory scriptUserCategory) {
		this.scriptUserCategory = scriptUserCategory;
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public UserCategoryTestResult test(User user, Map context,
			QueryManager queryManager) {
		return scriptUserCategory.test(user, context, queryManager);
	}
}
