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
import com.opsmatters.newrelic.api.model.Entity;

/**
 * Represents the new relic entity cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class EntityCache extends ResourceCache<Entity>
{
    private Map<Long,Entity> entities = new LinkedHashMap<Long,Entity>();

    /**
     * Default constructor.
     */
    public EntityCache()
    {
        super("Entities");
    }

    /**
     * Adds the entity to the entities for the account.
     * @param entity The entity to add
     */
    public void add(Entity entity)
    {
        this.entities.put(entity.getId(), entity);
    }

    /**
     * Adds the entity list to the entities for the account.
     * @param entities The entities to add
     */
    public void add(Collection<Entity> entities)
    {
        for(Entity entity : entities)
            this.entities.put(entity.getId(), entity);
    }

    /**
     * Returns the entity for the given id.
     * @param id The id of the entity
     * @return The entity for the given id
     */
    public Entity get(long id)
    {
        return this.entities.get(id);
    }

    /**
     * Returns the entities for the account.
     * @return The entities for the account
     */
    public Collection<Entity> list()
    {
        return this.entities.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.entities.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.entities.clear();
    }
}