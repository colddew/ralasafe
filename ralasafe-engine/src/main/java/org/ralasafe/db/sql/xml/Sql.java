/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: Sql.java,v 1.1 2010/07/09 08:17:06 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Sql.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:06 $
 */
public class Sql implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _queryList.
     */
    private java.util.Vector _queryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() {
        super();
        this._queryList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQuery(
            final org.ralasafe.db.sql.xml.Query vQuery)
    throws java.lang.IndexOutOfBoundsException {
        this._queryList.addElement(vQuery);
    }

    /**
     * 
     * 
     * @param index
     * @param vQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQuery(
            final int index,
            final org.ralasafe.db.sql.xml.Query vQuery)
    throws java.lang.IndexOutOfBoundsException {
        this._queryList.add(index, vQuery);
    }

    /**
     * Method enumerateQuery.
     * 
     * @return an Enumeration over all org.ralasafe.db.sql.xml.Query
     * elements
     */
    public java.util.Enumeration enumerateQuery(
    ) {
        return this._queryList.elements();
    }

    /**
     * Method getQuery.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.Query at the
     * given index
     */
    public org.ralasafe.db.sql.xml.Query getQuery(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryList.size()) {
            throw new IndexOutOfBoundsException("getQuery: Index value '" + index + "' not in range [0.." + (this._queryList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.Query) _queryList.get(index);
    }

    /**
     * Method getQuery.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.Query[] getQuery(
    ) {
        org.ralasafe.db.sql.xml.Query[] array = new org.ralasafe.db.sql.xml.Query[0];
        return (org.ralasafe.db.sql.xml.Query[]) this._queryList.toArray(array);
    }

    /**
     * Method getQueryCount.
     * 
     * @return the size of this collection
     */
    public int getQueryCount(
    ) {
        return this._queryList.size();
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
     */
    public void removeAllQuery(
    ) {
        this._queryList.clear();
    }

    /**
     * Method removeQuery.
     * 
     * @param vQuery
     * @return true if the object was removed from the collection.
     */
    public boolean removeQuery(
            final org.ralasafe.db.sql.xml.Query vQuery) {
        boolean removed = _queryList.remove(vQuery);
        return removed;
    }

    /**
     * Method removeQueryAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.Query removeQueryAt(
            final int index) {
        java.lang.Object obj = this._queryList.remove(index);
        return (org.ralasafe.db.sql.xml.Query) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setQuery(
            final int index,
            final org.ralasafe.db.sql.xml.Query vQuery)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryList.size()) {
            throw new IndexOutOfBoundsException("setQuery: Index value '" + index + "' not in range [0.." + (this._queryList.size() - 1) + "]");
        }
        
        this._queryList.set(index, vQuery);
    }

    /**
     * 
     * 
     * @param vQueryArray
     */
    public void setQuery(
            final org.ralasafe.db.sql.xml.Query[] vQueryArray) {
        //-- copy array
        _queryList.clear();
        
        for (int i = 0; i < vQueryArray.length; i++) {
                this._queryList.add(vQueryArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.Sql
     */
    public static org.ralasafe.db.sql.xml.Sql unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.Sql) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.Sql.class, reader);
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
