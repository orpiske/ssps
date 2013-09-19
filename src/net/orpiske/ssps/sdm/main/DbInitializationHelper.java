package net.orpiske.ssps.sdm.main;

import net.orpiske.ssps.common.db.derby.DerbyDatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.db.version.DbVersionDao;
import net.orpiske.ssps.common.db.version.DbVersionDto;
import net.orpiske.ssps.common.dependencies.cache.DependencyCacheDao;
import net.orpiske.ssps.common.registry.SoftwareInventoryDao;
import net.orpiske.ssps.common.repository.search.cache.PackageCacheDao;
import net.orpiske.ssps.common.utils.Utils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: orpiske
 * Date: 9/18/13
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbInitializationHelper {
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
				System.out.println("Software inventory table created successfully");
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
				System.out.println("Creating database version record");
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
				System.out.println("Database version table created successfully");
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
				System.out.println("Package cache table created successfully");
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
				System.out.println("Package cache table created successfully");
			}
			else {
				throw e;
			}
		}
	}
	
	
	private void initializeTables() throws SQLException, DatabaseInitializationException{
		initializeSoftwareInventory();
		initializeDbVersion();
		initializePackageCache();
		initializeDependencyCache();
		
	}
	
	public void initDatabase() {
		Properties props = System.getProperties();
		props.setProperty("derby.system.home", Utils.getSdmDirectoryPath());
		

		try {
			File dbDir = new File(Utils.getSdmDirectoryPath() + File.separator
					+ "registry");
			
			
			if (!dbDir.exists()) {
				System.out.println("This appears to be the first time you are"
						+ " using SDM. Creating database ...");
			}
			
			databaseManager = new DerbyDatabaseManager("registry", props);
			if (!dbDir.exists()) {
				System.out.println("Database created successfully");
			}

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
