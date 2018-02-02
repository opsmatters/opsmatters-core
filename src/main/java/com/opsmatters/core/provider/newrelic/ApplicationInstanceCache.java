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

package com.opsmatters.core.provider.newrelic;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.applications.ApplicationInstance;

/**
 * Represents the new relic application instance cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationInstanceCache extends ResourceCache<ApplicationInstance>
{
    private long applicationHostId;
    private Map<Long,ApplicationInstance> applicationInstances = new LinkedHashMap<Long,ApplicationInstance>();

    /**
     * Constructor that takes an application host id.
     * @param applicationHostId The application host id for the cache
     */
    public ApplicationInstanceCache(long applicationHostId)
    {
        super("Application Instances");
        this.applicationHostId = applicationHostId;
    }

    /**
     * Returns the application host id for the cache.
     * @return The application host id for the cache
     */
    public long getApplicationHostId()
    {
        return applicationHostId;
    }

    /**
     * Adds the application instance to the application instances for the account.
     * @param applicationInstance The application instance to add
     */
    public void add(ApplicationInstance applicationInstance)
    {
        this.applicationInstances.put(applicationInstance.getId(), applicationInstance);
    }

    /**
     * Adds the application instance list to the application instances for the account.
     * @param applicationInstances The application instances to add
     */
    public void add(Collection<ApplicationInstance> applicationInstances)
    {
        for(ApplicationInstance applicationInstance : applicationInstances)
            this.applicationInstances.put(applicationInstance.getId(), applicationInstance);
    }

    /**
     * Returns the application instance for the given id.
     * @param id The id of the application instance
     * @return The application instance for the given id
     */
    public ApplicationInstance get(long id)
    {
        return this.applicationInstances.get(id);
    }

    /**
     * Returns the application instances for the account.
     * @return The application instances for the account
     */
    public Collection<ApplicationInstance> list()
    {
        return this.applicationInstances.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.applicationInstances.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.applicationInstances.clear();
    }
}