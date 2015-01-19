/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: SimpleValueTypeTypeType.java,v 1.1 2010/07/09 08:17:54 back Exp $
 */

package org.ralasafe.db.sql.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class SimpleValueTypeTypeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:54 $
 */
public class SimpleValueTypeTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The string type
     */
    public static final int STRING_TYPE = 0;

    /**
     * The instance of the string type
     */
    public static final SimpleValueTypeTypeType STRING = new SimpleValueTypeTypeType(STRING_TYPE, "string");

    /**
     * The integer type
     */
    public static final int INTEGER_TYPE = 1;

    /**
     * The instance of the integer type
     */
    public static final SimpleValueTypeTypeType INTEGER = new SimpleValueTypeTypeType(INTEGER_TYPE, "integer");

    /**
     * The float type
     */
    public static final int FLOAT_TYPE = 2;

    /**
     * The instance of the float type
     */
    public static final SimpleValueTypeTypeType FLOAT = new SimpleValueTypeTypeType(FLOAT_TYPE, "float");

    /**
     * The boolean type
     */
    public static final int BOOLEAN_TYPE = 3;

    /**
     * The instance of the boolean type
     */
    public static final SimpleValueTypeTypeType BOOLEAN = new SimpleValueTypeTypeType(BOOLEAN_TYPE, "boolean");

    /**
     * The datetime type
     */
    public static final int DATETIME_TYPE = 4;

    /**
     * The instance of the datetime type
     */
    public static final SimpleValueTypeTypeType DATETIME = new SimpleValueTypeTypeType(DATETIME_TYPE, "datetime");

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

    private SimpleValueTypeTypeType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of SimpleValueTypeTypeType
     * 
     * @return an Enumeration over all possible instances of
     * SimpleValueTypeTypeType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this
     * SimpleValueTypeTypeType
     * 
     * @return the type of this SimpleValueTypeTypeType
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
        members.put("string", STRING);
        members.put("integer", INTEGER);
        members.put("float", FLOAT);
        members.put("boolean", BOOLEAN);
        members.put("datetime", DATETIME);
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
     * SimpleValueTypeTypeType
     * 
     * @return the String representation of this
     * SimpleValueTypeTypeType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new SimpleValueTypeTypeType based
     * on the given String value.
     * 
     * @param string
     * @return the SimpleValueTypeTypeType value of parameter
     * 'string'
     */
    public static org.ralasafe.db.sql.xml.types.SimpleValueTypeTypeType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid SimpleValueTypeTypeType";
            throw new IllegalArgumentException(err);
        }
        return (SimpleValueTypeTypeType) obj;
    }

}
