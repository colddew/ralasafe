/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.demo;

import java.util.Date;

public class LoanMoney {
	private int id;
	private int userId;
	private int money;
	private Date loanDate;
	public int getId() {
		return id;
	}
	public void setId( int id ) {
		this.id=id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId( int userId ) {
		this.userId=userId;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney( int money ) {
		this.money=money;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate( Date loanDate ) {
		this.loanDate=loanDate;
	}
}
