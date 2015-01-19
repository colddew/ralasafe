/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.EntityExistException;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.group.Node;

public class QueryTreeHandler extends AbstractTreeHandler {
	private QueryManager manager;
	
	public QueryTreeHandler(QueryManager manager){
		this.manager=manager;
	}

	public String getCreatePage() {
		return "/ralasafe/query/create.jsp";
	}
	public String getPageTitle(){
		return "Query_Tree";
	}
	
	public String getModifyPage(){
		return "/ralasafe/query/modify.jsp";
	}
	
	public String getContextMenuPage(){
		return "/ralasafe/query/contextMenu.jsp";
	}
	
	public void addNode(Object node) throws EntityExistException{
		Query query=(Query)node;
		manager.addQuery(query.getPid(), query.getName(), query.getDescription(), query.getIsLeaf());
	
	}
	public Node getNode(int id){
		 return manager.getQuery(id);
	}
	public void deleteNode(int id){
		manager.deleteQuery(id);
	}
	public Node getNode(HttpServletRequest req){
		Query  query=new Query();
		int id=getId(req);
		int pid=getParentId(req);
		String name=req.getParameter("name");
		String desc=req.getParameter("description");
		boolean isLeaf=WebUtil.getBooleanParameter(req,"isLeaf", false);
		
		query.setId(id);
		query.setIsLeaf(isLeaf);
		query.setName(name);
		query.setPid(pid);
		query.setDescription(desc);
		return query;
	}
	
	public Node getNode( String name ) {
		Collection nodes=manager.getAllQueries();
		for( Iterator iter=nodes.iterator(); iter.hasNext(); ) {
			Query node=(Query) iter.next();
			if( node.getName().equals( name ) ) {
				return node;
			}
		}
		return null;
	}

	public Collection getAllNodes(){
		return manager.getAllQueries();
	}
	
	public String getNodeName(Node node ){
		Query query=(Query)node;
		return query.getName();		
	}
	
	public void moveNode(int parentId,int id,int position){
		manager.moveQuery(id, parentId);
	}
	
	public void updateNode(Object node)throws EntityExistException{
		Query db=(Query)node;
		manager.updateQuery(db.getId(),db.getName(),db.getDescription());
	}

	public String getTreeType() {
		return "query";
	}


	public void copyNode( Object node ) throws EntityExistException {
		Query query=(Query)node;
		manager.copyQuery( query.getId(), query.getName(), query.getDescription() );
	}
}
