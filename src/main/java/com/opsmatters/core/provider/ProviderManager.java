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
 * Represents the interface for all provider managers.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public interface ProviderManager<T extends ProviderCache>
{
    /**
     * Returns <CODE>true</CODE> if the clients have been initialized.
     * @return <CODE>true</CODE> if the clients have been initialized
     */
    public boolean isInitialized();

    /**
     * Synchronises the cache.
     * @param cache The cache to synchronise
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean sync(T cache);  

    /**
     * Clears the cache.
     * @param cache The cache to synchronise
     */
    public void clear(T cache);  
}