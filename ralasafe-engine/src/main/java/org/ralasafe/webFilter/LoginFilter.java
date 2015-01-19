/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.webFilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ralasafe.Factory;
import org.ralasafe.WebRalasafe;
import org.ralasafe.encrypt.Base64Encrypt;
import org.ralasafe.encrypt.Encrypt;
import org.ralasafe.encrypt.Md5Encrypt;
import org.ralasafe.encrypt.PlainEncrypt;
import org.ralasafe.encrypt.ShaEncrypt;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.servlet.WebUtil;
import org.ralasafe.user.User;
import org.ralasafe.user.UserManager;
import org.ralasafe.userType.UserType;
import org.ralasafe.util.StringUtil;

/**
 * Check whether the user is logged in, if not, go to the login screen. 
 * User enters loginname(maybe many fields, like areaId and username) and password, and validated by the Filter. 
 * If it is validated, redirect to the former request url; Otherwise, stay in the login screen.
 * 
 * <p>
 * Config parameters:
 * <ul>
 * <li>loginPage: required, login page, example: /login.do</li>
 * <li>uniqueFieldsParams: optional, which parameter(s) is/are corresponding to uniqueField. 
 *     If it's not given, use uniqueFields difined in usermetadata</li>
 * <li>passwordParam: optional, default value: password.</li>
 * <li>userPasswordField: optional, default value: password. Which field defined in usermetadata means password</li>
 * <li>encryptMethod: optional, default: no encrypt. Ralasafe provides several encryption algorithms: base64,md5hex,shahex.
 *     You can develop your special encrypt method. If so, config your implment class there, which should implement interface  
 *     <code>org.ralasafe.encrypt.Encrypt</code></li>
 * <li>denyMessage: optional, default value: LoginName and password not matched. If loginName & password not matched,
 *     Ralasafe will give this message to login user.</li>
 * </ul>
 * </p>
 * 
 * 
 * 
 */
public class LoginFilter implements Filter {
	private static final String RALASAFE_REQUEST_LOGIN = "ralasafeRequestLogin";
	private static final String DENY_MESSAGE = "denyMessage";
	private static final String GOTO_PAGE = "gotoPage";
	private String loginPage;
	private String checkLoginUrlPattern;
	private String[] uniqueFieldsParams;
	private String passwordParam;
	private String userPasswordField;
	private String denyMessage;
	private String successPage;
	private int maxAttempts;
	private String attemptMessage;
	private String userAttemptField;
	private String resetInterval;
	private Encrypt encrypt;

	// private String redirectLoginPage;

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) (req);
		HttpServletResponse httpResp = (HttpServletResponse) (resp);

		// If the request is login screen, there's no need to filter now
		if (requestForLoginPage(httpReq)) {
			chain.doFilter(req, resp);
			return;
		}

		HttpSession session = httpReq.getSession();

		if (requestFromLoginPage(httpReq)) {
			WebRalasafe.setCurrentUser(httpReq, null);

			String gotoUrl = (String) session.getAttribute(GOTO_PAGE);

			User user = validUser(httpReq, httpResp);
			if (user == null) {

				// validation failed, login again
				gotoLoginPage(httpReq, httpResp, denyMessage, gotoUrl);
				return;
			} else {
				// validate successfully
				req.setAttribute(GOTO_PAGE, gotoUrl);

				session.removeAttribute(DENY_MESSAGE);
				session.removeAttribute(GOTO_PAGE);
				session.removeAttribute(RALASAFE_REQUEST_LOGIN);
				WebRalasafe.setCurrentUser(httpReq, user);

				chain.doFilter(req, resp);
				return;
			}
		} else {
			Object user = WebRalasafe.getCurrentUser(httpReq);
			if (user == null) {
				// not login yet, goto login screen
				session.removeAttribute(DENY_MESSAGE);
				String gotoPage = httpReq.getRequestURI(); 
				gotoLoginPage(httpReq, httpResp, null, gotoPage);
				return;
			} else {
				chain.doFilter(req, resp);
				return;
			}
		}
	}

	/**
	 * Does client side request for login page?
	 * 
	 * @param httpReq
	 * @return
	 */
	private boolean requestForLoginPage(HttpServletRequest httpReq) {
		String url = httpReq.getServletPath();
		if (url.toLowerCase().startsWith(loginPage.toLowerCase())) {
			// is ralasafe system direct to this page?
			String flag = (String) httpReq.getSession().getAttribute(
					RALASAFE_REQUEST_LOGIN);
			// String flag=httpReq.getParameter( RALASAFE_REQUEST_LOGIN );
			if (!"y".equalsIgnoreCase(flag)) {
				HttpSession session = httpReq.getSession();
				session.removeAttribute(DENY_MESSAGE);
				session.removeAttribute(GOTO_PAGE);
			}

			return true;
		}

		return false;
	}

	/**
	 * Client side filled username and password, request for login.
	 * 
	 * @param httpReq
	 * @return
	 */
	private boolean requestFromLoginPage(HttpServletRequest httpReq) {
		String url = httpReq.getHeader("REFERER");
		if (StringUtil.isEmpty(url)) {
			url = httpReq.getHeader("referer");
		}

		String fromPath = "";
		if (StringUtil.isEmpty(url)) {
			fromPath = httpReq.getServletPath();
		} else {
			String subUrl = url.substring(url.indexOf("://") + "://".length());
			
			String contextPath = httpReq.getContextPath();
			// whether is a ROOT context. ROOT context path is "".
			if( "".equals( contextPath ) ) {
				fromPath = subUrl.substring( subUrl.indexOf( "/" ) );
			} else {
				fromPath = subUrl.substring(subUrl.indexOf(contextPath)	+ contextPath.length());
			}
		}

		return fromPath.toLowerCase().startsWith(loginPage.toLowerCase());
	}

	private void gotoLoginPage(HttpServletRequest httpReq,
			HttpServletResponse httpResp, String denyMessage, String gotoPage)
			throws ServletException, IOException {
		httpReq.getSession().setAttribute(RALASAFE_REQUEST_LOGIN, "y");
		httpReq.getSession().setAttribute(DENY_MESSAGE, denyMessage);
		httpReq.getSession().setAttribute(GOTO_PAGE, gotoPage);

		httpResp.sendRedirect(httpReq.getContextPath() + loginPage);
	}

	private User validUser(HttpServletRequest httpReq,
			HttpServletResponse httpResp) {
		// convert uniqueFields to object
		UserType userType = WebUtil.getUserType(httpReq);
		FieldMetadata[] uniqueFields = userType.getUserMetadata()
				.getMainTableMetadata().getUniqueFields();

		User expectUser = new User();
		for (int i = 0; i < uniqueFields.length; i++) {
			FieldMetadata fieldMetadata = uniqueFields[i];
			String javaType = fieldMetadata.getJavaType();

			String paramName = fieldMetadata.getName();
			if (uniqueFieldsParams != null) {
				paramName = uniqueFieldsParams[i];
			}
			String rawValue = httpReq.getParameter(paramName);

			Object value = parse(javaType, rawValue);
			expectUser.set(fieldMetadata.getName(), value);
		}

		String rawPassword = httpReq.getParameter(passwordParam);
		String password = encrypt.encrypt(rawPassword);

		UserManager manager = Factory.getUserManager(userType.getName());
		User findUser = manager.selectByUniqueFields(expectUser);

		if (findUser != null
				&& findUser.get(userPasswordField).equals(password)) {
			return findUser;
		} else {
			return null;
		}
	}

	private Object parse(String javaType, String rawValue) {
		if (javaType.equalsIgnoreCase("java.lang.String")) {
			return rawValue;
		} else if (javaType.equalsIgnoreCase("java.lang.Integer")) {
			return new Integer(rawValue);
		} else if (javaType.equalsIgnoreCase("java.lang.Double")) {
			return new Double(rawValue);
		} else if (javaType.equalsIgnoreCase("java.lang.Float")) {
			return new Float(rawValue);
		}
		return null;
	}

	public void init(FilterConfig config) throws ServletException {
		String rawUniqueFieldsParams = config
				.getInitParameter("uniqueFieldsParams");
		if (!StringUtil.isEmpty(rawUniqueFieldsParams)) {
			uniqueFieldsParams = StringUtil.splitAndTrim(rawUniqueFieldsParams,
					",");
		}

		passwordParam = config.getInitParameter("passwordParam");
		if (StringUtil.isEmpty(passwordParam)) {
			passwordParam = "password";
		}

		userPasswordField = config.getInitParameter("userPasswordField");
		if (StringUtil.isEmpty(userPasswordField)) {
			userPasswordField = "password";
		}

		denyMessage = config.getInitParameter("denyMessage");
		if (StringUtil.isEmpty(denyMessage)) {
			denyMessage = "LoginName and password not matched";
		}

		String encryptMethod = config.getInitParameter("encryptMethod");
		if (StringUtil.isEmpty(encryptMethod)) {
			encrypt = new PlainEncrypt();
		} else if ("base64".equalsIgnoreCase(encryptMethod)) {
			encrypt = new Base64Encrypt();
		} else if ("md5hex".equalsIgnoreCase(encryptMethod)) {
			encrypt = new Md5Encrypt();
		} else if ("shahex".equalsIgnoreCase(encryptMethod)) {
			encrypt = new ShaEncrypt();
		} else {
			// developer customize encrypt method
			try {
				Object instance = Class.forName(encryptMethod).newInstance();
				encrypt = (Encrypt) instance;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Encry method not found: " + encryptMethod);
			}
		}

		loginPage = config.getInitParameter("loginPage");

		// redirectLoginPage="/"+config.getServletContext().getServletContextName()+loginPage;
		// if( redirectLoginPage.indexOf( "?" )==-1 ) {
		// redirectLoginPage=redirectLoginPage+"?1=1";
		// }
	}
}
