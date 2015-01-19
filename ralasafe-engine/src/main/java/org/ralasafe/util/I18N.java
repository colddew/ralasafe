/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public class I18N {
	private ResourceBundle rb;
	private static Locale locale;
	private I18N( ResourceBundle rb ) {
		this.rb=rb;
	}
	
	public static I18N getWebInstance( HttpServletRequest req ) {
		locale=req.getLocale();
		ResourceBundle bundle=ResourceBundle.getBundle("Ralasafe_Web", locale);
		
		return new I18N( bundle );
	}
	
	public String getValidateMessageFile() {
		String country=locale.getCountry();
		if( country!=null ) {
			return "messages_"+country.toLowerCase()+".js";
		} else {
			return "";
		}
	}
	
	public String say( String msg ) {
		return rb.getString( msg );
	}
}
