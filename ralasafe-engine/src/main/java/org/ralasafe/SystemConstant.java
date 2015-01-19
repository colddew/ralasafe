/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe;

public class SystemConstant {
	public static final String USER_KEY = "_$user";
	public static final String CONTEXT = "_$context";
	public static final String QUERY_MANAGER = "_$queryManager";
	public static final String SIMPLE_DATE_FORMAT = "_$simpleDateFormat";
	public static final String DOES_USER_CATEGORY_CONTAIN = "_$doesUserCategoryContain";
	public static final String IS_BUSINESS_DATA_VALID = "_$isBusinessDataValid";
	public static final String VARIABLE_MAP = "_$variableMap";
	/**
	 * Put business data into context with this key(, for ralasafe decision policy)
	 */
	public static final String BUSINESS_DATA = "_$businessData";

	private static String repositoryDir = "WebContent/WEB-INF/";
	private static String businessDataStoreDir = "WebContent/WEB-INF/ralasafe/businessData/";
	private static String userCategoryStoreDir = "WebContent/WEB-INF/ralasafe/userCategory/";
	private static String queryStoreDir = "WebContent/WEB-INF/ralasafe/query/";
	private static String userTypeStoreDir = "WebContent/WEB-INF/ralasafe/userType/";
	
	private static int queryLimit;
	
	public static int getQueryLimit() {
		return queryLimit;
	}

	public static void setQueryLimit( int queryLimit ) {
		SystemConstant.queryLimit=queryLimit;
	}

	public static String getRepositoryDir() {
		return repositoryDir;
	}

	public static void setRepositoryDir(String repositoryDir) {
		SystemConstant.repositoryDir = repositoryDir;
	}

	public static String getBusinessDataStoreDir() {
		return businessDataStoreDir;
	}

	public static void setBusinessDataStoreDir(String businessDataStoreDir) {
		SystemConstant.businessDataStoreDir = businessDataStoreDir;
	}

	public static String getUserCategoryStoreDir() {
		return userCategoryStoreDir;
	}

	public static void setUserCategoryStoreDir(String userCategoryStoreDir) {
		SystemConstant.userCategoryStoreDir = userCategoryStoreDir;
	}

	public static String getQueryStoreDir() {
		return queryStoreDir;
	}

	public static void setQueryStoreDir(String queryStoreDir) {
		SystemConstant.queryStoreDir = queryStoreDir;
	}

	public static String getUserTypeStoreDir() {
		return userTypeStoreDir;
	}

	public static void setUserTypeStoreDir(String userTypeStoreDir) {
		SystemConstant.userTypeStoreDir = userTypeStoreDir;
	}
}
