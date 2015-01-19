/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * <code>DataSource</code> a normal class/method implement. 
 * Get datasource through a method.
 */
public class DataSourceProviderMethodImpl extends DataSource {
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
		String clazz=prop.getProperty( "class" );
		String method=prop.getProperty( "method" );
		
		try {
			Class implClass=Class.forName( clazz );
			Method m=implClass.getMethod( method, new Class[]{});
			dataSource=(javax.sql.DataSource) m.invoke( implClass.newInstance(), new Object[]{} );
		} catch( Exception e ) {
			e.printStackTrace();
			throw new RuntimeException( e );
		}
	}
	
	public String getValidInfo( Properties prop ) {
		this.prop=prop;
		if( prop.containsKey( "class" ) 
				&& prop.containsKey( "method" ) ) {
			return null;
		} else {
			
			return "Required properties: class,method";
		}
	}
	
	public String toString() {
		return "PoweredBy JavaMethod [" + prop.getProperty( "class" )
			+ "." + prop.getProperty( "method" )+"();]";
	}
}
