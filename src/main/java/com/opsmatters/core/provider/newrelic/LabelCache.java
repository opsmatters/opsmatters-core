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
import com.opsmatters.newrelic.api.model.labels.Label;

/**
 * Represents the new relic label cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class LabelCache extends ResourceCache<Label>
{
    private String monitorId;
    private long applicationId;
    private Map<String,Label> labels = new LinkedHashMap<String,Label>();

    /**
     * Default constructor.
     */
    public LabelCache()
    {
        super("Labels");
    }

    /**
     * Constructor that takes a monitor id.
     * @param monitorId The monitor id for the cache
     */
    public LabelCache(String monitorId)
    {
        this();
        this.monitorId = monitorId;
    }

    /**
     * Constructor that takes an application id.
     * @param applicationId The application id for the cache
     */
    public LabelCache(long applicationId)
    {
        this();
        this.applicationId = applicationId;
    }

    /**
     * Returns the monitor id for the cache.
     * @return The monitor id for the cache
     */
    public String getMonitorId()
    {
        return monitorId;
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
     * Adds the label to the labels for the account.
     * @param label The label to add
     */
    public void add(Label label)
    {
        this.labels.put(label.getKey(), label);
    }

    /**
     * Adds the label list to the labels for the account.
     * @param labels The labels to add
     */
    public void add(Collection<Label> labels)
    {
        for(Label label : labels)
            this.labels.put(label.getKey(), label);
    }

    /**
     * Returns the label for the given id.
     * @param id The id of the label
     * @return The label for the given id
     */
    public Label get(long id)
    {
        return this.labels.get(id);
    }

    /**
     * Returns the labels for the account.
     * @return The labels for the account
     */
    public Collection<Label> list()
    {
        return this.labels.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.labels.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.labels.clear();
    }
}