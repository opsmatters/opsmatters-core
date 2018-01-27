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

package com.opsmatters.core.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import java.util.logging.Logger;

/**
 * A set of utility methods to perform miscellaneous tasks related to strings.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class StringUtilities
{
    private static final Logger logger = Logger.getLogger(StringUtilities.class.getName());

    public static final String YEAR   = "y";
    public static final String MONTH  = "M";
    public static final String WEEK   = "w";
    public static final String DAY    = "d";
    public static final String HOUR   = "h";
    public static final String MINUTE = "m";
    public static final String SECOND = "s";

    /**
     * The default depth for stack traces.
     */
    public static final int DEFAULT_DEPTH    = 20;

    /**
     * Private constructor as this class shouldn't be instantiated.
     */
    private StringUtilities()
    {
    }    

    /**
     * Serializes the given exception as a stack trace string.
     */
    static public String serialize(Throwable ex)
    {
        return serialize(ex, DEFAULT_DEPTH, 0);
    }

    /**
     * Serializes the given exception as a stack trace string.
     */
    static public String serialize(Throwable ex, int depth)
    {
        return serialize(ex, depth, 0);
    }

    /**
     * Serializes the given exception as a stack trace string.
     */
    static private String serialize(Throwable ex, int depth, int level)
    {
        StringBuffer buff = new StringBuffer();
        String str = ex.toString();

        // Split the first line if it's too long
        int pos = str.indexOf(":");
        if(str.length() < 80 || pos == -1)
        {
            buff.append(str);
        }
        else
        {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos+2);
            if(str2.indexOf(str1) == -1)
            {
                buff.append(str1);
                buff.append(": \n\t");
            }
            buff.append(str2);
        }

        if(depth > 0)
        {
            StackTraceElement[] elements = ex.getStackTrace();
            for(int i = 0; i < elements.length; i++)
            {
                buff.append("\n\tat ");
                buff.append(elements[i]);
                if(i == (depth-1) && elements.length > depth)
                {
                    buff.append("\n\t... "+(elements.length-depth)+" more ...");
                    i = elements.length;
                }
            }
        }

        if(ex.getCause() != null && level < 3)
        {
            buff.append("\nCaused by: ");
            buff.append(serialize(ex.getCause(), depth, ++level));
        }

        return buff.toString();
    }

    /**
     * Returns <CODE>true</CODE> if the given string matches the given regular expression.
     * @param str the string against which the expression is to be matched
     * @param expr the regular expression to match with the input string
     * @return <CODE>true</CODE> if the given string matches the given regular expression
     */
    public static boolean getMatchResult(String str, String expr)
        throws Exception
    {
        Pattern pattern = Pattern.compile(expr, Pattern.DOTALL);
        return pattern.matcher(str).matches();
    }

    /**
     * Returns the given array as a string.
     */
    public static String serialize(Object[] objs)
    {
        StringBuffer buff = new StringBuffer();
        for(int i = 0; i < objs.length; i++)
        {
            if(objs[i] != null)
            {
                buff.append(objs[i].toString());
                if(i != objs.length-1)
                    buff.append(",");
            }
        }
        return buff.toString();
    }

    /**
     * Returns the given string after if it has been obfuscated.
     */
    public static String encode(String str)
    {
        String ret = str;

        try
        {
            // Obfuscate the string
            if(ret != null)
                ret = new String(Base64.encodeBase64(ret.getBytes()));
        }
        catch(NoClassDefFoundError e)
        {
            System.out.println("WARNING: unable to encode: "
                +e.getClass().getName()+": "+e.getMessage());
        }

        return ret;
    }

    /**
     * Returns the given byte array after if it has been obfuscated.
     */
    public static String encodeBytes(byte[] bytes)
    {
        String ret = null;

        try
        {
            // Obfuscate the string
            if(bytes != null)
                ret = new String(Base64.encodeBase64(bytes));
        }
        catch(NoClassDefFoundError e)
        {
            ret = new String(bytes);
            System.out.println("WARNING: unable to encode: "
                +e.getClass().getName()+": "+e.getMessage());
        }

        return ret;
    }

    /**
     * Returns the given string after if it has been de-obfuscated.
     */
    public static String decode(String str)
    {
        String ret = str;

        try
        {
            // De-obfuscate the string
            if(ret != null)
                ret = new String(Base64.decodeBase64(ret.getBytes()));
        }
        catch(NoClassDefFoundError e)
        {
            System.out.println("WARNING: unable to decode: "
                +e.getClass().getName()+": "+e.getMessage());
        }

        return ret;
    }

    /**
     * Returns the given byte array after if it has been de-obfuscated.
     */
    public static byte[] decodeBytes(String str)
    {
        byte[] ret = null;

        try
        {
            // De-obfuscate the string
            if(str != null)
                ret = Base64.decodeBase64(str.getBytes());
        }
        catch(NoClassDefFoundError e)
        {
            ret = str.getBytes();
            System.out.println("WARNING: unable to decode: "
                +e.getClass().getName()+": "+e.getMessage());
        }

        return ret;
    }

    /**
     * Returns the given string truncated at a word break before the given number of characters.
     */
    public static String truncate(String str, int count)
    {
        if(count < 0 || str.length() <= count)
            return str;

        int pos = count;
        for(int i = count; i >= 0 && !Character.isWhitespace(str.charAt(i)); i--, pos--);
        return str.substring(0, pos)+"...";
    }

    /**
     * Returns the number of occurences of the given character in the given string.
     */
    public static int getOccurenceCount(char c, String s) 
    {
        int ret = 0;

        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == c)
                ++ret;
        }

        return ret;
    }

    /**
     * Returns the number of occurrences of the substring in the given string.
     */
    public static int getOccurrenceCount(String expr, String str)
    {
        int ret = 0;
        Pattern p = Pattern.compile(expr);
        Matcher m = p.matcher(str);
        while(m.find()) 
            ++ret;
        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if the given string matches the given regular expression.
     * @param str the string against which the expression is to be matched
     * @param expr the regular expression to match with the input string
     * @param whole indicates that a whole word match is required
     * @return an object giving the results of the search (or null if no match found)
     */
    public static Matcher getWildcardMatcher(String str, String expr, boolean whole)
    {
        expr = expr.replaceAll("\\?",".?");
        expr = expr.replaceAll("\\*",".*?");
        if(whole)
            expr = "^"+expr+"$";
        Pattern pattern = Pattern.compile(expr/*, Pattern.DOTALL*/);
        return pattern.matcher(str);
    }

    /**
     * Returns <CODE>true</CODE> if the given string matches the given regular expression.
     * @param str the string against which the expression is to be matched
     * @param expr the regular expression to match with the input string
     * @return an object giving the results of the search (or null if no match found)
     */
    public static Matcher getWildcardMatcher(String str, String expr)
    {
        return getWildcardMatcher(str, expr, false);
    }

    /**
     * Returns <CODE>true</CODE> if the given string matches the given regular expression.
     * @param str the string against which the expression is to be matched
     * @param expr the regular expression to match with the input string
     * @param whole indicates that a whole word match is required
     * @return <CODE>true</CODE> if a match was found
     */
    public static boolean isWildcardMatch(String str, String expr, boolean whole)
    {
        return getWildcardMatcher(str, expr, whole).find();
    }

    /**
     * Returns <CODE>true</CODE> if the given string matches the given regular expression.
     * @param str the string against which the expression is to be matched
     * @param expr the regular expression to match with the input string
     * @return <CODE>true</CODE> if a match was found
     */
    public static boolean isWildcardMatch(String str, String expr)
    {
        return isWildcardMatch(str, expr, false);
    }

    /**
     * Checks that a string buffer ends up with a given string.
     * @param buffer the buffer to perform the check on
     * @param suffix the suffix
     * @return <code>true</code> if the character sequence represented by the
     * argument is a suffix of the character sequence represented by
     * the StringBuffer object; <code>false</code> otherwise. Note that the
     * result will be <code>true</code> if the argument is the empty string.
     */
    public static boolean endsWith(StringBuffer buffer, String suffix)
    {
        if (suffix.length() > buffer.length()) 
            return false;
        int endIndex = suffix.length() - 1;
        int bufferIndex = buffer.length() - 1;
        while (endIndex >= 0)
        {
            if (buffer.charAt(bufferIndex) != suffix.charAt(endIndex))
                return false;
            bufferIndex--;
            endIndex--;
        }
        return true;
    }

    /**
     * Converts the given string with CR and LF character correctly formatted.
     */
    public static String toReadableForm(String str)
    {
        String ret = str;
        if(str != null && str.length() > 0 
            && str.indexOf("\n") != -1 
            && str.indexOf("\r") == -1)
        {
            str.replaceAll("\n", "\r\n");
        }

        return ret;
    }

    /**
     * Normalises the given string, replacing certain special characters with their HTML escape sequences.
     * <P>
     * <UL>
     * <LI> <B><CODE>&lt;</CODE></B> becomes <B><CODE>&amp;lt;</CODE></B>
     * <LI> <B><CODE>&gt;</CODE></B> becomes <B><CODE>&amp;gt;</CODE></B>
     * <LI> <B><CODE>&amp;</CODE></B> becomes <B><CODE>&amp;amp;</CODE></B>
     * <LI> <B><CODE>&quot;</CODE></B> becomes <B><CODE>&amp;quot;</CODE></B>
     * <LI> <B><CODE>'</CODE></B> becomes <B><CODE>&amp;apos;</CODE></B>
     * </UL>
     */
    static public String normalise(String s) 
    {
        StringBuffer str = new StringBuffer();

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) 
        {
            char ch = s.charAt(i);
            switch (ch) 
            {
                case '<': 
                {
                    str.append("&lt;");
                    break;
                }
                case '>': 
                {
                    str.append("&gt;");
                    break;
                }
                case '&': 
                {
                    str.append("&amp;");
                    break;
                }
                case '"': 
                {
                    str.append("&quot;");
                    break;
                }
                case '\'': 
                {
                    str.append("&apos;");
                    break;
                }
                default: 
                {
                    str.append(ch);
                }
            }
        }
        return str.toString();    
    }

    /**
     * Eencode the special characters in the string to it's URL encoded representation.
     * @param str the string to encode
     * @return the encoded string
     */
    public static String urlEncode(String str)
    {
        String ret = str;

        try 
        {
            ret = URLEncoder.encode(str, "UTF-8");
        } 
        catch (UnsupportedEncodingException e) 
        {
            logger.severe("Failed to encode value: "+str);
        }

        return ret;
    }

    /**
     * Returns the given string with all spaces removed.
     */
    public static String stripSpaces(String s)
    {
        StringBuffer buff = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c != ' ')
                buff.append(c);
        }
        return buff.toString();
    }

    /**
     * Returns the given string with all non-ASCII removed.
     */
    public static String stripNonAsciiPrintable(String s)
    {
        StringBuffer buff = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            int c = (int)s.charAt(i);
            if(c >= 32 && c < 127)
                buff.append((char)c);
        }
        return buff.toString();
    }

    /**
     * Returns <CODE>true</CODE> if the the given string contains only numeric digits [0-9].
     */
    public static boolean isNumeric(String s)
    {
        boolean ret = true;
        for(int i = 0; i < s.length() && ret; i++)
        {
            char c = s.charAt(i);
            ret = (c >= '0' && c <= '9');
        }
        return ret;
    }

    /**
     * Remove any extraneous control characters from text fields.
     */
    public static String removeControlCharacters(String s, boolean removeCR)
    {
        String ret = s;
        if(ret != null)
        {
            ret = ret.replaceAll("_x000D_","");
            if(removeCR)
                ret = ret.replaceAll("\r","");
        }
        return ret;
    }

    /**
     * Remove any extraneous control characters from text fields.
     */
    public static String removeControlCharacters(String s)
    {
        return removeControlCharacters(s, false);
    }

    /**
     * Print the characters and control characters from the given string.
     */
    public static void printCharacters(String s)
    {
        if(s != null)
        {
            logger.info("string length="+s.length());
            for(int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                logger.info("char["+i+"]="+c+" ("+(int)c+")");
            }
        }
    }

    /**
     * Returns <CODE>true</CODE> if the given string has leading and trailing double quotes.
     */
    public static boolean hasDoubleQuotes(String s)
    {
        return s != null && s.startsWith("\"") && s.endsWith("\"") && s.length() > 2;
    }

    /**
     * Returns the given string with leading and trailing quotes removed.
     */
    public static String stripDoubleQuotes(String s)
    {
        String ret = s;
        if(hasDoubleQuotes(s))
            ret = s.substring(1, s.length()-1);
        return ret;
    }

    private static final String STRICT_EMAIL_REGEX = "[A-Z0-9a-z.'_%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,7}";
    private static final String LAX_EMAIL_REGEX = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";

    private static Pattern strictEmailPattern = Pattern.compile(STRICT_EMAIL_REGEX);
    private static Pattern laxEmailPattern = Pattern.compile(LAX_EMAIL_REGEX);

    /**
     * Returns <CODE>true</CODE> if the given string is a valid email address.
     */
    public static boolean isValidEmailAddress(String email, boolean strict)
    {
        Pattern p = strict ? strictEmailPattern : laxEmailPattern;
        return p.matcher(email).matches();
    }

    /**
     * Returns <CODE>true</CODE> if the given string is a valid email address.
     */
    public static boolean isValidEmailAddress(String email)
    {
        return isValidEmailAddress(email, true);
    }

    /**
     * Returns the given query with illegal characters on the end removed.
     */
    public static String removeIllegalQueryCharacters(String str)
    {
        return removeIllegalQueryCharacters(str, str.toLowerCase());
    }

    /**
     * Returns the given query with an semi-colons on the end removed.
     */
    public static String removeIllegalQueryCharacters(String str, String lower)
    {
        String ret = stripDoubleQuotes(str);

        if(lower.startsWith("select")
            || lower.startsWith("update")
            || lower.startsWith("insert")
            || lower.startsWith("delete")
            || lower.startsWith("create")
            || lower.startsWith("drop")
            || lower.startsWith("call"))
        {
            while(ret.endsWith(";") || ret.endsWith("/"))
                ret = ret.substring(0,ret.length()-1);
        }
        return ret;
    }

    /**
     * Strips class name prefixes from the start of the given string.
     */
    public static String stripClassNames(String str)
    {
        String ret = str;
        if(ret != null)
        {
            while(ret.startsWith("java.security.PrivilegedActionException:")
                || ret.startsWith("com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl:")
                || ret.startsWith("javax.jms.JMSSecurityException:"))
            {
                ret = ret.substring(ret.indexOf(":")+1).trim();
            }
        }
        return ret;
    }

    /**
     * Returns the given hostname with the domain removed.
     */
    public static String stripDomain(String hostname)
    {
        String ret = hostname;
        int pos = hostname.indexOf(".");
        if(pos != -1)
            ret = hostname.substring(0,pos);
        return ret;
    }

    /**
     * This method ensures that the output String has only
     * valid XML unicode characters as specified by the
     * XML 1.0 standard. For reference, please see
     * <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
     * standard</a>. This method will return an empty
     * String if the input is null or empty.
     *
     * @param in The String whose non-valid characters we want to remove.
     * @return The in String, stripped of non-valid characters.
     */
    public static String stripNonValidXMLCharacters(String in)
    {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if(in == null || ("".equals(in))) return ""; // vacancy test.
        for(int i = 0; i < in.length(); i++)
        {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9)
                || (current == 0xA)
                || (current == 0xD)
                || ((current >= 0x20) && (current <= 0xD7FF))
                || ((current >= 0xE000) && (current <= 0xFFFD))
                || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }
}