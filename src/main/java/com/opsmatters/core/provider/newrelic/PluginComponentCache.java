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
import com.opsmatters.newrelic.api.model.plugins.PluginComponent;

/**
 * Represents the new relic plugin component cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class PluginComponentCache extends ResourceCache<PluginComponent>
{
    private long pluginId;
    private Map<Long,PluginComponent> components = new LinkedHashMap<Long,PluginComponent>();

    /**
     * Constructor that takes a plugin id.
     * @param pluginId The plugin id for the cache
     */
    public PluginComponentCache(long pluginId)
    {
        super("Plugin Components");
        this.pluginId = pluginId;
    }

    /**
     * Returns the plugin id for the cache.
     * @return The plugin id for the cache
     */
    public long getPluginId()
    {
        return pluginId;
    }

    /**
     * Adds the plugin component to the plugin components for the account.
     * @param component The plugin component to add
     */
    public void add(PluginComponent component)
    {
        this.components.put(component.getId(), component);
    }

    /**
     * Adds the plugin component list to the plugin components for the account.
     * @param components The plugin components to add
     */
    public void add(Collection<PluginComponent> components)
    {
        for(PluginComponent component : components)
            this.components.put(component.getId(), component);
    }

    /**
     * Returns the plugin component for the given id.
     * @param id The id of the plugin component
     * @return The plugin component for the given id
     */
    public PluginComponent get(long id)
    {
        return this.components.get(id);
    }

    /**
     * Returns the plugin components for the account.
     * @return The plugin components for the account
     */
    public Collection<PluginComponent> list()
    {
        return this.components.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.components.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.components.clear();
    }
}