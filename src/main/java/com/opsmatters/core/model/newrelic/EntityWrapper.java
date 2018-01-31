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

import com.opsmatters.newrelic.api.model.Entity;

/**
 * Represents a New Relic entity.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class EntityWrapper implements NewRelicWrapper
{
    private Entity entity;

    /**
     * Constructor that takes an entity.
     * @param entity The entity
     */
    public EntityWrapper(Entity entity)
    {
        this.entity = entity;
    }

    /**
     * Returns the id of the entity.
     * @return The id of the entity
     */
    public long getId()
    {
        return entity.getId();
    }

    /**
     * Returns the name of the entity.
     * @return The name of the entity
     */
    public String getName()
    {
        return entity.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return entity.toString();
    }
}