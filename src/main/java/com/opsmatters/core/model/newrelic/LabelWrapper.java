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
import com.opsmatters.newrelic.api.model.labels.Label;

/**
 * Represents a New Relic label.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class LabelWrapper implements ResourceWrapper
{
    private Label label;

    /**
     * Constructor that takes a label.
     * @param label The label
     */
    public LabelWrapper(Label label)
    {
        this.label = label;
    }

    /**
     * Returns the key of the label.
     * @return The key of the label
     */
    public String getKey()
    {
        return label.getKey();
    }

    /**
     * Returns the name of the label.
     * @return The name of the label
     */
    public String getName()
    {
        return label.getName();
    }
   
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return label.toString();
    }
}