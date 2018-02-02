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
import com.opsmatters.newrelic.api.model.alerts.conditions.SyntheticsAlertCondition;

/**
 * Represents the new relic alert condition cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class SyntheticsAlertConditionCache extends ResourceCache<SyntheticsAlertCondition>
{
    private long policyId;
    private Map<Long,SyntheticsAlertCondition> conditions = new LinkedHashMap<Long,SyntheticsAlertCondition>();

    /**
     * Constructor that takes a policy id.
     * @param policyId The policy id for the cache
     */
    public SyntheticsAlertConditionCache(long policyId)
    {
        super("Synthetics Alert Conditions");
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
     * Adds the condition to the conditions for the account.
     * @param condition The condition to add
     */
    public void add(SyntheticsAlertCondition condition)
    {
        this.conditions.put(condition.getId(), condition);
    }

    /**
     * Adds the condition list to the conditions for the account.
     * @param conditions The conditions to add
     */
    public void add(Collection<SyntheticsAlertCondition> conditions)
    {
        for(SyntheticsAlertCondition condition : conditions)
            this.conditions.put(condition.getId(), condition);
    }

    /**
     * Returns the condition for the given id.
     * @param id The id of the condition
     * @return The condition for the given id
     */
    public SyntheticsAlertCondition get(long id)
    {
        return this.conditions.get(id);
    }

    /**
     * Returns the conditions for the account.
     * @return The conditions for the account
     */
    public Collection<SyntheticsAlertCondition> list()
    {
        return this.conditions.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.conditions.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.conditions.clear();
    }
}