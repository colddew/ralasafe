/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: UserCategoryType.java,v 1.2 2010/07/29 02:41:59 back Exp $
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
public class UserCategoryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _isRawScript.
     */
    private boolean _isRawScript;

    /**
     * keeps track of state for field: _isRawScript
     */
    private boolean _has_isRawScript;

    /**
     * Field _defineVariableList.
     */
    private java.util.Vector _defineVariableList;

    /**
     * Field _exprGroup.
     */
    private org.ralasafe.db.sql.xml.ExprGroup _exprGroup;

    /**
     * Field _rawScript.
     */
    private org.ralasafe.db.sql.xml.RawScript _rawScript;


      //----------------/
     //- Constructors -/
    //----------------/

    public UserCategoryType() {
        super();
        this._defineVariableList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDefineVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDefineVariable(
            final org.ralasafe.db.sql.xml.DefineVariable vDefineVariable)
    throws java.lang.IndexOutOfBoundsException {
        this._defineVariableList.addElement(vDefineVariable);
    }

    /**
     * 
     * 
     * @param index
     * @param vDefineVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDefineVariable(
            final int index,
            final org.ralasafe.db.sql.xml.DefineVariable vDefineVariable)
    throws java.lang.IndexOutOfBoundsException {
        this._defineVariableList.add(index, vDefineVariable);
    }

    /**
     */
    public void deleteIsRawScript(
    ) {
        this._has_isRawScript= false;
    }

    /**
     * Method enumerateDefineVariable.
     * 
     * @return an Enumeration over all
     * org.ralasafe.db.sql.xml.DefineVariable elements
     */
    public java.util.Enumeration enumerateDefineVariable(
    ) {
        return this._defineVariableList.elements();
    }

    /**
     * Method getDefineVariable.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.DefineVariable
     * at the given index
     */
    public org.ralasafe.db.sql.xml.DefineVariable getDefineVariable(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._defineVariableList.size()) {
            throw new IndexOutOfBoundsException("getDefineVariable: Index value '" + index + "' not in range [0.." + (this._defineVariableList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.DefineVariable) _defineVariableList.get(index);
    }

    /**
     * Method getDefineVariable.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.DefineVariable[] getDefineVariable(
    ) {
        org.ralasafe.db.sql.xml.DefineVariable[] array = new org.ralasafe.db.sql.xml.DefineVariable[0];
        return (org.ralasafe.db.sql.xml.DefineVariable[]) this._defineVariableList.toArray(array);
    }

    /**
     * Method getDefineVariableCount.
     * 
     * @return the size of this collection
     */
    public int getDefineVariableCount(
    ) {
        return this._defineVariableList.size();
    }

    /**
     * Returns the value of field 'exprGroup'.
     * 
     * @return the value of field 'ExprGroup'.
     */
    public org.ralasafe.db.sql.xml.ExprGroup getExprGroup(
    ) {
        return this._exprGroup;
    }

    /**
     * Returns the value of field 'isRawScript'.
     * 
     * @return the value of field 'IsRawScript'.
     */
    public boolean getIsRawScript(
    ) {
        return this._isRawScript;
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
     * Returns the value of field 'rawScript'.
     * 
     * @return the value of field 'RawScript'.
     */
    public org.ralasafe.db.sql.xml.RawScript getRawScript(
    ) {
        return this._rawScript;
    }

    /**
     * Method hasIsRawScript.
     * 
     * @return true if at least one IsRawScript has been added
     */
    public boolean hasIsRawScript(
    ) {
        return this._has_isRawScript;
    }

    /**
     * Returns the value of field 'isRawScript'.
     * 
     * @return the value of field 'IsRawScript'.
     */
    public boolean isIsRawScript(
    ) {
        return this._isRawScript;
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
    public void removeAllDefineVariable(
    ) {
        this._defineVariableList.clear();
    }

    /**
     * Method removeDefineVariable.
     * 
     * @param vDefineVariable
     * @return true if the object was removed from the collection.
     */
    public boolean removeDefineVariable(
            final org.ralasafe.db.sql.xml.DefineVariable vDefineVariable) {
        boolean removed = _defineVariableList.remove(vDefineVariable);
        return removed;
    }

    /**
     * Method removeDefineVariableAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.DefineVariable removeDefineVariableAt(
            final int index) {
        java.lang.Object obj = this._defineVariableList.remove(index);
        return (org.ralasafe.db.sql.xml.DefineVariable) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDefineVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDefineVariable(
            final int index,
            final org.ralasafe.db.sql.xml.DefineVariable vDefineVariable)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._defineVariableList.size()) {
            throw new IndexOutOfBoundsException("setDefineVariable: Index value '" + index + "' not in range [0.." + (this._defineVariableList.size() - 1) + "]");
        }
        
        this._defineVariableList.set(index, vDefineVariable);
    }

    /**
     * 
     * 
     * @param vDefineVariableArray
     */
    public void setDefineVariable(
            final org.ralasafe.db.sql.xml.DefineVariable[] vDefineVariableArray) {
        //-- copy array
        _defineVariableList.clear();
        
        for (int i = 0; i < vDefineVariableArray.length; i++) {
                this._defineVariableList.add(vDefineVariableArray[i]);
        }
    }

    /**
     * Sets the value of field 'exprGroup'.
     * 
     * @param exprGroup the value of field 'exprGroup'.
     */
    public void setExprGroup(
            final org.ralasafe.db.sql.xml.ExprGroup exprGroup) {
        this._exprGroup = exprGroup;
    }

    /**
     * Sets the value of field 'isRawScript'.
     * 
     * @param isRawScript the value of field 'isRawScript'.
     */
    public void setIsRawScript(
            final boolean isRawScript) {
        this._isRawScript = isRawScript;
        this._has_isRawScript = true;
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
     * Sets the value of field 'rawScript'.
     * 
     * @param rawScript the value of field 'rawScript'.
     */
    public void setRawScript(
            final org.ralasafe.db.sql.xml.RawScript rawScript) {
        this._rawScript = rawScript;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.ralasafe.db.sql.xml.UserCategoryType
     */
    public static org.ralasafe.db.sql.xml.UserCategoryType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.UserCategoryType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.UserCategoryType.class, reader);
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
