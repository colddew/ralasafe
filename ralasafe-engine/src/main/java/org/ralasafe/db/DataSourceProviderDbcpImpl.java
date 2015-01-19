/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * <code>DataSource</code> dbcp's implement.
 * 
 * @author back
 *
 */
public class DataSourceProviderDbcpImpl extends DataSource {
	private javax.sql.DataSource ds = null;
	private String name;
	private Properties prop;
	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name=name;
	}

	public void setup( Properties prop ) {
		this.prop=prop;

        GenericObjectPool.Config conPoolCfg = new GenericObjectPool.Config();
        conPoolCfg.maxActive = Integer.parseInt( prop.getProperty( "connectionPoolMaxSize", "15" ) );
        conPoolCfg.maxIdle = Integer.parseInt( prop.getProperty( "connectionPoolMaxIdle", "8" ) );
        conPoolCfg.maxWait = Integer.parseInt( prop.getProperty( "connectionPoolMaxWait", "60000" ) );
        conPoolCfg.minIdle = Integer.parseInt( prop.getProperty( "connectionPoolMinSize", "2" ) );


        ObjectPool connectionPool = new GenericObjectPool( null, conPoolCfg );
        try {
			Class.forName( prop.getProperty( "jdbcDriver" ) );
		} catch( ClassNotFoundException e ) {
			e.printStackTrace();
			throw new RuntimeException();
		}

        ConnectionFactory connectionFactory = new
            DriverManagerConnectionFactory( prop.getProperty( "jdbcUrl" ),
                                           prop.getProperty( "jdbcUser" ), 
                                           prop.getProperty( "jdbcPassword" ) );


        new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);

        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
        
        ds = dataSource;
	}

	public javax.sql.DataSource getDataSource() {
		return ds;
	}
	
	public String getValidInfo( Properties prop ) {
		this.prop=prop;
		if( prop.containsKey( "jdbcUrl" ) 
				&& prop.containsKey( "jdbcUser" )
				&& prop.containsKey( "jdbcPassword" )
				&& prop.containsKey( "jdbcDriver" ) ) {
			return null;
		}
		
		return "Required properties: jdbcUrl,jdbcUser,jdbcPassword,jdbcDriver";
	}
	public String toString() {
		String line=System.getProperty( "line.separator" );
		StringBuffer buff=new StringBuffer();
		buff.append( "PoweredBy DBCP [ jdbcDriver:" ).append( prop.getProperty( "jdbcDriver" ) ).append( line );
		buff.append( "               \tjdbcUrl:" ).append( prop.getProperty( "jdbcUrl" ) ).append( line );
		buff.append( "               \tjdbcUser:" ).append( prop.getProperty( "jdbcUser" ) ).append( "]" );
		
		return buff.toString();
	}
}
