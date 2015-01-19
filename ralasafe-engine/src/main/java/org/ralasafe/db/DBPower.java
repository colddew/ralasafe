/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.util.DBUtil;
import org.ralasafe.util.IOUtil;
import org.ralasafe.util.StringUtil;

public class DBPower {
	private static Log log=LogFactory.getLog( DBPower.class );
	
	private static final String DATASOURCES_CONFIG_FILE = "ralasafe/datasources.xml";
	public static final String BASE_CONFIG_DIR_MAP_KEY = "baseConfigDir";
	public static final String DATASOURCES_CONFIG_FILE_MAP_KEY = "datasourcesConfigFile";
	private static final int DEFAULT_MAX_BATCH_SIZE = 50;
	private static int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;
	// private static DataSourceProviderDbcpImpl defaultDs=null;
	private static String defaultDsName = "ralasafe";
	private static String defaultAppDsName;
	/** key/value=datasourceName<String>/ds<DataSource> */
	private static Map dsnameMap = new HashMap();
	/** key/value=tableId<Integer>/DataSource */
	private static Map tableIdDsMap = new HashMap();
	/**
	 * key/value=datasourceName<String>/Map[key/value=tableName<String>/tableId<
	 * Integer>]
	 */
	private static Map dsTableNameMap = new HashMap();
	private static int tableIdIndex = 1;
	private static boolean started;

	public static boolean isStarted() {
		return started;
	}

	public static void on(Map map) {
		if (started) {
			return;
		}

		started = true;
		if (map == null) {
			map = new HashMap();
		}
		String configFile = (String) map.get(DATASOURCES_CONFIG_FILE_MAP_KEY);
		String baseDir = (String) map.get(BASE_CONFIG_DIR_MAP_KEY);
		if (baseDir == null) {
			baseDir = "";
		}
		if (configFile == null) {
			configFile = DATASOURCES_CONFIG_FILE;
		}
		configFile = baseDir + configFile;

		String line = System.getProperty("line.separator");
		System.out.println("**** Starting Ralasafe datasource ......");
		System.out.println("\t\tDirectory: " + baseDir);

		// startup datasource and regist into dsnameMap
		DataSourceHelper helper = new DataSourceHelper(configFile);
		List configs = helper.parseConfig();

		int count = configs.size();
		for (int i = 0; i < count; i++) {
			DataSourceConfig config = (DataSourceConfig) configs.get(i);
			Properties props = new Properties();
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(baseDir + config.getConfigFile());
				props.load(fis);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtil.close(fis);
			}

			DataSource ds = null;
			String implType = props.getProperty("type", "ralasafe");
			if (implType.equalsIgnoreCase("ralasafe")) {
				DataSourceProviderDbcpImpl impl = new DataSourceProviderDbcpImpl();

				String validInfo = impl.getValidInfo(props);

				StringBuffer buff = new StringBuffer();
				buff.append(config.toString()).append(line);

				if (validInfo == null) {
					impl.setup(props);
					impl.setName(config.getName());

					ds = impl;
				} else {
					buff.append("Error: " + validInfo).append(line);
				}

				buff.append(impl).append(line);
				System.out.println(buff.toString());
			} else if (implType.equalsIgnoreCase("jndi")) {
				DataSourceProviderJndiImpl impl = new DataSourceProviderJndiImpl();

				String validInfo = impl.getValidInfo(props);

				StringBuffer buff = new StringBuffer();
				buff.append(config.toString()).append(line);

				if (validInfo == null) {
					impl.setup(props);
					impl.setName(config.getName());

					ds = impl;
				} else {
					buff.append("Error: " + validInfo).append(line);
				}

				buff.append(impl).append(line);
				System.out.println(buff.toString());
			} else if (implType.equalsIgnoreCase("method")) {
				DataSourceProviderMethodImpl impl = new DataSourceProviderMethodImpl();

				String validInfo = impl.getValidInfo(props);

				StringBuffer buff = new StringBuffer();
				buff.append(config.toString()).append(line);

				if (validInfo == null) {
					impl.setup(props);
					impl.setName(config.getName());

					ds = impl;
				} else {
					buff.append("Error: " + validInfo).append(line);
				}

				buff.append(impl).append(line);
				System.out.println(buff.toString());
			} else {
				String msg="Can't setup data source! Unknown data source type:" + implType;
				log.error( msg );
				throw new RuntimeException( msg );
			}

			if (ds != null) {
				ds.setShowAllSchemas(config.isShowAllSchemas());
				ds.setSchemas(config.getSchemas());

				dsnameMap.put(config.getName(), ds);
			}
		}

		// find default app ds name (business ds name)
		for (int i = 0; i < count; i++) {
			DataSourceConfig config = (DataSourceConfig) configs.get(i);
			String name = config.getName();
			if (!name.equals("ralasafe")) {
				defaultAppDsName = name;
				break;
			}
		}

		if (!dsnameMap.containsKey("ralasafe")) {
			String str = "Error: no datasource with name=\"ralasafe\" found!";
			System.out.println(str);
			System.out.println("**** Ralasafe datasource failed!");

			throw new RuntimeException();
		}
		System.out.println("**** Ralasafe datasource started successfully!");
	}

	public static int getTableId(String dsName, String tableName) {
		if (StringUtil.isEmpty(dsName)) {
			dsName = defaultDsName;
		}
		Map tableNameMap = (Map) dsTableNameMap.get(dsName);
		if (tableNameMap == null) {
			tableNameMap = new HashMap();
			dsTableNameMap.put(dsName, tableNameMap);
		}
		Integer tableId = (Integer) tableNameMap.get(tableName);
		if (tableId == null) {
			tableId = new Integer(tableIdIndex);
			tableIdIndex++;
			tableNameMap.put(tableName, tableId);
			DataSource ds = (DataSource) dsnameMap.get(dsName);
			tableIdDsMap.put(tableId, ds);
		}
		return tableId.intValue();
	}

	/**
	 * 
	 * @param entityName
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(int tableId) throws DBLevelException {
		try {
			DataSource ds = (DataSource) tableIdDsMap.get(new Integer(tableId));
			if (ds == null) {
				String msg = "No datasource found in table with tableId="
						+ tableId + "";
				
				throw new RalasafeException(msg);
				// ds = (DataSource)dsnameMap.get( defaultDsName);
			}
			return ds.getDataSource().getConnection();
		} catch (SQLException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		}
	}

	public static String getDatasourceName(int tableId) {
		DataSource ds = (DataSource) tableIdDsMap.get(new Integer(tableId));
		return ds.getName();
	}

	public static Connection[] getConnections(int[] tableIds)
			throws DBLevelException {
		Connection[] conns = new Connection[tableIds.length];
		Map dsNameConnIndexMap = new HashMap();
		try {
			for (int i = 0; i < tableIds.length; i++) {
				int tableId = tableIds[i];
				DataSource ds = (DataSource) tableIdDsMap.get(new Integer(
						tableId));
				String name = ds.getName();
				if (dsNameConnIndexMap.containsKey(name)) {
					int previousIndex = ((Integer) dsNameConnIndexMap.get(name))
							.intValue();
					conns[i] = conns[previousIndex];
				} else {
					conns[i] = ds.getDataSource().getConnection();
					dsNameConnIndexMap.put(name, new Integer(i));
				}
			}
		} catch (SQLException e) {
			dsNameConnIndexMap.clear();
			for (int i = 0; i < conns.length; i++) {
				Connection conn = conns[i];
				DBUtil.close(conn);
			}
			throw new DBLevelException(e);
		}
		return conns;
	}

	public static Connection[] getUniqueConnections(int[] tableIds)
			throws DBLevelException {
		Connection[] conns = new Connection[tableIds.length];
		try {
			for (int i = 0; i < tableIds.length; i++) {
				int tableId = tableIds[i];
				DataSource ds = (DataSource) tableIdDsMap.get(new Integer(
						tableId));
				String name = ds.getName();
				conns[i] = ds.getDataSource().getConnection();
			}
		} catch (SQLException e) {
			for (int i = 0; i < conns.length; i++) {
				Connection conn = conns[i];
				DBUtil.close(conn);
			}
			throw new DBLevelException(e);
		}
		return conns;
	}

	public static void setMaxBatchSize(int maxBatchSize) {
		DBPower.maxBatchSize = maxBatchSize;
	}

	public static int getMaxBatchSize() {
		return maxBatchSize;
	}

	public static Connection getConnection(String dsName) {
		if (dsName == null) {
			dsName = defaultDsName;
		}
		DataSource ds = (DataSource) dsnameMap.get(dsName);

		if (ds == null) {
			String msg = "Can't get connection. Cause: datasource with name=" + dsName + " not found.";
			log.error( msg );
			throw new RalasafeException(msg);
		}

		try {
			return ds.getDataSource().getConnection();
		} catch (SQLException e) {
			log.error( "", e );
			throw new DBLevelException(e);
		}
	}

	public static Collection getDsNames() {
		return dsnameMap.keySet();
	}
	
	/**
	 * Return default application datasource name. If many application datasource exist, return the first one
	 * which it not named with 'ralasafe'. 
	 * If no application datasource exist, return 'ralasafe'.
	 * 
	 * @return
	 */
	public static String getDefaultAppDsName() {
		return defaultAppDsName == null ? defaultDsName : defaultAppDsName;
	}

	public static String getDefaultDsName() {
		return defaultDsName;
	}

	public static DataSource getDataSource(String dsName) {
		return (DataSource) dsnameMap.get(dsName);
	}
}
