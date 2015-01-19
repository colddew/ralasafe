/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.xml.Column;
import org.ralasafe.db.sql.xml.Parameter;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.db.sql.xml.types.QueryTypeTypeType;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.util.StringUtil;

public class QueryRawAction extends Action {
	private static final Log log=LogFactory.getLog( QueryRawAction.class );
	
	public QueryRawHandler createHandler( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		QueryManager queryManager=WebUtil.getQueryManager( req );
		Query query=queryManager.getQuery( id );
		
		org.ralasafe.db.sql.xml.QueryType xmlQuery;
		try {
			xmlQuery = org.ralasafe.db.sql.xml.Query
					.unmarshal(new StringReader(query.getXmlContent()));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		
		return new QueryRawHandler( xmlQuery );
	}
	
	public String getHandlerAttributeKey( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		return "_$ralasafeQuery_" + id;
	}
	
	private QueryRawHandler getHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getHandlerAttributeKey( req );
		Object obj=req.getSession().getAttribute( key );
		QueryRawHandler handler=null;
		
		if( obj==null||"loadFresh".equals(oper) ) {
			handler=createHandler( req );
			
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		} else if( obj instanceof QueryDesignHandler ) {
			QueryDesignHandler designHandler=(QueryDesignHandler) obj;
			QueryType query=designHandler.getQuery();
			handler=new QueryRawHandler( query );
			req.getSession().setAttribute( key, handler );
			
			// set to raw sql type
			query.setIsRawSQL( true );
			query.setType( QueryTypeTypeType.SQL );
		}  else if( obj instanceof QueryTestHandler ) {
			QueryTestHandler rawHandler=(QueryTestHandler) obj;
			QueryType query=rawHandler.getQuery();
			handler=new QueryRawHandler( query );
			req.getSession().setAttribute( key, handler );
		} else {
			handler=(QueryRawHandler) obj;
		}
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		QueryRawHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() );
		}
		
		if( "rawSql".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/query/rawSql.jsp" );
		} else if( "param".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/query/rawParam.jsp" );
		} else if( "mapping".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/query/rawMapping.jsp" );
		} else if( "editParam".equals( oper ) ) {
			String strIndex=req.getParameter( "index" );
			if( !StringUtil.isEmpty( strIndex ) ) {
				int index=Integer.parseInt( strIndex );
				Parameter param=handler.getParameter( index );
				
				req.setAttribute( "param", param );
			} 
			WebUtil.forward( req, resp, "/ralasafe/query/editParam.jsp" );
		} else if( "editProperty".equals( oper ) ) {
			String strIndex=req.getParameter( "index" );
			if( !StringUtil.isEmpty( strIndex ) ) {
				int index=Integer.parseInt( strIndex );
				Column column=handler.getColumn( index );
				
				req.setAttribute( "column", column );
			} 
			
			String[][] mappingClassPropertyAndTypes=handler.getMappingClassPropertyAndTypes();
			req.setAttribute( "mappingClassPropertyAndTypes", mappingClassPropertyAndTypes );
			
			WebUtil.forward( req, resp, "/ralasafe/query/editProperty.jsp" );
		} else if( "return".equals( oper ) ) {
			String gotoPage=handler.getManagePage();
			
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			// goto manage page
			resp.sendRedirect( gotoPage );
			return;
		} else {
			WebUtil.forward( req, resp, "/ralasafe/query/raw.jsp" );
		}
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		QueryRawHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() );
		}		
		
		if( "addParam".equals( oper ) ) {
			String type=req.getParameter( "type" );
			String key=req.getParameter( "key" );
			
			handler.addParameter( type, key );
			return;
		} else if( "editParam".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			
			String type=req.getParameter( "type" );
			String key=req.getParameter( "key" );
			
			handler.updateParameter( index, type, key );
			return;
		} else if( "deleteParam".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			
			handler.deleteParameter( index );
			return;
		} else if( "moveParam".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			String direct=req.getParameter( "direct" );
			
			handler.moveParameter( direct, index );
			return;
		} else if( "addProperty".equals( oper ) ) {
			String property=req.getParameter( "property" );
			boolean readOnly=WebUtil.getBooleanParameter( req, "readOnly", false );
			String columnName=req.getParameter( "columnName" );
			
			int index=property.indexOf( "<" );
			String javaProp=property.substring( 0, index-1 ).trim();
			String javaType=property.substring( index+1, property.length()-1 ).trim();
			
			handler.addProperty( columnName, javaProp, javaType, readOnly );
			return;
		} else if( "editProperty".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			String property=req.getParameter( "property" );
			boolean readOnly=WebUtil.getBooleanParameter( req, "readOnly", false );
			String columnName=req.getParameter( "columnName" );
			
			int sindex=property.indexOf( "<" );
			String javaProp=property.substring( 0, sindex-1 ).trim();
			String javaType=property.substring( sindex+1, property.length()-1 ).trim();
			
			handler.updateProperty( index, columnName, javaProp, javaType, readOnly );
			return;
		} else if( "deleteProperty".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			
			handler.deleteProperty( index );
			return;
		} else if( "moveProperty".equals( oper ) ) {
			int index=WebUtil.getIntParameter( req, "index", -1 );
			String direct=req.getParameter( "direct" );
			
			handler.moveProperty( direct, index );
			return;
		} else if( "setMappingClass".equals( oper ) ) {
			String mappingClass=req.getParameter( "mappingClass" );
			handler.setMappingClass( mappingClass );
			return;
		} else if( "setRawSql".equals( oper ) ) {
			String rawSql=req.getParameter( "rawSql" );
			handler.setRawSql( rawSql );
			return;
		} else if( "save".equals( oper ) ) {
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			int id=WebUtil.getIntParameter( req, "id", -23 );
			QueryManager queryManager=WebUtil.getQueryManager( req );
			try {
				handler.save( id, queryManager );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			return;
		} 
	}
}
