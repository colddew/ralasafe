/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

public class ResultSetReaderUtil {
	public static ResultSetReader getReader( String fieldClassName ) {
		if( fieldClassName.equals( "float" )||fieldClassName.equals( "java.lang.Float" ) )
			return new ResultSetReader.FloatReader();
		else if( fieldClassName.equals( "double" )
				||fieldClassName.equals( "java.lang.Double" ) )
			return new ResultSetReader.DoubleReader();
		else if( fieldClassName.equals( "int" )
				||fieldClassName.equals( "java.lang.Integer" ) )
			return new ResultSetReader.IntegerReader();
		else if( fieldClassName.equals( "long" )
				||fieldClassName.equals( "java.lang.Long" ) )
			return new ResultSetReader.LongReader();
		else if( fieldClassName.equals( "boolean" )
				||fieldClassName.equals( "java.lang.Boolean" ) )
			return new ResultSetReader.BooleanReader();
		else if( fieldClassName.equals( "short" )
				||fieldClassName.equals( "java.lang.Short" ) )
			return new ResultSetReader.ShortReader();
		else if( fieldClassName.equals( "java.util.Date" ) )
			return new ResultSetReader.JavaUtilDateReader();
		else
			return new ResultSetReader.ObjectReader();
	}
}
