/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: BinaryExpressionType.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BinaryExpressionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class BinaryExpressionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _operand1.
     */
    private org.ralasafe.db.sql.xml.Operand1 _operand1;

    /**
     * Field _operator.
     */
    private org.ralasafe.db.sql.xml.Operator _operator;

    /**
     * Field _operand2.
     */
    private org.ralasafe.db.sql.xml.Operand2 _operand2;


      //----------------/
     //- Constructors -/
    //----------------/

    public BinaryExpressionType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'operand1'.
     * 
     * @return the value of field 'Operand1'.
     */
    public org.ralasafe.db.sql.xml.Operand1 getOperand1(
    ) {
        return this._operand1;
    }

    /**
     * Returns the value of field 'operand2'.
     * 
     * @return the value of field 'Operand2'.
     */
    public org.ralasafe.db.sql.xml.Operand2 getOperand2(
    ) {
        return this._operand2;
    }

    /**
     * Returns the value of field 'operator'.
     * 
     * @return the value of field 'Operator'.
     */
    public org.ralasafe.db.sql.xml.Operator getOperator(
    ) {
        return this._operator;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'operand1'.
     * 
     * @param operand1 the value of field 'operand1'.
     */
    public void setOperand1(
            final org.ralasafe.db.sql.xml.Operand1 operand1) {
        this._operand1 = operand1;
    }

    /**
     * Sets the value of field 'operand2'.
     * 
     * @param operand2 the value of field 'operand2'.
     */
    public void setOperand2(
            final org.ralasafe.db.sql.xml.Operand2 operand2) {
        this._operand2 = operand2;
    }

    /**
     * Sets the value of field 'operator'.
     * 
     * @param operator the value of field 'operator'.
     */
    public void setOperator(
            final org.ralasafe.db.sql.xml.Operator operator) {
        this._operator = operator;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.ralasafe.db.sql.xml.BinaryExpressionType
     */
    public static org.ralasafe.db.sql.xml.BinaryExpressionType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.BinaryExpressionType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.BinaryExpressionType.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
