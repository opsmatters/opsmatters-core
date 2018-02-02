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
import com.opsmatters.core.provider.newrelic.AlertChannelCache;
import com.opsmatters.core.provider.newrelic.AlertPolicyCache;
import com.opsmatters.core.provider.newrelic.ApplicationCache;
import com.opsmatters.core.provider.newrelic.BrowserApplicationCache;
import com.opsmatters.core.provider.newrelic.MobileApplicationCache;
import com.opsmatters.core.provider.newrelic.ServerCache;
import com.opsmatters.core.provider.newrelic.PluginCache;
import com.opsmatters.core.provider.newrelic.MonitorCache;
import com.opsmatters.core.provider.newrelic.EntityCache;
import com.opsmatters.core.provider.newrelic.DashboardCache;

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
    private AlertChannelCache alertChannels = new AlertChannelCache();
    private AlertPolicyCache alertPolicies = new AlertPolicyCache();
    private ApplicationCache applications = new ApplicationCache();
    private BrowserApplicationCache browserApplications = new BrowserApplicationCache();
    private MobileApplicationCache mobileApplications = new MobileApplicationCache();
    private ServerCache servers = new ServerCache();
    private PluginCache plugins = new PluginCache();
    private MonitorCache monitors = new MonitorCache();
    private EntityCache entities = new EntityCache();
    private DashboardCache dashboards = new DashboardCache();

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
     * Returns the alert channel cache.
     * @return The alert channel cache
     */
    public AlertChannelCache alertChannels()
    {
        return alertChannels;
    }

    /**
     * Returns the alert policy cache.
     * @return The alert policy cache
     */
    public AlertPolicyCache alertPolicies()
    {
        return alertPolicies;
    }

    /**
     * Returns the application cache.
     * @return The application cache
     */
    public ApplicationCache applications()
    {
        return applications;
    }

    /**
     * Returns the browser application cache.
     * @return The browser application cache
     */
    public BrowserApplicationCache browserApplications()
    {
        return browserApplications;
    }

    /**
     * Returns the mobile application cache.
     * @return The mobile application cache
     */
    public MobileApplicationCache mobileApplications()
    {
        return mobileApplications;
    }

    /**
     * Returns the server cache.
     * @return The server cache
     */
    public ServerCache servers()
    {
        return servers;
    }

    /**
     * Returns the plugin cache.
     * @return The plugin cache
     */
    public PluginCache plugins()
    {
        return plugins;
    }

    /**
     * Returns the monitor cache.
     * @return The monitor cache
     */
    public MonitorCache monitors()
    {
        return monitors;
    }

    /**
     * Returns the dashboard cache.
     * @return The dashboard cache
     */
    public DashboardCache dashboards()
    {
        return dashboards;
    }

    /**
     * Returns the entity cache.
     * @return The entity cache
     */
    public EntityCache entities()
    {
        return entities;
    }

    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString()
    {
        return "NewRelicCache ["+super.toString()
            +", apiKey="+getMaskedApiKey(6)
            +", channels="+alertChannels.size()
            +", policies="+alertPolicies.size()
            +", applications="+applications.size()
            +", browserApplications="+browserApplications.size()
            +", mobileApplications="+mobileApplications.size()
            +", servers="+servers.size()
            +", plugins="+plugins.size()
            +", monitors="+monitors.size()
            +", entities="+entities.size()
            +", dashboards="+dashboards.size()
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