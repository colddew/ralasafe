/**
 * Copyright (c) 2004-2011 Wang Jinbao(Julian Wong), http://www.ralasafe.com
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id: ColumnTypeOrderType.java,v 1.1 2010/07/09 08:17:54 back Exp $
 */

package org.ralasafe.db.sql.xml.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class ColumnTypeOrderType.
 * 
 * @version $Revision: 1.1 $ $Date: 2010/07/09 08:17:54 $
 */
public class ColumnTypeOrderType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The DESC type
     */
    public static final int DESC_TYPE = 0;

    /**
     * The instance of the DESC type
     */
    public static final ColumnTypeOrderType DESC = new ColumnTypeOrderType(DESC_TYPE, "DESC");

    /**
     * The ASC type
     */
    public static final int ASC_TYPE = 1;

    /**
     * The instance of the ASC type
     */
    public static final ColumnTypeOrderType ASC = new ColumnTypeOrderType(ASC_TYPE, "ASC");

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

    private ColumnTypeOrderType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of ColumnTypeOrderType
     * 
     * @return an Enumeration over all possible instances of
     * ColumnTypeOrderType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this ColumnTypeOrderType
     * 
     * @return the type of this ColumnTypeOrderType
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
        members.put("DESC", DESC);
        members.put("ASC", ASC);
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
     * ColumnTypeOrderType
     * 
     * @return the String representation of this ColumnTypeOrderType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new ColumnTypeOrderType based on
     * the given String value.
     * 
     * @param string
     * @return the ColumnTypeOrderType value of parameter 'string'
     */
    public static org.ralasafe.db.sql.xml.types.ColumnTypeOrderType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid ColumnTypeOrderType";
            throw new IllegalArgumentException(err);
        }
        return (ColumnTypeOrderType) obj;
    }

}
