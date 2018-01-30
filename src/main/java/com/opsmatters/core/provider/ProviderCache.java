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

/**
 * Represents the base class for all provider caches.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public abstract class ProviderCache
{
    private Provider provider;
    private long updatedAt = 0L;

    /**
     * Constructor that takes a provider.
     * @param provider The provider for the cache
     */
    public ProviderCache(Provider provider)
    {
        this.provider = provider;
    }

    /**
     * Returns the provider.
     * @return the provider
     */
    public Provider getProvider()
    {
        return provider;
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
        return "provider="+provider
            +"]";
    }
}