/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: UserCategories.java,v 1.1 2010/07/09 08:17:07 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class UserCategories.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:07 $
 */
public class UserCategories implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userCategoryList.
     */
    private java.util.Vector _userCategoryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UserCategories() {
        super();
        this._userCategoryList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUserCategory
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUserCategory(
            final org.ralasafe.db.sql.xml.UserCategory vUserCategory)
    throws java.lang.IndexOutOfBoundsException {
        this._userCategoryList.addElement(vUserCategory);
    }

    /**
     * 
     * 
     * @param index
     * @param vUserCategory
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUserCategory(
            final int index,
            final org.ralasafe.db.sql.xml.UserCategory vUserCategory)
    throws java.lang.IndexOutOfBoundsException {
        this._userCategoryList.add(index, vUserCategory);
    }

    /**
     * Method enumerateUserCategory.
     * 
     * @return an Enumeration over all
     * org.ralasafe.db.sql.xml.UserCategory elements
     */
    public java.util.Enumeration enumerateUserCategory(
    ) {
        return this._userCategoryList.elements();
    }

    /**
     * Method getUserCategory.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.UserCategory at
     * the given index
     */
    public org.ralasafe.db.sql.xml.UserCategory getUserCategory(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._userCategoryList.size()) {
            throw new IndexOutOfBoundsException("getUserCategory: Index value '" + index + "' not in range [0.." + (this._userCategoryList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.UserCategory) _userCategoryList.get(index);
    }

    /**
     * Method getUserCategory.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.UserCategory[] getUserCategory(
    ) {
        org.ralasafe.db.sql.xml.UserCategory[] array = new org.ralasafe.db.sql.xml.UserCategory[0];
        return (org.ralasafe.db.sql.xml.UserCategory[]) this._userCategoryList.toArray(array);
    }

    /**
     * Method getUserCategoryCount.
     * 
     * @return the size of this collection
     */
    public int getUserCategoryCount(
    ) {
        return this._userCategoryList.size();
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
    public void removeAllUserCategory(
    ) {
        this._userCategoryList.clear();
    }

    /**
     * Method removeUserCategory.
     * 
     * @param vUserCategory
     * @return true if the object was removed from the collection.
     */
    public boolean removeUserCategory(
            final org.ralasafe.db.sql.xml.UserCategory vUserCategory) {
        boolean removed = _userCategoryList.remove(vUserCategory);
        return removed;
    }

    /**
     * Method removeUserCategoryAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.UserCategory removeUserCategoryAt(
            final int index) {
        java.lang.Object obj = this._userCategoryList.remove(index);
        return (org.ralasafe.db.sql.xml.UserCategory) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vUserCategory
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setUserCategory(
            final int index,
            final org.ralasafe.db.sql.xml.UserCategory vUserCategory)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._userCategoryList.size()) {
            throw new IndexOutOfBoundsException("setUserCategory: Index value '" + index + "' not in range [0.." + (this._userCategoryList.size() - 1) + "]");
        }
        
        this._userCategoryList.set(index, vUserCategory);
    }

    /**
     * 
     * 
     * @param vUserCategoryArray
     */
    public void setUserCategory(
            final org.ralasafe.db.sql.xml.UserCategory[] vUserCategoryArray) {
        //-- copy array
        _userCategoryList.clear();
        
        for (int i = 0; i < vUserCategoryArray.length; i++) {
                this._userCategoryList.add(vUserCategoryArray[i]);
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
     * @return the unmarshaled org.ralasafe.db.sql.xml.UserCategories
     */
    public static org.ralasafe.db.sql.xml.UserCategories unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.UserCategories) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.UserCategories.class, reader);
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
