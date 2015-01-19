/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.metadata.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ralasafe.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserMetadataParser {
	private static final String TRUE = "true";
	private static final String EMT_USER = "user";
	private static final String EMT_TABLE = "table";
	private static final String EMT_MULTIS = "multis";
	private static final String EMT_SINGLES = "singles";
	private static final String EMT_FIELD = "field";

	private static final String ATTR_FIELD_JAVA_TYPE = "javaType";
	private static final String ATTR_FIELD_SQL_TYPE = "sqlType";
	private static final String ATTR_FIELD_COLUMN_NAME = "columnName";
	private static final String ATTR_FIELD_NAME = "name";
	private static final String ATTR_FIELD_DISPLAY_NAME = "displayName";
	private static final String ATTR_FIELD_SHOW = "show";

	private static final String ATTR_TABLE_MAX_OCC = "maxOcc";
	private static final String ATTR_TABLE_MIN_OCC = "minOcc";
	private static final String ATTR_TABLE_UNIQUE_FIELDS = "uniqueFields";
	private static final String ATTR_TABLE_SQL_NAME = "sqlName";
	private static final String ATTR_TABLE_NAME = ATTR_FIELD_NAME;
	private static final String ATTR_TABLE_DS = "ds";

	public UserMetadata parse(String file) {
		DocumentBuilderFactory fcty = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			DocumentBuilder builder = fcty.newDocumentBuilder();
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

		Element rootEmt = document.getDocumentElement();
		return parseUser(rootEmt);
	}

	public UserMetadata parse(InputStream in) {
		DocumentBuilderFactory fcty = DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			DocumentBuilder builder = fcty.newDocumentBuilder();
			document = builder.parse(in);
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

		Element rootEmt = document.getDocumentElement();
		return parseUser(rootEmt);
	}

	private UserMetadata parseUser(Element rootEmt) {
		UserMetadata user = new UserMetadata();

		// parse main table
		NodeList childNodes = rootEmt.getElementsByTagName(EMT_TABLE);
		Element tableEmt = (Element) childNodes.item(0);
		user.setMainTableMetadata(parseTable(tableEmt));

		List userFieldsList=new LinkedList();
		List userFieldDisplayNamesList=new LinkedList();
		List showUserFieldsList=new LinkedList();
		List showUserFieldDisplayNamesList=new LinkedList();
		
		FieldMetadata[] fields = user.getMainTableMetadata().getFields();
		for (int i = 0; i < fields.length; i++) {
			userFieldsList.add( fields[i].getName() );
			userFieldDisplayNamesList.add( fields[i].getDisplayName() );
			
			if (fields[i].isShow()) {
				showUserFieldsList.add( fields[i].getName() );
				showUserFieldDisplayNamesList.add( fields[i].getDisplayName() );
			}
		}
		
		String[] sa=new String[0];
		user.setUserFields( (String[]) userFieldsList.toArray(sa) );
		user.setUserFieldDisplayNames( (String[]) userFieldDisplayNamesList.toArray(sa) );
		user.setShowUserFields( (String[]) showUserFieldsList.toArray(sa) );
		user.setShowUserFieldDisplayNames( (String[]) showUserFieldDisplayNamesList.toArray(sa) );

		// parse single value tables
		childNodes = rootEmt.getElementsByTagName(EMT_SINGLES);
		if (childNodes != null && childNodes.getLength() > 0) {
			Element singlesEmt = (Element) childNodes.item(0);
			NodeList tableEmts = singlesEmt.getElementsByTagName(EMT_TABLE);

			TableMetadata[] tables = new TableMetadata[tableEmts.getLength()];
			for (int i = 0; i < tables.length; i++) {
				tables[i] = parseTable((Element) tableEmts.item(i));
			}
			user.setSingleValueTableMetadatas(tables);
		}

		// parse multi value tables
		childNodes = rootEmt.getElementsByTagName(EMT_MULTIS);
		if (childNodes != null && childNodes.getLength() > 0) {
			Element multisEmt = (Element) childNodes.item(0);
			NodeList tableEmts = multisEmt.getElementsByTagName(EMT_TABLE);

			TableMetadata[] tables = new TableMetadata[tableEmts.getLength()];
			for (int i = 0; i < tables.length; i++) {
				tables[i] = parseTable((Element) tableEmts.item(i));
			}
			user.setMultiValueTableMetadatas(tables);
		}

		return user;
	}

	private TableMetadata parseTable(Element tableEmt) {
		TableMetadata table = new TableMetadata();
		String dsName = tableEmt.getAttribute(ATTR_TABLE_DS);
		String name = tableEmt.getAttribute(ATTR_TABLE_NAME);
		String sqlName = tableEmt.getAttribute(ATTR_TABLE_SQL_NAME);
		String uniqueFieldsAttr = tableEmt
				.getAttribute(ATTR_TABLE_UNIQUE_FIELDS);
		String minOcc = tableEmt.getAttribute(ATTR_TABLE_MIN_OCC);
		String maxOcc = tableEmt.getAttribute(ATTR_TABLE_MAX_OCC);

		NodeList fields = tableEmt.getElementsByTagName(EMT_FIELD);
		table.setDatasourceName(dsName);
		table.setFields(parseFields(fields));
		table.setMaxOcc(StringUtil.isEmpty(minOcc) ? 0 : Integer
				.parseInt(minOcc));
		table.setMinOcc(StringUtil.isEmpty(maxOcc) ? 0 : Integer
				.parseInt(maxOcc));
		table.setName(name);
		table.setSqlTableName(sqlName);

		FieldMetadata[] uniqueFields = null;
		if (uniqueFieldsAttr != null) {
			Map fieldMap = new HashMap();
			FieldMetadata[] fields2 = table.getFields();
			for (int i = 0; i < fields2.length; i++) {
				FieldMetadata field = fields2[i];
				fieldMap.put(field.getName(), field);
			}

			String[] split = StringUtil.split(uniqueFieldsAttr, ",");
			uniqueFields = new FieldMetadata[split.length];
			for (int i = 0; i < split.length; i++) {
				FieldMetadata field = (FieldMetadata) fieldMap.get(split[i]);
				uniqueFields[i] = field;
			}
		}

		table.setUniqueFields(uniqueFields);
		return table;
	}

	private FieldMetadata[] parseFields(NodeList fieldEmts) {
		int size = fieldEmts.getLength();
		FieldMetadata[] fields = new FieldMetadata[size];

		for (int i = 0; i < size; i++) {
			fields[i] = parseField((Element) fieldEmts.item(i));
		}
		return fields;
	}

	private FieldMetadata parseField(Element fieldEmt) {
		String name = fieldEmt.getAttribute(ATTR_FIELD_NAME);
		String columnName = fieldEmt.getAttribute(ATTR_FIELD_COLUMN_NAME);
		String sqlType = fieldEmt.getAttribute(ATTR_FIELD_SQL_TYPE);
		String javaType = fieldEmt.getAttribute(ATTR_FIELD_JAVA_TYPE);

		String displayName = name;
		if (fieldEmt.hasAttribute(ATTR_FIELD_DISPLAY_NAME)) {
			displayName = fieldEmt.getAttribute(ATTR_FIELD_DISPLAY_NAME);
		}

		boolean show = false;
		if (fieldEmt.hasAttribute(ATTR_FIELD_SHOW)
				&& fieldEmt.getAttribute(ATTR_FIELD_SHOW).equals(TRUE)) {
			show = true;
		}

		FieldMetadata field = new FieldMetadata();
		field.setColumnName(columnName);
		field.setJavaType(javaType);
		field.setName(name);
		field.setSqlType(sqlType);
		field.setDisplayName(displayName);
		field.setShow(show);

		return field;
	}
}
