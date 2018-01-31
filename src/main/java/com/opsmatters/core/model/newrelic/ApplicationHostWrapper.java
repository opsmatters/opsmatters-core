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
import com.opsmatters.newrelic.api.model.applications.ApplicationHost;

/**
 * Represents a New Relic application host.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationHostWrapper extends EntityWrapper
{
    private ApplicationHost applicationHost;
    private Map<Long,ApplicationInstanceWrapper> applicationInstances = new LinkedHashMap<Long,ApplicationInstanceWrapper>();

    /**
     * Constructor that takes an application host.
     * @param applicationHost The application host
     */
    public ApplicationHostWrapper(ApplicationHost applicationHost)
    {
        super(applicationHost);
        this.applicationHost = applicationHost;
    }

    /**
     * Returns the application instances for the host.
     * @return The application instances for the host
     */
    public Map<Long,ApplicationInstanceWrapper> getApplicationInstances()
    {
        return applicationInstances;
    }

    /**
     * Adds the application host to the application instances for the host.
     * @param applicationInstance The application instance to add
     */
    public void addApplicationInstance(ApplicationInstanceWrapper applicationInstance)
    {
        applicationInstances.put(applicationInstance.getId(), applicationInstance);
    }

    /**
     * Returns the application instance for the given id.
     * @param applicationInstanceId The id of the application instance
     * @return The application instance for the given id
     */
    public ApplicationInstanceWrapper getApplicationInstance(long applicationInstanceId)
    {
        return applicationInstances.get(applicationInstanceId);
    }
}