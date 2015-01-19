/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.entitle;

import java.util.ArrayList;

public class DecisionEntitlementTestResult {
	private boolean failed;
	private String errorMessage;
	private Decision decision = new Decision();
	private ArrayList userCategoryTestResults = new ArrayList();
	private ArrayList businessDataTestResults = new ArrayList();

	public ArrayList getUserCategoryTestResults() {
		return userCategoryTestResults;
	}

	public void setUserCategoryTestResults(ArrayList userCategoryTestResults) {
		this.userCategoryTestResults = userCategoryTestResults;
	}

	public ArrayList getBusinessDataTestResults() {
		return businessDataTestResults;
	}

	public void setBusinessDataTestResults(ArrayList businessDataTestResults) {
		this.businessDataTestResults = businessDataTestResults;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Decision getDecision() {
		return decision;
	}

	public void setDecision(Decision decision) {
		this.decision = decision;
	}
}
