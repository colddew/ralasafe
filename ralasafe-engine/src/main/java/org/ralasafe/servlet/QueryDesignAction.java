/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package org.ralasafe.servlet;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ralasafe.EntityExistException;
import org.ralasafe.RalasafeException;
import org.ralasafe.db.sql.xml.BinaryExpression;
import org.ralasafe.db.sql.xml.Column;
import org.ralasafe.db.sql.xml.ContextValue;
import org.ralasafe.db.sql.xml.ExpressionGroupTypeItem;
import org.ralasafe.db.sql.xml.InExpression;
import org.ralasafe.db.sql.xml.IsNotNullExpression;
import org.ralasafe.db.sql.xml.IsNullExpression;
import org.ralasafe.db.sql.xml.NotInExpression;
import org.ralasafe.db.sql.xml.Operand;
import org.ralasafe.db.sql.xml.Operand1;
import org.ralasafe.db.sql.xml.Operand2;
import org.ralasafe.db.sql.xml.Operator;
import org.ralasafe.db.sql.xml.QueryType;
import org.ralasafe.db.sql.xml.SimpleValue;
import org.ralasafe.db.sql.xml.Table;
import org.ralasafe.db.sql.xml.UserValue;
import org.ralasafe.db.sql.xml.Value;
import org.ralasafe.db.sql.xml.types.QueryTypeTypeType;
import org.ralasafe.db.sql.xml.types.SimpleOperatorType;
import org.ralasafe.db.sql.xml.types.SimpleValueTypeTypeType;
import org.ralasafe.entitle.Query;
import org.ralasafe.entitle.QueryManager;
import org.ralasafe.util.StringUtil;

import com.google.gson.Gson;

public class QueryDesignAction extends Action {
	private static final Log log=LogFactory.getLog( QueryDesignAction.class );
	public QueryDesignHandler createHandler( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		QueryManager queryManager=WebUtil.getQueryManager( req );
		Query query=queryManager.getQuery( id );
		
		org.ralasafe.db.sql.xml.QueryType xmlQuery;
		try {
			xmlQuery = org.ralasafe.db.sql.xml.Query
					.unmarshal(new StringReader(query.getXmlContent()));
		} catch (Exception e) {
			throw new RalasafeException(e);
		}
		
		return new QueryDesignHandler( xmlQuery );
	}
	
	public String getHandlerAttributeKey( HttpServletRequest req ) {
		int id=WebUtil.getIntParameter( req, "id", 0 );
		return "_$ralasafeQuery_" + id;
	}
	
	private QueryDesignHandler getHandler( HttpServletRequest req ) {
		String oper=req.getParameter( "oper" );
		String key=getHandlerAttributeKey( req );
		QueryDesignHandler handler=null;
		Object obj=req.getSession().getAttribute( key );
		
		if( obj==null||"loadFresh".equals(oper) ) {
			handler=createHandler( req );
		
			// save into session, will be removed when policy is saved
			req.getSession().setAttribute( key, handler );
		} else if( obj instanceof QueryRawHandler ) {
			QueryRawHandler rawHandler=(QueryRawHandler) obj;
			QueryType query=rawHandler.getQuery();
			handler=new QueryDesignHandler( query );
			req.getSession().setAttribute( key, handler );
			
			// set to design sql type
			query.setIsRawSQL( false );
			query.setType( QueryTypeTypeType.SQL );
		} else if( obj instanceof QueryTestHandler ) {
			QueryTestHandler rawHandler=(QueryTestHandler) obj;
			QueryType query=rawHandler.getQuery();
			handler=new QueryDesignHandler( query );
			req.getSession().setAttribute( key, handler );
		} else {
			handler=(QueryDesignHandler) obj;
		}
		
		return handler;
	}
	
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );		
		String tableAlias=req.getParameter( "tableAlias" );
		String columnName=req.getParameter( "columnName" );
		QueryDesignHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() 
					+ ", tableAlias=" + tableAlias + ", columnName=" + columnName );
		}
		
		
		
		if( "designTables".equals( oper ) ) {
			WebUtil.forward( req, resp, "/ralasafe/query/tables.jsp" );
			return;
		} else if( "viewTable".equals( oper ) ) {
			Table[] tables=handler.getQuery().getQueryTypeSequence().getFrom().getTable();
			for( int i=0; i<tables.length; i++ ) {
				Table table=tables[i];
				
				if( table.getAlias().equals( tableAlias ) ) {
					req.setAttribute( "table", table );
					i=tables.length;
				}
			}
			WebUtil.forward( req, resp, "/ralasafe/query/table.jsp" );
			return;
		} else if( "getColumn".equals( oper ) ) { 
			Column column=handler.getColumn( tableAlias, columnName );
			String[][] mappingClassPropertyAndTypes=handler.getMappingClassPropertyAndTypes();
			
			req.setAttribute( "column", column );
			req.setAttribute( "mappingClassPropertyAndTypes", mappingClassPropertyAndTypes );
			
			WebUtil.forward( req, resp, "/ralasafe/query/editColumn.jsp" );			
			return;
		} else if( "getGroupColumn".equals( oper ) ) { 
			Column column=handler.getGroupColumn( tableAlias, columnName );
			
			req.setAttribute( "column", column );
			WebUtil.forward( req, resp, "/ralasafe/query/editGroupColumn.jsp" );			
			return;
		} else if( "getOrderColumn".equals( oper ) ) { 
			Column column=handler.getOrderColumn( tableAlias, columnName );
			
			req.setAttribute( "column", column );
			WebUtil.forward( req, resp, "/ralasafe/query/editOrderColumn.jsp" );			
			return;
		} else if( "designGroup".equals( oper ) ) { 
			WebUtil.forward( req, resp, "/ralasafe/query/group.jsp" );
			return;
		} else if( "designOrder".equals( oper ) ) { 
			WebUtil.forward( req, resp, "/ralasafe/query/order.jsp" );
			return;
		} else if( "designWhere".equals( oper ) ) { 
			WebUtil.forward( req, resp, "/ralasafe/query/where.jsp" );
			return;
		} else if( "whereExprContextMenu".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			Object item=handler.getWhereExprItem( nodeId );
			
			req.setAttribute( "item", item );
			WebUtil.forward( req, resp, "/ralasafe/query/whereExprContextMenu.jsp" );
			return;
		} else if( "loadWhereExprForm".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			//String pId=req.getParameter( "pId" );
			String exprType=req.getParameter( "exprType" );
			
			if( !StringUtil.isEmpty( nodeId ) ) {
				Object obj=handler.getWhereExprItem( nodeId );
				ExpressionGroupTypeItem item=(ExpressionGroupTypeItem) obj;
				req.setAttribute( "item", item );
				
				Object choiceValue=item.getChoiceValue();
				if( choiceValue instanceof BinaryExpression ) {
					exprType="binary";
				} else if( choiceValue instanceof InExpression
						|| choiceValue instanceof NotInExpression ) {
					exprType="in";
				} else if( choiceValue instanceof IsNullExpression
						|| choiceValue instanceof IsNotNullExpression ) {
					exprType="null";
				}  
			}
			
			String[] userFields=WebUtil.getUserType( req ).getUserMetadata().getUserFields();
			req.setAttribute( "userFields", userFields );
			
			if( "binary".equals( exprType ) ) {
				WebUtil.forward( req, resp, "/ralasafe/query/editWhereBinaryExpr.jsp" );
			} else if( "in".equals( exprType ) ) {
				WebUtil.forward( req, resp, "/ralasafe/query/editWhereInExpr.jsp" );
			} else if( "null".equals( exprType ) ) {
				WebUtil.forward( req, resp, "/ralasafe/query/editWhereNullExpr.jsp" );
			} 
			
			return;
		} else if( "return".equals( oper ) ) {
			String gotoPage=handler.getManagePage();
			
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			// goto manage page
			resp.sendRedirect( gotoPage );
			return;
		} else {
			WebUtil.forward( req, resp, "/ralasafe/query/design.jsp" );
			return;
		}		
	}

	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String oper=req.getParameter( "oper" );
		String tableAlias=req.getParameter( "tableAlias" );
		String columnName=req.getParameter( "columnName" );
		QueryDesignHandler handler=getHandler( req );
		req.setAttribute( "handler", handler );
		
		if( log.isDebugEnabled() ) {
			log.debug( "oper=" + oper + ", query="+handler.getQuery().getName() 
					+ ", tableAlias=" + tableAlias + ", columnName=" + columnName );
		}
				
		if( "checkTableColumns".equals( oper ) ) {
			handler.addTableColumns( tableAlias );
			return;
		} else if( "unCheckTableColumns".equals( oper ) ) {
			handler.deleteTableColumns( tableAlias );
			return;
		} else if( "deleteTable".equals( oper ) ) {
			handler.deleteTable( tableAlias );
			return;
		} else if( "addTable".equals( oper ) ) {
			String schema=req.getParameter( "schema" );
			String tableName=req.getParameter( "tableName" );
			String alias=handler.addTable( schema.trim(), tableName.trim() );
			
			Gson gson=new Gson();
			String json=gson.toJson( alias );
			resp.setContentType( "application/json" );
			resp.getWriter().print( json );
			return;
		} else if( "setMappingClass".equals( oper ) ) {
			String mappingClass=req.getParameter( "mappingClass" );
			handler.setMappingClass( mappingClass );
			return;
		} else if( "deleteColumn".equals( oper ) ) {
			handler.deleteColumn( tableAlias, columnName );
			return;
		} else if( "addColumn".equals( oper ) ) {
			handler.addColumn( tableAlias, columnName );
			return;
		} else if( "updateTableColumn".equals( oper ) ) {
			String function=req.getParameter( "function" );
			String property=req.getParameter( "property" );
			boolean readOnly=WebUtil.getBooleanParameter( req, "readOnly", false );
			
			int index=property.indexOf( "<" );
			String javaProp=property.substring( 0, index-1 ).trim();
			String javaType=property.substring( index+1, property.length()-1 ).trim();
			handler.changeColumnMapping( tableAlias, columnName, function, javaProp, javaType, readOnly );
			return;
		} else if( "moveGroupColumn".equals( oper ) ) {
			String direct=req.getParameter( "direct" );
			
			handler.moveGroupColumn( direct, tableAlias, columnName );
			return;
		} else if( "deleteGroupColumn".equals( oper ) ) {
			handler.deleteGroupColumn( tableAlias, columnName );
			return;
		} else if( "editGroupColumn".equals( oper ) ) {
			String aliasColumn=req.getParameter( "aliasColumn" );
			int firstIndex=aliasColumn.indexOf( "[" );
			int lastIndex=aliasColumn.indexOf( "]" );

			tableAlias=aliasColumn.substring( firstIndex+1,lastIndex );
			columnName=aliasColumn.substring( lastIndex+2 );
			
			int index=WebUtil.getIntParameter( req, "index", 0 );
			handler.editGroupColumn( index, tableAlias, columnName );
			return;
		} else if( "addGroupColumn".equals( oper ) ) {
			String aliasColumn=req.getParameter( "aliasColumn" );
			int firstIndex=aliasColumn.indexOf( "[" );
			int lastIndex=aliasColumn.indexOf( "]" );

			tableAlias=aliasColumn.substring( firstIndex+1,lastIndex );
			columnName=aliasColumn.substring( lastIndex+2 );
			
			handler.addGroupColumn( tableAlias, columnName );
			return;
		} else if( "moveOrderColumn".equals( oper ) ) {
			String direct=req.getParameter( "direct" );
			
			handler.moveOrderColumn( direct, tableAlias, columnName );
			return;
		} else if( "deleteOrderColumn".equals( oper ) ) {
			handler.deleteOrderColumn( tableAlias, columnName );
			return;
		} else if( "editOrderColumn".equals( oper ) ) {
			String aliasColumn=req.getParameter( "aliasColumn" );
			String orderType=req.getParameter( "orderType" );
			int firstIndex=aliasColumn.indexOf( "[" );
			int lastIndex=aliasColumn.indexOf( "]" );

			tableAlias=aliasColumn.substring( firstIndex+1,lastIndex );
			columnName=aliasColumn.substring( lastIndex+2 );
			
			int index=WebUtil.getIntParameter( req, "index", 0 );
			handler.editOrderColumn( index, tableAlias, columnName, orderType );
			return;
		} else if( "addOrderColumn".equals( oper ) ) {
			String aliasColumn=req.getParameter( "aliasColumn" );
			String orderType=req.getParameter( "orderType" );
			int firstIndex=aliasColumn.indexOf( "[" );
			int lastIndex=aliasColumn.indexOf( "]" );

			tableAlias=aliasColumn.substring( firstIndex+1,lastIndex );
			columnName=aliasColumn.substring( lastIndex+2 );
			
			handler.addOrderColumn( tableAlias, columnName, orderType );
			return;
		} else if( "loadWhereExprGroup".equals( oper ) ) { 
			String xml=handler.getWhere();
			
			resp.setContentType("application/json;charset=UTF-8");
			ServletOutputStream out=resp.getOutputStream();
			out.print( xml );
			return;
		} else if( "addWhereChildExprGroup".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String type=req.getParameter( "type" );
			
			handler.addWhereChildExprGroup( nodeId, type );
			return;
		} else if( "editWhereExprGroup".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String type=req.getParameter( "type" );
			
			handler.editWhereExprGroup( nodeId, type );
			return;
		} else if( "deleteWhereExpr".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			
			handler.deleteWhereExpr( nodeId );
			return;
		} else if( "addWhereExpr".equals( oper ) ) { 
			String pId=req.getParameter( "pId" );
			String exprType=req.getParameter( "exprType" );
			
			if( "binary".equals( exprType ) ) {
				BinaryExpression expr=getBinaryExpression( req );
				handler.addBinaryExpression( expr, pId );
			} else if( "null".equals( exprType ) ) {
				Column column=getWhereExprInColumn( req );
				
				String operator=req.getParameter( "operator" );
				handler.addNullExpression( column, operator, pId );
			}
			return;
		} else if( "editWhereExpr".equals( oper ) ) { 
			String nodeId=req.getParameter( "nodeId" );
			String exprType=req.getParameter( "exprType" );
			
			if( "binary".equals( exprType ) ) {
				BinaryExpression expr=getBinaryExpression( req );
				handler.editBinaryExpression( expr, nodeId );
			} else if( "null".equals( exprType ) ) {
				Column column=getWhereExprInColumn( req );
				
				String operator=req.getParameter( "operator" );
				handler.editNullExpression( column, operator, nodeId );
			}
			return;
		} else if( "save".equals( oper ) ) {
			// remove handler from session
			req.getSession().removeAttribute( getHandlerAttributeKey( req ) );
			
			int id=WebUtil.getIntParameter( req, "id", -23 );
			QueryManager queryManager=WebUtil.getQueryManager( req );
			try {
				handler.save( id, queryManager );
			} catch( EntityExistException e ) {
				log.error( "", e );
				throw new ServletException( e );
			}
			
			return;
		} 
	}

	private Column getWhereExprInColumn( HttpServletRequest req ) {
		String columnWithAlias=req.getParameter( "column" );
		int index1=columnWithAlias.indexOf( "[" );
		int index2=columnWithAlias.indexOf( "]" );
		int index3=columnWithAlias.lastIndexOf( "." );
		
		String exprTableAlias=columnWithAlias.substring( index1+1,index2 ).trim();
		String exprColumnName=columnWithAlias.substring( index3+1 ).trim();
		
		Column column=new Column();
		column.setName( exprColumnName );
		column.setTableAlias( exprTableAlias );
		
		return column;
	}

	private BinaryExpression getBinaryExpression( HttpServletRequest req ) {
		Operand innerOperand1=getOperand( req, 1 );
		Operand innerOperand2=getOperand( req, 2 );
		String strOperator=req.getParameter( "operator" );
		
		BinaryExpression expr=new BinaryExpression();
		Operand1 operand1=new Operand1();
		operand1.setOperand( innerOperand1 );
		Operand2 operand2=new Operand2();
		operand2.setOperand( innerOperand2 );
		Operator operator=new Operator();
		operator.setSimpleOperator( SimpleOperatorType.valueOf( strOperator ) );
		
		expr.setOperand1( operand1 );
		expr.setOperand2( operand2 );
		expr.setOperator( operator );
		return expr;
	}

	private Operand getOperand( HttpServletRequest req, int i ) {
		String type=req.getParameter( "operand"+i+"Type" );
		
		Operand operand=new Operand();
		if( "column".equals( type ) ) {
			String columnWithAlias=req.getParameter( "column"+i );
			int index1=columnWithAlias.indexOf( "[" );
			int index2=columnWithAlias.indexOf( "]" );
			int index3=columnWithAlias.lastIndexOf( "." );
			
			String tableAlias=columnWithAlias.substring( index1+1,index2 ).trim();
			String columnName=columnWithAlias.substring( index3+1 ).trim();
			
			Column column=new Column();
			column.setName( columnName );
			column.setTableAlias( tableAlias );
			operand.setColumn( column );
		} else if( "contextValue".equals( type ) ) {
			String str=req.getParameter( "contextValue"+i );
			
			ContextValue ctxValue=new ContextValue();
			ctxValue.setKey( str );
			Value value=new Value();
			value.setContextValue( ctxValue );
			operand.setValue( value );
		} else if( "userValue".equals( type ) ) {
			String str=req.getParameter( "userValue"+i );
			
			UserValue uValue=new UserValue();
			uValue.setKey( str );
			Value value=new Value();
			value.setUserValue( uValue );
			operand.setValue( value );
		} else if( "simpleValue".equals( type ) ) {
			String str=req.getParameter( "simpleValue"+i );
			String simpleValueType=req.getParameter( "simpleValueType"+i );
			SimpleValue sValue=new SimpleValue();
			sValue.setContent( str );
			sValue.setType( SimpleValueTypeTypeType.valueOf( simpleValueType ) );
			
			Value value=new Value();
			value.setSimpleValue( sValue );
			operand.setValue( value );
		}
		
		return operand;
	}
}
