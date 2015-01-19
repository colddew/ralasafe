/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ralasafe.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataSourceHelper {
	private String file;

	public DataSourceHelper(String file) {
		this.file = file;
	}

	public List parseConfig() {
		List list = new LinkedList();

		DocumentBuilderFactory fcty = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = fcty.newDocumentBuilder();
			document = builder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Element dssEmt = document.getDocumentElement();
		String defaultDsName = dssEmt.getAttribute("default-ds");

		NodeList dsEmts = dssEmt.getElementsByTagName("datasource");
		for (int i = 0, size = dsEmts.getLength(); i < size; i++) {
			Element dsEmt = (Element) dsEmts.item(i);
			String name = dsEmt.getAttribute("name");
			String configFile = dsEmt.getAttribute("configFile");
			String schemasAttr = dsEmt.getAttribute("schemas");
			if (schemasAttr == null) {
				schemasAttr = "";
			} else {
				schemasAttr = schemasAttr.trim();
			}

			DataSourceConfig config = new DataSourceConfig();
			config.setConfigFile(configFile);
			config.setDefault(name.equals(defaultDsName));
			config.setName(name);

			if (StringUtil.isEmpty(schemasAttr)
					|| schemasAttr.equalsIgnoreCase("all")
					|| schemasAttr.equalsIgnoreCase("*")) {
				config.setShowAllSchemas(true);
			} else {
				String[] splitArray = StringUtil.split(schemasAttr, ",");
				for (int j = 0; j < splitArray.length; j++) {
					splitArray[j] = splitArray[j].trim();
				}
				config.setSchemas(splitArray);
			}

			list.add(config);
		}

		return list;
	}
}

class DataSourceConfig {
	private boolean isDefault;
	private String name;
	private String configFile;
	private boolean showAllSchemas;
	private String[] schemas;

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public boolean isShowAllSchemas() {
		return showAllSchemas;
	}

	public void setShowAllSchemas(boolean showAllSchemas) {
		this.showAllSchemas = showAllSchemas;
	}

	public String[] getSchemas() {
		return schemas;
	}

	public void setSchemas(String[] schemas) {
		this.schemas = schemas;
	}

	public String toString() {
		String string = "Datasource: " + name + " [ConfigFile:" + configFile
				+ ", schemas=";
		if (showAllSchemas) {
			string += "*]";
		} else {
			for (int i = 0; i < schemas.length; i++) {
				if (i != 0) {
					string += ",";
				}
				string += schemas[i];
			}
			string += "]";
		}

		return string;
	}
}
