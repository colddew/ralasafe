/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import java.util.Collection;

public interface ComplexTableSelector {
	public void setComplexTable( ComplexTable complexTable );
	public void setSingleValueAdapters( SingleValueTableAdapter[] adapters );
	public void setMultiValueAdapters( MultiValueTableAdapter[] adapters );
	public Object selectByIdColumns( Object o ) throws DBLevelException;
	public Object selectByUniqueColumns( Object o ) throws DBLevelException;
	public Collection select( SelectCondition cdtn, Object hint ) throws DBLevelException;
	public Collection selectByPage( SelectCondition cdtn, 
			Object hint,
			int startIndex, int pageSize ) throws DBLevelException;
	
	public int selectCount() throws DBLevelException;
	public int selectCount( SelectCondition cdtn, Object hint ) throws DBLevelException;
}
