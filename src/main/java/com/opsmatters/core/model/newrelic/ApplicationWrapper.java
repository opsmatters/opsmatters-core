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
import com.opsmatters.newrelic.api.model.applications.Application;

/**
 * Represents a New Relic application.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ApplicationWrapper extends EntityWrapper
{
    private Application application;
    private Map<Long,ApplicationHostWrapper> applicationHosts = new LinkedHashMap<Long,ApplicationHostWrapper>();
    private Map<Long,KeyTransactionWrapper> keyTransactions = new LinkedHashMap<Long,KeyTransactionWrapper>();
    private Map<Long,DeploymentWrapper> deployments = new LinkedHashMap<Long,DeploymentWrapper>();
    private Map<String,LabelWrapper> labels = new LinkedHashMap<String,LabelWrapper>();

    /**
     * Constructor that takes an application.
     * @param application The application
     */
    public ApplicationWrapper(Application application)
    {
        super(application);
        this.application = application;
    }

    /**
     * Returns the application hosts for the application.
     * @return The application hosts for the application
     */
    public Map<Long,ApplicationHostWrapper> getApplicationHosts()
    {
        return applicationHosts;
    }

    /**
     * Adds the application host to the application hosts for the application.
     * @param applicationHost The application host to add
     */
    public void addApplicationHost(ApplicationHostWrapper applicationHost)
    {
        applicationHosts.put(applicationHost.getId(), applicationHost);
    }

    /**
     * Returns the application host for the given id.
     * @param applicationHostId The id of the application host
     * @return The application host for the given id
     */
    public ApplicationHostWrapper getApplicationHost(long applicationHostId)
    {
        return applicationHosts.get(applicationHostId);
    }

    /**
     * Returns the key transactions for the application.
     * @return The key transactions for the application
     */
    public Map<Long,KeyTransactionWrapper> getKeyTransactions()
    {
        return keyTransactions;
    }

    /**
     * Adds the key transaction to the key transactions for the application.
     * @param keyTransaction The key transaction to add
     */
    public void addKeyTransaction(KeyTransactionWrapper keyTransaction)
    {
        keyTransactions.put(keyTransaction.getId(), keyTransaction);
    }

    /**
     * Returns the key transaction for the given id.
     * @param entityId The id of the key transaction
     * @return The key transaction for the given id
     */
    public KeyTransactionWrapper getKeyTransaction(long entityId)
    {
        return keyTransactions.get(entityId);
    }

    /**
     * Returns the deployments for the application.
     * @return The deployments for the application
     */
    public Map<Long,DeploymentWrapper> getDeployments()
    {
        return deployments;
    }

    /**
     * Adds the deployment to the deployments for the application.
     * @param deployment The deployment to add
     */
    public void addDeployment(DeploymentWrapper deployment)
    {
        deployments.put(deployment.getId(), deployment);
    }

    /**
     * Returns the deployment for the given id.
     * @param deploymentId The id of the deployment
     * @return The deployment for the given id
     */
    public DeploymentWrapper getDeployment(long deploymentId)
    {
        return deployments.get(deploymentId);
    }

    /**
     * Returns the labels for the application.
     * @return The labels for the application
     */
    public Map<String,LabelWrapper> getLabels()
    {
        return labels;
    }

    /**
     * Adds the label to the labels for the application.
     * @param label The label to add
     */
    public void addLabel(LabelWrapper label)
    {
        labels.put(label.getKey(), label);
    }

    /**
     * Returns the label for the given key.
     * @param key The key of the label
     * @return The label for the given key
     */
    public LabelWrapper getLabel(String key)
    {
        return labels.get(key);
    }
}