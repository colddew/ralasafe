/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.db;

/**
 * WARN!!!!
 * We will deprecate it later.
 */
public class SelectConditionUtil {
	/**
	 * Use equals comparator and and linker type, combine them into a select condition.
	 * @param selectByColumns
	 * @return
	 */
	public static SelectCondition simplyConnectColumns( Column[] selectByColumns ) {
		if( selectByColumns==null )
			return null;
		ComplexWhereElement cpxEmt=new ComplexWhereElement();
		Column column=selectByColumns[0];
		FieldWhereElement emt=simplyToFieldWhereElement( column );
		cpxEmt.setFirstPart( emt );
		if( selectByColumns.length>1 ) {
			LWhereElement[] lEmts=new LWhereElement[selectByColumns.length-1];
			for( int i=1; i<selectByColumns.length; i++ ) {
				FieldWhereElement temp=simplyToFieldWhereElement( selectByColumns[i] );
				LWhereElement lEmt=new LWhereElement();
				lEmt.setWhereElement( temp );
				lEmt.setLinkType( LWhereElement.AND_LINK_TYPE );
				lEmts[i-1]=lEmt;
			}
			cpxEmt.setLinkedParts( lEmts );
		}
		SelectCondition cdtn=new SelectCondition();
		if( selectByColumns.length==1 ) {
			cdtn.setWhereElement( emt );
		} else {
			cdtn.setWhereElement( cpxEmt );
		}
		return cdtn;
	}

	/**
	 * Convert column to field where element, by using equals comparator.
	 * @param column
	 * @return
	 */
	public static FieldWhereElement simplyToFieldWhereElement( Column column ) {
		FieldWhereElement emt=new FieldWhereElement();
		emt.setColumn( column );
		emt.setCompartor( SingleValueComparator.EQUAL );
		emt.setContextValue( true );
		return emt;
	}
}
