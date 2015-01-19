/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.EntityExistException;
import org.ralasafe.entitle.UserCategory;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.group.Node;

public class UserCategoryTreeHandler extends AbstractTreeHandler {	
	private final UserCategoryManager manager;

	public UserCategoryTreeHandler( UserCategoryManager manager ) {
		this.manager=manager;
	}

	public String getCreatePage() {
		return "/ralasafe/userCategory/create.jsp";
	}

	public String getPageTitle() {
		return "User_Category_Tree";
	}

	public String getModifyPage() {
		return "/ralasafe/userCategory/modify.jsp";
	}

	public String getContextMenuPage() {
		return "/ralasafe/userCategory/contextMenu.jsp";
	}
	
	public void addNode( Object node ) throws EntityExistException {
		UserCategory uc=(UserCategory) node;
		manager.addUserCategory( uc.getPid(), uc.getName(), uc.getDescription(), uc.getIsLeaf() );
	}
	
	public Node getNode( int id ) {
		return manager.getUserCategory( id );
	}

	public void deleteNode( int id ) {
		manager.deleteUserCategory( id );
	}

	public Node getNode( HttpServletRequest req ) {
		UserCategory uc=new UserCategory();
		int id=getId( req );
		int pid=getParentId( req );
		String name=req.getParameter( "name" );
		String desc=req.getParameter( "description" );
		boolean isLeaf=WebUtil.getBooleanParameter( req, "isLeaf", false );
		
		uc.setId( id );
		uc.setIsLeaf( isLeaf );
		uc.setName( name );
		uc.setPid( pid );
		uc.setDescription( desc );
		return uc;
	}

	public Node getNode( String name ) {
		Collection nodes=manager.getAllUserCategories();
		for( Iterator iter=nodes.iterator(); iter.hasNext(); ) {
			UserCategory node=(UserCategory) iter.next();
			if( node.getName().equals( name ) ) {
				return node;
			}
		}
		return null;
	}

	public Collection getAllNodes() {
		return manager.getAllUserCategories();
	}

	public String getNodeName( Node node ) {
		UserCategory uc=(UserCategory) node;
		return uc.getName();
	}

	public void moveNode( int parentId, int id, int position ) {
		manager.moveUserCategory( id, parentId );
	}

	public void updateNode( Object node ) throws EntityExistException {
		UserCategory uc=(UserCategory) node;
		manager.updateUserCategory( uc.getId(), uc.getName(), uc.getDescription() );
	}

	public String getTreeType() {
		return "userCategory";
	}

	public void copyNode( Object node ) throws EntityExistException {
		UserCategory uc=(UserCategory) node;
		manager.copyUserCategory( uc.getId(), uc.getName(), uc.getDescription() );
	}	
}
