/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.group;

import java.util.Collection;

public interface NodeManager {
 
	public abstract void addNode(Node node);
	public abstract void updateNode(Node node);
	public abstract void deleteNode(int nodeId);
	public abstract void deleteNodeCascade(int nodeId);
	public abstract Node getNode(int nodeId);
	public abstract Node getParentNode(Node node);
	public abstract Collection getChildrenNodes(Node pnode);
	public abstract boolean isChild(int pnodeId, int nodeId);
	public abstract boolean isCascadeChild(int pnodeId, int nodeId);
	public abstract void moveNode(Node node, Node targetNode);
	public abstract void moveNode(Collection nodes, Node targetNode);
	public abstract Node getRoot();
	public abstract Node getTree(int nodeId);
}