/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: ExpressionGroupTypeItem.java,v 1.1 2010/07/09 08:17:06 back Exp $
 */

package org.ralasafe.db.sql.xml;

/**
 * Class ExpressionGroupTypeItem.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:06 $
 */
public class ExpressionGroupTypeItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _binaryExpression.
     */
    private org.ralasafe.db.sql.xml.BinaryExpression _binaryExpression;

    /**
     * Field _inExpression.
     */
    private org.ralasafe.db.sql.xml.InExpression _inExpression;

    /**
     * Field _notInExpression.
     */
    private org.ralasafe.db.sql.xml.NotInExpression _notInExpression;

    /**
     * Field _isNullExpression.
     */
    private org.ralasafe.db.sql.xml.IsNullExpression _isNullExpression;

    /**
     * Field _isNotNullExpression.
     */
    private org.ralasafe.db.sql.xml.IsNotNullExpression _isNotNullExpression;

    /**
     * Field _expressionGroup.
     */
    private org.ralasafe.db.sql.xml.ExpressionGroup _expressionGroup;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExpressionGroupTypeItem() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'binaryExpression'.
     * 
     * @return the value of field 'BinaryExpression'.
     */
    public org.ralasafe.db.sql.xml.BinaryExpression getBinaryExpression(
    ) {
        return this._binaryExpression;
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'expressionGroup'.
     * 
     * @return the value of field 'ExpressionGroup'.
     */
    public org.ralasafe.db.sql.xml.ExpressionGroup getExpressionGroup(
    ) {
        return this._expressionGroup;
    }

    /**
     * Returns the value of field 'inExpression'.
     * 
     * @return the value of field 'InExpression'.
     */
    public org.ralasafe.db.sql.xml.InExpression getInExpression(
    ) {
        return this._inExpression;
    }

    /**
     * Returns the value of field 'isNotNullExpression'.
     * 
     * @return the value of field 'IsNotNullExpression'.
     */
    public org.ralasafe.db.sql.xml.IsNotNullExpression getIsNotNullExpression(
    ) {
        return this._isNotNullExpression;
    }

    /**
     * Returns the value of field 'isNullExpression'.
     * 
     * @return the value of field 'IsNullExpression'.
     */
    public org.ralasafe.db.sql.xml.IsNullExpression getIsNullExpression(
    ) {
        return this._isNullExpression;
    }

    /**
     * Returns the value of field 'notInExpression'.
     * 
     * @return the value of field 'NotInExpression'.
     */
    public org.ralasafe.db.sql.xml.NotInExpression getNotInExpression(
    ) {
        return this._notInExpression;
    }

    /**
     * Sets the value of field 'binaryExpression'.
     * 
     * @param binaryExpression the value of field 'binaryExpression'
     */
    public void setBinaryExpression(
            final org.ralasafe.db.sql.xml.BinaryExpression binaryExpression) {
        this._binaryExpression = binaryExpression;
        this._choiceValue = binaryExpression;
    }

    /**
     * Sets the value of field 'expressionGroup'.
     * 
     * @param expressionGroup the value of field 'expressionGroup'.
     */
    public void setExpressionGroup(
            final org.ralasafe.db.sql.xml.ExpressionGroup expressionGroup) {
        this._expressionGroup = expressionGroup;
        this._choiceValue = expressionGroup;
    }

    /**
     * Sets the value of field 'inExpression'.
     * 
     * @param inExpression the value of field 'inExpression'.
     */
    public void setInExpression(
            final org.ralasafe.db.sql.xml.InExpression inExpression) {
        this._inExpression = inExpression;
        this._choiceValue = inExpression;
    }

    /**
     * Sets the value of field 'isNotNullExpression'.
     * 
     * @param isNotNullExpression the value of field
     * 'isNotNullExpression'.
     */
    public void setIsNotNullExpression(
            final org.ralasafe.db.sql.xml.IsNotNullExpression isNotNullExpression) {
        this._isNotNullExpression = isNotNullExpression;
        this._choiceValue = isNotNullExpression;
    }

    /**
     * Sets the value of field 'isNullExpression'.
     * 
     * @param isNullExpression the value of field 'isNullExpression'
     */
    public void setIsNullExpression(
            final org.ralasafe.db.sql.xml.IsNullExpression isNullExpression) {
        this._isNullExpression = isNullExpression;
        this._choiceValue = isNullExpression;
    }

    /**
     * Sets the value of field 'notInExpression'.
     * 
     * @param notInExpression the value of field 'notInExpression'.
     */
    public void setNotInExpression(
            final org.ralasafe.db.sql.xml.NotInExpression notInExpression) {
        this._notInExpression = notInExpression;
        this._choiceValue = notInExpression;
    }

}
