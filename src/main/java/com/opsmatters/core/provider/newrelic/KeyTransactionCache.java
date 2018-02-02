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
import java.util.Collection;
import com.opsmatters.core.provider.ResourceCache;
import com.opsmatters.newrelic.api.model.transactions.KeyTransaction;

/**
 * Represents the new relic key transaction cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class KeyTransactionCache extends ResourceCache<KeyTransaction>
{
    private long applicationId;
    private Map<Long,KeyTransaction> keyTransactions = new LinkedHashMap<Long,KeyTransaction>();

    /**
     * Constructor that takes an application id.
     * @param applicationId The application id for the cache
     */
    public KeyTransactionCache(long applicationId)
    {
        super("Key Transactions");
        this.applicationId = applicationId;
    }

    /**
     * Returns the application id for the cache.
     * @return The application id for the cache
     */
    public long getApplicationId()
    {
        return applicationId;
    }

    /**
     * Adds the key transaction to the key transactions for the account.
     * @param keyTransaction The key transaction to add
     */
    public void add(KeyTransaction keyTransaction)
    {
        this.keyTransactions.put(keyTransaction.getId(), keyTransaction);
    }

    /**
     * Adds the key transaction list to the key transactions for the account.
     * @param keyTransactions The key transactions to add
     */
    public void add(Collection<KeyTransaction> keyTransactions)
    {
        for(KeyTransaction keyTransaction : keyTransactions)
            this.keyTransactions.put(keyTransaction.getId(), keyTransaction);
    }

    /**
     * Returns the key transaction for the given id.
     * @param id The id of the key transaction
     * @return The key transaction for the given id
     */
    public KeyTransaction get(long id)
    {
        return this.keyTransactions.get(id);
    }

    /**
     * Returns the key transactions for the account.
     * @return The key transactions for the account
     */
    public Collection<KeyTransaction> list()
    {
        return this.keyTransactions.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.keyTransactions.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.keyTransactions.clear();
    }
}