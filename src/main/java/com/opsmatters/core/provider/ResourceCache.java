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

package com.opsmatters.core.provider;

import java.util.Collection;

/**
 * Represents the base class for all provider resource caches.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public abstract class ResourceCache<T>
{
    private String name = "";
    private long updatedAt = 0L;

    /**
     * Constructor that takes a name.
     * @param name The name of the items in the cache
     */
    public ResourceCache(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of the cache.
     * @return The cache name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the date the cache was last updated.
     * @param updatedAt The date the cache was last updated
     */
    protected void setUpdatedAt(long updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    /**
     * Sets the date the cache was last updated to the current date.
     */
    protected void setUpdatedAt()
    {
        setUpdatedAt(System.currentTimeMillis());
    }

    /**
     * Returns the date the cache was last updated.
     * @return The date the cache was last updated
     */
    public long getUpdatedAt()
    {
        return updatedAt;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return getName();
    }

    /**
     * Adds an item to the cache.
     * @param item The item to add
     */
    public abstract void add(T item);

    /**
     * Adds the list to the items in the cache.
     * @param items The items to add
     */
    public abstract void add(Collection<T> items);

    /**
     * Sets the cache to the items in the list.
     * @param items The items to set
     */
    public void set(Collection<T> items)
    {
        clear();
        add(items);
    }

    /**
     * Returns the item for the given id.
     * @param id The id of the item
     * @return The item for the given id
     */
    public abstract T get(long id);

    /**
     * Returns the items in the cache.
     * @return The items in the cache
     */
    public abstract Collection<T> list();

    /**
     * Returns the size of the cache.
     * @return The size of the cache
     */
    public abstract int size();

    /**
     * Clears the cache.
     */
    public abstract void clear();
}