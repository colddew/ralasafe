/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

import org.ralasafe.ObjectNewer;

public class JavaBeanObjectNewer implements ObjectNewer {

	private Class clas;

	public JavaBeanObjectNewer(String className) {
		try {
			clas = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Object newObject() {
		try {
			return clas.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}