/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.script;

import java.util.ArrayList;

public class Formula extends DefineVariable {
	private String type;
	private String operator;
	private ArrayList variables=new ArrayList();

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type=type;
	}

	/**
	 * Script likes: Integer v = (Integer)v1 + (Integer)v2 + ....; Integer v =
	 * (Integer)v1 - (Integer)v2 - ....; Integer v = (Integer)v1 * (Integer)v2 *
	 * ....; Integer v = (Integer)v1 / (Integer)v2 / ....;
	 */
	public String toScript() {
		String clazz=null;
		if( type.equalsIgnoreCase( "integer" ) ) {
			clazz="Integer";
		} else if( type.equalsIgnoreCase( "float" ) ) {
			clazz="Double";
		}
		String v=getVariableName();
		StringBuffer buff=new StringBuffer();
		buff.append( clazz ).append( " " ).append( v ).append( " = " );
		int size=variables.size();
		for( int i=0; i<size; i++ ) {
			Variable variable=(Variable) variables.get( i );
			if( i>0 ) {
				buff.append( " " ).append( operator ).append( " " );
			}
			buff.append( "(" ).append( variable.getName() ).append(
					" == null ? 0 : new " ).append( clazz ).append( "(" )
					.append( variable.getName() ).append( "))" );
		}
		buff.append( ";\n" );
		return buff.toString();
	}

	public ArrayList getVariables() {
		return variables;
	}

	public void setVariables( ArrayList variables ) {
		this.variables=variables;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator( String operator ) {
		this.operator=operator;
	}
}
