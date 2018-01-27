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
     */
    static public String getFormattedBytes(long bytes)
    {
        return getFormattedBytes(bytes, "Bytes", "0.0#");
    }

    /**
     * Returns the given bytes number formatted as KBytes, MBytes or GBytes as appropriate.
     */
    static public String getFormattedBytes(long bytes, String units)
    {
        return getFormattedBytes(bytes, units, "0.0#");
    }

    /**
     * Returns the given bytes number formatted as KBytes, MBytes or GBytes as appropriate.
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
     * Returns the current system time formatted as "dd-MM-yyyy HH:mm:ss".
     */
    static public String getFormattedDateTime()
    {
        return getFormattedDateTime(System.currentTimeMillis(), DATETIME_FORMAT, true, 0L);
    }

    /**
     * Returns the current system time formatted using the given format.
     */
    static public String getFormattedDateTime(String format)
    {
        return getFormattedDateTime(System.currentTimeMillis(), format, true, 0L);
    }

    /**
     * Returns the given date time formatted as "dd-MM-yyyy HH:mm:ss".
     */
    static public String getFormattedDateTime(long l)
    {
        return getFormattedDateTime(l, DATETIME_FORMAT, true, 0L);
    }

    /**
     * Returns the given date time formatted using the given format.
     */
    static public String getFormattedDateTime(long l, String format, long tolerance)
    {
        return getFormattedDateTime(l, format, true, tolerance);
    }

    /**
     * Returns the given date time formatted using the given format.
     */
    static public String getFormattedDateTime(long l, String format)
    {
        return getFormattedDateTime(l, format, true, 0L);
    }

    /**
     * Returns the given date time formatted using the given format.
     */
    static public String getFormattedDateTime(long l, String format, 
        boolean useTZ, long tolerance)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(l > tolerance)
            {
                df = formatPool.getFormat(format);
                if(useTZ)
                    df.setTimeZone(DateUtilities.getCurrentTimeZone());
                else
                    df.setTimeZone(TimeZone.getTimeZone("GMT"));
                ret = df.format(new Date(l));
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
     * Returns the given date time formatted as "dd-MM-yyyy HH:mm:ss" in the given timezone and country.
     */
    static public String getFormattedDateTime(long l, TimeZone tz, String country)
    {
        return getFormattedDateTime(l, DATETIME_FORMAT, tz, country, 0L);
    }

    /**
     * Returns the given date time formatted using the given format, timezone and country.
     */
    static public String getFormattedDateTime(long l, String format, TimeZone tz, String country)
    {
        return getFormattedDateTime(l, format, tz, country, 0L);
    }

    /**
     * Returns the given date time formatted using the given format, timezone and country.
     */
    static public String getFormattedDateTime(long l, String format, TimeZone tz, String country, long tolerance)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(l > tolerance)
            {
                df = formatPool.getFormat(format);
                df.setTimeZone(tz);
                DateUtilities.setCalendarData(df.getCalendar(), DateUtilities.getLocale(country));
                ret = df.format(new Date(l));
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
     * Returns the given date time parsed using "dd-MM-yyyy HH:mm:ss".
     */
    static public long getDateTime(String s)
    {
        return getDateTime(s, DATETIME_FORMAT);
    }

    /**
     * Returns the given date time parsed using the given format.
     */
    static public long getDateTime(String s, String fmt)
    {
        return getDateTime(s, fmt, true, false);
    }

    /**
     * Returns the given date time parsed using the given format.
     */
    static public long getDateTime(String s, String fmt, boolean useTZ)
    {
        return getDateTime(s, fmt, useTZ, false);
    }

    /**
     * Returns the given date time parsed using the given format.
     */
    static public long getDateTime(String s, String fmt, boolean useTZ, boolean throwException)
    {
        long ret = 0L;
        SimpleDateFormat df = null;

        try
        {
            if(s!= null && s.length() > 0)
            {
                df = formatPool.getFormat(fmt);
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
     */
    static public long getDateTime(String s, String fmt, TimeZone tz)
    {
        long ret = 0L;
        SimpleDateFormat df = null;

        try
        {
            if(s!= null && s.length() > 0)
            {
                df = formatPool.getFormat(fmt);
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
     */
    static public String getFormattedDate(long l)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(l > 0)
            {
                df = formatPool.getFormat(DATE_FORMAT);
                df.setTimeZone(DateUtilities.getCurrentTimeZone());
                ret = df.format(new Date(l));
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
     */
    static public String getFormattedTime(long l)
    {
        return getFormattedTime(l, true);
    }

    /**
     * Returns the given time parsed using "HH:mm:ss".
     */
    static public String getFormattedTime(long l, boolean isTime)
    {
        return getFormattedTime(l, TIME_FORMAT, isTime);
    }

    /**
     * Returns the given time parsed using "HH:mm:ss".
     */
    static public String getFormattedTime(long l, String fmt, boolean isTime)
    {
        String ret = "";
        SimpleDateFormat df = null;

        try
        {
            if(l > 0)
            {
                df = formatPool.getFormat(fmt);
                if(isTime)
                    df.setTimeZone(DateUtilities.getCurrentTimeZone());
                else
                    df.setTimeZone(DateUtilities.defaultTZ);
                ret = df.format(new Date(l));
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
     */
    static public String getFormattedDays(long l)
    {
        StringBuffer ret = new StringBuffer();

        long days = l/86400000L;
        long millis = l-(days*86400000L);
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
     */
    static public String getLongFormattedDays(long l)
    {
        StringBuffer ret = new StringBuffer();

        long days = l/86400000L;
        long millis = l-(days*86400000L);
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
     */
    static public String getFormattedSeconds(double t)
    {
        DecimalFormat f = new DecimalFormat("0.0##");
        return f.format(t)+"s";
    }

    /**
     * Returns the given fractional percentage formatted as "0.0#%".
     */
    static public String getFormattedPercentage(double t)
    {
        DecimalFormat f = new DecimalFormat("0.0#");
        return f.format(t)+"%";
    }
}