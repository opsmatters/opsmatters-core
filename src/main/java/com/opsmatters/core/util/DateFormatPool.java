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

import java.util.ArrayList;
import java.text.SimpleDateFormat;

/**
 * Methods to define a utility class for a pool of SimpleDateFormat objects.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class DateFormatPool
{
    /**
     * Default constructor.
     */
    public DateFormatPool(int capacity)
    {
        this.capacity = capacity;
        freePool = new ArrayList(capacity);
        usedPool = new ArrayList(capacity);
        fill(capacity);
    }

    /**
     * Fills the pool with a set of objects.
     */
    public void fill(int size)
    {
        synchronized(this)
        {
            for(int i = 0; i < size; i++)
                add(new SimpleDateFormat());
        }
    }

    /**
     * Add a new resource to the pool
     */
    public void add(Object o)
    {
        synchronized(this)
        {
            freePool.add(o);
        }
    }

    /**
     * Retrieve a resource from the pool of free resources.
     */
    public Object get()
    {
        synchronized(this)
        {
            if (freePool.isEmpty() || usedPool.size() >= capacity)
            {
                try
                {
                    wait(1000);
                } 
                catch (InterruptedException e)
                {
                }

                // The timeout value was reached
                if (freePool.isEmpty())
                    add(new SimpleDateFormat());
            }

            Object o = freePool.get(0);
            freePool.remove(o);
            usedPool.add(o);
            return o;
        }
    }

    /**
     * Retrieve a date format from the pool of free resources.
     */
    public SimpleDateFormat getFormat(String format)
    {
        SimpleDateFormat f = (SimpleDateFormat)get();
        f.applyPattern(format);
        return f;
    }

    /**
     * Release a resource and put it back in the pool of free resources.
     */
    public void release(Object o)
    {
        synchronized(this)
        {
            usedPool.remove(o);
            freePool.add(o);
            notify();
        }
    }

    private ArrayList freePool;
    private ArrayList usedPool;
    private int capacity;
}