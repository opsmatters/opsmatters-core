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
import com.opsmatters.newrelic.api.model.deployments.Deployment;

/**
 * Represents the new relic deployment cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class DeploymentCache extends ResourceCache<Deployment>
{
    private long applicationId;
    private Map<Long,Deployment> deployments = new LinkedHashMap<Long,Deployment>();

    /**
     * Constructor that takes an application id.
     * @param applicationId The application id for the cache
     */
    public DeploymentCache(long applicationId)
    {
        super("Deployments");
        this.applicationId = applicationId;
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
     * Adds the deployment to the deployments for the account.
     * @param deployment The deployment to add
     */
    public void add(Deployment deployment)
    {
        this.deployments.put(deployment.getId(), deployment);
    }

    /**
     * Adds the deployment list to the deployments for the account.
     * @param deployments The deployments to add
     */
    public void add(Collection<Deployment> deployments)
    {
        for(Deployment deployment : deployments)
            this.deployments.put(deployment.getId(), deployment);
    }

    /**
     * Returns the deployment for the given id.
     * @param id The id of the deployment
     * @return The deployment for the given id
     */
    public Deployment get(long id)
    {
        return this.deployments.get(id);
    }

    /**
     * Returns the deployments for the account.
     * @return The deployments for the account
     */
    public Collection<Deployment> list()
    {
        return this.deployments.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.deployments.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.deployments.clear();
    }
}