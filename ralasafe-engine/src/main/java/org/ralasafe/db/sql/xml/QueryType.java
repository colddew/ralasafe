/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: QueryType.java,v 1.2 2010/07/29 02:41:59 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class QueryType.
 * 
 * @version $Revision: 1.2 $ $Date: 2010/07/29 02:41:59 $
 */
public class QueryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    private java.lang.String _ds;

    /**
     * Field _isRawSQL.
     */
    private boolean _isRawSQL = false;

    /**
     * keeps track of state for field: _isRawSQL
     */
    private boolean _has_isRawSQL;

    /**
     * Field _isStoredProcedure.
     */
    private boolean _isStoredProcedure = false;

    /**
     * keeps track of state for field: _isStoredProcedure
     */
    private boolean _has_isStoredProcedure;

    /**
     * Field _type.
     */
    private org.ralasafe.db.sql.xml.types.QueryTypeTypeType _type = org.ralasafe.db.sql.xml.types.QueryTypeTypeType.valueOf("sql");

    /**
     * Field _queryTypeSequence.
     */
    private org.ralasafe.db.sql.xml.QueryTypeSequence _queryTypeSequence;

    /**
     * Field _rawSQL.
     */
    private org.ralasafe.db.sql.xml.RawSQL _rawSQL;

    /**
     * Field _storedProcedure.
     */
    private org.ralasafe.db.sql.xml.StoredProcedure _storedProcedure;


      //----------------/
     //- Constructors -/
    //----------------/

    public QueryType() {
        super();
        setType(org.ralasafe.db.sql.xml.types.QueryTypeTypeType.valueOf("sql"));
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteIsRawSQL(
    ) {
        this._has_isRawSQL= false;
    }

    /**
     */
    public void deleteIsStoredProcedure(
    ) {
        this._has_isStoredProcedure= false;
    }

    /**
     * Returns the value of field 'ds'. The field 'ds' has the
     * following description: 
     * @return the value of field 'Ds'.
     */
    public java.lang.String getDs(
    ) {
        return this._ds;
    }

    /**
     * Returns the value of field 'isRawSQL'.
     * 
     * @return the value of field 'IsRawSQL'.
     */
    public boolean getIsRawSQL(
    ) {
        return this._isRawSQL;
    }

    /**
     * Returns the value of field 'isStoredProcedure'.
     * 
     * @return the value of field 'IsStoredProcedure'.
     */
    public boolean getIsStoredProcedure(
    ) {
        return this._isStoredProcedure;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'queryTypeSequence'.
     * 
     * @return the value of field 'QueryTypeSequence'.
     */
    public org.ralasafe.db.sql.xml.QueryTypeSequence getQueryTypeSequence(
    ) {
        return this._queryTypeSequence;
    }

    /**
     * Returns the value of field 'rawSQL'.
     * 
     * @return the value of field 'RawSQL'.
     */
    public org.ralasafe.db.sql.xml.RawSQL getRawSQL(
    ) {
        return this._rawSQL;
    }

    /**
     * Returns the value of field 'storedProcedure'.
     * 
     * @return the value of field 'StoredProcedure'.
     */
    public org.ralasafe.db.sql.xml.StoredProcedure getStoredProcedure(
    ) {
        return this._storedProcedure;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public org.ralasafe.db.sql.xml.types.QueryTypeTypeType getType(
    ) {
        return this._type;
    }

    /**
     * Method hasIsRawSQL.
     * 
     * @return true if at least one IsRawSQL has been added
     */
    public boolean hasIsRawSQL(
    ) {
        return this._has_isRawSQL;
    }

    /**
     * Method hasIsStoredProcedure.
     * 
     * @return true if at least one IsStoredProcedure has been added
     */
    public boolean hasIsStoredProcedure(
    ) {
        return this._has_isStoredProcedure;
    }

    /**
     * Returns the value of field 'isRawSQL'.
     * 
     * @return the value of field 'IsRawSQL'.
     */
    public boolean isIsRawSQL(
    ) {
        return this._isRawSQL;
    }

    /**
     * Returns the value of field 'isStoredProcedure'.
     * 
     * @return the value of field 'IsStoredProcedure'.
     */
    public boolean isIsStoredProcedure(
    ) {
        return this._isStoredProcedure;
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
     * Sets the value of field 'ds'. The field 'ds' has the
     * following description: 
     * @param ds the value of field 'ds'.
     */
    public void setDs(
            final java.lang.String ds) {
        this._ds = ds;
    }

    /**
     * Sets the value of field 'isRawSQL'.
     * 
     * @param isRawSQL the value of field 'isRawSQL'.
     */
    public void setIsRawSQL(
            final boolean isRawSQL) {
        this._isRawSQL = isRawSQL;
        this._has_isRawSQL = true;
    }

    /**
     * Sets the value of field 'isStoredProcedure'.
     * 
     * @param isStoredProcedure the value of field
     * 'isStoredProcedure'.
     */
    public void setIsStoredProcedure(
            final boolean isStoredProcedure) {
        this._isStoredProcedure = isStoredProcedure;
        this._has_isStoredProcedure = true;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'queryTypeSequence'.
     * 
     * @param queryTypeSequence the value of field
     * 'queryTypeSequence'.
     */
    public void setQueryTypeSequence(
            final org.ralasafe.db.sql.xml.QueryTypeSequence queryTypeSequence) {
        this._queryTypeSequence = queryTypeSequence;
    }

    /**
     * Sets the value of field 'rawSQL'.
     * 
     * @param rawSQL the value of field 'rawSQL'.
     */
    public void setRawSQL(
            final org.ralasafe.db.sql.xml.RawSQL rawSQL) {
        this._rawSQL = rawSQL;
    }

    /**
     * Sets the value of field 'storedProcedure'.
     * 
     * @param storedProcedure the value of field 'storedProcedure'.
     */
    public void setStoredProcedure(
            final org.ralasafe.db.sql.xml.StoredProcedure storedProcedure) {
        this._storedProcedure = storedProcedure;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final org.ralasafe.db.sql.xml.types.QueryTypeTypeType type) {
        this._type = type;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.QueryType
     */
    public static org.ralasafe.db.sql.xml.QueryType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.QueryType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.QueryType.class, reader);
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
