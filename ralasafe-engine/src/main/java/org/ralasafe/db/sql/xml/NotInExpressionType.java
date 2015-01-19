/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: NotInExpressionType.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class NotInExpressionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class NotInExpressionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _left.
     */
    private org.ralasafe.db.sql.xml.Left _left;

    /**
     * Field _right.
     */
    private org.ralasafe.db.sql.xml.Right _right;


      //----------------/
     //- Constructors -/
    //----------------/

    public NotInExpressionType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'left'.
     * 
     * @return the value of field 'Left'.
     */
    public org.ralasafe.db.sql.xml.Left getLeft(
    ) {
        return this._left;
    }

    /**
     * Returns the value of field 'right'.
     * 
     * @return the value of field 'Right'.
     */
    public org.ralasafe.db.sql.xml.Right getRight(
    ) {
        return this._right;
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
     * Sets the value of field 'left'.
     * 
     * @param left the value of field 'left'.
     */
    public void setLeft(
            final org.ralasafe.db.sql.xml.Left left) {
        this._left = left;
    }

    /**
     * Sets the value of field 'right'.
     * 
     * @param right the value of field 'right'.
     */
    public void setRight(
            final org.ralasafe.db.sql.xml.Right right) {
        this._right = right;
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
     * org.ralasafe.db.sql.xml.NotInExpressionType
     */
    public static org.ralasafe.db.sql.xml.NotInExpressionType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.NotInExpressionType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.NotInExpressionType.class, reader);
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
