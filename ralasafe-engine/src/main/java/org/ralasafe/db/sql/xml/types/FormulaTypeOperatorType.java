/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: FormulaTypeOperatorType.java,v 1.1 2010/07/09 08:17:54 back Exp $
 */

package org.ralasafe.db.sql.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class FormulaTypeOperatorType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:54 $
 */
public class FormulaTypeOperatorType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The + type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the + type
     */
    public static final FormulaTypeOperatorType VALUE_0 = new FormulaTypeOperatorType(VALUE_0_TYPE, "+");

    /**
     * The - type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the - type
     */
    public static final FormulaTypeOperatorType VALUE_1 = new FormulaTypeOperatorType(VALUE_1_TYPE, "-");

    /**
     * The * type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the * type
     */
    public static final FormulaTypeOperatorType VALUE_2 = new FormulaTypeOperatorType(VALUE_2_TYPE, "*");

    /**
     * The / type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the / type
     */
    public static final FormulaTypeOperatorType VALUE_3 = new FormulaTypeOperatorType(VALUE_3_TYPE, "/");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private final int type;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private FormulaTypeOperatorType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of FormulaTypeOperatorType
     * 
     * @return an Enumeration over all possible instances of
     * FormulaTypeOperatorType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this
     * FormulaTypeOperatorType
     * 
     * @return the type of this FormulaTypeOperatorType
     */
    public int getType(
    ) {
        return this.type;
    }

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init(
    ) {
        Hashtable members = new Hashtable();
        members.put("+", VALUE_0);
        members.put("-", VALUE_1);
        members.put("*", VALUE_2);
        members.put("/", VALUE_3);
        return members;
    }

    /**
     * Method readResolve. will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve(
    ) {
        return valueOf(this.stringValue);
    }

    /**
     * Method toString.Returns the String representation of this
     * FormulaTypeOperatorType
     * 
     * @return the String representation of this
     * FormulaTypeOperatorType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new FormulaTypeOperatorType based
     * on the given String value.
     * 
     * @param string
     * @return the FormulaTypeOperatorType value of parameter
     * 'string'
     */
    public static org.ralasafe.db.sql.xml.types.FormulaTypeOperatorType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid FormulaTypeOperatorType";
            throw new IllegalArgumentException(err);
        }
        return (FormulaTypeOperatorType) obj;
    }

}
