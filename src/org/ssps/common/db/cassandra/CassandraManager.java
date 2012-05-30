/**
  Copyright 2012 Otavio Rodolfo Piske

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package org.ssps.common.db.cassandra;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.ssps.common.db.DatabaseManager;
import org.ssps.common.db.exceptions.DatabaseException;
import org.ssps.common.discovery.Discovery;
import org.ssps.common.discovery.exceptions.InvalidBeanType;
import org.ssps.common.configuration.ConfigurationWrapper;

public class CassandraManager implements DatabaseManager {
    private static final Logger logger = Logger
	    .getLogger(CassandraManager.class);
    private static final PropertiesConfiguration config = ConfigurationWrapper
	    .getConfig();

    private Cluster cluster;
    private Keyspace keyspace;
    private ColumnFamilyTemplate<String, String> template;

    public CassandraManager() {

    }

    public void connect() {
	final String clusterName = config.getString("cassandra.cluster.name");
	final String hostName = config.getString("cassandra.cluster.hostname");
	final String port = config.getString("cassandra.rpc.port");

	cluster = HFactory.getOrCreateCluster(clusterName, hostName + ":"
		+ port);

	keyspace = HFactory.createKeyspace("SSPS", cluster);

	template = new ThriftColumnFamilyTemplate<String, String>(keyspace,
		"deliverables", StringSerializer.get(), StringSerializer.get());

	logger.info("Successfully connected to " + clusterName + "/SSPS");
    }

    public void disconnect() {

    }

    /**
     * Inserts data into a single column
     * @param key The key
     * @param cf The column family
     * @param column The column name
     * @param data The data
     */
    public void runInsert(final String key, final String cf, 
	    final String column, final String data) 
    {
	Mutator<String> mutator = HFactory.createMutator(keyspace, 
		StringSerializer.get());
	
	mutator.insert(key, cf, HFactory.createStringColumn(column, data));
    }
    
    
    /**
     * Updates data into a single column
     * @param key The key
     * @param cf The column family
     * @param column The column name
     * @param data The data
     */
    public void runUpdate(final String key, final String column, 
	    final String data) {
	// <String, String> correspond to key and Column name.
	ColumnFamilyUpdater<String, String> updater = 
		template.createUpdater(key);
	
	updater.setString(column, data);

	try {
	    template.update(updater);
	} catch (HectorException e) {
	    // do something ...
	}
    }
    
    
   
    
    /**
     * Gets data from the database using 'key'
     * @param key The key
     * @param cf The column family
     * @param column The column name
     * @param data The data
     * @throws DatabaseException 
     */
    public void runGet(final String key, final Object object) throws DatabaseException {
	try {
	    ColumnFamilyResult<String, String> res = template.queryColumns(key);
	    
	    Collection<String> collection = res.getColumnNames();
	    
	    for (String column : collection) {
		String data = res.getString(column);
		String propertyName = Discovery.getNameWithoutUnderscore(column);
	
		try {
		    PropertyUtils.setProperty(object, propertyName, data);
		} catch (IllegalAccessException e) {
		    throw new DatabaseException(
			    "Unauthorized to access bean method", e);
		} catch (InvocationTargetException e) {
		    throw new DatabaseException(
			    "Unable to access bean method", e);
		} catch (NoSuchMethodException e) {
		    throw new DatabaseException(
			    "The bean method does not exist", e);
		}
		
		
	    }
	} catch (HectorException e) {
	    throw new DatabaseException("Unable to read from the database", e);
	}
    }
    
    
    public void runDelete(final String key, final String column) {
	try {
	    template.deleteColumn(key, column);
	} catch (HectorException e) {
	    // do something
	}
    }
}
