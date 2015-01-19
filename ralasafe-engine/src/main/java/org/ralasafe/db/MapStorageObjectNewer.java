/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.ObjectNewer;

public class MapStorageObjectNewer implements ObjectNewer {
	public Object newObject() {
		return new MapStorgeObject();
	}
}
