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
import com.opsmatters.newrelic.api.model.plugins.Plugin;

/**
 * Represents the new relic plugin cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class PluginCache extends ResourceCache<Plugin>
{
    private Map<Long,Plugin> plugins = new LinkedHashMap<Long,Plugin>();
    private Map<Long,PluginComponentCache> components = new LinkedHashMap<Long,PluginComponentCache>();

    /**
     * Default constructor.
     */
    public PluginCache()
    {
        super("Plugins");
    }

    /**
     * Adds the plugin to the plugins for the account.
     * @param plugin The plugin to add
     */
    public void add(Plugin plugin)
    {
        this.plugins.put(plugin.getId(), plugin);
    }

    /**
     * Adds the plugin list to the plugins for the account.
     * @param plugins The plugins to add
     */
    public void add(Collection<Plugin> plugins)
    {
        for(Plugin plugin : plugins)
            this.plugins.put(plugin.getId(), plugin);
    }

    /**
     * Returns the plugin for the given id.
     * @param id The id of the plugin
     * @return The plugin for the given id
     */
    public Plugin get(long id)
    {
        return this.plugins.get(id);
    }

    /**
     * Returns the plugins for the account.
     * @return The plugins for the account
     */
    public Collection<Plugin> list()
    {
        return this.plugins.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.plugins.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.plugins.clear();
    }

    /**
     * Returns the cache of plugin components for the given plugin, creating one if it doesn't exist .
     * @param pluginId The id of the plugin for the cache of plugin component
     * @return The cache of plugin components for the given plugin
     */
    public PluginComponentCache components(long pluginId)
    {
        PluginComponentCache cache = components.get(pluginId);
        if(cache == null)
            components.put(pluginId, cache = new PluginComponentCache(pluginId));
        return cache;
    }
}