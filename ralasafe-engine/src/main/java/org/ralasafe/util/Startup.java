/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ralasafe.SystemConstant;
import org.ralasafe.db.DBPower;

public class Startup {
	private static boolean started=false;
	
	public static void startup( String datasourceDir, String repositoryDir ) {
		if( started ) {
			return;
		}
		
		innerStart( datasourceDir, repositoryDir );
		
		started=true;
	}

	private static void innerStart( String datasourceDir,
			String repositoryDir ) {
		datasourceDir=datasourceDir.trim();
		if( !datasourceDir.endsWith( "/" )
				&&!datasourceDir.endsWith( "\\" )) {
			datasourceDir=datasourceDir+"/";
		}
		
		if (StringUtil.isEmpty( repositoryDir )) {
			repositoryDir = datasourceDir+"xml/";
		}
		repositoryDir = repositoryDir.trim();
		if (!repositoryDir.endsWith("/")
				&&!repositoryDir.endsWith("\\")) {
			repositoryDir = repositoryDir + "/";
		}
		
		//PropertyConfigurator.configure(webinfoDir + "ralasafe/log4j.properties");
		Map map = new HashMap();
		map.put(DBPower.BASE_CONFIG_DIR_MAP_KEY, datasourceDir);
		map.put(DBPower.DATASOURCES_CONFIG_FILE_MAP_KEY, "datasources.xml");
		DBPower.on(map);

		String businessDataStoreDir = repositoryDir + "ralasafe/businessData/";
		String queryStoreDir = repositoryDir + "ralasafe/query/";
		String userCategoryStoreDir = repositoryDir + "ralasafe/userCategory/";
		String userTypeStoreDir = repositoryDir + "ralasafe/userType/";

		new File(businessDataStoreDir).mkdirs();
		new File(queryStoreDir).mkdirs();
		new File(userCategoryStoreDir).mkdirs();
		new File(userTypeStoreDir).mkdirs();

		SystemConstant.setRepositoryDir(repositoryDir);
		SystemConstant.setBusinessDataStoreDir(businessDataStoreDir);
		SystemConstant.setQueryStoreDir(queryStoreDir);
		SystemConstant.setUserCategoryStoreDir(userCategoryStoreDir);
		SystemConstant.setUserTypeStoreDir(userTypeStoreDir);
	}
}
