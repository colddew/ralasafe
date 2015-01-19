/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public interface ComplexTableDeletor {
	public void setComplexTable( ComplexTable complexTable );
	public void delete( Object o ) throws DBLevelException;
}
