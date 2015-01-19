/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.ArrayList;

/**
 * Customized where condition for Ralasafe query API.
 *  
 */
public class CustomizedWhere {
	public static final String AND = "AND";
	public static final String OR = "OR";
	private ArrayList likeConditions = new ArrayList();
	private ArrayList equalConditions = new ArrayList();
	private ArrayList notEqualConditions = new ArrayList();
	private ArrayList lessThanConditions = new ArrayList();
	private ArrayList lessEqualConditions = new ArrayList();
	private ArrayList greaterThanConditions = new ArrayList();
	private ArrayList greaterEqualConditions = new ArrayList();
	private ArrayList orderByConditions = new ArrayList();
	private String linker = CustomizedWhere.AND;

	public boolean hasCondition() {
		return (likeConditions.size()>0 || equalConditions.size()>0
				|| notEqualConditions.size()>0 || lessThanConditions.size()>0
				|| lessEqualConditions.size()>0 || greaterThanConditions.size()>0
				|| greaterEqualConditions.size()>0
				|| orderByConditions.size()>0 );
	}

	protected ArrayList getLikeConditions() {
		return likeConditions;
	}

	protected ArrayList getEqualConditions() {
		return equalConditions;
	}

	protected ArrayList getNotEqualConditions() {
		return notEqualConditions;
	}

	protected ArrayList getLessThanConditions() {
		return lessThanConditions;
	}

	protected ArrayList getLessEqualConditions() {
		return lessEqualConditions;
	}

	protected ArrayList getGreaterThanConditions() {
		return greaterThanConditions;
	}

	protected ArrayList getGreaterEqualConditions() {
		return greaterEqualConditions;
	}
	
	protected ArrayList getOrderByConditions() {
		return orderByConditions;
	}
	
	protected String getLinker() {
		return linker;
	}

	/**
	 * ADD a like condition like: "property LIKE %value%"
	 * 
	 * @param property   your mapping javabean's propety
	 * @param value
	 */
	public void addLike(String property, Object value) {
		likeConditions.add(new LikeCondition(property, value));
	}

	/**
	 * Add condition like: "property = value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addEqual(String property, Object value) {
		equalConditions.add(new EqualCondition(property, value));
	}

	/**
	 * Add condition like: "property != value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addNotEqual(String property, Object value) {
		notEqualConditions.add(new NotEqualCondition(property, value));
	}

	/**
	 * Add condition like: "property < value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addLessThan(String property, Object value) {
		lessThanConditions.add(new LessThanCondition(property, value));
	}

	/**
	 * Add condition like: "property > value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addGreaterThan(String property, Object value) {
		greaterThanConditions.add(new GreaterThanCondition(property, value));
	}

	/**
	 * Add condition like: "property >= value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addGreaterEqual(String property, Object value) {
		greaterEqualConditions.add(new GreaterEqualCondition(property, value));
	}

	/**
	 * Add condition like: "property <= value"
	 * 
	 * @param property     your mapping javabean's propety 
	 * @param value
	 */
	public void addLessEqual(String property, Object value) {
		lessEqualConditions.add(new LessEqualCondition(property, value));
	}

	/**
	 * Add condition like: "order by property"
	 * @param property      your mapping javabean's propety 
	 */
	public void addAscOrderBy(String property) {
		orderByConditions.add( new OrderByCondition(property, "asc") );
	}
	
	/**
	 * Add condition like: "order by property desc"
	 * @param property      your mapping javabean's propety 
	 */
	public void addDescOrderBy(String property) {
		orderByConditions.add( new OrderByCondition(property, "desc") );
	}
	
	/**
	 * Set linker for conditions
	 * 
	 * @param linker     linker, value should be CustomizedWhere.AND or CustomizedWhere.OR.
	 *                   default value: CustomizedWhere.AND
	 */
	public void setLinker(String linker) {
		if (linker.equals(OR)) {
			this.linker = OR;
		} else {
			this.linker = AND;
		}
	}

	public class LikeCondition {
		private String property;
		private Object value;

		protected LikeCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class NotEqualCondition {
		private String property;
		private Object value;

		protected NotEqualCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class EqualCondition {
		private String property;
		private Object value;

		protected EqualCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class LessThanCondition {
		private String property;
		private Object value;

		protected LessThanCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class LessEqualCondition {
		private String property;
		private Object value;

		protected LessEqualCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class GreaterThanCondition {
		private String property;
		private Object value;

		protected GreaterThanCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}

	public class GreaterEqualCondition {
		private String property;
		private Object value;

		protected GreaterEqualCondition(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		protected Object getValue() {
			return value;
		}

		protected void setValue(Object value) {
			this.value = value;
		}
	}
	
	public class OrderByCondition {
		private String property;
		private String type;

		protected OrderByCondition(String property, String type) {
			this.property = property;
			this.type = type;
		}

		protected String getProperty() {
			return property;
		}

		protected void setProperty(String property) {
			this.property = property;
		}

		public String getType() {
			return type;
		}

		public void setType( String type ) {
			this.type=type;
		}
	}
}
