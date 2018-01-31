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
import com.opsmatters.newrelic.api.model.alerts.conditions.MetricCondition;

/**
 * Represents a New Relic metric alert condition.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class MetricConditionWrapper extends BaseConditionWrapper
{
    private MetricCondition condition;

    private List<EntityWrapper> entities = new ArrayList<EntityWrapper>();

    /**
     * Constructor that takes an alert condition.
     * @param condition The alert condition
     */
    public MetricConditionWrapper(MetricCondition condition)
    {
        super(condition);
        this.condition = condition;
    }

    /**
     * Adds the given entity.
     * @param entity The entity to add
     */
    public void addEntity(EntityWrapper entity)
    {
        entities.add(entity);
    }

    /**
     * Return the number of entities for the condition.
     * @return The number of entities
     */
    public int numEntities()
    {
        return entities.size();
    }
}