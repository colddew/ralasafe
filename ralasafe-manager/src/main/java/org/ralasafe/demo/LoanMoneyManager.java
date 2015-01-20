/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ralasafe.util.DBUtil;


public class LoanMoneyManager {
	private static String INSERT_SQL="INSERT INTO loan_money(userId,money,loanDate) values(?,?,?)";
	public void addLoadMoney( LoanMoney money ) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			conn=DemoUtil.getConnection();
			pstmt=conn.prepareStatement( INSERT_SQL );
			pstmt.setInt( 1, money.getUserId() );
			pstmt.setInt( 2, money.getMoney() );
			pstmt.setDate( 3, new Date(money.getLoanDate().getTime()) );
			
			pstmt.execute();
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			DBUtil.close( pstmt, conn );
		}
	}
}
