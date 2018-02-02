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
import com.opsmatters.newrelic.api.model.servers.Server;

/**
 * Represents the new relic server cache.  
 * 
 * @author Gerald Curley (opsmatters)
 */
public class ServerCache extends ResourceCache<Server>
{
    private Map<Long,Server> servers = new LinkedHashMap<Long,Server>();

    /**
     * Default constructor.
     */
    public ServerCache()
    {
        super("Servers");
    }

    /**
     * Adds the server to the servers for the account.
     * @param server The server to add
     */
    public void add(Server server)
    {
        this.servers.put(server.getId(), server);
    }

    /**
     * Adds the server list to the servers for the account.
     * @param servers The servers to add
     */
    public void add(Collection<Server> servers)
    {
        for(Server server : servers)
            this.servers.put(server.getId(), server);
    }

    /**
     * Returns the server for the given id.
     * @param id The id of the server
     * @return The server for the given id
     */
    public Server get(long id)
    {
        return this.servers.get(id);
    }

    /**
     * Returns the servers for the account.
     * @return The servers for the account
     */
    public Collection<Server> list()
    {
        return this.servers.values();
    }

    /**
     * Returns the size of the cache.
     */
    public int size()
    {
        return this.servers.size();
    }

    /**
     * Clears the cache.
     */
    public void clear()
    {
        this.servers.clear();
    }
}