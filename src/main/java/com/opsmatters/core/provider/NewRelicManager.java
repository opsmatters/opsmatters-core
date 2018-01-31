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

import java.util.List;
import java.util.Collection;
import java.util.logging.Logger;
import com.opsmatters.newrelic.api.NewRelicApi;
import com.opsmatters.newrelic.api.NewRelicInfraApi;
import com.opsmatters.newrelic.api.NewRelicSyntheticsApi;
import com.opsmatters.newrelic.api.services.PluginComponentService;
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.alerts.conditions.MetricCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.AlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.NrqlAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.ExternalServiceAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.SyntheticsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.PluginsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.InfraAlertCondition;
import com.opsmatters.newrelic.api.model.Entity;
import com.opsmatters.newrelic.api.model.applications.Application;
import com.opsmatters.newrelic.api.model.applications.BrowserApplication;
import com.opsmatters.newrelic.api.model.applications.MobileApplication;
import com.opsmatters.newrelic.api.model.applications.ApplicationHost;
import com.opsmatters.newrelic.api.model.applications.ApplicationInstance;
import com.opsmatters.newrelic.api.model.transactions.KeyTransaction;
import com.opsmatters.newrelic.api.model.plugins.Plugin;
import com.opsmatters.newrelic.api.model.plugins.PluginComponent;
import com.opsmatters.newrelic.api.model.synthetics.Monitor;
import com.opsmatters.newrelic.api.model.servers.Server;
import com.opsmatters.newrelic.api.model.deployments.Deployment;
import com.opsmatters.newrelic.api.model.labels.Label;
import com.opsmatters.core.model.newrelic.AlertChannelWrapper;
import com.opsmatters.core.model.newrelic.AlertPolicyWrapper;
import com.opsmatters.core.model.newrelic.BaseConditionWrapper;
import com.opsmatters.core.model.newrelic.MetricConditionWrapper;
import com.opsmatters.core.model.newrelic.ApplicationWrapper;
import com.opsmatters.core.model.newrelic.ApplicationHostWrapper;
import com.opsmatters.core.model.newrelic.ApplicationInstanceWrapper;
import com.opsmatters.core.model.newrelic.KeyTransactionWrapper;
import com.opsmatters.core.model.newrelic.PluginWrapper;
import com.opsmatters.core.model.newrelic.PluginComponentWrapper;
import com.opsmatters.core.model.newrelic.EntityWrapper;
import com.opsmatters.core.model.newrelic.MonitorWrapper;
import com.opsmatters.core.model.newrelic.DeploymentWrapper;
import com.opsmatters.core.model.newrelic.LabelWrapper;

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
                AlertPolicyWrapper p = new AlertPolicyWrapper(policy);
                cache.addAlertPolicy(p);

                // Add the alert conditions
                if(cache.isApmEnabled() || cache.isServersEnabled() || cache.isBrowserEnabled() || cache.isMobileEnabled())
                    addAlertConditions(p, cache);
                addNrqlAlertConditions(p, cache);
                if(cache.isApmEnabled() || cache.isMobileEnabled())
                    addExternalServiceAlertConditions(p, cache);
                if(cache.isSyntheticsEnabled())
                    addSyntheticsAlertConditions(p, cache);
                if(cache.isPluginsEnabled())
                    addPluginsAlertConditions(p, cache);
                if(cache.isInfrastructureEnabled())
                    addInfraAlertConditions(p, cache);
            }

            // Get the alert channels
            logger.info("Getting the alert channels");
            Collection<AlertChannel> channels = apiClient.alertChannels().list();
            for(AlertChannel channel : channels)
            {
                AlertChannelWrapper c = new AlertChannelWrapper(channel);
                cache.addAlertChannel(c);

                // Add the channel to any policies it is associated with
                List<Long> policyIds = channel.getLinks().getPolicyIds();
                for(long policyId : policyIds)
                {
                    AlertPolicyWrapper policy = cache.getAlertPolicy(policyId);
                    if(policy != null)
                        policy.addChannel(c);
                    else
                        logger.severe(String.format("Unable to find policy for channel '%s': %d", channel.getName(), policyId));
                }
            }

            cache.setUpdatedAt();

            ret = true;
        }

        return ret;
    }

    /**
     * Adds the alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the alert conditions for policy: "+policy.getId());
        Collection<AlertCondition> conditions = apiClient.alertConditions().list(policy.getId());
        for(AlertCondition condition : conditions)
            policy.addCondition(getMetricConditionWrapper(condition, cache));
    }

    /**
     * Adds the NRQL alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addNrqlAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the NRQL alert conditions for policy: "+policy.getId());
        Collection<NrqlAlertCondition> conditions = apiClient.nrqlAlertConditions().list(policy.getId());
        for(NrqlAlertCondition condition : conditions)
            policy.addNrqlCondition(new BaseConditionWrapper(condition));
    }

    /**
     * Adds the external service alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addExternalServiceAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the external service alert conditions for policy: "+policy.getId());
        Collection<ExternalServiceAlertCondition> conditions = apiClient.externalServiceAlertConditions().list(policy.getId());
        for(ExternalServiceAlertCondition condition : conditions)
            policy.addExternalServiceCondition(getMetricConditionWrapper(condition, cache));
    }

    /**
     * Adds the Synthetics alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addSyntheticsAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the Synthetics alert conditions for policy: "+policy.getId());
        Collection<SyntheticsAlertCondition> conditions = apiClient.syntheticsAlertConditions().list(policy.getId());
        for(SyntheticsAlertCondition condition : conditions)
            policy.addSyntheticsCondition(new BaseConditionWrapper(condition));
    }

    /**
     * Adds the Plugins alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addPluginsAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the Plugins alert conditions for policy: "+policy.getId());
        Collection<PluginsAlertCondition> conditions = apiClient.pluginsAlertConditions().list(policy.getId());
        for(PluginsAlertCondition condition : conditions)
            policy.addPluginsCondition(getMetricConditionWrapper(condition, cache));
    }

    /**
     * Adds the Infrastructure alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     * @param cache The provider cache
     */
    private void addInfraAlertConditions(AlertPolicyWrapper policy, NewRelicCache cache)
    {
        logger.fine("Getting the Infrastructure alert conditions for policy: "+policy.getId());
        Collection<InfraAlertCondition> conditions = infraApiClient.infraAlertConditions().list(policy.getId());
        for(InfraAlertCondition condition : conditions)
            policy.addInfraCondition(new BaseConditionWrapper(condition));
    }

    /**
     * Creates a wrapper for the given alert condition, adding the associated entities.
     * @param cache The provider cache
     * @param condition The alert condition containing the entity ids
     * @return The alert condition wrapper
     */
    private MetricConditionWrapper getMetricConditionWrapper(MetricCondition condition, NewRelicCache cache)
    {
        MetricConditionWrapper ret = new MetricConditionWrapper(condition);
        List<Long> entities = condition.getEntities();
        for(Long entityId : entities)
        {
            EntityWrapper entity = cache.getEntity(entityId);
            if(entity == null)
                logger.warning("Unable to find entity for condition: "+entityId);
            ret.addEntity(entity);
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
                    ApplicationWrapper a = new ApplicationWrapper(application);
                    cache.addApplication(a);

                    logger.fine("Getting the hosts for application: "+application.getId());
                    Collection<ApplicationHost> applicationHosts = apiClient.applicationHosts().list(application.getId());
                    for(ApplicationHost applicationHost : applicationHosts)
                        a.addApplicationHost(new ApplicationHostWrapper(applicationHost));

                    logger.fine("Getting the instances for application: "+application.getId());
                    Collection<ApplicationInstance> applicationInstances = apiClient.applicationInstances().list(application.getId());
                    for(ApplicationInstance applicationInstance : applicationInstances)
                    {
                        ApplicationInstanceWrapper ai = new ApplicationInstanceWrapper(applicationInstance);
                        long applicationHostId = applicationInstance.getLinks().getApplicationHost();
                        ApplicationHostWrapper ah = a.getApplicationHost(applicationHostId);
                        if(ah != null)
                            ah.addApplicationInstance(ai);
                        else
                            logger.severe(String.format("Unable to find application instance for host '%s': %d", applicationInstance.getName(), applicationHostId));
                    }

                    logger.fine("Getting the deployments for application: "+application.getId());
                    Collection<Deployment> deployments = apiClient.deployments().list(application.getId());
                    for(Deployment deployment : deployments)
                        a.addDeployment(new DeploymentWrapper(deployment));
                }

                // Get the key transaction configuration using the REST API
                logger.info("Getting the key transactions");
                Collection<KeyTransaction> keyTransactions = apiClient.keyTransactions().list();
                for(KeyTransaction keyTransaction : keyTransactions)
                {
                    KeyTransactionWrapper kt = new KeyTransactionWrapper(keyTransaction);
                    long applicationId = keyTransaction.getLinks().getApplication();
                    ApplicationWrapper a = cache.getApplication(applicationId);
                    if(a != null)
                        a.addKeyTransaction(kt);
                    else
                        logger.severe(String.format("Unable to find application for key transaction '%s': %d", keyTransaction.getName(), applicationId));
                }
            }

            if(cache.isBrowserEnabled())
            {
                logger.info("Getting the browser applications");
                Collection<BrowserApplication> browserApplications = apiClient.browserApplications().list();
                for(BrowserApplication application : browserApplications)
                    cache.addBrowserApplication(new EntityWrapper(application));
            }

            if(cache.isBrowserEnabled())
            {
                logger.info("Getting the mobile applications");
                Collection<MobileApplication> mobileApplications = apiClient.mobileApplications().list();
                for(MobileApplication application : mobileApplications)
                    cache.addMobileApplication(new EntityWrapper(application));
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
                PluginWrapper p = new PluginWrapper(plugin);
                cache.addPlugin(p);

                logger.fine("Getting the components for plugin: "+plugin.getId());
                Collection<PluginComponent> components = apiClient.pluginComponents().list(PluginComponentService.filters().pluginId(plugin.getId()).build());
                for(PluginComponent component : components)
                    p.addPluginComponent(new PluginComponentWrapper(component));
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
            Collection<Monitor> monitors = syntheticsApiClient.monitors().list();
            for(Monitor monitor : monitors)
                cache.addMonitor(new MonitorWrapper(monitor));

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
            Collection<Server> servers = apiClient.servers().list();
            for(Server server : servers)
                cache.addServer(new EntityWrapper(server));

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
                LabelWrapper l = new LabelWrapper(label);
                List<Long> applicationIds = label.getLinks().getApplications();
                for(long applicationId : applicationIds)
                {
                    ApplicationWrapper application = cache.getApplication(applicationId);
                    if(application != null)
                        application.addLabel(l);
                    else
                        logger.severe(String.format("Unable to find application for label '%s': %d", label.getName(), applicationId));
                }

                // Also check to see if this label is associated with any monitors
                Collection<Monitor> monitors = syntheticsApiClient.monitors().list(label);
                for(Monitor monitor : monitors)
                {
                    MonitorWrapper m = cache.getMonitor(monitor.getId());
                    if(m != null)
                        m.addLabel(l);
                    else
                        logger.severe(String.format("Unable to find monitor for label '%s': %d", label.getName(), monitor.getId()));
                }
            }

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
        cache.clearApplications();
        cache.clearPlugins();
        cache.clearMonitors();
        cache.clearServers();
        cache.clearEntities();
        cache.clearAlerts();
    }
}