/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.util;

import java.util.StringTokenizer;

public class StringUtil {
	public static boolean isEmpty(String s) {
		if (s == null) {
			return true;
		}

		if (s.length() == 0) {
			return true;
		}

		return false;
	}

	public static void append(StringBuffer buff, String[] names) {
		if (Util.isEmpty(names)) {
			return;
		}

		buff.append(names[0]);
		for (int i = 1; i < names.length; i++) {
			buff.append(",").append(names[i]);
		}
	}
	
	public static String toString( String[] names ) {
		StringBuffer buff=new StringBuffer();
		append( buff, names );
		return buff.toString();
	}
	
	/**
	 * Append every elements in names array into buff. Some like:
	 * names[0]${appendSting}${seperator}names[1]${appendString
	 * }${seperator}...names[n-1]${appendString}.
	 * 
	 * 
	 * <p>
	 * Exapmle:
	 * INSERT SQL can be appended with: append( buff, names, false, "?", "," );
	 * UPDATE SQL can be appended with: append( buff, names, true, "=?", "," ); 
	 * WHERE SQL can be appended with: append( buff, names, true, "=?", " AND " );
	 * 
	 * @param buff
	 * @param names
	 * @param displayName        If true, append every names[i] into buff, else names[i] is not appended
	 * @param appendString
	 * @param seperator
	 */
	public static void append(StringBuffer buff, String[] names,
			boolean displayName, String appendString, String seperator) {
		if (Util.isEmpty(names))
			return;

		if (displayName)
			buff.append(names[0]);
		buff.append(appendString);

		for (int i = 1; i < names.length; i++) {
			buff.append(seperator);
			if (displayName)
				buff.append(names[i]);
			buff.append(appendString);
		}
	}

	public static String[] split(String s, String token) {
		if (s == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(s, token);
		int size = st.countTokens();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = st.nextToken();
		}

		return result;
	}

	public static String[] splitAndTrim(String s, String token) {
		if (s == null) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(s, token);
		int size = st.countTokens();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = st.nextToken().trim();
		}

		return result;
	}

	/**
	 * Resever whitespace, breakline into <content>
	 */
	public static String keepSpaceInContent(String xmlStr, String content) {
		StringBuffer xml = new StringBuffer(xmlStr);
		int start = xml.indexOf("<content>");
		int end = xml.indexOf("</content>");

		xml.replace(start, end, "<content xml:space=\"preserve\"><![CDATA[" + content+"]]>");

		return xml.toString();
	}

	public static String getEvalError(String error) {
		StringBuffer buff = new StringBuffer(error);
		int start = buff.indexOf("Sourced file:");
		int end = buff.indexOf(". . . '' : ");
		if (start >= 0 && end >= 0 && start < end) {
			buff.replace(start, end + 11, "");
		}

		return buff.toString();
	}
	
	public static String toJavascriptValue( String[] s ) {
		StringBuffer buff=new StringBuffer();
		buff.append( "" );
		for( int i=0; i<s.length; i++ ) {
			String t=s[i];
			if( i>0 ) {
				buff.append( "," );
			}
			buff.append( "'" )
				.append( t )
				.append( "'" );
		}
		
		return buff.toString();
	}
}
