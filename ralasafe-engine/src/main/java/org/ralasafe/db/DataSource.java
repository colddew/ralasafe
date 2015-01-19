/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Properties;

/**
 * A datasource.
 * A datasource can be startup, get connection.
 * Schema infos can be read too.
 * 
 */
public abstract class DataSource {
	private boolean showAllSchemas;
	private String[] schemas;
	
	public abstract String getName();

	public abstract void setName( String name );

	public abstract void setup( Properties prop );

	public abstract javax.sql.DataSource getDataSource();
	
	/**
	 * Valid the setup properties, if it's ok, return null, else return error infos.
	 * @return
	 */
	public abstract String getValidInfo( Properties prop );
	
	/**
	 * Only these schemas' info is loaded for fast design web page loading.
	 * Can be configed in WEB-INF/ralasafe/datasource.xml.
	 * @return
	 */
	public String[] getSchemas() {
		return schemas;
	}

	/**
	 * Does display all schemas?
	 *  
	 * <p>
	 * In datasource.xml, if schema attribute is not configed or values=ALL/all/*, 
	 * it means display all schemas.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isShowAllSchemas() {
		return showAllSchemas;
	}

	public void setShowAllSchemas( boolean showAllSchemas ) {
		this.showAllSchemas=showAllSchemas;
	}

	public void setSchemas( String[] schemas ) {
		this.schemas=schemas;
	}
}