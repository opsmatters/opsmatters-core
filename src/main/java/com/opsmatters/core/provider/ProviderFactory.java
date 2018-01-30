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
 * Represents the factory to create provider managers.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ProviderFactory
{
    /**
     * Private constructor.
     */
    private ProviderFactory()
    {
    }

    /**
     * Returns a new provider manager of the given type.
     * @param provider The provider to use for creating the manager
     * @return The provider manager created
     */
    public static ProviderManager getManager(Provider provider)
    {
        if(provider == Provider.NEW_RELIC)
            return new NewRelicManager();
        throw new IllegalArgumentException("not a valid provider");
    }  
}