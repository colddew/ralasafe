/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.Date;
import java.util.Map;

import org.ralasafe.group.Node;
import org.ralasafe.user.User;

public class BusinessData extends Node {
	private String name;
	private String description;
	private Date installDate;
	private String xmlContent;
	private int id;
	private String file;
	private org.ralasafe.script.BusinessData scriptBusinessData;

	public org.ralasafe.script.BusinessData getScriptBusinessData() {
		return scriptBusinessData;
	}

	public void setScriptBusinessData(
			org.ralasafe.script.BusinessData scriptBusinessData) {
		this.scriptBusinessData = scriptBusinessData;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public BusinessDataTestResult test(User user, Map context,
			QueryManager queryManager) {
		return scriptBusinessData.test(user, context, queryManager);
	}
}
