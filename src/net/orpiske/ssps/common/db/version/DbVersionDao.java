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
package net.orpiske.ssps.common.db.version;

import net.orpiske.ssps.common.db.AbstractDao;
import net.orpiske.ssps.common.db.DatabaseManager;
import net.orpiske.ssps.common.db.SimpleRsHandler;
import net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * This DAO is used to interface with the database version
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public class DbVersionDao extends AbstractDao {
	
	private Map<String, String> queries;
	
	/**
	 * Constructor
	 * @param databaseManager The database manager
	 * @throws net.orpiske.ssps.common.db.exceptions.DatabaseInitializationException if it fails to read the queries for this 
	 * DAO.
	 */
	public DbVersionDao(final DatabaseManager databaseManager) throws DatabaseInitializationException {
		super(databaseManager);
		
		QueryLoader queryLoader = QueryLoader.instance();
		
		try { 
			queries = queryLoader.load("/net/orpiske/ssps/common/db/version/DbVersion.properties");
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

	
	/**
	 * Inserts a record into the inventory
	 * @param dto A DTO containing the data to insert into the DB
	 * @return The number of affected records
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int insert(final DbVersionDto dto) throws SQLException {
		String query = queries.get("insert");
		
		return runUpdate(query, dto.getVersion(), dto.getCreationDate(), 
					dto.getConversionDate());
	}


	/**
	 * Updates a record into the inventory
	 * @param dto A DTO containing the new data to update into the DB
	 * @param oldVersion The old version of the database   
	 * @return The number of affected records
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public int update(final DbVersionDto dto, final String oldVersion) throws SQLException {
		String query = queries.get("update");

		return runUpdate(query, dto.getVersion(), dto.getConversionDate(), oldVersion);
	}
	
	
	/**
	 * Gets the DB version
	 * @return The DB version
	 * @throws java.sql.SQLException If unable to perform the query
	 */
	public DbVersionDto get() throws SQLException {
		String query = queries.get("queryAll");		
		
		return runQuery(query, new SimpleRsHandler<DbVersionDto>(new DbVersionDto()));
	}
}
