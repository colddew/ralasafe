/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.Ralasafe;
import org.ralasafe.WebRalasafe;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.user.User;

public class DesignerAction extends Action {

	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {		
		Token token=new Token();
		
		// Is ralasafe security enabled?
		boolean secured=RalasafeController.isSecured();
		
		if( secured ) {
			User currentUser=WebRalasafe.getCurrentUser( req );
			token.setCanAssignRoleToUser( Ralasafe.hasPrivilege( Privilege.ASSIGN_ROLE_TO_USER_ID, currentUser ) );
			token.setCanAdminRole( Ralasafe.hasPrivilege( Privilege.ROLE_ADMIN_ID, currentUser ) );
			token.setCanAdminPolicy( Ralasafe.hasPrivilege( Privilege.POLICY_ADMIN_ID, currentUser ) );
		}
		
		req.setAttribute( "token", token );
		WebUtil.forward( req, resp, "/ralasafe/main.jsp" );
	}
	
	/**
	 * Only used in designer.jsp. So there's no need extract it as a 'normal' class
	 * @author Julian Wong
	 *
	 */
	public class Token {
		private boolean canAssignRoleToUser=true;
		private boolean canAdminRole=true;
		private boolean canAdminPolicy=true;
		
		public boolean isCanAssignRoleToUser() {
			return canAssignRoleToUser;
		}
		public void setCanAssignRoleToUser( boolean canAssignRoleToUser ) {
			this.canAssignRoleToUser=canAssignRoleToUser;
		}
		public boolean isCanAdminRole() {
			return canAdminRole;
		}
		public void setCanAdminRole( boolean canAdminRole ) {
			this.canAdminRole=canAdminRole;
		}
		public boolean isCanAdminPolicy() {
			return canAdminPolicy;
		}
		public void setCanAdminPolicy( boolean canAdminPolicy ) {
			this.canAdminPolicy=canAdminPolicy;
		}
	}
}
