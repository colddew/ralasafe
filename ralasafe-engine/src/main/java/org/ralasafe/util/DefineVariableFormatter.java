/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import org.ralasafe.db.sql.xml.ContextValue;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.Formula;
import org.ralasafe.db.sql.xml.Variable;

public class DefineVariableFormatter {
	public static String getValueType( DefineVariable var ) {
		if( var.getContextValue()!=null ) {
			return "Context value";
		} else if( var.getFormula()!=null ) {
			return "Formula";
		} /*else if( var.getHintValue()!=null ) {
			
		}*/else if( var.getQueryRef()!=null ) {
			return "Query";
		} else if( var.getSimpleValue()!=null ) {
			return "Simple value";
		} else if( var.getUserValue()!=null ) {
			return "User attribute";
		} else {
			return "Unknown type";
		}
	}
	
	public static String getFormatValue( DefineVariable var ) {
		if( var.getContextValue()!=null ) {
			ContextValue contextValue=var.getContextValue();
			return "key:"+contextValue.getKey();
		} else if( var.getFormula()!=null ) {
			Formula formula=var.getFormula();
			
			String oper=formula.getOperator().toString();
			Variable var0=formula.getVariable( 0 );
			Variable var1=formula.getVariable( 1 );
			return var0.getName()+oper+var1.getName();
		} /*else if( var.getHintValue()!=null ) {
			
		}*/else if( var.getQueryRef()!=null ) {
			return var.getQueryRef().getName();
		} else if( var.getSimpleValue()!=null ) {
			return var.getSimpleValue().getContent();
		} else if( var.getUserValue()!=null ) {
			return var.getUserValue().getKey();
		} else {
			return "";
		}
	}
}
