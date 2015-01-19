/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.WebRalasafe;
import org.ralasafe.util.StringUtil;

public class EmployeeServlet extends HttpServlet {
	//private EmployeeManager employeeManager = new EmployeeManager();

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Get employees that current user is permitted to view.
		Collection employees = WebRalasafe.query(req, Privilege.QUERY_EMPLOYEE);
		req.setAttribute("employees", employees);

		RequestDispatcher rd = req.getRequestDispatcher("employee.jsp");
		rd.forward(req, resp);
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
