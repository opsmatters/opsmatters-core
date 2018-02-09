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

package com.opsmatters.core.provider;

import java.util.Collection;
import java.util.logging.Logger;
import com.opsmatters.newrelic.api.NewRelicApi;
import com.opsmatters.newrelic.api.NewRelicInfraApi;
import com.opsmatters.newrelic.api.NewRelicSyntheticsApi;
import com.opsmatters.newrelic.api.services.PluginComponentService;
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.applications.Application;
import com.opsmatters.newrelic.api.model.plugins.Plugin;
import com.opsmatters.newrelic.api.model.plugins.PluginComponent;
import com.opsmatters.newrelic.api.model.synthetics.Monitor;
import com.opsmatters.newrelic.api.model.labels.Label;

/**
 * Represents a manager of a New Relic configuration.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class NewRelicManager implements ProviderManager<NewRelicCache>
{
    private static final Logger logger = Logger.getLogger(NewRelicManager.class.getName());

    private NewRelicApi apiClient;
    private NewRelicInfraApi infraApiClient;
    private NewRelicSyntheticsApi syntheticsApiClient;
    private boolean initialized = false;

    /**
     * Default constructor.
     */
    public NewRelicManager()
    {
    }

    /**
     * Initialise the clients.
     * @param cache The provider cache
     */
    private void checkInitialize(NewRelicCache cache)
    {
        if(!initialized)
            initialize(cache);
    }

    /**
     * Called after setting configuration properties.
     * @param cache The provider cache
     */
    public void initialize(NewRelicCache cache)
    {
        String apiKey = cache.getApiKey();
        if(apiKey == null)
            throw new IllegalArgumentException("null API key");

        logger.info("Initialising the clients");

        initialized = false;
        if(cache.isAlertsEnabled())
        {
            apiClient = NewRelicApi.builder().apiKey(apiKey).build();
            infraApiClient = NewRelicInfraApi.builder().apiKey(apiKey).build();
            syntheticsApiClient = NewRelicSyntheticsApi.builder().apiKey(apiKey).build();
        }

        logger.info("Initialised the clients");

        initialized = true;
    }

    /**
     * Returns <CODE>true</CODE> if the clients have been initialized.
     * @return <CODE>true</CODE> if the clients have been initialized
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * Returns the REST API client.
     * @return the REST API client 
     */
    public NewRelicApi getApiClient()
    {
        return apiClient;
    }

    /**
     * Returns the Infrastructure API client.
     * @return the Infrastructure API client 
     */
    public NewRelicInfraApi getInfraApiClient()
    {
        return infraApiClient;
    }

    /**
     * Synchronises the cache.
     * @param cache The provider cache
     */
    public boolean sync(NewRelicCache cache)
    {
        if(cache == null)
            throw new IllegalArgumentException("null cache");

        checkInitialize(cache);
        boolean ret = isInitialized();
        if(!ret)
            throw new IllegalStateException("cache not initialized");

        clear(cache);

        if(ret)
            ret = syncApplications(cache);
        if(ret)
            ret = syncPlugins(cache);
        if(ret)
            ret = syncMonitors(cache);
        if(ret)
            ret = syncServers(cache);
        if(ret)
            ret = syncLabels(cache);
        if(ret)
            ret = syncAlerts(cache);
        if(ret)
            ret = syncDashboards(cache);

        return ret;
    }

    /**
     * Synchronise the alerts configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncAlerts(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the alert configuration using the REST API
        if(cache.isAlertsEnabled())
        {
            ret = false;

            // Get the alert policies
            logger.info("Getting the alert policies");
            Collection<AlertPolicy> policies = apiClient.alertPolicies().list();
            for(AlertPolicy policy : policies)
            {
                cache.alertPolicies().add(policy);

                // Add the alert conditions
                if(cache.isApmEnabled() || cache.isServersEnabled() || cache.isBrowserEnabled() || cache.isMobileEnabled())
                    cache.alertPolicies().alertConditions(policy.getId()).add(apiClient.alertConditions().list(policy.getId()));
                cache.alertPolicies().nrqlAlertConditions(policy.getId()).add(apiClient.nrqlAlertConditions().list(policy.getId()));
                if(cache.isApmEnabled() || cache.isMobileEnabled())
                    cache.alertPolicies().externalServiceAlertConditions(policy.getId()).add(apiClient.externalServiceAlertConditions().list(policy.getId()));
                if(cache.isSyntheticsEnabled())
                    cache.alertPolicies().syntheticsAlertConditions(policy.getId()).add(apiClient.syntheticsAlertConditions().list(policy.getId()));
                if(cache.isPluginsEnabled())
                    cache.alertPolicies().pluginsAlertConditions(policy.getId()).add(apiClient.pluginsAlertConditions().list(policy.getId()));
                if(cache.isInfrastructureEnabled())
                    cache.alertPolicies().infraAlertConditions(policy.getId()).add(infraApiClient.infraAlertConditions().list(policy.getId()));
            }

            // Get the alert channels
            logger.info("Getting the alert channels");
            Collection<AlertChannel> channels = apiClient.alertChannels().list();
            cache.alertChannels().set(channels);
            cache.alertPolicies().setAlertChannels(channels);
            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the application configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncApplications(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the application configuration using the REST API
        if(cache.isApmEnabled() || cache.isBrowserEnabled() || cache.isMobileEnabled())
        {
            ret = false;

            if(cache.isApmEnabled())
            {
                logger.info("Getting the applications");
                Collection<Application> applications = apiClient.applications().list();
                for(Application application : applications)
                {
                    cache.applications().add(application);

                    logger.fine("Getting the hosts for application: "+application.getId());
                    cache.applications().applicationHosts(application.getId()).add(apiClient.applicationHosts().list(application.getId()));

                    logger.fine("Getting the instances for application: "+application.getId());
                    cache.applications().applicationHosts(application.getId()).addApplicationInstances(apiClient.applicationInstances().list(application.getId()));

                    logger.fine("Getting the deployments for application: "+application.getId());
                    cache.applications().deployments(application.getId()).add(apiClient.deployments().list(application.getId()));
                }

                // Get the key transaction configuration using the REST API
                logger.info("Getting the key transactions");
                cache.applications().addKeyTransactions(apiClient.keyTransactions().list());
            }

            if(cache.isBrowserEnabled())
            {
                logger.info("Getting the browser applications");
                cache.browserApplications().add(apiClient.browserApplications().list());
            }

            if(cache.isBrowserEnabled())
            {
                logger.info("Getting the mobile applications");
                cache.mobileApplications().add(apiClient.mobileApplications().list());
            }

            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the Plugins configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncPlugins(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the Plugins configuration using the REST API
        if(cache.isPluginsEnabled())
        {
            ret = false;

            logger.info("Getting the plugins");
            Collection<Plugin> plugins = apiClient.plugins().list(true);
            for(Plugin plugin : plugins)
            {
                cache.plugins().add(plugin);

                logger.fine("Getting the components for plugin: "+plugin.getId());
                Collection<PluginComponent> components = apiClient.pluginComponents().list(PluginComponentService.filters().pluginId(plugin.getId()).build());
                cache.plugins().components(plugin.getId()).add(components);
            }

            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the Synthetics configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncMonitors(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the Synthetics configuration using the REST API
        if(cache.isSyntheticsEnabled())
        {
            ret = false;

            logger.info("Getting the monitors");
            cache.monitors().add(syntheticsApiClient.monitors().list());
            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the server configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncServers(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the server configuration using the REST API
        if(cache.isServersEnabled())
        {
            ret = false;

            logger.info("Getting the servers");
            cache.servers().add(apiClient.servers().list());
            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the label configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncLabels(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the label configuration using the REST API
        if(cache.isApmEnabled() || cache.isSyntheticsEnabled())
        {
            ret = false;

            logger.info("Getting the labels");
            Collection<Label> labels = apiClient.labels().list();
            for(Label label : labels)
            {
                cache.applications().addLabel(label);

                try
                {
                    // Also check to see if this label is associated with any monitors
                    Collection<Monitor> monitors = syntheticsApiClient.monitors().list(label);
                    for(Monitor monitor : monitors)
                       cache.monitors().labels(monitor.getId()).add(label);
                }
                catch(NullPointerException e)
                {
                    logger.severe("Unable to get monitor labels: "+e.getClass().getName()+": "+e.getMessage());
                }
            }

            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Synchronise the dashboard configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncDashboards(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the dashboard configuration using the REST API
        if(cache.isInsightsEnabled())
        {
            ret = false;

            logger.info("Getting the dashboards");
            cache.dashboards().set(apiClient.dashboards().list());
            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Clears the cache.
     */
    public void clear(NewRelicCache cache)
    {
        if(cache == null)
            throw new IllegalArgumentException("null cache");
        cache.applications().clear();
        cache.browserApplications().clear();
        cache.mobileApplications().clear();
        cache.plugins().clear();
        cache.monitors().clear();
        cache.servers().clear();
        cache.entities().clear();
        cache.alertPolicies().clear();
        cache.alertChannels().clear();
        cache.dashboards().clear();
    }
}