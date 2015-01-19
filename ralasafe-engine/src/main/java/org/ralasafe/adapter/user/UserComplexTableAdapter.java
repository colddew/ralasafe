/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.adapter.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ralasafe.db.ComplexTable;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.MapStorageObjectNewer;
import org.ralasafe.db.MapStorgeColumnAdapter;
import org.ralasafe.db.MapStorgeObject;
import org.ralasafe.db.MultiValueTableAdapter;
import org.ralasafe.db.SingleValueTableAdapter;
import org.ralasafe.db.Table;
import org.ralasafe.db.TableNewer;
import org.ralasafe.db.impl.ComplexTableDeletorImpl;
import org.ralasafe.db.impl.ComplexTableSaverImpl;
import org.ralasafe.db.impl.ComplexTableSelectorImpl;
import org.ralasafe.db.impl.ComplexTableUpdatorImpl;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.metadata.user.TableMetadata;
import org.ralasafe.metadata.user.UserMetadata;
import org.ralasafe.user.User;
import org.ralasafe.util.Util;

public class UserComplexTableAdapter {
	private UserMetadata userMetadata;
	private ComplexTable complexTable;
	private String[] idNames = new String[] { User.idFieldName };
	private String idColumnName;

	private SingleValueTableAdapter[] singleValueTableAdapters;
	private MultiValueTableAdapter[] multiValueTableAdapters;

	private ComplexTableDeletorImpl deletor;
	private ComplexTableSaverImpl saver;
	private ComplexTableUpdatorImpl updator;
	private ComplexTableSelectorImpl selector;

	public UserComplexTableAdapter(UserMetadata userMetadata) {
		this.userMetadata = userMetadata;
		adapter();
		FieldMetadata[] fields = userMetadata.getMainTableMetadata()
				.getFields();
		for (int i = 0; i < fields.length; i++) {
			FieldMetadata field = fields[i];
			if (field.getName().equals(User.idFieldName)) {
				idColumnName = field.getColumnName();
			}
		}

		readySingleValueTableAdapter();
		readyMultiValueTableAdapter();

		readyDeletor();
		readySaver();
		readyUpdator();
		readySelector();
	}

	public ComplexTableDeletorImpl getDeletor() {
		return deletor;
	}

	public ComplexTableSaverImpl getSaver() {
		return saver;
	}

	public ComplexTableUpdatorImpl getUpdator() {
		return updator;
	}

	public ComplexTableSelectorImpl getSelector() {
		return selector;
	}

	private void readySelector() {
		selector = new ComplexTableSelectorImpl();
		selector.setComplexTable(complexTable);
		selector.setMainTableObjectNewer(new MapStorageObjectNewer());
		selector.setSingleValueAdapters(singleValueTableAdapters);
		selector.setMultiValueAdapters(multiValueTableAdapters);
	}

	private void readySingleValueTableAdapter() {
		singleValueTableAdapters = complexTable
				.getDefaultSingleValueTableAdapters();
		/**
		 * Table[] singleValueTables=complexTable.getSingleValueTables(); if(
		 * !Util.isEmpty( singleValueTables ) ) { singleValueTableAdapters=new
		 * DefaultSingleValueTableAdapter[singleValueTables.length]; for( int
		 * i=0; i<singleValueTables.length; i++ ) { Table
		 * table=singleValueTables[i]; String mapKey=table.getId()+"";
		 * 
		 * DefaultSingleValueTableAdapter adapter=new
		 * DefaultSingleValueTableAdapter( mapKey, idColumnName );
		 * singleValueTableAdapters[i]=adapter; } }
		 */
	}

	private void readyMultiValueTableAdapter() {
		multiValueTableAdapters = complexTable
				.getDefaultMultiValueTableAdapters();
		/**
		 * Table[] multiValueTables=complexTable.getMultiValueTables(); if(
		 * !Util.isEmpty( multiValueTables ) ) { multiValueTableAdapters=new
		 * DefaultMultiValueTableAdapter[multiValueTables.length]; for( int i=0;
		 * i<multiValueTables.length; i++ ) { Table table=multiValueTables[i];
		 * String mapKey=table.getId()+"";
		 * 
		 * DefaultMultiValueTableAdapter adapter=new
		 * DefaultMultiValueTableAdapter( mapKey, idColumnName );
		 * multiValueTableAdapters[i]=adapter; } }
		 */
	}

	private void readyUpdator() {
		updator = new ComplexTableUpdatorImpl();
		updator.setComplexTable(complexTable);
		updator.setSingleValueAdapters(singleValueTableAdapters);
		updator.setMultiValueAdapters(multiValueTableAdapters);
	}

	private void readySaver() {
		saver = new ComplexTableSaverImpl();
		saver.setComplexTable(complexTable);
		saver.setSingleValueAdapters(singleValueTableAdapters);
		saver.setMultiValueAdapters(multiValueTableAdapters);
	}

	private void readyDeletor() {
		deletor = new ComplexTableDeletorImpl();
		deletor.setComplexTable(complexTable);
	}

	public ComplexTable getComplexTable() {
		return complexTable;
	}

	public User toUser(MapStorgeObject mso) {
		if (mso == null)
			return null;

		User user = new User();
		readFromMainTable(mso, user);
		readFromSingleValueTables(mso, user);
		readFromMultiValueTables(mso, user);

		return user;
	}

	private void readFromMultiValueTables(MapStorgeObject mso, User user) {
		TableMetadata[] tableMds = userMetadata.getMultiValueTableMetadatas();
		if (Util.isEmpty(tableMds))
			return;

		Table[] tables = complexTable.getMultiValueTables();
		for (int tableIndex = 0; tableIndex < tableMds.length; tableIndex++) {
			TableMetadata tableMd = tableMds[tableIndex];
			FieldMetadata[] fields = tableMd.getFields();
			String mapKey = tables[tableIndex].getId() + "";

			List tableMsos = (List) mso.get(mapKey);
			if (tableMsos != null) {
				for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
					FieldMetadata field = fields[fieldIndex];
					String columnName = field.getColumnName();
					String name = field.getName();

					if (!name.equals(User.idFieldName)) {
						List values = new ArrayList(tableMsos.size());
						for (Iterator iter = tableMsos.iterator(); iter
								.hasNext();) {
							MapStorgeObject tableMso = (MapStorgeObject) iter
									.next();
							Object value = tableMso.get(columnName);
							values.add(value);
						}

						user.set(name, values);
					}// end of if
				}// end of fields for loop
			}// end of tableMsos if
		}
	}

	private void readFromSingleValueTables(MapStorgeObject mso, User user) {
		TableMetadata[] tableMds = userMetadata.getSingleValueTableMetadatas();
		if (Util.isEmpty(tableMds))
			return;

		Table[] tables = complexTable.getSingleValueTables();
		for (int tableIndex = 0; tableIndex < tableMds.length; tableIndex++) {
			TableMetadata tableMd = tableMds[tableIndex];
			FieldMetadata[] fields = tableMd.getFields();
			String mapKey = tables[tableIndex].getId() + "";

			MapStorgeObject tableMso = (MapStorgeObject) mso.get(mapKey);
			if (tableMso != null) {
				for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
					FieldMetadata field = fields[fieldIndex];
					String columnName = field.getColumnName();
					Object value = tableMso.get(columnName);

					String name = field.getName();
					if (!name.equals(User.idFieldName))
						user.set(name, value);
				}
			}
		}
	}

	private void readFromMainTable(MapStorgeObject mso, User user) {
		TableMetadata mainTableMetadata = userMetadata.getMainTableMetadata();
		FieldMetadata[] fields = mainTableMetadata.getFields();
		for (int i = 0; i < fields.length; i++) {
			String columnName = fields[i].getColumnName();
			Object value = mso.get(columnName);

			String name = fields[i].getName();
			user.set(name, value);
		}
	}

	public MapStorgeObject toMSO(User user) {
		if (user == null) {
			return null;
		}

		MapStorgeObject mso = new MapStorgeObject();

		setMainTableValues(user, mso);
		setSingleValueTableValues(user, mso);
		setMultiValueTableValues(user, mso);

		return mso;
	}

	private void setMultiValueTableValues(User user, MapStorgeObject mso) {
		TableMetadata[] tableMds = userMetadata.getMultiValueTableMetadatas();
		if (Util.isEmpty(tableMds)) {
			return;
		}

		Table[] tables = complexTable.getMultiValueTables();
		for (int tableIndex = 0; tableIndex < tableMds.length; tableIndex++) {
			TableMetadata tableMd = tableMds[tableIndex];
			FieldMetadata[] fields = tableMd.getFields();
			String mapKey = tables[tableIndex].getId() + "";

			Object idValue = mso.get(User.idFieldName);
			List[] values = new List[fields.length - 1];
			Iterator[] iters = new Iterator[values.length];
			String[] columnNames = new String[values.length];
			String idColumnName = "";

			int index = 0;
			int maxSize = 0;
			for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
				FieldMetadata field = fields[fieldIndex];
				String columnName = field.getColumnName();
				String name = field.getName();

				if (name.equals(User.idFieldName)) {
					idColumnName = columnName;
				} else {
					List fieldValues = (List) user.get(name);
					if (fieldValues == null) {
						fieldValues = new ArrayList(0);
					}

					values[index] = fieldValues;
					iters[index] = fieldValues.iterator();
					columnNames[index] = columnName;
					index++;

					int size = fieldValues.size();
					if (size > maxSize) {
						maxSize = size;
					}
				}
			}

			if (maxSize > 0) {
				List tableMsos = new ArrayList(maxSize);
				mso.put(mapKey, tableMsos);

				for (int msoIndex = 0; msoIndex < maxSize; msoIndex++) {
					MapStorgeObject tableMso = new MapStorgeObject();
					tableMsos.add(tableMso);

					tableMso.put(idColumnName, idValue);
					for (int iterIndex = 0; iterIndex < iters.length; iterIndex++) {
						Iterator iter = iters[iterIndex];
						if (iter.hasNext()) {
							tableMso.put(columnNames[iterIndex], iter.next());
						}/*
						 * else { tableMso.put( columnNames[i], null ); }
						 */
					}
				}
			}
		}
	}

	private void setSingleValueTableValues(User user, MapStorgeObject mso) {
		TableMetadata[] tableMds = userMetadata.getSingleValueTableMetadatas();
		if (Util.isEmpty(tableMds)) {
			return;
		}

		Table[] tables = complexTable.getSingleValueTables();
		for (int i = 0; i < tableMds.length; i++) {
			TableMetadata tableMd = tableMds[i];
			FieldMetadata[] fields = tableMd.getFields();
			String mapKey = tables[i].getId() + "";

			Object idValue = mso.get(User.idFieldName);
			String idColumnName = "";

			boolean noValue = true;
			MapStorgeObject tableMso = new MapStorgeObject();

			for (int j = 0; j < fields.length; j++) {
				FieldMetadata field = fields[j];
				String columnName = field.getColumnName();
				String name = field.getName();

				if (name.equals(User.idFieldName)) {
					idColumnName = columnName;
				} else {
					Object value = user.get(name);
					tableMso.put(columnName, value);

					if (noValue && value != null) {
						noValue = false;
					}
				}
			}

			if (!noValue) {
				tableMso.put(idColumnName, idValue);
				mso.put(mapKey, tableMso);
			}
		}
	}

	private void setMainTableValues(User user, MapStorgeObject mso) {
		TableMetadata table = userMetadata.getMainTableMetadata();
		FieldMetadata[] fields = table.getFields();
		for (int i = 0; i < fields.length; i++) {
			FieldMetadata field = fields[i];
			String columnName = field.getColumnName();
			String name = field.getName();

			Object value = user.get(name);
			mso.put(columnName, value);
		}
	}

	private ComplexTable adapter() {
		complexTable = new ComplexTable();

		TableMetadata mainTableMetadata = userMetadata.getMainTableMetadata();
		Table mainTable = adapter(mainTableMetadata, false);
		complexTable.setMainTable(mainTable);
		// complexTable.setIdColumns( mainTable.getIdColumns() );

		TableMetadata[] singleValueTableMetadatas = userMetadata
				.getSingleValueTableMetadatas();
		complexTable.setSingleValueTables(adapter(singleValueTableMetadatas,
				false));

		TableMetadata[] multiValueTableMetadatas = userMetadata
				.getMultiValueTableMetadatas();
		complexTable
				.setMultiValueTables(adapter(multiValueTableMetadatas, true));

		return complexTable;
	}

	private Table[] adapter(TableMetadata[] tablesMetadatas, boolean isMulti) {
		if (Util.isEmpty(tablesMetadatas))
			return null;

		Table[] tables = new Table[tablesMetadatas.length];
		for (int i = 0; i < tables.length; i++) {
			tables[i] = adapter(tablesMetadatas[i], isMulti);
		}

		return tables;
	}

	public Table adapter(TableMetadata table, boolean isMulti) {
		FieldMetadata[] fields = table.getFields();
		FieldMetadata[] uniqueFields = table.getUniqueFields();

		TableNewer tn = new TableNewer();
		String[] names = getColumnNames(fields);
		tn.setColumnNames(names);
		tn.setId(DBPower.getTableId(table.getDatasourceName(), table
				.getSqlTableName()));
		String idColumnName=null;
		for (int i = 0; i < fields.length; i++) {
			FieldMetadata metadata=fields[i];
			if( metadata.getName().equals( User.idFieldName ) ) {
				idColumnName=metadata.getColumnName();
				break;
			}
		}
		
		if (!isMulti) {
			tn.setIdColumnNames(new String[]{idColumnName});
		}
		tn.setTableName(table.getSqlTableName());
		tn.setUniqueColumnNames(getColumnNames(uniqueFields));

		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].getColumnName();
			tn.put(name, new MapStorgeColumnAdapter(name, fields[i]
					.getJavaType()));
		}

		Table table2 = tn.getTable();
		table2.setDatasourceName(table.getDatasourceName());
		return table2;
	}

	private String[] getColumnNames(FieldMetadata[] fields) {
		if (fields==null||Util.isEmpty(fields))
			return null;

		String[] names = new String[fields.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = fields[i].getColumnName();
		}

		return names;
	}
	
	public User javabeanToUser(MapStorgeObject mso) {
		if (mso == null)
			return null;

		User user = new User();
		user.setId( mso.get( User.idFieldName ) );
		FieldMetadata[] fields=userMetadata.getMainTableMetadata().getFields();
		for( int i=0; i<fields.length; i++ ) {
			String name=fields[i].getName();
			user.set( name, mso.get( name ) );
		}

		return user;
	}
}
