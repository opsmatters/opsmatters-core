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

import java.util.List;
import java.util.ArrayList;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;

/**
 * Represents a New Relic alert policy.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class AlertPolicyWrapper implements ResourceIdWrapper
{
    private AlertPolicy policy;
    private List<AlertChannelWrapper> channels = new ArrayList<AlertChannelWrapper>();
    private List<MetricConditionWrapper> conditions = new ArrayList<MetricConditionWrapper>();
    private List<BaseConditionWrapper> nrqlConditions = new ArrayList<BaseConditionWrapper>();
    private List<MetricConditionWrapper> externalServiceConditions = new ArrayList<MetricConditionWrapper>();
    private List<BaseConditionWrapper> syntheticsConditions = new ArrayList<BaseConditionWrapper>();
    private List<MetricConditionWrapper> pluginsConditions = new ArrayList<MetricConditionWrapper>();
    private List<BaseConditionWrapper> infraConditions = new ArrayList<BaseConditionWrapper>();

    /**
     * Constructor that takes an alert policy.
     * @param policy The alert policy
     */
    public AlertPolicyWrapper(AlertPolicy policy)
    {
        this.policy = policy;
    }

    /**
     * Returns the id of the policy.
     * @return The id of the policy
     */
    public long getId()
    {
        return policy.getId();
    }

    /**
     * Returns the name of the policy.
     * @return The name of the policy
     */
    public String getName()
    {
        return policy.getName();
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return policy.toString();
    }

    /**
     * Adds the given alert channel.
     * @param channel The alert channel to add
     */
    public void addChannel(AlertChannelWrapper channel)
    {
        channels.add(channel);
    }

    /**
     * Return the number of alert channels for the policy.
     * @return The number of alert channels
     */
    public int numChannels()
    {
        return channels.size();
    }

    /**
     * Adds the given alert condition.
     * @param condition The alert condition to add
     */
    public void addCondition(MetricConditionWrapper condition)
    {
        conditions.add(condition);
    }

    /**
     * Return the number of alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numConditions()
    {
        return conditions.size();
    }

    /**
     * Adds the given NRQL alert condition.
     * @param condition The alert condition to add
     */
    public void addNrqlCondition(BaseConditionWrapper condition)
    {
        nrqlConditions.add(condition);
    }

    /**
     * Return the number of NRQL alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numNrqlConditions()
    {
        return nrqlConditions.size();
    }

    /**
     * Adds the given external service alert condition.
     * @param condition The alert condition to add
     */
    public void addExternalServiceCondition(MetricConditionWrapper condition)
    {
        externalServiceConditions.add(condition);
    }

    /**
     * Return the number of external service alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numExternalServiceConditions()
    {
        return externalServiceConditions.size();
    }

    /**
     * Adds the given Synthetics alert condition.
     * @param condition The alert condition to add
     */
    public void addSyntheticsCondition(BaseConditionWrapper condition)
    {
        syntheticsConditions.add(condition);
    }

    /**
     * Return the number of Synthetics alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numSyntheticsConditions()
    {
        return syntheticsConditions.size();
    }

    /**
     * Adds the given Plugins alert condition.
     * @param condition The alert condition to add
     */
    public void addPluginsCondition(MetricConditionWrapper condition)
    {
        pluginsConditions.add(condition);
    }

    /**
     * Return the number of Plugins alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numPluginsConditions()
    {
        return pluginsConditions.size();
    }

    /**
     * Adds the given Infrastructure alert condition.
     * @param condition The alert condition to add
     */
    public void addInfraCondition(BaseConditionWrapper condition)
    {
        infraConditions.add(condition);
    }

    /**
     * Return the number of Infrastructure alert conditions for the policy.
     * @return The number of alert conditions
     */
    public int numInfraConditions()
    {
        return infraConditions.size();
    }
}