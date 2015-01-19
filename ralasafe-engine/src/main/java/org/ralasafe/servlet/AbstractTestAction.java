/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.user.User;
import org.ralasafe.user.UserManager;
import org.ralasafe.util.Util;

public abstract class AbstractTestAction extends Action {
	public Map buildTestContext( HttpServletRequest req,
			String[] testContextFields ) throws ParseException {
		Map ctx=null;
		if( testContextFields.length>0 ) {
			ctx=new HashMap();
			
			for( int i=0; i<testContextFields.length; i++ ) {
				String field=testContextFields[i];
				String rawValue=req.getParameter( "ctx"+field );
				String fieldType=req.getParameter( "ctx"+field+"type" );
				
				Object value=Util.parse( rawValue, fieldType );
				ctx.put( field, value );
			}
		}
		
		return ctx;
	}

	public Object buildTestBusinessData( HttpServletRequest req,
			String[] testBusinessDataFields ) throws InstantiationException, IllegalAccessException, ClassNotFoundException, ParseException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		Object bdData=null;
		Class bdClazz=null;
		
		if( testBusinessDataFields.length>0 ) {
			String bdClass=req.getParameter( "bdClass" );
			bdClazz=Class.forName( bdClass );
			bdData=bdClazz.newInstance();
		}
		
		for( int i=0; i<testBusinessDataFields.length; i++ ) {
			String field=testBusinessDataFields[i];
			String rawValue=req.getParameter( "bd"+field );
			String fieldType=req.getParameter( "bd"+field+"type" );
			
			Object value=Util.parse( rawValue, fieldType );
			
			Class fieldClass=null;
			if (fieldType.equals("int"))
				fieldClass = Integer.TYPE;
			else if (fieldType.equals("float"))
				fieldClass = Float.TYPE;
			else if (fieldType.equals("long"))
				fieldClass = Long.TYPE;
			else if (fieldType.equals("double"))
				fieldClass = Double.TYPE;
			else if (fieldType.equals("boolean"))
				fieldClass = Boolean.TYPE;
			else
				fieldClass = Class.forName(fieldType);
			
			// try setXyz method
			String methodName="set" + field.substring( 0, 1 ).toUpperCase() + field.substring( 1 );
			Method method=bdClazz.getMethod( methodName, new Class[]{fieldClass} );
			method.invoke( bdData, new Object[]{ value } );
		}
		return bdData;
	}

	public User buildTestUser( HttpServletRequest req, boolean testUserNeeded ) {
		User testUser=null;
		if( testUserNeeded ) {
			// userId
			String strUserId=req.getParameter( "userId" );
			Object userId=WebUtil.convertUserId( req, strUserId );
			testUser=new User();
			testUser.setId( userId );
			
			UserManager userMng=WebUtil.getUserManager( req );
			testUser=userMng.selectById( testUser );
		}
		
		return testUser;
	}
}
