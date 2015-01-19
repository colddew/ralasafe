/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: ExpressionGroupType.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ExpressionGroupType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class ExpressionGroupType implements java.io.Serializable {


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

    public ExpressionGroupType() {
        super();
        this._items = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vExpressionGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExpressionGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExpressionGroupTypeItem vExpressionGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.addElement(vExpressionGroupTypeItem);
    }

    /**
     * 
     * 
     * @param index
     * @param vExpressionGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExpressionGroupTypeItem(
            final int index,
            final org.ralasafe.db.sql.xml.ExpressionGroupTypeItem vExpressionGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        this._items.add(index, vExpressionGroupTypeItem);
    }

    /**
     * Method enumerateExpressionGroupTypeItem.
     * 
     * @return an Enumeration over all
     * org.ralasafe.db.sql.xml.ExpressionGroupTypeItem elements
     */
    public java.util.Enumeration enumerateExpressionGroupTypeItem(
    ) {
        return this._items.elements();
    }

    /**
     * Method getExpressionGroupTypeItem.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.ralasafe.db.sql.xml.ExpressionGroupTypeItem at the given inde
     */
    public org.ralasafe.db.sql.xml.ExpressionGroupTypeItem getExpressionGroupTypeItem(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("getExpressionGroupTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.ExpressionGroupTypeItem) _items.get(index);
    }

    /**
     * Method getExpressionGroupTypeItem.Returns the contents of
     * the collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[] getExpressionGroupTypeItem(
    ) {
        org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[] array = new org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[0];
        return (org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[]) this._items.toArray(array);
    }

    /**
     * Method getExpressionGroupTypeItemCount.
     * 
     * @return the size of this collection
     */
    public int getExpressionGroupTypeItemCount(
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
    public void removeAllExpressionGroupTypeItem(
    ) {
        this._items.clear();
    }

    /**
     * Method removeExpressionGroupTypeItem.
     * 
     * @param vExpressionGroupTypeItem
     * @return true if the object was removed from the collection.
     */
    public boolean removeExpressionGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExpressionGroupTypeItem vExpressionGroupTypeItem) {
        boolean removed = _items.remove(vExpressionGroupTypeItem);
        return removed;
    }

    /**
     * Method removeExpressionGroupTypeItemAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.ExpressionGroupTypeItem removeExpressionGroupTypeItemAt(
            final int index) {
        java.lang.Object obj = this._items.remove(index);
        return (org.ralasafe.db.sql.xml.ExpressionGroupTypeItem) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vExpressionGroupTypeItem
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setExpressionGroupTypeItem(
            final int index,
            final org.ralasafe.db.sql.xml.ExpressionGroupTypeItem vExpressionGroupTypeItem)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._items.size()) {
            throw new IndexOutOfBoundsException("setExpressionGroupTypeItem: Index value '" + index + "' not in range [0.." + (this._items.size() - 1) + "]");
        }
        
        this._items.set(index, vExpressionGroupTypeItem);
    }

    /**
     * 
     * 
     * @param vExpressionGroupTypeItemArray
     */
    public void setExpressionGroupTypeItem(
            final org.ralasafe.db.sql.xml.ExpressionGroupTypeItem[] vExpressionGroupTypeItemArray) {
        //-- copy array
        _items.clear();
        
        for (int i = 0; i < vExpressionGroupTypeItemArray.length; i++) {
                this._items.add(vExpressionGroupTypeItemArray[i]);
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
     * @return the unmarshaled
     * org.ralasafe.db.sql.xml.ExpressionGroupType
     */
    public static org.ralasafe.db.sql.xml.ExpressionGroupType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.ExpressionGroupType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.ExpressionGroupType.class, reader);
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
