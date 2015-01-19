/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: FormulaType.java,v 1.1 2010/07/09 08:17:05 back Exp $
 */

package org.ralasafe.db.sql.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class FormulaType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:05 $
 */
public class FormulaType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _operator.
     */
    private org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType _operator;

    /**
     * Field _type.
     */
    private org.ralasafe.db.sql.xml.types.FormulaTypeTypeType _type;

    /**
     * Field _variableList.
     */
    private java.util.Vector _variableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FormulaType() {
        super();
        this._variableList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVariable(
            final org.ralasafe.db.sql.xml.Variable vVariable)
    throws java.lang.IndexOutOfBoundsException {
        this._variableList.addElement(vVariable);
    }

    /**
     * 
     * 
     * @param index
     * @param vVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addVariable(
            final int index,
            final org.ralasafe.db.sql.xml.Variable vVariable)
    throws java.lang.IndexOutOfBoundsException {
        this._variableList.add(index, vVariable);
    }

    /**
     * Method enumerateVariable.
     * 
     * @return an Enumeration over all org.ralasafe.db.sql.xml.Variable
     * elements
     */
    public java.util.Enumeration enumerateVariable(
    ) {
        return this._variableList.elements();
    }

    /**
     * Returns the value of field 'operator'.
     * 
     * @return the value of field 'Operator'.
     */
    public org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType getOperator(
    ) {
        return this._operator;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public org.ralasafe.db.sql.xml.types.FormulaTypeTypeType getType(
    ) {
        return this._type;
    }

    /**
     * Method getVariable.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.ralasafe.db.sql.xml.Variable at the
     * given index
     */
    public org.ralasafe.db.sql.xml.Variable getVariable(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._variableList.size()) {
            throw new IndexOutOfBoundsException("getVariable: Index value '" + index + "' not in range [0.." + (this._variableList.size() - 1) + "]");
        }
        
        return (org.ralasafe.db.sql.xml.Variable) _variableList.get(index);
    }

    /**
     * Method getVariable.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.ralasafe.db.sql.xml.Variable[] getVariable(
    ) {
        org.ralasafe.db.sql.xml.Variable[] array = new org.ralasafe.db.sql.xml.Variable[0];
        return (org.ralasafe.db.sql.xml.Variable[]) this._variableList.toArray(array);
    }

    /**
     * Method getVariableCount.
     * 
     * @return the size of this collection
     */
    public int getVariableCount(
    ) {
        return this._variableList.size();
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
    public void removeAllVariable(
    ) {
        this._variableList.clear();
    }

    /**
     * Method removeVariable.
     * 
     * @param vVariable
     * @return true if the object was removed from the collection.
     */
    public boolean removeVariable(
            final org.ralasafe.db.sql.xml.Variable vVariable) {
        boolean removed = _variableList.remove(vVariable);
        return removed;
    }

    /**
     * Method removeVariableAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.ralasafe.db.sql.xml.Variable removeVariableAt(
            final int index) {
        java.lang.Object obj = this._variableList.remove(index);
        return (org.ralasafe.db.sql.xml.Variable) obj;
    }

    /**
     * Sets the value of field 'operator'.
     * 
     * @param operator the value of field 'operator'.
     */
    public void setOperator(
            final org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType operator) {
        this._operator = operator;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final org.ralasafe.db.sql.xml.types.FormulaTypeTypeType type) {
        this._type = type;
    }

    /**
     * 
     * 
     * @param index
     * @param vVariable
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setVariable(
            final int index,
            final org.ralasafe.db.sql.xml.Variable vVariable)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._variableList.size()) {
            throw new IndexOutOfBoundsException("setVariable: Index value '" + index + "' not in range [0.." + (this._variableList.size() - 1) + "]");
        }
        
        this._variableList.set(index, vVariable);
    }

    /**
     * 
     * 
     * @param vVariableArray
     */
    public void setVariable(
            final org.ralasafe.db.sql.xml.Variable[] vVariableArray) {
        //-- copy array
        _variableList.clear();
        
        for (int i = 0; i < vVariableArray.length; i++) {
                this._variableList.add(vVariableArray[i]);
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
     * @return the unmarshaled org.ralasafe.db.sql.xml.FormulaType
     */
    public static org.ralasafe.db.sql.xml.FormulaType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.ralasafe.db.sql.xml.FormulaType) Unmarshaller.unmarshal(org.ralasafe.db.sql.xml.FormulaType.class, reader);
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
