/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.group.Node;

public abstract class AbstractTreeAction extends Action {
	private static final Log log=LogFactory.getLog( AbstractTreeAction.class );
	
	public abstract AbstractTreeHandler createTreeHandler( HttpServletRequest req );
	
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractTreeHandler handler=createTreeHandler( req );
		req.setAttribute( "treeHandler", handler );
		
		if( "menu".equals( oper ) ) {
			// get current edit node
			int id=handler.getId( req );
			Object node=handler.getNode( id );
			req.setAttribute( "node", node );
			
			// goto context menu page
			WebUtil.forward( req, resp, handler.getContextMenuPage() );
		} else if( "preCreate".equals( oper ) ) {
			// goto preCreate page
			WebUtil.forward( req, resp, handler.getCreatePage() );
		} else if( "preCopy".equals( oper ) ) {
			int id=handler.getId( req );
			Object node=handler.getNode( id );
			req.setAttribute( "node", node );
			
			// goto preCopy page
			WebUtil.forward( req, resp, handler.getCopyPage() );
		} else if( "preModify".equals( oper ) ) {
			// get current edit node
			int id=handler.getId( req );
			Object node=handler.getNode( id );
			req.setAttribute( "node", node );
			
			// goto preModify page
			WebUtil.forward( req, resp, handler.getModifyPage() );
		} else if( "isNameValid".equals( oper ) ) { 
			String name=req.getParameter( "name" );
			Node node=handler.getNode( name );
			String sId=req.getParameter( "id" );
			
			boolean valid=false;
			if( node==null || ("n"+node.getId()).equals( sId ) ) {
				valid=true;
			}
			
			resp.setContentType("application/json;charset=UTF-8");
			PrintWriter writer=resp.getWriter();
			writer.write( valid+"" );			
			writer.flush();
		} else {
			// default: goto main jsp page
			WebUtil.forward( req, resp, handler.getTreePage() );
			return;
		}
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractTreeHandler handler=createTreeHandler( req );
		req.setAttribute( "treeHandler", handler );
		
		if( "add".equals( oper ) ) {
			Object node=handler.getNode( req );
			try {
				handler.addNode( node );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
		} else if( "copy".equals( oper ) ) {
			Object node=handler.getNode( req );
			try {
				handler.copyNode( node );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
		} else if( "delete".equals(  oper ) ) {
			int id=handler.getId( req );
			handler.deleteNode( id );
		} else if( "update".equals( oper ) ) {
			Object node=handler.getNode( req );
			try {
				handler.updateNode( node );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
		} else if( "move".equals( oper ) ) {
			int parentId=handler.getParentId( req );
			int id=handler.getId( req );
			int position=handler.getPosition( req );
			
			handler.moveNode( parentId, id, position );
		} else if( "loadTree".equals( oper ) ) {
			//print tree xml data
			String treeJson=handler.getTree();
			if( log.isDebugEnabled() ) {
				log.debug( "Json:" + treeJson );
			}
			
			resp.setContentType("application/json;charset=UTF-8");
			PrintWriter writer=resp.getWriter();
			writer.write( treeJson );			
			writer.flush();
		}
	}
}
