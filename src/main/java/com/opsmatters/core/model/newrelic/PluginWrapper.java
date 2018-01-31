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

package com.opsmatters.core.model.newrelic;

import java.util.Map;
import java.util.LinkedHashMap;
import com.opsmatters.newrelic.api.model.plugins.Plugin;
import com.opsmatters.newrelic.api.model.plugins.PluginComponent;

/**
 * Represents a New Relic plugin.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class PluginWrapper extends EntityWrapper
{
    private Plugin plugin;
    private Map<Long,PluginComponentWrapper> pluginComponents = new LinkedHashMap<Long,PluginComponentWrapper>();

    /**
     * Constructor that takes an plugin.
     * @param plugin The plugin
     */
    public PluginWrapper(Plugin plugin)
    {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Returns the components for the plugin.
     * @return The components for the plugin
     */
    public Map<Long,PluginComponentWrapper> getPluginCompnents()
    {
        return pluginComponents;
    }

    /**
     * Adds the component to the components for the plugin.
     * @param pluginComponent The component to add
     */
    public void addPluginComponent(PluginComponentWrapper pluginComponent)
    {
        pluginComponents.put(pluginComponent.getId(), pluginComponent);
    }

    /**
     * Returns the component for the given id.
     * @param componentId The id of the component
     * @return The component for the given id
     */
    public PluginComponentWrapper getPluginCompnent(long componentId)
    {
        return pluginComponents.get(componentId);
    }
}