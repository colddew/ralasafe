/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: ColumnType.java,v 1.2 2010/07/29 02:41:59 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * 
 * @version $Revision: 1.2 $ $Date: 2010/07/29 02:41:59 $
 */
public class ColumnType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 
     */
    private java.lang.String _name;

    /**
     * Field _tableAlias.
     */
    private java.lang.String _tableAlias;

    /**
     * sum, max, min, avg
     *  
     */
    private java.lang.String _function;

    /**
     * desc/asc
     *  
     */
    private org.ralasafe.db.sql.xml.types.ColumnTypeOrderType _order = org.ralasafe.db.sql.xml.types.ColumnTypeOrderType.valueOf("ASC");

    /**
     * Field _sqlType.
     */
    private java.lang.String _sqlType;

    /**
     * Field _javaType.
     */
    private java.lang.String _javaType;

    /**
     * 
     *  
     */
    private java.lang.String _property;

    /**
     * Field _readOnly.
     */
    private boolean _readOnly = false;

    /**
     * keeps track of state for field: _readOnly
     */
    private boolean _has_readOnly;


      //----------------/
     //- Constructors -/
    //----------------/

    public ColumnType() {
        super();
        setOrder(org.ralasafe.db.sql.xml.types.ColumnTypeOrderType.valueOf("ASC"));
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteReadOnly(
    ) {
        this._has_readOnly= false;
    }

    /**
     * Returns the value of field 'function'. The field 'function'
     * has the following description: 
     * avg
     *  
     * 
     * @return the value of field 'Function'.
     */
    public java.lang.String getFunction(
    ) {
        return this._function;
    }

    /**
     * Returns the value of field 'javaType'.
     * 
     * @return the value of field 'JavaType'.
     */
    public java.lang.String getJavaType(
    ) {
        return this._javaType;
    }

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description:
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'order'. The field 'order' has
     * the following description:
     *  
     * 
     * @return the value of field 'Order'.
     */
    public org.ralasafe.db.sql.xml.types.ColumnTypeOrderType getOrder(
    ) {
        return this._order;
    }

    /**
     * Returns the value of field 'property'. The field 'property'
     * has the following description: 
     *  
     * 
     * @return the value of field 'Property'.
     */
    public java.lang.String getProperty(
    ) {
        return this._property;
    }

    /**
     * Returns the value of field 'readOnly'.
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean getReadOnly(
    ) {
        return this._readOnly;
    }

    /**
     * Returns the value of field 'sqlType'.
     * 
     * @return the value of field 'SqlType'.
     */
    public java.lang.String getSqlType(
    ) {
        return this._sqlType;
    }

    /**
     * Returns the value of field 'tableAlias'.
     * 
     * @return the value of field 'TableAlias'.
     */
    public java.lang.String getTableAlias(
    ) {
        return this._tableAlias;
    }

    /**
     * Method hasReadOnly.
     * 
     * @return true if at least one ReadOnly has been added
     */
    public boolean hasReadOnly(
    ) {
        return this._has_readOnly;
    }

    /**
     * Returns the value of field 'readOnly'.
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean isReadOnly(
    ) {
        return this._readOnly;
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
     * Sets the value of field 'function'. The field 'function' has
     * the following description: 
     *  
     * 
     * @param function the value of field 'function'.
     */
    public void setFunction(
            final java.lang.String function) {
        this._function = function;
    }

    /**
     * Sets the value of field 'javaType'.
     * 
     * @param javaType the value of field 'javaType'.
     */
    public void setJavaType(
            final java.lang.String javaType) {
        this._javaType = javaType;
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: 
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'order'. The field 'order' has the
     * following description:
     * 
     * @param order the value of field 'order'.
     */
    public void setOrder(
            final org.ralasafe.db.sql.xml.types.ColumnTypeOrderType order) {
        this._order = order;
    }

    /**
     * Sets the value of field 'property'. The field 'property' has
     * the following description: 
     * 
     * @param property the value of field 'property'.
     */
    public void setProperty(
            final java.lang.String property) {
        this._property = property;
    }

    /**
     * Sets the value of field 'readOnly'.
     * 
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(
            final boolean readOnly) {
        this._readOnly = readOnly;
        this._has_readOnly = true;
    }

    /**
     * Sets the value of field 'sqlType'.
     * 
     * @param sqlType the value of field 'sqlType'.
     */
    public void setSqlType(
            final java.lang.String sqlType) {
        this._sqlType = sqlType;
    }

    /**
     * Sets the value of field 'tableAlias'.
     * 
     * @param tableAlias the value of field 'tableAlias'.
     */
    public void setTableAlias(
            final java.lang.String tableAlias) {
        this._tableAlias = tableAlias;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.ColumnType
     */
    public static org.ralasafe.db.sql.xml.ColumnType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.ColumnType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.ColumnType.class, reader);
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
