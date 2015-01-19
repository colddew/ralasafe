/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.ContextValue;
import org.ralasafe.db.sql.Value;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.Query;
import org.ralasafe.entitle.QueryManager;

public class Util {
	private static final Log log=LogFactory.getLog( Util.class );
	private static final SimpleDateFormat sdf=new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
	private static final SimpleDateFormat shortSdf=new SimpleDateFormat( "yyyy-MM-dd" );
	// key/value=ClassName:fieldName/Method
	private static final Map javabeanGetMethodMap=new HashMap();
	
	public static boolean isEmpty(Object[] objs) {
		if (objs == null || objs.length == 0)
			return true;
		else
			return false;
	}

	public static int[] convert(String[] strs) {
		int[] result;
		if (strs == null) {
			result = new int[0];
		} else {
			result = new int[strs.length];
			for (int i = 0; i < strs.length; i++) {
				String s = strs[i];
				result[i] = Integer.parseInt(s);
			}
		}

		return result;
	}

	public static Collection convert2IntegerCollection(String[] strs) {
		Collection result = null;
		if (strs == null) {
			result = new ArrayList(0);
		} else {
			result = new ArrayList(strs.length);
			for (int i = 0; i < strs.length; i++) {
				// String s=strs[i];
				result.add(new Integer(strs[i]));
			}
		}

		return result;
	}

	public static Collection copy2Collection(Object[] strs) {
		Collection result = null;
		if (strs == null) {
			result = new ArrayList(0);
		} else {
			result = new ArrayList(strs.length);
			for (int i = 0; i < strs.length; i++) {
				// String s=strs[i];
				result.add(strs[i]);
			}
		}

		return result;
	}

	public static String getMessage(Locale locale, String key) {
		return ResourceBundle.getBundle("Ralasafe", locale).getString(key);
	}

	public static String getMessage(Locale locale, String key, String argument) {
		ResourceBundle rb= ResourceBundle.getBundle("Ralasafe");
		return MessageFormat.format(rb.getString(key), new Object[]{argument});
	}
	
	public static Query getTemplateQuery() {
		String s=
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+"<query name=\"\" ds=\"\""
			+"    isRawSQL=\"false\" type=\"sql\">"
			+"    <select isDistinct=\"false\" mappingClass=\"\"/>"
			+"    <from>"
			+"       <table schema=\"\" name=\"\" alias=\"\"/>"
			+"    </from>"
			+"    <where>"
			+"        <expressionGroup linker=\"AND\"/>"
			+"    </where>"
			+"    <groupBy/>"
			+"    <orderBy/>"
			+"    <rawSQL>"
			+"        <content xml:space=\"preserve\"><![CDATA[]]></content>"
			+"        <select isDistinct=\"false\"/>"
			+"    </rawSQL>"
			+"    <storedProcedure>"
			+"        <content></content>"
			+"        <select isDistinct=\"false\"/>"
			+"    </storedProcedure>"
			+"</query>";
		
		try {
			Query query=(Query) Query.unmarshal( new StringReader( s ) );
			query.getQueryTypeSequence().getFrom().removeAllTable();
			return query;
		} catch( MarshalException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		} catch( ValidationException e ) {
			log.error( "", e );
			throw new RalasafeException( e );
		}
	}

	public static boolean equals( Object o1, Object o2 ) {
		if( o1==null ) {
			return o2==null;
		} else {
			return o1.equals( o2 );
		}
	}

	public static Object parse( String rawValue, String fieldType ) throws ParseException {
		if( "java.lang.String".equals( fieldType ) ) {
			return rawValue;
		} else if( "int".equals( fieldType ) || "java.lang.Integer".equals( fieldType ) ) {
			return new Integer( rawValue );
		} else if( "java.lang.Double".equals( fieldType ) || "double".equals( fieldType ) ) {
			return new Double( rawValue );
		} else if( "java.lang.Boolean".equals( fieldType ) || "boolean".equals( fieldType ) ) {
			return new Boolean( rawValue );
		} else if( "java.lang.Float".equals( fieldType ) || "float".equals( fieldType ) ) {
			return new Float( rawValue );
		} else if( "java.util.Date".equals( fieldType ) ) {
			try {
				return Util.sdf.parseObject( rawValue );
			} catch( Exception e ) {
				return Util.shortSdf.parseObject( rawValue );
			}
		} else if( "java.sql.Date".equals( fieldType ) ) {
			java.util.Date utilDate=(java.util.Date) Util.sdf.parseObject( rawValue );
			return new java.sql.Date( utilDate.getTime() );
		}
		return null;
	}
	
	/**
	 * Reflect a javabean, return properties and javatypes.
	 * @param clazz
	 * @return [properties][javatypes]
	 * @throws ClassNotFoundException 
	 */
	public static String[][] reflectJavaBean( String clazz ) {
		Class c;
		try {
			c=Class.forName( clazz );
		} catch( ClassNotFoundException e ) {
			throw new RalasafeException( e );
		}
		
		List allFields=new LinkedList();
		allFields.addAll( Arrays.asList( c.getDeclaredFields() ) );
		while( !c.getSuperclass().equals( Object.class ) ) {
			Field[] declaredFields=c.getSuperclass().getDeclaredFields();
			allFields.addAll( Arrays.asList( declaredFields ) );
			c=c.getSuperclass();
		}
		
		String[][] result=new String[allFields.size()][2];
		for( int i=0; i<allFields.size(); i++ ) {
			Field f=(Field) allFields.get( i );
			String name=f.getName();
			String type=f.getType().getName();
			result[i]=new String[] { name, type };
		}
		return result;
	}
	
	/**
	 * Convert collection java bean datas into string array.
	 * @param javabeanData
	 * @param javabeanClass
	 * @param fields
	 * @return
	 */
	public static String[][] formatJavabeans( Collection javabeanData, 
			String javabeanClass, String[] fields ) {
		if( javabeanData==null || javabeanData.size()==0 ) {
			return new String[0][fields.length];
		}
		
		// prepare methods
		Method[] methods=new Method[fields.length];
		for( int j=0; j<fields.length; j++ ) {
			String field=fields[j];
			methods[j]=getJavabeanGetMethod( javabeanClass, field );
		}
		
		String[][] result=new String[javabeanData.size()][fields.length];
		int i=0; 
		
		for( Iterator iter=javabeanData.iterator(); iter.hasNext(); ) {
			Object obj=(Object) iter.next();
			
			for( int j=0; j<methods.length; j++ ) {
				Method method=methods[j];
				
				try {
					Object invoke=method.invoke( obj, null );
					
					if( invoke!=null ) {
						if( invoke instanceof Date ) {
							Date date=(Date) invoke;							
							result[i][j]=shortSdf.format( date );
						} else {
							result[i][j]=invoke.toString();
						}
					}
				} catch( Exception e ) {
					log.error( "", e );
					throw new RalasafeException( e );
				} 
			}
			
			i++;
		}
		
		return result;
	}

	public static Method getJavabeanGetMethod( String javabeanClass,
			String fieldName ) {
		String cacheKey=javabeanClass+"::"+fieldName;
		
		// get from cache
		Method method=(Method) javabeanGetMethodMap.get( cacheKey );
		
		if( method==null ) {
			// construct method
			Class c=null;
			try {
				c=Class.forName( javabeanClass );
			} catch( ClassNotFoundException e2 ) {
				log.error( "", e2 );
				throw new RalasafeException( e2 );
			}
			
			String refinedFieldName=fieldName.substring( 0,1 ).toUpperCase()+fieldName.substring( 1 );
			String methodName="get"+refinedFieldName;
			
			try {
				method=c.getMethod( methodName, null );
			} catch( SecurityException e ) {
				log.error( "", e );
				throw new RalasafeException( e );
			} catch( NoSuchMethodException e ) {
				// try isAbc method
				methodName="is"+refinedFieldName;
				
				try {
					method=c.getMethod( methodName, null );
				} catch( SecurityException e1 ) {
					log.error( "", e1 );
					throw new RalasafeException( e );
				} catch( NoSuchMethodException e1 ) {
					log.error( "", e1 );
					throw new RalasafeException( "get"+refinedFieldName+" or is"+refinedFieldName 
							+ " method not found in class " + javabeanClass );
				}
			}
			
			// cache method
			javabeanGetMethodMap.put( cacheKey, method );
		}
		
		return method;
	}

	public static Collection sub( Collection coll, int first, int size ) {
		if( coll==null ) {
			return null;
		}
		
		if( coll.size()<size&&first==0 ) {
			return coll;
		} else {
			List sub=new ArrayList(size);
			
			int i=-1;
			int fetchsize=0;
			for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
				Object object=(Object) iter.next();
				i++;
				if( i<first ) {
					continue;
				}
				
				
				sub.add( object );
				fetchsize++;
				
				if( fetchsize==size ) {
					break;
				}
			}
			
			return sub;
		}
	}
	
	public static Object get( Collection coll, int index ) {
		if( coll instanceof List ) {
			return ( (List) coll ).get( index );
		} else {
			int i=0; 
			for( Iterator iter=coll.iterator(); iter.hasNext(); ) {
				Object object=(Object) iter.next();
				if( i==index ) {
					return object;
				}
				
				i++;
			}
		}
		
		return null;
	}
	
	public static void extractContextValueFields( org.ralasafe.db.sql.Query query, Collection fields ) {
		ArrayList values=query.getValues();
		
		for( Iterator iter2=values.iterator(); iter2.hasNext(); ) {
			Value v=(Value) iter2.next();
			
			if( v instanceof ContextValue ) {
				ContextValue cv=(ContextValue) v;
				String key=cv.getKey();
				
				fields.add( key );
			}
		}
	}

	public static void extractContextValueFields( DefineVariable[] variables,
			QueryManager queryManager, Collection fields ) {
		for( int i=0; i<variables.length; i++ ) {
			DefineVariable var=variables[i];
						
			if( var.getContextValue()!=null ) {
				fields.add( var.getContextValue().getKey() );
			} else if( var.getQueryRef()!=null ) {
				int refId=var.getQueryRef().getId();
				
				org.ralasafe.entitle.Query query=queryManager.getQuery( refId );
				ArrayList values=query.getSqlQuery().getValues();
				
				for( Iterator iter=values.iterator(); iter.hasNext(); ) {
					Value v=(Value) iter.next();
					
					if( v instanceof org.ralasafe.db.sql.ContextValue ) {
						org.ralasafe.db.sql.ContextValue cv=(org.ralasafe.db.sql.ContextValue) v;
						String key=cv.getKey();
						
						fields.add( key );
					}
				}
			}
		}
	}

	public static void extractBusinessDataFields( DefineVariable[] variables,
			Collection fields ) {
		for( int i=0; i<variables.length; i++ ) {
			DefineVariable var=variables[i];
			
			if( var.getHintValue()!=null ) {
				fields.add( var.getHintValue().getKey() );
			}
		}
	}
}
