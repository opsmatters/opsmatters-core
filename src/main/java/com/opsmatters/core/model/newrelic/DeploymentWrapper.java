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

import java.util.Map;
import java.util.LinkedHashMap;
import com.opsmatters.newrelic.api.model.deployments.Deployment;

/**
 * Represents a New Relic deployment.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class DeploymentWrapper implements ResourceIdWrapper
{
    private Deployment deployment;

    /**
     * Constructor that takes a deployment.
     * @param deployment The deployment
     */
    public DeploymentWrapper(Deployment deployment)
    {
        this.deployment = deployment;
    }

    /**
     * Returns the id of the deployment.
     * @return The id of the deployment
     */
    public long getId()
    {
        return deployment.getId();
    }

    /**
     * Returns the name of the deployment.
     * @return The name of the deployment
     */
    public String getName()
    {
        return deployment.getRevision();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return deployment.toString();
    }
}