/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.SystemConstant;
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
import org.ralasafe.db.sql.xml.Operator;
import org.ralasafe.db.sql.xml.QueryRef;
import org.ralasafe.db.sql.xml.SimpleValue;
import org.ralasafe.db.sql.xml.UserValue;
import org.ralasafe.db.sql.xml.Variable;
import org.ralasafe.db.sql.xml.Variable1;
import org.ralasafe.db.sql.xml.Variable2;
import org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType;
import org.ralasafe.db.sql.xml.types.FormulaTypeTypeType;
import org.ralasafe.db.sql.xml.types.SimpleOperatorType;
import org.ralasafe.db.sql.xml.types.SimpleValueTypeTypeType;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.util.StringUtil;

public abstract class AbstractPolicyDesignAction extends Action {
	private static final Log log=LogFactory.getLog( AbstractPolicyDesignAction.class );
	
	public abstract AbstractPolicyDesignHandler createPolicyHandler( HttpServletRequest req );
	public abstract String getPolicyHandlerAttributeKey( HttpServletRequest req );
	
	private AbstractPolicyDesignHandler getPolicyHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getPolicyHandlerAttributeKey( req );
		AbstractPolicyDesignHandler handler=(AbstractPolicyDesignHandler) req.getSession().getAttribute( key );
		
		if( handler==null||"loadFresh".equals(oper) ) {
			handler=createPolicyHandler( req );
		
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		} 
		
		handler.setDesignMode();
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractPolicyDesignHandler handler=getPolicyHandler( req );
		req.setAttribute( "handler", handler );
		
		if( "designVariables".equals( oper ) ) {
			DefineVariable[] variables=handler.getVariables();
			req.setAttribute( "variables", variables );
			
			WebUtil.forward( req, resp, handler.getVariablesPage() );
		} else if( "designExpr".equals( oper ) ) {
			WebUtil.forward( req, resp, handler.getExprTreePage() );
		} else if( "getVariable".equals( oper ) ) {
			DefineVariable[] variables=handler.getVariables();
			int index=WebUtil.getIntParameter( req, "index", 0 );
			
			DefineVariable var=variables[index];
			String gotoPage=getVariableDefinePage( var );
			
			String[] userFields=WebUtil.getUserType( req ).getUserMetadata().getUserFields();
			req.setAttribute( "userFields", userFields );
			req.setAttribute( "variable", var );
			
			WebUtil.forward( req, resp, gotoPage );
		} else if( "newVariable".equals( oper ) ) {
			String gotoPage=getVariableDefinePage( req );
			String[] userFields=WebUtil.getUserType( req ).getUserMetadata().getUserFields();
			req.setAttribute( "userFields", userFields );
			
			WebUtil.forward( req, resp, gotoPage );
		} else if( "return".equals( oper ) ) {
			String gotoPage=handler.getManagePage();
			
			// remove handler from session
			req.getSession().removeAttribute( getPolicyHandlerAttributeKey( req ) );
			
			// goto manage page
			resp.sendRedirect( gotoPage );
			return;
		} else if( "exprContextMenu".equals( oper ) ) {
			String nodeId=req.getParameter( "nodeId" );
			Object obj=handler.getExprItem( nodeId );
			req.setAttribute( "item", obj );
			WebUtil.forward( req, resp, handler.getExprContextMenuPage() );
		} else if( "loadExprForm".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			//String pId=req.getParameter( "pId" );
			String exprType=req.getParameter( "exprType" );
			
			if( !StringUtil.isEmpty( nodeId ) ) {
				Object obj=handler.getExprItem( nodeId );
				ExprGroupTypeItem item=(ExprGroupTypeItem) obj;
				req.setAttribute( "item", item );
				
				Object choiceValue=item.getChoiceValue();
				if( choiceValue instanceof BinaryExpr ) {
					exprType="binary";
				} else if( choiceValue instanceof InExpr
						|| choiceValue instanceof NotInExpr ) {
					exprType="in";
				} else if( choiceValue instanceof IsNullExpr
						|| choiceValue instanceof IsNotNullExpr ) {
					exprType="null";
				}  
			}
			
			if( "binary".equals( exprType ) ) {
				WebUtil.forward( req, resp, handler.getBinaryExprPage() );
			} else if( "in".equals( exprType ) ) {
				WebUtil.forward( req, resp, handler.getInExprPage() );
			} else if( "null".equals( exprType ) ) {
				WebUtil.forward( req, resp, handler.getNullExprPage() );
			} 
			
			return;
		} else {
			// default: goto design page
			WebUtil.forward( req, resp, handler.getDesignMainPage() );
		}
	}
	
	private String getVariableDefinePage( HttpServletRequest req ) {
		String varType=req.getParameter( "type" );
		
		if( "contextValue".equals( varType ) ) {
			return "/ralasafe/common/contextValue.jsp";
		} else if( "formula".equals( varType ) ) {
			return "/ralasafe/common/formula.jsp";
		} else if( "hintValue".equals( varType ) ) {
			return "/ralasafe/common/businessData.jsp";
		} else if( "query".equals( varType ) ) {
			return "/ralasafe/common/queryRef.jsp";
		} else if( "simpleValue".equals( varType ) ) {
			return "/ralasafe/common/simpleValue.jsp";
		} else if( "userValue".equals( varType ) ) {
			return "/ralasafe/common/userValue.jsp";
		} else if( "hintValue".equals( varType ) ) {
			return "/ralasafe/common/businessData.jsp";
		} else {
			throw new RalasafeException( "Unknown variable type" );
		}
	}
	
	private String getVariableDefinePage( DefineVariable var ) {
		if( var.getContextValue()!=null ) {
			return "/ralasafe/common/contextValue.jsp";
		} else if( var.getFormula()!=null ) {
			return "/ralasafe/common/formula.jsp";
		} else if( var.getHintValue()!=null ) {
			return "/ralasafe/common/businessData.jsp";
		} else if( var.getQueryRef()!=null ) {
			return "/ralasafe/common/queryRef.jsp";
		} else if( var.getSimpleValue()!=null ) {
			return "/ralasafe/common/simpleValue.jsp";
		} else if( var.getUserValue()!=null ) {
			return "/ralasafe/common/userValue.jsp";
		} else {
			throw new RalasafeException( "Unknown variable type" );
		}
	}
	
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper );
		}
		
		AbstractPolicyDesignHandler handler=getPolicyHandler( req );
		req.setAttribute( "handler", handler );
		
		if( "deleteVariable".equals( oper ) ) {
			int id=WebUtil.getIntParameter( req, "index", 0 );
			handler.deleteVariable( id );
		} else if( "addVariable".equals( oper ) ) {
			DefineVariable var=getDefineVariable( req );
			
			handler.addVariable( var );
		} else if( "updateVariable".equals( oper ) ) {
			DefineVariable var=getDefineVariable( req );
			
			int varIndex=WebUtil.getIntParameter( req, "index", -1 );
			handler.updateVariable( varIndex, var );
		}  else if( "loadExprTree".equals( oper ) ) {
			ExprGroup exprGroup=handler.getExprGroup();
			
			resp.setContentType("application/json;charset=UTF-8");
			ServletOutputStream out=resp.getOutputStream();
			
			String xml=handler.getExpressionTree( exprGroup );
			
			if( log.isDebugEnabled() ) {
				log.debug( "Expression tree json: " + xml );
			}
			
			out.println( xml );
			out.flush();
		} else if( "addExprChildExprGroup".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String type=req.getParameter( "type" );
			
			handler.addExprChildExprGroup( nodeId, type );
			return;
		} else if( "editExprGroup".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String type=req.getParameter( "type" );
			
			handler.editExprGroup( nodeId, type );
			return;
		} else if( "deleteExpr".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			
			handler.deleteExpr( nodeId );
			return;
		} else if( "addExprChild".equals( oper ) ) { 
			String pId=req.getParameter( "pId" );
			String exprType=req.getParameter( "exprType" );
			
			if( "binary".equals( exprType ) ) {
				BinaryExpr expr=getBinaryExpr( req );
				
				handler.addBinaryExpr( expr, pId );
			} else if( "null".equals( exprType ) ) {
				Variable var=getVariable( req );
				String operator=req.getParameter( "operator" );
				
				handler.addNullExpr( var, operator, pId );
			} else if( "in".equals( exprType ) ) {
				Variable var1=getVariable( req, 1 );
				Variable var2=getVariable( req, 2 );
				String operator=req.getParameter( "operator" );
				
				handler.addInExpr( var1, var2, operator, pId );
			}
			return;
		} else if( "editExpr".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String exprType=req.getParameter( "exprType" );
			
			if( "binary".equals( exprType ) ) {
				BinaryExpr expr=getBinaryExpr( req );
				
				handler.editBinaryExpr( expr, nodeId );
			} else if( "null".equals( exprType ) ) {
				Variable var=getVariable( req );
				
				String operator=req.getParameter( "operator" );
				handler.editNullExpr( var, operator, nodeId );
			} else if( "in".equals( exprType ) ) {
				Variable var1=getVariable( req, 1 );
				Variable var2=getVariable( req, 2 );
				String operator=req.getParameter( "operator" );
				
				handler.editInExpr( var1, var2, operator, nodeId );
			}
			return;
		} else if( "save".equals( oper ) ) {
			// remove handler from session
			req.getSession().removeAttribute( getPolicyHandlerAttributeKey( req ) );
			
			int id=WebUtil.getIntParameter( req, "id", -23 );
			try {
				handler.save( id );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			return;
		} 
	}
	
	private Variable getVariable( HttpServletRequest req ) {
		String varName=req.getParameter( "varName" );
		Variable var=new Variable();
		var.setName( varName );
		return var;
	}
	
	private Variable getVariable( HttpServletRequest req, int index ) {
		String varName=req.getParameter( "varName"+index );
		Variable var=new Variable();
		var.setName( varName );
		return var;
	}
	
	private BinaryExpr getBinaryExpr( HttpServletRequest req ) {
		String varName1=req.getParameter( "varName1" );
		String varName2=req.getParameter( "varName2" );
		String operator=req.getParameter( "operator" );
		
		Variable1 v1=new Variable1();
		v1.setName( varName1 );
		Variable2 v2=new Variable2();
		v2.setName( varName2 );
	
		Operator oper=new Operator();
		oper.setSimpleOperator( SimpleOperatorType.valueOf( operator ) );
		
		BinaryExpr expr=new BinaryExpr();
		expr.setVariable1( v1 );
		expr.setVariable2( v2 );
		expr.setOperator( oper );
		
		return expr;
	}
	private DefineVariable getDefineVariable( HttpServletRequest req ) {
		String type=req.getParameter( "type" );
		String name=req.getParameter( "name" );		
		
		DefineVariable var=new DefineVariable();
		var.setName( name );
		
		if( "userValue".equals( type ) ) {
			String key=req.getParameter( "key" );
			
			UserValue userValue=new UserValue();
			userValue.setKey( key );
			
			var.setUserValue( userValue );
		} else if( "simpleValue".equals( type ) ) {
			String content=req.getParameter( "content" );
			String stype=req.getParameter( "stype" );
			SimpleValue value=new SimpleValue();
			value.setContent( content );
			value.setType( SimpleValueTypeTypeType.valueOf( stype ) );
			
			var.setSimpleValue( value );
		} else if( "contextValue".equals( type ) ) {
			String key=req.getParameter( "key" );
			
			ContextValue value=new ContextValue();
			value.setKey( key );
			
			var.setContextValue( value );
		} else if( "hintValue".equals( type ) ) {
			String key=req.getParameter( "key" );
			
			HintValue value=new HintValue();
			value.setKey( key );
			value.setHint( SystemConstant.BUSINESS_DATA );
			
			var.setHintValue( value );
		} else if( "formula".equals( type ) ) {
			String operName1=req.getParameter( "operName1" );
			String operName2=req.getParameter( "operName2" );
			String operator=req.getParameter( "operator" );
			String returnType=req.getParameter( "returnType" );
			
			Variable var0=new Variable();
			var0.setName( operName1 );
			Variable var1=new Variable();
			var1.setName( operName2 );
			
			Formula value=new Formula();
			
			value.addVariable( var0 );
			value.addVariable( var1 );
			value.setOperator( FormulaTypeOperatorType.valueOf( operator ) );
			value.setType( FormulaTypeTypeType.valueOf( returnType ) );
			var.setFormula( value );
		} else if( "queryRef".equals( type ) ) {
			int id=WebUtil.getIntParameter( req, "queryId", -999 );
			QueryManager mng=WebUtil.getQueryManager( req );
			String queryName=mng.getQuery( id ).getName();
			
			QueryRef value=new QueryRef();
			value.setId( id );
			value.setName( queryName );
			
			var.setQueryRef( value );
		} 
		
		return var;
	}
}
