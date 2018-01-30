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
import com.opsmatters.newrelic.api.model.alerts.channels.AlertChannel;
import com.opsmatters.newrelic.api.model.alerts.policies.AlertPolicy;
import com.opsmatters.newrelic.api.model.alerts.conditions.AlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.NrqlAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.ExternalServiceAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.SyntheticsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.PluginsAlertCondition;
import com.opsmatters.newrelic.api.model.alerts.conditions.InfraAlertCondition;
import com.opsmatters.newrelic.api.model.applications.Application;
import com.opsmatters.core.model.newrelic.AlertChannelWrapper;
import com.opsmatters.core.model.newrelic.AlertPolicyWrapper;
import com.opsmatters.core.model.newrelic.AlertConditionWrapper;
import com.opsmatters.core.model.newrelic.NrqlAlertConditionWrapper;
import com.opsmatters.core.model.newrelic.ExternalServiceAlertConditionWrapper;
import com.opsmatters.core.model.newrelic.SyntheticsAlertConditionWrapper;
import com.opsmatters.core.model.newrelic.PluginsAlertConditionWrapper;
import com.opsmatters.core.model.newrelic.InfraAlertConditionWrapper;
import com.opsmatters.core.model.newrelic.ApplicationWrapper;

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
        if(ret)
            ret = syncAlerts(cache);
        if(ret)
            ret = syncApplications(cache);
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

            cache.clearAlerts();

            // Get the alert policies
            logger.info("Getting the alert policies");
            Collection<AlertPolicy> policies = apiClient.alertPolicies().list();
            for(AlertPolicy policy : policies)
            {
                AlertPolicyWrapper p = new AlertPolicyWrapper(policy);
                cache.getAlertPolicies().put(policy.getId(), p);

                // Add the alert conditions
                if(cache.isApmEnabled() || cache.isServersEnabled() || cache.isBrowserEnabled() || cache.isMobileEnabled())
                    addAlertConditions(p);
                addNrqlAlertConditions(p);
                if(cache.isApmEnabled() || cache.isMobileEnabled())
                    addExternalServiceAlertConditions(p);
                if(cache.isSyntheticsEnabled())
                    addSyntheticsAlertConditions(p);
                if(cache.isPluginsEnabled())
                    addPluginsAlertConditions(p);
                if(cache.isInfrastructureEnabled())
                    addInfraAlertConditions(p);
            }

            // Get the alert channels
            logger.info("Getting the alert channels");
            Collection<AlertChannel> channels = apiClient.alertChannels().list();
            for(AlertChannel channel : channels)
            {
                AlertChannelWrapper c = new AlertChannelWrapper(channel);
                cache.getAlertChannels().put(channel.getId(), c);

                // Add the channel to any policies it is associated with
                List<Long> policyIds = channel.getLinks().getPolicyIds();
                for(long policyId : policyIds)
                {
                    AlertPolicyWrapper policy = cache.getAlertPolicies().get(policyId);
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
     */
    private void addAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the alert conditions for policy: "+policy.getId());
        Collection<AlertCondition> conditions = apiClient.alertConditions().list(policy.getId());
        for(AlertCondition condition : conditions)
            policy.addCondition(new AlertConditionWrapper(condition));
    }

    /**
     * Adds the NRQL alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     */
    private void addNrqlAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the NRQL alert conditions for policy: "+policy.getId());
        Collection<NrqlAlertCondition> conditions = apiClient.nrqlAlertConditions().list(policy.getId());
        for(NrqlAlertCondition condition : conditions)
            policy.addNrqlCondition(new NrqlAlertConditionWrapper(condition));
    }

    /**
     * Adds the external service alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     */
    private void addExternalServiceAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the external service alert conditions for policy: "+policy.getId());
        Collection<ExternalServiceAlertCondition> conditions = apiClient.externalServiceAlertConditions().list(policy.getId());
        for(ExternalServiceAlertCondition condition : conditions)
            policy.addExternalServiceCondition(new ExternalServiceAlertConditionWrapper(condition));
    }

    /**
     * Adds the Synthetics alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     */
    private void addSyntheticsAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the Synthetics alert conditions for policy: "+policy.getId());
        Collection<SyntheticsAlertCondition> conditions = apiClient.syntheticsAlertConditions().list(policy.getId());
        for(SyntheticsAlertCondition condition : conditions)
            policy.addSyntheticsCondition(new SyntheticsAlertConditionWrapper(condition));
    }

    /**
     * Adds the Plugins alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     */
    private void addPluginsAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the Plugins alert conditions for policy: "+policy.getId());
        Collection<PluginsAlertCondition> conditions = apiClient.pluginsAlertConditions().list(policy.getId());
        for(PluginsAlertCondition condition : conditions)
            policy.addPluginsCondition(new PluginsAlertConditionWrapper(condition));
    }

    /**
     * Adds the Infrastructure alert conditions to the given policy.
     * @param policy the policy to add the conditions to
     */
    private void addInfraAlertConditions(AlertPolicyWrapper policy)
    {
        logger.fine("Getting the Infrastructure alert conditions for policy: "+policy.getId());
        Collection<InfraAlertCondition> conditions = infraApiClient.infraAlertConditions().list(policy.getId());
        for(InfraAlertCondition condition : conditions)
            policy.addInfraCondition(new InfraAlertConditionWrapper(condition));
    }

    /**
     * Synchronise the APM configuration with the cache.
     * @param cache The provider cache
     * @return <CODE>true</CODE> if the operation was successful
     */
    public boolean syncApplications(NewRelicCache cache)
    {
        boolean ret = true;

        if(apiClient == null)
            throw new IllegalArgumentException("null API client");

        // Get the application configuration using the REST API
        if(cache.isApmEnabled())
        {
            ret = false;

            cache.clearApplications();

            // Get the alert policies
            logger.info("Getting the applications");
            Collection<Application> applications = apiClient.applications().list();
            for(Application application : applications)
            {
                ApplicationWrapper a = new ApplicationWrapper(application);
                cache.getApplications().put(application.getId(), a);
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
        cache.clearAlerts();
        cache.clearApplications();
    }
}