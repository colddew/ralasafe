/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: FromType.java,v 1.1 2010/07/09 08:17:06 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class FromType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:06 $
 */
public class FromType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableList.
     */
    private java.util.Vector _tableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FromType() {
        super();
        this._tableList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vTable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTable(
            final org.ralasafe.db.sql.xml.Table vTable)
    throws java.lang.IndexOutOfBoundsException {
        this._tableList.addElement(vTable);
    }

    /**
     * 
     * 
     * @param index
     * @param vTable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTable(
            final int index,
            final org.ralasafe.db.sql.xml.Table vTable)
    throws java.lang.IndexOutOfBoundsException {
        this._tableList.add(index, vTable);
    }

    /**
     * Method enumerateTable.
     * 
     * @return an Enumeration over all org.ralasafe.db.sql.xml.Table
     * elements
     */
    public java.util.Enumeration enumerateTable(
    ) {
        return this._tableList.elements();
    }

    /**
     * Method getTable.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.Table at the
     * given index
     */
    public org.ralasafe.db.sql.xml.Table getTable(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._tableList.size()) {
            throw new IndexOutOfBoundsException("getTable: Index value '" + index + "' not in range [0.." + (this._tableList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.Table) _tableList.get(index);
    }

    /**
     * Method getTable.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.Table[] getTable(
    ) {
        org.ralasafe.db.sql.xml.Table[] array = new org.ralasafe.db.sql.xml.Table[0];
        return (org.ralasafe.db.sql.xml.Table[]) this._tableList.toArray(array);
    }

    /**
     * Method getTableCount.
     * 
     * @return the size of this collection
     */
    public int getTableCount(
    ) {
        return this._tableList.size();
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
    public void removeAllTable(
    ) {
        this._tableList.clear();
    }

    /**
     * Method removeTable.
     * 
     * @param vTable
     * @return true if the object was removed from the collection.
     */
    public boolean removeTable(
            final org.ralasafe.db.sql.xml.Table vTable) {
        boolean removed = _tableList.remove(vTable);
        return removed;
    }

    /**
     * Method removeTableAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.Table removeTableAt(
            final int index) {
        java.lang.Object obj = this._tableList.remove(index);
        return (org.ralasafe.db.sql.xml.Table) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vTable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTable(
            final int index,
            final org.ralasafe.db.sql.xml.Table vTable)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._tableList.size()) {
            throw new IndexOutOfBoundsException("setTable: Index value '" + index + "' not in range [0.." + (this._tableList.size() - 1) + "]");
        }
        
        this._tableList.set(index, vTable);
    }

    /**
     * 
     * 
     * @param vTableArray
     */
    public void setTable(
            final org.ralasafe.db.sql.xml.Table[] vTableArray) {
        //-- copy array
        _tableList.clear();
        
        for (int i = 0; i < vTableArray.length; i++) {
                this._tableList.add(vTableArray[i]);
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
     * @return the unmarshaled org.ralasafe.db.sql.xml.FromType
     */
    public static org.ralasafe.db.sql.xml.FromType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.FromType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.FromType.class, reader);
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
