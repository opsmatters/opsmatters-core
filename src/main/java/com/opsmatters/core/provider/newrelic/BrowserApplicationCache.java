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
import com.opsmatters.newrelic.api.model.applications.BrowserApplication;

/**
 * Represents the new relic browser application cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class BrowserApplicationCache extends ResourceCache<BrowserApplication>
{
    private Map<Long,BrowserApplication> browserApplications = new LinkedHashMap<Long,BrowserApplication>();

    /**
     * Default constructor.
     */
    public BrowserApplicationCache()
    {
        super("Browser Applications");
    }

    /**
     * Adds the browser application to the browser applications for the account.
     * @param browserApplication The browser application to add
     */
    public void add(BrowserApplication browserApplication)
    {
        this.browserApplications.put(browserApplication.getId(), browserApplication);
    }

    /**
     * Adds the browser application list to the browser applications for the account.
     * @param browserApplications The browser applications to add
     */
    public void add(Collection<BrowserApplication> browserApplications)
    {
        for(BrowserApplication browserApplication : browserApplications)
            this.browserApplications.put(browserApplication.getId(), browserApplication);
    }

    /**
     * Returns the browser application for the given id.
     * @param id The id of the browser application
     * @return The browser application for the given id
     */
    public BrowserApplication get(long id)
    {
        return this.browserApplications.get(id);
    }

    /**
     * Returns the browser applications for the account.
     * @return The browser applications for the account
     */
    public Collection<BrowserApplication> list()
    {
        return this.browserApplications.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.browserApplications.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.browserApplications.clear();
    }
}