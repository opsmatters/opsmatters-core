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
 * Class that encapsulates the possible codes for file delimiters that can be stored in the configuration and maps them to names for display in a GUI combo box.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class FileDelimiterType extends BasicType
{
    /**
     * The comma delimiter.
     */
    public static final String COMMA = "comma";

    /**
     * The pipe delimiter.
     */
    public static final String PIPE = "pipe";

    /**
     * The space delimiter.
     */
    public static final String SPACE = "space";

    /**
     * The tab delimiter.
     */
    public static final String TAB = "tab";

    /**
     * The array of names for this type.
     */
    public static final String [] NAMES = 
    {   
        "Comma",
        "Pipe",
        "Space",
        "Tab"
    };

    /**
     * The array of codes for this type.
     */
    public static final String [] CODES = 
    {   
        COMMA,
        PIPE,
        SPACE,
        TAB
    };

    /**
     * The array of delimiters characters.
     */
    public static final String [] DELIMITERS = 
    {
        ",",
        "|",
        " ",
        "	"
    };

    /**
     * Empty constructor.
     */
    public FileDelimiterType()
    {
        this("");
    }

    /**
     * Constructor that takes a string.
     */
    public FileDelimiterType(String s)
    {
        super(s);
        setDisplayCode();
    }

    /**
     * Copy constructor.
     */
    public FileDelimiterType(FileDelimiterType t)
    {
        super(t);
    }

    /**
     * Returns the array of codes for this type.
     */
    public String[] getCodes()
    {
        return CODES;
    }

    /**
     * Returns the array of names for this type.
     */
    public String[] getNames()
    {
        return NAMES;
    }

    /**
     * Returns the positional index of the code for this type within the array of types.
     */
    static public int getIndex(FileDelimiterType type)
    {
        return getIndex(type.getCode());
    }

    /**
     * Returns the name of this code within the array of codes for this type.
     */
    static public String getName(String code)
    {
        String ret = "";
        for(int i = 0; i < CODES.length; i++)
        {
            if(code.equals(CODES[i]))
            {
                ret = NAMES[i];
                break;
            }
        }
        return ret;
    }

    /**
     * Returns the code of this name within the array of names for this type.
     */
    static public String getCode(Object o)
    {
        String ret = "";
        if(o != null)
        {
            String name = o.toString();
            for(int i = 0; i < NAMES.length; i++)
            {
                if(name.equals(NAMES[i]))
                {
                    ret = CODES[i];
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Returns the code of this name within the array of names for this type.
     */
    static public String getCodeIgnoreCase(Object o)
    {
        String ret = "";
        if(o != null)
        {
            String name = o.toString().toLowerCase();
            for(int i = 0; i < NAMES.length; i++)
            {
                if(name.equals(NAMES[i].toLowerCase()))
                {
                    ret = CODES[i];
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Returns the positional index of this code within the array of codes for this type.
     */
    static public int getIndex(String type)
    {
        int ret = 0;
        for(int i = 0; i < CODES.length; i++)
        {
            if(type.equals(CODES[i]))
            {
                ret = i;
                break;
            }
        }
        return ret;
    }

    /**
     * Given a delimiter name (eg. "Comma"), returns the actual delimiter (eg. ",").
     */
    public static String getDelimiterByName(String name)
    {
        String ret = null;
        for(int i = 0; i < NAMES.length && ret == null; i++)
        {
            if(NAMES[i].equals(name))
                ret = DELIMITERS[i];
        }
        return ret;
    }

    /**
     * Given a delimiter code (eg. "comma"), returns the actual delimiter (eg. ",").
     */
    public static String getDelimiterByCode(String code)
    {
        String ret = null;
        for(int i = 0; i < CODES.length && ret == null; i++)
        {
            if(CODES[i].equals(code))
                ret = DELIMITERS[i];
        }
        return ret;
    }

    /**
     * Given a delimiter (eg. ",", returns the delimiter name (eg. "comma").
     */
    public static String getDelimiterName(String delim)
    {
        String ret = null;
        for(int i = 0; i < DELIMITERS.length && ret == null; i++)
        {
            if(DELIMITERS[i].equals(delim))
                ret = NAMES[i];
        }
        return ret;
    }
}