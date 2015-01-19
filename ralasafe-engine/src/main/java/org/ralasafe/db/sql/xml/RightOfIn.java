/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: RightOfIn.java,v 1.1 2010/07/09 08:17:05 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class RightOfIn.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:05 $
 */
public class RightOfIn implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _valueList.
     */
    private java.util.Vector _valueList;

    /**
     * Field _query.
     */
    private org.ralasafe.db.sql.xml.Query _query;


      //----------------/
     //- Constructors -/
    //----------------/

    public RightOfIn() {
        super();
        this._valueList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValue(
            final org.ralasafe.db.sql.xml.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        this._valueList.addElement(vValue);
    }

    /**
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValue(
            final int index,
            final org.ralasafe.db.sql.xml.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        this._valueList.add(index, vValue);
    }

    /**
     * Method enumerateValue.
     * 
     * @return an Enumeration over all org.ralasafe.db.sql.xml.Value
     * elements
     */
    public java.util.Enumeration enumerateValue(
    ) {
        return this._valueList.elements();
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
     * Returns the value of field 'query'.
     * 
     * @return the value of field 'Query'.
     */
    public org.ralasafe.db.sql.xml.Query getQuery(
    ) {
        return this._query;
    }

    /**
     * Method getValue.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.Value at the
     * given index
     */
    public org.ralasafe.db.sql.xml.Value getValue(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("getValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.Value) _valueList.get(index);
    }

    /**
     * Method getValue.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.Value[] getValue(
    ) {
        org.ralasafe.db.sql.xml.Value[] array = new org.ralasafe.db.sql.xml.Value[0];
        return (org.ralasafe.db.sql.xml.Value[]) this._valueList.toArray(array);
    }

    /**
     * Method getValueCount.
     * 
     * @return the size of this collection
     */
    public int getValueCount(
    ) {
        return this._valueList.size();
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
    public void removeAllValue(
    ) {
        this._valueList.clear();
    }

    /**
     * Method removeValue.
     * 
     * @param vValue
     * @return true if the object was removed from the collection.
     */
    public boolean removeValue(
            final org.ralasafe.db.sql.xml.Value vValue) {
        boolean removed = _valueList.remove(vValue);
        return removed;
    }

    /**
     * Method removeValueAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.Value removeValueAt(
            final int index) {
        java.lang.Object obj = this._valueList.remove(index);
        return (org.ralasafe.db.sql.xml.Value) obj;
    }

    /**
     * Sets the value of field 'query'.
     * 
     * @param query the value of field 'query'.
     */
    public void setQuery(
            final org.ralasafe.db.sql.xml.Query query) {
        this._query = query;
        this._choiceValue = query;
    }

    /**
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setValue(
            final int index,
            final org.ralasafe.db.sql.xml.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("setValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        this._valueList.set(index, vValue);
    }

    /**
     * 
     * 
     * @param vValueArray
     */
    public void setValue(
            final org.ralasafe.db.sql.xml.Value[] vValueArray) {
        //-- copy array
        _valueList.clear();
        
        for (int i = 0; i < vValueArray.length; i++) {
                this._valueList.add(vValueArray[i]);
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
     * @return the unmarshaled org.ralasafe.db.sql.xml.RightOfIn
     */
    public static org.ralasafe.db.sql.xml.RightOfIn unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.RightOfIn) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.RightOfIn.class, reader);
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
