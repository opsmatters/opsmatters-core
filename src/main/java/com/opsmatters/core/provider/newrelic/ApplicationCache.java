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

package com.opsmatters.core.provider.newrelic;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Collection;
import java.util.logging.Logger;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.applications.Application;
import com.opsmatters.newrelic.api.model.transactions.KeyTransaction;
import com.opsmatters.newrelic.api.model.labels.Label;

/**
 * Represents the new relic application cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationCache extends ResourceCache<Application>
{
    private static final Logger logger = Logger.getLogger(ApplicationCache.class.getName());

    private Map<Long,Application> applications = new LinkedHashMap<Long,Application>();
    private Map<Long,ApplicationHostCache> applicationHosts = new LinkedHashMap<Long,ApplicationHostCache>();
    private Map<Long,KeyTransactionCache> keyTransactions = new LinkedHashMap<Long,KeyTransactionCache>();
    private Map<Long,DeploymentCache> deployments = new LinkedHashMap<Long,DeploymentCache>();
    private Map<Long,LabelCache> labels = new LinkedHashMap<Long,LabelCache>();

    /**
     * Default constructor.
     */
    public ApplicationCache()
    {
        super("Applications");
    }

    /**
     * Adds the application to the applications for the account.
     * @param application The application to add
     */
    public void add(Application application)
    {
        this.applications.put(application.getId(), application);
    }

    /**
     * Adds the application list to the applications for the account.
     * @param applications The applications to add
     */
    public void add(Collection<Application> applications)
    {
        for(Application application : applications)
            this.applications.put(application.getId(), application);
    }

    /**
     * Returns the application for the given id.
     * @param id The id of the application
     * @return The application for the given id
     */
    public Application get(long id)
    {
        return this.applications.get(id);
    }

    /**
     * Returns the applications for the account.
     * @return The applications for the account
     */
    public Collection<Application> list()
    {
        return this.applications.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.applications.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.applications.clear();
    }

    /**
     * Returns the cache of application hosts for the given application, creating one if it doesn't exist .
     * @param applicationId The id of the application for the cache of application hosts
     * @return The cache of application hosts for the given application
     */
    public ApplicationHostCache applicationHosts(long applicationId)
    {
        ApplicationHostCache cache = applicationHosts.get(applicationId);
        if(cache == null)
            applicationHosts.put(applicationId, cache = new ApplicationHostCache(applicationId));
        return cache;
    }

    /**
     * Returns the cache of key transactions for the given application, creating one if it doesn't exist .
     * @param applicationId The id of the application for the cache of key transactions
     * @return The cache of key transactions for the given application
     */
    public KeyTransactionCache keyTransactions(long applicationId)
    {
        KeyTransactionCache cache = keyTransactions.get(applicationId);
        if(cache == null)
            keyTransactions.put(applicationId, cache = new KeyTransactionCache(applicationId));
        return cache;
    }

    /**
     * Adds the key transactions to the applications for the account.
     * @param keyTransactions The key transactions to add
     */
    public void addKeyTransactions(Collection<KeyTransaction> keyTransactions)
    {
        for(KeyTransaction keyTransaction : keyTransactions)
        {
            // Add the transaction to any applications it is associated with
            long applicationId = keyTransaction.getLinks().getApplication();
            Application application = applications.get(applicationId);
            if(application != null)
                keyTransactions(applicationId).add(keyTransaction);
            else
                logger.severe(String.format("Unable to find application for key transaction '%s': %d", keyTransaction.getName(), applicationId));
        }
    }

    /**
     * Returns the cache of deployments for the given application, creating one if it doesn't exist .
     * @param applicationId The id of the application for the cache of deployments
     * @return The cache of deployments for the given application
     */
    public DeploymentCache deployments(long applicationId)
    {
        DeploymentCache cache = deployments.get(applicationId);
        if(cache == null)
            deployments.put(applicationId, cache = new DeploymentCache(applicationId));
        return cache;
    }

    /**
     * Returns the cache of labels for the given application, creating one if it doesn't exist .
     * @param applicationId The id of the application for the cache of labels
     * @return The cache of labels for the given application
     */
    public LabelCache labels(long applicationId)
    {
        LabelCache cache = labels.get(applicationId);
        if(cache == null)
            labels.put(applicationId, cache = new LabelCache(applicationId));
        return cache;
    }

    /**
     * Adds the label to the applications for the account.
     * @param label The label to add
     */
    public void addLabel(Label label)
    {
        // Add the label to any applications it is associated with
        List<Long> applicationIds = label.getLinks().getApplications();
        for(long applicationId : applicationIds)
        {
            Application application = applications.get(applicationId);
            if(application != null)
                labels(applicationId).add(label);
            else
                logger.severe(String.format("Unable to find application for label '%s': %d", label.getKey(), applicationId));
        }
    }
}