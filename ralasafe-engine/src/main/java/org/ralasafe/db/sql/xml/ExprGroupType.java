/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: ExprGroupType.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ExprGroupType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class ExprGroupType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _linker.
     */
    private org.ralasafe.db.sql.xml.types.LinkerType _linker;

    /**
     * Field _items.
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExprGroupType() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vExprGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExprGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExprGroupTypeItem vExprGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vExprGroupTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vExprGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExprGroupTypeItem(
            final int index,
            final org.ralasafe.db.sql.xml.ExprGroupTypeItem vExprGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vExprGroupTypeItem);
    }

    /**
     * Method enumerateExprGroupTypeItem.
     * 
     * @return an Enumeration over all
     * org.ralasafe.db.sql.xml.ExprGroupTypeItem elements
     */
    public java.util.Enumeration enumerateExprGroupTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getExprGroupTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.ralasafe.db.sql.xml.ExprGroupTypeItem at the given index
     */
    public org.ralasafe.db.sql.xml.ExprGroupTypeItem getExprGroupTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getExprGroupTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.ExprGroupTypeItem) _items.get(index);
    }

    /**
     * Method getExprGroupTypeItem.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.ExprGroupTypeItem[] getExprGroupTypeItem(
    ) {
        org.ralasafe.db.sql.xml.ExprGroupTypeItem[] array = new org.ralasafe.db.sql.xml.ExprGroupTypeItem[0];
        return (org.ralasafe.db.sql.xml.ExprGroupTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getExprGroupTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getExprGroupTypeItemCount(
    ) {
        return this._items.size();
    }

    /**
     * Returns the value of field 'linker'.
     * 
     * @return the value of field 'Linker'.
     */
    public org.ralasafe.db.sql.xml.types.LinkerType getLinker(
    ) {
        return this._linker;
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
    public void removeAllExprGroupTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeExprGroupTypeItem.
     * 
     * @param vExprGroupTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeExprGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExprGroupTypeItem vExprGroupTypeItem) {
        boolean removed = _items.remove(vExprGroupTypeItem);
        return removed;
    }

    /**
     * Method removeExprGroupTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.ExprGroupTypeItem removeExprGroupTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (org.ralasafe.db.sql.xml.ExprGroupTypeItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vExprGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setExprGroupTypeItem(
            final int index,
            final org.ralasafe.db.sql.xml.ExprGroupTypeItem vExprGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setExprGroupTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vExprGroupTypeItem);
    }

    /**
     * 
     * 
     * @param vExprGroupTypeItemArray
     */
    public void setExprGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExprGroupTypeItem[] vExprGroupTypeItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vExprGroupTypeItemArray.length; i++) {
                this._items.add(vExprGroupTypeItemArray[i]);
        }
    }

    /**
     * Sets the value of field 'linker'.
     * 
     * @param linker the value of field 'linker'.
     */
    public void setLinker(
            final org.ralasafe.db.sql.xml.types.LinkerType linker) {
        this._linker = linker;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.ExprGroupType
     */
    public static org.ralasafe.db.sql.xml.ExprGroupType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.ExprGroupType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.ExprGroupType.class, reader);
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
