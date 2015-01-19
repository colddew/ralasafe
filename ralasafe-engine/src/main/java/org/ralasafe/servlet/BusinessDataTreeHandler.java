/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import org.ralasafe.EntityExistException;
import org.ralasafe.entitle.BusinessData;
import org.ralasafe.entitle.BusinessDataManager;
import org.ralasafe.group.Node;
public class BusinessDataTreeHandler extends AbstractTreeHandler {
	private final BusinessDataManager manager;

	public BusinessDataTreeHandler(BusinessDataManager manager) {
		this.manager = manager;
	}

	public String getCreatePage() {
		return "/ralasafe/businessData/create.jsp";
	}
	public String getPageTitle(){
		return "Business_Data_Tree";
	}
	
	public String getModifyPage(){
		return "/ralasafe/businessData/modify.jsp";
	}
	
	public String getContextMenuPage(){
		return "/ralasafe/businessData/contextMenu.jsp";
	}
	
	public void addNode(Object node) throws EntityExistException{
		BusinessData bd=(BusinessData)node;
		manager.addBusinessData(bd.getPid(), bd.getName(), bd.getDescription(), bd.getIsLeaf());
	
	}
	public Node getNode(int id){
		 return manager.getBusinessData(id);
	}
	public void deleteNode(int id){
		manager.deleteBusinessData(id);
	}
	public Node getNode(HttpServletRequest req){
		BusinessData bd=new BusinessData();
		int id=getId(req);
		int pid=getParentId(req);
		String name=req.getParameter("name");
		String desc=req.getParameter("description");
		boolean isLeaf=WebUtil.getBooleanParameter(req,"isLeaf", false);
		
		bd.setId(id);
		bd.setIsLeaf(isLeaf);
		bd.setName(name);
		bd.setPid(pid);
		bd.setDescription(desc);
		return bd;
	}
	
	public Node getNode( String name ) {
		Collection data=manager.getAllBusinessData();
		for( Iterator iter=data.iterator(); iter.hasNext(); ) {
			BusinessData bizData=(BusinessData) iter.next();
			if( bizData.getName().equals( name ) ) {
				return bizData;
			}
		}
		
		return null;
	}

	public Collection getAllNodes(){
		return manager.getAllBusinessData();
	}
	
	public String getNodeName(Node node ){
		BusinessData bd=(BusinessData)node;
		return bd.getName();		
	}
	
	public void moveNode(int parentId,int id,int position){
		manager.moveBusinessData(id, parentId);
	}
	
	public void updateNode(Object node)throws EntityExistException{
		BusinessData bd=(BusinessData)node;
		manager.updateBusinessData(bd.getId(),bd.getName(),bd.getDescription());
	}

	public String getTreeType() {
		return "businessData";
	}

	public void copyNode( Object node ) throws EntityExistException {
		BusinessData bd=(BusinessData)node;
		manager.copyBusinessData( bd.getId(), bd.getName(), bd.getDescription() );
	}
}
