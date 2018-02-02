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
import java.util.logging.Logger;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;
import com.opsmatters.newrelic.api.model.alerts.conditions.AlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.NrqlAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.ExternalServiceAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.SyntheticsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.PluginsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.InfraAlertCondition;

/**
 * Represents the new relic alert policy cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class AlertPolicyCache extends ResourceCache<AlertPolicy>
{
    private static final Logger logger = Logger.getLogger(AlertPolicyCache.class.getName());

    private Map<Long,AlertPolicy> policies = new LinkedHashMap<Long,AlertPolicy>();
    private Map<Long,AlertChannelCache> channels = new LinkedHashMap<Long,AlertChannelCache>();
    private Map<Long,AlertConditionCache> conditions = new LinkedHashMap<Long,AlertConditionCache>();
    private Map<Long,NrqlAlertConditionCache> nrqlConditions = new LinkedHashMap<Long,NrqlAlertConditionCache>();
    private Map<Long,ExternalServiceAlertConditionCache> externalServiceConditions = new LinkedHashMap<Long,ExternalServiceAlertConditionCache>();
    private Map<Long,SyntheticsAlertConditionCache> syntheticsConditions = new LinkedHashMap<Long,SyntheticsAlertConditionCache>();
    private Map<Long,PluginsAlertConditionCache> pluginsConditions = new LinkedHashMap<Long,PluginsAlertConditionCache>();
    private Map<Long,InfraAlertConditionCache> infraConditions = new LinkedHashMap<Long,InfraAlertConditionCache>();

    /**
     * Default constructor.
     */
    public AlertPolicyCache()
    {
        super("Alert Policies");
    }

    /**
     * Adds the policy to the policies for the account.
     * @param policy The policy to add
     */
    public void add(AlertPolicy policy)
    {
        this.policies.put(policy.getId(), policy);
    }

    /**
     * Adds the policy list to the policies for the account.
     * @param policies The policies to add
     */
    public void add(Collection<AlertPolicy> policies)
    {
        for(AlertPolicy policy : policies)
            this.policies.put(policy.getId(), policy);
    }

    /**
     * Returns the policy for the given id.
     * @param id The id of the policy
     * @return The policy for the given id
     */
    public AlertPolicy get(long id)
    {
        return this.policies.get(id);
    }

    /**
     * Returns the policies for the account.
     * @return The policies for the account
     */
    public Collection<AlertPolicy> list()
    {
        return this.policies.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.policies.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.policies.clear();
    }

    /**
     * Returns the cache of alert channels for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of alert channels
     * @return The cache of alert channels for the given policy
     */
    public AlertChannelCache alertChannels(long policyId)
    {
        AlertChannelCache cache = channels.get(policyId);
        if(cache == null)
            channels.put(policyId, cache = new AlertChannelCache(policyId));
        return cache;
    }

    /**
     * Sets the channels on the policies for the account.
     * @param channels The channels to set
     */
    public void setAlertChannels(Collection<AlertChannel> channels)
    {
        for(AlertChannel channel : channels)
        {
            // Add the channel to any policies it is associated with
            List<Long> policyIds = channel.getLinks().getPolicyIds();
            for(long policyId : policyIds)
            {
                AlertPolicy policy = policies.get(policyId);
                if(policy != null)
                    alertChannels(policyId).add(channel);
                else
                    logger.severe(String.format("Unable to find policy for channel '%s': %d", channel.getName(), policyId));
            }
        }
    }

    /**
     * Returns the cache of alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of alert conditions
     * @return The cache of alert conditions for the given policy
     */
    public AlertConditionCache alertConditions(long policyId)
    {
        AlertConditionCache cache = conditions.get(policyId);
        if(cache == null)
            conditions.put(policyId, cache = new AlertConditionCache(policyId));
        return cache;
    }

    /**
     * Returns the cache of NRQL alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of NRQL alert conditions
     * @return The cache of NRQL alert conditions for the given policy
     */
    public NrqlAlertConditionCache nrqlAlertConditions(long policyId)
    {
        NrqlAlertConditionCache cache = nrqlConditions.get(policyId);
        if(cache == null)
            nrqlConditions.put(policyId, cache = new NrqlAlertConditionCache(policyId));
        return cache;
    }

    /**
     * Returns the cache of external service alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of external service alert conditions
     * @return The cache of external service alert conditions for the given policy
     */
    public ExternalServiceAlertConditionCache externalServiceAlertConditions(long policyId)
    {
        ExternalServiceAlertConditionCache cache = externalServiceConditions.get(policyId);
        if(cache == null)
            externalServiceConditions.put(policyId, cache = new ExternalServiceAlertConditionCache(policyId));
        return cache;
    }

    /**
     * Returns the cache of Synthetics alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of Synthetics alert conditions
     * @return The cache of Synthetics alert conditions for the given policy
     */
    public SyntheticsAlertConditionCache syntheticsAlertConditions(long policyId)
    {
        SyntheticsAlertConditionCache cache = syntheticsConditions.get(policyId);
        if(cache == null)
            syntheticsConditions.put(policyId, cache = new SyntheticsAlertConditionCache(policyId));
        return cache;
    }

    /**
     * Returns the cache of Plugins alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of Plugins alert conditions
     * @return The cache of Plugins alert conditions for the given policy
     */
    public PluginsAlertConditionCache pluginsAlertConditions(long policyId)
    {
        PluginsAlertConditionCache cache = pluginsConditions.get(policyId);
        if(cache == null)
            pluginsConditions.put(policyId, cache = new PluginsAlertConditionCache(policyId));
        return cache;
    }

    /**
     * Returns the cache of Infrastructure alert conditions for the given policy, creating one if it doesn't exist .
     * @param policyId The id of the policy for the cache of Infrastructure alert conditions
     * @return The cache of Infrastructure alert conditions for the given policy
     */
    public InfraAlertConditionCache infraAlertConditions(long policyId)
    {
        InfraAlertConditionCache cache = infraConditions.get(policyId);
        if(cache == null)
            infraConditions.put(policyId, cache = new InfraAlertConditionCache(policyId));
        return cache;
    }
}