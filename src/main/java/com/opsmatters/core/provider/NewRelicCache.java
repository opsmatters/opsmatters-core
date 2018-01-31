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

import java.util.Map;
import java.util.LinkedHashMap;
import org.apache.commons.lang3.StringUtils;
import com.opsmatters.core.model.newrelic.AlertChannelWrapper;
import com.opsmatters.core.model.newrelic.AlertPolicyWrapper;
import com.opsmatters.core.model.newrelic.ApplicationWrapper;
import com.opsmatters.core.model.newrelic.EntityWrapper;
import com.opsmatters.core.model.newrelic.MonitorWrapper;

/**
 * Represents a cache containing a New Relic configuration.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class NewRelicCache extends ProviderCache
{
    private String apiKey;
    private boolean alertsEnabled = false;
    private boolean apmEnabled = false;
    private boolean browserEnabled = false;
    private boolean syntheticsEnabled = false;
    private boolean mobileEnabled = false;
    private boolean pluginsEnabled = false;
    private boolean insightsEnabled = false;
    private boolean infrastructureEnabled = false;
    private boolean serversEnabled = false;
    private Map<Long,AlertChannelWrapper> channels = new LinkedHashMap<Long,AlertChannelWrapper>();
    private Map<Long,AlertPolicyWrapper> policies = new LinkedHashMap<Long,AlertPolicyWrapper>();
    private Map<Long,ApplicationWrapper> applications = new LinkedHashMap<Long,ApplicationWrapper>();
    private Map<Long,EntityWrapper> browserApplications = new LinkedHashMap<Long,EntityWrapper>();
    private Map<Long,EntityWrapper> mobileApplications = new LinkedHashMap<Long,EntityWrapper>();
    private Map<Long,EntityWrapper> servers = new LinkedHashMap<Long,EntityWrapper>();
    private Map<Long,EntityWrapper> plugins = new LinkedHashMap<Long,EntityWrapper>();
    private Map<String,MonitorWrapper> monitors = new LinkedHashMap<String,MonitorWrapper>();
    private Map<Long,EntityWrapper> entities = new LinkedHashMap<Long,EntityWrapper>();

    /**
     * Constructor that takes a provider.
     */
    public NewRelicCache()
    {
        super(Provider.NEW_RELIC);
    }

    /**
     * Sets the API key used to authenticate the clients.
     * @param apiKey the API key used to authenticate the clients
     */
    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    /**
     * Returns the API key used to authenticate the clients.
     * @return the API key used to authenticate the clients
     */
    public String getApiKey()
    {
        return apiKey;
    }

    /**
     * Returns the API key used to authenticate the clients (masked except for the last n digits).
     * @param count The number of right-hand digits to retain after masking
     * @return the API key used to authenticate the clients
     */
    public String getMaskedApiKey(int count)
    {
        if(apiKey != null)
            return StringUtils.overlay(apiKey, StringUtils.repeat("X", apiKey.length()-count), 0, apiKey.length()-count);
        return null;
    }

    /**
     * Set to <CODE>true</CODE> if the alert configuration should be included.
     * @param alertsEnabled <CODE>true</CODE> if the alert configuration should be included
     */
    public void setAlertsEnabled(boolean alertsEnabled)
    {
        this.alertsEnabled = alertsEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the alert configuration should be included.
     * @return <CODE>true</CODE> if the alert configuration should be included
     */
    public boolean isAlertsEnabled()
    {
        return alertsEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the APM configuration should be included.
     * @param apmEnabled <CODE>true</CODE> if the APM configuration should be included
     */
    public void setApmEnabled(boolean apmEnabled)
    {
        this.apmEnabled = apmEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the APM configuration should be included.
     * @return <CODE>true</CODE> if the APM configuration should be included
     */
    public boolean isApmEnabled()
    {
        return apmEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Browser configuration should be included.
     * @param browserEnabled <CODE>true</CODE> if the Browser configuration should be included
     */
    public void setBrowserEnabled(boolean browserEnabled)
    {
        this.browserEnabled = browserEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Browser configuration should be included.
     * @return <CODE>true</CODE> if the Browser configuration should be included
     */
    public boolean isBrowserEnabled()
    {
        return browserEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Synthetics configuration should be included.
     * @param syntheticsEnabled <CODE>true</CODE> if the Synthetics configuration should be included
     */
    public void setSyntheticsEnabled(boolean syntheticsEnabled)
    {
        this.syntheticsEnabled = syntheticsEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Synthetics configuration should be included.
     * @return <CODE>true</CODE> if the Synthetics configuration should be included
     */
    public boolean isSyntheticsEnabled()
    {
        return syntheticsEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Mobile configuration should be included.
     * @param mobileEnabled <CODE>true</CODE> if the Mobile configuration should be included
     */
    public void setMobileEnabled(boolean mobileEnabled)
    {
        this.mobileEnabled = mobileEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Mobile configuration should be included.
     * @return <CODE>true</CODE> if the Mobile configuration should be included
     */
    public boolean isMobileEnabled()
    {
        return mobileEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Plugins configuration should be included.
     * @param pluginsEnabled <CODE>true</CODE> if the Plugins configuration should be included
     */
    public void setPluginsEnabled(boolean pluginsEnabled)
    {
        this.pluginsEnabled = pluginsEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Plugins configuration should be included.
     * @return <CODE>true</CODE> if the Plugins configuration should be included
     */
    public boolean isPluginsEnabled()
    {
        return pluginsEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Insights configuration should be included.
     * @param insightsEnabled <CODE>true</CODE> if the Insights configuration should be included
     */
    public void setInsightsEnabled(boolean insightsEnabled)
    {
        this.insightsEnabled = insightsEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Insights configuration should be included.
     * @return <CODE>true</CODE> if the Insights configuration should be included
     */
    public boolean isInsightsEnabled()
    {
        return insightsEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Infrastructure configuration should be included.
     * @param infrastructureEnabled <CODE>true</CODE> if the Infrastructure configuration should be included
     */
    public void setInfrastructureEnabled(boolean infrastructureEnabled)
    {
        this.infrastructureEnabled = infrastructureEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Infrastructure configuration should be included.
     * @return <CODE>true</CODE> if the Infrastructure configuration should be included
     */
    public boolean isInfrastructureEnabled()
    {
        return infrastructureEnabled;
    }

    /**
     * Set to <CODE>true</CODE> if the Servers configuration should be included.
     * @param serversEnabled <CODE>true</CODE> if the Servers configuration should be included
     */
    public void setServersEnabled(boolean serversEnabled)
    {
        this.serversEnabled = serversEnabled;
    }

    /**
     * Returns <CODE>true</CODE> if the Servers configuration should be included.
     * @return <CODE>true</CODE> if the Servers configuration should be included
     */
    public boolean isServersEnabled()
    {
        return serversEnabled;
    }
   
    /**
     * Returns the alert channels for the account.
     * @return The alert channels for the account
     */
    public Map<Long,AlertChannelWrapper> getAlertChannels()
    {
        return channels;
    }

    /**
     * Adds the alert channel to the alert channels for the account.
     * @param channel The alert channel to add
     */
    public void addAlertChannel(AlertChannelWrapper channel)
    {
        channels.put(channel.getId(), channel);
    }

    /**
     * Returns the alert channel for the given id.
     * @param channelId The id of the alert channel
     * @return The alert channel for the given id
     */
    public AlertChannelWrapper getAlertChannel(long channelId)
    {
        return channels.get(channelId);
    }

    /**
     * Returns the alert policies for the account.
     * @return The alert policies for the account
     */
    public Map<Long,AlertPolicyWrapper> getAlertPolicies()
    {
        return policies;
    }

    /**
     * Adds the alert policy to the alert policies for the account.
     * @param policy The alert policy to add
     */
    public void addAlertPolicy(AlertPolicyWrapper policy)
    {
        policies.put(policy.getId(), policy);
    }

    /**
     * Returns the alert policy for the given id.
     * @param policyId The id of the alert policy
     * @return The alert policy for the given id
     */
    public AlertPolicyWrapper getAlertPolicy(long policyId)
    {
        return policies.get(policyId);
    }

    /**
     * Returns the applications for the account.
     * @return The applications for the account
     */
    public Map<Long,ApplicationWrapper> getApplications()
    {
        return applications;
    }

    /**
     * Adds the application to the applications for the account.
     * @param application The application to add
     */
    public void addApplication(ApplicationWrapper application)
    {
        applications.put(application.getId(), application);
        entities.put(application.getId(), application);
    }

    /**
     * Returns the application for the given id.
     * @param entityId The id of the application
     * @return The application for the given id
     */
    public ApplicationWrapper getApplication(long entityId)
    {
        return applications.get(entityId);
    }

    /**
     * Returns the browser applications for the account.
     * @return The browser applications for the account
     */
    public Map<Long,EntityWrapper> getBrowserApplications()
    {
        return browserApplications;
    }

    /**
     * Adds the browser application to the browser applications for the account.
     * @param browserApplication The browser application to add
     */
    public void addBrowserApplication(EntityWrapper browserApplication)
    {
        browserApplications.put(browserApplication.getId(), browserApplication);
        entities.put(browserApplication.getId(), browserApplication);
    }

    /**
     * Returns the browser application for the given id.
     * @param entityId The id of the browser application
     * @return The browser application for the given id
     */
    public EntityWrapper getBrowserApplication(long entityId)
    {
        return browserApplications.get(entityId);
    }

    /**
     * Returns the mobile applications for the account.
     * @return The mobile applications for the account
     */
    public Map<Long,EntityWrapper> getMobileApplications()
    {
        return mobileApplications;
    }

    /**
     * Adds the mobile application to the mobile applications for the account.
     * @param mobileApplication The mobile application to add
     */
    public void addMobileApplication(EntityWrapper mobileApplication)
    {
        mobileApplications.put(mobileApplication.getId(), mobileApplication);
        entities.put(mobileApplication.getId(), mobileApplication);
    }

    /**
     * Returns the mobile application for the given id.
     * @param entityId The id of the mobile application
     * @return The mobile application for the given id
     */
    public EntityWrapper getMobileApplication(long entityId)
    {
        return mobileApplications.get(entityId);
    }

    /**
     * Returns the plugins for the account.
     * @return The plugins for the account
     */
    public Map<Long,EntityWrapper> getPlugins()
    {
        return plugins;
    }

    /**
     * Adds the plugin to the plugins for the account.
     * @param plugin The plugin to add
     */
    public void addPlugin(EntityWrapper plugin)
    {
        plugins.put(plugin.getId(), plugin);
        entities.put(plugin.getId(), plugin);
    }

    /**
     * Returns the plugin for the given id.
     * @param entityId The id of the plugin
     * @return The plugin for the given id
     */
    public EntityWrapper getPlugin(long entityId)
    {
        return plugins.get(entityId);
    }

    /**
     * Returns the monitors for the account.
     * @return The monitors for the account
     */
    public Map<String,MonitorWrapper> getMonitors()
    {
        return monitors;
    }

    /**
     * Adds the monitor to the monitors for the account.
     * @param monitor The monitor to add
     */
    public void addMonitor(MonitorWrapper monitor)
    {
        monitors.put(monitor.getId(), monitor);
    }

    /**
     * Returns the monitor for the given id.
     * @param monitorId The id of the monitor
     * @return The monitor for the given id
     */
    public MonitorWrapper getMonitor(String monitorId)
    {
        return monitors.get(monitorId);
    }

    /**
     * Returns the servers for the account.
     * @return The servers for the account
     */
    public Map<Long,EntityWrapper> getServers()
    {
        return servers;
    }

    /**
     * Adds the server to the servers for the account.
     * @param server The server to add
     */
    public void addServer(EntityWrapper server)
    {
        servers.put(server.getId(), server);
        entities.put(server.getId(), server);
    }

    /**
     * Returns the server for the given id.
     * @param entityId The id of the server
     * @return The server for the given id
     */
    public EntityWrapper getServer(long entityId)
    {
        return servers.get(entityId);
    }

    /**
     * Returns the entities for the account.
     * @return The entities for the account
     */
    public Map<Long,EntityWrapper> getEntities()
    {
        return entities;
    }

    /**
     * Returns the entity for the given id.
     * @param entityId The id of the entity
     * @return The entity for the given id
     */
    public EntityWrapper getEntity(long entityId)
    {
        return entities.get(entityId);
    }

    /**
     * Clear the alerts configuration.
     */
    public void clearAlerts()
    {
        channels.clear();
        policies.clear();
    }

    /**
     * Clear the applications.
     */
    public void clearApplications()
    {
        applications.clear();
        browserApplications.clear();
        mobileApplications.clear();
    }

    /**
     * Clear the plugins.
     */
    public void clearPlugins()
    {
        plugins.clear();
    }

    /**
     * Clear the monitors.
     */
    public void clearMonitors()
    {
        monitors.clear();
    }

    /**
     * Clear the servers.
     */
    public void clearServers()
    {
        servers.clear();
    }

    /**
     * Clear the entities.
     */
    public void clearEntities()
    {
        entities.clear();
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return "NewRelicCache ["+super.toString()
            +", apiKey="+getMaskedApiKey(6)
            +", channels="+channels.size()
            +", policies="+policies.size()
            +", applications="+applications.size()
            +", browserApplications="+browserApplications.size()
            +", mobileApplications="+mobileApplications.size()
            +", servers="+servers.size()
            +", plugins="+plugins.size()
            +", monitors="+monitors.size()
            +", entities="+entities.size()
            +"]";
    }

    /**
     * Returns a builder for the provider cache.
     * @return The builder instance.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to make provider cache construction easier.
     */
    public static class Builder
    {
        private NewRelicCache cache = new NewRelicCache();

        /**
         * Sets the API key used to authenticate the connection.
         * @param key The API key
         * @return This object
         */
        public Builder apiKey(String key)
        {
            cache.setApiKey(key);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the alert configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the alert configuration should be included in the cache
         * @return This object
         */
        public Builder alerts(boolean include)
        {
            cache.setAlertsEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the APM configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the APM configuration should be included in the cache
         * @return This object
         */
        public Builder apm(boolean include)
        {
            cache.setApmEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Browser configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Browser configuration should be included in the cache
         * @return This object
         */
        public Builder browser(boolean include)
        {
            cache.setBrowserEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Synthetics configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Synthetics configuration should be included in the cache
         * @return This object
         */
        public Builder synthetics(boolean include)
        {
            cache.setSyntheticsEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Mobile configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Mobile configuration should be included in the cache
         * @return This object
         */
        public Builder mobile(boolean include)
        {
            cache.setMobileEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Plugins configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Plugins configuration should be included in the cache
         * @return This object
         */
        public Builder plugins(boolean include)
        {
            cache.setPluginsEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Insights configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Insights configuration should be included in the cache
         * @return This object
         */
        public Builder insights(boolean include)
        {
            cache.setInsightsEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Infrastructure configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Infrastructure configuration should be included in the cache
         * @return This object
         */
        public Builder infrastructure(boolean include)
        {
            cache.setInfrastructureEnabled(include);
            return this;
        }

        /**
         * Sets to <CODE>true</CODE> if the Servers configuration should be included in the cache.
         * @param include <CODE>true</CODE> if the Servers configuration should be included in the cache
         * @return This object
         */
        public Builder servers(boolean include)
        {
            cache.setServersEnabled(include);
            return this;
        }

        /**
         * Returns the configured provider cache instance
         * @return The provider cache instance
         */
        public NewRelicCache build()
        {
            return cache;
        }
    }
}