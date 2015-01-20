/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.WebRalasafe;
import org.ralasafe.user.User;

public class LoanMoneyServlet extends HttpServlet {
	private LoanMoneyManager loanManager=new LoanMoneyManager();
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		Map context=new HashMap();
		context.put( "today", new java.sql.Date(System.currentTimeMillis()) );
		
		String op=req.getParameter( "op" );
		
		if( "add".equalsIgnoreCase( op ) ) {
			LoanMoney money=new LoanMoney();
			money.setLoanDate( new Date() );
			
			User user=WebRalasafe.getCurrentUser(req);
			money.setUserId( ((Integer)user.get( User.idFieldName ) ).intValue() );
			
			String strMoney=req.getParameter( "money" );
			money.setMoney( Integer.parseInt( strMoney ) );
			
			if( WebRalasafe.permit( req, Privilege.LOAN, money, context ) ) {
				loanManager.addLoadMoney( money );
			} 
		} 
		
		Collection loanMoneyList=WebRalasafe.query( req, Privilege.QUERY_LOAN, context );
		req.setAttribute( "loanMoneyList", loanMoneyList );
		
		RequestDispatcher rd=req.getRequestDispatcher( "loanMoney.jsp" );
		rd.forward( req, resp );
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		doGet( req, resp );
	}
}
