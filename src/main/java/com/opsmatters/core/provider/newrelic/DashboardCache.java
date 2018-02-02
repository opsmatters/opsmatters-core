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
import com.opsmatters.newrelic.api.model.insights.Dashboard;

/**
 * Represents the new relic dashboard cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class DashboardCache extends ResourceCache<Dashboard>
{
    private Map<Long,Dashboard> dashboards = new LinkedHashMap<Long,Dashboard>();

    /**
     * Default constructor.
     */
    public DashboardCache()
    {
        super("Dashboards");
    }

    /**
     * Adds the dashboard to the dashboards for the account.
     * @param dashboard The dashboard to add
     */
    public void add(Dashboard dashboard)
    {
        this.dashboards.put(dashboard.getId(), dashboard);
    }

    /**
     * Adds the dashboard list to the dashboards for the account.
     * @param dashboards The dashboards to add
     */
    public void add(Collection<Dashboard> dashboards)
    {
        for(Dashboard dashboard : dashboards)
            this.dashboards.put(dashboard.getId(), dashboard);
    }

    /**
     * Returns the dashboard for the given id.
     * @param id The id of the dashboard
     * @return The dashboard for the given id
     */
    public Dashboard get(long id)
    {
        return this.dashboards.get(id);
    }

    /**
     * Returns the dashboards for the account.
     * @return The dashboards for the account
     */
    public Collection<Dashboard> list()
    {
        return this.dashboards.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.dashboards.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.dashboards.clear();
    }
}