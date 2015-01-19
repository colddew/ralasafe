/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ralasafe.EntityExistException;
import org.ralasafe.db.sql.Value;
import org.ralasafe.db.sql.xml.BinaryExpr;
import org.ralasafe.db.sql.xml.ContextValue;
import org.ralasafe.db.sql.xml.DefineVariable;
import org.ralasafe.db.sql.xml.ExprGroup;
import org.ralasafe.db.sql.xml.ExprGroupTypeItem;
import org.ralasafe.db.sql.xml.Formula;
import org.ralasafe.db.sql.xml.HintValue;
import org.ralasafe.db.sql.xml.InExpr;
import org.ralasafe.db.sql.xml.IsNotNullExpr;
import org.ralasafe.db.sql.xml.IsNullExpr;
import org.ralasafe.db.sql.xml.NotInExpr;
import org.ralasafe.db.sql.xml.QueryRef;
import org.ralasafe.db.sql.xml.SimpleValue;
import org.ralasafe.db.sql.xml.UserValue;
import org.ralasafe.db.sql.xml.Variable;
import org.ralasafe.db.sql.xml.Variable1;
import org.ralasafe.db.sql.xml.Variable2;
import org.ralasafe.db.sql.xml.types.LinkerType;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.entitle.ScriptTestResult;
import org.ralasafe.script.AbstractPolicy;
import org.ralasafe.user.User;
import org.ralasafe.util.StringUtil;
import org.ralasafe.util.Util;

public abstract class AbstractPolicyDesignHandler {
	private String designMainPage="/ralasafe/common/policyDesignTemplate.jsp";
	private String rawMainPage="/ralasafe/common/policyRawTemplate.jsp";
	private String variablesPage="/ralasafe/common/variables.jsp";
	private String exprTreePage="/ralasafe/common/exprTree.jsp";
	private String exprContextMenuPage="/ralasafe/common/exprContextMenu.jsp";
	private String binaryExprPage="/ralasafe/common/editBinaryExpr.jsp";
	private String inExprPage="/ralasafe/common/editInExpr.jsp";
	private String nullExprPage="/ralasafe/common/editNullExpr.jsp";
	private String testMainPage="/ralasafe/common/policyTest.jsp";
	private String showTestResultPage="/ralasafe/common/showTestResult.jsp";
	private String businessDataClass;
	
	private AbstractPolicy policy;
	private QueryManager queryManager;
	
	public String getTestMainPage() {
		return testMainPage;
	}
	public void setTestMainPage( String testMainPage ) {
		this.testMainPage=testMainPage;
	}
	public String getShowTestResultPage() {
		return showTestResultPage;
	}
	public void setShowTestResultPage( String showTestResultPage ) {
		this.showTestResultPage=showTestResultPage;
	}
	public String getBinaryExprPage() {
		return binaryExprPage;
	}
	public void setBinaryExprPage( String binaryExprPage ) {
		this.binaryExprPage=binaryExprPage;
	}
	public String getInExprPage() {
		return inExprPage;
	}
	public void setInExprPage( String inExprPage ) {
		this.inExprPage=inExprPage;
	}
	public String getNullExprPage() {
		return nullExprPage;
	}
	public void setNullExprPage( String nullExprPage ) {
		this.nullExprPage=nullExprPage;
	}
	public abstract String getPolicyType();
	
	public String getDesignMainPage() {
		return designMainPage;
	}
	public void setDesignMainPage( String designMainPage ) {
		this.designMainPage=designMainPage;
	}
	public String getRawMainPage() {
		return rawMainPage;
	}
	public void setRawMainPage( String rawMainPage ) {
		this.rawMainPage=rawMainPage;
	}
	public String getVariablesPage() {
		return variablesPage;
	}

	public void setVariablesPage( String variablesPage ) {
		this.variablesPage=variablesPage;
	}

	public abstract String getDesignPageTitle();
	public abstract String getRawPageTitle();
	public abstract String getManagePage();

	public String getExprContextMenuPage() {
		return exprContextMenuPage;
	}
	public void setExprContextMenuPage( String exprContextMenuPage ) {
		this.exprContextMenuPage=exprContextMenuPage;
	}
	public abstract DefineVariable[] getVariables();

	public abstract void deleteVariable( int id );

	public abstract void addVariable( DefineVariable var );

	public abstract void updateVariable( int varIndex, DefineVariable var );

	public abstract ExprGroup getExprGroup();
	
	public String getExprTreePage() {
		return exprTreePage;
	}
	public void setExprTreePage( String exprTreePage ) {
		this.exprTreePage=exprTreePage;
	}
	public String getExpressionTree( ExprGroup exprGroup ) {
		StringBuffer buff=new StringBuffer();
		buff.append( "[" );
		
		buff.append( "{id:'0',pId:'-1', iconSkin: 'exprGroup', name:'Root Expr group (" )
			.append( exprGroup.getLinker() )
			.append( ")', open:true}" );
		
		ExprGroupTypeItem[] items=exprGroup.getExprGroupTypeItem();
		if( items!=null ) {
			for( int i=0; i<items.length; i++ ) {
				ExprGroupTypeItem item=items[i];
				
				Object value=item.getChoiceValue();
				if( value instanceof BinaryExpr ) {
					print( (BinaryExpr) value, buff, "0", i );
				} else if( value instanceof ExprGroup ) {
					print( (ExprGroup) value, buff, "0", i );
				} else if( value instanceof InExpr ) {
					print( (InExpr) value, buff, "0", i );
				} else if( value instanceof IsNullExpr ) {
					print( (IsNullExpr) value, buff, "0", i );
				} else if( value instanceof IsNotNullExpr ) {
					print( (IsNotNullExpr) value, buff, "0", i );
				} else if( value instanceof NotInExpr ) {
					print( (NotInExpr) value, buff, "0", i );
				} 
			}
		}
		
		buff.append( "]" );
		return buff.toString();
	}
	
	private void print( NotInExpr value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getVariable1() ) )
			.append( " NOT IN " )
			.append( format( value.getVariable2() ) )
			.append( "'}" );
	}

	private Object format( Variable1 variable1 ) {
		return variable1.getName();
	}

	private String format( Variable2 variable2 ) {
		return variable2.getName();
	}

	private void print( IsNotNullExpr value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getVariable() ) )
			.append( " NOT NULL" )
			.append( "'}" );
	}

	private Object format( Variable variable ) {
		return variable.getName();
	}

	private void print( IsNullExpr value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getVariable() ) )
			.append( " NULL" )
			.append( "'}" );
	}

	private void print( InExpr value, StringBuffer buff, 
			String pid,	int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format( value.getVariable1() ) )
			.append( " IN " )
			.append( format( value.getVariable2() ) )
			.append( "'}" );
	}

	private void print( BinaryExpr value, StringBuffer buff,
			String pid, int i ) {
		String nodeId=pid+"-"+i;
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'expr',pId:'"+pid+"', name:'" )
			.append( format(value.getVariable1() ) )
			.append( value.getOperator().getSimpleOperator() )
			.append( format(value.getVariable2() ) )
			.append( "'}" );
	}

	private void print( ExprGroup exprGroup, StringBuffer buff,
			String pid, int xPosition ) {
		String nodeId=pid+"-"+xPosition;
		// print this node
		buff.append( ",{id:'"+nodeId+"',iconSkin: 'exprGroup',pId:'"+pid+"', name:'Expr group (" )
		.append( exprGroup.getLinker() )
		.append( ")', open:true}" );
		
		// print children items
		ExprGroupTypeItem[] items=exprGroup.getExprGroupTypeItem();
		if( items!=null ) {
			for( int i=0; i<items.length; i++ ) {
				ExprGroupTypeItem item=items[i];
				
				Object value=item.getChoiceValue();
				if( value instanceof BinaryExpr ) {
					print( (BinaryExpr) value, buff, nodeId, i );
				} else if( value instanceof ExprGroup ) {
					print( (ExprGroup) value, buff, nodeId, i );
				} else if( value instanceof InExpr ) {
					print( (InExpr) value, buff, nodeId, i );
				} else if( value instanceof IsNullExpr ) {
					print( (IsNullExpr) value, buff, nodeId, i );
				} else if( value instanceof IsNotNullExpr ) {
					print( (IsNotNullExpr) value, buff, nodeId, i );
				} else if( value instanceof NotInExpr ) {
					print( (NotInExpr) value, buff, nodeId, i );
				} 
			}
		}
	}
	
	
	public String[] format( DefineVariable var ) {
		Object value=var.getChoiceValue();
		
		if( value instanceof ContextValue ) {
			ContextValue c=(ContextValue) value;
			return new String[]{
					"Context_value",
					var.getName(),
					c.getKey()};
		} else if( value instanceof Formula ) {
			Formula f=(Formula) value;
			return new String[] {
					"Formula",
					var.getName(),
					f.getVariable( 0 ).getName()+f.getOperator()+f.getVariable( 1 ).getName()
			};
		} else if( value instanceof HintValue ) {
			return new String[] {
					"Business_data_attribute",
					var.getName(),
					( (HintValue) value ).getKey()
			};
		} else if( value instanceof QueryRef ) {
			return new String[] {
					"Query",
					var.getName(),
					( (QueryRef) value ).getName() };
		} else if( value instanceof SimpleValue ) {
			return new String[] {
					"Simple_value",
					var.getName(),
					( (SimpleValue) value ).getContent()
			};
		} else if( value instanceof UserValue ) {
			return new String[] {
					"User_value",
					var.getName(),
					( (UserValue) value ).getKey()};
		} else {
			return new String[] {
					"Unknown_type",
					var.getName(),
					""};
		}
	}
	
	public Object getExprItem( String nodeId ) {
		ExprGroup group=getExprGroup();
		
		if( "0".equals( nodeId ) ) {
			return group;
		} else {
			return getExprItem( group, nodeId );
		}
	}
	
	private Object getExprItem( ExprGroup group,
			String nodeId ) {
		int indexOf=nodeId.indexOf( "-" );
		
		String childNodeId=nodeId.substring( indexOf+1 );
		indexOf=childNodeId.indexOf( "-" );
		
		if( indexOf>0 ) {
			int xPosition=Integer.parseInt( childNodeId.substring( 0, indexOf ) );
			
			ExprGroupTypeItem item=group.getExprGroupTypeItem( xPosition );
			
			return getExprItem( item.getExprGroup(), childNodeId );
		} else {
			int xPosition=Integer.parseInt( childNodeId );
			
			return group.getExprGroupTypeItem( xPosition );
		}
	}
	
	public void addExprChildExprGroup( String nodeId, String type ) {
		Object obj=getExprItem( nodeId );
		ExprGroup parent=toGroup( obj );
		ExprGroup newGrp=new ExprGroup();
		newGrp.setLinker( LinkerType.valueOf( type ) );
		ExprGroupTypeItem newItem=new ExprGroupTypeItem();
		newItem.setExprGroup( newGrp );
		
		parent.addExprGroupTypeItem( newItem );
	}

	public void editExprGroup( String nodeId, String type ) {
		Object obj=getExprItem( nodeId );
		ExprGroup group=toGroup( obj );
		group.setLinker( LinkerType.valueOf( type ) );
	}
	
	private ExprGroup toGroup( Object obj ) {
		if( obj instanceof ExprGroup ) {
			ExprGroup grp=(ExprGroup) obj;
			return grp;
		} else {
			ExprGroupTypeItem item=(ExprGroupTypeItem) obj;
			return item.getExprGroup();
		}
	}
	
	public void deleteExpr( String nodeId ) {
		int index=nodeId.lastIndexOf( "-" );
		String pId=nodeId.substring( 0,index );
		int xPosition=Integer.parseInt( nodeId.substring( index+1 ) );
		
		Object obj=getExprItem( pId );
		ExprGroup parent=toGroup( obj );
		
		parent.removeExprGroupTypeItemAt( xPosition );
	}

	public void addBinaryExpr( BinaryExpr expr, String pId ) {
		Object item=getExprItem( pId );
		ExprGroup group=toGroup( item );
		
		ExprGroupTypeItem newItem=new ExprGroupTypeItem();
		newItem.setBinaryExpr( expr );
		group.addExprGroupTypeItem( newItem );
	}

	public void editBinaryExpr( BinaryExpr expr, String nodeId ) {
		Object obj=getExprItem( nodeId );
		ExprGroupTypeItem item=(ExprGroupTypeItem) obj;
		
		item.setBinaryExpr( expr );
	}

	public void editNullExpr( Variable variable, String operator, String nodeId ) {
		Object obj=getExprItem( nodeId );
		ExprGroupTypeItem item=(ExprGroupTypeItem) obj;
		
		if( "NULL".equals( operator ) ) {
			IsNullExpr expr=new IsNullExpr();
			expr.setVariable( variable );
			item.setIsNotNullExpr( null );
			item.setIsNullExpr( expr );
		} else {
			IsNotNullExpr expr=new IsNotNullExpr();
			expr.setVariable( variable );
			item.setIsNullExpr( null );
			item.setIsNotNullExpr( expr );
		}
	}

	public void addNullExpr( Variable variable, String operator, String pId ) {
		Object item=getExprItem( pId );
		ExprGroup group=toGroup( item );
		
		ExprGroupTypeItem newItem=new ExprGroupTypeItem();
		if( "NULL".equals( operator ) ) {
			IsNullExpr expr=new IsNullExpr();
			expr.setVariable( variable );
			newItem.setIsNullExpr( expr );
		} else {
			IsNotNullExpr expr=new IsNotNullExpr();
			expr.setVariable( variable );
			newItem.setIsNotNullExpr( expr );
		}
		group.addExprGroupTypeItem( newItem );
	}
	
	public abstract void save( int id ) throws EntityExistException;
	public abstract void setDesignMode();
	public abstract void setRawMode();
	public abstract void setRawScript( String script );
	public abstract String getRawScript();
	public abstract AbstractPolicy transferXml2Policy();
	
	public void prepareTest( QueryManager queryMng ) {
		this.policy=transferXml2Policy();
		this.queryManager=queryMng;
	}
	
	public QueryManager getQueryManager() {
		return queryManager;
	}
	public AbstractPolicy getPolicy() {
		return policy;
	}
	
	public String getScript() {
		if( policy.isRawScript() ) {
			return policy.getRawScript().toScript();
		} else {
			return policy.getExprGroup().toScript();
		}
	}
	
	public abstract ScriptTestResult run( User user, Object businessData, Map context );
	
	public String[] getTestContextFields() {
		DefineVariable[] variables=getVariables();
		List fields=new ArrayList(variables.length);
		
		Util.extractContextValueFields( variables, queryManager, fields );

		return (String[]) fields.toArray( new String[0] );
	}
	
	public String[] getTestBusinessDataFields() {
		DefineVariable[] variables=getVariables();
		List fields=new ArrayList(variables.length);
		Util.extractBusinessDataFields( variables, fields );
		
		return (String[]) fields.toArray( new String[0] );
	}
	
	public boolean isTestUserNeeded() {
		DefineVariable[] variables=getVariables();
		
		for( int i=0; i<variables.length; i++ ) {
			DefineVariable var=variables[i];
			
			if( var.getUserValue()!=null ) {
				return true;
			} else if( var.getQueryRef()!=null ) {
				int refId=var.getQueryRef().getId();
				
				Query query=queryManager.getQuery( refId );
				ArrayList values=query.getSqlQuery().getValues();
				
				for( Iterator iter=values.iterator(); iter.hasNext(); ) {
					Value v=(Value) iter.next();
					
					if( v instanceof org.ralasafe.db.sql.UserValue ) {
						return true;				
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isTestContextNeeded() {
		return getTestContextFields().length>0;
	}
	public boolean isTestBusinessDataNeeded() {
		return getTestBusinessDataFields().length>0;
	}
	public void setBusinessDataClass( String bdClass ) {
		this.businessDataClass=bdClass;
	}
	public String getBusinessDataClass() {
		return businessDataClass;
	}
	
	public String[] getTestBusinessDataFieldTypes() {
		String[] fields=getTestBusinessDataFields();
		String[] types=new String[fields.length];
		
		if( !StringUtil.isEmpty( businessDataClass ) ) {
			String[][] reflectJavaBean=Util.reflectJavaBean( businessDataClass );
			
			for( int i=0; i<fields.length; i++ ) {
				String field=fields[i];
				
				for( int j=0; j<reflectJavaBean.length; j++ ) {
					String[] strings=reflectJavaBean[j];
					
					if( strings[0].equals( field ) ) {
						types[i]=strings[1];
						j=reflectJavaBean.length;
					}
				}
			}
		}
		
		return types;
	}
	public void addInExpr( Variable var1, Variable var2, String operator,
			String pId ) {
		Object item=getExprItem( pId );
		ExprGroup group=toGroup( item );
		
		Variable1 var_1=new Variable1();
		var_1.setName( var1.getName() );
		Variable2 var_2=new Variable2();
		var_2.setName( var2.getName() );
		
		ExprGroupTypeItem newItem=new ExprGroupTypeItem();
		if( "IN".equals( operator ) ) {
			InExpr expr=new InExpr();			
			expr.setVariable1( var_1 );
			expr.setVariable2( var_2 );
			
			newItem.setInExpr( expr );
		} else {
			NotInExpr expr=new NotInExpr();			
			expr.setVariable1( var_1 );
			expr.setVariable2( var_2 );
			
			newItem.setNotInExpr( expr );
		}
		group.addExprGroupTypeItem( newItem );
	}
	
	public void editInExpr( Variable var1, Variable var2, String operator,
			String nodeId ) {
		Object obj=getExprItem( nodeId );
		ExprGroupTypeItem item=(ExprGroupTypeItem) obj;
		
		Variable1 var_1=new Variable1();
		var_1.setName( var1.getName() );
		Variable2 var_2=new Variable2();
		var_2.setName( var2.getName() );
		
		if( "IN".equals( operator ) ) {
			InExpr expr=new InExpr();			
			expr.setVariable1( var_1 );
			expr.setVariable2( var_2 );
			
			item.setNotInExpr( null );
			item.setInExpr( expr );
		} else {
			NotInExpr expr=new NotInExpr();			
			expr.setVariable1( var_1 );
			expr.setVariable2( var_2 );
			
			item.setInExpr( null );
			item.setNotInExpr( expr );
		}
	}
}
