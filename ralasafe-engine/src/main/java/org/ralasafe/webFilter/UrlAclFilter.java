/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.webFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.Factory;
import org.ralasafe.WebRalasafe;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.privilege.PrivilegeManager;
import org.ralasafe.privilege.UserRoleManager;
import org.ralasafe.user.User;
import org.ralasafe.util.StringUtil;

/**
 * Validate function-level privilege by RBAC model.
 * 
 * <p>
 * Validate logic is:
 * <ol>
 * <li>The request url by user can be get by <code>request.getServletPath().</code>
 *     We will check the ${ralasafe_privilege} table, to find out this url should be validated or not. 
 *     (If the url in ${ralasafe_privilege} has parameter, like customerMng?op=add,
 *      parameter value will be validated too); </li>
 * <li>If so, validate by USER-ROLE-PRIVILEGE model;</li>
 * <ol>
 * 		<li>If user not logged in, goto login screen;</li>
 * 		<li>If user has no privilege to request the url, goto deny screen;</li>
 * 		<li>If user has privilege to request the url, continue filter chain;</li>
 * </ol>
 * <li>If not, skip validate, continue filter chain.</li>
 * </ol>
 * </p>
 * 
 * <p>
 * When direct to login or deny screen, the original request url will be saved in 
 * session as an attribute with "gotoPage" key.
 * </p>
 * 
 * <p>
 * Config Parameter(web.xml/filter/init-param):
 * <ol>
 * <li>loginPage: required, if user not logged in, go to this screen;</li>
 * <li>denyPage: required, if user has no privilege to request the url, go to this screen.</li>
 * </ol>
 * </p>
 * 
 * 
 */
public class UrlAclFilter implements Filter {
	private static final String GOTO_PAGE = "gotoPage";
	private String denyPage;
	private String loginPage;

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;

		String url = httpReq.getServletPath();
		String gotoPage=httpReq.getContextPath()+url;
		
		PrivilegeManager pvlgMng = Factory.getPrivilegeManager("ralasafe");
		UserRoleManager userRoleMng = Factory.getUserRoleManager("ralasafe",
				"ralasafe");

		Collection pvlgs = pvlgMng.getLikelyPrivilegesByUrl( url );

		User user = WebRalasafe.getCurrentUser(httpReq);
		Object userId = null;
		if (user != null) {
			userId = user.get(User.idFieldName);
		}

		for (Iterator iter = pvlgs.iterator(); iter.hasNext();) {
			Privilege pvlg = (Privilege) iter.next();
			String pvlgUrl = pvlg.getUrl();

			if (!StringUtil.isEmpty(pvlgUrl)
					&& pvlgUrl.toUpperCase().startsWith(url.toUpperCase())) {
				boolean match = match(pvlgUrl, url, httpReq);

				if (match) {
					// the url must be validated
					boolean hasPrivilege = false;
					// validate user roles
					if (userId != null) {
						hasPrivilege = userRoleMng.hasPrivilege(userId, pvlg
								.getId());
					} else {
						// go to login screen
						httpReq.getSession().setAttribute(GOTO_PAGE, gotoPage);
						httpResp.sendRedirect(httpReq.getContextPath()
								+ loginPage);
						return;
					}

					if (hasPrivilege) {
						// continue filter chain
						chain.doFilter(req, resp);
						return;
					} else {
						// go to deny screen
						httpReq.getSession().setAttribute(GOTO_PAGE, gotoPage);
						httpResp.sendRedirect(httpReq.getContextPath()
								+ denyPage);
						return;
					}
				}
			}
		}

		// no url is matched, no need to validate this url
		chain.doFilter(req, resp);
	}

	/**
	 * validate the url's paramter, match the parameter and paramter value.
	 * 
	 * @param pvlgUrl
	 * @param url
	 * @param httpReq
	 * @return
	 */
	private boolean match(String pvlgUrl, String url, HttpServletRequest httpReq) {
		int indexOf = pvlgUrl.indexOf("?");
		if (indexOf == -1) {
			return true;
		}

		String paramPart = pvlgUrl.substring(indexOf + 1);
		String[] params = StringUtil.split(paramPart, "&");

		for (int i = 0; i < params.length; i++) {
			String param = params[i];
			int indexOf2 = param.indexOf("=");

			String paramName = param.substring(0, indexOf2);
			String paramValue = param.substring(indexOf2 + 1);

			String actualValue = httpReq.getParameter(paramName);
			if (!paramValue.equals(actualValue)) {
				return false;
			}
		}

		return true;
	}

	public void init(FilterConfig arg0) throws ServletException {
		denyPage = arg0.getInitParameter("denyPage");
		loginPage = arg0.getInitParameter("loginPage");
	}
}
