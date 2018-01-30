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

package com.opsmatters.core;

import java.util.logging.Logger;
import org.junit.Test;
import junit.framework.Assert;
import com.opsmatters.core.provider.Provider;
import com.opsmatters.core.provider.ProviderFactory;
import com.opsmatters.core.provider.ProviderManager;
import com.opsmatters.core.provider.NewRelicCache;
import com.opsmatters.newrelic.api.Constants;

/**
 * The set of tests used for provider services.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ProviderTest
{
    private static final Logger logger = Logger.getLogger(ProviderTest.class.getName());

    // Get the properties
    private String apiKey = System.getProperty(Constants.API_KEY_PROPERTY);

    @Test
    public void testNewRelicCache()
    {
        String testName = "NewRelicCacheTest";
        logger.info("Starting test: "+testName);

        // Initialise the cache
        logger.info("Initialise the cache");
        ProviderManager manager = ProviderFactory.getManager(Provider.NEW_RELIC);
        NewRelicCache cache = NewRelicCache.builder().apiKey(apiKey).alerts(true).apm(true).build();
        manager.sync(cache);

        logger.info("Check the cache: "+cache);
        Assert.assertTrue(manager.isInitialized());
        Assert.assertTrue(cache.getUpdatedAt() > 0L);
        Assert.assertTrue(cache.getAlertChannels().size() > 0);
        Assert.assertTrue(cache.getAlertPolicies().size() > 0);
        Assert.assertTrue(cache.getApplications().size() > 0);

        logger.info("Completed test: "+testName);
    }
}