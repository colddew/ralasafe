/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.ralasafe.RalasafeException;
import org.ralasafe.entitle.BusinessData;
import org.ralasafe.entitle.BusinessDataManager;

public class BusinessDataPolicyTestAction extends AbstractPolicyTestAction {

	public String getPolicyHandlerAttributeKey( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		return "_$ralasafeBusinessData_" + id;
	}

	public AbstractPolicyDesignHandler createPolicyHandler( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		
		BusinessDataManager manager=WebUtil.getBusinessDataManager( req );
		BusinessData uc=manager.getBusinessData( id );
		
		org.ralasafe.db.sql.xml.BusinessDataType xmlUc;
		try {
			xmlUc = org.ralasafe.db.sql.xml.BusinessData.unmarshal(new StringReader(uc.getXmlContent()));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		
		return new BusinessDataPolicyDesignHandler( manager, xmlUc );
	}
}
