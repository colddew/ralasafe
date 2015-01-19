/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The user class. A user may contains many attributes of any types( Integer, Float, String, Date, SomeYourClass ).
 * A user must contains attribute named "id", which is the identity of user.
 * 
 * <p>
 * Generally, we write config file usermetadata.xml, then install it through url: $yourWebapp/ralasafe/userTypeMng. 
 * </p>
 * 
 */
public class User {
	public static final String idFieldName = "id";
	private Map storage = new HashMap();

	public User() {

	}

	public User(Map storage) {
		this.storage = storage;
	}

	/**
	 * Set user id value.
	 * 
	 * @param id
	 */
	public void setId(Object id) {
		storage.put(idFieldName, id);
	}

	/**
	 * Get user id value.
	 * 
	 * @return 
	 */
	public Object getId() {
		return storage.get(idFieldName);
	}

	/**
	 * Set user attribute's value.
	 * 
	 * @param field    attribute's name
	 * @param value    attribute's value
	 */
	public void set(String field, Object value) {
		storage.put(field, value);
	}

	/**
	 * Get user attribute's value
	 * 
	 * @param field    attribute's name
	 * @return         attribute's value
	 */
	public Object get(String field) {
		return storage.get(field);
	}

	/**
	 * Does's user contain this attribute?
	 * 
	 * @param field       attribute's name
	 * @return   true/false
	 */
	public boolean containsField(String field) {
		return storage.containsKey(field);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		User another = (User) obj;
		if (storage.size() != another.storage.size()) {
			return false;
		}
		for (Iterator iter = storage.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			Object anotherValue = another.storage.get(key);
			if (value == null) {
				if (anotherValue != null) {
					return false;
				}
			} else {
				if (value instanceof List) {
					if (!equals((List) value, anotherValue)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean equals(List list1, Object o) {
		List list2 = (List) o;
		if (list1.size() != list2.size()) {
			return false;
		}
		for (int i = 0, size = list2.size(); i < size; i++) {
			Object o1 = list1.get(i);
			Object o2 = list2.get(i);
			if (o1 == null) {
				if (o2 != null) {
					return false;
				}
			} else if (!o1.equals(o2)) {
				return false;
			}
		}
		return true;
	}
}
