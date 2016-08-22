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
package net.orpiske.ssps.common.dependencies.cache;

import net.orpiske.ssps.common.db.AbstractDao;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import net.orpiske.ssps.common.repository.PackageInfo;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryLoader;
import net.orpiske.ssps.common.dependencies.Dependency;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This DAO is used to interface with the dependency cache table
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class DependencyCacheDao extends AbstractDao {
	
	private Map<String, String> queries;
	
	/**
	 * Constructor
	 * @param databaseManager The database manager
	 * @throws net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException if it fails to read the queries for this 
	 * DAO.
	 */
	public DependencyCacheDao(final DatabaseManager databaseManager) throws DatabaseInitializationException {
		super(databaseManager);
		
		QueryLoader queryLoader = QueryLoader.instance();
		
		try { 
			queries = queryLoader.load("/net/orpiske/ssps/common/dependencies/cache/DependencyCache.properties");
		}
		catch (IOException e) {
			throw new DatabaseInitializationException("Unable to load queries: " 
					+ e.getMessage(), e);
		}
	}
	
	
	/**
	 * Creates the tables 
	 * @throws java.sql.SQLException if unable to create the table
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
	
	
	private int insert(PackageInfo dependant, String dependencyQualifiedName, 
					   String dependencyVersionRange) 
			throws SQLException 
	{
		String query = queries.get("insert");

		return runUpdate(query, dependant.getGroupId(), dependant.getName(), 
				dependant.getVersion().toString(), dependant.getRepository(),
				dependencyQualifiedName, dependencyVersionRange);
	}


	/**
	 * Inserts a record into the inventory
	 * @param dependant A DTO containing the data to insert into the DB
	 * @return The number of affected records
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int insert(PackageInfo dependant) throws SQLException {
		int count = 0;
		HashMap<String, String> map = dependant.getDependencies();

		if (map == null) {
			return 0;
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String dependencyQualifiedName = entry.getKey();
			String dependencyVersionRange = entry.getValue();
		
			count += insert(dependant, dependencyQualifiedName, dependencyVersionRange);
		}
		
		return count;
	}

	
	/**
	 * Inserts a record into the inventory
	 * @param dto A DTO containing the data to insert into the DB
	 * @return The number of affected records
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int insert(final Dependency dto) throws SQLException {
		int count = 0;
		
		for (Dependency dependency : dto.getDependencies()) {
			count += insert(dependency);
		}
		
		return insert(dto.getPackageInfo());
	}

	/**
	 * Gets a package by the primary keys
	 * @param name The package name
	 * @param version The package version
	 * @return A DTO with the package information
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public List<DependencyCacheDto> get(final String name, final String version)
			throws SQLException
	{
		String query = queries.get("queryByNameAndVersion");

		return runQueryMany(query, new MultiRsHandler(), 
				name, version);
		
		
	}


	/**
	 * Gets a package by the primary keys
	 * @param name The package name
	 * @param version The package version
	 * @return A DTO with the package information
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public List<DependencyCacheDto> get(final String groupId, 
		final String name, final String version) throws SQLException
	{
		String query = queries.get("queryByKeys");

		return runQueryMany(query, new MultiRsHandler(), groupId,
				name, version);


	}
	



	/**
	 * Deletes a package
	 * @return The number of affected records (should always be 1)
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int deleteAll() throws SQLException {
		String query = queries.get("deleteAll");

		return runUpdate(query);
	}
	
	
	/**
	 * Deletes a package
	 * @param dto An input DTO to delete
	 * @return The number of affected records (should always be 1)
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int delete(final PackageInfo dto) throws SQLException {
		String query = queries.get("deleteByKeys");
		
		return runUpdate(query, dto.getGroupId(), dto.getName(), 
				dto.getVersion().toString());
	}


	/**
	 * Deletes all packages in the cache from a given repository
	 * @param repository The name of the repository
	 * @return The number of affected records (should always be 1)
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int deleteByRepository(final String repository) throws SQLException {
		String query = queries.get("deleteByRepository");

		return runUpdate(query, repository);
	}
	
	/**
	 * Gets the number of packages in the DB
	 * @return the number of packages in the DB
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int getCount() throws SQLException {
		String query = queries.get("count");

		return runQueryCount(query);
	}
}
