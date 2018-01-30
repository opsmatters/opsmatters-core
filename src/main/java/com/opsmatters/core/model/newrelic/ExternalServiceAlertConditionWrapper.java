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

import com.opsmatters.newrelic.api.model.alerts.conditions.ExternalServiceAlertCondition;

/**
 * Represents a New Relic external service alert condition.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ExternalServiceAlertConditionWrapper implements NewRelicWrapper
{
    private ExternalServiceAlertCondition condition;

    /**
     * Constructor that takes an alert condition.
     * @param condition The alert condition
     */
    public ExternalServiceAlertConditionWrapper(ExternalServiceAlertCondition condition)
    {
        this.condition = condition;
    }

    /**
     * Returns the id of the condition.
     * @return The id of the condition
     */
    public long getId()
    {
        return condition.getId();
    }

    /**
     * Returns the name of the condition.
     * @return The name of the condition
     */
    public String getName()
    {
        return condition.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return condition.toString();
    }

}