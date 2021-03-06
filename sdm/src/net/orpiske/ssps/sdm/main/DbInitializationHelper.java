/**
 Copyright 2013 Otavio Rodolfo Piske

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
package net.orpiske.ssps.sdm.main;

import net.orpiske.ssps.common.configuration.ConfigurationWrapper;
import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.db.version.DbVersionDao;
import net.orpiske.ssps.common.db.version.DbVersionDto;
import net.orpiske.ssps.common.dependencies.cache.DependencyCacheDao;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.common.utils.Utils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/**
 * Helper class to setup the internal database
 */
public class DbInitializationHelper {
	private static final Logger logger = Logger.getLogger(DbInitializationHelper.class);
	private static final PropertiesConfiguration config = ConfigurationWrapper.getConfig();
	private DerbyDatabaseManager databaseManager = null;
	
	public DbInitializationHelper() {
		
	}

	
	private void initializeSoftwareInventory() throws SQLException, DatabaseInitializationException {
		SoftwareInventoryDao inventory = new SoftwareInventoryDao(databaseManager);

		try {
			inventory.getCount();
		}
		catch (SQLException e) {
			String err = e.getMessage();
			
			if (StringUtils.containsIgnoreCase(err, "does not exist")) {
				inventory.createTable();
				logger.debug("Software inventory table created successfully");
			}
			else {
				throw e;
			}
		} 
	}


	private void initializeDbVersion() throws SQLException, DatabaseInitializationException {
		DbVersionDao dao = new DbVersionDao(databaseManager);
		
		try { 
			DbVersionDto dto = dao.get();
			
			if (dto == null) {
				logger.debug("Creating database version record");
				dto = new DbVersionDto();
				
				dto.setCreationDate(new Date());
				dto.setConversionDate(new Date());
				dto.setVersion("0.3.x");
				
				dao.insert(dto);
			}
		}
		catch (SQLException e) {
			String err = e.getMessage();
			
			if (StringUtils.containsIgnoreCase(err, "does not exist")) {
				dao.createTable();
				logger.debug("Database version table created successfully");
			}
			else {
				throw e;
			}
		}
	}


	private void initializePackageCache() throws SQLException, DatabaseInitializationException {
		PackageCacheDao dao = new PackageCacheDao(databaseManager);

		try {
			dao.getCount();
		}
		catch (SQLException e) {
			String err = e.getMessage();
			
			if (StringUtils.containsIgnoreCase(err, "does not exist")) {
				dao.createTable();
				logger.debug("Package cache table created successfully");
			}
			else {
				throw e;
			}
		}
	}


	private void initializeDependencyCache() throws SQLException, DatabaseInitializationException {
		DependencyCacheDao dao = new DependencyCacheDao(databaseManager);

		try {
			dao.getCount();
		}
		catch (SQLException e) {
			String err = e.getMessage();

			if (StringUtils.containsIgnoreCase(err, "does not exist")) {
				dao.createTable();
				logger.debug("Dependency cache table created successfully");
			}
			else {
				throw e;
			}
		}
	}
	
	
	private void initializeTables() throws SQLException, DatabaseInitializationException{
		logger.debug("Initializing software inventory");
		initializeSoftwareInventory();

		logger.debug("Initializing db version");
		initializeDbVersion();

		logger.debug("Initializing package cache");
		initializePackageCache();

		logger.debug("Initializing dependency cache");
		initializeDependencyCache();
		
	}
	
	private void getLock() throws Exception {
		File lockFile = new File(Utils.getSdmDirectoryPathFile(), "sdm.lock");
		
		try {
			logger.trace("Trying to obtain a runtime lock");
			boolean created;
			
			do {
				if (lockFile.exists()) {
					Thread.sleep(100);	
				}
				created = lockFile.createNewFile();
			} while (!created);

			logger.trace("Runtime lock obtained successfully");
		}
		finally {
			lockFile.deleteOnExit(); 
		}
	}
	
	public void initDatabase() {
		Properties props = System.getProperties();
		props.setProperty("derby.system.home", Utils.getSdmDirectoryPath());
		

		try {
			File dbDir = new File(Utils.getSdmDirectoryPath() + File.separator
					+ "registry");

			boolean volatileStorage = config.getBoolean("registry.volatile.storage", false);
			
			if (!volatileStorage) { 
				getLock();

				if (!dbDir.exists()) {
					System.out.println("This appears to be the first time you are"
							+ " using SDM. Creating database ...");
				}
			}
			
			databaseManager = new DerbyDatabaseManager("registry", props, volatileStorage);
			
			initializeTables();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-5);
		} finally {
			if (databaseManager != null) {
				databaseManager.close();
			}
		}
	}
}
