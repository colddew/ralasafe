/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.EntityExistException;
import org.ralasafe.group.Node;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.util.StringUtil;

public abstract class AbstractTreeHandler {
	public static final String DEFAULT_TREE_MAIN_PAGE="/ralasafe/common/treeTemplate.jsp";
	public static final String DEFAULT_COPY_PAGE="/ralasafe/common/copyNode.jsp";
	
	private String treeUrl=DEFAULT_TREE_MAIN_PAGE;
	
	public abstract String getPageTitle();
	public abstract String getCreatePage();
	
	public String getTreePage() {
		return treeUrl;
	}

	public void setTreePage( String treeJsp ) {
		this.treeUrl=treeJsp;
	}
	
	public abstract String getNodeName( Node node );
	public abstract Collection getAllNodes();
	
	public String getTree() {
		StringBuffer buff=new StringBuffer(); 
		buff.append( "[" ); 
		Collection coll=getAllNodes();
		List list=new ArrayList();
		list.addAll( coll );
		if( getTreeType().equals( "privilege" ) ) {
			Collections.sort( list, new Comparator() {
				public int compare( Object arg0, Object arg1 ) {
					Privilege n0=(Privilege) arg0;
					Privilege n1=(Privilege) arg1;
					return n0.getOrderNum()-n1.getOrderNum();
				}
			}); 
		}
		int i=0;
		for( Iterator iter=list.iterator(); iter.hasNext(); ) {
			Node node=(Node ) iter.next(); 
			if( i>0 ) {
				buff.append( ",\r\n" );
			} 
			buff.append( "{id:'" )
				.append( node.getId() )
				.append( "',name:\"" )
				.append( getNodeName( node ) )
				.append( "\",pId:'" )
				.append( node.getPid() )
				.append( "'" )
				.append( ",isLeaf:'" )
				.append( node.getIsLeaf()?"1":"0" )
				.append( "'" )
				.append( node.getIsLeaf()?"":",type:'group',iconSkin:'group'" )
				.append( "}"); 
			i++;
		} 
		buff.append( "]" ); 
		return buff.toString();
	}

	public abstract Node getNode( HttpServletRequest req );
	public abstract Node getNode( String name );
	
	public abstract void addNode( Object node ) throws EntityExistException;

	public int getId( HttpServletRequest req ) {
		// id values are n0, n1...
		String strId=req.getParameter( "id" );
		if( StringUtil.isEmpty( strId ) ) {
			return 0;
		} else {
			return Integer.parseInt( strId.substring( 1 ) );
		}
	}

	public abstract void deleteNode( int id );
	
	public abstract void updateNode( Object node ) throws EntityExistException;

	public int getPosition( HttpServletRequest req ) {
		return WebUtil.getIntParameter( req, "position", 0 );
	}

	public int getParentId( HttpServletRequest req ) {
		// parentId values are n0, n1...
		String strId=req.getParameter( "parentId" );
		if( StringUtil.isEmpty( strId ) ) {
			return 0;
		} else {
			return Integer.parseInt( strId.substring( 1 ) );
		}
	}

	public abstract void moveNode( int parentId, int id, int position );

	public abstract String getContextMenuPage();

	public abstract Node getNode( int id );
	public abstract String getModifyPage();
	public abstract String getTreeType();
	

 	public  void movePrivilege(HttpServletRequest req){}
	public String getCopyPage() {
		return DEFAULT_COPY_PAGE;
	}
	public abstract void copyNode( Object node ) throws EntityExistException;
}
