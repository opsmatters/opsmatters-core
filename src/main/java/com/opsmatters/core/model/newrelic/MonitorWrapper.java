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
import com.opsmatters.newrelic.api.model.synthetics.Monitor;

/**
 * Represents a New Relic Synthetics monitor.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class MonitorWrapper implements ResourceWrapper
{
    private Monitor monitor;
    private Map<String,LabelWrapper> labels = new LinkedHashMap<String,LabelWrapper>();

    /**
     * Constructor that takes a monitor.
     * @param monitor The monitor
     */
    public MonitorWrapper(Monitor monitor)
    {
        this.monitor = monitor;
    }

    /**
     * Returns the id of the monitor.
     * @return The id of the monitor
     */
    public String getId()
    {
        return monitor.getId();
    }

    /**
     * Returns the name of the monitor.
     * @return The name of the monitor
     */
    public String getName()
    {
        return monitor.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return monitor.toString();
    }

    /**
     * Returns the labels for the monitor.
     * @return The labels for the monitor
     */
    public Map<String,LabelWrapper> getLabels()
    {
        return labels;
    }

    /**
     * Adds the label to the labels for the monitor.
     * @param label The label to add
     */
    public void addLabel(LabelWrapper label)
    {
        labels.put(label.getKey(), label);
    }

    /**
     * Returns the label for the given key.
     * @param key The key of the label
     * @return The label for the given key
     */
    public LabelWrapper getLabel(String key)
    {
        return labels.get(key);
    }
}