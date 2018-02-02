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
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;

/**
 * Represents the new relic alert channel cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class AlertChannelCache extends ResourceCache<AlertChannel>
{
    private long policyId;
    private Map<Long,AlertChannel> channels = new LinkedHashMap<Long,AlertChannel>();

    /**
     * Default constructor.
     */
    public AlertChannelCache()
    {
        super("Alert Channels");
    }

    /**
     * Constructor that takes a policy id.
     * @param policyId The policy id for the cache
     */
    public AlertChannelCache(long policyId)
    {
        this();
        this.policyId = policyId;
    }

    /**
     * Returns the policy id for the cache.
     * @return The policy id for the cache
     */
    public long getPolicyId()
    {
        return policyId;
    }

    /**
     * Adds the channel to the channels for the account.
     * @param channel The channel to add
     */
    public void add(AlertChannel channel)
    {
        this.channels.put(channel.getId(), channel);
    }

    /**
     * Adds the channel list to the channels for the account.
     * @param channels The channels to add
     */
    public void add(Collection<AlertChannel> channels)
    {
        for(AlertChannel channel : channels)
            this.channels.put(channel.getId(), channel);
    }

    /**
     * Returns the channel for the given id.
     * @param id The id of the channel
     * @return The channel for the given id
     */
    public AlertChannel get(long id)
    {
        return this.channels.get(id);
    }

    /**
     * Returns the channels for the account.
     * @return The channels for the account
     */
    public Collection<AlertChannel> list()
    {
        return this.channels.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.channels.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.channels.clear();
    }
}