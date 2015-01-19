/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.EntityExistException;

public interface ComplexTableUpdator {
	public void setComplexTable( ComplexTable complexTable );
	public void setSingleValueAdapters( SingleValueTableAdapter[] adapters );
	public void setMultiValueAdapters( MultiValueTableAdapter[] adapters );
	public void update( Object o ) throws EntityExistException, DBLevelException;
}
