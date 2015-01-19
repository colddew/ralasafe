/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.ObjectNewer;

public interface SingleValueTableAdapter {
	//public void setComplexTable( ComplexTable table );
	public Object extract( Object o );
	public Object extractEvenNoValueExist( Object o );
	public void combine( Object o, Object extractValue );
	public ObjectNewer getObjectNewer();
}
