/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import org.ralasafe.db.sql.xml.BinaryExpr;
import org.ralasafe.db.sql.xml.InExpr;
import org.ralasafe.db.sql.xml.IsNotNullExpr;
import org.ralasafe.db.sql.xml.IsNullExpr;
import org.ralasafe.db.sql.xml.NotInExpr;
import org.ralasafe.db.sql.xml.types.LinkerType;

public class ExpressionFormatter {

	public static String format( LinkerType linker ) {
		return "Expression Group(" + linker.toString() + ")";
	}

	public static String format( BinaryExpr binaryExpr ) {
		return "Binary Expression";
	}

	public static String format( NotInExpr notInExpr ) {
		return "Not In Expression";
	}

	public static String format( IsNullExpr isNullExpr ) {
		return "Is Null Expression";
	}

	public static String format( IsNotNullExpr isNotNullExpr ) {
		return "Is Not Null Expression";
	}

	public static String format( InExpr inExpr ) {
		return "In Expression";
	}
}
