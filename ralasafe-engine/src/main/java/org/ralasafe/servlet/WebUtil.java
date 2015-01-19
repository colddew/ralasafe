/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.Factory;
import org.ralasafe.WebConstants;
import org.ralasafe.application.Application;
import org.ralasafe.application.ApplicationManager;
import org.ralasafe.entitle.BusinessDataManager;
import org.ralasafe.entitle.EntitleManager;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.UserCategoryManager;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.privilege.RoleManager;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;
import org.ralasafe.user.UserManager;
import org.ralasafe.userType.UserType;
import org.ralasafe.userType.UserTypeManager;
import org.ralasafe.util.StringUtil;

/**
 * For web util. Including methods for getting UserType, ApplicationManager,
 * UserManager, and other things.
 * 
 * 
 * <p>
 * WebUtil returns managers (or other things) for client according to name
 * stored in session. If no name is stored, return a default one.<br/>
 * 
 * Take <code>getApplication</code> for example. Just applicationName="abc" is
 * stored in session, returns ApplicationManager of abc, else return the first
 * ApplicationManager in ralasafe system.
 * </p>
 * 
 * <p>
 * This util wraps details of how to get managers and hot to cache managers.
 * </p>
 * 
 * @author Julian Wong
 */
public class WebUtil {
	/**
	 * Return an application with name stored in session or just return a
	 * default one. The store key is {@link WebConstants.CURR_APP}.
	 * 
	 * @param req
	 * @return an application or null
	 */
	public static Application getApplication( HttpServletRequest req ) {
		ApplicationManager appMng=Factory.getApplicationManager();
		
		String name=(String) req.getSession().getAttribute(
				WebConstants.CURR_APP );
		if( StringUtil.isEmpty( name ) ) {
			// get a default one
			Collection apps=appMng.getAllApplications();
			if( apps.size()>0 ) {
				Application app=(Application) apps.iterator().next();
				name=app.getName();
				req.getSession().setAttribute( WebConstants.CURR_APP, name );
				
				return app;
			}	
			return null;
		} else {
			return appMng.getApplication( name );
		}
	}

	/**
	 * Return an UserType with name stored in session or just return a default
	 * one. The store key is {@link WebConstants.CURR_USRE_TYPE}.
	 * 
	 * @param req
	 * @return an UserType or null
	 */
	public static UserType getUserType( HttpServletRequest req ) {
		UserTypeManager userTypeMng=Factory.getUserTypeManager();
		
		String name=(String) req.getSession().getAttribute(
				WebConstants.CURR_USER_TYPE );
		if( StringUtil.isEmpty( name ) ) {
			// get a default one
			Collection types=userTypeMng.getAllUserTypes();
			if( types.size()>0 ) {
				UserType type=(UserType) types.iterator().next();
				
				name=type.getName();
				req.getSession().setAttribute( WebConstants.CURR_USER_TYPE,name );
				
				return type;
			} else {
				return null;
			}
		} else {
			return userTypeMng.getUserType( name );
		}
	}
	
	/**
	 * Return an UserManager with current <code>UserType</code> name stored in session.
	 * 
	 * @param req
	 * @return     an UserManager
	 */
	public static UserManager getUserManager( HttpServletRequest req ) {
		UserType userType=getUserType( req );
		
		return Factory.getUserManager( userType.getName() );
	}
	
	/**
	 * Return an UserRoleManager with current Application and UserType.
	 * 
	 * @param req
	 * @return   an UserRoleManager
	 */
	public static UserRoleManager getUserRoleManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		UserType userType=getUserType( req );
		
		return Factory.getUserRoleManager( app.getName(), userType.getName() );
	}
	
	/**
	 * Return a PrivilegeManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static PrivilegeManager getPrivilegeManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getPrivilegeManager( app.getName() );
	}
	
	/**
	 * Return a EntitleManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static EntitleManager getEntitleManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getEntitleManager( app.getName() );
	}
	
	/**
	 * Return a QueryManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static QueryManager getQueryManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getQueryManager( app.getName() );
	}
	
	/**
	 * Return a BusinessDataManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static BusinessDataManager getBusinessDataManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getBusinessDataManager( app.getName() );
	}
	
	/**
	 * Return a RoleManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static RoleManager getRoleManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getRoleManager( app.getName() );
	}
	
	/**
	 * Return a UserCategoryManager with current Application name stored in session.
	 * 
	 * @param req
	 * @return
	 */
	public static UserCategoryManager getUserCategoryManager( HttpServletRequest req ) {
		Application app=getApplication( req );
		
		return Factory.getUserCategoryManager( app.getName() );
	}
	
	public static void forward( HttpServletRequest req,
			HttpServletResponse resp, String url ) throws ServletException,
			IOException {
		RequestDispatcher rd=req.getRequestDispatcher( url );
		rd.forward( req, resp );
	}

	/**
	 * Convert string form userId to appropriate form (according to usermetadata)
	 * @param req
	 * @param strUserId
	 * @return
	 */
	public static Object convertUserId( HttpServletRequest req, String strUserId ) {
		FieldMetadata[] fields=getUserType( req ).getUserMetadata().getMainTableMetadata().getFields();
		String idClass="";
		for (int i=0; i<fields.length; i++) {
			FieldMetadata field=fields[i];
			if( field.getName().equals( User.idFieldName ) ) {
				idClass=field.getJavaType(); // return;
			}
		}
		
		if( "int".equals( idClass )||"java.lang.Integer".equals( idClass ) ) {
			return new Integer( strUserId );
		} else if( "long".equals( idClass )||"java.lang.Long".equals( idClass ) ) {
			return new Long( strUserId );
		}
		
		return strUserId;
	}
	
	public static int getIntParameter( HttpServletRequest req, String paramName, int defaultValue ) {
		String value=req.getParameter( paramName );
		if( StringUtil.isEmpty( value ) ) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt( value );
			} catch (Exception e ) {
				return defaultValue;
			} 
		}
	}
	
	public static boolean getBooleanParameter( HttpServletRequest req, String paramName, boolean defaultValue ) {
		String value=req.getParameter( paramName );
		if( StringUtil.isEmpty( value ) ) {
			return defaultValue;
		} else {
			try {
				return Boolean.parseBoolean( value );
			} catch (Exception e ) {
				return defaultValue;
			} 
		}
	}
	
	public static long getLongParameter( HttpServletRequest req, String paramName, long defaultValue ) {
		String value=req.getParameter( paramName );
		if( StringUtil.isEmpty( value ) ) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong( value );
			} catch (Exception e ) {
				return defaultValue;
			} 
		}
	}
	
	public static double getDoubleParameter( HttpServletRequest req, String paramName, double defaultValue ) {
		String value=req.getParameter( paramName );
		if( StringUtil.isEmpty( value ) ) {
			return defaultValue;
		} else {
			try {
				return Double.parseDouble( value );
			} catch (Exception e ) {
				return defaultValue;
			} 
		}
	}
}
