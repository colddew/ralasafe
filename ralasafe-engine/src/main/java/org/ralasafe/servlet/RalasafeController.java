package org.ralasafe.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.RalasafeException;
import org.ralasafe.SystemConstant;
import org.ralasafe.util.Startup;
import org.ralasafe.util.StringUtil;

public class RalasafeController extends HttpServlet {
	private static final long serialVersionUID=4879579300062880446L;
	private static final Log log=LogFactory.getLog( RalasafeController.class );
	
	private Map actionMap; //key/value=url/Action
	private Map actionNameMap; //key/value=url/actionName
	private static boolean secured;
	private static int queryLimit;

	public static boolean isSecured() {
		return secured;
	}

	public static int getQueryLimit() {
		return queryLimit;
	}
	
	public void init( ServletConfig config ) throws ServletException {
		super.init( config );
		
		// start up ralasafe
		startup( config );
		
		// action-mapping
		String path=config.getServletContext().getRealPath( "/WEB-INF/ralasafe/actions.xml" );
		ActionParser parser=new ActionParser();
		actionNameMap=parser.parse( path );
		actionMap=new HashMap();
		
//		// instance actions
//		for( Iterator iter=actionMap.keySet().iterator(); iter.hasNext(); ) {
//			Object url=iter.next();
//			String actionName=(String) actionMap.get( url );
//			
//			try {
//				Object action=Class.forName( actionName ).newInstance();
//				actionMap.put( url, action );
//			} catch( Exception e ) {
//				log.error( "", e );
//				throw new ServletException( e );
//			} 
//		}
	}

	private void startup( ServletConfig config ) {
		System.out.println("**** Starting Ralasafe ......");

		String securedStr = config.getInitParameter("secured");
		if (securedStr != null && securedStr.trim().equalsIgnoreCase("false")) {
			secured = false;
			System.out.println("Ralasafe security is disabled. " +
					"You can enable it in web.xml->RalasafeController->secured.");
		} else {
			secured = true;
			System.out.println("Ralasafe security is enabled.");
		}

		String queryLimitStr = config.getInitParameter("queryLimit");
		if (!StringUtil.isEmpty(queryLimitStr)) {
			String errorMessage = "Error: queryLimit must be an integer greater than 0!";
			try {
				queryLimit = new Integer(queryLimitStr).intValue();
				SystemConstant.setQueryLimit( queryLimit );
			} catch (NumberFormatException e) {
				System.out.println(errorMessage);
				throw new RalasafeException(errorMessage, e);
			}

			if (queryLimit <= 0) {
				System.out.println(errorMessage);
				throw new RalasafeException(errorMessage);
			}

			System.out.println("Ralasafe query limitation is enabled. queryLimit=" + queryLimit);
		} else {
			System.out.println("Ralasafe query limitation is disabled. " +
					"You can enable it in web.xml->RalasafeController->queryLimit.");
		}

		String repositoryDir = getServletConfig().getInitParameter("repositoryDir");
		String realPath = getServletContext().getRealPath("/");
		if(!realPath.endsWith("/")){
			realPath+="/";
		}
		String datasourceDir = realPath	+ "WEB-INF/ralasafe/";

		Startup.startup(datasourceDir, repositoryDir);
		
		String lineSep=System.getProperty( "line.separator" );
		String consoleMsg="========================================================================================"+lineSep;
		consoleMsg      +="                                                                                       "+lineSep;
		consoleMsg      +="  Ralasafe web console: http://${serverip}:${port}/${context}/ralasafe/designer.rls    "+lineSep;
		consoleMsg      +="                                                                                       "+lineSep;
		consoleMsg      +="========================================================================================"+lineSep;
		System.out.println( consoleMsg );
		
		System.out.println( "**** Ralasafe started successfully!" );
	}

	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		if( log.isDebugEnabled() ) {
			log.debug( "Get request for " + req.getServletPath() );
		}
		
		Action action=getAction( req );		
		action.doGet( req, resp );
	}

	private Action getAction( HttpServletRequest req ) throws ServletException {
		String url=req.getServletPath();
		Action action=(Action) actionMap.get( url );
		
		if( action==null ) {
			// instance it
			String actionName=(String) actionNameMap.get( url );
			
			try {
				action=(Action) Class.forName( actionName ).newInstance();
				actionMap.put( url, action );
			} catch( Exception e ) {
				log.error( "", e );
				throw new ServletException( e );
			} 
		}
		
		return action;
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		if( log.isDebugEnabled() ) {
			log.debug( "Post request for " + req.getServletPath() );
		}
		
		Action action=getAction( req );		
		action.doPost( req, resp );
	}
	
}
