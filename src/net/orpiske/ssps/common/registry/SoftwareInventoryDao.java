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
package net.orpiske.ssps.common.registry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import net.orpiske.ssps.common.db.AbstractDao;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.version.Version;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryLoader;

/**
 * This DAO is used to interface with the software inventory table
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class SoftwareInventoryDao extends AbstractDao {
	
	private Map<String, String> queries;
	
	/**
	 * Constructor
	 * @param databaseManager The database manager
	 * @throws DatabaseInitializationException if it fails to read the queries for this 
	 * DAO.
	 */
	public SoftwareInventoryDao(final DatabaseManager databaseManager) throws DatabaseInitializationException {
		super(databaseManager);
		
		QueryLoader queryLoader = QueryLoader.instance();
		
		try { 
			queries = queryLoader.load("/net/orpiske/ssps/common/registry/SoftwareInventory.properties");
		}
		catch (IOException e) {
			throw new DatabaseInitializationException("Unable to load queries: " 
					+ e.getMessage(), e);
		}
	}
	
	
	/**
	 * Creates the tables 
	 * @throws SQLException if unable to create the table
	 */
	public void createTable() throws SQLException {
		DatabaseManager databaseManager = getDatabaseManager();
		Connection conn = databaseManager.getConnection();
		
		String query = queries.get("createTable");
		
		
		Statement s = null;
		
		try { 
			s = conn.createStatement();
			s.execute(query);
		}
		finally { 
			DbUtils.close(s);
		}
	}

	
	/**
	 * Inserts a record into the inventory
	 * @param dto A DTO containing the data to insert into the DB
	 * @return The number of affected records
	 * @throws SQLException If unable to perform the query
	 */
	public int insert(final SoftwareInventoryDto dto) throws SQLException {
		String query = queries.get("insert");
		
		return runUpdate(query, dto.getGroupId(), dto.getName(), 
					dto.getVersion().toString(), dto.getType(), 
					dto.getInstallDir(), dto.getInstallDate());
	}
	
	
	/**
	 * Gets a list of packages with a given name 
	 * @param name The name to look for
	 * @return A list of all packages that have the given name
	 * @throws SQLException If unable to perform the query
	 */
	public List<SoftwareInventoryDto> getByName(final String name) throws SQLException {
		String query = queries.get("queryByName");		
		
		return runQueryMany(query, new MultiRsHandler(), name);
	}
	
	
	/**
	 * Gets all the records in the database
	 * @return A list of all packages
	 * @throws SQLException If unable to perform the query
	 */
	public List<SoftwareInventoryDto> getAll() throws SQLException {
		String query = queries.get("queryAll");
		
		return runQueryMany(query, new MultiRsHandler());
	}
	
	
	/**
	 * Gets a package by the primary keys
	 * @param groupId The group ID
	 * @param name The package name
	 * @param version The package version
	 * @param type The package type
	 * @return A DTO with the package information
	 * @throws SQLException If unable to perform the query
	 */
	public SoftwareInventoryDto getByKeys(final String groupId, final String name, 
			final String version, final String type) throws SQLException		
	{
		String query = queries.get("queryByKeys");
		
		SoftwareInventoryRsHandler handler = new SoftwareInventoryRsHandler();
		
		return runQuery(query, handler, groupId, name, version, type);
	}
	
	
	/**
	 * Deletes a package
	 * @param dto An input DTO to delete
	 * @return The number of affected records (should always be 1)
	 * @throws SQLException If unable to perform the query
	 */
	public int delete(final SoftwareInventoryDto dto) throws SQLException {
		String query = queries.get("deleteByKeys");
		
		return runUpdate(query, dto.getGroupId(), dto.getName(), 
				dto.getVersion().toString(), 
				dto.getType());
	}
	
	
	/**
	 * Updates the package version
	 * @param newVersion The new version
	 * @param dto A DTO with the old version
	 * @return A DTO with the new version
	 * @throws SQLException If unable to perform the query
	 */
	public SoftwareInventoryDto updateVersion(final Version newVersion, 
			final SoftwareInventoryDto dto) throws SQLException 
	{
		String query = queries.get("updateVersion");
		
		int ret = runUpdate(query, newVersion.toString(), dto.getGroupId(), 
				dto.getName(), dto.getVersion().toString(), dto.getType());
		
		if (ret == 1) {
			dto.setVersion(newVersion);
		}
		
		return dto;
	}
	
	
	/**
	 * Updates the package information for reinstalled packages
	 * @param dto The DTO with the new information
	 * @return The DTO with the new information or null if no records were changed
	 * @throws SQLException If unable to perform the query
	 */
	public SoftwareInventoryDto updateReinstalled(final SoftwareInventoryDto dto) 
			throws SQLException 
	{
		String query = queries.get("updateReinstalled");
		
		int ret = runUpdate(query, dto.getInstallDate(), dto.getInstallDir(), 
				dto.getGroupId(), dto.getName(), dto.getVersion().toString(), 
				dto.getType());
		
		if (ret == 1) {
			return dto;
		}
		
		return null;
	}

}
