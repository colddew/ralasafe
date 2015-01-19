/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.adapter.user;

import java.util.HashMap;
import java.util.Map;

import org.ralasafe.db.Column;
import org.ralasafe.db.ColumnAdapter;
import org.ralasafe.db.Comparator;
import org.ralasafe.db.ComplexWhereElement;
import org.ralasafe.db.FieldWhereElement;
import org.ralasafe.db.GroupPart;
import org.ralasafe.db.LWhereElement;
import org.ralasafe.db.MapStorgeColumnAdapter;
import org.ralasafe.db.NoValueComparator;
import org.ralasafe.db.OrderPart;
import org.ralasafe.db.SelectCondition;
import org.ralasafe.db.SingleValueComparator;
import org.ralasafe.db.Type;
import org.ralasafe.db.WhereElement;
import org.ralasafe.metadata.user.FieldMetadata;
import org.ralasafe.metadata.user.UserMetadata;
import org.ralasafe.user.UserSelectCondition;

public class SelectConditionAdapter {
	private Map columnMap;
	private static Map compartorMap=new HashMap();
	static {
		compartorMap.put( org.ralasafe.user.NoValueComparator.IS_NULL.getType(), 
				NoValueComparator.IS_NULL );
		compartorMap.put( org.ralasafe.user.NoValueComparator.NOT_NULL.getType(), 
				NoValueComparator.NOT_NULL );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.EQUAL.getType(), 
				SingleValueComparator.EQUAL );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.GREATER.getType(), 
				SingleValueComparator.GREATER );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.LEFT_LIKE.getType(), 
				SingleValueComparator.LEFT_LIKE );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.LESS.getType(), 
				SingleValueComparator.LESS );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.LIKE.getType(), 
				SingleValueComparator.LIKE );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.NOT_EQUAL.getType(), 
				SingleValueComparator.NOT_EQUAL );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.NOT_GREATER.getType(), 
				SingleValueComparator.NOT_GREATER );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.NOT_LESS.getType(), 
				SingleValueComparator.NOT_LESS );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.NOT_LIKE.getType(), 
				SingleValueComparator.NOT_LIKE );
		compartorMap.put( org.ralasafe.user.SingleValueComparator.RIGHT_LIKE.getType(), 
				SingleValueComparator.RIGHT_LIKE );
	}
	
	public SelectConditionAdapter( UserMetadata metadata ) {
		columnMap=new HashMap();
		
		FieldMetadata[] fields=metadata.getMainTableMetadata().getFields();
		for( int i=0; i<fields.length; i++ ) {
			FieldMetadata fieldMetadata=fields[i];
			String columnName=fieldMetadata.getColumnName();
			ColumnAdapter adapter=new MapStorgeColumnAdapter( columnName, fieldMetadata.getJavaType() );
			Column column=new Column();
			column.setAdapter( adapter );
			column.setName( columnName );
			column.setType( new Type( fieldMetadata.getJavaType(), fieldMetadata.getSqlType() ) );
			
			columnMap.put( fieldMetadata.getName(), column );
		}
	}
	
	public SelectCondition adapter( UserSelectCondition userCdtn ) {
		if( userCdtn==null ) {
			return null;
		}
		
		SelectCondition cdtn=new SelectCondition();
		GroupPart gp=adapter( userCdtn.getGroupPart() );
		OrderPart op=adapter( userCdtn.getOrderPart() );
		WhereElement whereEmt=adapter( userCdtn.getWhereElement() );
		
		cdtn.setGroupPart( gp );
		cdtn.setOrderPart( op );
		cdtn.setWhereElement( whereEmt );
		
		return cdtn;
	}

	private WhereElement adapter( org.ralasafe.user.WhereElement whereElement ) {
		if( whereElement==null ) {
			return null;
		}
		
		if( whereElement instanceof org.ralasafe.user.FieldWhereElement ) {
			return adapter( (org.ralasafe.user.FieldWhereElement)whereElement );
		} else if( whereElement instanceof org.ralasafe.user.ComplexWhereElement ) {
			return adapter( (org.ralasafe.user.ComplexWhereElement) whereElement );
		}
		
		return null;
	}
	
	private ComplexWhereElement adapter( org.ralasafe.user.ComplexWhereElement whereElement ) {
		if( whereElement==null ) {
			return null;
		}
		
		ComplexWhereElement cpx=new ComplexWhereElement();
		cpx.setFirstPart( adapter( whereElement.getFirstPart() ) );
		cpx.setLinkedParts( adapter( whereElement.getLinkedParts() ) );
		return cpx;
	}
	
	private LWhereElement[] adapter( org.ralasafe.user.LWhereElement[] linkedParts ) {
		if( linkedParts==null ) {
			return null;
		}
		
		LWhereElement[] lwes=new LWhereElement[ linkedParts.length ];
		for( int i=0; i<linkedParts.length; i++ ) {
			LWhereElement lwe=new LWhereElement();
			lwe.setLinkType( linkedParts[i].getLinkType() );
			lwe.setWhereElement( adapter( linkedParts[i].getWhereElement() ) );
		}
		return lwes;
	}

	private WhereElement adapter( org.ralasafe.user.FieldWhereElement whereElement ) {
		if( whereElement==null ) {
			return null;
		}
		
		FieldWhereElement fwe=new FieldWhereElement();
		fwe.setCompartor( adapter( whereElement.getCompartor() ) );
		fwe.setContextValue( false );
		fwe.setValue( whereElement.getValue() );
		fwe.setColumn( (Column) columnMap.get( whereElement.getName() ) );
		return fwe;
	}
	
	private Comparator adapter( org.ralasafe.user.Comparator compartor ) {
		if( compartor==null ) {
			return null;
		}
		
		return (Comparator) compartorMap.get( compartor.getType() );
	}

	private OrderPart adapter( org.ralasafe.user.OrderPart orderPart ) {
		if( orderPart==null ) {
			return null;
		}
		
		OrderPart op=new OrderPart();
		op.setColumnNames( adapter( orderPart.getNames() ) );
		op.setOrderTypes( orderPart.getOrderTypes() );
		
		return op;
	}

	private GroupPart adapter( org.ralasafe.user.GroupPart groupPart ) {
		if( groupPart==null ) {
			return null;
		}
		
		GroupPart gp=new GroupPart();
		gp.setColumnNames( adapter( groupPart.getNames() ) );
		return gp;
	}

	private String[] adapter( String[] names ) {
		if( names==null ) {
			return null;
		}
		
		String[] columnNames=new String[names.length];
		for( int i=0; i<names.length; i++ ) {
			String name=names[i];
			Column column=(Column) columnMap.get( name );
			columnNames[i]=column.getName();
		}
		
		return columnNames;
	}
}
