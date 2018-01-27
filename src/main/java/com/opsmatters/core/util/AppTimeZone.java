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

import java.util.TimeZone;
import java.util.Vector;
import java.util.Comparator;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper class to store and display a timezone.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class AppTimeZone
{
    private TimeZone tz;
    private String name = "";

    private static AppTimeZone[] timezones = null;
    private static String[] IDs = null;
    private static String[] names = null;

    // Reduced list of "main" timezons
    public static String[] TIMEZONES =
    {
        "Africa/Johannesburg",
        //"America/Argentina/Buenos_Aires",
        "America/Lima",
        "America/Montevideo",
        "Asia/Calcutta",
        "Asia/Istanbul",
        "Asia/Qatar",
        "Asia/Tel_Aviv",
        "Asia/Tokyo",
        "Australia/North",
        "Australia/South",
        "Australia/Sydney",
        "Australia/West",
        "Brazil/East",
        "Canada/Atlantic",
        "Canada/Eastern",
        "Canada/Central",
        "Canada/Mountain",
        "Canada/Newfoundland",
        "Canada/Pacific",
        "Chile/Continental",
        "Europe/Amsterdam",
        "Europe/Belfast",
        "Europe/Berlin",
        "Europe/Brussels",
        "Europe/Bucharest",
        "Europe/Budapest",
        "Europe/Dublin",
        "Europe/Lisbon",
        "Europe/London",
        "Europe/Madrid",
        "Europe/Paris",
        "Europe/Rome",
        "Europe/Vienna",
        "Europe/Zurich",
        "Europe/Warsaw",
        "Mexico/BajaNorte",
        "Mexico/BajaSur",
        "Mexico/General",
        "Pacific/Auckland",
        "US/Central",
        "US/Eastern",
        "US/Mountain",
        "US/Pacific",
        "Greenwich",
        "Universal",
    };

    /**
     * Constructor that takes a timezone.
     */
    public AppTimeZone(TimeZone t)
    {
        tz = t;
        name = getDisplayName();
    }

    /**
     * Returns the id of the timezone.
     */
    public String getID()
    {
        return tz.getID();
    }

    /**
     * Returns the offset in milliseconds of the timezone.
     */
    public int getRawOffset()
    {
        return tz.getRawOffset();
    }

    /**
     * Returns the name of the timezone.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the string value of the timezone.
     */
    public String toString()
    {
        return getName();
    }

    /**
     * Returns the timezone wrapped by this object.
     */
    public TimeZone getTimeZone()
    {
        return tz;
    }

    /**
     * Returns the set of cached timezones.
     */
    public static AppTimeZone[] getAppTimeZones()
    {
        return timezones;
    }

    /**
     * Returns the set of IDs for the cached timezones.
     */
    public static String[] getIDs()
    {
        return IDs;
    }

    /**
     * Returns the set of names for the cached timezones.
     */
    public static String[] getNames()
    {
        return names;
    }

    /**
     * Returns the cached timezone with the given name.
     */
    public static TimeZone getTimeZone(String name)
    {
        TimeZone ret = null;
        if(timezones != null)
        {
            for(int i = 0; i < timezones.length && ret == null; i++)
            {
                if(timezones[i].getName().equals(name))
                    ret = timezones[i].getTimeZone();
            }
        }
        return ret;
    }

    /**
     * Returns the cached timezone with the given ID.
     */
    public static TimeZone getTimeZoneByID(String id)
    {
        TimeZone ret = null;
        if(timezones != null)
        {
            for(int i = 0; i < timezones.length && ret == null; i++)
            {
                if(timezones[i].getID().equals(id))
                    ret = timezones[i].getTimeZone();
            }
        }
        return ret;
    }

    /**
     * Returns the cached timezone with the given ID ignoring case.
     */
    public static TimeZone getTimeZoneByIDIgnoreCase(String id)
    {
        TimeZone ret = null;
        if(timezones != null)
        {
            id = id.toLowerCase();
            for(int i = 0; i < timezones.length && ret == null; i++)
            {
                if(timezones[i].getID().toLowerCase().equals(id))
                    ret = timezones[i].getTimeZone();
            }
        }
        return ret;
    }

    /**
     * Returns the display name of the timezone.
     */
    private String getDisplayName()
    {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
            -TimeUnit.HOURS.toMinutes(hours));
        return String.format("(GMT%+d:%02d) %s", hours, minutes, tz.getID());
    }

    static
    {
        // Cache all the available timezones
        Vector v = new Vector();
        //String[] ids = TimeZone.getAvailableIDs();
        String[] ids = TIMEZONES;
        for(int i = 0; i < ids.length; i++)
        {
            String id = ids[i];
            if((id.length() > 3) && !id.startsWith("Etc"))
            {
                TimeZone tz = TimeZone.getTimeZone(id);
                String tzName = tz.getDisplayName();

                // Don't include zones with generic "GMT+n" descriptions.
                if(!tzName.startsWith("GMT-") && !tzName.startsWith("GMT+")) 
                    v.addElement(new AppTimeZone(tz));
            }
        }

        Collections.sort(v, new Comparator()
        {
            public int compare(Object a, Object b)
            {
                AppTimeZone tz1 = (AppTimeZone)a;
                AppTimeZone tz2 = (AppTimeZone)b;
                int offset1 = tz1.getRawOffset();
                int offset2 = tz2.getRawOffset();
                if(offset1 == offset2)
                    return tz1.getID().compareTo(tz2.getID());
                else if(offset1 > offset2)
                    return 1;
                else
                    return -1;
            }
        });

        AppTimeZone[] ret = new AppTimeZone[v.size()];
        timezones = (AppTimeZone[])v.toArray(ret);

        IDs = new String[v.size()+1];
        names = new String[v.size()+1];
        IDs[0] = "";
        names[0] = "";
        for(int i = 0; i < v.size(); i++)
        {
            IDs[i+1] = timezones[i].getID();
            names[i+1] = timezones[i].getName();
        }
    }
}