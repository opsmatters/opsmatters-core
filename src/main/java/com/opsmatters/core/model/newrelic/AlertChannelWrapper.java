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

import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;

/**
 * Represents a New Relic alert channel.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class AlertChannelWrapper implements ResourceIdWrapper
{
    private AlertChannel channel;

    /**
     * Constructor that takes an alert channel.
     * @param channel The alert channel
     */
    public AlertChannelWrapper(AlertChannel channel)
    {
        this.channel = channel;
    }

    /**
     * Returns the id of the channel.
     * @return The id of the channel
     */
    public long getId()
    {
        return channel.getId();
    }

    /**
     * Returns the name of the channel.
     * @return The name of the channel
     */
    public String getName()
    {
        return channel.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return channel.toString();
    }

}