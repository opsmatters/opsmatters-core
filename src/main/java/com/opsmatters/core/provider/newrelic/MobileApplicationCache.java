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
import com.opsmatters.newrelic.api.model.applications.MobileApplication;

/**
 * Represents the new relic mobile application cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class MobileApplicationCache extends ResourceCache<MobileApplication>
{
    private Map<Long,MobileApplication> mobileApplications = new LinkedHashMap<Long,MobileApplication>();

    /**
     * Default constructor.
     */
    public MobileApplicationCache()
    {
        super("Mobile Applications");
    }

    /**
     * Adds the mobile application to the mobile applications for the account.
     * @param mobileApplication The mobile application to add
     */
    public void add(MobileApplication mobileApplication)
    {
        this.mobileApplications.put(mobileApplication.getId(), mobileApplication);
    }

    /**
     * Adds the mobile application list to the mobile applications for the account.
     * @param mobileApplications The mobile applications to add
     */
    public void add(Collection<MobileApplication> mobileApplications)
    {
        for(MobileApplication mobileApplication : mobileApplications)
            this.mobileApplications.put(mobileApplication.getId(), mobileApplication);
    }

    /**
     * Returns the mobile application for the given id.
     * @param id The id of the mobile application
     * @return The mobile application for the given id
     */
    public MobileApplication get(long id)
    {
        return this.mobileApplications.get(id);
    }

    /**
     * Returns the mobile applications for the account.
     * @return The mobile applications for the account
     */
    public Collection<MobileApplication> list()
    {
        return this.mobileApplications.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.mobileApplications.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.mobileApplications.clear();
    }
}