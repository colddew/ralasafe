/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.metadata.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


import org.ralasafe.db.DBPower;
import org.ralasafe.util.StringUtil;

public class UserMetadata {
	private TableMetadata mainTableMetadata;
	private TableMetadata[] singleValueTableMetadatas;
	private TableMetadata[] multiValueTableMetadatas;
	private String[] userFields;
	private String[] userFieldDisplayNames;
	private String[] showUserFields;
	private String[] showUserFieldDisplayNames;
	
	public static final String ID_FIELD_NAME = "id";
	public static final String NAME_FIELD_NAME = "name";

	public TableMetadata getMainTableMetadata() {
		return mainTableMetadata;
	}

	public void setMainTableMetadata(TableMetadata mainTableMetadata) {
		this.mainTableMetadata = mainTableMetadata;
	}

	public TableMetadata[] getSingleValueTableMetadatas() {
		return singleValueTableMetadatas;
	}

	public void setSingleValueTableMetadatas(
			TableMetadata[] singleValueTableMetadatas) {
		this.singleValueTableMetadatas = singleValueTableMetadatas;
	}

	public TableMetadata[] getMultiValueTableMetadatas() {
		return multiValueTableMetadatas;
	}

	public void setMultiValueTableMetadatas(
			TableMetadata[] multiValueTableMetadatas) {
		this.multiValueTableMetadatas = multiValueTableMetadatas;
	}

	public String[] getUserFields() {
		return userFields;
	}

	public void setUserFields(String[] userFields) {
		this.userFields = userFields;
	}

	public String[] getUserFieldDisplayNames() {
		return userFieldDisplayNames;
	}

	public void setUserFieldDisplayNames(String[] userFieldDisplayNames) {
		this.userFieldDisplayNames = userFieldDisplayNames;
	}

	public String[] getShowUserFields() {
		return showUserFields;
	}

	public void setShowUserFields(String[] showUserFields) {
		this.showUserFields = showUserFields;
	}

	public String[] getShowUserFieldDisplayNames() {
		return showUserFieldDisplayNames;
	}

	public void setShowUserFieldDisplayNames(String[] showUserFieldDisplayNames) {
		this.showUserFieldDisplayNames = showUserFieldDisplayNames;
	}

	/**
	 * Validate UserMetadata. 
	 * 
	 * @return         validate info
	 */
	public String getValidInfo() {
		String line = System.getProperty("line.separator");

		StringBuffer buff = new StringBuffer();
		if (mainTableMetadata == null) {
			buff
					.append(
							"The attribute name=\"mainTable\" is required in 'table' element.")
					.append(line);
		} else {
			String ds = mainTableMetadata.getDatasourceName();
			if (StringUtil.isEmpty(ds)) {
				buff.append(
						"The attribute 'ds' is required in 'table' element.")
						.append(line);
			} else {
				Collection dsNames = DBPower.getDsNames();
				if (!dsNames.contains(ds)) {
					buff.append("No definition found for datasource ds=\"")
							.append(ds).append("\".").append(line);
				}
			}

			FieldMetadata[] fields = mainTableMetadata.getFields();
			Set fieldNames = new HashSet();
			for (int i = 0; i < fields.length; i++) {
				FieldMetadata field = fields[i];
				fieldNames.add(field.getName());
			}

			if (mainTableMetadata.getUniqueFields() == null
					|| mainTableMetadata.getUniqueFields().length == 0) {
				buff
						.append(
								"The attribute 'uniqueFields' is required in 'table' element.")
						.append(line);
			} else {
				FieldMetadata[] uniqueFields = mainTableMetadata
						.getUniqueFields();
				for (int i = 0; i < uniqueFields.length; i++) {
					FieldMetadata fieldMetadata = uniqueFields[i];
					if (fieldMetadata == null) {
						buff
								.append(
										"No attribute 'uniqueFields' found in 'table' element or no field definition for 'uniqueFields'.")
								.append(line);
					} else {
						String name = fieldMetadata.getName();
						if (!fieldNames.contains(name)) {
							buff
									.append(
											"The field '"
													+ name
													+ "' specified in 'uniqueFields' cound not found.")
									.append(line);
						}
					}
				}
			}

			if (mainTableMetadata.getSqlTableName() == null) {
				buff
						.append(
								"The attribute 'sqlName' is required in 'table' element.")
						.append(line);
			}

			if (!fieldNames.contains(ID_FIELD_NAME)) {
				buff
						.append(
								"A 'field' element with attribute name=\"id\" is required.")
						.append(line);
			}

			if (!fieldNames.contains(NAME_FIELD_NAME)) {
				buff
						.append(
								"A 'field' element with attribute name=\"name\" is required.")
						.append(line);
			}
		}

		String info = buff.toString();
		return info.length() == 0 ? null : info;
	}
}
