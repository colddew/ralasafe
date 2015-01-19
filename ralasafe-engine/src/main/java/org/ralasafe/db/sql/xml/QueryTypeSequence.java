/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: QueryTypeSequence.java,v 1.1 2010/07/09 08:17:08 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class QueryTypeSequence.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:08 $
 */
public class QueryTypeSequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _select.
     */
    private org.ralasafe.db.sql.xml.Select _select;

    /**
     * Field _from.
     */
    private org.ralasafe.db.sql.xml.From _from;

    /**
     * Field _where.
     */
    private org.ralasafe.db.sql.xml.Where _where;

    /**
     * Field _groupBy.
     */
    private org.ralasafe.db.sql.xml.GroupBy _groupBy;

    /**
     * Field _orderBy.
     */
    private org.ralasafe.db.sql.xml.OrderBy _orderBy;


      //----------------/
     //- Constructors -/
    //----------------/

    public QueryTypeSequence() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'from'.
     * 
     * @return the value of field 'From'.
     */
    public org.ralasafe.db.sql.xml.From getFrom(
    ) {
        return this._from;
    }

    /**
     * Returns the value of field 'groupBy'.
     * 
     * @return the value of field 'GroupBy'.
     */
    public org.ralasafe.db.sql.xml.GroupBy getGroupBy(
    ) {
        return this._groupBy;
    }

    /**
     * Returns the value of field 'orderBy'.
     * 
     * @return the value of field 'OrderBy'.
     */
    public org.ralasafe.db.sql.xml.OrderBy getOrderBy(
    ) {
        return this._orderBy;
    }

    /**
     * Returns the value of field 'select'.
     * 
     * @return the value of field 'Select'.
     */
    public org.ralasafe.db.sql.xml.Select getSelect(
    ) {
        return this._select;
    }

    /**
     * Returns the value of field 'where'.
     * 
     * @return the value of field 'Where'.
     */
    public org.ralasafe.db.sql.xml.Where getWhere(
    ) {
        return this._where;
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
     * Sets the value of field 'from'.
     * 
     * @param from the value of field 'from'.
     */
    public void setFrom(
            final org.ralasafe.db.sql.xml.From from) {
        this._from = from;
    }

    /**
     * Sets the value of field 'groupBy'.
     * 
     * @param groupBy the value of field 'groupBy'.
     */
    public void setGroupBy(
            final org.ralasafe.db.sql.xml.GroupBy groupBy) {
        this._groupBy = groupBy;
    }

    /**
     * Sets the value of field 'orderBy'.
     * 
     * @param orderBy the value of field 'orderBy'.
     */
    public void setOrderBy(
            final org.ralasafe.db.sql.xml.OrderBy orderBy) {
        this._orderBy = orderBy;
    }

    /**
     * Sets the value of field 'select'.
     * 
     * @param select the value of field 'select'.
     */
    public void setSelect(
            final org.ralasafe.db.sql.xml.Select select) {
        this._select = select;
    }

    /**
     * Sets the value of field 'where'.
     * 
     * @param where the value of field 'where'.
     */
    public void setWhere(
            final org.ralasafe.db.sql.xml.Where where) {
        this._where = where;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.QueryTypeSequence
     */
    public static org.ralasafe.db.sql.xml.QueryTypeSequence unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.QueryTypeSequence) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.QueryTypeSequence.class, reader);
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
