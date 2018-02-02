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
import java.util.List;
import java.util.Collection;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.synthetics.Monitor;
import com.opsmatters.newrelic.api.model.labels.Label;

/**
 * Represents the new relic monitor cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class MonitorCache extends ResourceCache<Monitor>
{
    private Map<String,Monitor> monitors = new LinkedHashMap<String,Monitor>();
    private Map<String,LabelCache> labels = new LinkedHashMap<String,LabelCache>();

    /**
     * Default constructor.
     */
    public MonitorCache()
    {
        super("Monitors");
    }

    /**
     * Adds the monitor to the monitors for the account.
     * @param monitor The monitor to add
     */
    public void add(Monitor monitor)
    {
        this.monitors.put(monitor.getId(), monitor);
    }

    /**
     * Adds the monitor list to the monitors for the account.
     * @param monitors The monitors to add
     */
    public void add(Collection<Monitor> monitors)
    {
        for(Monitor monitor : monitors)
            this.monitors.put(monitor.getId(), monitor);
    }

    /**
     * Returns the monitor for the given id.
     * @param id The id of the monitor
     * @return The monitor for the given id
     */
    public Monitor get(long id)
    {
        return this.monitors.get(id);
    }

    /**
     * Returns the monitors for the account.
     * @return The monitors for the account
     */
    public Collection<Monitor> list()
    {
        return this.monitors.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.monitors.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.monitors.clear();
    }

    /**
     * Returns the cache of labels for the given monitor, creating one if it doesn't exist .
     * @param monitorId The id of the monitor for the cache of labels
     * @return The cache of labels for the given monitor
     */
    public LabelCache labels(String monitorId)
    {
        LabelCache cache = labels.get(monitorId);
        if(cache == null)
            labels.put(monitorId, cache = new LabelCache(monitorId));
        return cache;
    }
}