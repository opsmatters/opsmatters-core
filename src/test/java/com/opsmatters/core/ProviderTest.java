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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import org.junit.Test;
import junit.framework.Assert;
import com.opsmatters.core.provider.Provider;
import com.opsmatters.core.provider.ProviderFactory;
import com.opsmatters.core.provider.ProviderManager;
import com.opsmatters.core.provider.NewRelicCache;
import com.opsmatters.core.documents.OutputFileWriter;
import com.opsmatters.core.util.StringUtilities;
import com.opsmatters.newrelic.api.Constants;
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.alerts.conditions.AlertCondition;
import com.opsmatters.newrelic.api.model.insights.Dashboard;

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
        NewRelicCache cache = NewRelicCache.builder().apiKey(apiKey).alerts(true).apm(true).synthetics(true).insights(true).build();
        manager.sync(cache);

        logger.info("Check the cache: "+cache);
        Assert.assertTrue(manager.isInitialized());
        Assert.assertTrue(cache.getUpdatedAt() > 0L);
        Assert.assertTrue(cache.alertChannels().size() > 0);
        Assert.assertTrue(cache.alertPolicies().size() > 0);
        Assert.assertTrue(cache.applications().size() > 0);
        Assert.assertTrue(cache.monitors().size() > 0);
        Assert.assertTrue(cache.dashboards().size() > 0);

        // Write the alert channel reports
        List<String[]> lines = getChannelLines(cache);
        createReport("target", "channels.csv", null, lines);
        createReport("target", "channels.xls", "Channels", lines);
        createReport("target", "channels.xlsx", "Channels", lines);

        // Write the alert policy reports
        lines = getPolicyLines(cache);
        createReport("target", "policies.csv", null, lines);
        createReport("target", "policies.xls", "Policies", lines);
        createReport("target", "policies.xlsx", "Policies", lines);

        // Write the alert condition reports
        Collection<AlertPolicy> policies = cache.alertPolicies().list();
        AlertPolicy policy = policies.iterator().next();
        if(policy != null)
        {
            lines = getConditionLines(cache, policy);
            createReport("target", "conditions.csv", null, lines);
            createReport("target", "conditions.xls", "Conditions", lines);
            createReport("target", "conditions.xlsx", "Conditions", lines);
        }

        // Write the dashboard reports
        lines = getDashboardLines(cache);
        createReport("target", "dashboards.csv", null, lines);
        createReport("target", "dashboards.xls", "Dashboards", lines);
        createReport("target", "dashboards.xlsx", "Dashboards", lines);

        logger.info("Completed test: "+testName);
    }

    public List<String[]> getChannelLines(NewRelicCache cache)
    {
        List<String[]> lines = new ArrayList<String[]>();
        Collection<AlertChannel> channels = cache.alertChannels().list();
        lines.add(new String[] {"ID", "Name", "Type"} );
        for(AlertChannel channel : channels)
        {
            lines.add(new String[] {Long.toString(channel.getId()), channel.getName(), channel.getType()} );
        }
        return lines;
    }

    public List<String[]> getPolicyLines(NewRelicCache cache)
    {
        List<String[]> lines = new ArrayList<String[]>();
        Collection<AlertPolicy> policies = cache.alertPolicies().list();
        lines.add(new String[] {"ID", "Name", "Incident Preference"} );
        for(AlertPolicy policy : policies)
        {
            lines.add(new String[] {Long.toString(policy.getId()), policy.getName(), policy.getIncidentPreference()} );
        }
        return lines;
    }

    public List<String[]> getConditionLines(NewRelicCache cache, AlertPolicy policy)
    {
        List<String[]> lines = new ArrayList<String[]>();
        Collection<AlertCondition> conditions = cache.alertPolicies().alertConditions(policy.getId()).list();
        lines.add(new String[] {"ID", "Name", "Type", "Scope"} );
        for(AlertCondition condition : conditions)
        {
            lines.add(new String[] {Long.toString(condition.getId()), condition.getName(), condition.getType(), condition.getConditionScope()} );
        }
        return lines;
    }

    public List<String[]> getDashboardLines(NewRelicCache cache)
    {
        List<String[]> lines = new ArrayList<String[]>();
        Collection<Dashboard> dashboards = cache.dashboards().list();
        lines.add(new String[] {"ID", "Title", "Visibility", "Owner Email", "UI URL"} );
        for(Dashboard dashboard : dashboards)
        {
            lines.add(new String[] {Long.toString(dashboard.getId()), dashboard.getTitle(), dashboard.getVisibility(), dashboard.getOwnerEmail(), dashboard.getUiUrl()} );
        }
        return lines;
    }

    public void createReport(String directory, String filename, String sheetName, List<String[]> lines)
    {
        OutputStream os = null;
        try
        {
            // Write the contents of the stream
            os = new FileOutputStream(new File(directory, filename), false);
            OutputFileWriter writer = OutputFileWriter.builder()
                .name(filename)
                .worksheet(sheetName)
                .withOutputStream(os)
                .build();
            writer.write(lines);
        }
        catch(IOException e)
        {
            logger.severe(StringUtilities.serialize(e));
        }
        finally
        {
            try
            {
                // Close the output stream
                if(os != null)
                {
                    os.flush();
                    os.close();
                }
            }
            catch(IOException e)
            {
            }
        }
    }
}