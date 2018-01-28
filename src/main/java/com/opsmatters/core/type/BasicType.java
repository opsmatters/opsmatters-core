/*
 * Copyright 2018 Gerald Curley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opsmatters.core.type;

/**
 * The base class for all classes that represent a type consisting of a set of codes with display names.
 * 
 * @author Gerald Curley (opsmatters)
 */
public abstract class BasicType
{
    private String code = "";
    protected boolean displayCode = true;
    private Object object;

    /**
     * Default constructor.
     */
    public BasicType()
    {
    }

    /**
     * Constructor that takes a string.
     * @param s The string value of the type
     */
    public BasicType(String s)
    {
        super();
        code = s;
    }

    /**
     * Copy constructor.
     * @param t The type to copy to this object
     */
    public BasicType(BasicType t)
    {
        super();
        code = t.getCode();
        displayCode = t.displayCode;
    }

    /**
     * Returns the code for this type.
     * @return The code for this type
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Sets the code for this type.
     * @param s The code for this type
     */
    public void setCode(String s)
    {
        code = new String(s);
    }

    /**
     * Sets this type to display the code rather than the name.
     */
    public void setDisplayCode()
    {
        displayCode = true;
    }

    /**
     * Sets this type to display the name rather than the code.
     */
    public void setDisplayName()
    {
        displayCode = false;
    }

    /**
     * Returns the code or name of this type (depending on the value of displayCode).
     */
    public String toString()
    {
        if(displayCode)
            return code;
        else
            return getName();
    }

    /**
     * Returns the length of the code.
     * @return The length of the code
     */
    public int length()
    {
        return code.length();
    }

    /**
     * Returns <CODE>true</CODE> if this type equals the given type.
     * @param t The type to compare
     * @return <CODE>true</CODE> if this type equals the given string
     */
    public boolean equals(BasicType t)
    {
        boolean ret = false;

        if(code != null && t != null)
        {
            ret = code.equals(t.getCode());
        }

        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if this type equals the given type (ignoring the case of the code).
     * @param t The type to compare
     * @return <CODE>true</CODE> if this type equals the given string
     */
    public boolean equalsIgnoreCase(BasicType t)
    {
        boolean ret = false;

        if(code != null && t != null)
        {
            ret = code.equalsIgnoreCase(t.getCode());
        }

        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if this type equals the given string.
     * @param s The string to compare
     * @return <CODE>true</CODE> if this type equals the given string
     */
    public boolean equals(String s)
    {
        boolean ret = false;

        if(code != null && s != null)
        {
            ret = code.equals(s);
        }

        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if this type equals the given string (ignoring the case of the code).
     * @param s The string to compare
     * @return <CODE>true</CODE> if this type equals the given string (ignoring the case of the code)
     */
    public boolean equalsIgnoreCase(String s)
    {
        boolean ret = false;

        if(code != null && s != null)
        {
            ret = code.equalsIgnoreCase(s);
        }

        return ret;
    }

    /**
     * Returns the array of codes for this type.
     * @return The array of codes for this type
     */
    public abstract String[] getCodes();

    /**
     * Returns the array of names for this type.
     * @return The array of names for this type
     */
    public abstract String[] getNames();

    /**
     * Returns the positional index of this code within the array of codes for this type.
     * @return The positional index of this code within the array of codes for this type
     */
    public int getIndex()
    {
        int ret = -1;
        String[] codes = getCodes();
        for(int i = 0; i < codes.length; i++)
        {
            if(code != null && code.equals(codes[i]))
            {
                ret = i;
                break;
            }
        }
        return ret;
    }

    /**
     * Returns the display name of this type.
     * @return The display name of this type
     */
    public String getName()
    {
        int idx = getIndex();
        if(idx != -1)
            return getNames()[getIndex()];
        return "";
    }

    /**
     * Sets the object for the row associated with this type.
     * @param o The object to associate with the row
     */
    public void setRowObject(Object o)
    {
        object = o;
    }

    /**
     * Returns the object for the row associated with this type.
     * @return The object for the row associated with this type
     */
    public Object getRowObject()
    {
        return object;
    }
}