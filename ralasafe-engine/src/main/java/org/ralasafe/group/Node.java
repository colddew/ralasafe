/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class Node {
 
	private int id;
	private int pid;
	 
	/**
	 * 0 -> false
	 * 1 -> true
	 */
	private boolean isLeaf;
	
	private Node parent;
	
	private Collection children = new ArrayList();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Collection getChildren() {
		return children;
	}

	public void setChildren(Collection children) {
		this.children = children;
	}

	public boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * Get all children nodes cascade, exclude this node.
	 * @return
	 */
	public Collection getAllChildrenNodeIds() {
		Collection nodeIds=new LinkedList();
		if( children!=null ) {
			for( Iterator iter=children.iterator(); iter.hasNext(); ) {
				Node child=(Node) iter.next();
				nodeIds.add( new Integer( child.getId() ) );
				getAllChildrenNodeIds( nodeIds, child );
			}
		}
		
		return nodeIds;
	}

	private void getAllChildrenNodeIds( Collection nodeIds, Node node ) {
		Collection thisNodeChildren=node.getChildren();
		if( thisNodeChildren!=null ) {
			for( Iterator iter=thisNodeChildren.iterator(); iter.hasNext(); ) {
				Node child=(Node) iter.next();
				nodeIds.add( new Integer( child.getId() ) );
				getAllChildrenNodeIds( nodeIds, child );
			}
		}
	}
}
 
