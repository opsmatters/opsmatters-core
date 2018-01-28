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

import java.util.*;
import java.text.*;

/**
 * A set of utility methods to perform miscellaneous tasks related to formatting.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class FormatUtilities extends Formats
{
    /**
     * The pool of date formats.
     */
    private static DateFormatPool formatPool = new DateFormatPool(50);

    /**
     * Private constructor as this class shouldn't be instantiated.
     */
    private FormatUtilities()
    {
    }    

    /**
     * Returns the given bytes number formatted as KBytes, MBytes or GBytes as appropriate.
     * @param bytes The bytes to be converted
     * @return The given bytes number formatted as KBytes, MBytes or GBytes as appropriate
     */
    static public String getFormattedBytes(long bytes)
    {
        return getFormattedBytes(bytes, "Bytes", "0.0#");
    }

    /**
     * Returns the given bytes number formatted as KBytes, MBytes or GBytes as appropriate.
     * @param bytes The bytes to be converted
     * @param units The units to be displayed with the converted bytes
     * @return The given bytes number formatted as KBytes, MBytes or GBytes as appropriate
     */
    static public String getFormattedBytes(long bytes, String units)
    {
        return getFormattedBytes(bytes, units, "0.0#");
    }

    /**
     * Returns the given bytes number formatted as KBytes, MBytes or GBytes as appropriate.
     * @param bytes The bytes to be converted
     * @param units The units to be displayed with the converted bytes
     * @param format The format to use to display the bytes
     * @return The given bytes number formatted as KBytes, MBytes or GBytes as appropriate
     */
    static public String getFormattedBytes(long bytes, String units, String format)
    {
        double num = bytes;
        String[] prefix = {"", "K", "M", "G", "T"};
        int count = 0;

        while(num >= 1024.0)
        {
            num = num/1024.0;
            ++count;
        }

        DecimalFormat f = new DecimalFormat(format);
        return f.format(num)+" "+prefix[count]+units;
    }

    /**
     * Returns the current system date formatted as "dd-MM-yyyy HH:mm:ss".
     * @return The current system date formatted as "dd-MM-yyyy HH:mm:ss"
     */
    static public String getFormattedDateTime()
    {
        return getFormattedDateTime(System.currentTimeMillis(), DATETIME_FORMAT, true, 0L);
    }

    /**
     * Returns the current system date formatted using the given format.
     * @param format The format to use to display the date
     * @return The current system date formatted using the given format
     */
    static public String getFormattedDateTime(String format)
    {
        return getFormattedDateTime(System.currentTimeMillis(), format, true, 0L);
    }

    /**
     * Returns the given date formatted as "dd-MM-yyyy HH:mm:ss".
     * @param dt The date to be formatted
     * @return The given date formatted as "dd-MM-yyyy HH:mm:ss"
     */
    static public String getFormattedDateTime(long dt)
    {
        return getFormattedDateTime(dt, DATETIME_FORMAT, true, 0L);
    }

    /**
     * Returns the given date formatted using the given format.
     * @param dt The date to be formatted
     * @param format The format to use to display the date
     * @param tolerance The tolerance for the date to be formatted
     * @return The given date formatted using the given format
     */
    static public String getFormattedDateTime(long dt, String format, long tolerance)
    {
        return getFormattedDateTime(dt, format, true, tolerance);
    }

    /**
     * Returns the given date formatted using the given format.
     * @param dt The date to be formatted
     * @param format The format to use to display the date
     * @return The given date formatted using the given format
     */
    static public String getFormattedDateTime(long dt, String format)
    {
        return getFormattedDateTime(dt, format, true, 0L);
    }

    /**
     * Returns the given date formatted using the given format.
     * @param dt The date to be formatted
     * @param format The format to use to display the date
     * @param useTZ <CODE>true</CODE> if the date should be converted using the current timezone
     * @param tolerance The tolerance for the date to be formatted
     * @return The given date formatted using the given format
     */
    static public String getFormattedDateTime(long dt, String format, 
        boolean useTZ, long tolerance)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(dt > tolerance)
            {
                df = formatPool.getFormat(format);
                if(useTZ)
                    df.setTimeZone(DateUtilities.getCurrentTimeZone());
                else
                    df.setTimeZone(TimeZone.getTimeZone("GMT"));
                ret = df.format(new Date(dt));
            }
        }
        catch(Exception e)
        {
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given date formatted as "dd-MM-yyyy HH:mm:ss" in the given timezone and country.
     * @param dt The date to be formatted
     * @param tz The timezone associated with the date
     * @param country The country associated with the date
     * @return The given date formatted as "dd-MM-yyyy HH:mm:ss" in the given timezone and country
     */
    static public String getFormattedDateTime(long dt, TimeZone tz, String country)
    {
        return getFormattedDateTime(dt, DATETIME_FORMAT, tz, country, 0L);
    }

    /**
     * Returns the given date formatted using the given format, timezone and country.
     * @param dt The date to be formatted
     * @param format The format to use to display the date
     * @param tz The timezone associated with the date
     * @param country The country associated with the date
     * @return The given date formatted using the given format, timezone and country
     */
    static public String getFormattedDateTime(long dt, String format, TimeZone tz, String country)
    {
        return getFormattedDateTime(dt, format, tz, country, 0L);
    }

    /**
     * Returns the given date formatted using the given format, timezone and country.
     * @param dt The date to be formatted
     * @param format The format to use to display the date
     * @param tz The timezone associated with the date
     * @param country The country associated with the date
     * @param tolerance The tolerance for the date to be formatted
     * @return The given date formatted using the given format, timezone and country
     */
    static public String getFormattedDateTime(long dt, String format, TimeZone tz, String country, long tolerance)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(dt > tolerance)
            {
                df = formatPool.getFormat(format);
                df.setTimeZone(tz);
                DateUtilities.setCalendarData(df.getCalendar(), DateUtilities.getLocale(country));
                ret = df.format(new Date(dt));
            }
        }
        catch(Exception e)
        {
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given date parsed using "dd-MM-yyyy HH:mm:ss".
     * @param s The formatted date to be parsed
     * @return The given date parsed using "dd-MM-yyyy HH:mm:ss"
     */
    static public long getDateTime(String s)
    {
        return getDateTime(s, DATETIME_FORMAT);
    }

    /**
     * Returns the given date parsed using the given format.
     * @param s The formatted date to be parsed
     * @param format The format to use when parsing the date
     * @return The given date parsed using the given format
     */
    static public long getDateTime(String s, String format)
    {
        return getDateTime(s, format, true, false);
    }

    /**
     * Returns the given date parsed using the given format.
     * @param s The formatted date to be parsed
     * @param format The format to use when parsing the date
     * @param useTZ <CODE>true</CODE> if the date should be parsed using the current timezone
     * @return The given date parsed using the given format
     */
    static public long getDateTime(String s, String format, boolean useTZ)
    {
        return getDateTime(s, format, useTZ, false);
    }

    /**
     * Returns the given date time parsed using the given format.
     * @param s The formatted date to be parsed
     * @param format The format to use when parsing the date
     * @param useTZ <CODE>true</CODE> if the date should be parsed using the current timezone
     * @param throwException <CODE>true</CODE> if an exception should be thrown for an illegal date format
     * @return The given date parsed using the given format
     */
    static public long getDateTime(String s, String format, boolean useTZ, boolean throwException)
    {
        long ret = 0L;
        SimpleDateFormat df = null;

        try
        {
            if(s!= null && s.length() > 0)
            {
                df = formatPool.getFormat(format);
                if(useTZ)
                    df.setTimeZone(DateUtilities.getCurrentTimeZone());
                else
                    df.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dt = df.parse(s);
                ret = dt.getTime();
            }
        }
        catch(ParseException e)
        {
            if(throwException)
                throw new RuntimeException(e);
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given date time parsed using the given format.
     * @param s The formatted date to be parsed
     * @param format The format to use when parsing the date
     * @param tz The timezone associated with the date
     * @return The given date parsed using the given format
     */
    static public long getDateTime(String s, String format, TimeZone tz)
    {
        long ret = 0L;
        SimpleDateFormat df = null;

        try
        {
            if(s != null && s.length() > 0)
            {
                df = formatPool.getFormat(format);
                df.setTimeZone(tz);
                Date dt = df.parse(s);
                ret = dt.getTime();
            }
        }
        catch(Exception e)
        {
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given date parsed using "dd-MM-yyyy".
     * @param dt The date to be parsed
     * @return The given date parsed using "dd-MM-yyyy"
     */
    static public String getFormattedDate(long dt)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(dt > 0)
            {
                df = formatPool.getFormat(DATE_FORMAT);
                df.setTimeZone(DateUtilities.getCurrentTimeZone());
                ret = df.format(new Date(dt));
            }
        }
        catch(Exception e)
        {
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given time parsed using "HH:mm:ss".
     * @param dt The time to be parsed
     * @return The given time parsed using "HH:mm:ss"
     */
    static public String getFormattedTime(long dt)
    {
        return getFormattedTime(dt, true);
    }

    /**
     * Returns the given time parsed using "HH:mm:ss".
     * @param dt The time to be parsed
     * @param isTime <CODE>true</CODE> if the given time has a timezone
     * @return The given time parsed using "HH:mm:ss"
     */
    static public String getFormattedTime(long dt, boolean isTime)
    {
        return getFormattedTime(dt, TIME_FORMAT, isTime);
    }

    /**
     * Returns the given time parsed using "HH:mm:ss".
     * @param dt The time to be parsed
     * @param format The format to use when parsing the date
     * @param isTime <CODE>true</CODE> if the given time has a timezone
     * @return The given time parsed using "HH:mm:ss"
     */
    static public String getFormattedTime(long dt, String format, boolean isTime)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(dt > 0)
            {
                df = formatPool.getFormat(format);
                if(isTime)
                    df.setTimeZone(DateUtilities.getCurrentTimeZone());
                else
                    df.setTimeZone(DateUtilities.defaultTZ);
                ret = df.format(new Date(dt));
            }
        }
        catch(Exception e)
        {
        }

        if(df != null)
            formatPool.release(df);

        return ret;
    }

    /**
     * Returns the given time formatted as a number of days plus "HH:mm:ss".
     * @param dt The time to be parsed
     * @return The given time formatted as a number of days plus "HH:mm:ss"
     */
    static public String getFormattedDays(long dt)
    {
        StringBuffer ret = new StringBuffer();

        long days = dt/86400000L;
        long millis = dt-(days*86400000L);
        if(days > 0)
        {
            ret.append(Long.toString(days));
            ret.append(" days ");
        }
        ret.append(getFormattedTime(millis, false));

        return ret.toString();
    }

    /**
     * Returns the given time formatted as a number of days, hours and minutes.
     * @param dt The time to be parsed
     * @return The given time formatted as a number of days, hours and minutes
     */
    static public String getLongFormattedDays(long dt)
    {
        StringBuffer ret = new StringBuffer();

        long days = dt/86400000L;
        long millis = dt-(days*86400000L);
        if(days > 0)
        {
            ret.append(Long.toString(days));
            ret.append(" day");
            if(days > 1)
                ret.append("s");
        }

        long hours = millis/3600000L;
        millis = millis-(hours*3600000L);

        if(hours > 0)
        {
            if(ret.length() > 0)
                ret.append(" ");
            ret.append(Long.toString(hours));
            ret.append(" hour");
            if(hours > 1)
                ret.append("s");
        }

        long minutes = millis/60000L;
        millis = millis-(minutes*60000L);

        if(minutes > 0)
        {
            if(ret.length() > 0)
                ret.append(" ");
            ret.append(Long.toString(minutes));
            ret.append(" minute");
            if(minutes > 1)
                ret.append("s");
        }

        long seconds = millis/1000L;
        if(seconds > 0)
        {
            if(ret.length() > 0)
                ret.append(" ");
            ret.append(Long.toString(seconds));
            ret.append(" second");
            if(seconds > 1)
                ret.append("s");
        }

        return ret.toString();
    }

    /**
     * Returns the given fractional number of seconds formatted as "0.0##".
     * @param t The seconds to be formatted
     * @return The given fractional number of seconds formatted as "0.0##"
     */
    static public String getFormattedSeconds(double t)
    {
        DecimalFormat f = new DecimalFormat("0.0##");
        return f.format(t)+"s";
    }

    /**
     * Returns the given fractional percentage formatted as "0.0#%".
     * @param t The percentage to be formatted
     * @return The given fractional percentage formatted as "0.0#%"
     */
    static public String getFormattedPercentage(double t)
    {
        DecimalFormat f = new DecimalFormat("0.0#");
        return f.format(t)+"%";
    }
}