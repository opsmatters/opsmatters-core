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

import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.SimpleTimeZone;
import java.util.Locale;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.LocaleUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A set of utility methods to perform miscellaneous tasks related to dates and calendars.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class DateUtilities
{
    /**
     * The default timezone.
     */
    public static final String DEFAULT_TIMEZONE = "Europe/London";

    /**
     * The default country.
     */
    public static final String DEFAULT_COUNTRY = "GB";

    /**
     * The default timezone.
     */
    public static final TimeZone defaultTZ = new SimpleTimeZone(0, "");

    /**
     * The current timezone.
     */
    public static TimeZone currentTZ = null;

    /**
     * The current locale.
     */
    public static Locale currentLocale = null;

    /**
     * The cache of locale data.
     */
    private static final ConcurrentMap<Locale, int[]> localeData = new ConcurrentHashMap(3);

    /**
     * The name of the BST time zone.
     */
    public static final String BST_TZ       = "GMT0BST";

    public static SimpleTimeZone tzBST;

    static
    {
        // Set up the default GMT0BST time zone
        tzBST = new SimpleTimeZone(0,     // no offset from GMT
            BST_TZ,                       // individualized tz id
            Calendar.MARCH,-1,Calendar.SUNDAY,1*60*60*1000,    // last Sun Mar 1AM
            Calendar.OCTOBER,-1,Calendar.SUNDAY,2*60*60*1000); // last Sun Oct 2AM
    }

    /**
     * Private constructor as this class shouldn't be instantiated.
     */
    private DateUtilities()
    {
    }    

    /**
     * Returns <CODE>true</CODE> if the given date is inside the start and end hours.
     */
    public static boolean isBetweenHours(long dt, TimeZone tz, int from, int to, int tolerance) 
    {
        long start = 0L;
        long end = 0L;

        // If from > to then the "to" date may be in the next day
        //   or the "from" date may be in the previous day, depending
        //   on whether or not it's before or after midnight
        if(from > to)
        {
            // Assume that start is in today and end is in tomorrow
            start = getDateForHour(dt, tz, from, 0);
            end = getDateForHour(dt, tz, to, 1)+(tolerance*1000L);

            // If we end up before the start date,
            //   try moving the dates back by a day
            if(dt < start) // move start to yesterday
            {
                start = getDateForHour(dt, tz, from, -1);
                end = getDateForHour(dt, tz, to, 0)+(tolerance*1000L);
            }
        }
        else // to > from
        {
            start = getDateForHour(dt, tz, from, 0);
            end = getDateForHour(dt, tz, to, 0)+(tolerance*1000L);
        }

        return dt >= start && dt <= end;
    }

    /**
     * Returns <CODE>true</CODE> if the current date is inside the start and end hours.
     */
    public static boolean isBetweenHours(int from, int to) 
    {
        return isBetweenHours(System.currentTimeMillis(), currentTZ, from, to, 0);
    }

    /**
     * Returns <CODE>true</CODE> if the current date is inside the start and end hours.
     */
    public static boolean isBetweenHours(int from, int to, TimeZone tz) 
    {
        return isBetweenHours(System.currentTimeMillis(), tz, from, to, 0);
    }

    /**
     * Returns <CODE>true</CODE> if the given date is inside the start and end hours.
     */
    public static boolean isBetweenHours(long dt, TimeZone tz, int from, int to) 
    {
        return isBetweenHours(dt, tz, from, to, 0);
    }

    /**
     * Returns the date for the given hour.
     */
    private static long getDateForHour(long dt, TimeZone tz, int hour, int dayoffset)
    {
        Calendar c = getCalendar(tz);
        c.setTimeInMillis(dt);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int mm = c.get(Calendar.MONTH);
        int yy = c.get(Calendar.YEAR);
        c.set(yy, mm, dd, hour, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        if(dayoffset != 0)
            c.add(Calendar.DAY_OF_MONTH, dayoffset);
        return c.getTimeInMillis();
    }

    /**
     * Returns a new calendar object using the current timezone and locale.
     */
    public static Calendar getCalendar(TimeZone tz, Locale locale)
    {
        if(tz == null)
            tz = getCurrentTimeZone();
        if(locale == null)
            locale = getCurrentLocale();
        return Calendar.getInstance(tz, locale);
    }

    /**
     * Returns a new calendar object using the current timezone.
     */
    public static Calendar getCalendar(TimeZone tz)
    {
        return getCalendar(tz, null);
    }

    /**
     * Returns a new calendar object in the current timezone.
     */
    public static Calendar getCalendar()
    {
        return getCalendar(null, null);
    }

    /**
     * Returns the current time zone.
     */
    public static TimeZone getCurrentTimeZone()
    {
        if(currentTZ == null)
            setCurrentTimeZone(getUserTimeZone());
        return currentTZ;
    }

    /**
     * Sets the current time zone.
     */
    public static void setCurrentTimeZone(TimeZone tz)
    {
        currentTZ = tz;
    }

    /**
     * Returns the current configured time zone.
     * <P>
     * Converts GMT to British Summer Time (BST) so that it uses Daylight Savings.
     */
    public static TimeZone getUserTimeZone()
    {
        TimeZone ret = null;

        // First try the configured timezone
        String tz = DEFAULT_TIMEZONE;
        ret = AppTimeZone.getTimeZoneByID(tz);
        if(ret == null)
        {
            // Next try the locale timezone
            tz = System.getProperty("user.timezone");

            // Next try the default timezone
            if(tz == null || tz.length() == 0)
                ret = TimeZone.getDefault();
            else if(tz.equals("GMT"))
                ret = tzBST;                     // Use BST instead of GMT
            else
                ret = TimeZone.getTimeZone(tz);  // Otherwise use the standard TZ
        }

        return ret;
    }

    /**
     * Returns the current locale.
     */
    public static Locale getCurrentLocale()
    {
        if(currentLocale == null)
            setCurrentLocale(getUserLocale());
        return currentLocale;
    }

    /**
     * Sets the current locale.
     */
    public static void setCurrentLocale(Locale locale)
    {
        currentLocale = locale;
    }

    /**
     * Returns the locale for the current country.
     */
    public static Locale getUserLocale()
    {
        String country = System.getProperty("user.country");
        return getLocale(country);
    }

    /**
     * Returns the country from the default locale.
     */
    public static String getCurrentCountry()
    {
        return getCurrentLocale().getCountry();
    }

    /**
     * Returns the locale for the given country.
     */
    public static Locale getLocale(String country)
    {
        if(country == null || country.length() == 0)
            country = Locale.getDefault().getCountry();
        List<Locale> locales = LocaleUtils.languagesByCountry(country);
        Locale locale = Locale.getDefault();
        if(locales.size() > 0)
            locale = locales.get(0); // Use the first locale that matches the country
        return locale;
    }

    /**
     * Set the attributes of the given calendar from the given locale.
     */
    public static void setCalendarData(Calendar calendar, Locale locale)
    {
        int[] array = (int[])localeData.get(locale);
        if(array == null)
        {
            Calendar c = Calendar.getInstance(locale);
            array = new int[2];
            array[0] = c.getFirstDayOfWeek();
            array[1] = c.getMinimalDaysInFirstWeek();
            localeData.putIfAbsent(locale, array);
        }
        calendar.setFirstDayOfWeek(array[0]);
        calendar.setMinimalDaysInFirstWeek(array[1]);
    }

    /**
     * Returns the given date adding the given number of days.
     */
    public static Date addDays(long dt, int days)
    {
        Calendar c = getCalendar();
        if(dt > 0L)
            c.setTimeInMillis(dt);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * Returns the system date adding the given number of days.
     */
    public static Date addDays(int days)
    {
        return addDays(0L, days);
    }

    /**
     * Returns the given date adjusted using the given timezone.
     */
    public static long convertToUTC(long l, String timezone)
    {
        long ret = l;
        if(timezone != null && timezone.length() > 0)
        {
            DateTime dt = new DateTime(l, DateTimeZone.forID(timezone));
            ret = dt.withZoneRetainFields(DateTimeZone.forID("UTC")).getMillis();
        }
        return ret;
    }

    /**
     * Returns the given date adjusted using the given timezone.
     */
    public static long convertFromUTC(long l, String timezone)
    {
        long ret = l;
        if(timezone != null && timezone.length() > 0)
        {
            DateTime dt = new DateTime(l, DateTimeZone.forID("UTC"));
            ret = dt.withZoneRetainFields(DateTimeZone.forID(timezone)).getMillis();
        }
        return ret;
    }
}