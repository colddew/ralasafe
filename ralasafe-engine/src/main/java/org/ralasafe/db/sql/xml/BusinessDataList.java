/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: BusinessDataList.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BusinessDataList.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class BusinessDataList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _businessDataList.
     */
    private java.util.Vector _businessDataList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BusinessDataList() {
        super();
        this._businessDataList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vBusinessData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addBusinessData(
            final org.ralasafe.db.sql.xml.BusinessData vBusinessData)
    throws java.lang.IndexOutOfBoundsException {
        this._businessDataList.addElement(vBusinessData);
    }

    /**
     * 
     * 
     * @param index
     * @param vBusinessData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addBusinessData(
            final int index,
            final org.ralasafe.db.sql.xml.BusinessData vBusinessData)
    throws java.lang.IndexOutOfBoundsException {
        this._businessDataList.add(index, vBusinessData);
    }

    /**
     * Method enumerateBusinessData.
     * 
     * @return an Enumeration over all
     * org.ralasafe.db.sql.xml.BusinessData elements
     */
    public java.util.Enumeration enumerateBusinessData(
    ) {
        return this._businessDataList.elements();
    }

    /**
     * Method getBusinessData.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.BusinessData at
     * the given index
     */
    public org.ralasafe.db.sql.xml.BusinessData getBusinessData(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._businessDataList.size()) {
            throw new IndexOutOfBoundsException("getBusinessData: Index value '" + index + "' not in range [0.." + (this._businessDataList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.BusinessData) _businessDataList.get(index);
    }

    /**
     * Method getBusinessData.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.BusinessData[] getBusinessData(
    ) {
        org.ralasafe.db.sql.xml.BusinessData[] array = new org.ralasafe.db.sql.xml.BusinessData[0];
        return (org.ralasafe.db.sql.xml.BusinessData[]) this._businessDataList.toArray(array);
    }

    /**
     * Method getBusinessDataCount.
     * 
     * @return the size of this collection
     */
    public int getBusinessDataCount(
    ) {
        return this._businessDataList.size();
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
    public void removeAllBusinessData(
    ) {
        this._businessDataList.clear();
    }

    /**
     * Method removeBusinessData.
     * 
     * @param vBusinessData
     * @return true if the object was removed from the collection.
     */
    public boolean removeBusinessData(
            final org.ralasafe.db.sql.xml.BusinessData vBusinessData) {
        boolean removed = _businessDataList.remove(vBusinessData);
        return removed;
    }

    /**
     * Method removeBusinessDataAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.BusinessData removeBusinessDataAt(
            final int index) {
        java.lang.Object obj = this._businessDataList.remove(index);
        return (org.ralasafe.db.sql.xml.BusinessData) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vBusinessData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setBusinessData(
            final int index,
            final org.ralasafe.db.sql.xml.BusinessData vBusinessData)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._businessDataList.size()) {
            throw new IndexOutOfBoundsException("setBusinessData: Index value '" + index + "' not in range [0.." + (this._businessDataList.size() - 1) + "]");
        }
        
        this._businessDataList.set(index, vBusinessData);
    }

    /**
     * 
     * 
     * @param vBusinessDataArray
     */
    public void setBusinessData(
            final org.ralasafe.db.sql.xml.BusinessData[] vBusinessDataArray) {
        //-- copy array
        _businessDataList.clear();
        
        for (int i = 0; i < vBusinessDataArray.length; i++) {
                this._businessDataList.add(vBusinessDataArray[i]);
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
     * @return the unmarshaled org.ralasafe.db.sql.xml.BusinessDataList
     */
    public static org.ralasafe.db.sql.xml.BusinessDataList unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.BusinessDataList) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.BusinessDataList.class, reader);
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
