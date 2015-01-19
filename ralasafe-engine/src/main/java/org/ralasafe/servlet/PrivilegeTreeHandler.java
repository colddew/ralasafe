/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.EntityExistException;
import org.ralasafe.group.Node;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;

public class PrivilegeTreeHandler extends AbstractTreeHandler {
	protected final PrivilegeManager manager;
	//private final Privilege rootPvlg=new Privilege();
	
	public PrivilegeTreeHandler(PrivilegeManager manager) {
		this.manager = manager;
//		rootPvlg.setId( 0 );
//		rootPvlg.setPid( -333 );
//		rootPvlg.setName( "Root" );
//		rootPvlg.setType( 0 );
	}

	public String getCreatePage() {
		return "/ralasafe/privilege/create.jsp";
	}
	public String getPageTitle(){
		return "Privilege_Tree";
	}
	
	public String getModifyPage(){
		return "/ralasafe/privilege/modify.jsp";
	}
	
	public String getContextMenuPage(){
		return "/ralasafe/privilege/contextMenu.jsp";
	}
	
	public void addNode(Object node) throws EntityExistException{
		Privilege pvlg=(Privilege)node;
		manager.addPrivilege(pvlg);
	
	}
	public Node getNode(int id){
		 return manager.getPrivilege(id);
	}
	public void deleteNode(int id){
		manager.deletePrivilegeCascade( id );
	}
	public Node getNode(HttpServletRequest req){ 
		Privilege pvlg=new Privilege();
		int id=getId(req);
		int pid=getParentId(req);
		String name=req.getParameter("name");
		String desc=req.getParameter("description");
		boolean isLeaf=WebUtil.getBooleanParameter(req,"isLeaf", false); 
		String constantName = req.getParameter("constantName");
		String target = req.getParameter("target");
		String url = req.getParameter("url"); 
		boolean display=WebUtil.getBooleanParameter( req, "display", true );
        pvlg.setConstantName(constantName);
		pvlg.setTarget(target);
		pvlg.setUrl(url);
		pvlg.setId(id);
		pvlg.setIsLeaf(isLeaf);
		pvlg.setDisplay(display);
		pvlg.setName(name);
		pvlg.setPid(pid);
		pvlg.setDescription(desc);
		return pvlg; 
	}
	
	public Node getNode( String name ) {
		Collection nodes=manager.getAllBusinessPrivileges();
		for( Iterator iter=nodes.iterator(); iter.hasNext(); ) {
			Privilege node=(Privilege) iter.next();
			if( node.getName().equals( name ) ) {
				return node;
			}
		}
		
		nodes=manager.getAllNonRolePrivileges();
		for( Iterator iter=nodes.iterator(); iter.hasNext(); ) {
			Privilege node=(Privilege) iter.next();
			if( node.getName().equals( name ) ) {
				return node;
			}
		}
		return null;
	}

	public Collection getAllNodes(){
		Collection pvlgs=manager.getAllBusinessPrivileges();
		List allPvlgs=new ArrayList(pvlgs.size()+1);
		//allPvlgs.add( rootPvlg );
		allPvlgs.addAll( pvlgs );
		return allPvlgs;
	}
	
	public String getNodeName(Node node ){
		Privilege pvlg=(Privilege)node;
		return pvlg.getName();		
	}
	
	public void moveNode(int targetId,int nodeId,int position){
		Privilege node=manager.getPrivilege( nodeId );
		Privilege targetNode=manager.getPrivilege( targetId );
		manager.movePrivilege(node, targetNode,position);
	}
	
	public void updateNode(Object node)throws EntityExistException{
		Privilege db=(Privilege)node;
		manager.updatePrivilege(db);
	}

	public String getTreeType() {
		return "privilege";
	}
	
	public void copyNode( Object node ) throws EntityExistException {
		// privilege doesn't support copy
		throw new UnsupportedOperationException();
	}
}