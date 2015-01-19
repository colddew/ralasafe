/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import org.ralasafe.SystemConstant;

public class HintValue extends DefineVariable {
	private String hint;
	private String key;

	public String getHint() {
		return hint;
	}

	public void setHint( String hint ) {
		this.hint=hint;
	}

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key=key;
	}

	/**
	 * Script likes:
	 * 	Object v=null;
	 *  Object hintObj = context.get("hint");
	 *	if(hintObj instanceof hintObj org.ralasafe.db.MapStorgeObject){
	 *		v = ((org.ralasafe.db.MapStorgeObject)hintObj).get("key");
	 *	}else{
	 *		//java bean
	 *		Class clazz=hintObj.getClass();
	 *		String getterStr="get"+key.substring( 0, 1 ).toUpperCase()+key.substring( 1 );
	 *		java.lang.reflect.Method getter=clazz.getMethod( getterStr, new Class[] {} );
	 *		v=getter.invoke( o, new Object[] {} );
	 *	}
	 *
	 */
	public String toScript() {
		String v=getVariableName();
		String context=SystemConstant.CONTEXT;
		String hintObj="hintObj_" + getVariableName();
		StringBuffer buff=new StringBuffer();
		buff.append( "Object "+v+"=null; \n" );
		buff.append( "Object hintObj = "+context+".get(\""+hint+"\"); \n" );
		buff.append( "if(hintObj instanceof org.ralasafe.db.MapStorgeObject){ \n" );
		buff.append( "	"+v+"=((org.ralasafe.db.MapStorgeObject)hintObj).get(\""+key+"\"); \n" );
		buff.append( "}else{ \n" );
		buff.append( "	Class clazz=hintObj.getClass(); \n" );
		//buff.append( "	String getterStr=\"get\"+\""+key+"\".substring( 0, 1 ).toUpperCase()+\""+key+"\".substring( 1 ); \n" );
		//buff.append( "	java.lang.reflect.Method getter=clazz.getMethod( getterStr, new Class[] {} ); \n" );
		buff.append( "	java.lang.reflect.Method getter=org.ralasafe.util.Util.getJavabeanGetMethod( clazz.getName(), \""+key+"\" ); \n" );
		buff.append( "	"+v+"=getter.invoke( hintObj, new Object[] {} ); \n" );
		buff.append( "} \n" );
		return buff.toString();
	}
}
