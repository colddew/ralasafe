/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ralasafe.util.DBUtil;

public class EmployeeManager {
	private static final String INSERT_SQL="INSERT INTO demouser(loginName,name,password," +
			"departmentId,companyId,isManager) values(?,?,?,?,?,?)";
	private static final String UPDATE_SQL="UPDATE demouser SET loginName=?,name=?, " +
			"password=?,departmentId=?,companyId=?,isManager=?" +
			" WHERE id=?";
	private static final String DELETE_SQL="DELETE FROM demouser WHERE id=?";
	private static final String SELECT_SQL="SELECT loginName,name,password," +
			"departmentId,companyId,isManager,id FROM demoUSER where id=?";

	public void addEmployee( Employee emp ) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			conn=DemoUtil.getConnection();
			pstmt=conn.prepareStatement( INSERT_SQL );
			pstmt.setString( 1, emp.getLoginName() );
			pstmt.setString( 2, emp.getName() );
			pstmt.setString( 3, emp.getPassword() );
			pstmt.setInt( 4, emp.getDepartmentId() );
			pstmt.setInt( 5, emp.getCompanyId() );
			pstmt.setInt( 6, emp.getIsManager() );
			pstmt.execute();
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			DBUtil.close( pstmt, conn );
		}
	}

	public void updateEmployee( Employee emp ) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			conn=DemoUtil.getConnection();
			pstmt=conn.prepareStatement( UPDATE_SQL );
			pstmt.setString( 1, emp.getLoginName() );
			pstmt.setString( 2, emp.getName() );
			pstmt.setString( 3, emp.getPassword() );
			pstmt.setInt( 4, emp.getDepartmentId() );
			pstmt.setInt( 5, emp.getCompanyId() );
			pstmt.setInt( 6, emp.getIsManager() );
			pstmt.setInt( 7, emp.getId() );
			pstmt.execute();
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			DBUtil.close( pstmt, conn );
		}
	}

	public void deleteEmployee( int deptId ) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		
		try {
			conn=DemoUtil.getConnection();
			pstmt=conn.prepareStatement( DELETE_SQL );
			pstmt.setInt( 1, deptId );
			pstmt.execute();
		} catch( SQLException e ) {
			e.printStackTrace();
		} finally {
			DBUtil.close( pstmt, conn );
		}
	}
	
	public Employee getEmployee( int customerId ) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			conn=DemoUtil.getConnection();
			pstmt=conn.prepareStatement( SELECT_SQL );
			pstmt.setInt( 1, customerId );
			rs=pstmt.executeQuery();
			
			Employee employee=null;
			if( rs.next() ) {
				employee=new Employee();
				employee.setLoginName( rs.getString(1) );
				employee.setName( rs.getString(2) );
				employee.setPassword( rs.getString(3) );
				employee.setDepartmentId( rs.getInt(4) );
				employee.setCompanyId( rs.getInt(5) );
				employee.setIsManager( rs.getInt(6) );
				employee.setId( rs.getInt(7) );
			} 
			
			return employee;
		} catch( SQLException e ) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.close(rs);
			DBUtil.close( pstmt, conn );
		}
	}
}
