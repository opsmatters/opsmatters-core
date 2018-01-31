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
import com.opsmatters.newrelic.api.model.plugins.PluginComponent;

/**
 * Represents a New Relic plugin component.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class PluginComponentWrapper extends EntityWrapper
{
    private PluginComponent component;

    /**
     * Constructor that takes an plugin component.
     * @param component The plugin component
     */
    public PluginComponentWrapper(PluginComponent component)
    {
        super(component);
        this.component = component;
    }
}