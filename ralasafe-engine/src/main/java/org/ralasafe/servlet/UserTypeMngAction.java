/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.Factory;
import org.ralasafe.RalasafeException;
import org.ralasafe.ResourceConstants;
import org.ralasafe.WebRalasafe;
import org.ralasafe.application.ApplicationManager;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.userType.UserType;
import org.ralasafe.userType.UserTypeManager;
import org.ralasafe.util.Util;


public class UserTypeMngAction extends Action {
	

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (RalasafeController.isSecured()) {
			if (!WebRalasafe
					.hasPrivilege(req, Privilege.POLICY_ADMIN_ID)) {
				throw new RalasafeException(Util.getMessage(req.getLocale(),
						ResourceConstants.NO_PRIVILEGE, Util.getMessage(req
								.getLocale(), ResourceConstants.POLICY_ADMIN)));
			}
		}

		String op = req.getParameter("op");
		String name = req.getParameter("name");

		if (op == null) {
			op = "select";
		}
		
		UserTypeManager userTypeMng=Factory.getUserTypeManager();
		if (op.equalsIgnoreCase("delete")) {
			userTypeMng.deleteUserType(name);
			// currently, we only support one application, one usertype
			// so, when delete this usertype, the application will be deleted too
			ApplicationManager appManager = Factory.getApplicationManager();
			appManager.deleteApplication("ralasafe");
		} else if (op.equalsIgnoreCase("view")) {
			UserType userType = userTypeMng.getUserType(name);
			req.setAttribute("userType", userType);
			req.setAttribute("editable", Boolean.FALSE);
			WebUtil.forward( req, resp, "/ralasafe/userType/view.jsp");
			return;
		}

		Collection userTypes = userTypeMng.getAllUserTypes();
		req.setAttribute("userTypes", userTypes);

		WebUtil.forward( req, resp, "/ralasafe/userType/index.jsp" );
		return;
	}

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
	 * HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}