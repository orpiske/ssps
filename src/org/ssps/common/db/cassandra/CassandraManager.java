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
     */
    public void runGet(final String key, final Object object) {
	try {
	    ColumnFamilyResult<String, String> res = template.queryColumns(key);
	    
	    Collection<String> collection = res.getColumnNames();
	    
	    for (String s : collection) {
		
		
		//PropertyUtils.get
	    }
	} catch (HectorException e) {
	    // do something ...
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
