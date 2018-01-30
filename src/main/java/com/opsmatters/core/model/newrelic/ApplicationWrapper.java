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

import com.opsmatters.newrelic.api.model.applications.Application;

/**
 * Represents a New Relic application.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationWrapper implements NewRelicWrapper
{
    private Application application;

    /**
     * Constructor that takes an alert application.
     * @param application The alert application
     */
    public ApplicationWrapper(Application application)
    {
        this.application = application;
    }

    /**
     * Returns the id of the application.
     * @return The id of the application
     */
    public long getId()
    {
        return application.getId();
    }

    /**
     * Returns the name of the application.
     * @return The name of the application
     */
    public String getName()
    {
        return application.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return application.toString();
    }

}