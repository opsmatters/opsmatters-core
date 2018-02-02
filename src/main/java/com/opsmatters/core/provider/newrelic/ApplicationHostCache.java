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
import java.util.logging.Logger;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.applications.ApplicationHost;
import com.opsmatters.newrelic.api.model.applications.ApplicationInstance;

/**
 * Represents the new relic application host cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationHostCache extends ResourceCache<ApplicationHost>
{
    private static final Logger logger = Logger.getLogger(ApplicationHostCache.class.getName());

    private long applicationId;
    private Map<Long,ApplicationHost> applicationHosts = new LinkedHashMap<Long,ApplicationHost>();
    private Map<Long,ApplicationInstanceCache> applicationInstances = new LinkedHashMap<Long,ApplicationInstanceCache>();

    /**
     * Constructor that takes an application id.
     * @param applicationId The application id for the cache
     */
    public ApplicationHostCache(long applicationId)
    {
        super("Application Hosts");
        this.applicationId = applicationId;
    }

    /**
     * Returns the application id for the cache.
     * @return The application id for the cache
     */
    public long getApplicationId()
    {
        return applicationId;
    }

    /**
     * Adds the application host to the application hosts for the account.
     * @param applicationHost The application host to add
     */
    public void add(ApplicationHost applicationHost)
    {
        this.applicationHosts.put(applicationHost.getId(), applicationHost);
    }

    /**
     * Adds the application host list to the application hosts for the account.
     * @param applicationHosts The application hosts to add
     */
    public void add(Collection<ApplicationHost> applicationHosts)
    {
        for(ApplicationHost applicationHost : applicationHosts)
            this.applicationHosts.put(applicationHost.getId(), applicationHost);
    }

    /**
     * Returns the application host for the given id.
     * @param id The id of the application host
     * @return The application host for the given id
     */
    public ApplicationHost get(long id)
    {
        return this.applicationHosts.get(id);
    }

    /**
     * Returns the application hosts for the account.
     * @return The application hosts for the account
     */
    public Collection<ApplicationHost> list()
    {
        return this.applicationHosts.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.applicationHosts.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.applicationHosts.clear();
    }

    /**
     * Returns the cache of application instances for the given application host, creating one if it doesn't exist .
     * @param applicationHostId The id of the application host for the cache of application instances
     * @return The cache of application instances for the given application host
     */
    public ApplicationInstanceCache applicationInstances(long applicationHostId)
    {
        ApplicationInstanceCache cache = applicationInstances.get(applicationHostId);
        if(cache == null)
            applicationInstances.put(applicationHostId, cache = new ApplicationInstanceCache(applicationHostId));
        return cache;
    }

    /**
     * Adds the application instances to the applications for the account.
     * @param applicationInstances The application instances to add
     */
    public void addApplicationInstances(Collection<ApplicationInstance> applicationInstances)
    {
        for(ApplicationInstance applicationInstance : applicationInstances)
        {
            // Add the instance to any application hosts it is associated with
            long applicationHostId = applicationInstance.getLinks().getApplicationHost();
            ApplicationHost applicationHost = applicationHosts.get(applicationHostId);
            if(applicationHost != null)
                applicationInstances(applicationHostId).add(applicationInstance);
            else
                logger.severe(String.format("Unable to find application host for application instance '%s': %d", applicationInstance.getName(), applicationHostId));
        }
    }
}