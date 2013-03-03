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
package net.orpiske.ssps.common.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;




/**
 * Abstract DAO
 * 
 * @author Otavio R. Piske <angusyoung@gmail.com>
 */
public abstract class AbstractDao {
	
	private DatabaseManager databaseManager;

	/**
	 * Default constructor
	 */
	public AbstractDao() {}
	
	
	/**
	 * Constructor
	 * @param databaseManager a previously setup database manager object
	 */
	public AbstractDao(final DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}


	/**
	 * Gets the database manager
	 * @return the database manager instance
	 */
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}



	/**
	 * Runs a simple (SELECT) query
	 * @param query The query to run
	 * @param rs The result set handler to use
	 * @param args The arguments to the query
	 * @return A previously specified (generic) DTO type
	 * @throws SQLException
	 */
	protected <T> T runQuery(String query, ResultSetHandler<T> rs, Object... args) throws SQLException {
		Connection conn = databaseManager.getConnection();
		
		QueryRunner run = new QueryRunner();
		
		T result = run.query(conn, query, rs, args);
		return result;
	}
	
	

	/**
	 * Runs an UPDATE/INSERT/DELETE statement
	 * @param query The query (statement) to run
	 * @param args The arguments to the query
	 * @return The number of affected rows
	 * @throws SQLException If unable to perform the operation
	 */
	protected int runUpdate(String query, Object... args) throws SQLException {
		Connection conn = databaseManager.getConnection();
		
		QueryRunner run = new QueryRunner();
		
		return run.update(conn, query, args);
	}


}
