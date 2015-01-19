/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.db.DBPower;
import org.ralasafe.db.DBView;
import org.ralasafe.db.TableView;

import com.google.gson.Gson;

public class UtilAction extends Action {
	private static final Log log=LogFactory.getLog( UtilAction.class );
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		if( "getJavaBeanProperties".equals( oper ) ) {
			getJavaBeanProperties( req, resp );
			return;
		} else if( "getAppDsNames".equals( oper ) ) {
			getAppDsNames( req, resp );
			return;
		} else if( "loadDbView".equals( oper ) ) {
			loadDbView( req, resp );
			return;
		} else if( "getTableColumns".equals( oper ) ) {
			getTableColumns( req, resp );
			return;
		} 
	}

	
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		if( "loadDbView".equals( oper ) ) {
			loadDbView( req, resp );
			return;
		} 
	}


	private void getTableColumns( HttpServletRequest req,
			HttpServletResponse resp ) throws IOException {
		// get table or view definition
		String schema=req.getParameter( "schema" );
		String tableName=req.getParameter( "tableName" );
		String dsName=req.getParameter( "dsName" );
		//String queryId=req.getParameter( "queryId" );
		String alias=req.getParameter( "alias" );
		
		TableView tableView=DBView.getTable( dsName, schema, tableName );
		
		responseTableDefinition( resp, tableView, alias );
	}
	
	private void responseTableDefinition( HttpServletResponse resp,
			TableView tableView, String alias ) throws IOException {
		Gson gson=new Gson();
		String json=gson.toJson( tableView );
		// append alias to json
		json=json.substring( 0, json.lastIndexOf('}') ) 
			+ ",\"alias\":\""+ alias + "\"}";
		
		if( log.isDebugEnabled() ) {
			log.debug( "The json is:" +json );
		}
		
		resp.setContentType("application/json;charset=UTF-8");   
		resp.setCharacterEncoding("UTF-8");   
		PrintWriter pw=resp.getWriter();   
		pw.write(json);   
		pw.flush();
	}
	
	private void getAppDsNames( HttpServletRequest req, HttpServletResponse resp ) throws IOException {
		Collection dsNames=DBPower.getDsNames();
		ArrayList appDsNames=new ArrayList( dsNames.size() );
		
		for( Iterator iterator=dsNames.iterator(); iterator.hasNext(); ) {
			String name=(String) iterator.next();
			if( !name.equals( "ralasafe" ) ) {
				appDsNames.add( name );
			}
		}
		
		Gson gson=new Gson();
		String json=gson.toJson( appDsNames );
		
		if( log.isDebugEnabled() ) {
			log.debug( "The json is:" +json );
		}
		
		resp.setContentType("application/json;charset=UTF-8");   
		resp.setCharacterEncoding("UTF-8");   
		PrintWriter pw=resp.getWriter();   
		//pw.write( "\"appDsNames\":" );
		pw.write(json);   
		pw.flush();
	}
	
	private void loadDbView( HttpServletRequest req, HttpServletResponse resp ) throws IOException {
		String dsName=req.getParameter( "dsName" );
		String[] allSchemas=DBView.getSchemas( dsName );
		String defaultSchema=DBView.getDefaultSchema( dsName );
		
		int id=0;
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter writer=resp.getWriter();
//		writer.write( "[                                                                           " );
//		writer.write( "	{id:0, name:'mydemo [Default]', nodes:[                              " );
//		writer.write( "		{id:1, name:'Tables', nodes:[                                      " );
//		writer.write( "			{id:2, name:'company'},               " );
//		writer.write( "			{id:3, name:'demouser'},              " );
//		writer.write( "			{id:4, name:'department'},            " );
//		writer.write( "			{id:5, name:'loan_money'},            " );
//		writer.write( "			{id:6, name:'ralasafe_sequence'}		]}" );
//		writer.write( "	]}                                                                         " );
//		writer.write( "]                                                                           " );
		writer.write( "[\r\n" );
		for( int i=0; i<allSchemas.length; i++ ) {
			String schema=allSchemas[i];
			boolean isDefault=defaultSchema.equals( schema );
			String[] tableNames=DBView.getTableNames( dsName, schema );
			String[] viewNames=DBView.getViewNames( dsName, schema );
			
			if( isDefault ) {
				schema=schema+" [Default]";
			}
			
			if( i!=0 ) {
				writer.write( ",\r\n" );
			}
			writer.write( "\t{id:" + id++ + ", iconSkin: 'schema', name:'" + schema + "', nodes:[\r\n" );
			
			// if it's default schema, keep schema attribute value as blank
			if( isDefault ) {
				schema="";
			}
			
			writer.write( "\t\t{id:" + id++ + ", iconSkin: 'tables', name:'Tables', nodes:[\r\n" );						
			id=write( writer, tableNames, schema, "table", id );
			writer.write( "\t\t]},\r\n" );
			
			writer.write( "\t\t{id:" + id++ + ", iconSkin: 'views', name:'Views', nodes:[\r\n" );						
			id=write( writer, viewNames, schema, "view", id );
			writer.write( "\t\t]}\r\n" );
			
			writer.write( "\t]}" );
		}
		writer.write( "]" );
		writer.flush();
	}
	
	private int write( PrintWriter writer, String[] names, String schema, String type, int id ) {
		for( int i=0; i<names.length; i++ ) {
			String name=names[i];
			
			if( i!=0 ) {
				writer.write( ",\r\n" );
			}
			writer.write( "\t\t\t{id:" + id++ + ", iconSkin: 'dbObject', name:'" + name 
					+ "', type:'" + type + "', schema:'" + schema + "'}" );
		}
		
		return id;
	}
	
	private void getJavaBeanProperties( HttpServletRequest req,
			HttpServletResponse resp ) throws IOException, ServletException {
		String clazz=req.getParameter( "clazz" );
		Class c;
		Properties prop=new Properties();
		try {
			c=Class.forName( clazz );
			Field[] fields=c.getDeclaredFields();
			int length=fields.length;
			String[] properties=new String[length];
			String[] javaTypes=new String[length];
			
			for( int i=0; i<fields.length; i++ ) {
				Field field=fields[i];
				properties[i]=field.getName();
				javaTypes[i]=field.getType().getName();
			}
			
			prop.setJavaTypes( javaTypes );
			prop.setProperties( properties );
		} catch( ClassNotFoundException e ) {
			prop.setErrorMsg( e.toString() );
		}
		
		Gson gson=new Gson();
		String json=gson.toJson( prop );

		if( log.isDebugEnabled() ) {
			log.debug( "The json is:" +json );
		}
		resp.setContentType("application/json;charset=UTF-8");   
		resp.setCharacterEncoding("UTF-8");   
		PrintWriter pw=resp.getWriter();   
		pw.write(json);   
		pw.flush();
	}
	
	class Properties {
		private String errorMsg="";
		private String[] properties;
		private String[] javaTypes;
		
		public String getErrorMsg() {
			return errorMsg;
		}
		public void setErrorMsg( String errorMsg ) {
			this.errorMsg=errorMsg;
		}
		public String[] getProperties() {
			return properties;
		}
		public void setProperties( String[] properties ) {
			this.properties=properties;
		}
		public String[] getJavaTypes() {
			return javaTypes;
		}
		public void setJavaTypes( String[] javaTypes ) {
			this.javaTypes=javaTypes;
		}
	}
}