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

/**
 * Represents a cache containing a New Relic configuration.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class NewRelicCache extends ProviderCache
{
    private String apiKey;
    private boolean alerts = false;
    private boolean apm = false;
    private boolean browser = false;
    private boolean synthetics = false;
    private boolean mobile = false;
    private boolean plugins = false;
    private boolean insights = false;
    private boolean infrastructure = false;
    private boolean servers = false;
    private Map<Long,AlertChannelWrapper> channels = new LinkedHashMap<Long,AlertChannelWrapper>();
    private Map<Long,AlertPolicyWrapper> policies = new LinkedHashMap<Long,AlertPolicyWrapper>();
    private Map<Long,ApplicationWrapper> applications = new LinkedHashMap<Long,ApplicationWrapper>();

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
     * @param alerts <CODE>true</CODE> if the alert configuration should be included
     */
    public void setAlertsEnabled(boolean alerts)
    {
        this.alerts = alerts;
    }

    /**
     * Returns <CODE>true</CODE> if the alert configuration should be included.
     * @return <CODE>true</CODE> if the alert configuration should be included
     */
    public boolean isAlertsEnabled()
    {
        return alerts;
    }

    /**
     * Set to <CODE>true</CODE> if the APM configuration should be included.
     * @param apm <CODE>true</CODE> if the APM configuration should be included
     */
    public void setApmEnabled(boolean apm)
    {
        this.apm = apm;
    }

    /**
     * Returns <CODE>true</CODE> if the APM configuration should be included.
     * @return <CODE>true</CODE> if the APM configuration should be included
     */
    public boolean isApmEnabled()
    {
        return apm;
    }

    /**
     * Set to <CODE>true</CODE> if the Browser configuration should be included.
     * @param browser <CODE>true</CODE> if the Browser configuration should be included
     */
    public void setBrowserEnabled(boolean browser)
    {
        this.browser = browser;
    }

    /**
     * Returns <CODE>true</CODE> if the Browser configuration should be included.
     * @return <CODE>true</CODE> if the Browser configuration should be included
     */
    public boolean isBrowserEnabled()
    {
        return browser;
    }

    /**
     * Set to <CODE>true</CODE> if the Synthetics configuration should be included.
     * @param synthetics <CODE>true</CODE> if the Synthetics configuration should be included
     */
    public void setSyntheticsEnabled(boolean synthetics)
    {
        this.synthetics = synthetics;
    }

    /**
     * Returns <CODE>true</CODE> if the Synthetics configuration should be included.
     * @return <CODE>true</CODE> if the Synthetics configuration should be included
     */
    public boolean isSyntheticsEnabled()
    {
        return synthetics;
    }

    /**
     * Set to <CODE>true</CODE> if the Mobile configuration should be included.
     * @param mobile <CODE>true</CODE> if the Mobile configuration should be included
     */
    public void setMobileEnabled(boolean mobile)
    {
        this.mobile = mobile;
    }

    /**
     * Returns <CODE>true</CODE> if the Mobile configuration should be included.
     * @return <CODE>true</CODE> if the Mobile configuration should be included
     */
    public boolean isMobileEnabled()
    {
        return mobile;
    }

    /**
     * Set to <CODE>true</CODE> if the Plugins configuration should be included.
     * @param plugins <CODE>true</CODE> if the Plugins configuration should be included
     */
    public void setPluginsEnabled(boolean plugins)
    {
        this.plugins = plugins;
    }

    /**
     * Returns <CODE>true</CODE> if the Plugins configuration should be included.
     * @return <CODE>true</CODE> if the Plugins configuration should be included
     */
    public boolean isPluginsEnabled()
    {
        return plugins;
    }

    /**
     * Set to <CODE>true</CODE> if the Insights configuration should be included.
     * @param insights <CODE>true</CODE> if the Insights configuration should be included
     */
    public void setInsightsEnabled(boolean insights)
    {
        this.insights = insights;
    }

    /**
     * Returns <CODE>true</CODE> if the Insights configuration should be included.
     * @return <CODE>true</CODE> if the Insights configuration should be included
     */
    public boolean isInsightsEnabled()
    {
        return insights;
    }

    /**
     * Set to <CODE>true</CODE> if the Infrastructure configuration should be included.
     * @param infrastructure <CODE>true</CODE> if the Infrastructure configuration should be included
     */
    public void setInfrastructureEnabled(boolean infrastructure)
    {
        this.infrastructure = infrastructure;
    }

    /**
     * Returns <CODE>true</CODE> if the Infrastructure configuration should be included.
     * @return <CODE>true</CODE> if the Infrastructure configuration should be included
     */
    public boolean isInfrastructureEnabled()
    {
        return infrastructure;
    }

    /**
     * Set to <CODE>true</CODE> if the Servers configuration should be included.
     * @param servers <CODE>true</CODE> if the Servers configuration should be included
     */
    public void setServersEnabled(boolean servers)
    {
        this.servers = servers;
    }

    /**
     * Returns <CODE>true</CODE> if the Servers configuration should be included.
     * @return <CODE>true</CODE> if the Servers configuration should be included
     */
    public boolean isServersEnabled()
    {
        return servers;
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
     * Returns the alert policies for the account.
     * @return The alert policies for the account
     */
    public Map<Long,AlertPolicyWrapper> getAlertPolicies()
    {
        return policies;
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
     * Clear the alerts configuration.
     */
    public void clearAlerts()
    {
        channels.clear();
        policies.clear();
    }

    /**
     * Clear the Application configuration.
     */
    public void clearApplications()
    {
        applications.clear();
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