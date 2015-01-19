/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: SimpleOperatorType.java,v 1.1 2010/07/09 08:17:54 back Exp $
 */

package org.ralasafe.db.sql.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class SimpleOperatorType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:54 $
 */
public class SimpleOperatorType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The = type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the = type
     */
    public static final SimpleOperatorType VALUE_0 = new SimpleOperatorType(VALUE_0_TYPE, "=");

    /**
     * The != type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the != type
     */
    public static final SimpleOperatorType VALUE_1 = new SimpleOperatorType(VALUE_1_TYPE, "!=");

    /**
     * The <= type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the <= type
     */
    public static final SimpleOperatorType VALUE_2 = new SimpleOperatorType(VALUE_2_TYPE, "<=");

    /**
     * The < type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the < type
     */
    public static final SimpleOperatorType VALUE_3 = new SimpleOperatorType(VALUE_3_TYPE, "<");

    /**
     * The >= type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the >= type
     */
    public static final SimpleOperatorType VALUE_4 = new SimpleOperatorType(VALUE_4_TYPE, ">=");

    /**
     * The > type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the > type
     */
    public static final SimpleOperatorType VALUE_5 = new SimpleOperatorType(VALUE_5_TYPE, ">");

    /**
     * The LIKE type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the LIKE type
     */
    public static final SimpleOperatorType VALUE_6 = new SimpleOperatorType(VALUE_6_TYPE, "LIKE");

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

    private SimpleOperatorType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of SimpleOperatorType
     * 
     * @return an Enumeration over all possible instances of
     * SimpleOperatorType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this SimpleOperatorType
     * 
     * @return the type of this SimpleOperatorType
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
        members.put("=", VALUE_0);
        members.put("!=", VALUE_1);
        members.put("<=", VALUE_2);
        members.put("<", VALUE_3);
        members.put(">=", VALUE_4);
        members.put(">", VALUE_5);
        members.put("LIKE", VALUE_6);
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
     * SimpleOperatorType
     * 
     * @return the String representation of this SimpleOperatorType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new SimpleOperatorType based on the
     * given String value.
     * 
     * @param string
     * @return the SimpleOperatorType value of parameter 'string'
     */
    public static org.ralasafe.db.sql.xml.types.SimpleOperatorType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid SimpleOperatorType";
            throw new IllegalArgumentException(err);
        }
        return (SimpleOperatorType) obj;
    }

}
