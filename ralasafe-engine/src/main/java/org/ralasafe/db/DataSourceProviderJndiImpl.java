/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <code>DataSource</code> jndi's implement.
 */
public class DataSourceProviderJndiImpl extends DataSource {
	private String name;
	private javax.sql.DataSource dataSource;
	private Properties prop;
	
	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name=name;
	}
	public javax.sql.DataSource getDataSource() {
		return dataSource;
	}
	public void setup( Properties prop ) {
		this.prop=prop;
		String jndiName=prop.getProperty( "jndiName" );
		try {
			InitialContext ctx=new InitialContext();
			dataSource=(javax.sql.DataSource) ctx.lookup( jndiName );
		} catch( NamingException e ) {
			e.printStackTrace();
			throw new RuntimeException( e );
		}
	}
	public String getValidInfo( Properties prop ) {
		this.prop=prop;
		if( prop.containsKey( "jndiName" ) ) {
			return null;
		}
		return "Required properties: jndiName";
	}
	public String toString() {
		return "PoweredBy Jndi [ jndiName=" + prop.getProperty( "jndiName" ) + "]";
	}
}
