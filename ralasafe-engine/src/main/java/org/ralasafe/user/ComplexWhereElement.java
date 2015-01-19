/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;

public class ComplexWhereElement implements WhereElement {
	private WhereElement firstPart;
	private LWhereElement[] linkedParts;
	
	public WhereElement getFirstPart() {
		return firstPart;
	}
	public void setFirstPart( WhereElement firstPart ) {
		this.firstPart = firstPart;
	}
	public LWhereElement[] getLinkedParts() {
		return linkedParts;
	}
	public void setLinkedParts( LWhereElement[] linkedParts ) {
		this.linkedParts = linkedParts;
	}
}
